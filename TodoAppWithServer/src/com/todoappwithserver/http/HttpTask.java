package com.todoappwithserver.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.todoappwithserver.model.Task;
import com.todoappwithserver.tables.TaskDbHelper;

import android.accounts.Account;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

public class HttpTask extends AsyncTask<String, Void, String> {

	// Map<String, String> params;
	List<Map<String, String>> params;
	TaskDbHelper mDbHelper;
	Account account;

	public HttpTask(List<Map<String, String>> params) {
		super();
		this.params = params;
	}

	public HttpTask(TaskDbHelper mDbHelper, Account account) {
		super();
		this.mDbHelper = mDbHelper;
		this.account = account;
	}

	public String putData(String... urls) {
		String response = "";
		for (String url : urls) {
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			HttpPost httpPost = new HttpPost(url);

			JsonArray taskArray = new JsonArray();
			try {
				for (Map<String, String> task : params) {
					Set<String> keys = task.keySet();
					JsonObject taskObj = new JsonObject();
					for (String key : keys) {
						String value = task.get(key);
						taskObj.addProperty(key, value);
					}
					taskArray.add(taskObj);
				}
				if (taskArray.size() > 0) {
					JsonObject finalTaskObj = new JsonObject();
					finalTaskObj.add("tasks", taskArray);
					StringEntity entity = new StringEntity(finalTaskObj.toString(), HTTP.UTF_8);
					entity.setContentType("application/json");

					// Set up the header types needed to properly transfer JSON
					httpPost.setHeader("Content-Type", "application/json");
					httpPost.setHeader("Accept-Encoding", "application/json");
					httpPost.setHeader("Accept-Language", "en-US");

					httpPost.setEntity(entity);

					HttpResponse execute = client.execute(httpPost);
					execute.getStatusLine();
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				client.close();
			}
		}
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		if (result != null && result != "") {
			try {
				JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
				JsonArray taskArray = (JsonArray) jsonObject.get("tasks");
				for (JsonElement taskEle : taskArray) {
					JsonObject taskObj = (JsonObject) taskEle;
					Task task = new Task();
					task.setId(taskObj.get("id").getAsInt());
					task.setTitle(taskObj.get("title").getAsString());
					task.setDescription(taskObj.get("description").getAsString());
					task.setTimeStamp(taskObj.get("timestamp").getAsLong());
					mDbHelper.addTask(task);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected String doInBackground(String... urls) {
		String response = "";
		for (String url : urls) {
			// DefaultHttpClient client = new DefaultHttpClient();
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			HttpPost httpPost = new HttpPost(url);
			try {
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("email", account.name));
				httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
				HttpResponse execute = client.execute(httpPost);
				execute.getStatusLine();
				InputStream content = execute.getEntity().getContent();

				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				String s = "";
				while ((s = buffer.readLine()) != null) {
					response += s;
				}
				System.out.print(response);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				client.close();
			}
		}
		return response;
	}

}
