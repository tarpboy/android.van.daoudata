package com.payfun.van.daou.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payfun.van.daou.R;

import java.util.Hashtable;

import ginu.android.library.signpad.SignatureHandler;
import ginu.android.library.utils.common.ApiAux;
import ginu.android.library.utils.common.ApiDate;
import ginu.android.library.utils.common.ApiLog;
import ginu.android.library.utils.common.ApiString;
import ginu.android.van.app_daou.BaseFragment.FragmentPaymentBase;
import ginu.android.van.app_daou.cardreader.EmvUtils;
import ginu.android.van.app_daou.cardreader.IEmvUserMessages;
import ginu.android.van.app_daou.daou.CashReceipt;
import ginu.android.van.app_daou.daou.CreditCard;
import ginu.android.van.app_daou.daou.DaouData;
import ginu.android.van.app_daou.daou.DaouDataContants;
import ginu.android.van.app_daou.daou.EmvTc;
import ginu.android.van.app_daou.database.IVanSpecification;
import ginu.android.van.app_daou.database.VanStaticData;
import ginu.android.van.app_daou.entity.CompanyEntity;
import ginu.android.van.app_daou.entity.EmvTcEntity;
import ginu.android.van.app_daou.entity.EncPayInfo;
import ginu.android.van.app_daou.entity.ReceiptEntity;
import ginu.android.van.app_daou.entity.TerminalInfo;
import ginu.android.van.app_daou.helper.AppHelper;
import ginu.android.van.app_daou.helper.VanHelper;
import ginu.android.van.app_daou.utils.DialogCancelList;
import ginu.android.van.app_daou.utils.IVanString;
import ginu.android.van.app_daou.utils.MyToast;
import ginu.android.van.app_daou.utils.MyTypeFace;
import ginu.android.van.app_daou.utils.PaymentTask;

import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangePage;
import static ginu.android.library.utils.gui.IFragmentConstant.ARG_SECTION_NUMBER;

/**
 * Created by david_shkim on 2018-03-13.
 */

