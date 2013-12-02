package com.todoapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class MainActivity extends Activity {
	
	// The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "google.com";
    // The account name
    public static final String ACCOUNT = "twinklebenedict";
    private static final long SYNC_FREQUENCY = 60 * 60;  // 1 hour (in seconds)
    private static final String CONTENT_AUTHORITY = "com.todoapp.SyncAdapter";
    private static final String PREF_SETUP_COMPLETE = "setup_complete";
    // Instance fields
    Account mAccount;
    
  static final int REQUEST_ACCOUNT_PICKER = 1;
  static final int REQUEST_AUTHORIZATION = 2;
  static final int CAPTURE_IMAGE = 3;

  private static String fileName = "todoList.txt";
  private static Drive service;
  private GoogleAccountCredential credential;

  @TargetApi(19)
@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
 // Create the dummy account
    Account[] accounts = AccountManager.get(this).getAccounts();
    if(accounts.length > 1){
    	mAccount = accounts[0];
    }
    CreateSyncAccount(this, mAccount);
    
    //creation of view
    final EditText todoList = (EditText)findViewById(R.id.todoList);
    Button refresh = (Button)findViewById(R.id.refresh);
    Button save = (Button)findViewById(R.id.save);
    
    //Set text todoList
    String data = readFromFile();
	todoList.setText(data);
    
    save.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			writeToFile(todoList.getText().toString());
			saveFileToDrive();
			
		}
	});
    
    refresh.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String data = readFromFile();
			todoList.setText(data);
		}
	});
    
    

    credential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(DriveScopes.DRIVE));
    startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
  }
  
  /**
   * Create a new dummy account for the sync adapter
   *
   * @param context The application context
   */
  public static Account CreateSyncAccount(Context context, Account account) {
      // Create the account type and default account
      /*Account newAccount = new Account(
              ACCOUNT, ACCOUNT_TYPE);*/
      // Get an instance of the Android account manager
	  if(account == null){
		  return null;
	  }
      AccountManager accountManager =
              (AccountManager) context.getSystemService(
                      ACCOUNT_SERVICE);
      /*
       * Add the account and account type, no password or user data
       * If successful, return the Account object, otherwise report an error.
       */
      if (accountManager.addAccountExplicitly(account, null, null)) {
    	  ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);
          // Inform the system that this account is eligible for auto sync when the network is up
          ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
          // Recommend a schedule for automatic synchronization. The system may modify this based
          // on other scheduled syncs and network utilization.
          ContentResolver.addPeriodicSync(
        		  account, CONTENT_AUTHORITY, new Bundle(),SYNC_FREQUENCY);
      } else {
          /*
           * The account exists or some other error occurred. Log this, report it,
           * or handle it internally.
           */
      }
      return account;
  }
  
  private String readFromFile() {

      String ret = "";

      try {
          InputStream inputStream = openFileInput(fileName);

          if ( inputStream != null ) {
              InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
              BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
              String receiveString = "";
              StringBuilder stringBuilder = new StringBuilder();

              while ( (receiveString = bufferedReader.readLine()) != null ) {
                  stringBuilder.append(receiveString);
              }

              inputStream.close();
              ret = stringBuilder.toString();
          }
      }
      catch (FileNotFoundException e) {
          Log.e("TAG", "File not found: " + e.toString());
      } catch (IOException e) {
          Log.e("TAG", "Can not read file: " + e.toString());
      }

      return ret;
  }
  
  private void writeToFile(String data) {
      try {
          OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE));
          outputStreamWriter.write(data);
          outputStreamWriter.close();
      }
      catch (IOException e) {
          Log.e("TAG", "File write failed: " + e.toString());
      }

  }

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    switch (requestCode) {
    case REQUEST_ACCOUNT_PICKER:
      if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
        String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        if (accountName != null) {
          credential.setSelectedAccountName(accountName);
          service = getDriveService(credential);
//          startCameraIntent();
        }
      }
      break;
    case REQUEST_AUTHORIZATION:
      if (resultCode == Activity.RESULT_OK) {
        saveFileToDrive();
      } else {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
      }
      break;
    case CAPTURE_IMAGE:
      if (resultCode == Activity.RESULT_OK) {
//        saveFileToDrive();
      }
    }
  }

  /*private void startCameraIntent() {
    String mediaStorageDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES).getPath();
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
    fileUri = Uri.fromFile(new java.io.File(mediaStorageDir + java.io.File.separator + "IMG_"
        + timeStamp + ".jpg"));

    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
    startActivityForResult(cameraIntent, CAPTURE_IMAGE);
  }*/

  private void saveFileToDrive() {
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          // File's binary content
          java.io.File fileContent = new java.io.File(getFilesDir() + java.io.File.separator + fileName);
          FileContent textContent = new FileContent("text/plain", fileContent);

          // File's metadata.
          File body = new File();
          body.setTitle(fileContent.getName());
          body.setMimeType("text/plain");

          File file = service.files().insert(body, textContent).execute();
          if (file != null) {
            showToast("Text uploaded: " + file.getTitle());
//            startCameraIntent();
          }
        } catch (UserRecoverableAuthIOException e) {
          startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    t.start();
  }

  private Drive getDriveService(GoogleAccountCredential credential) {
    return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
        .build();
  }

  public void showToast(final String toast) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
      }
    });
  }
}