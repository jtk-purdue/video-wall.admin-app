package edu.purdue.cs.vwadmin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
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
	
	final boolean[] displayState = new boolean[16]; //true is on and false is off


	private static final String TAG = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remoteswitch);

		internetcheck = checkInternetConnection();

		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);  
		gridview.setAdapter(new ButtonAdapter(this)); 
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(RemoteSwitch.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });

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
	     btn.setBackgroundColor(Color.argb(100, 100, 100, 255));
	     //btn.setBackgroundResource(R.drawable.button);  
	     btn.setId(position);  
	     btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            //Toast.makeText(v.getContext(), "" + ((Button)v).getText(), Toast.LENGTH_SHORT).show();
				if(displayState[position])
				{
	            //Toast.makeText(v.getContext(), "" + ((Button)v).getText(), Toast.LENGTH_SHORT).show();
	            v.setBackgroundColor(Color.argb(100, 124, 252, 0));
	            
	            Toast.makeText(RemoteSwitch.this, "Now Turning Display Off...", Toast.LENGTH_SHORT).show();
	            new Thread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub

					}
	            }).start();
	            
				}
				else
				{
		            Toast.makeText(RemoteSwitch.this, "Now Turning Display On...", Toast.LENGTH_SHORT).show();
		            v.setBackgroundColor(Color.argb(100, 100, 100, 255));
				}
			}
				
	    	 
	     });
	     
	     return btn;  
	    }  
	   } 
	
}

 
