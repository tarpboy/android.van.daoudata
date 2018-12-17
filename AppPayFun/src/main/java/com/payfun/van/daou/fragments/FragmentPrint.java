package com.payfun.van.daou.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.payfun.van.daou.R;

import java.io.File;
import java.lang.reflect.Type;

import ginu.android.library.print.APrintItemKeys;
import ginu.android.library.utils.common.ApiAux;
import ginu.android.library.utils.common.ApiBitmap;
import ginu.android.library.utils.common.ApiExtStorage;
import ginu.android.library.utils.common.ApiLog;
import ginu.android.van.app_daou.BaseFragment.FragmentPrintBase;
import ginu.android.van.app_daou.database.VanStaticData;
import ginu.android.van.app_daou.entity.ReceiptEntity;
import ginu.android.van.app_daou.utils.MyToast;

import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangePage;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_HideSoftKeyboard;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_StopAppToReturnExtCaller;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.PrintToActivityCmd_PrinterBluetoothConnected;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.PrintToActivityCmd_PrinterBluetoothDisconnected;
import static ginu.android.library.utils.gui.IFragmentConstant.ARG_SECTION_NUMBER;

/**
 * Created by david_shkim on 2018-03-13.
 */

public class FragmentPrint extends FragmentPrintBase implements FragmentCallbackInterface.ActivityToPrint
{

    /**
	 * Fragment life: step 1
	 * @brief called on attaching this fragment to Activity.
	 * @param context
	 */
    // ToDo:: Enable, if you want to use callback interface.
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);


		// U make sure that the container hsa implemented the callback interface.
		try {
			mCallback = (FragmentCallbackInterface.PrintToActivity) mmActivity;
		} catch (ClassCastException e) {
			throw new ClassCastException(mmActivity.toString()
					+ "U must implement CallbackListenerOnBenefit");
		}

	}
	// ToDo:: End of OnAttach

	/**
	 * Fragment life: step 2
	 * @brief   called on creating this fragment by system.
	 *          very like as that of Activity.
	 *          initialize essential components for paused, stopped, and resumed.
	 * @param savedInstanceState
	 */
/*	// ToDo:: Enable, if you want to initialize essential components.
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//  initialize essential components here
		initComponents();
	}
*/	// ToDo:: End of onCreate

	/**
	 * Fragment life: step 3
	 * @breif   called on creating a view by system.
	 *          do layout
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	// ToDo:: do layout the fragment on container.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		//  If activity recreated (such as from screen rotate),
 		//  restore the previous article selection set by onSaveInstanceState().
		if(savedInstanceState != null)
			mSectionNumber = savedInstanceState.getInt(ARG_SECTION_NUMBER);
		else {
			Bundle args = getArguments();
			if(args != null)
				mSectionNumber = args.getInt(ARG_SECTION_NUMBER);
		}

		ApiLog.Dbg(getString( R.string.fragment_section_format, mSectionNumber) + "onCreateView");

		//  allocate the dummy fragment onto container
		mmFragmentView = inflater.inflate(R.layout.fragment_print, container, false);

		//  set elements on the fragment
		setView(inflater, container, mmFragmentView);

		//  Inflate the layout for this fragment
		return mmFragmentView;
	}
	// ToDo:: End of onCreateView

	/**
	 * Fragment life: step 4
	 * @brief   called on creating a fragment by system completely .
	 *          can change all view on the fragment now.
	 * @param savedInstanceState
	 */
	// ToDo:: change view of fragment on container.
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		updateView(mmFragmentView);

	}
	// ToDo:: End of onActivityCreated

	/**
	 * Fragment life: step 5
	 * @breif   called on display a view by system.
	 *          no user Action.
	 */
/*	//  ToDo:: display your fragment on container.
	@Override
	public void onStart()
	{
		super.onStart();
	}
*/	// ToDo: End of onStart

	/**
	 * Fragment life: step 6
	 * @breif   called on display a view by system completely..
	 *          no user Action.
	 */
	//  ToDo:: display your fragment on container.
	@Override
	public void onResume()
	{
		super.onResume();
		ApiLog.Dbg(getString( R.string.fragment_section_format, mSectionNumber) + "onResume");
		showPrintView();
	}
	//  ToDo:: End of onResume

	/**
	 * Fragment life: step 7
	 * @breif   called on removing/replaced the fragment..
	 *          no user Action.
	 */
/*	//  ToDo:: remove your fragment on container.
 	@Override
	public void onPause()
	{
		super.onPause();
	}
*/	//  ToDo:: End of onPause

	/**
	 * Fragment life: step 8
	 * @breif   called on the fragment is stopped.
	 *          no user Action.
	 */
