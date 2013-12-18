package com.todoappwithserver.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

public class SyncAdapter extends AbstractThreadedSyncAdapter{
	
	private Context mContext;

	 public SyncAdapter(Context context, boolean autoInitialize) {
			super(context, autoInitialize);
			 mContext = context;
		}


	  @Override
	  public void onPerformSync(Account account, Bundle bundle, String authority,
	      ContentProviderClient provider, SyncResult syncResult) {
		  DataSyncer dataSyncer = DataSyncer.getInstance(mContext, account);
		  dataSyncer.syncData();
	  }


}
