package com.example.json_demo;

import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class Hospital extends Activity
{

	
		//double lati,lngi;
	String url_parts[],state_parts[];
	 ProgressBar pb1;
		ImageView Picture,category;
		ImageButton call,link,email,pin_address;
		TextView Services,Serv_dtails,Speciality,Spec_details,address,address_desc;
		 //SupportMapFragment fm;	
		 String spec,call_no,Website,email_address;
		String c_num,city_name,hospital_name,state_name,photo_reference;
		 String Call_parts[];
		 JSONArray results = null;
		 Bitmap	bmp;
		 URL url;
		 private static final String Photo_api = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
	
	String photo_url=""; 
	        private static final String API_KEY = "AIzaSyCA92qF_LxIbKCCbuGo-iXsFUkQ5Ynvzto";
	        HashMap<String, String> photo_ref = new HashMap<String, String>();
	        
	//TextView tv;
	
@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.temp);
		
	        
				
				pb1=(ProgressBar)findViewById(R.id.progressBar1);
				
			pin_address=(ImageButton)findViewById(R.id.imageButton4);	
			
			address=(TextView)findViewById(R.id.textView1);
			address_desc=(TextView)findViewById(R.id.textView2);
	       Picture=(ImageView)findViewById(R.id.imageView1);
	      call=(ImageButton)findViewById(R.id.imageButton1);
	      link=(ImageButton)findViewById(R.id.imageButton2);
	      email=(ImageButton)findViewById(R.id.imageButton3);
	      category=(ImageView)findViewById(R.id.imageView2);
	     /* Services=(TextView)findViewById(R.id.textView1);
	      Serv_dtails=(TextView)findViewById(R.id.textView2);*/
	      Speciality=(TextView)findViewById(R.id.textView3);
	      Spec_details=(TextView)findViewById(R.id.textView4);
	      
		
	      hospital_name=getIntent().getExtras().getString("Hospital");
	      
	      
	      spec=getIntent().getExtras().getString("Spec");
	       
	        // Getting Call No.
	        call_no=getIntent().getExtras().getString("Contact");
	      
	        address_desc.setText(call_no);
	      
	//  use map class to pin point address on Map.
	      
	     pin_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				Intent pinIntent=new Intent(getApplicationContext(), PinAddress.class);
				pinIntent.putExtra("Contact_Address", hospital_name+", "+city_name+", "+state_name+", India");
				
				startActivity(pinIntent);
				
			}
		});
	     
	      
	      
	      
	      
	     
	     
	      
	        // Code to get Phone and Emergency Contact num.
	        
	        if(call_no.contains(","))
	        {
	        	
	        	Call_parts=call_no.split("\\,");
	        
	        	for(int i=0;i<Call_parts.length;i++ )
	        	{
	        		if(Call_parts[i].contains("Phone"))
	        		{
	        			c_num=getNo(Call_parts[i]);
	        			//Toast.makeText(getApplicationContext(), c_num+"", Toast.LENGTH_LONG).show();
	        		}
	        	}
	        	
	        }
	        
	    // Getting Website
	        
	        Website=getIntent().getExtras().getString("link");
	     link.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				if(Website.equalsIgnoreCase("NA"))
				Toast.makeText(getApplicationContext(), "No Website Link !!!Sorry ", Toast.LENGTH_SHORT).show();
				else if (!Website.startsWith("http://") && !Website.startsWith("https://"))
				{
					Website = "http://" + Website;
				
					
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(Website));
					startActivity(i);
				}
				else
				{
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(Website));
					startActivity(i);
				
					
				}
			}
		});   
	        
	     
	 // Getting Email_address
	     
	     email_address=getIntent().getExtras().getString("email");
	     email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(email_address.equalsIgnoreCase("NA"))
					Toast.makeText(getApplicationContext(), "No Email-Id !!!Sorry ", Toast.LENGTH_SHORT).show();
					else
					{
						
						String[] mailto = {email_address};
						 
						Intent sendIntent = new Intent(Intent.ACTION_SEND);
						sendIntent.putExtra(Intent.EXTRA_EMAIL, mailto); 
						sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject");
						sendIntent.putExtra(Intent.EXTRA_TEXT,"");
						sendIntent.setType("message/rfc822");
						startActivity(Intent.createChooser(sendIntent, "MySendMail"));
					}
				
				
			}
		});
	        
