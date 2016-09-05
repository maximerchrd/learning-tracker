package com.sciquizapp.sciquiz;

import com.sciquizapp.sciquiz.AndroidClient.SendAsyncTask;
import com.sciquizapp.sciquiz.NetworkCommunication.NetworkCommunication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InteractiveModeActivity extends Activity {
	NetworkCommunication mNetCom;
	TextView intmod_out;
	TextView intmod_wait_for_question;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//initialize view
		setContentView(R.layout.activity_interactivemode);
		intmod_wait_for_question = (TextView) findViewById(R.id.intmod_wait_for_question);
		intmod_out = (TextView) findViewById(R.id.intmod_out);
		intmod_wait_for_question.append("En attente de la question suivante");
		
		mNetCom = new NetworkCommunication(this);
	}
	
	public void onStart() {
		super.onStart();
		
		mNetCom.startNetwork();		
	}
	
	public void onStop() {
		super.onStop();
	}
	
	/** Called when system is low on resources or finish() called on activity*/
	public void onDestroy() {
		super.onDestroy();
	}
}
