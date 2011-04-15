package edu.purdue.cs.vwadmin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*When this activity starts, it pulls a list of available shows that can be voted on. 
 * When a vote is cast it makes a connection with the server and send across what was voted for.
 */

public class Vote extends ListActivity {
	private LayoutInflater mInflater;
	private Vector<RowData> data;
	CustomAdapter adapter;
	RowData rd;
	Socket requestSocket;
 	PrintWriter out;
	BufferedReader in;
	String message;
	ArrayList<String> voteList;
	ArrayList<String> tempVoteList;
	ArrayList<String> votes;
	Runnable update;
	Animation anim = null;
	ProgressDialog myProgressDialog = null;
	String editTextPreference;
	int portnum;
	boolean internetcheck = false;
	Handler mHandler;

	static final String[] title = new String[] {
			"*New*Apple iPad Wi-Fi (16GB)",
			"7 Touch Tablet -2GB Google Android",
			"Apple iPad Wi-Fi (16GB) Rarely Used ",
			"Apple iPad Wi-Fi (16GB) AppleCase" };

	static final String[] detail = new String[] { "Number of Votes: ",
			"Number of Votes: ", "Number of Votes: ", "Number of Votes: ",
			"Number of Votes: ", "Number of Votes: ", "Number of Votes: ",
			"Number of Votes: ", "Number of Votes: ", "Number of Votes: ",
			"Number of Votes: ", "Number of Votes: ", "Number of Votes: ",
			"Number of Votes: ", "Number of Votes: ", "Number of Votes: ",
			"Number of Votes: ", "Number of Votes: ", "Number of Votes: ",
			"Number of Votes: ", "Number of Votes: ", "Number of Votes: ",
			"Number of Votes: ", "Number of Votes: ", "Number of Votes: ",
			"Number of Votes: ", "Number of Votes: ", "Number of Votes: " };

