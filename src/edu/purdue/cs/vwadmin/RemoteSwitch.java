package edu.purdue.cs.vwadmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.CursorJoiner.Result;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class RemoteSwitch extends Activity  {


	//private ImageSwitcher imageSwitcher;
	private TextView ins;
	private boolean internetcheck;
	Button turnoff;
	Button turnon;
	
	static final int DISPLAY_STANDBY=0;
	static final int DISPLAY_ON=1;
	static final int DISPLAY_OFF=2;
	
	final int[] displayState = new int[16]; //true is on and false is off


	private static final String TAG = null;
	Activity act = this;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remoteswitch);

		internetcheck = checkInternetConnection();

		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);  
		final ButtonAdapter buttonadapter = new ButtonAdapter(this);
		gridview.setAdapter(buttonadapter); 
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(RemoteSwitch.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	    
	    turnoff = (Button)findViewById(R.id.turnalloff);
	    turnon = (Button)findViewById(R.id.turnallon);
	    
	    turnoff.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//turnoff all
				for(int i=0;i<displayState.length;i++){
					displayState[i]=DISPLAY_OFF;
				}
				buttonadapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    turnon.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//turn on all
				for(int i=0;i<displayState.length;i++){
					displayState[i]=DISPLAY_ON;
				}
				buttonadapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    final ProgressDialog dialog = ProgressDialog.show(RemoteSwitch.this, "", 
                "Loading. Please wait...", true);
	    dialog.show();

	    
	    new AsyncTask(){

			protected Object doInBackground(Object... arg0) {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(int i=0;i<displayState.length;i++){
					displayState[i]=(int) Math.rint(Math.random()+1);;
				}
								
				return arg0;
			}
			
		     protected void onPostExecute(Object result) {
		    	 	Toast.makeText(getBaseContext(), "Display status is now updated", Toast.LENGTH_SHORT).show();
					buttonadapter.notifyDataSetChanged();
					dialog.cancel();
		     }
//sdsd
	    	
	    }.execute(null);
	    

	}
	
	private boolean checkInternetConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			Log.d("Connection State", "Internet Connection Not Present");
			return false;
		}
	}
	
	
	
	public void onPause()
	{
		super.onPause();
	}
	
	public void onStop()
	{
		super.onStop();
	}
	
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	public void onResume()
	{
		super.onResume();
	}
	

	
	class ButtonAdapter extends BaseAdapter {  
	    private Context mContext; 
	    
	    View  prev = null;
	    int check=-1;
	     
	    // Gets the context so it can be used later  
	    public ButtonAdapter(Context c) {  
	    	for(int i=0;i<displayState.length;i++){
				displayState[i]=DISPLAY_STANDBY;
			}
	     mContext = c;  
	    }  
	     
	    // Total number of things contained within the adapter  
	    public int getCount() {  
	     return 16;  
	    }  
	     
	     // Require for structure, not really used in my code.  
	    public Object getItem(int position) {  
	     return null;  
	    }  
	     
	    // Require for structure, not really used in my code. Can  
	    // be used to get the id of an item in the adapter for  
	    // manual control.  
	    public long getItemId(int position) {  
	     return position;  
	    }  
	     
	    public View getView(final int position,  
	                              View convertView, ViewGroup parent) {  
	     Button btn;  
	     if (convertView == null) {  
	      // if it's not recycled, initialize some attributes  
	      btn = new Button(mContext);  
	      btn.setLayoutParams(new GridView.LayoutParams(67, 75));  
	      btn.setPadding(8, 8, 8, 8);  
	      }  
	     else {  
	      btn = (Button) convertView;  
	     }  
	     //exus  
	     btn.setText(position+1 + "");  
	     // filenames is an array of strings  
	     btn.setTextColor(Color.WHITE);
	     
	     switch(displayState[position]){
	     case 0:
	    	 btn.setBackgroundColor(Color.argb(100, 100, 100, 255));
	    	 break;
	     case 1:
	    	 btn.setBackgroundColor(Color.argb(100, 124, 252, 0));
	    	 break;
	     case 2:
	    	 btn.setBackgroundColor(Color.argb(100, 255, 100, 100));
	    	 break;
	     }
	     
	     //btn.setBackgroundResource(R.drawable.button);  
	     btn.setId(position);  
	     btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            //Toast.makeText(v.getContext(), "" + ((Button)v).getText(), Toast.LENGTH_SHORT).show();
			     switch(displayState[position]){
			     case 0:
			    	 //v.setBackgroundColor(Color.argb(100, 100, 100, 255));
			         Toast.makeText(v.getContext(), "Please wait till the screen is determined as on or off", Toast.LENGTH_SHORT).show();
			    	 break;
			     case DISPLAY_ON:
			    	 v.setBackgroundColor(Color.argb(100, 255, 100, 100));
			    	 displayState[position]=DISPLAY_OFF;
			    	 break;
			     case DISPLAY_OFF:
			    	 v.setBackgroundColor(Color.argb(100, 124, 252, 0));		    
			    	 displayState[position]=DISPLAY_ON;
			    	 break;
			     }
			     
			}
				
	    	 
	     });
	     
	     return btn;  
	    }  
	   } 
	
}

 
