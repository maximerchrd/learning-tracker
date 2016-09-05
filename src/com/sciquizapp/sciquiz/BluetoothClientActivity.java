package com.sciquizapp.sciquiz;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

public class BluetoothClientActivity extends Activity implements Parcelable{			//serializable: to pass object to other class
	TextView out;
	TextView wait_for_question;
	ImageView picture;
	private static final int REQUEST_ENABLE_BT = 1;	
	private BluetoothAdapter btAdapter = null;
	private static BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	private int current = 0; //tells where is the "cursor" when reading a file
	private String question_text_string = "";
	private OldBluetoothCommunication mBTCom;
	private BroadcastReceiver mActivityReceiver;



	// Well known SPP UUID
	private static final UUID MY_UUID =	UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	//	private static final UUID MY_UUID =	UUID.fromString("125af5e2-1a2c-6980-c44d-23fd57103e39");

	// Insert your server's MAC address
	private  static String address = "C0:33:5E:11:B6:16";	

	public BluetoothClientActivity() {

	}
	public BluetoothClientActivity(Parcel in) {
		//just to declare a constructor with Parcel as argument

	}
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetoothclient);

		wait_for_question = (TextView) findViewById(R.id.waitingforquestion);
		out = (TextView) findViewById(R.id.out);

		wait_for_question.append("En attente de la question suivante");
		//		out.append("\n...In onCreate()...");

		mBTCom = new OldBluetoothCommunication(this);
		//		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			final BluetoothManager mbluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			btAdapter = mbluetoothManager.getAdapter();
		} else {
			btAdapter = BluetoothAdapter.getDefaultAdapter();
		}		
		CheckBTState();

		

	}

	public void onStart() {
		super.onStart();
		//		out.append("\n...In onStart()...");
	}

	public void onResume() {
		super.onResume();
		if (btSocket == null) {
			//			out.append("\n...In onResume...\n...Attempting client connect...");

			//for later: automatically pair the devices
			mActivityReceiver = mBTCom.ConnectToBluetoothMaster(btAdapter);

			//			// Set up a pointer to the remote node using its address.
			////			BluetoothDevice device = btAdapter.getRemoteDevice(mac_adress_list.get(0));
			//			
			//			// Two things are needed to make a connection:
			//			//   A MAC address, which we got above.
			//			//   A Service ID or UUID.  In this case we are using the
			//			//     UUID for SPP.
			//			try {
			//				btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
			//				//btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
			//			} catch (IOException e) {
			//				AlertBox("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
			//			}
			//
			//			// Discovery is resource intensive.  Make sure it isn't going on
			//			// when you attempt to connect and pass your message.
			//			btAdapter.cancelDiscovery();
			//
			//			// Establish the connection.  This will block until it connects.
			//			try {
			//				btSocket.connect();
			//				out.append("\n...Connection established and data link opened...");
			//			} catch (IOException e) {
			//				e.printStackTrace();
			//				try {
			//					btSocket.close();
			//				} catch (IOException e2) {
			//					AlertBox("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
			//				}
			//			}
			/*
			//test if socket open. if not, stops executing onresume()
			if(btSocket.isConnected()) {
				// Create a data stream so we can talk to server.
				out.append("\n...Sending message to server...");
				String message = "Hello from Android.\n";
				out.append("\n\n...The message that we will send to the server is: "+message);

				try {
					outStream = btSocket.getOutputStream();
				} catch (IOException e) {
					AlertBox("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
				}


				byte[] msgBuffer = message.getBytes();
				try {
					outStream.write(msgBuffer);
				} catch (IOException e) {
					String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
					if (address.equals("00:00:00:00:00:00")) 
						msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
					msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\nor server not started.\n";

					AlertBox("Fatal Error", msg);       
				}

				new Thread(new Runnable() {
					public void run() {
						while (true) {
							//reception of the question sent by the server per BT
							questionReception();

							//launches question activity
							launchQuestionActivity();
						}
					}
				}).start();

			} else {
				out.append("\n...socket not connected...");
			}*/
		}
	}

	public void onPause() {
		super.onPause();

		//out.append("\n...Hello\n");

		//		out.append("\n...In onPause()...");
	}

	public void onStop() {
		super.onStop();
		//		out.append("\n...In onStop()...");

		//		if (outStream != null) {
		//			try {
		//				outStream.flush();
		//			} catch (IOException e) {
		//				AlertBox("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
		//			}
		//		}
		//
		//		try     {
		//			((MyApplication) getApplication()).global_bluetoothSocket.close();
		//		} catch (IOException e2) {
		//			AlertBox("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
		//		}
	}

	public void onDestroy() {
		super.onDestroy();
		//		out.append("\n...In onDestroy()...");
		if (btAdapter.getName().startsWith("prefix")) {
			String new_name = btAdapter.getName();
			new_name.replaceFirst("prefix", "");
			btAdapter.setName(new_name);
		}
		
		this.unregisterReceiver(mActivityReceiver);
	}

	private void CheckBTState() {
		// Check for Bluetooth support and then check to make sure it is turned on

		// Emulator doesn't support Bluetooth and will return null
		if(btAdapter==null) { 
			AlertBox("Fatal Error", "Bluetooth Not supported. Aborting.");
		} else {
			if (btAdapter.isEnabled()) {
				out.append("\n...Bluetooth is enabled...");
			} else {
				//Prompt user to turn on Bluetooth
				Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	public void AlertBox( String title, String message ){
		new AlertDialog.Builder(this)
		.setTitle( title )
		.setMessage( message + " Press OK to exit." )
		.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		}).show();
	}

	/**
	 * Define the kind of object that you gonna parcel,
	 * You can use hashCode() here
	 */
	@Override
	public int describeContents() {
		return 0;
	}
	/**
	 * Actual object serialization happens here, Write object content
	 * to parcel, reading should be done according to this write order
	 * param dest - parcel
	 * param flags - Additional flags about how the object should be written
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//	    dest.writeString(id);
		//	    dest.writeString(username);
		//	    dest.writeString(email);
		//	    dest.writeString(password);
	} 
	/**
	 * This field is needed for Android to be able to
	 * create new objects, individually or as arrays
	 *
	 * If you don’t do that, Android framework will raises an exception
	 * Parcelable protocol requires a Parcelable.Creator object 
	 * called CREATOR
	 */
	public static final Parcelable.Creator<BluetoothClientActivity> CREATOR = new Parcelable.Creator<BluetoothClientActivity>() {

		public BluetoothClientActivity createFromParcel(Parcel in) {
			return new BluetoothClientActivity(in);
		}

		public BluetoothClientActivity[] newArray(int size) {
			return new BluetoothClientActivity[size];
		}
	};
}