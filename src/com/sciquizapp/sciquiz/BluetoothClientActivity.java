package com.sciquizapp.sciquiz;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.TextView;

public class BluetoothClientActivity extends Activity {
	TextView out;
	TextView wait_for_question;
	ImageView picture;
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	private int current = 0; //tells where is the "cursor" when reading a file
	public final static int FILE_SIZE = 7737; // file size temporary hard coded
	// should bigger than the file to be downloaded

	// Well known SPP UUID
	private static final UUID MY_UUID =
			UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	// Insert your server's MAC address
	private static String address = "C0:33:5E:11:B6:16";

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetoothclient);

		wait_for_question = (TextView) findViewById(R.id.waitingforquestion);
		out = (TextView) findViewById(R.id.out);

		wait_for_question.append("En attente de la question suivante");
		out.append("\n...In onCreate()...");

		btAdapter = BluetoothAdapter.getDefaultAdapter();
		CheckBTState();
		
		//File imgFile = new  File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/ap11.jpg");
		picture = (ImageView)findViewById(R.id.imageview);
		//int imageResource = getResources().getIdentifier("res/drawable/small.jpg", null, getPackageName());
		//String path = Environment.DIRECTORY_DCIM+"/ice_maze";
		//Bitmap image = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		//picture.setImageBitmap(image);
	}

	public void onStart() {
		super.onStart();
		out.append("\n...In onStart()...");
	}

	public void onResume() {
		super.onResume();

		out.append("\n...In onResume...\n...Attempting client connect...");

		//for later: automatically pair the devices

		// Set up a pointer to the remote node using its address.
		BluetoothDevice device = btAdapter.getRemoteDevice(address);

		// Two things are needed to make a connection:
		//   A MAC address, which we got above.
		//   A Service ID or UUID.  In this case we are using the
		//     UUID for SPP.
		try {
			btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
			//btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			AlertBox("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
		}

		// Discovery is resource intensive.  Make sure it isn't going on
		// when you attempt to connect and pass your message.
		btAdapter.cancelDiscovery();

		// Establish the connection.  This will block until it connects.
		try {
			btSocket.connect();
			out.append("\n...Connection established and data link opened...");
		} catch (IOException e) {
			e.printStackTrace();
			try {
				btSocket.close();
			} catch (IOException e2) {
				AlertBox("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
			}
		}

		//test if socket open. if not, stops executing onresume()
		//if(btSocket.isConnected()) {
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

			InputStream inStream = null;
			
			
			try {
				inStream = btSocket.getInputStream();
				//BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
				//String lineRead = bReader.readLine();
				byte[] stringBuffer = new byte[100];
				int sizeRead = 0;
				
				do {
					sizeRead = inStream.read(stringBuffer, current, (100 - current));
				    if(sizeRead >= 0) current += sizeRead;
				} while(sizeRead > 0);    //shall be sizeRead > -1, because .read returns -1 when finished reading, but outstream not closed on server side
				
				String string_file_size = new String(stringBuffer, "UTF-8");
				int file_size = Integer.parseInt(string_file_size.replaceAll("[\\D]", ""));
				byte [] imageBuffer = new byte[file_size];
				byte[] inputBuffer = new byte[100+file_size];
				do {
					sizeRead = inStream.read(inputBuffer, current, (100 + file_size - current));
				    if(sizeRead >= 0) current += sizeRead;
				} while(sizeRead > 0);    //shall be sizeRead > -1, because .read returns -1 when finished reading, but outstream not closed on server side
				
				for (int i = 100; i < file_size + 100; i++) {
					imageBuffer[i-100] = inputBuffer[i];
				}
				//String inputMsg = new String(inputBuffer,"US-ASCII");
				//out.append("\n...this is the line read: "+ inputMsg +"\n");
				ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBuffer);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				//picture = (ImageView)findViewById(R.id.imageview);
				picture.setImageBitmap(bitmap);			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//} else {
		//	out.append("\n...socket not connected...");
		//}
	}

	public void onPause() {
		super.onPause();

		//out.append("\n...Hello\n");

		out.append("\n...In onPause()...");



		if (outStream != null) {
			try {
				outStream.flush();
			} catch (IOException e) {
				AlertBox("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
			}
		}

		try     {
			btSocket.close();
		} catch (IOException e2) {
			AlertBox("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
		}
	}

	public void onStop() {
		super.onStop();
		out.append("\n...In onStop()...");
	}

	public void onDestroy() {
		super.onDestroy();
		out.append("\n...In onDestroy()...");
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
}

