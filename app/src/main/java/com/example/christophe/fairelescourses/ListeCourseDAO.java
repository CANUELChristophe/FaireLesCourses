package com.example.christophe.fairelescourses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Christophe on 07/11/2017.
 */

public final class ListeCourseDAO {

    public static final String TABLE_NAME = "LISTECOURSE";
    public static final String COLUMN_PRODUIT = "PRODUIT";
    public static final String COLUMN_QUANTITE = "QUANTITE";
    public static final String COLUMN_UNITE = "UNITE";
    public static final String COLUMN_CHECKBOX = "CHECKBOX";

    public static void insertRecord(SQLiteDatabase db, ListeCourse listeCourse) {
        db.execSQL("INSERT INTO "
                + TABLE_NAME      + "("
                + COLUMN_PRODUIT  + ","
                + COLUMN_QUANTITE + ","
                + COLUMN_UNITE    + ","
                + COLUMN_CHECKBOX + ") "
                + "VALUES ('"
                + listeCourse.getProduit()
                + "',"
                + listeCourse.getQuantite()
                + ",'"
                + listeCourse.getUnite()
                + "','"
                + CheckBoxBooleanToString(listeCourse.isEtat())
                + "')");
    }

    public static void updateRecord(SQLiteDatabase db, ListeCourse listeCourse) {
        db.execSQL("UPDATE "
                + TABLE_NAME
                + " SET "
                + COLUMN_QUANTITE + " = "  + listeCourse.getQuantite() + ", "
                + COLUMN_UNITE    + " = '" + listeCourse.getUnite()    + "', "
                + COLUMN_CHECKBOX + " = '" + CheckBoxBooleanToString(listeCourse.isEtat()) + "'"
                + " WHERE "
                + COLUMN_PRODUIT + " = '" + listeCourse.getProduit()+ "'");

        System.out.println("this.CheckBoxToString(listeCourse.isEtat()) " + CheckBoxBooleanToString(listeCourse.isEtat()));
    }

    public static void deleteRecordCHECKBOX(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_NAME + " where " + COLUMN_CHECKBOX + " = 'true'");
    }

    public static void deleteAllRecord(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_NAME);
    }

    public static ArrayList<ListeCourse> getAllRecordsCursorBAD(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<ListeCourse> listeCourse = new ArrayList<ListeCourse>();
        ListeCourse listeCourseLigne;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                listeCourseLigne = new ListeCourse(cursor.getString(0),cursor.getInt(1),cursor.getString(2),CheckBoxStringToBoolean(cursor.getString(3)));
                listeCourse.add(listeCourseLigne);
                System.out.println("cursor.getString(2) = " + cursor.getString(2));
            }
        }
        cursor.close();
        return listeCourse;
    }

    public static ArrayList<ListeCourse> getAllRecords(SQLiteDatabase db) {
        ArrayList<ListeCourse> listeCourse = new ArrayList<ListeCourse>();

        String query =
                "SELECT " + COLUMN_PRODUIT + ", "
                        + COLUMN_QUANTITE + ", "
                        + COLUMN_UNITE + ", "
                        + COLUMN_CHECKBOX
                        + " FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        ListeCourse listeCourseLigne = null;

        if (cursor.moveToFirst()) {
            do {
                listeCourseLigne = new ListeCourse(cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        CheckBoxStringToBoolean(cursor.getString(3)));
                listeCourse.add(listeCourseLigne);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listeCourse;
    }

    private static String CheckBoxBooleanToString (boolean checkBox) {
        if (checkBox == true) {
            return "true";
        }
        return "false";
    }

    private static boolean CheckBoxStringToBoolean (String checkBox) {
        if (checkBox == "true") {
            return true;
        }
        return false;
    }

}