public class FragmentCancelCredit extends FragmentPaymentBase implements FragmentCallbackInterface.ActivityToCancelCredit
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
			mCallback = (FragmentCallbackInterface.CancelCreditToActivity) mmActivity;
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
		mFragmentView = inflater.inflate(R.layout.fragment_cancel_credit, container, false);

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

	public void activityToCancelCreditCb(int cmd, Object obj) {
		switch(cmd)
		{
			//case    ActivityToHomeCmd_DeviceAdapter:
			//    mDeviceAdapter = (DeviceAdapter) obj;       //deviceAdapter;
			//   break;

			default:
				break;
        }
	}

	private void cancelCreditToActivity(int cmd, Object obj)
    {
        mCallback.cancelCreditToActivityCb(cmd, obj);
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
			mCheckCardMode = strCheckCardMode;			// backup for starEmv()
			startCheckCard(strCheckCardMode);
		}

		public void doStartEmv(String strCheckCardMode)
		{
			//	ToDo:: Start get Emv Card Data
			mCheckCardMode = strCheckCardMode;
			getEmvCardData();					// call FragmentPaymentBase.getEmvCardData()
		}

		public void endCardReader(Hashtable<String, Object> result)
		{
			//	ToDo:: do onlineProgress
			boolean finalResult = (boolean) result.get(IEmvUserMessages.UserKeys.finalResult);

			if(finalResult) {
				if(mmReceiptEntity == null)
				{	// ToDo:: 1st Fallback to find receipts
					String cardNo = mmTrack2Data;		// Swipe Card No
					findUsageHistory(cardNo);
					return;
				}
				//	ToDo:: 2nd Fallback to cancel transaction, when ICC ==>ICC_SWIPE
				doOnLineProgress();		// really happen? who makes this? Don't I remove this??
			}
			else {
				showDialog("카드리드오류");
			}
		}

		public void doUserTransactionResult(String result)
		{
			if( "DECLINED".equalsIgnoreCase(result) )
			{
				resetToPayAgain(true);
				showVanDisplayMessage( AppHelper.AppPref.getVanMsg() );
			}
			else
			if( "APPROVED".equalsIgnoreCase(result)) {
				doTransactionComplete();
			}else
			if( "ICC_CARD_REMOVED".equals(result) &&
					AppHelper.AppPref.getVanNetworkStatus().equals(IVanSpecification.NetworkResult.Success) )
			{
				doTransactionComplete();
			}
		}

		public void doEmvCardDataResult(boolean isSuccess)
		{
			//	ToDo:: to cancel procedure if success

			if(isSuccess)
			{
				String cardNo = AppHelper.isEmvCardProcessing() ? mmBankCardData: mmTrack2Data;	// added by David SH Kim. to select ICC or Swipe data

				findUsageHistory(cardNo);			// (mmBankCardData); changed by David SH Kim.
			}
			else
			{
				MyToast.showToast(mmActivity, IVanString.userNotification.msg_search_credit_receipt_failure);
			}
		}

		public void doEncryptedKeyInCardNoResult(boolean isSuccess, String message)
		{
			// ToDo:: Nothing in here
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
			cancelCreditToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.ReceiptViewPage);

		resetToTerminatePayment();
	}
	///================================
	// *  private methods
	//=================================

	/**
	 * @Notice:
	 * 다우서버에서 허용하는 취소 시나리오
	 * 	승인		취소		결과
	 * 	ICC		ICC		OK
	 * 	ICC		SWIPE	OK
	 * 	SWIPE	ICC		NOK
	 * 	SWIPE	SWIPE	OK
	 */
	private boolean isRightInputMethod()
	{
		boolean isRight = true;
		String paymentInputMethod = mmReceiptEntity.getCardInputMethod();
		String cancelInputMethod = VanStaticData.mmCardInputMethod;

		if(	paymentInputMethod.equals( DaouDataContants.VAL_WCC_SWIPE ) &&
			cancelInputMethod.equals( DaouDataContants.VAL_WCC_IC ) ) {
			isRight = false;
		}

		return isRight;
	}

	private void startCancelCredit()
	{
		if(mmReceiptEntity == null)
		{
			showDialog(IVanString.userNotification.msg_search_credit_receipt_failure);
			return;
		}

		// ToDo:: check input method is right or not
		if( ! isRightInputMethod() ) {
			showDialog(IVanString.userNotification.msg_cancel_receipt_subtype_error);
			return;
		}

		if( ! checkSign() )
			return;

		ApiLog.Dbg(">>==========	do CANCEL:: CREDIT	========<<");
		ApiLog.Dbg(Tag+"ReceiptType: " + mmReceiptEntity.getType() );

		if(	AppHelper.isEmvCardProcessing()	)
		{	//	ToDo:: start cancel Emv procedure
			startEmv( mCheckCardMode, mTvTotal.getText().toString() );
		}
		else
		{	//	ToDo:: start cancel Swipe procedure
			doCancelCredit();
		}

	}

	private void doOnLineProgress()
	{
		//	ToDo :: start cancel transaction
		doCancelCredit();
	}

	private void doCancelCredit()
	{
		//	ToDo :: update receipt to cancel Credit procedure
		String vanName = mmReceiptEntity.getVanName();
		ApiLog.Dbg(Tag+"VanName on Receipt:" + vanName);
		if(! vanName.equals( mmCompanyEntity.getVanName() ) )
		{
			showDialog(IVanString.userNotification.msg_cancel_van_error);
			return;
		}

		if(! mmReceiptEntity.getType().equals(IVanSpecification.PaymentType.Credit) )
		{
			showDialog(IVanString.userNotification.msg_cancel_receipt_type_error);
			return;
		}

		mmReceiptEntity.setCardNo(mmBankCardData);		// renew CardNo

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

		mmPaymentEmv = new CreditCard();
		mmPayment = new CreditCard();
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

		//if( receiptEntity.getCardInputMethod().equals(DaouDataContants.VAL_WCC_IC) )	// 이전거래(ICC/ICC_SWIPE)와 동일한 방식으로 해야만된다.????
		if( VanStaticData.mmCardInputMethod.equals(DaouDataContants.VAL_WCC_IC))
		{
			result = mmPaymentEmv.cancelEmv(receiptEntity, encPayInfo);
		}
		else
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

			ApiLog.Dbg(Tag+"sendOnlineProgressResult[ICC]:" + result);
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

			ApiLog.Dbg(Tag+"sendOnlineProgressResult[SWIPE]:" + result);
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
				MyToast.showToast(mmActivity, IVanString.userNotification.msg_search_credit_receipt_failure);
				return;
			}

			ApiLog.Dbg(Tag+"searched Receipt: "+ mmReceiptEntity.toString() );

			mTvTotal.setText( ApiString.formatNumberExcel( mmReceiptEntity.getTotalAmount() ) );
			mTvReqDate.setText( mmReceiptEntity.getRequestDate() );
			mTvApprovalNo.setText( mmReceiptEntity.getApprovalCode() );

			//	ToDo:: show signPad
			if( VanStaticData.mmSignatureAmountLimit <= Integer.parseInt( mmReceiptEntity.getTotalAmount() ) ) {
				mmSignatureHandler.showSignature(true);// showSignPad();
				AppHelper.AppPref.setNeedSignature(true);			// mmNeedSignature = true;
			}
			else {
				mmSignatureHandler.showSignature(false);
				AppHelper.AppPref.setNeedSignature(false);			// mmNeedSignature = false;
			}

		}
	};

	private void findUsageHistory(String cardNo)
	{
		findReceiptsForCancel(cardNo, IVanSpecification.PaymentType.Credit, mCancelListListener);
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
				startCheckCard(IEmvUserMessages.CheckCardMode.SWIPE_OR_INSERT );
			}
		});
		return;
	}

	private void updateView(View view)
	{
		// ToDo: update any view element you want

		mTvTotal = mFragmentView.findViewById(R.id.tv_cancel_credit_amount);
		mTvApprovalNo = mFragmentView.findViewById(R.id.tv_cancel_credit_approval_no);
		mTvReqDate = mFragmentView.findViewById(R.id.tv_cancel_credit_req_date);

		//	ToDo:: init SignPad view
		mmSignatureHandler = new SignatureHandler();
		LinearLayout viewSign = mFragmentView.findViewById(R.id.viewSignPad);
		if( ! mmSignatureHandler.initSignatureView(mmActivity, viewSign) ) {
			ApiLog.Dbg(Tag+"Fail to initialize SignView");
			MyToast.showToast(mmActivity, "Fail to initialize SignView");
		}

		LinearLayout topView = mFragmentView.findViewById(R.id.fragment_top_layout);
		ShowFragmentTopView.setFragmentTopView(mmActivity, topView, mmCompanyEntity);
	}

	private View.OnClickListener mButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case	R.id.btn_foot_cancel:
					cancelCreditToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.MainHomePage);
					break;
				case	R.id.btn_foot_confirm:
					startCancelCredit();
					break;
				default:
					break;
			}
		}
	};
	//##########################################
	//  private variables
	//##########################################
	private String Tag=String.format("[%s]",FragmentCancelCredit.class.getSimpleName() );
	/*
	 *  To communicate with parent activity
	 *  #1. declare callback
	 */
	private FragmentCallbackInterface.CancelCreditToActivity mCallback;
	private View							mFragmentView;

	private static int mSectionNumber = -1;

	private TextView						mTvTotal, mTvApprovalNo, mTvReqDate;
	private String							mCheckCardMode = "";
	private PaymentTask					mPaymentTask;
}
