package com.todoappwithserver.tables;

import java.util.ArrayList;
import java.util.List;

import com.todoappwithserver.model.Task;
import com.todoappwithserver.tables.TaskContract.TaskEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper {

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" + TaskEntry._ID + " INTEGER PRIMARY KEY," + TaskEntry.COLUMN_NAME_TASK_ID + TEXT_TYPE + COMMA_SEP
			+ TaskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP + TaskEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + " )";

	private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;

	// If you change the database schema, you must increment the database version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "TaskList.db";

	public TaskDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is
		// to simply to discard the data and start over
		db.execSQL(SQL_DELETE_TABLE);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	public long addTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
//		values.put(TaskEntry.COLUMN_NAME_TASK_ID, id);
		values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
		values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(TaskEntry.TABLE_NAME, TaskEntry.COLUMN_NAME_DESCRIPTION, values);
		return newRowId;
	}
	
	public List<Task> getAllTasks(){
		List<Task> tasks = new ArrayList<Task>();
		// Select All Query
	    String selectQuery = "SELECT  * FROM " + TaskEntry.TABLE_NAME;
	    
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Task task = new Task();
	            task.setId(Integer.parseInt(cursor.getString(0)));
	            task.setTitle(cursor.getString(2));
	            task.setDescription(cursor.getString(3));
	            // Adding contact to list
	            tasks.add(task);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return tasks;
	    
	    
	}
	
	public void readFromDb() {
		
	}
}
