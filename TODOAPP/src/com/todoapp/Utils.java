package com.todoapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import android.accounts.Account;
import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utils{
	
	public static void writeToFile(String data, Context context, String fileName) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
//			if(fileName.equals(BackupAgent.FILE_ID)){
				requestBackup(context);
//			}
		} catch (IOException e) {
			Log.e("TAG", "File write failed: " + e.toString());
		}

	}
	
	public static String readFromFile(Context context, String fileName) {

		String ret = "";

		try {
			InputStream inputStream = context.openFileInput(fileName);

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e("TAG", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("TAG", "Can not read file: " + e.toString());
		}

		return ret;
	}
	
	public static void requestBackup(Context context) {
		   BackupManager bm = new BackupManager(context);
		   bm.dataChanged();
	}
	
	public static void restoreBackup(Context context) {
		   BackupManager bm = new BackupManager(context);
		   bm.requestRestore(new RestoreObserver() {});
	}
	
}
