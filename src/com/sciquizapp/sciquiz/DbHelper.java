package com.sciquizapp.sciquiz;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DbHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "bioQuiz";
	// tasks table name
	private static final String TABLE_QUEST = "quest";
	// tasks Table Columns names for the question table
	private static final String KEY_ID = "id";
	private static final String KEY_SUBJECT = "subject";
	private static final String KEY_LEVEL = "level";
	private static final String KEY_QUES = "question";
	private static final String KEY_ANSWER = "answer"; //correct option
	private static final String KEY_OPTA= "opta"; //option a
	private static final String KEY_OPTB= "optb"; //option b
	private static final String KEY_OPTC= "optc"; //option c
	private static final String KEY_OPTD= "optd"; //option d
	private static final String KEY_IMAGE = "image";
	private static final String KEY_TRIAL1 = "questscore1"; //good answer on first trial
	private static final String KEY_TRIAL2 = "questscore2";
	private static final String KEY_TRIAL3 = "questscore3";
	private static final String KEY_TRIAL4 = "questscore4";

	// tasks table name
	private static final String TABLE_SCORES = "scores";
	// tasks Table Columns names for the score table
	private static final String KEY_IDscores = "idscores";
	private static final String KEY_TIME = "time";
	private static final String KEY_SUBJECTscores = "subjectscores";
	private static final String KEY_SCORE = "score";
	private static final String KEY_LEVELscore = "levelscore";

	// tasks table settings
	private static final String TABLE_SETTINGS = "settings";
	// tasks Table Columns names for the settings table
	private static final String KEY_IDsettings = "idsettings";
	private static final String KEY_NAME = "name";
	

	private SQLiteDatabase dbase;
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		dbase=db;
		//add table of questions
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_SUBJECT + " TEXT, "+ KEY_LEVEL + " TEXT, " +
				KEY_QUES + " TEXT, " + KEY_ANSWER+ " TEXT, "+KEY_OPTA +" TEXT, "
				+KEY_OPTB +" TEXT, "+KEY_OPTC+" TEXT, "+KEY_OPTD +" TEXT, "+KEY_TRIAL1 +" TEXT, "
				+KEY_TRIAL2 +" TEXT, "+KEY_TRIAL3+" TEXT, "+KEY_TRIAL4 +" TEXT,"+KEY_IMAGE+" TEXT)";
		db.execSQL(sql);		
		addQuestions();

		//add table for scores
		String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_SCORES + " ( "
				+ KEY_IDscores + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TIME + " TEXT, "+ KEY_SUBJECTscores + " TEXT, " +
				KEY_SCORE + " TEXT, " +	KEY_LEVELscore +" TEXT)";
		db.execSQL(sql2);

		//add table for scores
		String sql3 = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTINGS + " ( "
				+ KEY_IDsettings + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME +" TEXT)";
		db.execSQL(sql3);
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, "Anonyme");
		// Inserting of Replacing Row
		dbase.insert(TABLE_SETTINGS, null, values);
		//db.close();
	}
	//add new name
	public void addName(String newname)
	{
		//SQLiteDatabase db = this.getWritableDatabase();
		dbase=this.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, newname);
		// Replacing Row
		dbase.update(TABLE_SETTINGS, values, null, null);
	}
	//get name from db
	public String getName() {
		// Select All Query
		String name = "";
		String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		if (cursor.moveToPosition(0)) { 
			name = cursor.getString(1);
		}
		// return string name
		return name;
	}
	//add new score
	public void addScore(Score newscore)
	{
		//SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TIME, newscore.getTIME());
		values.put(KEY_SUBJECTscores, newscore.getSUBJECTscores());
		values.put(KEY_SCORE, newscore.getSCORE());
		values.put(KEY_LEVELscore, newscore.getLEVEL());
		// Inserting Row
		dbase.insert(TABLE_SCORES, null, values);
	}
	public List<Score> getScoresFromSubject(String subjectArg) {
		List<Score> scoreList = new ArrayList<Score>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SCORES;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToPosition(0)) {
			do {
				if (subjectArg.equals(cursor.getString(2))) {   
					Score score = new Score();
					score.setIDscores(cursor.getInt(0));
					score.setTIME(cursor.getString(1));
					score.setSUBJECTscores(cursor.getString(2));
					score.setSCORE(cursor.getString(3));
					score.setLEVEL(cursor.getString(4));
					scoreList.add(score);
				}
			} while (cursor.moveToNext());
		}
		// return quest list
		return scoreList;
	}
	public List<Score> getScores() {
		List<Score> scoreList = new ArrayList<Score>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SCORES;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToPosition(0)) {
			do {   
				Score score = new Score();
				score.setIDscores(cursor.getInt(0));
				score.setTIME(cursor.getString(1));
				score.setSUBJECTscores(cursor.getString(2));
				score.setSCORE(cursor.getString(3));
				score.setLEVEL(cursor.getString(4));
				scoreList.add(score);
			} while (cursor.moveToNext());
		}
		// return quest list
		return scoreList;
	}

	private void addQuestions()
	{
		Question q0=new Question("système cardiovasculaire/forces/immunité et pathogènes","1","0","0","0","0", "0", "0","none");
		this.addQuestion(q0);
		Question q1=new Question("système cardiovasculaire","2","Quel gaz contient le sang qui circule dans" +
				" la partie gauche du coeur?","azote","oxygène","gaz hilarant", "gaz carbonique", "oxygène","drawable/circulation2");
		this.addQuestion(q1);
		Question q2=new Question("système cardiovasculaire","2","Quel gaz contient le sang qui circule dans" +
				" l'artère pulmonaire?","protoxyde d'azote","oxygène","vapeur d'eau", "gaz carbonique", "gaz carbonique","drawable/circulation2");
		this.addQuestion(q2);
		Question q3=new Question("système cardiovasculaire","2","Quel gaz contient le sang qui circule dans" +
				" les veines pulmonaires?","azote","oxygène","argon", "gaz carbonique", "oxygène","drawable/circulation2");
		this.addQuestion(q3);
		Question q4=new Question("système cardiovasculaire","2","Quel gaz contient le sang qui circule dans" +
				" l'aorte?","azote","oxygène","gaz hilarant", "gaz carbonique", "oxygène","drawable/circulation2");
		this.addQuestion(q4);
		//Question q5=new Question("système cardiovasculaire","2","Que se passe-t-il dans les poumons?",
		//		"Le gaz carbonique du sang est échangé contre de l'oxygène","L'oxygène du sang est échangé contre du gaz carbonique","La respiration cellulaire 'donne' de l'oxygène au sang et 'prend' du gaz carbonique", "La respiration cellulaire 'donne' du gaz carbonique au sang et 'prend' de l'oxygène", "Le gaz carbonique du sang est échangé contre de l'oxygène","drawable/circulation2");
		//this.addQuestion(q5);
		//Question q6=new Question("système cardiovasculaire","2","Que se passe-t-il dans les muscles?",
		//		"Le gaz carbonique du sang est échangé contre de l'oxygène","La photosynthèse 'donne' de l'oxygène au sang et 'prend' du gaz carbonique","La respiration cellulaire 'prend' du gaz carbonique du sang et 'donne' de l'oxygène", "La respiration cellulaire 'prend' de l'oxygène du sang et 'donne' du gaz carbonique", "La respiration cellulaire 'prend' de l'oxygène du sang et 'donne' du gaz carbonique","drawable/circulation2");
		//this.addQuestion(q6);
		Question q7=new Question("système cardiovasculaire","1","Que représente le numéro 1 sur cette image?","coeur","poumons","muscle", "intestin grêle", "coeur","drawable/circulation1");
		this.addQuestion(q7);
		Question q8=new Question("système cardiovasculaire","1","Que représente le numéro 2 sur cette image?","coeur","poumons","muscle", "intestin grêle", "poumons","drawable/circulation1");
		this.addQuestion(q8);
		Question q9=new Question("système cardiovasculaire","1","Que représente le numéro 3 sur cette image?","cerveau","poumons","muscle", "intestin grêle", "cerveau","drawable/circulation1");
		this.addQuestion(q9);
		Question q10=new Question("système cardiovasculaire","1","Quel numéro n'est pas alimenté par la grande circulation?","1","2","3","4", "2","drawable/circulation1");
		this.addQuestion(q10);
		Question q11=new Question("système cardiovasculaire","1","Quel gaz contient le sang qui circule dans" +
				" la partie droite du coeur?","azote","oxygène","gaz hilarant", "gaz carbonique", "gaz carbonique","drawable/circulation2");
		this.addQuestion(q11);
		Question q12=new Question("système cardiovasculaire","1","Quel élément de la respiration cellulaire est symbolisé par la lettre A","oxygène","gaz carbonique","lumière","eau", "oxygène","drawable/respiration_cellulaire");
		this.addQuestion(q12);
		Question q13=new Question("système cardiovasculaire","1","Quel élément de la respiration cellulaire est symbolisé par la lettre B","oxygène","gaz carbonique","lumière","eau", "gaz carbonique","drawable/respiration_cellulaire");
		this.addQuestion(q13);
		Question q14=new Question("système cardiovasculaire","1","Que représente le numéro 1 sur cette image du coeur?","oreillette droite","oreillette gauche","ventricule droit","ventricule gauche", "oreillette droite","drawable/schema_coeur");
		this.addQuestion(q14);
		Question q15=new Question("système cardiovasculaire","1","Que représente le numéro 2 sur cette image du coeur?","oreillette droite","oreillette gauche","ventricule droit","ventricule gauche", "ventricule droit","drawable/schema_coeur");
		this.addQuestion(q15);
		Question q16=new Question("système cardiovasculaire","1","Que représente le numéro 3 sur cette image du coeur?","oreillette droite","oreillette gauche","ventricule droit","ventricule gauche", "oreillette gauche","drawable/schema_coeur");
		this.addQuestion(q16);
		Question q17=new Question("système cardiovasculaire","1","Que représente le numéro 4 sur cette image du coeur?","oreillette droite","oreillette gauche","ventricule droit","ventricule gauche", "ventricule gauche","drawable/schema_coeur");
		this.addQuestion(q17);
		Question q18=new Question("système cardiovasculaire","1","Que désigne le numéro 1 sur cette image?","veine","capillaires","artère","valvule", "veine","drawable/capillaires");
		this.addQuestion(q18);
		Question q19=new Question("système cardiovasculaire","1","Que désigne le numéro 2 sur cette image?","veine","capillaires","artère","valvule", "capillaires","drawable/capillaires");
		this.addQuestion(q19);
		Question q20=new Question("système cardiovasculaire","1","Que désigne le numéro 3 sur cette image?","veine","capillaires","artère","valvule", "artère","drawable/capillaires");
		this.addQuestion(q20);
		Question q21=new Question("système cardiovasculaire","1","Les artères ...","... partent du coeur","... arrivent au coeur","... contiennent du sang oxygéné","... contiennent du sang avec du gaz carbonique", "... partent du coeur","none");
		this.addQuestion(q21);
		Question q22=new Question("système cardiovasculaire","2","Laquelle de ces affirmations est exacte?","Les veines partent du coeur","Les artères contiennent des valvules","La paroi des veines est plus mince que celle des artères","Les capillaires ne se trouvent que dans les poumons", "La paroi des veines est plus mince que celle des artères","none");
		this.addQuestion(q22);
		Question q23=new Question("système cardiovasculaire","3","Que représente la zone du graphique désignée par la lettre B","la systole auriculaire","la systole ventriculaire","la diastole auriculaire et ventriculaire","la diastole ventriculaire", "la systole ventriculaire","drawable/ecg");
		this.addQuestion(q23);
		Question q24=new Question("système cardiovasculaire","3","Que représente la zone du graphique désignée par la lettre A","la systole auriculaire","la systole ventriculaire","la diastole auriculaire et ventriculaire","la diastole ventriculaire", "la systole auriculaire","drawable/ecg");
		this.addQuestion(q24);
		Question q25=new Question("système cardiovasculaire","3","Que représente la zone du graphique désignée par la lettre C","la systole auriculaire","la systole ventriculaire","la diastole auriculaire et ventriculaire","la diastole ventriculaire", "la diastole auriculaire et ventriculaire","drawable/ecg");
		this.addQuestion(q25);
		Question q26=new Question("système cardiovasculaire","3","Que représente ce graphique?","La fréquence cardiaque d'un coureur en fonction de sa vitesse","La vitesse d'un coureur en fonction de sa fréquence cardiaque","La fréquence cardiaque d'un coureur en fonction du temps","La vitesse d'un coureur en fonction de la distance", "La fréquence cardiaque d'un coureur en fonction de sa vitesse","drawable/fc");
		this.addQuestion(q26);
		Question q27=new Question("système cardiovasculaire","3","À quelle vitesse le coureur représenté par la courbe rouge atteint-il sa fréquence cardiaque maximale?","11 km/h","8 km/h","10 km/h","20 km/h", "11 km/h","drawable/fc");
		this.addQuestion(q27);
		Question q28=new Question("système cardiovasculaire","3","Quelle courbe représente le coureur le mieux entraîné?","La courbe rouge","La courbe bleue","La courbature","La courbe de croissance", "La courbe bleue","drawable/fc");
		this.addQuestion(q28);
		Question q29=new Question("système cardiovasculaire","1","Que représente le numéro 1 sur ce schéma de la circulation sanguine?","Les poumons","La partie droite du coeur","La partie gauche du coeur", "Les autres organes (muscles, cerveau, etc.)","Les poumons","drawable/circul_schema_simple");
		this.addQuestion(q29);
		Question q30=new Question("système cardiovasculaire","1","Que représente le numéro 2 sur ce schéma de la circulation sanguine?","Les poumons","La partie droite du coeur","La partie gauche du coeur", "Les autres organes (muscles, cerveau, etc.)","Les autres organes (muscles, cerveau, etc.)","drawable/circul_schema_simple");
		this.addQuestion(q30);
		Question q31=new Question("système cardiovasculaire","1","Que représente le numéro 3 sur ce schéma de la circulation sanguine?","Les poumons","La partie droite du coeur","La partie gauche du coeur", "Les autres organes (muscles, cerveau, etc.)","La partie droite du coeur","drawable/circul_schema_simple");
		this.addQuestion(q31);
		Question q32=new Question("système cardiovasculaire","1","Que représente le numéro 4 sur ce schéma de la circulation sanguine?","Les poumons","La partie droite du coeur","La partie gauche du coeur", "Les autres organes (muscles, cerveau, etc.)","La partie gauche du coeur","drawable/circul_schema_simple");
		this.addQuestion(q32);
		Question q33=new Question("système cardiovasculaire","2","Que représente le numéro 2 sur ce schéma de la circulation sanguine?","Les poumons","La partie droite du coeur","La partie gauche du coeur", "Les autres organes (muscles, cerveau, etc.)","Les poumons","drawable/circul_schema_simple2");
		this.addQuestion(q33);
		Question q34=new Question("système cardiovasculaire","2","Que représente le numéro 3 sur ce schéma de la circulation sanguine?","Les poumons","La partie droite du coeur","La partie gauche du coeur", "Les autres organes (muscles, cerveau, etc.)","La partie gauche du coeur","drawable/circul_schema_simple2");
		this.addQuestion(q34);
		Question q35=new Question("système cardiovasculaire","1","La diastole correspond...","à la contraction des muscles du coeur","au relâchement des muscles du coeur","-", "-","au relâchement des muscles du coeur","drawable/circul_schema_simple2");
		this.addQuestion(q35);
		Question q36=new Question("système cardiovasculaire","1","La systole correspond...","à la contraction des muscles du coeur","au relâchement des muscles du coeur","-", "-","à la contraction des muscles du coeur","drawable/circul_schema_simple2");
		this.addQuestion(q36);
		//Question el1=new Question("électricité","1","Qu'est-ce qui crée le courant électrique?","Le mouvement d'électrons","Le mouvement de protons","Le mouvement d'atomes de métal", "Le mouvement d'atomes d'oxygène","Le mouvement d'électrons","none");
		//this.addQuestion(el1);
		Question f1=new Question("forces","1","Quelle est l'unité de la force?","Ampère (A)","kg/l","Newton (N)", "Joule (J)","Newton (N)","");
		this.addQuestion(f1);
		Question f2=new Question("forces","1","Quelle est la formule pour calculer la force de pesanteur exercée par la terre sur un objet de masse m?","F = m * g","g = m * F","F = m / V", "F = g / m","F = m * g","");
		this.addQuestion(f2);
		Question f3=new Question("forces","1","Dans la formule: F = m * g, m est ...","la masse de l'objet","le volume de l'objet","la gravitation", "la masse de la terre","la masse de l'objet","");
		this.addQuestion(f3);
		Question f4=new Question("forces","1","Dans la formule: F = m * g, g est égal à","9,81 N/kg","9,81 N","6,81 N/kg", "6,81 N","9,81 N/kg","");
		this.addQuestion(f4);
		Question f5=new Question("forces","1","Sur l'image suivante, que représente la force rouge?","la force musculaire","la force de gravitation","la force d'Archimède", "la force de réaction du sol","la force musculaire","drawable/halterophile");
		this.addQuestion(f5);
		Question f6=new Question("forces","1","Sur l'image suivante, que représente la force verte?","la force musculaire","la force de gravitation","la force d'Archimède", "la force de réaction du sol","la force de gravitation","drawable/halterophile");
		this.addQuestion(f6);
		Question f7=new Question("forces","1","La force de pesanteur est aussi appelée:","poids","masse","force de frottement", "force électromagnétique","poids","");
		this.addQuestion(f7);
		Question f8=new Question("forces","1","La longueur de la force F1 représente:","son intensité","son point d'application","sa direction", "son sens","son intensité","drawable/bloc_table");
		this.addQuestion(f8);
		Question f9=new Question("forces","1","la droite d'action d'une force indique:","son intensité","son point d'application","sa direction", "son sens","sa direction","drawable/bloc_table");
		this.addQuestion(f9);
		Question f10=new Question("forces","1","Laquelle de ces forces agit verticalement sur la montgolfière?","la force de pesanteur","la force électromagnétique","la force musculaire", "la force de frottement","la force de pesanteur","drawable/montgolfiere");
		this.addQuestion(f10);
		Question f11=new Question("forces","1","Laquelle de ces forces agit verticalement sur la montgolfière?","la force d'Archimède","la force électromagnétique","la force musculaire", "la force de frottement","la force d'Archimède","drawable/montgolfiere");
		this.addQuestion(f11);
		Question f12=new Question("forces","1","Où aurez-vous la masse la plus grande?","sur la terre","sur la lune","dans l'espace", "votre masse est la même partout","votre masse est la même partout","");
		this.addQuestion(f12);
		Question f13=new Question("forces","1","Où aurez-vous le poids le plus grand?","sur la terre","sur la lune","dans l'espace", "votre poids est le même partout","sur la terre","");
		this.addQuestion(f13);
		Question f14=new Question("forces","1","Quelle force s'exerce verticalement sur le bateau?","force électromagnétique","force de frottement","force musculaire", "force de pesanteur","force de pesanteur","drawable/bateau");
		this.addQuestion(f14);
		Question f15=new Question("forces","1","Quelle force s'exerce verticalement sur le bateau?","force électromagnétique","force de frottement","force musculaire", "force d'Archimède","force d'Archimède","drawable/bateau");
		this.addQuestion(f15);
		Question f16=new Question("forces","1","Quelle caractéristique n'est pas la même dans ces deux forces?","l'intensité","le point d'application","la direction", "le sens","le sens","drawable/bloc_table");
		this.addQuestion(f16);
		Question f17=new Question("forces","2","Quelle force représentée sur cette image est la force d'Archimède?","F1","F2","F3", "F4","F1","drawable/bateau_forces");
		this.addQuestion(f17);
		Question f18=new Question("forces","2","Quelle force représentée sur cette image est le poids du bateau?","F1","F2","F3", "F4","F3","drawable/bateau_forces");
		this.addQuestion(f18);
		Question f19=new Question("forces","2","Quelle force représentée sur cette image est la force de frottement?","F1","F2","F3", "F4","F4","drawable/bateau_forces");
		this.addQuestion(f19);
		Question f20=new Question("forces","2","Quelle caractéristique n'est pas la même dans ces deux forces?","l'intensité","le point d'application","la direction", "le sens","le sens","drawable/bloc_table");
		this.addQuestion(f20);
		Question f21=new Question("forces","2","Sur un objet qui tombe, la force de frottement qui vient de la résistance de l'air agit...","dans le même sens que la chute ","dans le sens opposé à la chute","perpendiculairement à la chute", "n'agit pas sur l'objet","dans le sens opposé à la chute","");
		this.addQuestion(f21);
		Question f22=new Question("forces","2","La force d'Archimède agit sur un objet...","uniquement lorsqu'il est plongé dans un liquide","uniquement lorsqu'il est plongé dans un gaz","lorsqu'il est plongé dans un liquide ou un gaz", "lorsqu'il est posé sur le sol","lorsqu'il est plongé dans un liquide ou un gaz","");
		this.addQuestion(f22);
		Question f23=new Question("forces","3","Un bateau qui passe d'un fleuve à la mer remonte légèrement hors de l'eau. Pourquoi?","La masse du bateau est plus petite dans la mer","Le volume du bateau a augmenté","La masse volumique de l'eau salée est plus grande que celle de l'eau douce", "La masse volumique du bateau est plus grande dans l'eau douce que dans l'eau salée","La masse volumique de l'eau salée est plus grande que celle de l'eau douce","");
		this.addQuestion(f23);
		Question f24=new Question("forces","3","Sur la terre, le poids d'un objet de 10 kg est environ égal à:","10 N","50 N","1N", "100 N","100 N","");
		this.addQuestion(f24);
		Question f25=new Question("forces","3","Un objet est totalement immergé dans un liquide. Si son volume augmente, la force d'Archimède qui s'exerce sur lui...","diminue","augmente","reste la même", "-","augmente","");
		this.addQuestion(f25);
		Question f26=new Question("forces","2","La force d'Archimède qui agit sur un objet plongé dans un liquide dépend...","du volume total de l'objet","du volume immergé de l'objet","de la masse de l'objet", "du volume de liquide","du volume immergé de l'objet","");
		this.addQuestion(f26);
		Question f27=new Question("forces","3","Sur la lune, g égal environ 1,5N/kg. Le poids d'un objet de 10 kg y est environ égal à:","100 N","15 N","1,5N", "150 N","15 N","");
		this.addQuestion(f27);
		Question f28=new Question("forces","3","Si l'on suspend un objet à un dynamomètre, on pourra y lire:","sa masse","son poids","son volume", "sa longueur","son poids","");
		this.addQuestion(f28);
		Question f29=new Question("forces","3","Pour freiner une voiture, il faut...","appliquer une force dans le sens inverse de son déplacement","juste supprimer la force du moteur","que la force de gravitation augmente", "appliquer une force verticalement","appliquer une force dans le sens inverse de son déplacement","");
		this.addQuestion(f29);
		Question i1=new Question("immunité et pathogènes","1","Si le 2 est une bactérie, que représente numéro 3?","un virus","un parasite unicellulaire","un globule blanc", "un globule rouge","un virus","drawable/tailles_pathogenes");
		this.addQuestion(i1);
		Question i2=new Question("immunité et pathogènes","1","Quel élément est présent dans tous ces agents pathogènes: virus, bactérie, parasite unicellulaire, champignon","de l'ADN","un noyau","un cytoplasme", "une membrane plasmique","de l'ADN","");
		this.addQuestion(i2);
		Question i3=new Question("immunité et pathogènes","1","Quel type d'agents pathogènes provoque le paludisme","un parasite unicellulaire","un bactérie","un virus", "un champignon","un parasite unicellulaire","");
		this.addQuestion(i3);
		Question i4=new Question("immunité et pathogènes","1","Lequel de ces ennemis du corps n'a besoin de traverser aucune barrière du corps pour être dangereux?","une cellule cancéreuse","une bactérie","un virus", "un champignon","une cellule cancéreuse","");
		this.addQuestion(i4);
		Question i5=new Question("immunité et pathogènes","1","Quelle famille de cellules est responsable de la défense du corps?","les globules blancs","les globules rouges","les entérocytes", "les neurones","les globules blancs","");
		this.addQuestion(i5);
		Question i6=new Question("immunité et pathogènes","1","Quelle étape des défenses du corps intervient si vous êtes en contact avec le virus de la grippe alors que vous avez déjà été malade de la grippe cet hiver?","l'immunité acquise/adaptative","l'immunité innée","les barrières physiques", "l'immunité non spécifique","l'immunité acquise/adaptative","");
		this.addQuestion(i6);
		Question i7=new Question("immunité et pathogènes","1","Laquelle des défenses suivantes ne fait pas partie de l'immunité innée?","les lymphocytes","les phagocytes","les cellules tueuses naturelles", "la réaction inflammatoire","les lymphocytes","");
		this.addQuestion(i7);
		Question i8=new Question("immunité et pathogènes","1","Comment les phagocytes combattent-ils les agents pathogènes?","en les absorbant et en les digérant","en libérant des anticorps","en s'y collant pour y injecter un produit chimique", "-","en les absorbant et en les digérant","");
		this.addQuestion(i8);
		Question i9=new Question("immunité et pathogènes","1","Lequel de ces globules blancs libère des anticorps?","le lymphocyte B","le lymphocyte T","le phagocyte", "la cellule tueuse naturelle","le lymphocyte B","");
		this.addQuestion(i9);
		Question i10=new Question("immunité et pathogènes","1","Lequel de ces globules blancs libère des anticorps?","le lymphocyte B","le lymphocyte T","le phagocyte", "la cellule tueuse naturelle","le lymphocyte B","");
		this.addQuestion(i10);
		Question i11=new Question("immunité et pathogènes","1","Lequel de ces globules blancs libère des anticorps?","le lymphocyte B","le lymphocyte T","le phagocyte", "la cellule tueuse naturelle","le lymphocyte B","");
		this.addQuestion(i11);
		Question i12=new Question("immunité et pathogènes","1","Lequel de ces globules blancs libère des anticorps?","le lymphocyte B","le lymphocyte T","le phagocyte", "la cellule tueuse naturelle","le lymphocyte B","");
		this.addQuestion(i12);
		Question i13=new Question("immunité et pathogènes","1","Lequel de ces globules blancs libère des anticorps?","le lymphocyte B","le lymphocyte T","le phagocyte", "la cellule tueuse naturelle","le lymphocyte B","");
		this.addQuestion(i13);
		Question i14=new Question("immunité et pathogènes","1","Lequel de ces globules blancs libère des anticorps?","le lymphocyte B","le lymphocyte T","le phagocyte", "la cellule tueuse naturelle","le lymphocyte B","");
		this.addQuestion(i14);
		Question i15=new Question("immunité et pathogènes","1","Lequel de ces globules blancs libère des anticorps?","le lymphocyte B","le lymphocyte T","le phagocyte", "la cellule tueuse naturelle","le lymphocyte B","");
		this.addQuestion(i15);
		Question i16=new Question("immunité et pathogènes","1","Lequel de ces globules blancs libère des anticorps?","le lymphocyte B","le lymphocyte T","le phagocyte", "la cellule tueuse naturelle","le lymphocyte B","");
		this.addQuestion(i16);
		Question co1=new Question("chimie organique","1","Combien de produits différends obtient-on par la monochloration du 2-méthylbutane?","1","2","4", "5","4","");
		this.addQuestion(co1);
		Question co2=new Question("chimie organique","1","Quel est le produit principal de la réaction de l'acide bromhyrique sur du 2-méthylbut-2-ène?","2-bromo-2-méthylbutane","2-bromo-3-méthylbutane","3-bromo-3-méthylbutane", "3-bromo-2-méthylbutane","2-bromo-2-méthylbutane","");
		this.addQuestion(co2);
		Question co3=new Question("chimie organique","1","Quel métal est utilisé pour le couplage de Fukuyama?","Pd","Ru","Cu", "Mo","Pd","");
		this.addQuestion(co3);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
		// Create tables again
		onCreate(db);
	}
	// Adding new question
	public void addQuestion(Question quest) {
		//SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SUBJECT, quest.getSUBJECT());
		values.put(KEY_LEVEL, quest.getLEVEL());
		values.put(KEY_QUES, quest.getQUESTION()); 
		values.put(KEY_ANSWER, quest.getANSWER());
		values.put(KEY_OPTA, quest.getOPTA());
		values.put(KEY_OPTB, quest.getOPTB());
		values.put(KEY_OPTC, quest.getOPTC());
		values.put(KEY_OPTD, quest.getOPTD());
		values.put(KEY_TRIAL1, "0");
		values.put(KEY_TRIAL2, "0");
		values.put(KEY_TRIAL3, "0");
		values.put(KEY_TRIAL4, "0");
		values.put(KEY_IMAGE, quest.getIMAGE());
		// Inserting Row
		dbase.insert(TABLE_QUEST, null, values);		
	}
	public List<Question> getAllQuestions() {
		List<Question> quesList = new ArrayList<Question>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToPosition(1)) {
			do {
				Question quest = new Question();
				quest.setID(cursor.getInt(0));
				quest.setSUBJECT(cursor.getString(1));
				quest.setLEVEL(cursor.getString(2));
				quest.setQUESTION(cursor.getString(3));
				quest.setANSWER(cursor.getString(4));
				quest.setOPTA(cursor.getString(5));
				quest.setOPTB(cursor.getString(6));
				quest.setOPTC(cursor.getString(7));
				quest.setOPTD(cursor.getString(8));
				quest.setTRIAL1(cursor.getString(9));
				quest.setTRIAL2(cursor.getString(10));
				quest.setTRIAL3(cursor.getString(11));
				quest.setTRIAL4(cursor.getString(12));
				quest.setIMAGE(cursor.getString(13));  //13, because the trials are between OPTD and IMAGE
				quesList.add(quest);
			} while (cursor.moveToNext());
		}
		// return quest list
		return quesList;
	}
	public List<Question> getQuestionsFromSubject(String subjectArg) {
		List<Question> quesList = new ArrayList<Question>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToPosition(1)) {
			do {
				if (subjectArg.equals(cursor.getString(1))) {
					Question quest = new Question();
					quest.setID(cursor.getInt(0));
					quest.setSUBJECT(cursor.getString(1));
					quest.setLEVEL(cursor.getString(2));
					quest.setQUESTION(cursor.getString(3));
					quest.setANSWER(cursor.getString(4));
					quest.setOPTA(cursor.getString(5));
					quest.setOPTB(cursor.getString(6));
					quest.setOPTC(cursor.getString(7));
					quest.setOPTD(cursor.getString(8));
					quest.setTRIAL1(cursor.getString(9));
					quest.setTRIAL2(cursor.getString(10));
					quest.setTRIAL3(cursor.getString(11));
					quest.setTRIAL4(cursor.getString(12));
					quest.setIMAGE(cursor.getString(13)); //13, because the trials are between OPTD and IMAGE
					quesList.add(quest);
				}
			} while (cursor.moveToNext());
		}
		// return quest list
		return quesList;
	}
	public List<Question> getQuestionsFromSubjectAndLevel(String subjectArg, int levelArg) {
		List<Question> quesList = new ArrayList<Question>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToPosition(1)) {
			do {
				if (subjectArg.equals(cursor.getString(1)) && levelArg == Integer.parseInt(cursor.getString(2))) {
					Question quest = new Question();
					quest.setID(cursor.getInt(0));
					quest.setSUBJECT(cursor.getString(1));
					quest.setLEVEL(cursor.getString(2));
					quest.setQUESTION(cursor.getString(3));
					quest.setANSWER(cursor.getString(4));
					quest.setOPTA(cursor.getString(5));
					quest.setOPTB(cursor.getString(6));
					quest.setOPTC(cursor.getString(7));
					quest.setOPTD(cursor.getString(8));
					quest.setTRIAL1(cursor.getString(9));
					quest.setTRIAL2(cursor.getString(10));
					quest.setTRIAL3(cursor.getString(11));
					quest.setTRIAL4(cursor.getString(12));
					quest.setIMAGE(cursor.getString(13)); //13, because the trials are between OPTD and IMAGE
					quesList.add(quest);
				}
			} while (cursor.moveToNext());
		}
		// return quest list
		return quesList;
	}
	public String getSubjects() {
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);

		String dbSubjects = "";
		if (cursor.moveToPosition(0)) {
			dbSubjects = cursor.getString(1);
		}

		return dbSubjects;
	}
	public void incrementTrialNFromQuestion(int trial, Question question) {
		dbase=this.getReadableDatabase();
		int questionID = question.getID();
		ContentValues values = new ContentValues();
		if (trial == 1) {
			int trialTotal = Integer.parseInt(question.getTRIAL1());
			trialTotal++;
			values.put(KEY_TRIAL1, trialTotal);
			String[] questionIDArray = new String[1];
			questionIDArray[0] = Integer.toString(questionID);
			dbase.update(TABLE_QUEST, values, "id = ?", questionIDArray);
		} else if (trial == 2) {
			int trialTotal = Integer.parseInt(question.getTRIAL2());
			trialTotal++;
			values.put(KEY_TRIAL2, trialTotal);
			String[] questionIDArray = new String[1];
			questionIDArray[0] = Integer.toString(questionID);
			dbase.update(TABLE_QUEST, values, "id = ?", questionIDArray);
		} else if (trial == 3) {
			int trialTotal = Integer.parseInt(question.getTRIAL3());
			trialTotal++;
			values.put(KEY_TRIAL3, trialTotal);
			String[] questionIDArray = new String[1];
			questionIDArray[0] = Integer.toString(questionID);
			dbase.update(TABLE_QUEST, values, "id = ?", questionIDArray);
		} else if (trial == 4) {
			int trialTotal = Integer.parseInt(question.getTRIAL4());
			trialTotal++;
			values.put(KEY_TRIAL4, trialTotal);
			String[] questionIDArray = new String[1];
			questionIDArray[0] = Integer.toString(questionID);
			dbase.update(TABLE_QUEST, values, "id = ?", questionIDArray);
		}
	}
	public int rowcount()
	{
		int row=0;
		String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		row=cursor.getCount();
		return row;
	}
	
}
