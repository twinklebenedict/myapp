package com.todoappwithserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.AccountPicker;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.todoappwithserver.model.Task;
import com.todoappwithserver.tables.TaskDbHelper;
import com.todoappwithserver.views.ListViewArrayAdapter;

public class MainActivity extends Activity {
	
	Account mAccount;

	public static final int REQUEST_ACCOUNT_PICKER = 1;
	static final int REQUEST_AUTHORIZATION = 2;
	static final int CAPTURE_IMAGE = 3;
	private GoogleAccountCredential credential;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final TaskDbHelper mDbHelper = new TaskDbHelper(getApplicationContext());
		
		//Account picker
		Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
		         false, null, null, null, null);
		 startActivityForResult(intent, REQUEST_ACCOUNT_PICKER);

		// Create a list with check box later for this..

		final EditText text = (EditText) findViewById(R.id.text1);
		final Button save = (Button) findViewById(R.id.save);
		final ListView list = (ListView) findViewById(R.id.listView);
		final Button delete = (Button) findViewById(R.id.delete);
		populateListView(list, mDbHelper);
		
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List<Integer> taskIds = new ArrayList<Integer>(); 
				for (int i = 0; i < list.getChildCount(); i++) {
					CheckBox checkBox = (CheckBox) list.getChildAt(i);
					if(checkBox != null && checkBox.isChecked()){
						taskIds.add(checkBox.getId());
					}
				}
				
				for (int i = 0; i <taskIds.size(); i++) {
//					Task task = mDbHelper.getTask(taskIds.get(i));
					mDbHelper.deleteTask(taskIds.get(i));
				}
				populateListView(list, mDbHelper);
			}
		});

		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Task task = new Task("title", text.getText().toString());// test
				mDbHelper.addTask(task);
				populateListView(list, mDbHelper);
				text.setText("");
			}
		});
	}

	private List<String> populateListView(ListView list, TaskDbHelper mDbHelper) {
		List<Task> tasks = mDbHelper.getAllTasks();
		List<String> taskDes = new ArrayList<String>();
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getDescription() != null) {
				taskDes.add(tasks.get(i).getDescription());
			}
		}
		ListViewArrayAdapter adapter = new ListViewArrayAdapter(getBaseContext(), tasks, taskDes);
		list.setAdapter(adapter);
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:
			if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				Account[] accounts = AccountManager.get(this).getAccounts();
				for (int i = 0; i < accounts.length; i++) {
					if (accounts[i].name.equals(accountName)) {
						mAccount = accounts[i];
//						credential.setSelectedAccountName(accountName);
						createSyncAccount(this, mAccount);
						break;
					}
				}
			}
			break;
		case REQUEST_AUTHORIZATION:
			if (resultCode == Activity.RESULT_OK) {
				// Utils.saveFileToDrive(this, mAccount);
			} else {
				startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
			}
			break;
		case CAPTURE_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				// saveFileToDrive();
			}
		}
	}
	
	public static void createSyncAccount(Context context, Account account) {
		if (account == null) {
			return;
		}
		if (ContentResolver.isSyncPending(account, Constants.CONTENT_AUTHORITY) || ContentResolver.isSyncActive(account, Constants.CONTENT_AUTHORITY)) {
			Log.i("ContentResolver", "SyncPending, canceling");
			ContentResolver.cancelSync(account, Constants.CONTENT_AUTHORITY);
		}
		Bundle bundle = new Bundle();
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
		ContentResolver.setIsSyncable(account, Constants.CONTENT_AUTHORITY, 1);
		ContentResolver.setSyncAutomatically(account, Constants.CONTENT_AUTHORITY, true);
		ContentResolver.addPeriodicSync(account, Constants.CONTENT_AUTHORITY, bundle, Constants.SYNC_FREQUENCY);
	}

}
