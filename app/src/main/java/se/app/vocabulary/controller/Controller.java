package se.app.vocabulary.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import se.app.vocabulary.data.DatabaseHelper;
import se.app.vocabulary.model.Connection;
import se.app.vocabulary.model.Vocabulary;
import se.app.vocabulary.model.Words;

public class Controller {

    private final String LOG_TITLE = "Controller";
    Context context;
    DatabaseHelper dh;

    public Controller(Context context) {
        this.context = context;
        dh = new DatabaseHelper(context);
    }

    public int getTheNewID(String table){
        Cursor c = dh.getMaxValue(table);
        int newID = -1;
        if (c.getCount() == 0) {
            Log.e(LOG_TITLE, "Nem sikerült megtalálni az ID-t!");
        } else {
            while (c.moveToNext()) {
                newID = Integer.parseInt(c.getString(0));
            }
        }
        return newID;
    }

    public ArrayList<Vocabulary> getVocabularies() {
        ArrayList<Vocabulary> list = new ArrayList<>();
        Cursor c = dh.getDatas("SELECT * FROM " + dh.VOCABULARY);
        if(c.getCount() == 0) {
            Log.e(LOG_TITLE, "Sikertelen adatlekérdezés. (" + dh.VOCABULARY + ")");
        }
        else
        {
            while(c.moveToNext()){
                list.add(new Vocabulary(Integer.parseInt(c.getString(0)), c.getString(1)));
            }
        }
        return list;
    }

    public ArrayList<Words> getWords() {
        ArrayList<Words> list = new ArrayList<>();
        Cursor c = dh.getDatas("SELECT * FROM " + dh.WORDS);
        if(c.getCount() == 0) {
            Log.e(LOG_TITLE, "Sikertelen adatlekérdezés. (" + dh.WORDS + ")");
        }
        else
        {
            while(c.moveToNext()){
                list.add(new Words(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2)));
            }
        }
        return list;
    }

    public ArrayList<Words> getWords(int vocabularyID) {
        ArrayList<Words> list = new ArrayList<>();
        Cursor c = dh.getDatas("SELECT * FROM Words w JOIN Connection c ON c.WordID = w.ID WHERE c.VocabularyID = " + vocabularyID);

        if(c.getCount() == 0) {
            Log.e(LOG_TITLE, "Sikertelen adatlekérdezés. (" + dh.WORDS + ")");
        }
        else
        {
            while(c.moveToNext()){
                list.add(new Words(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2)));
            }
        }
        return list;
    }

    public ArrayList<Words> getData(String query) {
        ArrayList<Words> list = new ArrayList<>();
        Cursor c = dh.getDatas(query);

        if(c.getCount() == 0) {
            Log.e(LOG_TITLE, "Sikertelen adatlekérdezés. (" + dh.WORDS + ")");
        }
        else
        {
            while(c.moveToNext()){
                list.add(new Words(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2)));
            }
        }
        return list;
    }

    public ArrayList<Connection> getConnections() {
        ArrayList<Connection> list = new ArrayList<>();
        Cursor c = dh.getDatas("SELECT * FROM " + dh.CONNECT);
        if(c.getCount() == 0) {
            Log.e(LOG_TITLE, "Sikertelen adatlekérdezés. (" + dh.CONNECT + ")");
        }
        else
        {
            while(c.moveToNext()){
                list.add(new Connection(Integer.parseInt(c.getString(0)), Integer.parseInt(c.getString(1))));
            }
        }
        return list;
    }

}
