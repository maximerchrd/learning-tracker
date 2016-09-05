package com.sciquizapp.sciquiz.NetworkCommunication;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class NetworkCommunication {
	private Context mContextNetCom;
	private ArrayList<ArrayList<String>> mNetwork_addresses;
	private WifiCommunication mWifiCom;
	public String mWifiName = "LT_AdHoc";
	public String mWifiPassword = "L3J28#loL";

	public NetworkCommunication(Context arg_context) {
		mNetwork_addresses = new ArrayList<ArrayList<String>>();
		mContextNetCom = arg_context;
		mWifiCom = new WifiCommunication(arg_context);
	}

	/**
	 * method to launch the network of smartphones and 1 laptop communicating using wifi direct and bluetooth
	 */
	public void startNetwork() {
		new Thread(new Runnable() {
			public void run() {
				if (mWifiCom.scanForWifiName(mWifiName)) {
					mWifiCom.connectToWifiWithName(mWifiName, mWifiPassword);                 
				} else {
					Log.d("startNetwork", "starting new hotspot");
					mWifiCom.startAdhocWifi(mWifiName, mWifiPassword);
				}
			}
		}).start();
	}


}
