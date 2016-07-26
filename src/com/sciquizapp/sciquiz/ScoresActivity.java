package com.sciquizapp.sciquiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.ExpandableListView;
 
public class ScoresActivity extends Activity {
 
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Button backToMenuButton;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
 
        backToMenuButton = (Button)findViewById(R.id.button1);
		//listener for "back to menu" button
		backToMenuButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        DbHelper db = new DbHelper(this);
 
        // Adding child data
        listDataHeader.add("système cardiovasculaire");
        listDataHeader.add("forces");
        listDataHeader.add("immunité et pathogènes");
        
 
     // Adding child data
        List<Score> scoresSysCar;
        scoresSysCar = db.getScoresFromSubject("système cardiovasculaire");
        List<String> sysCar = new ArrayList<String>();
        sysCar.add("Date                     Niveau             Score");
        //sort list according to score
        for (int i = 0; i < scoresSysCar.size() && i < 5; i++) {
        	Score maxscore = scoresSysCar.get(i);
        	for (int j = i+1; j < scoresSysCar.size(); j++) {
        		if (Integer.parseInt(scoresSysCar.get(j).getSCORE()) > Integer.parseInt(maxscore.getSCORE())) {
        			maxscore = scoresSysCar.get(j);
        			scoresSysCar.set(j, scoresSysCar.get(i));
        			scoresSysCar.set(i, maxscore);
        		}
        	}
        	String time = scoresSysCar.get(i).getTIME();
        	String upToNCharacters = time.substring(0, Math.min(time.length(), 10));
        	sysCar.add(upToNCharacters+"       "+scoresSysCar.get(i).getLEVEL()+"                        "+scoresSysCar.get(i).getSCORE());
        }
     // Adding child data
        List<Score> scoreforces;
        scoreforces = db.getScoresFromSubject("forces");
        List<String> forces = new ArrayList<String>();
        forces.add("Date                     Niveau             Score");
        //sort list according to score
        for (int i = 0; i < scoreforces.size() && i < 5; i++) {
        	Score maxscore = scoreforces.get(i);
        	for (int j = i+1; j < scoreforces.size(); j++) {
        		if (Integer.parseInt(scoreforces.get(j).getSCORE()) > Integer.parseInt(maxscore.getSCORE())) {
        			maxscore = scoreforces.get(j);
        			scoreforces.set(j, scoreforces.get(i));
        			scoreforces.set(i, maxscore);
        		}
        	}
        	String time = scoreforces.get(i).getTIME();
        	String upToNCharacters = time.substring(0, Math.min(time.length(), 10));
        	forces.add(upToNCharacters+"       "+scoreforces.get(i).getLEVEL()+"                        "+scoreforces.get(i).getSCORE());
        }
        
     // Adding child data
        List<Score> scoreimmunite;
        scoreimmunite = db.getScoresFromSubject("immunité et pathogènes");
        List<String> immunite = new ArrayList<String>();
        immunite.add("Date                     Niveau             Score");
        //sort list according to score
        for (int i = 0; i < scoreimmunite.size() && i < 5; i++) {
        	Score maxscore = scoreimmunite.get(i);
        	for (int j = i+1; j < scoreimmunite.size(); j++) {
        		if (Integer.parseInt(scoreimmunite.get(j).getSCORE()) > Integer.parseInt(maxscore.getSCORE())) {
        			maxscore = scoreimmunite.get(j);
        			scoreimmunite.set(j, scoreimmunite.get(i));
        			scoreimmunite.set(i, maxscore);
        		}
        	}
        	String time = scoreimmunite.get(i).getTIME();
        	String upToNCharacters = time.substring(0, Math.min(time.length(), 10));
        	forces.add(upToNCharacters+"       "+scoreimmunite.get(i).getLEVEL()+"                        "+scoreforces.get(i).getSCORE());
        }

        listDataChild.put(listDataHeader.get(0), sysCar); // Header, Child data
        listDataChild.put(listDataHeader.get(1), forces);
        listDataChild.put(listDataHeader.get(2), immunite);
    }
}
