package com.sciquizapp.sciquiz;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.content.DialogInterface.OnClickListener;

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
				//				Toast.makeText(mContext, device.getName() + "\t" + device.getAddress() + "\t" + device.getUuids(),
				//						   Toast.LENGTH_LONG).show();
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
	public void ConnectToBluetoothMaster(BluetoothAdapter arg_btAdapter) {
		Boolean connected = false; 
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


}
