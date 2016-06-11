package com.sciquizapp.sciquiz;

import com.sciquizapp.sciquiz.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.view.View;


public class SettingsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		final EditText editName;
		final Button buttonSaveAndBack;
		final DbHelper db = new DbHelper(this);

		editName = (EditText) findViewById(R.id.edittextnom);
		buttonSaveAndBack = (Button) findViewById(R.id.buttonsaveandback);
		editName.setText(db.getName(), null);

		buttonSaveAndBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String name = editName.getText().toString();
				db.addName(name);
				Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("flag", "modify");
				startActivity(intent);
				finish();
			}
		});
	}
}
