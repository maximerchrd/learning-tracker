package com.sciquizapp.sciquiz;

import com.sciquizapp.sciquiz.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
public class ResultActivity extends Activity {
	Button backToMenuButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		backToMenuButton = (Button)findViewById(R.id.button1);
		//get rating bar object
		RatingBar bar=(RatingBar)findViewById(R.id.ratingBar1); 
		bar.setNumStars(3);
		bar.setStepSize(0.5f);
		//get text view
		TextView t=(TextView)findViewById(R.id.textResult);
		//get score
		Bundle b = getIntent().getExtras();
		int score= b.getInt("score");
		int level = b.getInt("level");
		float levelFloat = 0;
		if (level == 1 && score > 8) {
			levelFloat = (float)0.5;
		} else if (level == 2 && score <= 28) {
			levelFloat = (float)1;
		} else if (level == 2 && score > 28) {
			levelFloat = (float)1.5;
		} else if (level == 3 && score <= 65) {
			levelFloat = (float)2;
		} else if (level == 3 && score > 65) {
			levelFloat = (float)2.5;
		} else if (level == 3 && score == 96) {
			levelFloat = (float)3;
		}
		//display score
		bar.setRating(levelFloat);
		t.setText("Votre score: "+score);
		
		//listener for "back to menu" button
		backToMenuButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(ResultActivity.this, MenuActivity.class);
				//startActivity(intent);
				finish();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_result, menu);
		return true;
	}
}