package com.payfun.van.daou.Activities;

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
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bbpos.bbdevice.PayfunBBDeviceController;
import com.google.firebase.iid.FirebaseInstanceId;
import com.payfun.van.daou.R;
import com.payfun.van.daou.fragments.AMainFragPages;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.ConnectionResult;

import com.payfun.van.daou.fragments.FragmentCallbackInterface;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import ginu.android.library.emv.bbdevice.EmvApplication;
import ginu.android.library.emv.bbdevice.EmvReader;
import ginu.android.library.emv.bbdevice.IEmvReader;
import ginu.android.library.emv.bbdevice.listeners.IDetectEmvListenerCb;
import ginu.android.library.keyboard.ApiEditTextAmount;
import ginu.android.library.keyboard.ApiEditTextCompanyNo;
import ginu.android.library.keyboard.ApiEditTextPhoneNo;
import ginu.android.library.keyboard.KeyboardHandler;
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
import ginu.android.van.app_daou.utils.MyTypeFace;


import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangeHeaderTitle;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangePage;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowCompanyNumericKeyboard;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowNumericKeyboard;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowPhoneNumericKeyboard;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.HomeToActivityCmd_AttachEmvDetectionListener;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.HomeToActivityCmd_DetachEmvDetectionListener;
import static ginu.android.van.app_daou.database.VanStaticData.*;

