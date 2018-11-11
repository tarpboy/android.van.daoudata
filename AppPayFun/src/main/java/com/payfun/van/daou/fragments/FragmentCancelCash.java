package com.payfun.van.daou.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payfun.van.daou.R;

import java.util.Hashtable;

import ginu.android.library.keyboard.ApiEditTextCardNo;
import ginu.android.library.keyboard.ApiEditTextCompanyNo;
import ginu.android.library.keyboard.ApiEditTextPhoneNo;
import ginu.android.library.keyboard.KeyboardHandler;
import ginu.android.library.utils.common.ApiDate;
import ginu.android.library.utils.common.ApiLog;
import ginu.android.library.utils.common.ApiString;
import ginu.android.library.utils.security.ApiBase64;
import ginu.android.van.app_daou.BaseFragment.FragmentPaymentBase;
import ginu.android.van.app_daou.cardreader.EmvUtils;
import ginu.android.van.app_daou.cardreader.IEmvUserMessages;
import ginu.android.van.app_daou.daou.CashReceipt;
import ginu.android.van.app_daou.daou.CreditCard;
import ginu.android.van.app_daou.daou.DaouDataContants;
import ginu.android.van.app_daou.database.IVanSpecification;
import ginu.android.van.app_daou.database.VanStaticData;
import ginu.android.van.app_daou.entity.EncPayInfo;
import ginu.android.van.app_daou.entity.ReceiptEntity;
import ginu.android.van.app_daou.helper.AppHelper;
import ginu.android.van.app_daou.helper.VanHelper;
import ginu.android.van.app_daou.utils.DialogCancelList;
import ginu.android.van.app_daou.utils.IVanString;
import ginu.android.van.app_daou.utils.MyToast;
import ginu.android.van.app_daou.utils.PaymentTask;

import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangePage;
import static ginu.android.library.utils.gui.IFragmentConstant.ARG_SECTION_NUMBER;

/**
 * Created by david_shkim on 2018-03-13.
 */

