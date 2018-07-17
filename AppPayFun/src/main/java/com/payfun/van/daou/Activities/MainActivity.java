package com.payfun.van.daou.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.bbpos.bbdevice.PayfunBBDeviceController;
import com.google.firebase.iid.FirebaseInstanceId;
import com.payfun.van.daou.R;
import com.payfun.van.daou.fragments.FragmentPaymentCreadit;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.ConnectionResult;

import com.payfun.van.daou.fragments.FragmentCallbackInterface;
import com.payfun.van.daou.fragments.FragmentDummy;
import com.payfun.van.daou.fragments.FragmentHome;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import ginu.android.library.emv.bbdevice.EmvApplication;
import ginu.android.library.emv.bbdevice.EmvReader;
import ginu.android.library.emv.bbdevice.IEmvReader;
import ginu.android.library.emv.bbdevice.listeners.IDetectEmvListenerCb;
import ginu.android.library.keyboard.ApiEditTextAmount;
import ginu.android.library.utils.common.ApiAux;
import ginu.android.library.utils.common.ApiExtStorage;
import ginu.android.library.utils.common.ApiLog;
import ginu.android.library.utils.gui.DialogHandler;
import ginu.android.library.utils.gui.MyTaskProgress;
import ginu.android.van.app_daou.ExternalCall.ReqPara;
import ginu.android.van.app_daou.cardreader.EmvUtils;
import ginu.android.van.app_daou.daou.DaouDataContants;
import ginu.android.van.app_daou.database.PayFunDB;
import ginu.android.van.app_daou.database.VanStaticData;
import ginu.android.van.app_daou.entity.BTReaderInfo;
import ginu.android.van.app_daou.entity.KeyBindingEntity;
import ginu.android.van.app_daou.entity.UserEntity;
import ginu.android.van.app_daou.helper.AppHelper;
import ginu.android.van.app_daou.manager.CompanyManger;
import ginu.android.van.app_daou.manager.NoticeManager;
import ginu.android.van.app_daou.manager.UserManager;
import ginu.android.van.app_daou.utils.IVanString;
import ginu.android.van.app_daou.utils.MyPhoneNumber;
import ginu.android.van.app_daou.utils.MyReaderDevices;
import ginu.android.van.app_daou.utils.MyToast;


import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangePage;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowNumericKeyboard;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.HomeToActivityCmd_AttachEmvDetectionListener;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.HomeToActivityCmd_DetachEmvDetectionListener;
import static ginu.android.van.app_daou.database.VanStaticData.*;

