package com.sciquizapp.sciquiz;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BluetoothCommunication {

	//member variables
	private Context mContext;
	private IntentFilter mFilter;
	private ArrayList<String> mMac_adress_list = new ArrayList<String>();
	private ArrayList<String> mUuids_list = new ArrayList<String>();
	private String mDevices_list = "";
	private String mMasterAddress = "";
	private BluetoothAdapter mBTAdapter = null;
	private static BluetoothSocket mBTSocket = null;
	private Boolean mClientIsConnected = false;
	private OutputStream mOutStream = null;
	private InputStream mInStream = null;
	private int current = 0; //tells where is the "cursor" when reading a file
	private String question_text_string = "";
	// Well known SPP UUID
	private static final UUID MY_UUID =	UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


	// Create a BroadcastReceiver for ACTION_FOUND (for pairing and connecting without MAC address)
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				//discovery starts, we can show progress dialog or perform other tasks
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				//discovery finishes, dismis progress dialog
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				//bluetooth device found
				BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//				device.fetchUuidsWithSdp();
				// Add the name and address to an array adapter to show in a ListView
				//				mDevices_list += device.getName() + "\t" + device.getAddress() + "\t" + device.getUuids().toString() + "\n";
				//								Toast.makeText(mContext, device.getName() + "\t" + device.getAddress() + "\t" + device.getUuids(),
				//										   Toast.LENGTH_LONG).show();
				if (device.getUuids() != null && !mMac_adress_list.contains(device.getAddress())) {
					mMac_adress_list.add(device.getAddress());
				}
				//			} else if(BluetoothDevice.ACTION_UUID.equals(action)) {
				//		         BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//		         Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
				//		         for (int i=0; i<uuidExtra.length; i++) {
				////		           out.append("\n  Device: " + device.getName() + ", " + device + ", Service: " + uuidExtra[i].toString());
				////		           Toast.makeText(mContext, "\n  Device: " + device.getName() + ", " + device + ", Service: " + uuidExtra[i].toString(),
				////						   Toast.LENGTH_LONG).show();
				//		        	 mDevices_list += "\n  Device: " + device.getName() + ", " + device + ", Service: " + uuidExtra[i].toString();
				//		         }
			}
		}
	};

	/**
	 * Constructor taking the activity context as argument
	 * @param arg_context
	 */
	public BluetoothCommunication(Context arg_context) {
		mContext = arg_context;
	}

	/**
	 * Method for discovering bluetooth devices. It returns a string with addresses, names and uuids
	 * of the devices.
	 */
	public BroadcastReceiver ConnectToBluetoothMaster(BluetoothAdapter arg_btAdapter) {
		if (mClientIsConnected == false) {
			mBTAdapter = arg_btAdapter;
			// Register the BroadcastReceiver
			mFilter = new IntentFilter();
			mFilter.addAction(BluetoothDevice.ACTION_FOUND);
			mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
			mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			//		mFilter.addAction(BluetoothDevice.ACTION_UUID);

			mContext.registerReceiver(mReceiver, mFilter); // Don't forget to unregister during onDestroy
			mBTAdapter.startDiscovery();

			mClientIsConnected = false;


			new Thread(new Runnable() {
				public void run() {
					for (int i = 0; i < 12 && mClientIsConnected == false; i++) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (mMac_adress_list.size() > 0) {
							ConnectToDeviceWithAddress(mMac_adress_list.get(0));
							if (mClientIsConnected) {
								InputStream inStream = null;
								int current = 0;
								try {
									inStream = mBTSocket.getInputStream();
									byte[] stringBuffer = new byte[40];
									int sizeRead = 0;
									do {
										sizeRead = inStream.read(stringBuffer, current, (40 - current));
										if(sizeRead >= 0) current += sizeRead;
									} while(sizeRead > 0);    //shall be sizeRead > -1, because .read returns -1 when finished reading, but outstream not closed on server side
									String response = new String(stringBuffer, "UTF-8");
									if (!response.split("///")[0].equals("SERVER")) {
										mBTSocket.close();
									} else if (response.split("///")[0].equals("SERVER") && !response.split("///")[1].equals("OK")) {
										mBTSocket.close();
										ConnectToDeviceWithAddress(response.split("///")[1]);
									} else {
										mOutStream = mBTSocket.getOutputStream();
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
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}).start();
		}
		return mReceiver;
	}
	/**
	 * 
	 * @param arg_address
	 * @return
	 */
	public String ConnectToDeviceWithAddress(String arg_address) {
		String connectionInfos = "";

		// Set up a pointer to the remote node using its address.
		BluetoothDevice device = mBTAdapter.getRemoteDevice(arg_address);		
		// Two things are needed to make a connection:
		//   A MAC address, which we got above.
		//   A Service ID or UUID.  In this case we are using the
		//     UUID for SPP.
		try {
			mBTSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
			//btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			Log.e("Fatal Error In onResume() and socket create failed: ", e.getMessage());
		}

		// Discovery is resource intensive.  Make sure it isn't going on
		// when you attempt to connect and pass your message.
		mBTAdapter.cancelDiscovery();

		// Establish the connection.  This will block until it connects.
		try {
			mBTSocket.connect();
			mClientIsConnected = true;
			connectionInfos = "connected";
		} catch (IOException e) {
			Log.w("connection failed: the device cannot be a master or the server is not activated. Address of device: ", device.getAddress());
			e.printStackTrace();
			try {
				mBTSocket.close();
			} catch (IOException e2) {
				Log.e("Cannot close mBTSocket: ", e2.getMessage());
			}
		}


		return connectionInfos;
	}


	/**
	 * method used to send some string to the server. The BT connection
	 * has to be open.
	 */
	public void sendAnswerToServer(String answer) {
		if(mBTSocket.isConnected()) {
			try {
				mOutStream = mBTSocket.getOutputStream();
			} catch (IOException e) {
				Log.e("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
			}

			byte[] ansBuffer = answer.getBytes();
			try {
				mOutStream.write(ansBuffer);
			} catch (IOException e) {
				String msg = "In sendAnswerToServer() and an exception occurred during write: " + e.getMessage();
				Log.e("Fatal Error", msg);       
			}
		} else {
			Log.w("SendAnswerToServer","\n...socket not connected when trying to send answer...");
		}
	}
	/**
	 * 
	 */
	private void launchQuestionActivity() {
		Intent mIntent = new Intent(mContext, SingleQuestionActivity.class);
		Bundle bun = new Bundle();
		bun.putString("question", question_text_string.split("///")[0]);
		bun.putString("optA", question_text_string.split("///")[1]);
		bun.putString("optB", question_text_string.split("///")[2]);
		bun.putString("optC", question_text_string.split("///")[3]);
		bun.putString("optD", question_text_string.split("///")[4]);
		bun.putString("image_name", question_text_string.split("///")[5]);
		//		bun.putParcelable("bluetoothSocket", btSocket);
		//		bun.putParcelable("bluetoothObject", this);
		mIntent.putExtras(bun);
		mContext.startActivity(mIntent);
	}

	/**
	 * 
	 */
	private void questionReception() {
		InputStream inStream = null;
		current = 0;
		try {
			inStream = mBTSocket.getInputStream();

			//reads the sizes of the text and of the imagefile
			byte[] stringBuffer = new byte[20];
			int sizeRead = 0;
			do {
				sizeRead = inStream.read(stringBuffer, current, (20 - current));
				if(sizeRead >= 0) current += sizeRead;
			} while(sizeRead > 0);    //shall be sizeRead > -1, because .read returns -1 when finished reading, but outstream not closed on server side

			String string_sizes = new String(stringBuffer, "UTF-8");
			String string_file_size = string_sizes.split(":")[0];
			String string_text_size = string_sizes.split(":")[1];
			int text_size = Integer.parseInt(string_text_size.replaceAll("[\\D]", ""));
			int file_size = Integer.parseInt(string_file_size);

			//reads the text
			byte [] textBuffer = new byte[text_size];
			byte[] inputBuffer = new byte[20+text_size+file_size];

			do {
				sizeRead = inStream.read(inputBuffer, current, (20 + text_size + file_size - current));
				if(sizeRead >= 0) current += sizeRead;
			} while(sizeRead > 0);    //shall be sizeRead > -1, because .read returns -1 when finished reading, but outstream not closed on server side

			for (int i = 0; i < text_size; i++) {
				textBuffer[i] = inputBuffer[i+20];
			}
			question_text_string = new String(textBuffer, "UTF-8");

			//copy the file from inputbuffer the imagebuffer !!! large files throw an arrayoutofbonds exception (tested up to ~600 ko)
			byte [] imageBuffer = new byte[file_size];
			for (int i = 0; i < file_size; i++) {
				imageBuffer[i] = inputBuffer[i+20+text_size];
			}
			ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBuffer);
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			//			picture = (ImageView)findViewById(R.id.imageview);
			//			picture.setImageBitmap(bitmap);		
			SaveImageFile(bitmap, question_text_string.split("///")[5]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void SaveImageFile(Bitmap imageToSave, String fileName) {

		File directory = new File(mContext.getFilesDir(),"images");
		String path = mContext.getFilesDir().getAbsolutePath();
		if (!directory.exists()) {
			directory.mkdirs();
		}

		File file = new File(directory,fileName);
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
