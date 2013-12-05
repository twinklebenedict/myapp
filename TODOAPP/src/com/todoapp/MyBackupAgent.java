package com.todoapp;

import java.io.IOException;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class MyBackupAgent extends BackupAgentHelper{
	
	public static String FILE_ID = "fileid.txt";
	public static String BACKUP_KEY = "files";
	
	
	@Override
	public void onCreate() {
		FileBackupHelper helper = new FileBackupHelper(this, FILE_ID);
        addHelper(BACKUP_KEY, helper);
        Log.i("Test", "Adding backupagent... ##############");
	}
	
	@Override
	public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
		// TODO Auto-generated method stub
		super.onBackup(oldState, data, newState);
	}
	
	@Override
	public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
		// TODO Auto-generated method stub
		super.onRestore(data, appVersionCode, newState);
	}

}
