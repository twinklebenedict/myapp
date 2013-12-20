package com.todoappwithserver.sync;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.todoappwithserver.http.HttpTask;
import com.todoappwithserver.model.Task;
import com.todoappwithserver.tables.TaskDbHelper;

import android.accounts.Account;
import android.content.Context;

public class DataSyncer {

	Context context;
	Account account;
	Long lastSyncTimestamp;
	final TaskDbHelper mDbHelper;
	private static DataSyncer instace;

	private DataSyncer(Context context, Account account) {
		this.context = context;
		this.account = account;
		lastSyncTimestamp = new Long(0);
		mDbHelper = new TaskDbHelper(context);
	}

	public static synchronized DataSyncer getInstance(Context context, Account account) {
		if (instace == null) {
			instace = new DataSyncer(context, account);
		}
		return instace;
	}

	public boolean syncData() {
		HttpTask httpTask;
		List<Map<String, String>> params = new ArrayList<Map<String,String>>();
		String url = "http://107.108.202.232:8080/TodoAppServer/hello.htm";
		synchronized (lastSyncTimestamp) {
			long lastSynctimeTemp = lastSyncTimestamp;
			lastSyncTimestamp = getCurrentTimestamp();
			List<Task> tasks = mDbHelper.getAllTasks();
			for (Task task : tasks) {
				long timestamp = task.getTimeStamp();
				if (timestamp > lastSynctimeTemp) {
					Map<String, String> taskMap = new HashMap<String, String>();
					taskMap.put("id", String.valueOf(task.getId()));
					taskMap.put("title", task.getTitle());
					taskMap.put("description", task.getDescription());
					taskMap.put("email", account.name);
					taskMap.put("timestamp", String.valueOf(task.getTimeStamp()));
					params.add(taskMap);
				}
			}
			if(!params.isEmpty()){
				httpTask = new HttpTask(params);
				httpTask.putData(url);
			}
		}
		return true;
	}

	private long getCurrentTimestamp() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}

}
