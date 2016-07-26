package com.sciquizapp.sciquiz;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;



import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class QuestionActivity extends Activity {
	//List<Question> quesList;
	List<Question> quesList;
	int score=0;
	int qid=0;
	int level=1;
	int nbQuestionsLevel1 = 8;
	int nbQuestionsLevel2 = 6;
	int nbQuestionsLevel3 = 5;
	int trialCounter = 1;
	int questionId = 1;
	Question currentQ;
	TextView txtQuestion;
	Button answerButton1, answerButton2, answerButton3, answerButton4;
	ImageView picture;
	boolean isImageFitToScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_question);
		final DbHelper db = new DbHelper(this);
		Bundle bun = getIntent().getExtras();
		questionId = bun.getInt("questionID");
		//final String subjectQuiz = bun.getString("subject");
		//quesList = db.getQuestionsFromSubject(subjectQuiz);
		quesList = db.getAllQuestions();


		txtQuestion=(TextView)findViewById(R.id.textViewQuest1);
		answerButton1 = (Button)findViewById(R.id.answerbuttonQuest1);
		answerButton2 = (Button)findViewById(R.id.answerbuttonQuest2);
		answerButton3 = (Button)findViewById(R.id.answerbuttonQuest3);
		answerButton4 = (Button)findViewById(R.id.answerbuttonQuest4);
		picture = (ImageView)findViewById(R.id.pictureQuest);

		picture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isImageFitToScreen) {
					isImageFitToScreen=false;
					picture.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.19f));
					picture.setAdjustViewBounds(true);
				}else{
					isImageFitToScreen=true;
					picture.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
				}
			}
		});

		currentQ=quesList.get(questionId);
		setQuestionView();

		answerButton1.setOnClickListener(new View.OnClickListener() {		
			@SuppressLint("SimpleDateFormat") @Override
			public void onClick(View v) {
				Intent intent = new Intent(QuestionActivity.this, AndroidClient.class);
				Bundle b = new Bundle();
				//questionID = Integer.parseInt(incomingMessage);
				b.putString("question", currentQ.getQUESTION());
				b.putString("answer", String.valueOf(answerButton1.getText()));
				if (answerButton1.getText().toString().matches(currentQ.getANSWER())) {
					b.putString("result", "right");
				} else {
					b.putString("result", "wrong");
				}
				intent.putExtras(b);
				startActivity(intent);
				finish();
				invalidateOptionsMenu();
			}
		});
		answerButton2.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QuestionActivity.this, AndroidClient.class);
				Bundle b = new Bundle();
				//questionID = Integer.parseInt(incomingMessage);
				b.putString("answer", String.valueOf(answerButton2.getText()));
				intent.putExtras(b);
				startActivity(intent);
				finish();
				invalidateOptionsMenu();
			}
		});
		answerButton3.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QuestionActivity.this, AndroidClient.class);
				Bundle b = new Bundle();
				//questionID = Integer.parseInt(incomingMessage);
				b.putString("answer", String.valueOf(answerButton3.getText()));
				intent.putExtras(b);
				startActivity(intent);
				finish();
				invalidateOptionsMenu();
			}
		});
		answerButton4.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QuestionActivity.this, AndroidClient.class);
				Bundle b = new Bundle();
				//questionID = Integer.parseInt(incomingMessage);
				b.putString("answer", String.valueOf(answerButton4.getText()));
				intent.putExtras(b);
				startActivity(intent);
				finish();
				invalidateOptionsMenu();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quiz, menu);
		MenuItem menuLevel = menu.findItem(R.id.menu_level);
		String stringlevel = String.valueOf(level);
		menuLevel.setTitle("NIVEAU:\n"+stringlevel);
		MenuItem menuScore = menu.findItem(R.id.menu_score);
		String stringscore = String.valueOf(score);
		menuScore.setTitle("SCORE:\n"+stringscore);
		return true;
	}
	private void setQuestionView()
	{
		txtQuestion.setText(currentQ.getQUESTION());
		answerButton1.setBackgroundColor(Color.parseColor("#00CCCB"));
		answerButton2.setBackgroundColor(Color.parseColor("#00CCCB"));
		answerButton3.setBackgroundColor(Color.parseColor("#00CCCB"));
		answerButton4.setBackgroundColor(Color.parseColor("#00CCCB"));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,      
				LinearLayout.LayoutParams.WRAP_CONTENT
		);
		int height = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		int width = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		params.setMargins(width / 40, height / 200, width / 40, height / 200);  //left, top, right, bottom
		answerButton1.setLayoutParams(params);
		answerButton2.setLayoutParams(params);
		answerButton3.setLayoutParams(params);
		answerButton4.setLayoutParams(params);
		
		int imageResource = getResources().getIdentifier(currentQ.getIMAGE(), null, getPackageName());
		picture.setImageResource(imageResource);


		String[] answerOptions;
		answerOptions = new String[4];
		answerOptions[0] = currentQ.getOPTA();
		answerOptions[1] = currentQ.getOPTB();
		answerOptions[2] = currentQ.getOPTC();
		answerOptions[3] = currentQ.getOPTD();

		//implementing Fisher-Yates shuffle
		Random rnd = new Random();
		for (int i = answerOptions.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			String a = answerOptions[index];
			answerOptions[index] = answerOptions[i];
			answerOptions[i] = a;
		}


		answerButton1.setText(answerOptions[0]);
		answerButton2.setText(answerOptions[1]);
		answerButton3.setText(answerOptions[2]);
		answerButton4.setText(answerOptions[3]);
		qid++;
	}
}