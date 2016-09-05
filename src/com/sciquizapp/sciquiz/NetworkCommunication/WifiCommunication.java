package com.sciquizapp.sciquiz.NetworkCommunication;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.sciquizapp.sciquiz.WifiAccessManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiCommunication {
	private WifiManager mWifi;
	private Context mContextWifCom;
	List<android.net.wifi.ScanResult> mScanResults = new ArrayList<android.net.wifi.ScanResult>();
	BroadcastReceiver scanningreceiver;

	public WifiCommunication(Context arg_context) {
		mContextWifCom = arg_context;
		mWifi = (WifiManager) mContextWifCom.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * method that scans for wifi network and check if one of them is named wifiName
	 * @param wifiName
	 * @return true if the wifi name was detected, false if not
	 */
	public Boolean scanForWifiName(String wifiName) {
		//start wifi if not yet done
		if (mWifi.isWifiEnabled() == false) {
			mWifi.setWifiEnabled(true);
		}

		//scan for wifi networks and get the list
		mContextWifCom.registerReceiver(scanningreceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent intent) 
			{
				mScanResults = mWifi.getScanResults();
			}
		}, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		//start the scan and wait so that mScanResults isnt empty
		mWifi.startScan();
		try {
			Thread.sleep(6000);
			mContextWifCom.unregisterReceiver(scanningreceiver);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//get the names of the wifi networks
		List<String> ScanResultNames = new ArrayList<String>();
		for (int i = 0; i < mScanResults.size(); i++) {
			ScanResultNames.add(mScanResults.get(i).SSID);
		}

		if (ScanResultNames.contains(wifiName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * method used to start an adhoc wifi
	 * @param wifiName
	 * 
	 */
	public void startAdhocWifi(String wifiName, String wifiPassword) {
		//turn off the wifi if it is on
		new Thread(new Runnable() {
			public void run() {
				if (mWifi.isWifiEnabled()) {
					mWifi.setWifiEnabled(false);
				}
			}
		}).start();

		//wait for the wifi to be turned off, otherwise hotspot cannot be turned on. 200 ms work on huawei Y560/L01
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//setup the ad hoc wifi configuration
		WifiConfiguration wifi_configuration = new WifiConfiguration();
		wifi_configuration.SSID = wifiName;
		wifi_configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		wifi_configuration.preSharedKey = wifiPassword;
//		wifi_configuration.hiddenSSID = true;
//		wifi_configuration.status = WifiConfiguration.Status.DISABLED;     
//		wifi_configuration.priority = 40;
//		wifi_configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//		wifi_configuration.allowedProtocols.set(WifiConfiguration.Protocol.RSN); 
//		wifi_configuration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//		wifi_configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//		wifi_configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
//		wifi_configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//		wifi_configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//		wifi_configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//		wifi_configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
//
//		wifi_configuration.wepKeys[0] = "\"aaabbb1234\""; //This is the WEP Password
//		wifi_configuration.wepTxKeyIndex = 0;
		mWifi.addNetwork(wifi_configuration);
		try {
			mWifi.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class).invoke(mWifi, wifi_configuration, true);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method used to connect to an open wifi network
	 * @param wifiName
	 * 
	 */
	public void connectToWifiWithName (String wifiName, String wifiPassword) {
		//setup the wifi configuration
		WifiConfiguration wifi_configuration = new WifiConfiguration();
		wifi_configuration.SSID = "\"" +  wifiName + "\"";
//		wifi_configuration.hiddenSSID = true;
//		wifi_configuration.status = WifiConfiguration.Status.DISABLED;     
//		wifi_configuration.priority = 40;
//		wifi_configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
		wifi_configuration.preSharedKey = "\""+ "1p4W159#" +"\"";
//		wifi_configuration.allowedProtocols.set(WifiConfiguration.Protocol.RSN); 
//		wifi_configuration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//		wifi_configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//		wifi_configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
//		wifi_configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//		wifi_configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//		wifi_configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//		wifi_configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
//
//		wifi_configuration.wepKeys[0] = "\"aaabbb1234\""; //This is the WEP Password
//		wifi_configuration.wepTxKeyIndex = 0;

		int networkID = mWifi.addNetwork(wifi_configuration);
		mWifi.disconnect();
		mWifi.enableNetwork(networkID, true);
		mWifi.saveConfiguration();
		mWifi.reconnect();               

	}
}