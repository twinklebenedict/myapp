package com.todoapp;

import java.util.Arrays;

import android.accounts.Account;
import android.content.Context;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class DriveSyncer {

	private Context context;
	private Account account;
	private Drive service;
	private static String fileId = "";

	public DriveSyncer(Context context, Account account) {
		this.context = context;
		this.account = account;
		service = getDriveService();
	}

	public void saveFileToDrive() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// File's binary content
					java.io.File fileContent = new java.io.File(context.getFilesDir() + java.io.File.separator + Constants.fileName);
					FileContent textContent = new FileContent("text/plain", fileContent);

					// File's metadata.
					File body = new File();
					body.setTitle(fileContent.getName());
					body.setMimeType("text/plain");

					File driveFile = service.files().get(fileId).execute();
					if (driveFile != null) {
						service.files().update(fileId, driveFile, textContent);
					} else {
						driveFile = service.files().insert(body, textContent).execute();
						if (driveFile != null) {
							fileId = driveFile.getId();
							showToast("Text uploaded: " + driveFile.getTitle());
						}
					}

				} catch (UserRecoverableAuthIOException e) {
					// context.startActivityForResult(e.getIntent(), MainActivity.REQUEST_AUTHORIZATION);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	public void showToast(final String toast) {
		if (context instanceof MainActivity) {
			final MainActivity mainActivity = (MainActivity) context;
			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(mainActivity.getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	public Drive getDriveService() {
		GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(context, Arrays.asList(DriveScopes.DRIVE));
		credential.setSelectedAccountName(account.name);
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential).build();
	}

}
