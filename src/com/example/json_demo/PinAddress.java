package com.example.json_demo;



import java.io.IOException;
import java.util.List;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PinAddress extends FragmentActivity implements LocationListener,OnInfoWindowClickListener
{
	ProgressBar pb;
	GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    
    double lati,lngi;
    
    String address="";
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.pin_address);
		
	
		 pb=(ProgressBar)findViewById(R.id.progressBar1);
		 int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		 if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
	        	
	        	int requestCode = 10;
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,this, requestCode);
	            dialog.show();
	           dialog.dismiss();
		 }
		 
		 SupportMapFragment fm=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		 
			googleMap =fm.getMap();
			
			if(googleMap!=null)
			{
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setRotateGesturesEnabled(true);
			googleMap.getUiSettings().setTiltGesturesEnabled(true);
			
			LocationManager lcnm=(LocationManager)getSystemService(LOCATION_SERVICE);
			 Criteria criteria = new Criteria();
				
		        // Getting the name of the best provider
		        String provider = lcnm.getBestProvider(criteria, true);
		
		        // Getting Current Location
		        Location location = lcnm.getLastKnownLocation(provider);
		

		        if(location!=null){
		                onLocationChanged(location);
		        }
		        lcnm.requestLocationUpdates(provider, 1000, 0, this);
			
			}
			else
			{
				//Log.i("Ravin",googleMap+"");
				Toast.makeText(getBaseContext(), "Googlemap is null", Toast.LENGTH_LONG).show();
			}
			
		 address=getIntent().getExtras().getString("Contact_Address").toString();
		 
	//	 address="IIIT Hyderabad,India";
		Toast.makeText(getApplicationContext(), address+"", Toast.LENGTH_SHORT).show();
		 
		 
		 if(address!=null && !address.equals("")){
            
			 Geocoder coder = new Geocoder(getApplicationContext());
			    List<Address> Straddress;
			    
			    try {
			        Straddress = coder.getFromLocationName(address, 3);
			        if (address == null) {
			           Toast.makeText(getApplicationContext(), "Not Available....", Toast.LENGTH_LONG).show();
			           
			        }
			        final Address location = Straddress.get(0);
			        /*location.getLatitude();
			        location.getLongitude();
			        
			        
*/
			        
			        latLng = new LatLng(location.getLatitude(),location.getLongitude());
			        
			        Toast.makeText(getApplicationContext(), location.getLatitude()+"", Toast.LENGTH_SHORT).show();
			        
	                String addressText = String.format("%s, %s",location.getMaxAddressLineIndex() > 0 ? location.getAddressLine(0) : "",location.getCountryName());
	 if(latLng!=null)
	 {
		 pb.setVisibility(View.GONE);
		 
	 
	                
	                markerOptions = new MarkerOptions();
	                markerOptions.position(latLng);
	                markerOptions.title(addressText);
	 
	                googleMap.addMarker(markerOptions);
	              
	                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	                
	                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
	                
	                
	                googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
						
						@Override
						public void onInfoWindowClick(Marker arg0) {
							// TODO Auto-generated method stub
							
			
				//		Toast.makeText(getApplicationContext(), a+"", Toast.LENGTH_LONG).show();
		try
		{
				Intent my=new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?saddr=" +lati+","+lngi+"&daddr=" +location.getLatitude()+","+ location.getLongitude()));
					startActivity(my);				
						}
		catch(Exception e)
		{
			e.printStackTrace();
		}
						}
						
						});
					
	                
	                
	 }
	 else
	 {
		 
		 Toast.makeText(getApplicationContext(), "No such Location found on Map Sorry!!!", Toast.LENGTH_LONG).show();
		 this.finish();
	 }// Locate the first location
	               /* if(i==0)
	                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));*/
		 
		 }catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		
		 } 
		 
		 
}

	@Override
	public void onLocationChanged(Location lacation) {
		// TODO Auto-generated method stub
	
		lati=lacation.getLatitude();
		lngi=lacation.getLongitude();
		LatLng ltlng=new LatLng(lati, lngi);
		//googleMap.moveCamera(CameraUpdateFactory.newLatLng(ltlng));
		
	
       // googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
	
		
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	 // An AsyncTask class for accessing the GeoCoding Web Service
   /* private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
 
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
 
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
 
        @Override
        protected void onPostExecute(List<Address> addresses) {
 
            if(addresses==null){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
 
            // Clears all the existing markers on the map
            googleMap.clear();
 
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
 
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
 
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
 
                googleMap.addMarker(markerOptions);
 
                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }
	*/
}
