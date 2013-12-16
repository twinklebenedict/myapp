package com.todoappwithserver.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

public class HttpTask extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... urls) {
		String response = "";
		for (String url : urls) {
//	        DefaultHttpClient client = new DefaultHttpClient();
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	        HttpGet httpGet = new HttpGet(url);
	        try {
	          HttpResponse execute = client.execute(httpGet);
	          InputStream content = execute.getEntity().getContent();

	          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	          String s = "";
	          while ((s = buffer.readLine()) != null) {
	            response += s;
	          }

	        } catch (Exception e) {
	          e.printStackTrace();
	        }finally{
	        	client.close();
	        }
	      }
	      return response;
	}
	
	 @Override
	    protected void onPostExecute(String result) {
//	      textView.setText(result);
	    }

	
}
