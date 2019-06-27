package com.payfun.van.daou.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.jsoup.Jsoup;
import com.payfun.van.daou.R;

import ginu.android.library.emv.bbdevice.EmvReader;
import ginu.android.library.utils.common.ApiDate;
import ginu.android.library.utils.common.ApiIntegrity;
import ginu.android.library.utils.common.ApiLog;
import ginu.android.library.utils.common.ApiPermission;
import ginu.android.library.utils.gui.DialogHandler;
import ginu.android.library.utils.gui.MyTaskProgress;
import ginu.android.van.app_daou.database.PayFunDB;
import ginu.android.van.app_daou.database.VanStaticData;
import ginu.android.van.app_daou.entity.UserEntity;
import ginu.android.van.app_daou.helper.AppHelper;
import ginu.android.van.app_daou.manager.ReceiptManager;
import ginu.android.van.app_daou.utils.MyPermission;


public class LoadingActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		mMyPermission = new MyPermission();				//	ApiPermission.setPermission(this);
		mMyPermission.setPermissions(this);

		mPackageName = getApplicationInfo().packageName;


		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		int readerType = AppHelper.getReaderType();
		ApiLog.Dbg("readerType:"+readerType);

		EmvReader.setEmvReaderType(readerType);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if ( ApiIntegrity.isDeviceRooted(this) )
			showIntegrityErrorDialog(ERROR_MSG_DEVICE_ROOTED);
		else
		{
			MyTaskProgress.CallBackMethod callBackMethod = new MyTaskProgress.CallBackMethod() {
				@Override
				public boolean run() {
					if( VanStaticData.mmIsTesting )
						return false;				// skip check up-version on google play.
					else
						return web_update();
				}

				@Override
				public boolean res(boolean result) {
					removeReceiptBefore3Month();
					if( result )
						showNewAppUpdateDialog();
					else
						if( !mIsKeyBindingComplete )
						{
							mIsKeyBindingComplete = true;
							checkKeyBinding();
						}
					return false;
				}
			};

			MyTaskProgress taskProgress = new MyTaskProgress(this, callBackMethod,"Downloading", null, android.R.attr.progressBarStyleLarge);
			taskProgress.execute();
		}

	}

	//##########################################
	//	Permission Callback Methods
	//##########################################
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permission[], @NonNull int[] grantResults)
	{
		mMyPermission.Result(requestCode, permission, grantResults);
	}
	//##########################################
	//	Private Methods
	//##########################################
	private void showIntegrityErrorDialog(String msg)
	{
		//	Text Only Dialog
		DialogHandler dialogHandler = new DialogHandler(this, ERROR_INTEGRITY_TITLE);
		dialogHandler.setMessage(msg);
		dialogHandler.setCancelable(false);
		dialogHandler.setIcon(DialogHandler.dialogMode.MODE_ERROR);		// information Icon
		dialogHandler.setPositiveButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		dialogHandler.Create();
		dialogHandler.Show();
	}

	private void showNewAppUpdateDialog()
	{
		//	Text Only Dialog
		DialogHandler dialogHandler = new DialogHandler(this, INFO_APP_UPDATE_TITLE);
		dialogHandler.setMessage(INFO_MSG_UPDATE_NEW_APP_VERSION);
		dialogHandler.setCancelable(false);
		dialogHandler.setIcon(DialogHandler.dialogMode.MODE_ERROR);		// information Icon
		dialogHandler.setPositiveButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mPackageName)));
			}
		});
		dialogHandler.setNegativeButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				checkKeyBinding();
			}
		});
		dialogHandler.Create();
		dialogHandler.Show();
	}

	/*
	 *	## get version by using Jsoup Lib.
	 *	## Usage:: add Jsoup Lib on project gradle file.
	 *	==> implementation files('libs/jsoup-1.8.1.jar')
	 */
	private boolean web_update()
	{
		try {
			ApiLog.Dbg(Tag+"try init LocalDB");
			PayFunDB.InitializeDB( getBaseContext(), mPackageName );
			String curVersion = getApplication().getPackageManager().getPackageInfo(mPackageName, 0).versionName;
			String newVersion = curVersion;
			newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + mPackageName + "&hl=en")
					.timeout(30000)
					.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
					.referrer("http://www.google.com")
					.get()
					.select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
					//.select("div[itemprop=softwareVersion]")
					.first().ownText();

			ApiLog.Dbg(Tag+"NewVersion:" + newVersion);
			return ( versionValue(curVersion) < versionValue(newVersion) );
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private long versionValue( String string)
	{
		string = string.trim();
		if (string.contains(".")) {
			final int index = string.lastIndexOf(".");
			return versionValue(string.substring(0, index)) * 100 + versionValue(string.substring(index + 1));
		} else {
			return Long.valueOf(string);
		}
	}

	void removeReceiptBefore3Month()
	{
		try{
			ApiLog.Dbg(Tag+"Receipt before 3 month:"+ ReceiptManager.getReceiptBefore3Month());
			ReceiptManager.deleteBefore3Month();
			UserEntity key = new UserEntity(0);
			String f_UserID = AppHelper.AppPref.getCurrentUserID();
			ApiLog.Dbg(Tag+"f_UserID:"+f_UserID);
			if(!f_UserID.equals("")){
				ApiLog.Van(Tag+"update 3 month:"+ReceiptManager.updateReceiptBefore3Month(f_UserID));
			}
			ApiLog.Dbg(Tag+"Receipt before 3 month (after delete):"+ReceiptManager.getReceiptBefore3Month());
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}

	void checkKeyBinding(){

		String keySavedYear = AppHelper.AppPref.getKeyBindingYear();
		String currentYear = ApiDate.getYear();
		ApiLog.Dbg(Tag+"keySavedYear:"+ keySavedYear + ", currentYear:"+currentYear);
		if(keySavedYear.equals(currentYear)){
			getNotice();
		}else{
			DialogHandler dialogHandler = new DialogHandler(this, WARNING_KEY_UPDATE_TITLE);
			dialogHandler.setMessage(WARNING_MSG_KEY_UPDATE_WARNING);
			dialogHandler.setCancelable(false);
			dialogHandler.setIcon(DialogHandler.dialogMode.MODE_WARNING);		// information Icon
			dialogHandler.setPositiveButton(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getNotice();
				}
			});
			dialogHandler.Create();
			dialogHandler.Show();
		}
	}

	private void getNotice()
	{
		PayFunDB.InitializeDB( getBaseContext(), mPackageName );		// to check Device Rooting again.
		launchMainActivity();
	}

	private void launchMainActivity()
	{
		Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
		startActivity(mainIntent);
		finish();
	}
	//##########################################
	//	Private variables
	//##########################################
	private final String Tag = String.format("[%s]",LoadingActivity.class.getSimpleName() );
	private final static String ERROR_INTEGRITY_TITLE			= "무결성 검사오류";
	private final static String ERROR_MSG_DEVICE_ROOTED		= "Device is Rooted";
	private final static String INFO_APP_UPDATE_TITLE			= "앱 업데이트 정보";
	private final static String INFO_MSG_UPDATE_NEW_APP_VERSION	= "새로운 업데이트 버전이 있습니다. 업데이트 하시겠습니까?";
	private final static String WARNING_KEY_UPDATE_TITLE			= "키 교환 요청.";
	private final static String WARNING_MSG_KEY_UPDATE_WARNING	= "사업자정보-> 키연결 실행해 주세요";

	private String 			mPackageName = "";
	private boolean		mIsKeyBindingComplete = false;
	private MyPermission			mMyPermission;
}