/*	//  ToDo:: stop your fragment on container.
	@Override
	public void onStop()
	{
		super.onStop();
	}
*/	//  ToDo:: End of onStop

	/**
	 * Fragment life: step 9
	 * @breif   called on the fragment is destroyed.
	 *          no user Action.
	 */
/*	//  ToDo:: Destroy your fragment on container.
	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
	}
*/	//  ToDo:: End of onDestroyView

	/**
	 * Fragment life: step 10
	 * @breif   called on the fragment destroyed completely.
	 *          no user Action.
	 */
/*	//  ToDo:: Destroy your fragment on container.
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		ApiLog.Dbg(getString( R.string.section_format, mSectionNumber) + "onDestroy");
	}
*/	//  ToDo:: End of onDestroy

	/**
	 * Fragment life: step 11
	 * @breif   called on the fragment is detached completely.
	 *          no user Action.
	 */
	//  ToDo:: Detach your fragment on container.
	@Override
	public void onDetach()
	{
		super.onDetach();
		ApiLog.Dbg(getString( R.string.fragment_section_format, mSectionNumber) + "onDetach");
	}
	//  ToDo:: End of onDetach

	/**
	 * Fragment life: step 12
	 * @breif   called on the fragment disappeared and activity disappeared simultaneously.
	 *          save the status onto Bundle and get it when return.
	 * @param savedInstanceState
	 */