public class FragmentCancelCash extends FragmentPaymentBase implements FragmentCallbackInterface.ActivityToCancelCash
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

		/*
		 *  To communicate with parent activity
		 *  #2. assign callback function
		 */

		// U make sure that the container hsa implemented the callback interface.
		try {
			mCallback = (FragmentCallbackInterface.CancelCashToActivity) mmActivity;
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
	// ToDo:: Enable, if you want to initialize essential components.
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//  initialize essential components here
		initComponents();
	}
	// ToDo:: End of onCreate

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
		mFragmentView = inflater.inflate(R.layout.fragment_cancel_cash, container, false);

		//  set elements on the fragment
		setView(inflater, container, mFragmentView);

		//  Inflate the layout for this fragment
		return mFragmentView;
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

		updateView(mFragmentView);

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

	public void activityToCancelCashCb(int cmd, Object obj) {
		switch(cmd)
		{
			//case    ActivityToHomeCmd_DeviceAdapter:
			//    mDeviceAdapter = (DeviceAdapter) obj;       //deviceAdapter;
			//   break;

			default:
				break;
        }
	}

	private void cancelCashToActivity(int cmd, Object obj)
    {
        mCallback.cancelCashToActivityCb(cmd, obj);
    }

	//==========================================
	//	EmvReaderFragCallbackMethod
	//==========================================
	private class UserEmvReaderFragCb implements EmvReaderFragCallbackMethod {
		/**
		 * set Amount to response to the Set Amount Request of device.
		 * @param value		response container
		 * @return				accept / denny
		 */
		public boolean setAmountResp(Hashtable<String, Object> value)
		{
			//	ToDo:: Nothing in here
			return true;
		}

		/**
		 * do check card as a request of device
		 */
		public void doCheckCard(String strCheckCardMode)
		{
			//	ToDo:: Fallback ??? really Need ???
			startCheckCard(strCheckCardMode);
		}

		public void doStartEmv(String strCheckCardMode)
		{
			//	ToDo:: Start get Emv Card Data
			getEmvCardData();					// call FragmentPaymentBase.getEmvCardData()
		}

		public void endCardReader(Hashtable<String, Object> result)
		{
			//	ToDo:: do onlineProgress
			boolean finalResult = (boolean) result.get(IEmvUserMessages.UserKeys.finalResult);

			if(finalResult)
			{
				String cardNo = mmTrack2Data;		// added by David SH Kim. Swipe data

				findUsageHistory(cardNo);			// (mmBankCardData); changed by David SH Kim.
			}
			else
			{
				showDialog("카드리드오류");
			}
		}

		public void doUserTransactionResult(String result)
		{
			if( "DECLINED".equalsIgnoreCase(result) )
			{
				mEdCardNo.setText( "" );
				resetToPayAgain(true);
				showVanDisplayMessage( AppHelper.AppPref.getVanMsg() );
			}
			else
			if( "APPROVED".equalsIgnoreCase(result)) {
				doTransactionComplete();
			}
		}

		public void doEmvCardDataResult(boolean isSuccess)
		{
			//	ToDo:: Nothing in here
		}

		public void doEncryptedKeyInCardNoResult(boolean isSuccess, String message)
		{
			if( isSuccess )
				doOnLineProgress();
			else
				showDialog(message);
		}
	}

	private void doTransactionComplete( )
	{
		//	ToDo:: save receipt entity in json.
		ReceiptEntity receiptEntity = AppHelper.getReceiptEntity();
		String receiptEnJson = VanHelper.cancel(receiptEntity);        // make receiptEntity to Json data
		if (receiptEnJson != null)
			VanStaticData.setResultPayment(receiptEnJson);

		//	ToDo:: display Van Message when complete transaction
		showVanDisplayMessage(AppHelper.AppPref.getVanMsg());

		//	ToDo:: goto ReceiptViewFragment
		if (VanStaticData.isReadyShowReceipt())
			cancelCashToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.ReceiptViewPage);

		resetToStartPayment();
	}

	///================================
	// *  private methods
	//=================================

	private void startCancelCash()
	{
		if(mmReceiptEntity == null)
		{
			showDialog(IVanString.userNotification.msg_search_receipt_failure);
			return;
		}

		{	//	ToDo:: start cancel Swipe procedure
			doOnLineProgress();
		}

	}

	private void doOnLineProgress()
	{
		//	ToDo :: start cancel transaction
		ApiLog.Dbg(">>==========	do CANCEL:: CASH	========<<");
		ApiLog.Dbg(Tag+"ReceiptType: " + mmReceiptEntity.getType() );
		doCancelCash();
	}

	private void doCancelCash()
	{
		//	ToDo :: update receipt to cancel Cash procedure
		String cardNo;
		String vanName = mmReceiptEntity.getVanName();
		ApiLog.Dbg(Tag+"VanName on Receipt:" + vanName);
		if(! vanName.equals( mmCompanyEntity.getVanName() ) )
		{
			showDialog(IVanString.userNotification.msg_cancel_van_error);
			return;
		}

		if(! mmReceiptEntity.getType().equals(IVanSpecification.PaymentType.Cash) )
		{
			showDialog(IVanString.userNotification.msg_cancel_receipt_type_error);
			return;
		}

		if( mmBankCardData.equals("") )
		{
			cardNo = mmEncryptedKeyInCardNo;
			if( cardNo.equals("") )
			{
				showDialog(IVanString.payment.please_input_card_value);
				return;
			}
			cardNo = ApiBase64.base64Encode( ApiString.hexStringToByteArray(cardNo) );
			ApiLog.Dbg(Tag+"Base64EncryptedKeyInData: " + cardNo);
		}
		else
		{
			cardNo = mmBankCardData;
			cardNo = new String( ApiString.hexStringToByteArray(cardNo) );
			ApiLog.Dbg(Tag+"Base64EncryptedCardNo: "+cardNo);

			String formattedCardNumber = ApiString.formattedCardNumber(mmTrack2Data);
			mEdCardNo.setText(formattedCardNumber);
		}
		mmReceiptEntity.setCardNo(cardNo);		// renew CardNo

		String reqDate = ApiDate.formatCancelDateDaouData( mmReceiptEntity.getRequestDate() );
		mmReceiptEntity.setRequestDate(reqDate);			// fit date format :: YYYYMMDD

		doSendVanServer(mmReceiptEntity);
	}

	private void doSendVanServer(final ReceiptEntity receiptEntity)
	{
		PaymentTask.CallBackMethod callBackMethod = new PaymentTask.CallBackMethod() {
			@Override
			public String run() {
				mPaymentTask.updateCDialog(R.drawable.progress_sending);


				String result = setReceiptCancel(receiptEntity);
				return result;
			}

			@Override
			public boolean res(String result) {
				ApiLog.Dbg(Tag+"setReceiptCancel Result: "+result);
				mPaymentTask.updateCDialog(R.drawable.progress_exiting);

				checkNetworkResult( CreditCard.getVanNetworkResult() );

				return true;
			}
		};
		mPaymentTask = new PaymentTask(mmActivity, callBackMethod, 0, R.drawable.progress_ing);
		mPaymentTask.execute();

	}

	private String setReceiptCancel(ReceiptEntity receiptEntity)
	{
		String result = null;
		String signData = "";


		mmPayment = new CashReceipt();
		//	make SignData to String

		if( (mmSignImageByte != null) && (mmSignImageByte.length > 0) )
		{
			try {
				signData = new String(mmSignImageByte);
				}catch (Exception ex){
				ex.printStackTrace();
				signData="";
			}
		}

		//	get EMV Data saved
		String emvData = EmvUtils.getEmvData();

		//	make EncPayInfo to will be Encrypted.
		EncPayInfo encPayInfo = new EncPayInfo("", emvData, signData);

		{
			result = mmPayment.cancel(receiptEntity, encPayInfo);
		}

		if( AppHelper.isEmvCardProcessing(receiptEntity) )
		{	//	ToDo:: terminating EMV Procedure
			if( CreditCard.getVanNetworkResult().equals(IVanSpecification.NetworkResult.NoEOT) )
			{
				ApiLog.Dbg(Tag +"NoEOT::receiptEntry: "+ mmReceiptEntity.toString() );
				return "";
			}

			ApiLog.Dbg(Tag+"sendOnlineProgressResult:" + result);
			if( result != null && !result.equals("") )
			{
				if( VanStaticData.mmCreditSuccessWithEmv )
					onlineProgressResp(OnlineProgressReturnType.NORMAL_RESP, CreditCard.getCardType(), result);
				else
					onlineProgressResp(OnlineProgressReturnType.SIMPLE_RESP,null,null);

			}
			else
				onlineProgressResp(OnlineProgressReturnType.DECLINE_RESP,null,null);
		}
		else
		{	//	ToDo:: terminate MSR Swipe procedure
			// check NetworkResult incomplete transaction or not
			if( CreditCard.getVanNetworkResult().equals(IVanSpecification.NetworkResult.NoEOT) )
			{
				ApiLog.Dbg(Tag +"NoEOT::receiptEntry: "+ mmReceiptEntity.toString() );
				return "";
			}

			ApiLog.Dbg(Tag+"sendOnlineProgressResult:" + result);
			if( result != null && !result.equals("") )
				onlineProgressResp(OnlineProgressReturnType.SIMPLE_RESP,null,null);
			else
				onlineProgressResp(OnlineProgressReturnType.DECLINE_RESP,null,null);
		}

		signData = "";
		mmSignImageByte = null;
		emvData = null;
		encPayInfo = null;

		return result;
	}

	private void showDialog(String msg)
	{
		showUserMessage(msg);
	}

	private DialogCancelList.DialogCancelListListener mCancelListListener = new DialogCancelList.DialogCancelListListener() {
		@Override
		public void dialogCancelListListenerCB(ReceiptEntity receiptEntity)
		{
			mmReceiptEntity=receiptEntity;
			if( (mmReceiptEntity == null) || mmReceiptEntity.getRevStatus().equals(IVanSpecification.ReceiptStatus.CancelReceipt) )
			{
				resetCardNo();
				MyToast.showToast(mmActivity, IVanString.userNotification.msg_search_receipt_failure);
				return;
			}

			ApiLog.Dbg(Tag+"searched Receipt: "+ mmReceiptEntity.toString() );

			mEdCardNo.setText( mmReceiptEntity.getCardNo() );
			mTvTotal.setText( ApiString.formatNumberExcel( mmReceiptEntity.getTotalAmount() ) );
			mTvReqDate.setText( mmReceiptEntity.getRequestDate() );
			mTvApprovalNo.setText( mmReceiptEntity.getApprovalCode() );
		}
	};

	private void findUsageHistory(String cardNo)
	{
		findReceiptsForCancel(cardNo, mCancelListListener);
	}

	//==========================================
	//	Initialize Fragment Components
	//==========================================
	private void initComponents()
	{

		super.setEmvReaderFragCB( new UserEmvReaderFragCb() );

	}

	private void setView(LayoutInflater inflater, ViewGroup container, View view) {


		//  ToDo:: set all event listeners like button on click listener if you have
		Button btn = (Button)mFragmentView.findViewById(R.id.btn_foot_confirm);
		btn.setOnClickListener(mButtonListener);

		btn = mFragmentView.findViewById(R.id.btn_foot_cancel);
		btn.setOnClickListener(mButtonListener);


		btn = mFragmentView.findViewById(R.id.btn_cancel_credit_reading_card);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				VanStaticData.setIsCancelJob(true);
				VanStaticData.setIsCashJob(true);
				mKeyInCardNo = getKeyInData(mEdCardNo);
				if( ! mKeyInCardNo.equals("") )
				{	// ToDo:: Key In
					String maskedKeyInCardNo = EmvUtils.formatMaskedTrack2(mKeyInCardNo);
					findUsageHistory(maskedKeyInCardNo);
					return;
				}
				else
				{	// ToDo:: Swipe Card
					startCheckCard(IEmvUserMessages.CheckCardMode.SWIPE);
				}
			}
		});

		mEdCardNo = mFragmentView.findViewById(R.id.ed_cancel_cash_card_no);
		mEdCardNo.setOnTouchListener(mTouchListener);
		mTvTotal = mFragmentView.findViewById(R.id.tv_cancel_cash_amount);
		mTvApprovalNo = mFragmentView.findViewById(R.id.tv_cancel_cash_approval_no);
		mTvReqDate = mFragmentView.findViewById(R.id.tv_cancel_cash_req_date);

		return;
	}

	private void updateView(View view)
	{
		// ToDo: update any view element you want
		LinearLayout topView = mFragmentView.findViewById(R.id.fragment_top_layout);
		ShowFragmentTopView.setFragmentTopView(mmActivity, topView, mmCompanyEntity);
	}

	private View.OnTouchListener mTouchListener = new View.OnTouchListener(){
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch( v.getId() )
			{
				case	R.id.ed_cancel_cash_card_no:
					if( event.getAction() == MotionEvent.ACTION_DOWN) {
						showLocalNumberKeyboard(CashTransactionMethod.CardNo, mEdCardNo);            // default method
					}
					break;
			}

			return true;
		}
	};

	private void showLocalNumberKeyboard(String mode, EditText editText) {
		LinearLayout kbView = mmActivity.findViewById(R.id.numeric_keyboard_layout);

		if (KeyboardHandler.isShow()) {
			ApiLog.Dbg(Tag + "keyboard already runs");
			return;
		}

		switch(mode)
		{
			case	CashTransactionMethod.PhoneNo:
				ApiEditTextPhoneNo.disableShowSoftInput(editText);
				ApiEditTextPhoneNo.showKeyboard(mmActivity, kbView, editText);

				ApiEditTextPhoneNo.setTextChangeListener(editText);
				break;
			case	CashTransactionMethod.CompanyNo:
				ApiEditTextCompanyNo.disableShowSoftInput(editText);
				ApiEditTextCompanyNo.showKeyboard(mmActivity, kbView, editText);

				ApiEditTextCompanyNo.setTextChangeListener(editText);
				break;
			case	CashTransactionMethod.CardNo:
				ApiEditTextCardNo.disableShowSoftInput(editText);
				ApiEditTextCardNo.showKeyboard(mmActivity,kbView, editText);

				ApiEditTextCardNo.setTextChangeListener(editText);
				break;
			default:
				break;
		}
	}

	private View.OnClickListener mButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case	R.id.btn_foot_cancel:
					cancelCashToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.MainHomePage);
					break;
				case	R.id.btn_foot_confirm:
					//	ToDo:: case of Start Cancel Job with Swipe
					if(! mmTrack2Data.equals("") )
					{
						startCancelCash();
						break;
					}
					//	ToDo:: case of Start Cancel Job with Key-In
					if( mKeyInCardNo.equals("") )
					{
						showDialog( IVanString.payment.please_input_card_value );
						break;
					}
					//	ToDo:: Encrypt key in card number.
					String hexKeyInCardNo = ApiString.toHexString( mKeyInCardNo.getBytes() );
					encryptKeyInCardNo(hexKeyInCardNo);

					mKeyInCardNo = "";
					//ApiLog.Dbg(Tag+"Start Cancel Cash by KeyIn");
					//startCancelCash();
					break;
				default:
					break;
			}
		}
	};
	//##########################################
	//  private variables
	//##########################################
	private String Tag=String.format("[%s]",FragmentCancelCash.class.getSimpleName() );
	private interface CashTransactionMethod{
		String PhoneNo		= "phoneNumber";
		String CompanyNo	= "companyNumber";
		String CardNo		= "cardNumber";
	}
	/*
	 *  To communicate with parent activity
	 *  #1. declare callback
	 */
	private FragmentCallbackInterface.CancelCashToActivity mCallback;
	private View							mFragmentView;

	private static int mSectionNumber = -1;

	private EditText						mEdCardNo;
	private TextView						mTvTotal, mTvApprovalNo, mTvReqDate;
	private PaymentTask					mPaymentTask;
	private String							mKeyInCardNo = "";
}
