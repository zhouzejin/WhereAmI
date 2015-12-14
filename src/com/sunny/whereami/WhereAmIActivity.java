package com.sunny.whereami;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class WhereAmIActivity extends MapActivity {
	
	private MapController mapController;
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_where_am_i);
		
		// 获得对MapView的引用
		MapView myMapView = (MapView) findViewById(R.id.myMapView);
		
		// 获得MapView的控制器
		mapController = myMapView.getController();
		
		// 配置地图显示选项
		myMapView.setSatellite(true);
		myMapView.setBuiltInZoomControls(true);
		
		// 放大
		mapController.setZoom(17);
		
		LocationManager locationManager;
		String svcName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(svcName);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);
		String provider = locationManager.getBestProvider(criteria, true);
		
//		String provider = LocationManager.GPS_PROVIDER;
		Location l = locationManager.getLastKnownLocation(provider);
		
		updateWithNewLocation(l);
		
		locationManager.requestLocationUpdates(provider, 2000, 10, 
				locationListener);
	}

	private void updateWithNewLocation(Location location) {
		TextView myLocationText;
		myLocationText = (TextView) findViewById(R.id.myLocationText);
		
		String latLongString = "No location found";
		String addressString = "No address found";
		
		if (location != null) {
			// 更新地图位置
			Double geoLat  = location.getLatitude() * 1E6;
			Double getLng = location.getLongitude() * 1E6;
			GeoPoint point = new GeoPoint(geoLat.intValue(), 
					getLng.intValue());
			mapController.animateTo(point);
			
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			latLongString = "Lat:" + lat + "\nLong:" + lng;

			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			Geocoder gc = new Geocoder(this, Locale.getDefault());

			try {
				List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
				StringBuilder sb = new StringBuilder();
				if (addresses.size() > 0) {
					Address address = addresses.get(0);

					for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
						sb.append(address.getAddressLine(i)).append("\n");

					sb.append(address.getLocality()).append("\n");
					sb.append(address.getPostalCode()).append("\n");
					sb.append(address.getCountryName());
				}
				addressString = sb.toString();
			} catch (IOException e) {

			}
		}
		
		myLocationText.setText("Your Current Position is:\n" + 
				latLongString + "\n\n" + addressString);
	}
	
	private final LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}
	};

}
