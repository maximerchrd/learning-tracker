package com.sciquizapp.sciquiz;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.sciquizapp.sciquiz.R;


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
public class QuizActivity extends Activity {
	//List<Question> quesList;
	List<Question> quesList1;
	List<Question> quesList2;
	List<Question> quesList3;
	int score=0;
	int qid=0;
	int level=1;
	int nbQuestionsLevel1 = 8;
	int nbQuestionsLevel2 = 6;
	int nbQuestionsLevel3 = 5;
	int trialCounter = 1;
	Question currentQ;
	TextView txtQuestion;
	Button answerButton1, answerButton2, answerButton3, answerButton4;
	ImageView picture;
	boolean isImageFitToScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_quiz);
		final DbHelper db = new DbHelper(this);
		Bundle bun = getIntent().getExtras();
		final String subjectQuiz = bun.getString("subject");
		//quesList = db.getQuestionsFromSubject(subjectQuiz);
		quesList1 = db.getQuestionsFromSubjectAndLevel(subjectQuiz, 1);
		quesList2 = db.getQuestionsFromSubjectAndLevel(subjectQuiz, 2);
		quesList3 = db.getQuestionsFromSubjectAndLevel(subjectQuiz, 3);
		Collections.shuffle(quesList1);
		Collections.shuffle(quesList2);
		Collections.shuffle(quesList3);

		txtQuestion=(TextView)findViewById(R.id.textView1);
		answerButton1 = (Button)findViewById(R.id.answerbutton1);
		answerButton2 = (Button)findViewById(R.id.answerbutton2);
		answerButton3 = (Button)findViewById(R.id.answerbutton3);
		answerButton4 = (Button)findViewById(R.id.answerbutton4);
		picture = (ImageView)findViewById(R.id.picture);

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

		currentQ=quesList1.get(qid);
		setQuestionView();

		answerButton1.setOnClickListener(new View.OnClickListener() {		
			@SuppressLint("SimpleDateFormat") @Override
			public void onClick(View v) {
				if(currentQ.getANSWER().equals(answerButton1.getText()))
				{
					//write the number of trials in the database
					db.incrementTrialNFromQuestion(trialCounter, currentQ);
					
					if(level == 1 && qid < nbQuestionsLevel1){					
						score = score + 2;
						currentQ=quesList1.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else if (level == 2 && qid < nbQuestionsLevel2) {
						score = score + 5;
						currentQ=quesList2.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else if (level == 3 && qid < nbQuestionsLevel3) {
						score = score + 10;
						currentQ=quesList3.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else {
						if (level == 1) {
							score = score + 2;
							currentQ=quesList1.get(qid);
						} else if (level == 2) {
							score = score + 5;
							currentQ=quesList2.get(qid);
						} else {
							score = score + 10;
							currentQ=quesList3.get(qid);
						}
						if (level == 1 && score < 13) {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "1");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
							finish();
						} else if (level == 1 && score >= 13) {
							qid = 0;
							currentQ = quesList2.get(qid);
							level = 2;
							Toast toast = Toast.makeText(getApplicationContext(), "Bien joué! \n Vous passez au niveau 2", Toast.LENGTH_SHORT);
							LinearLayout toastLayout = (LinearLayout) toast.getView();
							TextView toastTV = (TextView) toastLayout.getChildAt(0);
							toastTV.setTextSize(30);
							toast.show();
							setQuestionView();
						} else if (level == 2 && score < 37) {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "2");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
						} else if (level == 2 && score >= 37) {
							qid = 0;
							currentQ = quesList3.get(qid);
							level = 3;
							Toast toast = Toast.makeText(getApplicationContext(), "Bien joué! \n Vous passez au niveau 3", Toast.LENGTH_SHORT);
							LinearLayout toastLayout = (LinearLayout) toast.getView();
							TextView toastTV = (TextView) toastLayout.getChildAt(0);
							toastTV.setTextSize(30);
							toast.show();
							setQuestionView();
						} else {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "3");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
							finish();
						}

					}
				}else{
					trialCounter++;
					answerButton1.setBackgroundColor(Color.parseColor("#FE1B00"));
					if (level == 1) {
						score = score - 1;
					} else if (level == 2) {
						score = score - 4;
					} else {
						score = score - 8;
					}
				}
				invalidateOptionsMenu();
			}
		});
		answerButton2.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if (currentQ.getANSWER().equals(answerButton2.getText()))
				{
					//write the number of trials in the database
					db.incrementTrialNFromQuestion(trialCounter, currentQ);
					
					if(level == 1 && qid < nbQuestionsLevel1){					
						score = score + 2;
						currentQ=quesList1.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else if (level == 2 && qid < nbQuestionsLevel2) {
						score = score + 5;
						currentQ=quesList2.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else if (level == 3 && qid < nbQuestionsLevel3) {
						score = score + 10;
						currentQ=quesList3.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else {
						if (level == 1) {
							score = score + 2;
							currentQ=quesList1.get(qid);
						} else if (level == 2) {
							score = score + 5;
							currentQ=quesList2.get(qid);
						} else {
							score = score + 10;
							currentQ=quesList3.get(qid);
						}
						if (level == 1 && score < 13) {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "1");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
							finish();
						} else if (level == 1 && score >= 13) {
							qid = 0;
							currentQ = quesList2.get(qid);
							level = 2;
							Toast toast = Toast.makeText(getApplicationContext(), "Bien joué! \n Vous passez au niveau 2", Toast.LENGTH_SHORT);
							LinearLayout toastLayout = (LinearLayout) toast.getView();
							TextView toastTV = (TextView) toastLayout.getChildAt(0);
							toastTV.setTextSize(30);
							toast.show();
							setQuestionView();
						} else if (level == 2 && score < 37) {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "2");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
						} else if (level == 2 && score >= 37) {
							qid = 0;
							currentQ = quesList3.get(qid);
							level = 3;
							Toast toast = Toast.makeText(getApplicationContext(), "Bien joué! \n Vous passez au niveau 3", Toast.LENGTH_SHORT);
							LinearLayout toastLayout = (LinearLayout) toast.getView();
							TextView toastTV = (TextView) toastLayout.getChildAt(0);
							toastTV.setTextSize(30);
							toast.show();
							setQuestionView();
						} else {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "3");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
							finish();
						}
					}
				} else {
					trialCounter++;
					answerButton2.setBackgroundColor(Color.parseColor("#FE1B00"));
					if (level == 1) {
						score = score - 1;
					} else if (level == 2) {
						score = score - 4;
					} else {
						score = score - 8;
					}
				}
				invalidateOptionsMenu();
			}
		});
		answerButton3.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(currentQ.getANSWER().equals(answerButton3.getText()))
				{
					//write the number of trials in the database
					db.incrementTrialNFromQuestion(trialCounter, currentQ);
					
					if(level == 1 && qid < nbQuestionsLevel1){					
						score = score + 2;
						currentQ=quesList1.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else if (level == 2 && qid < nbQuestionsLevel2) {
						score = score + 5;
						currentQ=quesList2.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else if (level == 3 && qid < nbQuestionsLevel3) {
						score = score + 10;
						currentQ=quesList3.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else {
						if (level == 1) {
							score = score + 2;
							currentQ=quesList1.get(qid);
						} else if (level == 2) {
							score = score + 5;
							currentQ=quesList2.get(qid);
						} else {
							score = score + 10;
							currentQ=quesList3.get(qid);
						}
						if (level == 1 && score < 13) {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "1");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
							finish();
						} else if (level == 1 && score >= 13) {
							qid = 0;
							currentQ = quesList2.get(qid);
							level = 2;
							Toast toast = Toast.makeText(getApplicationContext(), "Bien joué! \n Vous passez au niveau 2", Toast.LENGTH_SHORT);
							LinearLayout toastLayout = (LinearLayout) toast.getView();
							TextView toastTV = (TextView) toastLayout.getChildAt(0);
							toastTV.setTextSize(30);
							toast.show();
							setQuestionView();
						} else if (level == 2 && score < 37) {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "2");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
						} else if (level == 2 && score >= 37) {
							qid = 0;
							currentQ = quesList3.get(qid);
							level = 3;
							Toast toast = Toast.makeText(getApplicationContext(), "Bien joué! \n Vous passez au niveau 3", Toast.LENGTH_SHORT);
							LinearLayout toastLayout = (LinearLayout) toast.getView();
							TextView toastTV = (TextView) toastLayout.getChildAt(0);
							toastTV.setTextSize(30);
							toast.show();
							setQuestionView();
						} else {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "3");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
							finish();
						}
					}
				} else {
					trialCounter++;
					answerButton3.setBackgroundColor(Color.parseColor("#FE1B00"));
					if (level == 1) {
						score = score - 1;
					} else if (level == 2) {
						score = score - 4;
					} else {
						score = score - 8;
					}
				}
				invalidateOptionsMenu();
			}
		});
		answerButton4.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(currentQ.getANSWER().equals(answerButton4.getText()))
				{
					//write the number of trials in the database
					db.incrementTrialNFromQuestion(trialCounter, currentQ);
					
					if(level == 1 && qid < nbQuestionsLevel1){					
						score = score + 2;
						currentQ=quesList1.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else if (level == 2 && qid < nbQuestionsLevel2) {
						score = score + 5;
						currentQ=quesList2.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else if (level == 3 && qid < nbQuestionsLevel3) {
						score = score + 10;
						currentQ=quesList3.get(qid);
						Log.d("score", "Your score"+score);
						setQuestionView();
					} else{
						if (level == 1) {
							score = score + 2;
							currentQ=quesList1.get(qid);
						} else if (level == 2) {
							score = score + 5;
							currentQ=quesList2.get(qid);
						} else {
							score = score + 10;
							currentQ=quesList3.get(qid);
						}
						if (level == 1 && score < 13) {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);;
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "1");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
							finish();
						} else if (level == 1 && score >= 13) {
							qid = 0;
							currentQ = quesList2.get(qid);
							level = 2;
							Toast toast = Toast.makeText(getApplicationContext(), "Bien joué! \n Vous passez au niveau 2", Toast.LENGTH_SHORT);
							LinearLayout toastLayout = (LinearLayout) toast.getView();
							TextView toastTV = (TextView) toastLayout.getChildAt(0);
							toastTV.setTextSize(30);
							toast.show();
							setQuestionView();
						} else if (level == 2 && score < 37) {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);;
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "2");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
						} else if (level == 2 && score >= 37) {
							qid = 0;
							currentQ = quesList3.get(qid);
							level = 3;
							Toast toast = Toast.makeText(getApplicationContext(), "Bien joué! \n Vous passez au niveau 3", Toast.LENGTH_SHORT);
							LinearLayout toastLayout = (LinearLayout) toast.getView();
							TextView toastTV = (TextView) toastLayout.getChildAt(0);
							toastTV.setTextSize(30);
							toast.show();
							setQuestionView();
						} else {
							// get date and time
							Calendar c = Calendar.getInstance();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");
							String formattedDate = df.format(c.getTime());
							String scoreString = Integer.toString(score);;
							Score scoreTOdb = new Score(formattedDate, subjectQuiz, scoreString, "3");
							db.addScore(scoreTOdb);

							Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("score", score); //Your score
							b.putInt("level", level);
							intent.putExtras(b); //Put your score to your next Intent
							startActivity(intent);
							finish();
						}
					}
				} else {
					trialCounter++;
					answerButton4.setBackgroundColor(Color.parseColor("#FE1B00"));
					if (level == 1) {
						score = score - 1;
					} else if (level == 2) {
						score = score - 4;
					} else {
						score = score - 8;
					}
				}
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