/*	//  ToDo:: save status of fragment onto Bundle.
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{

		super.onSaveInstanceState(savedInstanceState);
	}
*/	//  ToDo:: End of onSaveInstanceState


	/**
	 * @brief   callback function:
	 *     - parent Activity tx data to this fragment
	 */

	public void activityToPrintCb(int cmd, Object obj) {
		switch(cmd)
		{
			//case    ActivityToHomeCmd_DeviceAdapter:
			//    mDeviceAdapter = (DeviceAdapter) obj;       //deviceAdapter;
			//   break;

			default:
				break;
        }
	}

	private void printToActivity(int cmd, Object obj)
    {
        mCallback.printToActivityCb(cmd, obj);
    }

    @Override
	protected void doPrinterBtConnected()
	{
		printToActivity(PrintToActivityCmd_PrinterBluetoothConnected, 50);
		doPrintOut();
	}
	@Override
	protected void doPrinterBtDisconnected()
	{
		printToActivity(PrintToActivityCmd_PrinterBluetoothDisconnected, 0);
	}

	///================================
	// *  private methods
	//=================================
	private void setView(LayoutInflater inflater, ViewGroup container, View view) {

		//  ToDo:: set all event listeners like button on click listener if you have
		Button btn = (Button)mmFragmentView.findViewById(R.id.btn_foot_confirm);
		btn.setText( getString( R.string.print_button_ok) );
		btn.setOnClickListener(mButtonListener);

		btn = mmFragmentView.findViewById(R.id.btn_foot_cancel);
		btn.setText( getString( R.string.print_button_cancel) );
		btn.setOnClickListener(mButtonListener);

		mPrintViewLayout = mmFragmentView.findViewById(R.id.fragment_print_view);
		return;
	}

	private void updateView(View view)
	{
		// ToDo: update any view element you want

		showSelectPrintoutModePopup();

		//connectPrinter();
	}

	private View.OnClickListener mButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case	R.id.btn_foot_cancel:
					// ToDo:: go to Home
					//printToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.MainHomePage);
					printToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.ReceiptViewPage);
					break;
				case	R.id.btn_foot_confirm:
					// ToDo:: print out
					if( VanStaticData.getIsExternalCall() )
						printToActivity(CommonFragToActivityCmd_StopAppToReturnExtCaller, null);
					else
						printToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.MainHomePage);
					break;
				default:
					break;
			}
		}
	};

	private void showPrintView()
	{
		ReceiptEntity receiptEntity = getReceiptEntityFromVanStaticData();
		makePrintData(receiptEntity);
	}

	private boolean doPrintOut()
	{
		String msg = "";
		String title	= mmTablePrint.get(APrintItemKeys.item_0).value;	// Title
		msg	+=	PrintPaperSubtitle.type +
				mmTablePrint.get(APrintItemKeys.item_7).value + "\n";		// 거래요청
		msg	+=	PrintPaperSubtitle.cardType +
				mmTablePrint.get(APrintItemKeys.item_2).value + "\n";		// 카드종류
		msg	+=	PrintPaperSubtitle.cardNo +
				mmTablePrint.get(APrintItemKeys.item_3).value + "\n";		// 카드번호
		msg	+=	PrintPaperSubtitle.requestDate +
				mmTablePrint.get(APrintItemKeys.item_5).value + "\n";		// 거래요청일시
		msg	+=	PrintPaperSubtitle.approvalNo +
				mmTablePrint.get(APrintItemKeys.item_25).value + "\n";		// 승인번호
		msg +=	PrintPaperSubtitle.amount +
				mmTablePrint.get(APrintItemKeys.item_11).value + "\n";		// 거래금액
		msg	+=	PrintPaperSubtitle.tax +
				mmTablePrint.get(APrintItemKeys.item_14).value + "\n";		// 세금
		msg +=	PrintPaperSubtitle.serviceCharge +
				mmTablePrint.get(APrintItemKeys.item_17).value + "\n";		// TIP
		msg	+=	PrintPaperSubtitle.totalAmount +
				mmTablePrint.get(APrintItemKeys.item_23).value + "\n";		// 합계
		msg	+=	PrintPaperSubtitle.installment +
				mmTablePrint.get(APrintItemKeys.item_15).value + "\n";		// 할부
		msg	+=	PrintPaperSubtitle.companyNo +
				mmTablePrint.get(APrintItemKeys.item_26).value + "\n";		// 사업자등록번호
		msg	+=	PrintPaperSubtitle.companyName +
				mmTablePrint.get(APrintItemKeys.item_27).value + "\n";		// 회사명
		msg	+=	PrintPaperSubtitle.companyPhone +
				mmTablePrint.get(APrintItemKeys.item_30).value + "\n";		// 가맹점전화번호
		msg	+=	PrintPaperSubtitle.companyAddress +
				mmTablePrint.get(APrintItemKeys.item_28).value + "\n";		// 가맹점주소

		if( ! printText(title, msg) )
			return false;
		if( ! printImage(mmPastSign) )
			return false;

		return true;
	}

	/**
	 * show PrintOutModeDialog to select a mode
	 */
	private void showSelectPrintoutModePopup()
	{
		mDialogPrintOutMode = new DialogPrintOutMode(mmActivity);

		/*
		 *	add button listeners
		 */
		mDialogPrintOutMode.setButtonListener(DialogPrintOutMode.IPrintOutModeDialogButtons.cancel, mBtnCancelListener);
		mDialogPrintOutMode.setButtonListener(DialogPrintOutMode.IPrintOutModeDialogButtons.confirm, mBtnOkListener);
		mDialogPrintOutMode.setButtonListener(DialogPrintOutMode.IPrintOutModeDialogButtons.email, mBtnEmailListener);
		mDialogPrintOutMode.setButtonListener(DialogPrintOutMode.IPrintOutModeDialogButtons.sms, mBtnSmsListener);
		mDialogPrintOutMode.setButtonListener(DialogPrintOutMode.IPrintOutModeDialogButtons.printer, mBtnPrinterListener);
		mDialogPrintOutMode.show();
	}

	private View.OnClickListener mBtnSmsListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mDialogPrintOutMode.setInputMode(DialogPrintOutMode.IPrintOutModeDialogButtons.sms);
		}
	};
	private View.OnClickListener mBtnEmailListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mDialogPrintOutMode.setInputMode(DialogPrintOutMode.IPrintOutModeDialogButtons.email);
		}
	};
	private View.OnClickListener mBtnPrinterListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mDialogPrintOutMode.setInputMode(DialogPrintOutMode.IPrintOutModeDialogButtons.printer);
			connectPrinter();
			mDialogPrintOutMode.dismiss();			// finish
		}
	};
	private View.OnClickListener mBtnCancelListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mDialogPrintOutMode.dismiss();			// finish
		}
	};

	private View.OnClickListener mBtnOkListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String info = null;
			Bundle bundle = null;
			switch( mDialogPrintOutMode.getSelectedMode() )
			{
				case	DialogPrintOutMode.IPrintOutModeDialogButtons.email:
					info = mDialogPrintOutMode.getEmailInfo();
					if( info.equals("") )
					{
						MyToast.showToast(mmActivity, "EMAIL 주소를 입력하지 않았습니다.\n 다시 입력해주세요");
						break;
					}

					mDialogPrintOutMode.hideSoftKeyboardOnDialog(mmActivity);

					//	ToDo:: request to send email
					bundle = new Bundle();
					bundle.putString(UserMessageKey.emailAddress, info);
					sendMessage(UserMessageID.MESSAGE_SEND_EMAIL_REQ, bundle);

					mDialogPrintOutMode.dismiss();			// finish
					break;
				case	DialogPrintOutMode.IPrintOutModeDialogButtons.sms:
					info = mDialogPrintOutMode.getSmsInfo();
					if( info.equals("") )
					{
						MyToast.showToast(mmActivity, "전화번호를 입력하지 않았습니다.\n 다시 입력해주세요");
						break;
					}

					//	ToDo:: request to send sms
					bundle = new Bundle();
					bundle.putString(UserMessageKey.phoneNumber, info);
					sendMessage(UserMessageID.MESSAGE_SEND_SMS_REQ, bundle);

					mDialogPrintOutMode.dismiss();			// finish
					break;
				case	DialogPrintOutMode.IPrintOutModeDialogButtons.printer:
					connectPrinter();
					mDialogPrintOutMode.dismiss();			// finish
					break;
			}
		}
	};

	private void sendMessage( int userPrim, Bundle data)
	{
		//	ToDo:: transmit signal message to MainActivity.

		if(data == null)
		{	//	ToDo:: tx message without data
			mHandler.sendEmptyMessage(userPrim);
		}
		else
		{	//	ToDo:: message with data
			Message msg = mHandler.obtainMessage();
			msg.what = userPrim;
			msg.setData(data);
			mHandler.sendMessage(msg);
		}

	}

	private String getEmailContentSale() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	private File getBitmapFile() {
		final Bitmap tmpBitmap = Bitmap.createBitmap(mPrintViewLayout.getWidth(), mPrintViewLayout.getHeight(), Bitmap.Config.ARGB_8888);
		mPrintViewLayout.draw(new Canvas(tmpBitmap));
		final File file = new File(ApiExtStorage.getExSD(null) + "email.png");
		ApiBitmap.saveBitmap(tmpBitmap, file);
		return file;
	}

	private void sendEmail(String info)
	{
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{info});
		String subject = mmTablePrint.get(APrintItemKeys.item_27).value;				// 회사명
		subject += "가 발행한 영수증입니다.";
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		email.setType("image/png");
		email.putExtra(Intent.EXTRA_TEXT, getEmailContentSale() );
		email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(getBitmapFile()));
		mmActivity.startActivity(Intent.createChooser(email, "Choose an Email client: "));
	}
	private void sendSMS(String info)
	{
		Intent sms = new Intent(Intent.ACTION_SEND);
		sms.setType("image/png");
		sms.putExtra("address", info);
		sms.putExtra("exit_on_sent", true);
		sms.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(getBitmapFile()));
		mmActivity.startActivity(sms);
	}

	private class MessageHandler extends Handler {
		public void handleMessage(Message msg) {
			Bundle bundle;
			switch (msg.what) {
				case	UserMessageID.MESSAGE_SEND_EMAIL_REQ:
					bundle = msg.getData();
					String emailAddr = bundle.getString(UserMessageKey.emailAddress);
					ApiAux.sleep(300);			// need a delay to capture
					sendEmail(emailAddr);
					break;
				case	UserMessageID.MESSAGE_SEND_SMS_REQ:
					bundle = msg.getData();
					String phoneNo = bundle.getString(UserMessageKey.phoneNumber);
					ApiAux.sleep(300);
					sendSMS(phoneNo);
					break;
			}
		}
	}

	//##########################################
	//  private variables
	//##########################################
	private interface UserMessageID{
		int MESSAGE_SEND_SMS_REQ		= 200;
		int MESSAGE_SEND_EMAIL_REQ	= 201;
	}
	private interface UserMessageKey{
		String emailAddress		= "emailAddress";
		String phoneNumber			= "phoneNumber";
	}

	private interface PrintPaperSubtitle{
		String type					= "거래요청  | ";
		String cardType			= "카드종류  | ";
		String cardNo				= "카드번호  | ";
	//	String cardTerm			= "유효기간  | ";
		String requestDate			= "거래일시  | ";
		String cancelDate			= "취소일시  | ";
		String subType				= "거래유형  | ";
		String amount				= "금    액  | ";
		String tax					= "세    금  | ";
		String installment			= "할부개월  | ";
		String serviceCharge		= "봉 사 료  | ";
		String buyerName			= "매 입 처  | ";
		String cardIssuer			= "카 드 사  | ";
		String totalAmount			= "합    계  | ";
		String approvalNo			= "승인번호  | ";
		String companyNo			= "사업자번호| ";
		String companyName			= "가맹점명  | ";
		String companyAddress		= "가맹점주소| ";
		String companyPhone		= "문    의  | ";
	}
	/*
	 *  To communicate with parent activity
	 *  #1. declare callback
	 */
	private FragmentCallbackInterface.PrintToActivity mCallback;

	private static int mSectionNumber = -1;

	private ScrollView 					mPrintViewLayout;				// to make print bmp.
	private DialogPrintOutMode				mDialogPrintOutMode;

	private MessageHandler					mHandler = new MessageHandler();
}
