package com.todoappwithserver.views;

import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.todoappwithserver.R;
import com.todoappwithserver.model.Task;

public class ListViewArrayAdapter  extends ArrayAdapter<String> {
	  private final Context context;
	  private final List<Task> tasks;

	  public ListViewArrayAdapter(Context context, List<Task> tasks, List<String> taskDesc) {
		  //list_check.xml is a custom layout
	    super(context, R.layout.list_check, taskDesc);
	    this.context = context;
	    this.tasks = tasks;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View checkedTextView = inflater.inflate(R.layout.list_check, parent, false);
	    CheckBox check = (CheckBox) checkedTextView.findViewById(R.id.checkBox1);
	    check.setText(tasks.get(position).getDescription());
	    check.setId(tasks.get(position).getId());
	    return checkedTextView;
	  }
}
