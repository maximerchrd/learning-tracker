package com.sciquizapp.sciquiz;

public class Score {
	private int IDscores;
	//private String DATE;
	private String TIME;
	private String SUBJECTscores;
	private String SCORE;
	private String LEVEL;
	
	public Score()
	{
		IDscores = 0;
		//DATE = "";
		TIME = "";
		SUBJECTscores = "";
		SCORE = "";
		LEVEL = "";
	}
	public Score(/*String dATE,*/ String tIME, String sUBJECTscores, String sCORE, String lEVEL) {
		//DATE = dATE;
		TIME = tIME;
		SUBJECTscores = sUBJECTscores;
		SCORE = sCORE;
		LEVEL = lEVEL;
	}
	public int getIDscores()
	{
		return IDscores;
	}
	/*public String getDATE() {
		return DATE;
	}*/
	public String getTIME() {
		return TIME;
	}
	public String getSUBJECTscores() {
		return SUBJECTscores;
	}
	public String getSCORE() {
		return SCORE;
	}
	public String getLEVEL() {
		return LEVEL;
	}
	public void setIDscores(int iDscores) {
		IDscores = iDscores;
	}
	/*public String setDATE(String dATE) {
		return DATE;
	}*/
	public void setTIME(String tIME) {
		TIME = tIME;
	}
	public void setSUBJECTscores(String sUBJECTscores) {
		SUBJECTscores = sUBJECTscores;
	}
	public void setSCORE(String sCORE) {
		SCORE = sCORE;
	}
	public void setLEVEL(String lEVEL) {
		LEVEL = lEVEL;
	}
}
