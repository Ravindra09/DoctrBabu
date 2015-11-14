package com.example.json_demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity implements OnItemClickListener {

	
	private ProgressDialog pDialog;
	ListView lv; 
	String contact_no,email_address,WebLink,specilaity,category,Medicine,Hospital_name,state_name;
	
    // URL to get contacts JSON
    private static String url = "https://data.gov.in/api/datastore/resource.json?resource_id=b4d77a09-9cdc-4a5b-b900-8fddb78f3cbe&api-key=f215b18af769770f445347e8ce36de4e";
 
    private static final String LOG_TAG = "Google Places Autocomplete";
 
        private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
 
        private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
  
        private static final String OUT_JSON = "/json";
 
        private static final String API_KEY = "AIzaSyCuGcQzbiVWZKWzIDTRUNdB47s_puxzU3I";

    
    // JSON Node names
  static final String TAG_RECORDS = "records";
    static final String TAG_ID = "id";
    static final String TAG_STATE = "State";
     static final String TAG_CITY = "City";
    static final String TAG_HOSPITAL = "Hospital / Private";
    
     static final String TAG_MEDICINE_TYPE="Systems of Medicine";
     static final String TAG_CONTACT="Contact Details";
     static final String TAG_EMAIL=  "Email address";
     static final String TAG_WEBSITE= "Website link";
     static final String TAG_SPECIALIZATION= "Specializations";
     static final String TAG_SERVICES="Services";
     static final String TAG_CATEGORY="Category";
    
    
    // contacts JSONArray
    JSONArray records = null;
 
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> recordList;
	
	AutoCompleteTextView state;
	String parts[];
	//String state_str[]={"Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chhattisgarh","Goa","Gujarat","Haryana","Himachal Pradesh","Jammu and Kashmir","Jharkhand","Karnataka","Kerala","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Orissa","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttar Pradesh","Uttrakahand","West Bengal","Delhi","Andaman & Nicobar","Chandigarh","Dadra & Nagar Haveli","Daman & Diu","Lakshadweep","Puducherry"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		
		
		state=(AutoCompleteTextView)findViewById(R.id.textView1);
		
		state.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.place_list));
	
		        state.setOnItemClickListener(this);

		
		
	
	
		 recordList = new ArrayList<HashMap<String, String>>();
		 
	      lv = getListView();
	
	     //   new GetRecords().execute();
	}

	public void onItemClick(AdapterView adapterView, View view, int position, long id) {
		
		        String str = (String) adapterView.getItemAtPosition(position);
		//String loc="";
	//	parts[0]="";
		// trying to get Place name from String of autoComplete textbox.
		        if(str.contains(","))
		{
	 parts = str.split("\\,");
	    System.out.print(parts[0]);
		        
		    //   Toast.makeText(this, parts[0], Toast.LENGTH_SHORT).show();
		    //   Toast.makeText(this, parts[1], Toast.LENGTH_SHORT).show();
		        url=url+"&filters[city]"+"="+parts[0];
		        
		     //   Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
			    
		        
		         new GetRecords().execute();
		         
		
		}
		    }

	
	
	public static ArrayList autocomplete(String input) {
		
		        ArrayList resultList = null;
		 
		        HttpURLConnection conn = null;
		
		        StringBuilder jsonResults = new StringBuilder();
	
		        try {
		
		            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
		
		            sb.append("?key=" + API_KEY);
	
		            sb.append("&components=country:in");
	
		            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
		
		 
		
		            URL url = new URL(sb.toString());
		         conn = (HttpURLConnection) url.openConnection();
	
		            InputStreamReader in = new InputStreamReader(conn.getInputStream());
	
		            // Load the results into a StringBuilder
		            int read;
			            char[] buff = new char[1024];
		       while ((read = in.read(buff)) != -1) {
		
		                jsonResults.append(buff, 0, read);
		
		            }
	
		        } catch (MalformedURLException e) {
		
		            Log.e(LOG_TAG, "Error processing Places API URL", e);
		
		            return resultList;
		
		        } catch (IOException e) {
		
		            Log.e(LOG_TAG, "Error connecting to Places API", e);
		
		            return resultList;
		
		        } finally {
		
		            if (conn != null) {
		
		                conn.disconnect();
		
		            }
	       }
		 
		        try {           
		    // Create a JSON object hierarchy from the results
		      JSONObject jsonObj = new JSONObject(jsonResults.toString());
		        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
	       // Extract the Place descriptions from the results
		         resultList = new ArrayList(predsJsonArray.length());
		        for (int i = 0; i < predsJsonArray.length(); i++) {
		            System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
		            System.out.println("============================================================");
		
		                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	        }
		    } catch (JSONException e) {
		        Log.e(LOG_TAG, "Cannot process JSON results", e);
		
		        }
		
		        return resultList;
		
		    }

	
	
	
	class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
		     private ArrayList resultList;
		    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
		        super(context, textViewResourceId);
		     }
		
		        @Override
		    public int getCount() {
		        return resultList.size();
		      }
		
		        @Override
		
		        public String getItem(int index) {
		        return (String) resultList.get(index);
		     }
		    @Override
	    public Filter getFilter() {
	
		            Filter filter = new Filter() {
		                @Override
		                protected FilterResults performFiltering(CharSequence constraint) {
		                 FilterResults filterResults = new FilterResults();
	                if (constraint != null) {
		                    // Retrieve the autocomplete results.
		                    resultList = autocomplete(constraint.toString());
	                  // Assign the data to the FilterResults
		                        filterResults.values = resultList;
		                        filterResults.count = resultList.size();
		                    }
		                    return filterResults;
		                }
		                protected void publishResults(CharSequence constraint, FilterResults results) {
		                    if (results != null && results.count > 0) {
		                        notifyDataSetChanged();
		
		                    } else {
		                        notifyDataSetInvalidated();
		                    }
		                }
		            };
		
		            return filter;
		        }
		
		    }

	
	
	
	private class GetRecords extends AsyncTask<Void, Void, Void> {
		 
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
		
			 // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
 
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
 
            Log.d("Response: ", "> " + jsonStr);
			
            if(jsonStr!=null)
            {
            	try
            	{
            	JSONObject jsonObj=new JSONObject(jsonStr);
            	
            	records=jsonObj.getJSONArray(TAG_RECORDS);
            	
            for(int i=0;i<records.length();i++)
            {
            	
            	JSONObject c= records.getJSONObject(i);
            	
            	String id = c.getString(TAG_ID);
                String hospital_name = c.getString(TAG_HOSPITAL); 
                String state = c.getString(TAG_STATE);
                String city = c.getString(TAG_CITY);
                String medicine_type=c.getString(TAG_MEDICINE_TYPE);
                String contact=c.getString(TAG_CONTACT);
                String email=c.getString(TAG_EMAIL);
                String website=c.getString(TAG_WEBSITE);
                String specialization=c.getString(TAG_SPECIALIZATION);
                String services=c.getString(TAG_SERVICES);
                String category=c.getString(TAG_CATEGORY);
                
             
                
             // tmp hashmap for single contact
                HashMap<String, String> record = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                record.put(TAG_ID, id);
            record.put(TAG_HOSPITAL, hospital_name);
                record.put(TAG_STATE, state);
              record.put(TAG_CITY, city);
              record.put(TAG_MEDICINE_TYPE,medicine_type);
              record.put(TAG_CONTACT, contact);
              record.put(TAG_EMAIL, email);
              record.put(TAG_WEBSITE, website);
              record.put(TAG_SPECIALIZATION, specialization);
              record.put(TAG_SERVICES, services);
              record.put(TAG_CATEGORY, category);
              
              
                // adding contact to contact list
                recordList.add(record);
            	
            }
            	}
            	catch(JSONException e)
            	{
            		
            		e.printStackTrace();
            		
            	}
            	
            	
            	
            }
            else
            {
            	Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            
            return null;
		
		
		}
	
		@Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
         

            
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, recordList,
                    R.layout.list_item, new String[] { TAG_HOSPITAL, TAG_STATE,
                            TAG_CITY }, new int[] { R.id.name,R.id.mobile });
 
            
          lv.setAdapter(adapter);
            lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView adapter, View view,
                    int position, long id) {
                
             	
                	 // String str = (String) adapter.getItemAtPosition(position);
                	 // Toast.makeText(getApplicationContext(), "clicked"+position, Toast.LENGTH_SHORT).show();
                	
                	specilaity=recordList.get(position).get(TAG_SPECIALIZATION).toString();
                	contact_no=recordList.get(position).get(TAG_CONTACT).toString();
                	email_address=recordList.get(position).get(TAG_EMAIL).toString();
                	WebLink=recordList.get(position).get(TAG_WEBSITE).toString();
                	category=recordList.get(position).get(TAG_CATEGORY).toString();
                	Medicine=recordList.get(position).get(TAG_MEDICINE_TYPE).toString();
                	Hospital_name=recordList.get(position).get(TAG_HOSPITAL).toString();
                	state_name=recordList.get(position).get(TAG_STATE).toString();
                	//Toast.makeText(getApplicationContext(), Ravindra+"", Toast.LENGTH_LONG).show();
                	  //HashMap myMap = recordList.get(position);
                      Intent nIntent = new Intent(getApplicationContext(),Hospital.class);
                      nIntent.putExtra("Spec", specilaity);
                      nIntent.putExtra("Contact", contact_no);
                      nIntent.putExtra("email", email_address);
                      nIntent.putExtra("link", WebLink);
                      nIntent.putExtra("cat", category);
                      nIntent.putExtra("medicine",Medicine);
                      nIntent.putExtra("Hospital",Hospital_name);
                      nIntent.putExtra("city", parts[0]);
                      nIntent.putExtra("State",state_name);
                      
                      
                      
                      
                      startActivity(nIntent);
                
                }}
                );
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

	}	
}
