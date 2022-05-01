package se.app.vocabulary.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final String LOG_TITLE = "DatabaseHelper";

    public final String WORDS = "Words";
    public final String CONNECT = "Connection";
    public final String VOCABULARY = "Vocabulary";


    public DatabaseHelper(Context context){
        super(context, "Words.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + WORDS + " (ID INTEGER Primary Key AUTOINCREMENT, English TEXT, Hungarian TEXT);");
        db.execSQL("CREATE TABLE " + CONNECT + " (WordID INT, VocabularyID INT, PRIMARY KEY( WordID, VocabularyID), " +
                "FOREIGN KEY (WordID) REFERENCES " +  WORDS + " (ID), FOREIGN KEY (VocabularyID) REFERENCES " +  VOCABULARY + " (ID) );");
        db.execSQL("CREATE TABLE " + VOCABULARY + " (ID INTEGER Primary Key AUTOINCREMENT, Name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE if exists " + CONNECT);
        db.execSQL("DROP TABLE if exists " + WORDS);
        db.execSQL("DROP TABLE if exists " + VOCABULARY);
    }


    private static boolean isNumeric(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public boolean insert(String table, String[] values) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(table.equals(WORDS)) {
            cv.put("English", values[0]);
            cv.put("Hungarian", values[1]);
        }
        else if(table.equals(CONNECT)) {
            cv.put("WordID", values[0]);
            cv.put("VocabularyID", values[1]);
        }
        else if(table.equals(VOCABULARY)) {
            cv.put("Name", values[0]);
        }
        else {
            Log.e(LOG_TITLE, "Ilyen tábla nem létezik!");
            return false;
        }

        long result = DB.insert(table, null, cv);

        if(result == -1) {
            Log.e(LOG_TITLE, "Nem sikerült feltölteni az adatokat! ( " + table + ")");
            return false;
        }
        else
        {
            Log.e(LOG_TITLE, "Az adat feltöltés sikeres! ( " + table + ")");
            return true;
        }
    }


    public boolean delete(String table, String first_column, String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(table,first_column + "=?", new String[] {row_id});


        if(result == -1){
            Log.e(LOG_TITLE,"Adat törlése NEM sikerült! (" + table + ")");
            return false;
        }
        else
        {
            Log.i(LOG_TITLE,"Adat feltöltése sikeres! (" + table + ")");
            return true;
        }
    }

    public boolean update(String table, String first_column, String[] values, String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(table.equals(WORDS)) {
            cv.put("English", values[0]);
            cv.put("Hungarian", values[1]);
        }
        else if(table.equals(CONNECT)) {
            cv.put("WordID", values[0]);
            cv.put("VocabularyID", values[1]);
        }
        else if(table.equals(VOCABULARY)) {
            cv.put("Name", values[0]);
        }
        else {
            Log.e(LOG_TITLE, "Ilyen tábla nem létezik!");
            return false;
        }

        long result = db.update(table, cv,first_column + "=?", new String[] {row_id});
        if(result == -1) {
            Log.e(LOG_TITLE,"Adat módosítása NEM sikerült! (" + table + ")");
            return false;
        }
        else {
            Log.i(LOG_TITLE,"Adat módosítása sikeres! (" + table + ")");
            return true;
        }
    }

    public Cursor getMaxValue(String table) {
        Cursor ID = null;
        String query = "SELECT MAX(ID) FROM " + table;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null) {
            ID = db.rawQuery(query, null);
        }
        return ID;
    }

    public Cursor getDatas(String query) {
        Log.i(LOG_TITLE, query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        if(db != null) {
            c = db.rawQuery(query, null);
        }
        return c;
    }


}
