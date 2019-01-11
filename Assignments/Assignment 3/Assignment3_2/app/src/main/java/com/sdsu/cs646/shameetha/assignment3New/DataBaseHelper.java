package com.sdsu.cs646.shameetha.assignment3New;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shameetha on 3/14/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper sInstance;

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "professorRating";

    // Table Names
    private static final String TABLE_PROFESSOR = "PROFESSOR";
    private static final String TABLE_COMMENTS = "COMMENTS";

    // Common column names
    private static final String KEY_ID = "id";

    // PROFESSOR Table - column names
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_OFFICE = "office";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_AVERAGE = "average";
    private static final String KEY_TOTAL_RATINGS = "totalRatings";

    // COMMENTS Table - column names
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATE = "date";
    private static final String KEY_PROFESSOR_ID = "professor_id";

    // Table Create Statements

    // Professor table create statement
    private static final String CREATE_TABLE_PROFESSOR = "CREATE TABLE IF NOT EXISTS " + TABLE_PROFESSOR
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
            + KEY_FIRST_NAME + " TEXT,"
            + KEY_LAST_NAME + " TEXT,"
            + KEY_OFFICE + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_PHONE + " TEXT,"
            + KEY_AVERAGE + " FLOAT,"
            + KEY_TOTAL_RATINGS + " INTEGER"
            + " );";

    // Comments table create statement
    private static final String CREATE_TABLE_COMMENTS = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMENTS
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TEXT + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_PROFESSOR_ID + " INTEGER REFERENCES PROFESSOR(_id)"
            + " );";


    public static DataBaseHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_COMMENTS);
        db.execSQL(CREATE_TABLE_PROFESSOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFESSOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);

        // create new tables
        onCreate(db);
    }


    /*
 * getting all comments under single professor
 * */
    public List<Comments> getAllCommentsByProfessor(long selectedProfessorId) {
        List<Comments> commentsList = new ArrayList<Comments>();

        String selectQuery = "SELECT  * FROM "
                + TABLE_COMMENTS + " tc "
                + "WHERE tc." + KEY_PROFESSOR_ID + " = " + selectedProfessorId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Comments comments = new Comments();
                comments.setId(cursor.getInt((cursor.getColumnIndex(KEY_ID))));
                comments.setText((cursor.getString(cursor.getColumnIndex(KEY_TEXT))));
                comments.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));

                // adding to comments list
                commentsList.add(comments);
            } while (cursor.moveToNext());
        }

        cursor.close();
        closeDB();
        return commentsList;
    }

    /* get single professor
    */
    public Professor getProfessorDetails(long professor_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PROFESSOR + " WHERE "
                + KEY_ID + " = " + professor_id;

        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();

        Professor professor = new Professor();
        professor.setId(professor_id);
        professor.setFirstName((cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME))));
        professor.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
        professor.setOffice((cursor.getString(cursor.getColumnIndex(KEY_OFFICE))));
        professor.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
        professor.setEmail((cursor.getString(cursor.getColumnIndex(KEY_EMAIL))));
        professor.setAverage(cursor.getFloat(cursor.getColumnIndex(KEY_AVERAGE)));
        professor.setTotalRatings(cursor.getInt(cursor.getColumnIndex(KEY_TOTAL_RATINGS)));

        cursor.close();
        closeDB();
        return professor;
    }

    /**
     * getting all professor
     */
    public List<Professor> getAllProfessors() {
        List<Professor> professorArrayList = new ArrayList<Professor>();
        String selectQuery = "SELECT  * FROM " + TABLE_PROFESSOR;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Professor professor = new Professor();
                professor.setId(cursor.getInt((cursor.getColumnIndex(KEY_ID))));
                professor.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                professor.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                professor.setOffice(cursor.getString(cursor.getColumnIndex(KEY_OFFICE)));
                professor.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                professor.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
                professor.setAverage(cursor.getFloat(cursor.getColumnIndex(KEY_AVERAGE)));
                professor.setTotalRatings(cursor.getInt(cursor.getColumnIndex(KEY_TOTAL_RATINGS)));

                // adding to tags list
                professorArrayList.add(professor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return professorArrayList;
    }

    /**
     * inserting into PROFESSOR
     */
    public boolean insertProfessor(Professor professor) {

        SQLiteDatabase database = this.getWritableDatabase();
        int rowsAffected = 0;
        String[] Values = new String[1];
        String Where = "";
        try {
            database = this.getWritableDatabase();
            if (database.isOpen()) {
                database.beginTransaction();
                ContentValues myValues = new ContentValues();
                myValues.put(KEY_ID, professor.getId());
                myValues.put(KEY_FIRST_NAME, professor.getFirstName());
                myValues.put(KEY_LAST_NAME, professor.getLastName());
                Values[0] = String.valueOf(professor.getId());
                Where = KEY_ID + "= ?";
                rowsAffected = database.update(TABLE_PROFESSOR, myValues, Where, Values);
                // Check to see if anything was really updated
                if (rowsAffected == 0) {
                    //Insert row since it doesn't truly exist.
                    database.insert(TABLE_PROFESSOR, "", myValues);
                }
                database.setTransactionSuccessful();
                return true;
            } else {
                return true;
            }
        } catch (SQLiteException e) {
            Log.e("ProfessorDB", e.getMessage());
            return false;
        } finally {
            database.endTransaction();
            closeDB();
        }
    }

    /**
     * insert professor details
     */
    public boolean insertProfessorDetails(Professor professor) {

        SQLiteDatabase database = this.getWritableDatabase();
        String[] Values = new String[1];
        String Where = "";
        try {
            database = this.getWritableDatabase();
            if (database.isOpen()) {
                database.beginTransaction();
                ContentValues values = new ContentValues();
                values.put(KEY_OFFICE, professor.getOffice());
                values.put(KEY_EMAIL, professor.getEmail());
                values.put(KEY_PHONE, professor.getPhone());
                values.put(KEY_AVERAGE, professor.getAverage());
                values.put(KEY_TOTAL_RATINGS, professor.getTotalRatings());
                Values[0] = String.valueOf(professor.getId());
                Where = KEY_ID + "= ?";
                database.update(TABLE_PROFESSOR, values, Where, Values);
                database.setTransactionSuccessful();
                return true;
            } else {
                return true;
            }
        } catch (SQLiteException e) {
            Log.e("ProfessorDB", e.getMessage());
            return false;
        } finally {
            database.endTransaction();
            closeDB();
        }
    }

    /**
     * inserting into Comments Table
     */
    public boolean insertComments(Comments comments) {

        SQLiteDatabase database = this.getWritableDatabase();
        int rowsAffected = 0;
        String[] Values = new String[1];
        String Where = "";
        try {
            database = this.getWritableDatabase();
            if (database.isOpen()) {
                database.beginTransaction();
                ContentValues myValues = new ContentValues();
                myValues.put(KEY_TEXT, comments.getText());
                myValues.put(KEY_DATE, comments.getDate());
                myValues.put(KEY_PROFESSOR_ID, comments.getProfessorId());
                Values[0] = String.valueOf(comments.getId());
                Where = KEY_ID + "= ?";
                rowsAffected = database.update(TABLE_COMMENTS, myValues, Where, Values);
                // Check to see if anything was really updated
                if (rowsAffected == 0) {
                    //Insert row since it doesn't truly exist.
                    database.insert(TABLE_COMMENTS, "", myValues);
                }
                database.setTransactionSuccessful();
                return true;
            } else {
                return true;
            }
        } catch (SQLiteException e) {
            Log.e("ProfessorDB", e.getMessage());
            return false;
        } finally {
            database.endTransaction();
            closeDB();
        }
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}