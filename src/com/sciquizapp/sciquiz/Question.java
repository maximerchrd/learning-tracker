package com.sciquizapp.sciquiz;

public class Question {
	private int ID;
	private String SUBJECT;
	private String LEVEL;
	private String QUESTION;
	private String OPTA;
	private String OPTB;
	private String OPTC;
	private String OPTD;
	private String ANSWER;
	private String IMAGE;
	private String TRIAL1;
	private String TRIAL2;
	private String TRIAL3;
	private String TRIAL4;
	public Question()
	{
		ID=0;
		SUBJECT="";
		LEVEL="";
		QUESTION="";
		OPTA="";
		OPTB="";
		OPTC="";
		OPTD="";
		ANSWER="";
		TRIAL1 = "0";
		TRIAL2 = "0";
		TRIAL3 = "0";
		TRIAL4 = "0";
		IMAGE="none";
	}
	public Question(String sUBJECT, String lEVEL, String qUESTION, String oPTA, String oPTB, String oPTC, String oPTD,
			String aNSWER, String iMAGE) {
		
		SUBJECT = sUBJECT;
		LEVEL = lEVEL;
		QUESTION = qUESTION;
		OPTA = oPTA;
		OPTB = oPTB;
		OPTC = oPTC;
		OPTD = oPTD;
		ANSWER = aNSWER;
		TRIAL1 = "0";
		TRIAL2 = "0";
		TRIAL3 = "0";
		TRIAL4 = "0";
		IMAGE = iMAGE;
	}
	public int getID()
	{
		return ID;
	}
	public String getSUBJECT() {
		return SUBJECT;
	}
	public String getLEVEL() {
		return LEVEL;
	}
	public String getQUESTION() {
		return QUESTION;
	}
	public String getOPTA() {
		return OPTA;
	}
	public String getOPTB() {
		return OPTB;
	}
	public String getOPTC() {
		return OPTC;
	}
	public String getOPTD() {
		return OPTD;
	}
	public String getANSWER() {
		return ANSWER;
	}
	public String getTRIAL1() {
		return TRIAL1;
	}
	public String getTRIAL2() {
		return TRIAL2;
	}
	public String getTRIAL3() {
		return TRIAL3;
	}
	public String getTRIAL4() {
		return TRIAL4;
	}
	public String getIMAGE() {
		return IMAGE;
	}
	public void setID(int id)
	{
		ID=id;
	}
	public void setSUBJECT(String sUBJECT) {
		SUBJECT = sUBJECT;
	}
	public void setLEVEL(String lEVEL) {
		LEVEL = lEVEL;
	}
	public void setQUESTION(String qUESTION) {
		QUESTION = qUESTION;
	}
	public void setOPTA(String oPTA) {
		OPTA = oPTA;
	}
	public void setOPTB(String oPTB) {
		OPTB = oPTB;
	}
	public void setOPTC(String oPTC) {
		OPTC = oPTC;
	}
	public void setOPTD(String oPTD) {
		OPTD = oPTD;
	}
	public void setANSWER(String aNSWER) {
		ANSWER = aNSWER;
	}
	public void setTRIAL1(String tRIAL1) {
		TRIAL1 = tRIAL1;
	}
	public void setTRIAL2(String tRIAL2) {
		TRIAL2 = tRIAL2;
	}
	public void setTRIAL3(String tRIAL3) {
		TRIAL3 = tRIAL3;
	}
	public void setTRIAL4(String tRIAL4) {
		TRIAL4 = tRIAL4;
	}
	public void setIMAGE(String iMAGE) {
		IMAGE = iMAGE;
	}
	
}