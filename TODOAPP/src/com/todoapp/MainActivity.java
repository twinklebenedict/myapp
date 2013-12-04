package com.todoapp;

import java.util.Arrays;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class MainActivity extends Activity {

	Account mAccount;

	public static final int REQUEST_ACCOUNT_PICKER = 1;
	static final int REQUEST_AUTHORIZATION = 2;
	static final int CAPTURE_IMAGE = 3;

	// private static Drive service;
	private GoogleAccountCredential credential;

	@TargetApi(19)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Utils.restoreBackup(this);
		credential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(DriveScopes.DRIVE));
		startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);

		// Get account
		/*Account[] accounts = AccountManager.get(this).getAccounts();
		if (accounts.length > 0) {
			mAccount = accounts[0];
		}*/

		// creation of view
		final EditText todoList = (EditText) findViewById(R.id.todoList);
		Button refresh = (Button) findViewById(R.id.refresh);
		Button save = (Button) findViewById(R.id.save);

		// Set text todoList
		String data = Utils.readFromFile(this, Constants.fileName);
		todoList.setText(data);

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Utils.writeToFile(todoList.getText().toString(), MainActivity.this, Constants.fileName);
				// Utils.saveFileToDrive(MainActivity.this, mAccount);

			}
		});

		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String data = Utils.readFromFile(MainActivity.this, Constants.fileName);
				todoList.setText(data);
			}
		});

	}

	/**
	 * Create a new dummy account for the sync adapter
	 * 
	 * @param context
	 *            The application context
	 */
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

	/*
	 * private String readFromFile() {
	 * 
	 * String ret = "";
	 * 
	 * try { InputStream inputStream = openFileInput(Constants.fileName);
	 * 
	 * if (inputStream != null) { InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader); String receiveString
	 * = ""; StringBuilder stringBuilder = new StringBuilder();
	 * 
	 * while ((receiveString = bufferedReader.readLine()) != null) { stringBuilder.append(receiveString); }
	 * 
	 * inputStream.close(); ret = stringBuilder.toString(); } } catch (FileNotFoundException e) { Log.e("TAG", "File not found: " + e.toString()); } catch (IOException e) { Log.e("TAG",
	 * "Can not read file: " + e.toString()); }
	 * 
	 * return ret; }
	 */

	/*
	 * private void writeToFile(String data) { try { OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(Constants.fileName, Context.MODE_PRIVATE));
	 * outputStreamWriter.write(data); outputStreamWriter.close(); } catch (IOException e) { Log.e("TAG", "File write failed: " + e.toString()); }
	 * 
	 * }
	 */

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
						credential.setSelectedAccountName(accountName);
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

	/*
	 * private void saveFileToDrive() { Thread t = new Thread(new Runnable() {
	 * 
	 * @Override public void run() { try { // File's binary content java.io.File fileContent = new java.io.File(getFilesDir() + java.io.File.separator + Constants.fileName); FileContent textContent =
	 * new FileContent("text/plain", fileContent);
	 * 
	 * // File's metadata. File body = new File(); body.setTitle(fileContent.getName()); body.setMimeType("text/plain");
	 * 
	 * File file = service.files().insert(body, textContent).execute(); if (file != null) { showToast("Text uploaded: " + file.getTitle()); // startCameraIntent(); } } catch
	 * (UserRecoverableAuthIOException e) { startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION); } catch (Exception e) { e.printStackTrace(); } } }); t.start(); }
	 */

	/*
	 * private Drive getDriveService(GoogleAccountCredential credential) { return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential).build(); }
	 */
	/*
	 * public void showToast(final String toast) { runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show(); } }); }
	 */

	/*
	 * public Drive getService() { return service; }
	 */
}