public class MainActivity extends AppCompatActivity implements
		FragmentCallbackInterface.HomeToActivity,
		FragmentCallbackInterface.PaymentCreaditToActivity
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //
		//	TODO:: Add user code here
        //

		mActivity = this;
		mPackageName = getApplicationInfo().packageName;

		// check Play service available
		String fcmToken = "";
		if( isPlayServiceAvailable(this, 0) )
		{
			fcmToken = FirebaseInstanceId.getInstance().getToken();
			ApiLog.Dbg("fcm_token: " + fcmToken);
			if( fcmToken != null && ! fcmToken.equals("") )
			{
				AppHelper.AppPref.setFcmToken(fcmToken);
				autoLogin();
			}
		}

		AppHelper.AppPref.setAppSleep(false);


		StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder().permitAll().build() );

		//	Start EmvReader Service:: Owner is this MainActivity
		mEmvApp = (EmvApplication)getApplication();
		mEmvApp.startEmvReaderService( MainActivity.class );
		ApiLog.Dbg("!!==== Start Emv Reader Service in Local ====!!");

		//	register EmvReader Broadcast Receiver
		attachEmvReaderBroadcastReceiver();
	//	attachBluetoothListener();
		checkDBBeforeRunningApp();
		mShowingDialogCount = 0;
    }

	@Override
	public void onResume() {
		super.onResume();
		mIsResumeOnMain = true;
		VanStaticData.mmGET_COUPON = false;
		//setIsBlueTooth();
		//wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		ApiLog.Dbg("onResume on MainActivity");

		if (!"".equals( AppHelper.AppPref.getDeviceTID() ) ) {
			ExternalCallPayment();
			EmvUtils.setIsReadyIC(false);

			//check if user enable app after sleep
			if ( AppHelper.AppPref.getAppSleep() ) {
				connectDeviceOnWakeUp();
			}
		}
	}

	@Override
	protected void onStop() {
		ApiLog.Dbg("onStop on MainActivity");
		AppHelper.AppPref.setAppSleep(true);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		ApiLog.Dbg("onDestroy");
		super.onDestroy();
		detachServices();
		if (mEmvApp != null)
			mEmvApp.stopEmvReaderService();
	}
    //==========================
    //  Fragment Callback functions
    //==========================
    public void homeToActivityCb(int cmd, Object obj)
    {
        switch(cmd)
        {
            case    CommonFragToActivityCmd_ChangePage:
                int page = (int)obj;
                changePage(page);
                break;
			case HomeToActivityCmd_AttachEmvDetectionListener:
				attachDetectEmvServiceListener();
				break;
			case HomeToActivityCmd_DetachEmvDetectionListener:
				detachDetectEmvServiceListener();
				break;
            default:
                break;
        }
    }

    public void paymentCreaditToActivityCb(int cmd, Object obj)
    {
        switch(cmd)
        {
            case    CommonFragToActivityCmd_ChangePage:
                int page = (int)obj;
                changePage(page);
                break;
			case 	CommonFragToActivityCmd_ShowNumericKeyboard:
				showNumericKeyboard((EditText)obj);
				break;
                default:
                break;
        }
    }

    //==========================
    //  Fragment Page Adapter
    //==========================
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment;
            switch(position)
            {
                case 0:
                    //return FragmentHome.newInstance(position + 1);
                    fragment = FragmentHome.newInstance(position+1);
                    mActivityToHome = (FragmentCallbackInterface.ActivityToHome) fragment;
                //    mActivityToHome.activityToHomeCb(ActivityToHomeCmd_DeviceAdapter, mDeviceAdapter);
                    return fragment;
                case 1:
                    fragment = FragmentPaymentCreadit.newInstance(position + 1);
                    mActivityToPaymentCreadit = (FragmentCallbackInterface.ActivityToPaymentCreadit) fragment;
                    return fragment;
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }


    //==========================================
	//	Emv Detect Callbacks
	//	** see IDetectEmvListenerCb
	//==========================================
	protected class EmvDetectListener implements IDetectEmvListenerCb
	{
		@Override
		public void onReturnDeviceInfo( Hashtable<String, String> deviceInfoData ) {
			ApiLog.Dbg("onReturnDeviceInfo in MainActivity");
			closeDialog();

			EmvUtils.setIsReadyIC(true);

			Set<String> keys = deviceInfoData.keySet();
			for (String key : keys) {
				ApiLog.Dbg(key + ":" + deviceInfoData.get(key));
			}

			String pinKsn = deviceInfoData.get("pinKsn") == null ? "" : deviceInfoData.get("pinKsn");
			String modelName = deviceInfoData.get("modelName") == null ? "" : deviceInfoData.get("modelName");
			EmvUtils.saveHWModelName(modelName);
			EmvUtils.saveKsn(pinKsn);

			String firmwareVersion = deviceInfoData.get("firmwareVersion") == null ? "" : deviceInfoData.get("firmwareVersion");
			String trackKsn = deviceInfoData.get("trackKsn") == null ? "" : deviceInfoData.get("trackKsn");
			String emvKsn = deviceInfoData.get("emvKsn") == null ? ""	: deviceInfoData.get("emvKsn");
			String uid = deviceInfoData.get("uid") == null ? "" : deviceInfoData.get("uid");
			String csn = deviceInfoData.get("csn") == null ? "" : deviceInfoData.get("csn");

			String deviceSerial = EmvUtils.extractSerialNumber(pinKsn);
			String serialNumber = deviceInfoData.get("serialNumber")==null?
					DaouDataContants.VAL_PRODUCTION_SERIAL_NUMBER : deviceInfoData.get("serialNumber");
			// check to show Device name

			//save HwModelNo
			String modelNo = "";
			firmwareVersion = firmwareVersion.replace(".", "");
			if (firmwareVersion.length() >= 4)
				modelNo = firmwareVersion.substring(0, 4);

			EmvUtils.saveHwModelNo(modelNo);
			EmvUtils.saveHwSerialNumber(serialNumber);
			String publicKeyVersion = deviceInfoData.get("publicKeyVersion") == null? "" : deviceInfoData.get("publicKeyVersion");
			EmvUtils.savePublicKeyVersion(publicKeyVersion);
			EmvUtils.saveHwSerialNumber(serialNumber);

			String batteryLevel = "Battery: " + deviceInfoData.get("batteryPercentage") + " %";
			String hwModelInfo = EmvUtils.getHWModelName() + " " + EmvUtils.getHwModelNo();
			String swModelInfo = DaouDataContants.SWModelName + " " + DaouDataContants.SWModelNo;

/*???????	masked by David SH Kim.
			listener에서 GUI 건디리면 안된다.
			나중에 별도의 Handler를 이용하여 setting하도록 하자.

			((TextView) findViewById(R.id.tvMenuRightBatteryInfo)).setText(batteryLevel);
			((TextView) findViewById(R.id.tvMenuHWModelInfo)).setText(hwModelInfo);
			((TextView) findViewById(R.id.tvMenuSWInfo)).setText(swModelInfo);
*/

			KeyBindingEntity entity = new KeyBindingEntity("");
			entity.setCsn(csn);
			entity.setDeviceNo(deviceSerial);
			entity.setEmvKsn(emvKsn);
			entity.setFirmwareVersion(firmwareVersion);
			entity.setPinKsn(pinKsn);
			entity.setTrackKsn(trackKsn);
			entity.setUid(uid);

			EmvUtils.saveEmvSerial(pinKsn);
			EmvUtils.saveKeyBinding(entity);
			KeyBindingEntity bindingEntity = EmvUtils.getKeyBinding(mActivity, deviceSerial);

			//reset variables
			pinKsn = modelName = trackKsn =  emvKsn = uid = csn = batteryLevel = hwModelInfo = swModelInfo = firmwareVersion = deviceSerial = modelNo ="";
			deviceInfoData = null;

			// didn't select company
			if (bindingEntity == null) {
				return;
			}

		}

		@Override
		public void onDeviceHere(boolean isHere) {
			// TODO Auto-generated method stub
			if ( isHere ) {
				mEmvReader.integrityCheck();
			} else
			if( (mEmvReader.getEmvReaderType() == IEmvReader.DeviceType.audioPlug ) &&
					MyReaderDevices.isHeadsetConnected(mActivity) )
			{	// earjack is Plug In.
				mEmvReader.mmDeviceAudio.restartAudio();
				closeDialog();
				MyToast.showToast(mActivity, IVanString.userNotification.msg_reconnect_device);	//R.string.msg_reconnect_device);
			} else
			{
				closeDialog();
				MyToast.showToast(mActivity, IVanString.userNotification.msg_reconnect_device);	//R.string.msg_reconnect_device);
			}
			mShowingDialogCount = 0;
			isHere = false;
		}

		@Override
		public void onDevicePlugged() {
			// TODO Auto-generated method stub
			ApiLog.Dbg("current volumn:"+ MyReaderDevices.getCurrentVolume(mActivity) );

			if (mEmvReader != null && mEmvReader.IsEmvReaderReady() && ! VanStaticData.mmIsOnPaymentScreen )
			{
				String connectionMode =mEmvReader.getConnectionMode().toString();
				ApiLog.Dbg("connectionMode:"+connectionMode);
				if(connectionMode.equals(PayfunBBDeviceController.ConnectionMode.AUDIO.toString()))
				{	// Audio Earjack Mpde.
					try {
						showDialog();
						Thread.sleep(1000);
						mEmvReader.isDeviceHere();				// Event:: onDeviceHere
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else
				if(connectionMode.equals(PayfunBBDeviceController.ConnectionMode.NONE.toString()))
				{	//	No Connected device
					try {
						mEmvReader.startReaderDevice();
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								mEmvReader.isDeviceHere();		// Event:: onDeviceHere
							}
						}, 1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}


			}

		}

		@Override
		public void onDeviceUnplugged() {
			MyReaderDevices.restoreVolume(mActivity);
			closeDialog();
			// TODO Auto-generated method stub
/** ???????	masked by David SH Kim.
 			listener에서 GUI 건디리면 안된다.
 			나중에 별도의 Handler를 이용하여 setting하도록 하자.

 			((TextView) findViewById(R.id.tvMenuRightBatteryInfo)).setText("");
			((TextView) findViewById(R.id.tvMenuHWModelInfo)).setText("");
 */
			EmvUtils.cleanDeviceValue();
		}

		@Override
		public void onNoDeviceDetected() {
			// TODO Auto-generated method stub
			closeDialog();
		}

		@Override
		public void onError(PayfunBBDeviceController.Error errorState) {
			// TODO Auto-generated method stub
			ApiLog.Dbg("onError on MainActivity");

			ApiLog.Dbg( EmvUtils.getEmvErrorString(mActivity, errorState));
			if( mmIsWaitingTurnOnEmvBTReader && (mEmvReader.getEmvReaderType() == IEmvReader.DeviceType.bluetooth) )
				return;
			switch (errorState){
				case COMM_LINK_UNINITIALIZED:
				case COMM_ERROR:
					if ( mShowingDialogCount < SHOWING_DIALOG_LIMIT) {
						mShowingDialogCount += 1;
						if ( (AppHelper.getReaderType() == IEmvReader.DeviceType.audioPlug) &&
								MyReaderDevices.isHeadsetConnected(mActivity) ) {
							mEmvReader.mmDeviceAudio.restartAudio();
						}
						closeDialog();
						MyToast.showToast(mActivity, IVanString.userNotification.msg_reconnect_device);	//R.string.msg_reconnect_device);
					}
					break;

				case DEVICE_BUSY:
					String msg = IVanString.userNotification.msg_reconnect_device;	//at.getString(R.string.msg_reconnect_device);
					updateDialogMsg(msg);
					break;
				case FAIL_TO_START_AUDIO:
				case FAIL_TO_START_BT:
					//VanStaticData.mmIsBTReaderConnected = false;
					AppHelper.AppPref.setIsBTReaderConnected(false);
					closeDialog();
					MyToast.showToast(mActivity, IVanString.userNotification.msg_reconnect_device);		//R.string.msg_reconnect_device);
					break;
				case INVALID_FUNCTION_IN_CURRENT_CONNECTION_MODE:
					mEmvReader.stopConnection();
				default:
					break;
			}
		}

		@Override
		public void onAutoConfigCompleted(boolean isDefaultSettings, String autoConfigSettings)
		{
			// TODO Auto-generated method stub
			ApiLog.Dbg("autoConfigSettings:"+autoConfigSettings);
			if(autoConfigSettings != null)
				mEmvReader.mmDeviceAudio.setAutoConfig();
			closeDialog();
			ApiLog.Dbg("auto config is completed");
			MyToast.showToast(mActivity, IVanString.userNotification.msg_config_device_success);
		}

		@Override
		public void onAutoConfigError(PayfunBBDeviceController.AudioAutoConfigError autoConfigError) {
			// TODO Auto-generated method stub
			closeDialog();
			ApiLog.Dbg("auto config is error");
			MyToast.showToast(mActivity, IVanString.userNotification.msg_config_device_failed);
		}

		@Override
		public void onAutoConfigProgressUpdate(double percentage) {
			// TODO Auto-generated method stub
			ApiLog.Dbg("config percent:" + (int) percentage);
			if (mDialog != null && mDialog.isShowing()) {
				updateDialogMsg( IVanString.userNotification.msg_config_device_doing + " " + (int) percentage + " %");
			}
		}

		@Override
		public void onReturnIntegrityCheckResult(boolean result) {
			ApiLog.Dbg("onReturnIntegrityCheckResult:" + result);

			String logData = "INTERGRITY CHECK";
			logData += "\nResult:" + result;
			ApiExtStorage.writeIntegrityLog(logData);
			logData = "";

			mEmvReader.getDeviceInfo();
			result = false;
		}

		@Override
		public void onBTReturnScanResults(List<BluetoothDevice> foundDevices) {

		}

		@Override
		public void onBTScanTimeout() {
			ApiLog.Dbg(getString(R.string.bluetooth_2_scan_timeout));
		}

		@Override
		public void onBTScanStopped() {
			ApiLog.Dbg(getString(R.string.bluetooth_2_scan_stopped));
		}

		@Override
		public void onBTConnected(BluetoothDevice bluetoothDevice) {
			ApiLog.Dbg(getString(R.string.bluetooth_connected) + ": " + bluetoothDevice.getAddress());

			// VanStaticData.mmIsBTReaderConnected = true;		removed by David SH Kim.
			AppHelper.AppPref.setIsBTReaderConnected(true);

			VanStaticData.mmIsRequiredWait = true;
			mEmvReader.integrityCheck();			// wait for onReturnIntegrityCheckResult

			mTotalTime = TOTAL_TIME_LIMIT;
		}

		@Override
		public void onBTDisconnected() {
			// VanStaticData.mmIsBTReaderConnected = false;
			AppHelper.AppPref.setIsBTReaderConnected(false);
			closeDialog();
/*	???????	masked by David SH Kim.
			listener에서 GUI 건디리면 안된다.
 			나중에 별도의 Handler를 이용하여 setting하도록 하자.

			((TextView) findViewById(R.id.tvMenuRightBatteryInfo)).setText("");
			((TextView) findViewById(R.id.tvMenuHWModelInfo)).setText("");
*/
			ApiLog.Dbg(getString(R.string.bluetooth_disconnected));
			EmvUtils.cleanDeviceValue();
			if(	(mEmvReader.getEmvReaderType() != IEmvReader.DeviceType.bluetooth) ||
				! VanStaticData.mmIsRequiredWait || mmIsWaitingTurnOnEmvBTReader)
				return;

			waitTurnBTReader();
		}

		public void waitTurnBTReader(){
			mmIsWaitingTurnOnEmvBTReader = true;
		//	mTotalTime = TOTAL_TIME_LIMIT;			// added by David SH Kim. to try to connect again.
													// ?????? Must check it makes trouble or not.!!!
			mHandler.sendEmptyMessage(MessageID.WAIT_FOR_BT_TURN_ON);
		}
	}	// end of Class



    //##########################################
	//	Private Methods
	//##########################################

	private void connectDeviceOnWakeUp()
	{
		if( mEmvReader.getEmvReaderType() == IEmvReader.DeviceType.bluetooth )
		{
			if( ! VanStaticData.mmIsOnPaymentScreen )
				showDialog();

			connectBT();
		}
	}
	private void connectBT()
	{
		//	Check BT connected or not.
		switch( mEmvReader.getConnectionMode() )
		{
			case NONE:
				ApiLog.Dbg("No connected device, start connecting BT");
				break;
			case BLUETOOTH:
				ApiLog.Dbg("BT Already connected");
				closeDialog();
				return;
		}

		BTReaderInfo btReaderInfo = AppHelper.getBTReaderInfo();
		ApiLog.Dbg("connecting BT:" + btReaderInfo.getName() + ", " + btReaderInfo.getAddress());
		if ( btReaderInfo!=null && !"".equals( btReaderInfo.getName() ) )
		{
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice( btReaderInfo.getAddress() );

			mEmvReader.mmDeviceBT.connect(device);

		} else {
			closeDialog();
			MyToast.showToast(mActivity, R.string.bluetooth_not_configured);
		}
	}

	private boolean checkImCalledByExternalUser() {
		Intent intentCaller = getIntent();
		String isCalled = intentCaller.getStringExtra("isCalled");
		if ( isCalled != null && isCalled.equals("true") ) {
			mIsExternalCall = true;
			VanStaticData.setToExit(false);
			VanStaticData.setIsExternalCall(true);
			return true;
		} else {
			mIsExternalCall = false;
			VanStaticData.setToExit(false);
			VanStaticData.setIsExternalCall(true);
			return false;
		}
	}

	private void checkDBBeforeRunningApp()
	{
		MyTaskProgress.CallBackMethod callBackMethod = new MyTaskProgress.CallBackMethod() {
			@Override
			public boolean run() {
				//	init DataBase.
				PayFunDB.InitializeDB( getBaseContext(), mPackageName );		// twice @LoadApp and here again ??

				//	check notification
				if( ! mIsExternalCall )
					NoticeManager.getListWS();

				return true;
			}

			@Override
			public boolean res(boolean result) {
				checkImCalledByExternalUser();
				return true;
			}
		};

		MyTaskProgress progressDialog = new MyTaskProgress(mActivity, callBackMethod, getString(R.string.msg_processing),
				null, android.R.attr.progressBarStyleSmall );
		progressDialog.execute();
	}

	private void ExternalCallPayment()
	{
		if( checkImCalledByExternalUser() )
		{
			mEmvApp.stopNotification();

			final String reqParaJson = getIntent().getStringExtra("reqParaJson");
			ApiLog.Dbg("reqParaJson:" + reqParaJson);
			ReqPara reqPara = ReqPara.fromJsonString(reqParaJson);
			final String userID = reqPara.getUserID();
			final String passWD = reqPara.getPassWD();
			ApiLog.Dbg("userID 0:" + userID);
			doExternalCaller1CheckEmail(userID, passWD, reqParaJson);
		}
	}

	private void doExternalCaller1CheckEmail(String email, String pwd, String reqParaJson)
	{
		//	ToDo:: not yet!
	}

	private class MessageHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MessageID.WAIT_FOR_BT_TURN_ON:
					ApiLog.Dbg("Will close dialog after wait 30s to insert card with paras Total: " + mTotalTime +
							"  isBTReaderConnected: " + AppHelper.AppPref.getIsBTReaderConnected() );
					//		"  isBTReaderConnected: " + VanStaticData.mmIsBTReaderConnected);		changed by David SH Kim.
					mTotalTime -= 5;		// 5 Sec :: delay time
					if	(	(mEmvReader.getEmvReaderType() == IEmvReader.DeviceType.bluetooth) &&
							!AppHelper.AppPref.getIsBTReaderConnected() )
					//		!VanStaticData.mmIsBTReaderConnected )				changed by David SH Kim.
					{
						if(mTotalTime > 0) {
							msg = obtainMessage(MessageID.WAIT_FOR_BT_TURN_ON);
							sendMessageDelayed(msg, 5000);		// 5 Sec :: delay time
							connectBT();
							ApiLog.Dbg("Wait to close diaglog:" + mTotalTime);
						}
						else
						{
							mmIsWaitingTurnOnEmvBTReader = false;
							closeDialog();
						}
					}

					break;

				case MessageID.STOP_SEARCHING:
					mmIsWaitingTurnOnEmvBTReader = false;
					break;

				default:
					break;
			}
		}
	}

	//==========================================
	//	Dialogs in Local
	//==========================================
	private void closeDialog() {
		if (mDialog != null) {
			mDialog.Dismiss();
			mDialog = null;
		}
	}

	private void showDialog() {

		if (mDialog == null || !mDialog.isShowing()) {
			mDialog = new DialogHandler(mActivity, "");
			mDialog.setCancelable(false);
			mDialog.setMessage( getString( R.string.msg_processing) );
			mDialog.Create();
			mDialog.Show();
		}
	}
	private void showDialogProgress(int rsId){
		String msg = getString(rsId);
		showDialogProgress(msg);
	}
	private void showDialogProgress(String msg){
		if( mDialog == null || !mDialog.isShowing() )
		{
			mDialog = new DialogHandler(mActivity, "");
			mDialog.setCancelable(false);
			mDialog.setMessage( msg );
			mDialog.setProgressBar(android.R.attr.progressBarStyleHorizontal);
			mDialog.Create();
			mDialog.Show();
		}
	}
	private void updateDialogMsg(String msg) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.setMessage(msg);
		}
	}

	private void showFallbackDlg(String msg) {
		if(msg==null || msg.equals(""))
			return;
		mFallbackDlg = new DialogHandler(mActivity, IVanString.payment.emv_fallback_report);
		mFallbackDlg.setCancelable(false);
		mFallbackDlg.setIcon(DialogHandler.dialogMode.MODE_WARNING);
		mFallbackDlg.setMessage(msg);
		mFallbackDlg.setPositiveButton( new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		mFallbackDlg.Create();
		mFallbackDlg.Show();
	}

	private void showDownloadConfirm(String msg) {
		if(msg==null || msg.equals(""))
			return;
		mFallbackDlg = new DialogHandler(mActivity, "가맹점 개통");
		mFallbackDlg.setCancelable(false);
		mFallbackDlg.setIcon(DialogHandler.dialogMode.MODE_WARNING);
		mFallbackDlg.setMessage(msg);
		mFallbackDlg.setPositiveButton( new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				//	ToDo:: Profile Activity를 호출하여 바로 설정에 들어 가게 하는것이 좋을지 모르겠다.
				//	현재는 그냥 알림으로 끝내자!!
			}
		});
		mFallbackDlg.Create();
		mFallbackDlg.Show();
	}
	//==========================================
	//	Broadcast Receivers and Listeners
	//==========================================
	private void attachEmvReaderBroadcastReceiver()
	{
		IntentFilter intentFilter = new IntentFilter("InitializationBroadcast");
		registerReceiver(mEmvReaderBroadcastReceiver, intentFilter);
	}
	private void attachBluetoothListener()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

		registerReceiver(mBluetoothReceiver, intentFilter);

	}

	/**
	 * @ Must attach this after registering EmvReaderService.
 	 */
	private void attachDetectEmvServiceListener()
	{
		//	ToDo:: add additional your services
		// if( mIsExternalCall )
		//	mEmvReader.setIsForCancel(true);

		mEmvReader.mmListenerHelper.attachDetectEmvListener( mEmvDetectListener );
	}
	private void detachDetectEmvServiceListener()
	{
		mEmvReader.mmListenerHelper.detachDetectEmvListener( mEmvDetectListener );
	}

	private void detachServices()
	{
		unregisterReceiver(mEmvReaderBroadcastReceiver);		// unregister EmvReader
	//	unregisterReceiver(mBluetoothReceiver);				// unregister Bluetooth
		detachDetectEmvServiceListener();
	}

    private void showNumericKeyboard(EditText editTextAmount)
	{

		LinearLayout kbView = findViewById(R.id.numeric_keyboard_layout);

		ApiEditTextAmount.disableShowSoftInput(editTextAmount);
		ApiEditTextAmount.showKeyboard(this, kbView, editTextAmount);

		ApiEditTextAmount.setTextChangeListener(editTextAmount);
	}

    private boolean isPlayServiceAvailable(Context context, int requestCode)
    {
        boolean ret = false;
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int statusCode = availability.isGooglePlayServicesAvailable(context);
        if (statusCode == ConnectionResult.SUCCESS) {
            ret = true;
        } else {
            if (availability.isUserResolvableError(statusCode)) {
                availability.getErrorDialog(this, statusCode, requestCode).show();
            } else {
				ApiLog.Dbg("This device is not supported.");
                finish();
            }
        }
        return ret;
    }

    private void autoLogin()
	{

		final String phoneNo = MyPhoneNumber.get(this);
		final String deviceID = android.provider.Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		final String fcmToken = AppHelper.AppPref.getFcmToken();
		ApiLog.Dbg("autoLogin");
		final String f_Passwd = AppHelper.AppPref.getUserPassword();				// prefGet(mUserEntityKey.getPassword(), "");

		//case user login before by old way then can not login this time. have to do check login in HomeFragment
		if(!AppHelper.AppPref.getIsLogin() && ! f_Passwd.isEmpty()) {
			ApiLog.Dbg("Already login by old way. so have to update device info first");
			return;
		}

		MyTaskProgress.CallBackMethod callBackMethod = new MyTaskProgress.CallBackMethod() {
			@Override
			public boolean run() {
				mUserID = UserManager.checkLoginV2(phoneNo, deviceID, fcmToken);

				return ( mUserID != null );
			}

			@Override
			public boolean res(boolean result) {
				ApiLog.Dbg("USER_ID: " + mUserID);
				if( ! result )
				{
					AppHelper.AppPref.setUserEmail("");
					AppHelper.AppPref.setUserPassword("");
					return false;
				}

				AppHelper.AppPref.setUserEmail("");
				AppHelper.AppPref.setUserPassword("");
				AppHelper.AppPref.setCurrentUserID(mUserID);
				AppHelper.AppPref.setIsLogin(true);
				if( AppHelper.AppPref.getIsLogin() )
				{
					String userID = AppHelper.AppPref.getCurrentUserID();
					if( !CompanyManger.isExistCompanyLocal(userID) )
					{
						showDownloadConfirm("개통이 필요합니다.");
					}
				}
				return false;
			}
		};

		MyTaskProgress taskProgress = new MyTaskProgress(this, callBackMethod,"Login", null, android.R.attr.progressBarStyleLarge);
		taskProgress.execute();
	}

	/*
	 *  change Fragment page
	 */
	private void changePage(int page)
	{
		mViewPager.setCurrentItem(page);
	}

	private final BroadcastReceiver mEmvReaderBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				if (extras.containsKey("value")) {
					ApiLog.Dbg("Value is:" + extras.get("value"));
					mEmvReader = AppHelper.getEmvReaderInService();
					if( mEmvReader != null)
					{
						if( mIsExternalCall )
							mEmvReader.setIsForCancel(true);

						ApiLog.Dbg("initEmvResources");
						attachDetectEmvServiceListener();
						if( mEmvReader.getEmvReaderType() == IEmvReader.DeviceType.bluetooth )
						{
							connectBT();
							return;
						}
						if( mEmvReader.getEmvReaderType() == IEmvReader.DeviceType.audioPlug )
						{
							// ToDo:: Not support now.
							if( MyReaderDevices.isHeadsetConnected(mActivity) )
							{
								ApiAux.sleep(4000);
								mEmvReader.getDeviceInfo();
							}
						}
						else
						{
							ApiLog.Dbg("!!!!==== Not support DeviceType: " + mEmvReader.getEmvReaderType() );
						}
					} else {
						ApiLog.Dbg("getEmvReaderService is null");
					}
				}
			}
		}
	};

	private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent){

			final String action = intent.getAction();
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			BTReaderInfo btReaderInfo = AppHelper.getBTReaderInfo();
			switch(action)
			{
				case	BluetoothDevice.ACTION_ACL_CONNECTED:
					ApiLog.Dbg("connected BT:" + device.getName() );
					ApiLog.Dbg("saved BT: " + btReaderInfo.getName() );
					if (device.getName().contains(btReaderInfo.getName()) && !"".equals(btReaderInfo.getName())) {

						//VanStaticData.mmIsBTReaderConnected = true;
						AppHelper.AppPref.setIsBTReaderConnected(true);

						MyToast.showToast(mActivity, R.string.bluetooth_connected);
					}
					break;
				case BluetoothDevice.ACTION_ACL_DISCONNECTED:
					ApiLog.Dbg("disconnected BT:" + device.getName());

					//VanStaticData.mmIsBTReaderConnected = false;
					AppHelper.AppPref.setIsBTReaderConnected(false);

					//show show toast for case Reader
					if(device.getName().contains("CHB10"))
						MyToast.showToast(mActivity, R.string.bluetooth_disconnected);
					break;
				case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
					//	ToDo:: Nothing
					break;
				default:
					ApiLog.Dbg("UnRegisted Action Occurs: " + action);
					break;
			}
		}
	};

    //##########################################
    //  Public Variables
    //##########################################

    //##########################################
    //  Private variables
    //##########################################
	private interface MessageID{
		int		WAIT_FOR_BT_TURN_ON	= 0;
		int		STOP_SEARCHING			= 100;
	}


	private final static int				SHOWING_DIALOG_LIMIT = 1;
	private final static int				TOTAL_TIME_LIMIT = 30;

	private Activity						mActivity;
	private EmvApplication					mEmvApp;
    private EmvReader						mEmvReader;


    private int							mShowingDialogCount = 0;
    private int							mTotalTime = 0;

    private String							mPackageName;
	private String							mUserID = "";

	private	 boolean						mIsExternalCall = false;
//	private boolean						mIsBTReaderConnected = false;		move to VanStaticData.
	private boolean						mIsResumeOnMain = false;

	private Handler							mHandler	= new MessageHandler();
	private DialogHandler					mDialog;
	private DialogHandler					mFallbackDlg;

	private UserEntity						mUserEntityKey = new UserEntity(0);

	private EmvDetectListener				mEmvDetectListener = new EmvDetectListener();

    private FragmentCallbackInterface.ActivityToHome        mActivityToHome;
    private FragmentCallbackInterface.ActivityToPaymentCreadit  mActivityToPaymentCreadit;



}