Spec_details.setMovementMethod(new ScrollingMovementMethod());
Spec_details.setText(spec);  
	        		
	    


call.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
		String url = "tel:"+c_num;
	     Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
	startActivity(intent);

		
		
	}
});


//  Getting Photo using google api   passing Hospital name and City name 
city_name=getIntent().getExtras().getString("city");

String hos="";
String st="";
if(hospital_name.contains(" "))
		{
	 url_parts = hospital_name.split("\\ ");
	 for(int x=0;x<url_parts.length;x++)
	 {
		 hos=hos+url_parts[x]+"%20";
	 }
	 
	 // Toast.makeText(getApplicationContext(), url_parts[0]+"RR"+url_parts[1], Toast.LENGTH_SHORT).show();
		}

state_name=getIntent().getExtras().getString("State");

if(state_name.contains(" "))
{
 	state_parts=state_name.split("\\ ");
 	st=st+state_parts[0]+"%20"+state_parts[1];
 	
}
else
{
	st=state_name;
}
photo_url=Photo_api+hos+city_name+"%20"+st+"&key="+API_KEY;





//photo_url="https://maps.googleapis.com/maps/api/place/textsearch/json?query=SMS%20Medical%20College%20Jaipur%20Rajasthan&key=AIzaSyCA92qF_LxIbKCCbuGo-iXsFUkQ5Ynvzto";

new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
  
            	new GetPhoto().execute();

            }
        });
    }
}).start();





			 }
	        
	        
	   /*	Intent intent = getIntent();
	   	ArrayList<HashMap<String,String>> data = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("myItems"); 
	        
	   	for(Map<String, String> newItem:data)
	   	{
	   	newUrl = newItem.get(MainActivity.TAG_WEBSITE);
	   
	   	}
	   	Serv_dtails.setText(newUrl);
	   	
	   	// String Call=data.get(MainActivity.TAG_CONTACT);
*/
private class GetPhoto extends AsyncTask<Void, Void, Void> {
	 
    protected void onPreExecute() {
        super.onPreExecute();
      //  Showing progress dialog
       
    
      
        
      /*  pDialog = new ProgressDialog(Hospital.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();*/

    }

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
	
		 // Creating service handler class instance
       ServiceHandler sh1 = new ServiceHandler();

        // Making a request to url and getting response
        String jsonStr = sh1.makeServiceCall(photo_url, ServiceHandler.GET);

        Log.d("Response: ", "> " + jsonStr);
		
        if(jsonStr!=null)
        {
        	try
        	{
        	JSONObject jsonObj=new JSONObject(jsonStr);
        	
        	results=jsonObj.getJSONArray("results");
        	
        	/*for(int i=0;i<results.length();i++)
        	{*/
        	JSONObject c= results.getJSONObject(0);
        JSONArray pic = c.getJSONArray("photos");
           JSONObject p_ref=pic.getJSONObject(0);
        	
        	photo_reference= p_ref.getString("photo_reference");
        	
        	try{
        		
        		 url = new URL("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photo_reference+"&key=AIzaSyCA92qF_LxIbKCCbuGo-iXsFUkQ5Ynvzto");
        		//In URL pass your link

        	bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        	
        	
        	//image_view.setImageBitmap(bm);
        		}catch(Exception e)
        		{
        			e.printStackTrace();
        		}
        	
        	
            
       // 	photo_ref.put("p_ref", photo_reference);
        	
        	
        	//}
        	
        	}catch(Exception e)
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
	
        pb1.setVisibility(View.GONE);
        
        // Dismiss the progress dialog
        /*if (pDialog.isShowing())
            pDialog.dismiss();*/
	Toast.makeText(getApplicationContext(),bmp+"", Toast.LENGTH_SHORT).show();
    
if(bmp!=null)
{
Picture.setImageBitmap(bmp);
}
else
{
Picture.setImageResource(R.drawable.hospital);
}
	
	}
	
	
	
}
		
public String getNo(String  input )
{
	//int sum=0;	   
	final StringBuilder sb = new StringBuilder(
		            input.length() /* also inspired by seh's comment */);
		    for(int i = 0; i < input.length(); i++){
		        final char c = input.charAt(i);
		        if(c > 47 && c < 58){
		            sb.append(c);
		        }
		    }
		   
return sb.toString();
}
	
	



	
}
