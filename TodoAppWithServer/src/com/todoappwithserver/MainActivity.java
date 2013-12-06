package com.todoappwithserver;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.todoappwithserver.model.Task;
import com.todoappwithserver.tables.TaskDbHelper;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final TaskDbHelper mDbHelper = new TaskDbHelper(getApplicationContext());

		// Create a list with check box later for this..

		final EditText text = (EditText) findViewById(R.id.text1);
		final Button save = (Button) findViewById(R.id.save);
		final ListView list = (ListView) findViewById(R.id.listView);
		populateListView(list, mDbHelper);

		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
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
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, taskDes);
		list.setAdapter(adapter);
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
