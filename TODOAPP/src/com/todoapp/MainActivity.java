package com.todoapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
  static final int REQUEST_ACCOUNT_PICKER = 1;
  static final int REQUEST_AUTHORIZATION = 2;
  static final int CAPTURE_IMAGE = 3;

  private static Uri fileUri;
  private static Drive service;
  private GoogleAccountCredential credential;

  @TargetApi(19)
@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    //Creation of file
    String mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
	fileUri = Uri.fromFile(new java.io.File(mediaStorageDir + java.io.File.separator + "todoList.doc"));
    
    //creation of view
    final EditText todoList = (EditText)findViewById(R.id.todoList);
    Button refresh = (Button)findViewById(R.id.refresh);
    Button save = (Button)findViewById(R.id.save);
    
    save.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			writeToFile(todoList.getText().toString());
			
		}
	});
    
    

//    credential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(DriveScopes.DRIVE));
//    startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
  }
  
  private String readFromFile() {

      String ret = "";

      try {
          InputStream inputStream = openFileInput(fileUri.getLastPathSegment());

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
          OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileUri.getLastPathSegment(), Context.MODE_PRIVATE));
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
          startCameraIntent();
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
        saveFileToDrive();
      }
    }
  }

  private void startCameraIntent() {
    String mediaStorageDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES).getPath();
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
    fileUri = Uri.fromFile(new java.io.File(mediaStorageDir + java.io.File.separator + "IMG_"
        + timeStamp + ".jpg"));

    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
    startActivityForResult(cameraIntent, CAPTURE_IMAGE);
  }

  private void saveFileToDrive() {
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          // File's binary content
          java.io.File fileContent = new java.io.File(fileUri.getPath());
          FileContent mediaContent = new FileContent("image/jpeg", fileContent);

          // File's metadata.
          File body = new File();
          body.setTitle(fileContent.getName());
          body.setMimeType("image/jpeg");

          File file = service.files().insert(body, mediaContent).execute();
          if (file != null) {
            showToast("Photo uploaded: " + file.getTitle());
            startCameraIntent();
          }
        } catch (UserRecoverableAuthIOException e) {
          startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
        } catch (IOException e) {
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