public class MainActivity extends AppCompatActivity implements
		FragmentCallbackInterface.HomeToActivity,
		FragmentCallbackInterface.PaymentCreditToActivity,
		FragmentCallbackInterface.PaymentCashToActivity,
		FragmentCallbackInterface.CancelSelectorToActivity,
		FragmentCallbackInterface.ReceiptToActivity,
		FragmentCallbackInterface.PrintToActivity,
		FragmentCallbackInterface.CancelCreditToActivity,
		FragmentCallbackInterface.CancelCashToActivity,
		FragmentCallbackInterface.DummyToActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		//
		//	TODO:: Add user code here
		//
		mActivity = this;

		if( ! changePage(AMainFragPages.MainHomePage) )
			return;

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

		dismissNumericKeyboard();		// 2. destroy key board

		detachServices();

		if (mEmvApp != null)
			mEmvApp.stopEmvReaderService();
	}

	@Override
	public void onBackPressed()
	{
	//	super.onBackPressed();

		hideNumericKeyboard();		// 1. hide key board

		if( MainActivityFragmentMapper.atHome() )
			showDownloadFinish("앱을 종료하시겠습니까?");
		else
			changePage(AMainFragPages.MainHomePage);
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

    public void paymentCreditToActivityCb(int cmd, Object obj)
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

	public void paymentCashToActivityCb(int cmd, Object obj)
	{
		switch(cmd)
		{
			case    CommonFragToActivityCmd_ChangePage:
				int page = (int)obj;
				changePage(page);
				break;
			case	CommonFragToActivityCmd_ChangeHeaderTitle:
				String title = (String)obj;
				setHeaderView(title);
				break;
			case 	CommonFragToActivityCmd_ShowNumericKeyboard:
				showNumericKeyboard((EditText)obj);
				break;
			case CommonFragToActivityCmd_ShowPhoneNumericKeyboard:
				showPhoneNumberKeyboard( (EditText) obj);
				break;
			case CommonFragToActivityCmd_ShowCompanyNumericKeyboard:
				showCompanyNumberKeyboard( (EditText) obj);
				break;
			default:
				break;
		}
	}

	public void cancelSelectorToActivityCb(int cmd, Object obj)
	{
		switch(cmd)
		{
			case    CommonFragToActivityCmd_ChangePage:
				int page = (int)obj;
				changePage(page);
				break;
			default:
				break;
		}
	}

	public void receiptToActivityCb(int cmd, Object obj)
	{
		switch(cmd)
		{
			case    CommonFragToActivityCmd_ChangePage:
				int page = (int)obj;
				changePage(page);
				break;
			case	CommonFragToActivityCmd_ChangeHeaderTitle:
				String title = (String)obj;
				setHeaderView(title);
				break;
			default:
				break;
		}
	}

	public void printToActivityCb(int cmd, Object obj)
	{
		switch(cmd)
		{
			case    CommonFragToActivityCmd_ChangePage:
				int page = (int)obj;
				changePage(page);
				break;

			default:
				break;
		}
	}

	public void cancelCreditToActivityCb(int cmd, Object obj)
	{
		switch(cmd)
		{
			case    CommonFragToActivityCmd_ChangePage:
				int page = (int)obj;
				changePage(page);
				break;
			default:
				break;
		}
	}
	public void cancelCashToActivityCb(int cmd, Object obj)
	{
		switch(cmd)
		{
			case    CommonFragToActivityCmd_ChangePage:
				int page = (int)obj;
				changePage(page);
				break;
			default:
				break;
		}
	}
	/*		<<	==	for Dummy Fragment	==	>>		*/
	//		Must mask on release
	public void dummyToActivityCb(int cmd, Object obj)
	{
		switch(cmd)
		{
			case    CommonFragToActivityCmd_ChangePage:
				int page = (int)obj;
				changePage(page);
				break;

			default:
				break;
		}
	}
    //==========================
    //  Fragment Page Adapter
    //==========================
	/*
	 *  change Fragment page
	 **/
	public static boolean changePage(@AMainFragPages int page)
	{
		return MainActivityFragmentMapper.changePage(mActivity, page, null);
	}

	public static void setHeaderView(String title)
	{
		FrameLayout headerView = mActivity.findViewById(R.id.viewHeader);
		TextView tvHeaderTitle = mActivity.findViewById(R.id.tvHeaderTitle);
		if(title == null) {
			headerView.setBackgroundResource(R.drawable.bg_title_no_text);
			tvHeaderTitle.setText("");
			return;
		}

		headerView.setBackgroundResource(R.drawable.bg_title_text);
		MyTypeFace.setTypeFace(mActivity, headerView);
		tvHeaderTitle.setText(title);
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

			String batteryLevel = deviceInfoData.get("batteryPercentage");
			String hwModelInfo = EmvUtils.getHWModelName() + " " + EmvUtils.getHwModelNo();
			String swModelInfo = DaouDataContants.SWModelName + " " + DaouDataContants.SWModelNo;

			//	TodDo::	update main status bar.
			Bundle bundle = new Bundle();
			bundle.putString(MessageKeys.CommDevice , CommunicationDevice.BLUETOOTH_READER);
			bundle.putBoolean(MessageKeys.IsConnected, true);
			bundle.putInt( MessageKeys.BatteryLevel, Integer.parseInt(batteryLevel) );
			sendMessage(MessageID.UPDATE_MAIN_STATUS_BAR, bundle);

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
			ApiLog.Dbg("current volume:"+ MyReaderDevices.getCurrentVolume(mActivity) );

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

			//	ToDo::	update main status bar / Disconnected.
			Bundle bundle = new Bundle();
			bundle.putString(MessageKeys.CommDevice , CommunicationDevice.BLUETOOTH_READER);
			bundle.putBoolean(MessageKeys.IsConnected, false);
			bundle.putInt( MessageKeys.BatteryLevel, 0 );
			sendMessage(MessageID.UPDATE_MAIN_STATUS_BAR, bundle);

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
	private String mapBatteryLevelToLMH(int batteryLevel)
	{
		if( 0 <= batteryLevel && batteryLevel < 30 )
			return "LOW";
		else if( batteryLevel >= 30 && batteryLevel < 70 )
			return "MIDDLE";
		else if( batteryLevel <= 100)
			return "HIGH";

		return "unknown";
	}

	private void updateMainStatusBar(String whatDevice, boolean connected, int batteryLevel)
	{
		String levelLMH;
		LinearLayout lLayout;
		switch(whatDevice)
		{
			case	CommunicationDevice.BLUETOOTH_READER:
				if(mImageBleDongleConnectionStatus == null || mImageBleDongleBatteryStatus == null || mTvBleDongleBatteryLevel == null)
				{	// ToDo:: find resources
					lLayout = findViewById(R.id.layout_main_status_bar);
					lLayout = lLayout.findViewById(R.id.layout_main_status_bluetooth);
					mImageBleDongleConnectionStatus = lLayout.findViewById(R.id.iv_main_status_bluetooth_conn);
					mImageBleDongleBatteryStatus = lLayout.findViewById(R.id.iv_main_status_bluetooth_battery);
					mTvBleDongleBatteryLevel = lLayout.findViewById(R.id.tv_main_status_bluetooth_battery_level);
				}
				if(connected)
					mImageBleDongleConnectionStatus.setImageDrawable( getResources().getDrawable(R.drawable.iconconnection_a) );
				else
					mImageBleDongleConnectionStatus.setImageDrawable( getResources().getDrawable(R.drawable.iconconnection) );

				levelLMH = mapBatteryLevelToLMH(batteryLevel);
				switch(levelLMH)
				{
					case	"LOW":
						mImageBleDongleBatteryStatus.setImageDrawable( getResources().getDrawable( R.drawable.battery_green) );
						break;
					case "MIDDLE":
						mImageBleDongleBatteryStatus.setImageDrawable( getResources().getDrawable( R.drawable.battery_green) );
						break;
					case	"HIGH":
						mImageBleDongleBatteryStatus.setImageDrawable( getResources().getDrawable( R.drawable.battery_green) );
						break;
					default:
						break;
				}
				mTvBleDongleBatteryLevel.setText(""+batteryLevel);
				break;

			case	CommunicationDevice.BLUETOOTH_PRINT:
				if(mImageBlePrinterConnectionStatus == null || mImageBlePrinterBatteryStatus == null || mTvBlePrinterBatteryLevel == null)
				{	// ToDo:: find resources
					lLayout = findViewById(R.id.layout_main_status_bar);
					lLayout = lLayout.findViewById(R.id.layout_main_status_printer);
					mImageBlePrinterConnectionStatus = lLayout.findViewById(R.id.iv_main_status_printer_conn);
					mImageBlePrinterBatteryStatus = lLayout.findViewById(R.id.iv_main_status_printer_battery);
					mTvBlePrinterBatteryLevel = lLayout.findViewById(R.id.tv_main_status_printer_battery_level);
				}
				if(connected)
					mImageBlePrinterConnectionStatus.setImageDrawable( getResources().getDrawable(R.drawable.iconconnection_a) );
				else
					mImageBlePrinterConnectionStatus.setImageDrawable( getResources().getDrawable(R.drawable.iconconnection) );

				levelLMH = mapBatteryLevelToLMH(batteryLevel);
				switch(levelLMH)
				{
					case	"LOW":
						mImageBlePrinterBatteryStatus.setImageDrawable( getResources().getDrawable( R.drawable.battery_green) );
						break;
					case "MIDDLE":
						mImageBlePrinterBatteryStatus.setImageDrawable( getResources().getDrawable( R.drawable.battery_green) );
						break;
					case	"HIGH":
						mImageBlePrinterBatteryStatus.setImageDrawable( getResources().getDrawable( R.drawable.battery_green) );
						break;
					default:
						break;
				}
				mTvBlePrinterBatteryLevel.setText(""+batteryLevel);
				break;
			default:
				break;
		}
	}
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

	private void sendMessage( int userPrim, Bundle data)
	{
		//	ToDo:: transmit signal message to user app.
		//???????????????????????? David SH Kim. not yet!! ?????????????

		Message msg = mHandler.obtainMessage();
		msg.what = userPrim;
		msg.setData(data);
		mHandler.sendMessage(msg);
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

				case MessageID.UPDATE_MAIN_STATUS_BAR:
					Bundle bundle = msg.getData();
					String whatDevice = bundle.getString("whatDevice", CommunicationDevice.BLUETOOTH_READER);
					Boolean isConnected = AppHelper.AppPref.getIsBTReaderConnected();
					int batteryLevel = bundle.getInt("batteryLevel");
					updateMainStatusBar(whatDevice, isConnected, batteryLevel);
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
			mDialog = new DialogHandler(mActivity, IVanString.device.device_title);
			mDialog.setCancelable(false);
			mDialog.setMessage( IVanString.device.device_trying_connection );
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
	private void showDownloadFinish(String msg) {
		if(msg==null || msg.equals(""))
			return;
		mFallbackDlg = new DialogHandler(mActivity, "앱 종료");
		mFallbackDlg.setCancelable(false);
		mFallbackDlg.setIcon(DialogHandler.dialogMode.MODE_WARNING);
		mFallbackDlg.setMessage(msg);
		mFallbackDlg.setPositiveButton( new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				//	ToDo:: exit
				finish();
			}
		});
		mFallbackDlg.setNegativeButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				changePage(AMainFragPages.MainHomePage);
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

		if( KeyboardHandler.isShow() ) {
			ApiLog.Dbg("MainActivity" + "keyboard already runs");
			return;
		}
		ApiEditTextAmount.disableShowSoftInput(editTextAmount);
		ApiEditTextAmount.showKeyboard(this, kbView, editTextAmount);

		ApiEditTextAmount.setTextChangeListener(editTextAmount);
	}

	private void showPhoneNumberKeyboard(EditText editText)
	{
		LinearLayout kbView = findViewById(R.id.numeric_keyboard_layout);

		if( KeyboardHandler.isShow() ) {
			ApiLog.Dbg(Tag + "keyboard already runs");
			return;
		}

		ApiEditTextPhoneNo.disableShowSoftInput(editText);
		ApiEditTextPhoneNo.showKeyboard(this, kbView, editText);

		ApiEditTextPhoneNo.setTextChangeListener(editText);

	}

	private void showCompanyNumberKeyboard(EditText editText)
	{
		LinearLayout kbView = findViewById(R.id.numeric_keyboard_layout);

		if( KeyboardHandler.isShow() ) {
			ApiLog.Dbg(Tag + "keyboard already runs");
			return;
		}

		ApiEditTextCompanyNo.disableShowSoftInput(editText);
		ApiEditTextCompanyNo.showKeyboard(this, kbView, editText);

		ApiEditTextCompanyNo.setTextChangeListener(editText);
	}

	private void dismissNumericKeyboard()
	{
		ApiEditTextAmount.dissmissKeyboard();
	}

	private void hideNumericKeyboard()
	{
		ApiEditTextAmount.hideKeyboard(mActivity, this);
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

						AppHelper.AppPref.setIsBTReaderConnected(true);

					}
					break;
				case BluetoothDevice.ACTION_ACL_DISCONNECTED:
					ApiLog.Dbg("disconnected BT:" + device.getName());

					if(device.getName().contains(btReaderInfo.getName())){
						AppHelper.AppPref.setIsBTReaderConnected(false);
						MyToast.showToast(mActivity, R.string.bluetooth_disconnected);
					}

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
	private final String Tag = String.format("[%s] ", MainActivity.class.getSimpleName() );
	private interface MessageID{
		int		WAIT_FOR_BT_TURN_ON		= 100;
		int		STOP_SEARCHING			= 101;
		int		UPDATE_MAIN_STATUS_BAR		= 110;
	}

	private interface MessageKeys{
		String	CommDevice				= "whatDevice";
		String	IsConnected			= "isConnected";
		String	BatteryLevel			= "batteryLevel";
	}

	private interface CommunicationDevice{
		String	BLUETOOTH_READER		= "bluetoothReader";
		String	BLUETOOTH_PRINT		= "bluetoothPrinter";
		String	EARJACK_DONGLE		= "earJackDongle";
	}

	private final static int				SHOWING_DIALOG_LIMIT = 1;
	private final static int				TOTAL_TIME_LIMIT = 30;

	private static AppCompatActivity		mActivity;
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
    private FragmentCallbackInterface.ActivityToPaymentCredit  mActivityToPaymentCredit;

	private ImageView						mImageBleDongleConnectionStatus, mImageBleDongleBatteryStatus;
	private ImageView						mImageBlePrinterConnectionStatus, mImageBlePrinterBatteryStatus;
	private TextView						mTvBleDongleBatteryLevel, mTvBlePrinterBatteryLevel;

}
