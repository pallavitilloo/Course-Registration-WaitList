package info.tilloop1.courseregistration.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.tilloop1.courseregistration.database.model.Student;

/**************************************************************************************************
 * Class Database Helper provides methods to create and update database tables and columns
 * Created By: Pallavi Tilloo
 * Dt: 04/22/2021
 *************************************************************************************************/

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "waiting_list_db";

    // Constructors
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Waiting list table
        db.execSQL(Student.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**********************************************************************************************
     * Inserts new name into the Waiting list
     * @param entry: Entry for the student to be inserted into Waiting list
     * @return ID of the created entry
     */
    public long insertEntry(Student entry) {

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // 'id' will be inserted automatically. No need to add it
        values.put(Student.COLUMN_STUDENT_NAME, entry.getName());
        values.put(Student.COLUMN_PRIORITY, entry.getPriority());

        // insert row
        long id = db.insert(Student.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    /**********************************************************************************************
     * Fetches the Entry for the supplied ID
     * @param id: The ID for which entry is to be fetched
     * @return: The object containing details of the supplied ID
     */
    public Student getEntry(long id) {

        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Student.TABLE_NAME,
                new String[]{Student.COLUMN_ID, Student.COLUMN_STUDENT_NAME, Student.COLUMN_PRIORITY},
                Student.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Student entry = new Student(
                cursor.getInt(cursor.getColumnIndex(Student.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_STUDENT_NAME)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_PRIORITY)));

        // close the db connection
        cursor.close();

        return entry;
    }

    /*********************************************************************************************
     * Returns all the entries in the Waiting list table
     * @return list of the all entries in Waiting list
     */
    public List<Student> getAllEntries() {
        List<Student> student = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student entry = new Student();

                // Add each value into entry object
                entry.setId(cursor.getInt(cursor.getColumnIndex(Student.COLUMN_ID)));
                entry.setName(cursor.getString(cursor.getColumnIndex(Student.COLUMN_STUDENT_NAME)));
                entry.setPriority(cursor.getString(cursor.getColumnIndex(Student.COLUMN_PRIORITY)));

                // Add the entry object into the list
                student.add(entry);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return waiting list
        return student;
    }

    /**********************************************************************************************
     * Get count of the entries in the Waiting List
     * @return count of entries in Waiting list
     */
    public int getNameCount() {

        // Form the query as SELECT *
        String countQuery = "SELECT  * FROM " + Student.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        // To get count of values returned
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*********************************************************************************************
     * Update the entry in waiting list with supplied information
     * @param entry: Entry to be updated
     * @return: ID of the entry updated
     */
    public int updateEntry(Student entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_STUDENT_NAME, entry.getName());
        values.put(Student.COLUMN_PRIORITY, entry.getPriority());

        // updating row
        return db.update(Student.TABLE_NAME, values, Student.COLUMN_ID + " = ?",
                new String[]{String.valueOf(entry.getId())});
    }

    /*********************************************************************************************
     * Deletes a particular entry from the Waiting List
     * @param entry: Entry to be deleted from the database
     */
    public void deleteEntry(Student entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Student.TABLE_NAME, Student.COLUMN_ID + " = ?",
                new String[]{String.valueOf(entry.getId())});
        db.close();
    }
}
/************************************ End of class DatabaseHelper *******************************/