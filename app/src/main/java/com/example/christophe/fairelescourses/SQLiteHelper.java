package com.example.christophe.fairelescourses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by christophe on 05/11/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "ListeDesCourses.db";

    private SQLiteDatabase database;

    public static final String TABLE_NAME = "LISTECOURSE";
    public static final String COLUMN_PRODUIT = "PRODUIT";
    public static final String COLUMN_QUANTITE = "QUANTITE";
    public static final String COLUMN_UNITE = "UNITE";
    public static final String COLUMN_CHECKBOX = "CHECKBOX";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COLUMN_PRODUIT + " VARCHAR," + COLUMN_QUANTITE + " REAL," + COLUMN_UNITE + " INTEGER, " + COLUMN_CHECKBOX + " VARCHAR);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}