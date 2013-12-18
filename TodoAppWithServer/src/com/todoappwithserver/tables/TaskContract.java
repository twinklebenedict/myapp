package com.todoappwithserver.tables;

import android.provider.BaseColumns;

public final class TaskContract {
	
	public TaskContract() {
		// TODO Auto-generated constructor stub
	}
	
	 public static abstract class TaskEntry implements BaseColumns {
	        public static final String TABLE_NAME = "task";
	        public static final String COLUMN_NAME_TASK_ID = "taskid";
	        public static final String COLUMN_NAME_TITLE = "title";
	        public static final String COLUMN_NAME_DESCRIPTION = "description";
	        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
	    }

}
