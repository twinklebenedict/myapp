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
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.google.gson.JsonObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

public class HttpTask/* extends AsyncTask<String, Void, String>*/ {

	Map<String, String> params;

	public HttpTask(Map<String, String> params) {
		super();
		this.params = params;
	}

//	@Override
	public String doInBackground(String... urls) {
		String response = "";
		for (String url : urls) {
			// DefaultHttpClient client = new DefaultHttpClient();
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			HttpPost httpPost = new HttpPost(url);
			
			JsonObject jsonObj = new JsonObject();
			try {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					String value = params.get(key);
					jsonObj.addProperty(key, value);
				}
				StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
				entity.setContentType("application/json");
				
				// Set up the header types needed to properly transfer JSON
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept-Encoding", "application/json");
				httpPost.setHeader("Accept-Language", "en-US");
				
				/*List<NameValuePair> postParams = new ArrayList<NameValuePair>();
	            postParams.add(new BasicNameValuePair("data", jsonObj.toString()));

	            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams);
	            entity.setContentEncoding(HTTP.UTF_8);*/

				httpPost.setEntity(entity);
				
				HttpResponse execute = client.execute(httpPost);
				execute.getStatusLine();
				InputStream content = execute.getEntity().getContent();

				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				String s = "";
				while ((s = buffer.readLine()) != null) {
					response += s;
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				client.close();
			}
		}
		return response;
	}

//	@Override
	protected void onPostExecute(String result) {
		// textView.setText(result);
	}

}