	/*
	private Integer[] imgid = { R.drawable.voteicon, R.drawable.voteicon,
			R.drawable.voteicon, R.drawable.voteicon, R.drawable.voteicon,
			R.drawable.voteicon, R.drawable.voteicon, R.drawable.voteicon,
			R.drawable.voteicon, R.drawable.voteicon, R.drawable.voteicon,
			R.drawable.voteicon, R.drawable.voteicon, R.drawable.voteicon,
			R.drawable.voteicon, R.drawable.voteicon, R.drawable.voteicon,
			R.drawable.voteicon, R.drawable.voteicon, R.drawable.voteicon,
			R.drawable.voteicon, R.drawable.voteicon, R.drawable.voteicon,
			R.drawable.voteicon, R.drawable.voteicon, R.drawable.voteicon,
			R.drawable.voteicon, R.drawable.voteicon, R.drawable.voteicon };
			*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		internetcheck = checkInternetConnection();// check if Internet
		// connection is present and
		// set to true if it is.
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		voteList = new ArrayList<String>();
		votes = new ArrayList<String>();

		portnum = 4242;

	

		setContentView(R.layout.vote);
		//anim = AnimationUtils.loadAnimation(this, R.anim.shake); // Sets the
																	// animation
																	// to shake
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		data = new Vector<RowData>();
		if (internetcheck) {
			try {
				
				voteList.clear();
				connect("GET", "");
				connect("GETCOUNT", "");
			} catch (ConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Contacts server and gets list of available shows

			for (int i = 0; i < voteList.size(); i++) {
				try {
					rd = new RowData(i, voteList.get(i), "Number of votes: "
							+ votes.get(i));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				data.add(rd);
			}
			adapter = new CustomAdapter(this, R.layout.list, R.id.title, data);
			setListAdapter(adapter);
			getListView().setOnItemLongClickListener( new AdapterView.OnItemLongClickListener
					(){
					                @Override
					                public boolean onItemLongClick(AdapterView<?> av, View v, int
					pos, long id) {
					                        onLongListItemClick(v,pos,id);
					                        return true;
					        }

					}); 
			

			getListView().setTextFilterEnabled(true);
		
		} else {
			Toast.makeText(getApplicationContext(),
					"No active internet connection.", Toast.LENGTH_SHORT)
					.show();

		}
		


		update = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					RowData r = null;
					if (internetcheck) {
						try {
							//voteList.clear();
							votes.clear();
							//connect("GET", "");
							connect("GETCOUNT", "");
						} catch (ConnectException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // Contacts server and gets list of available shows
					}

					for (int i = 0; i < voteList.size(); i++) {

						try {
							r = (RowData) data.elementAt(i);
							String temp = "Number of votes: " + votes.get(i);
							r.setDetail(temp);

						}

						catch (ParseException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					adapter.notifyDataSetChanged();
					mHandler.postDelayed(this, 3000);
				}

				catch (Exception e) {
					// do nothing
				}
			}
		};

		mHandler = new Handler();
		// mHandler.removeCallbacks(update);
		mHandler.postDelayed(update, 5000);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("On PAUSE", "TRUE");
		mHandler.removeCallbacks(update);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("On RESUME", "TRUE");
		mHandler = new Handler();
		mHandler.postDelayed(update, 5000);
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d("On STOP", "TRUE");
		mHandler.removeCallbacks(update);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("On DESTROY", "TRUE");
		mHandler.removeCallbacks(update);
	}
	
	protected void onLongListItemClick(View v,int pos,long id) 
	{
        registerForContextMenu(v);  
	     Log.i( "TAG", "onLongListItemClick id=" + id );

	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		TextView title = (TextView) v.findViewById(R.id.title);
		String vi = (String) ((TextView) title).getText();
		if (internetcheck) {
			try 
			{
		        //registerForContextMenu(v);  
				
			} 
		
			 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Contacts server and gets list of available shows

		}// end if

		else {
			Toast.makeText(getApplicationContext(),
					"No active internet connection.", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) 
    {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
    	menu.setHeaderTitle("Context Menu");  
    	menu.add(0, v.getId(), 0, "Remove Item");
    	menu.add(0, v.getId(), 0, "Remove Item");  
    	menu.add(0, v.getId(), 0, "Clear Votes");  
    }  
	
    @Override  
    public boolean onContextItemSelected(MenuItem item)
    {  
            if(item.getTitle()=="Action 1")
            {
            	//function1(item.getItemId());
            }
            
            else if(item.getTitle()=="Action 2")
            {
            	//function2(item.getItemId());
            } 
            else 
            {
            	return false;
            }  
            return true;  
    }  

	private class RowData {
		protected int mId;
		protected String mTitle;
		protected String mDetail;

		RowData(int id, String title, String detail) {
			mId = id;
			mTitle = title;
			mDetail = detail;
		}

		@Override
		public String toString() {
			return mId + " " + mTitle + " " + mDetail;
		}

		public void setDetail(String item) {
			mDetail = item;
		}

	}

	private class CustomAdapter extends ArrayAdapter<RowData> {
		public CustomAdapter(Context context, int resource,
				int textViewResourceId, List<RowData> objects) {
			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			TextView title = null;
			TextView detail = null;
			ImageView i11 = null;
			RowData rowData = getItem(position);
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.list, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			holder = (ViewHolder)convertView.getTag();
			title = holder.gettitle();
			title.setText(rowData.mTitle);
			detail = holder.getdetail();
			detail.setText(rowData.mDetail);
			i11 = holder.getImage();
			i11.setImageResource(R.drawable.icon);
			return convertView;
		}

		public class ViewHolder {
			private View mRow;
			private TextView title = null;
			private TextView detail = null;
			private ImageView i11 = null;

			public ViewHolder(View row) {
				mRow = row;
			}

			public TextView gettitle() {
				if (null == title) {
					title = (TextView) mRow.findViewById(R.id.title);
				}
				return title;
			}

			public TextView getdetail() {
				if (null == detail) {
					detail = (TextView) mRow.findViewById(R.id.detail);
				}
				return detail;
			}

			public ImageView getImage() {
				if (null == i11) {
					i11 = (ImageView) mRow.findViewById(R.id.img);
				}
				return i11;
			}

		}
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

	public void connect(String voteitem, String option)
			throws UnknownHostException, IOException, ConnectException,
			Exception {

		try {
			// 1. creating a socket to connect to the server
			requestSocket = new Socket("pc2.cs.purdue.edu", portnum);
			Log.d("Connection", "Connected to localhost in port " + portnum);

			if (requestSocket == null) {
				Log.d("REQUEST SOCKET DID NOT WORK", "NULL");
			}

			// 2. get Input and Output streams
			out = new PrintWriter(requestSocket.getOutputStream(), true);
			out.flush();
			in = new BufferedReader(new InputStreamReader(
					requestSocket.getInputStream()));

			// 3: Communicating with the server
			Log.d("DO", "Test1");
			do {
				message = in.readLine();
				System.out.println("server>" + message);
				message = "END";
				sendMessage(voteitem);

				if (voteitem.equals("VOTE"))
					sendMessage(option);

				sendMessage("END");
				do {
					message = in.readLine();
					Log.d("server>", message);

					if (voteitem.equals("GET") && !message.equals("END"))
						voteList.add(message);

					else if (voteitem.equals("GETCOUNT")
							&& !message.equals("END")) {
						votes.add(message);
					}

					Collections.sort(voteList);// Sorting Array List in Alpha
					// order

				} while (!message.equals("END"));
			} while (!message.equals("END"));

		}

		// Handle exception if server was not found
		catch (ConnectException e) {
			Log.d("SERVER", "Server NOT FOUND");
			Toast.makeText(getApplicationContext(),
					"Server Error!! Please try again later..",
					Toast.LENGTH_LONG).show();
		}

		// Handles unknown host exception
		catch (UnknownHostException unknownHost) {
			Log.d("Error", "You are trying to connect to an unknown host!");
		}
		// Catch Input Output exception
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
		// Catch general exception
		catch (Exception e) {
			Log.d("Error", e.getMessage());
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public void sendMessage(String msg) {
		out.println(msg);
		out.flush();
		Log.d("client>", msg);
	}

	// @Override
	/*
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu((android.view.Menu) menu);
	}

	// @Override
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.voteoptionsmenu, menu);
		return true;
	}

	/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.help:
			Intent i = new Intent();
			i.setClass(Voting.this, Help.class);
			startActivity(i);
			break;

		case R.id.stats:
			Intent i2 = new Intent();
			i2.putExtra("votes", voteList);
			i2.putExtra("votesint", votes);
			i2.setClass(Voting.this, Stats.class);
			startActivity(i2);
			break;

		case R.id.settings:
			Intent i3 = new Intent();
			i3.setClass(Voting.this, Settings.class);
			startActivity(i3);
			break;

		case R.id.quit:
			finish();
			break;
		}
		return true;
	}
	*/
}