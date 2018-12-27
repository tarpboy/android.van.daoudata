package com.payfun.van.daou.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payfun.van.daou.R;

import java.util.Hashtable;

import ginu.android.library.keyboard.ApiEditTextAmount;
import ginu.android.library.keyboard.ApiEditTextCardNo;
import ginu.android.library.keyboard.ApiEditTextCompanyNo;
import ginu.android.library.keyboard.ApiEditTextPhoneNo;
import ginu.android.library.keyboard.KeyboardHandler;
import ginu.android.library.utils.common.ApiAux;
import ginu.android.library.utils.common.ApiDate;
import ginu.android.library.utils.common.ApiLog;
import ginu.android.library.utils.common.ApiString;
import ginu.android.library.utils.security.ApiBase64;
import ginu.android.van.app_daou.BaseFragment.FragmentPaymentBase;
import ginu.android.van.app_daou.ExternalCall.ExtCallReqData;
import ginu.android.van.app_daou.cardreader.EmvUtils;
import ginu.android.van.app_daou.cardreader.IEmvUserMessages;
import ginu.android.van.app_daou.daou.CashReceipt;
import ginu.android.van.app_daou.daou.DaouDataContants;
import ginu.android.van.app_daou.database.IVanSpecification;
import ginu.android.van.app_daou.database.VanStaticData;
import ginu.android.van.app_daou.entity.EncPayInfo;
import ginu.android.van.app_daou.entity.ReceiptEntity;
import ginu.android.van.app_daou.helper.AppHelper;
import ginu.android.van.app_daou.helper.CalculateHelper;
import ginu.android.van.app_daou.helper.VanHelper;
import ginu.android.van.app_daou.utils.IVanString;
import ginu.android.van.app_daou.utils.MyToast;
import ginu.android.van.app_daou.utils.PaymentTask;

import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangeHeaderTitle;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangePage;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowCompanyNumericKeyboard;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowNumericKeyboard;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowPhoneNumericKeyboard;
import static ginu.android.library.utils.common.ApiDate.getDatePreferenceBasicFormat;
import static ginu.android.library.utils.gui.IFragmentConstant.ARG_SECTION_NUMBER;

/**
 * Created by david_shkim on 2018-03-13.
 */

public class FragmentPaymentCash extends FragmentPaymentBase implements FragmentCallbackInterface.ActivityToPaymentCash
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
			mCallback = (FragmentCallbackInterface.PaymentCashToActivity) mmActivity;
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
		mFragmentView = inflater.inflate(R.layout.fragment_payment_cash, container, false);

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

	public void activityToPaymentCashCb(int cmd, Object obj) {
		switch(cmd)
		{
			//case    ActivityToHomeCmd_DeviceAdapter:
			//    mDeviceAdapter = (DeviceAdapter) obj;       //deviceAdapter;
			//   break;

			default:
				break;
        }
	}

	private void paymentCashToActivity(int cmd, Object obj)
    {
        mCallback.paymentCashToActivityCb(cmd, obj);
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
			boolean ret = true;

			return ret;
		}

		/**
		 * do check card as a request of device
		 */
		public void doCheckCard(String strCheckCardMode)
		{

		}

		public void doStartEmv(String strCheckCardMode)
		{

		}

		public void endCardReader(Hashtable<String, Object> result)
		{
			boolean finalResult = (boolean) result.get(IEmvUserMessages.UserKeys.finalResult);

			if(finalResult)
				doOnLineProgress();
			else
				showDialog("카드리드오류");
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

	private void doTransactionComplete()
	{
		//	ToDo:: save receipt entity in json.
		ReceiptEntity receiptEntity = AppHelper.getReceiptEntity();
		String receiptEnJson = VanHelper.payment(receiptEntity);        // make receiptEntity to Json data
		if (receiptEnJson != null)
			VanStaticData.setResultPayment(receiptEnJson);

		//	ToDo:: display Van Message when complete transaction
		if( mIsVanRequest )											// Van Accessing Procedure only
			showVanDisplayMessage(AppHelper.AppPref.getVanMsg());

		//	ToDo:: goto ReceiptViewFragment
		if (VanStaticData.isReadyShowReceipt())
			paymentCashToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.ReceiptViewPage);

		resetToTerminatePayment();
	}

	//##########################################
	// *  private methods
	//##########################################
	private void doOnLineProgress(){
		/*
		 *	prevent a consequence onLineProgress Request
		 */
		if(	! AppHelper.checkLastPayment() ) {
			super.resetToPayAgain();
			return;
		}

		//	save last transaction date:: yyyy-MM-dd a hh:mm:ss
		String currentDate = getDatePreferenceBasicFormat();
		AppHelper.AppPref.setLastPaymentInfo(currentDate);

		doPaymentCash();
	}

	private void doPaymentCash()
	{
		String amount = mEditTextAmount.getText().toString().replace(",", "");
		String cardNo;

		if(amount.equals("") || amount.equals("0")) {
			showDialog(IVanString.payment.please_input_total_amount);
			return;
		}

		if( mmBankCardData.equals(""))
		{	//	ToDo:: KeyIn Card Data
			cardNo = mmEncryptedKeyInCardNo;
			if( cardNo.equals("") )
			{
				showDialog(IVanString.payment.please_input_card_value);
				return;
			}
			cardNo = ApiBase64.base64Encode( ApiString.hexStringToByteArray(cardNo) );
			ApiLog.Dbg(Tag+"Base64EncryptedKeyInData: "+cardNo);
		}
		else
		{	//	ToDo:: SWIPE Card Data
			cardNo = mmBankCardData;
			cardNo = new String( ApiString.hexStringToByteArray(cardNo) );
			ApiLog.Dbg(Tag+"Base64EncryptedCardNo: "+cardNo);

			String formattedCardNumber = ApiString.formattedCardNumber(mmTrack2Data);
			mEditTextSelectedTarget.setText(formattedCardNumber);
		}

		mVanName = mmCompanyEntity.getVanName();

		//	account for point
		String point = "0";
		if (! mmPointRate.equals("0") ) {
			point = CalculateHelper.getPoint(mmPointRate, amount);
			amount = CalculateHelper.getTAmount(point, amount);
		}

		//	account for tax
		if (mmCompanyEntity.getWithTax())
			mmReceiptEntity = CalculateHelper.calWithTax( amount, point, mmCompanyEntity.getTaxRate(), mmCompanyEntity.getServiceTaxRate() );
		else
			mmReceiptEntity = CalculateHelper.calNoTax( amount, point, mmCompanyEntity.getTaxRate(), mmCompanyEntity.getServiceTaxRate() );

		mmReceiptEntity.setCouponDiscountRate(mmPointRate);
		mmReceiptEntity.setCouponDiscountAmount(point);
		mmReceiptEntity.setCouponID(mmCouponID);

		mmReceiptEntity.setVanName(mVanName);
		mmReceiptEntity.setCompanyNo( mmCompanyEntity.getCompanyNo() );
		mmReceiptEntity.setMachineCode( mmCompanyEntity.getMachineCode() );
		mmReceiptEntity.setTypeSub( mCashTypeSub );
		mmReceiptEntity.setType( IVanSpecification.PaymentType.Cash );
		mmReceiptEntity.setReciptImage("");
		mmReceiptEntity.setCardInputMethod(VanStaticData.mmCardInputMethod);
		mmReceiptEntity.setStaffName(AppHelper.AppPref.getCurrentUserName());
		mmReceiptEntity.setUserID(AppHelper.AppPref.getCurrentUserID());
		mmReceiptEntity.setRequestDate(ApiDate.getYYYYMMDD());			//DateHelper.getCurrentDateFull()//
		if( IVanSpecification.CashSubType.EVIDENCE_EXPENDITURE.equals(mCashTypeSub) )
			mmReceiptEntity.setMonth( "01" );
		else
			mmReceiptEntity.setMonth( "00" );

		mmReceiptEntity.setApprovalCode("");

		mmReceiptEntity.setCardNo(cardNo);

		//	ToDo:: for Not Access Van Server, fake some information for receipt processing.
		if( ! mIsVanRequest )
		{
			String maskedKeyInData = EmvUtils.formatMaskedTrack2( getKeyInData(mEditTextSelectedTarget) );
			mmReceiptEntity.setCardNo( maskedKeyInData );
			mmReceiptEntity.setBuyerName( mCashTypeSub );
			mmReceiptEntity.setApprovalCode( ApiAux.getUnique10Num() );
			mmReceiptEntity.setRequestDate( ApiDate.getCurrentDateFull() );
			mmReceiptEntity.setRevDate( ApiDate.getCurrentDateFull() );
			mmReceiptEntity.setRevStatus( IVanSpecification.ReceiptStatus.PaymentReceipt );
/*
			String result = VanHelper.payment(mmReceiptEntity, true);
			VanStaticData.setResultPayment(result);							// backup for Receipt Fragment

			paymentCashToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.ReceiptViewPage);	// End
*/
			AppHelper.setReceiptEntity(mmReceiptEntity);
			doTransactionComplete();
			return;
		}

		resetCardNo();
		ApiLog.Dbg("receipt to pay:" + mmReceiptEntity.toString());

		doSendVanServer(mmReceiptEntity);
	}

	private void doSendVanServer(final ReceiptEntity receiptEntity)
	{
		VanStaticData.mmPaymentSuccess = IVanSpecification.SuccessMsg.ForPayment.PaymentSuccess_Cash;

		PaymentTask.CallBackMethod callBackMethod = new PaymentTask.CallBackMethod() {
			@Override
			public String run() {
				mPaymentTask.updateCDialog(R.drawable.progress_sending);

				String result = setReceiptPayment(receiptEntity);
				return result;
			}

			@Override
			public boolean res(String result) {
				ApiLog.Dbg(Tag+"setReceiptPayment Result: "+result);
				mPaymentTask.updateCDialog(R.drawable.progress_exiting);

				checkNetworkResult( CashReceipt.getVanNetworkResult() );
				return true;
			}
		};
		mPaymentTask = new PaymentTask(mmActivity, callBackMethod, 0, R.drawable.progress_ing);
		mPaymentTask.execute();
	}

	private String setReceiptPayment(ReceiptEntity receiptEntity)
	{
		String result = null;
		ApiLog.Dbg(Tag+"VanName:" + mVanName);

		mmPayment = new CashReceipt();

		result = mmPayment.pay(receiptEntity, new EncPayInfo());

		// check NetworkResult incomplete transaction or not
		if( CashReceipt.getVanNetworkResult().equals(IVanSpecification.NetworkResult.NoEOT) )
		{
			ApiLog.Dbg(Tag +"NoEOT::receiptEntry: "+ mmReceiptEntity.toString() );
			return "";
		}

		//	ToDo:: terminate Swipe procedure
		ApiLog.Dbg(Tag+"sendOnlineProgressResult:" + result);
		if( result != null && !result.equals("") )
			onlineProgressResp(OnlineProgressReturnType.SIMPLE_RESP,null,null);
		else
			onlineProgressResp(OnlineProgressReturnType.DECLINE_RESP,null,null);

		//	clear sensitive data

		return result;
	}

	//==========================================
	//	Initialize Fragment View.
	//==========================================
	private void initComponents()
	{
		super.setEmvReaderFragCB( new FragmentPaymentCash.UserEmvReaderFragCb() );
	}

	private void setView(LayoutInflater inflater, ViewGroup container, View view)
	{
		//  ToDo:: set all event listeners like button on click listener if you have
		Button btn = (Button)mFragmentView.findViewById(R.id.btn_foot_confirm);
		btn.setOnClickListener(mButtonListener);

		btn = mFragmentView.findViewById(R.id.btn_foot_cancel);
		btn.setOnClickListener(mButtonListener);

		//	Button :: Reading Card
		mBtnCardRead = mFragmentView.findViewById(R.id.fragment_cash_btn_card_read);
		setCardReaderOnClick(false);

		/*
		 *	link the numeric keyboard to Amount EditText Field
		 */
		mEditTextPhone = mFragmentView.findViewById(R.id.fragment_cash_input_phone);

		mEditTextCompanyCode = mFragmentView.findViewById(R.id.fragment_cash_input_company_code);

		mEditTextCardNo = mFragmentView.findViewById(R.id.fragment_cash_input_card_number);

		final LinearLayout amountLayout = mFragmentView.findViewById(R.id.fragment_cash_input_money_layout);
		mEditTextAmount = amountLayout.findViewById(R.id.edInputAmount);
		mEditTextAmount.setOnTouchListener( mTouchListener	);

		//	ImageButton :: Cash Categories
		mImgBtnSelect1 = mFragmentView.findViewById(R.id.fragment_cash_image_btn_select1);
		mImgBtnSelect1.setOnClickListener(mCashCategorySelectListener);
		mImgBtnSelect2 = mFragmentView.findViewById(R.id.fragment_cash_image_btn_select2);
		mImgBtnSelect2.setOnClickListener(mCashCategorySelectListener);
		mImgBtnSelect3 = mFragmentView.findViewById(R.id.fragment_cash_image_btn_select3);
		mImgBtnSelect3.setOnClickListener(mCashCategorySelectListener);
		mImgBtnSelect4 = mFragmentView.findViewById(R.id.fragment_cash_image_btn_select4);
		mImgBtnSelect4.setOnClickListener(mCashCategorySelectListener);
		mImgBtnSelect5 = mFragmentView.findViewById(R.id.fragment_cash_image_btn_select5);
		mImgBtnSelect5.setOnClickListener(mCashCategorySelectListener);
	}

	private void updateView(View view)
	{
		// ToDo: update any view element you want
		VanStaticData.mmCardInputMethod = DaouDataContants.VAL_WCC_KEYIN;			// default:: KeyIn. updated by EmvReadFSM for Swiping Card

		//	ToDo:: default Cash Category: 현금매출
		mCashTypeSub = IVanSpecification.CashSubType.CASH_SALES;
		selectCategory( R.id.fragment_cash_image_btn_select1 );
		selectCashTransactionMethodView(CashTransactionMethod.PhoneNo);

		LinearLayout topView = mFragmentView.findViewById(R.id.fragment_top_layout);
		ShowFragmentTopView.setFragmentTopView(mmActivity, topView, mmCompanyEntity);

		/*
		 *	ToDo: for external caller
		 *	added by David SH Kim. 2018/12/18
		 */
		if( VanStaticData.getIsExternalCall() ) {
			String jsonExtCallerReqData = AppHelper.AppPref.getCallerReq();
			ExtCallReqData reqData = ExtCallReqData.fromJsonString(jsonExtCallerReqData);
			mEditTextAmount.setText( reqData.getTotalAmount() );
		}
	}

	private View.OnTouchListener mTouchListener = new View.OnTouchListener(){
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch( v.getId() )
			{
				case	R.id.edInputAmount:
					if( event.getAction() == MotionEvent.ACTION_DOWN)
						paymentCashToActivity(CommonFragToActivityCmd_ShowNumericKeyboard, mEditTextAmount);
					break;
				case R.id.fragment_cash_input_phone:
					if( event.getAction() == MotionEvent.ACTION_DOWN)
						showLocalNumberKeyboard(CashTransactionMethod.PhoneNo, mEditTextPhone);
						//paymentCashToActivity(CommonFragToActivityCmd_ShowPhoneNumericKeyboard, mEditTextPhone);
					break;
				case R.id.fragment_cash_input_company_code:
					if( event.getAction() == MotionEvent.ACTION_DOWN)
						showLocalNumberKeyboard(CashTransactionMethod.CompanyNo,mEditTextCompanyCode);
						//paymentCashToActivity(CommonFragToActivityCmd_ShowCompanyNumericKeyboard, mEditTextCompanyCode);
					break;
				case	R.id.fragment_cash_input_card_number:
					if( event.getAction() == MotionEvent.ACTION_DOWN)
						showLocalNumberKeyboard(CashTransactionMethod.CardNo,mEditTextCardNo);
					break;
			}

			return true;
		}
	};

	private View.OnClickListener mButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case	R.id.btn_foot_cancel:
					paymentCashToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.MainHomePage);
					break;
				case	R.id.btn_foot_confirm:
					String keyInCardNo = getKeyInData(mEditTextSelectedTarget);
					if( keyInCardNo.equals("") )
					{
						showDialog(IVanString.payment.please_input_card_value);
						return;
					}
					//	ToDo:: Encrypt key in card number.
					keyInCardNo = ApiString.toHexString( keyInCardNo.getBytes() );
					encryptKeyInCardNo(keyInCardNo);
					ApiLog.Dbg(Tag+"Start Cash Payment by KeyIn");
					// doPaymentCash();
					break;
				case R.id.fragment_cash_btn_card_read:
					//	ToDo:: start reading card by swipe
					VanStaticData.setIsCashJob(true);
					startCheckCard(IEmvUserMessages.CheckCardMode.SWIPE);
					selectCashTransactionMethodView(CashTransactionMethod.CardNo);		// view EditTextCardNo
					break;
				default:
					break;
			}
		}
	};

	private View.OnClickListener mCashCategorySelectListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int cashCategoryId = v.getId();
			ApiLog.Dbg(Tag+"imageButton OnClickListener: "+ cashCategoryId);

			VanStaticData.mmCardInputMethod = DaouDataContants.VAL_WCC_KEYIN;			// default:: KeyIn.

			//	ToDo:: set default to the selected EditText
			mEditTextSelectedTarget.setText("");					// clear selected EditText.		2018/10/25
			if(! mEditTextSelectedTarget.isEnabled() )
				mEditTextSelectedTarget.setEnabled(true);

			switch ( cashCategoryId )
			{
				case	R.id.fragment_cash_image_btn_select1:		// ToDo:: 현금매출
					paymentCashToActivity(CommonFragToActivityCmd_ChangeHeaderTitle, "현금 매출");
					selectCashTransactionMethodView(CashTransactionMethod.PhoneNo);
					selectCategory( cashCategoryId );
					mIsVanRequest = false;							// Not allow accessing Van
					setCardReaderOnClick(false);						// Not allow Reading Card
					mCashTypeSub = IVanSpecification.CashSubType.CASH_SALES;			// 현금매출
					break;
				case R.id.fragment_cash_image_btn_select2:			// ToDo:: 일반영수증
					paymentCashToActivity(CommonFragToActivityCmd_ChangeHeaderTitle, "일반 영수증");
					selectCashTransactionMethodView(CashTransactionMethod.PhoneNo);
					selectCategory( cashCategoryId );
					mIsVanRequest = false;							// Not allow accessing Van
					setCardReaderOnClick(false);						// Not allow Reading Card
					mCashTypeSub = IVanSpecification.CashSubType.RECEIPT_PRINTING;		// 일반영수증
					break;
				case R.id.fragment_cash_image_btn_select3:			// ToDo:: 소득공제영수증
					paymentCashToActivity(CommonFragToActivityCmd_ChangeHeaderTitle, "소득공제영수증");
					selectCashTransactionMethodView(CashTransactionMethod.PhoneNo);
					selectCategory( cashCategoryId );
					mIsVanRequest = true;
					setCardReaderOnClick(true);
					mCashTypeSub = IVanSpecification.CashSubType.INCOME_DEDUCTION;		// 소득공제
					break;
				case R.id.fragment_cash_image_btn_select4:			// ToDo:: 사업자지출증빙영수증
					paymentCashToActivity(CommonFragToActivityCmd_ChangeHeaderTitle, "사업자지출증빙영수증");
					selectCashTransactionMethodView(CashTransactionMethod.PhoneNo);
					selectCategory( cashCategoryId );
					mIsVanRequest = true;
					setCardReaderOnClick(true);
					mCashTypeSub = IVanSpecification.CashSubType.EVIDENCE_EXPENDITURE;	// 지출증빙
					break;
				case R.id.fragment_cash_image_btn_select5:			// ToDo:: 자진발급
					paymentCashToActivity(CommonFragToActivityCmd_ChangeHeaderTitle, "자진발급 영수증");
					selectCashTransactionMethodView(CashTransactionMethod.PhoneNo);
					selectCategory( cashCategoryId );
					mIsVanRequest = true;
					setCardReaderOnClick(false);						// Not allow Reading Card
					mCashTypeSub = IVanSpecification.CashSubType.VOLUNTARY_ISSUANCE;	// 자진발급
					mEditTextSelectedTarget.setText("010-000-1234");
					mEditTextSelectedTarget.setEnabled(false);
					break;
				default:
					break;
			}
		}
	};

	private void setCardReaderOnClick(boolean enable)
	{
		if(enable) {
			mBtnCardRead.setOnClickListener(mButtonListener);
			mBtnCardRead.setBackgroundResource(R.drawable.button_confirm_selector);
		} else {
			mBtnCardRead.setOnClickListener(null);
			mBtnCardRead.setBackgroundResource(R.drawable.button_cancel_selector);
		}
	}

	private void selectCategory(int cashCategoryId)
	{
		mImgBtnSelect1.setSelected(false);
		mImgBtnSelect2.setSelected(false);
		mImgBtnSelect3.setSelected(false);
		mImgBtnSelect4.setSelected(false);
		mImgBtnSelect5.setSelected(false);
		switch( cashCategoryId )
		{
			case	R.id.fragment_cash_image_btn_select1:	mImgBtnSelect1.setSelected(true);	break;
			case	R.id.fragment_cash_image_btn_select2:	mImgBtnSelect2.setSelected(true);	break;
			case	R.id.fragment_cash_image_btn_select3:	mImgBtnSelect3.setSelected(true);	break;
			case	R.id.fragment_cash_image_btn_select4:	mImgBtnSelect4.setSelected(true);	break;
			case	R.id.fragment_cash_image_btn_select5:	mImgBtnSelect5.setSelected(true);	break;
			default: break;
		}
	}

	private void selectCashTransactionMethodView(String method)
	{
		switch(method)
		{
			case	CashTransactionMethod.PhoneNo:
				mEditTextPhone.setOnTouchListener( mTouchListener);				// PhoneNumber		:: ON
				mEditTextPhone.setVisibility(View.VISIBLE);
				mEditTextCompanyCode.setOnTouchListener( null );				// CompanyNumber	:: OFF
				mEditTextCompanyCode.setVisibility(View.GONE);
				mEditTextCardNo.setOnTouchListener(null);						// CardNumber		:: OFF
				mEditTextCardNo.setVisibility(View.GONE);

				mEditTextSelectedTarget = mEditTextPhone;						// selected Target input method
				break;
			case	CashTransactionMethod.CompanyNo:
				mEditTextPhone.setOnTouchListener( null);						// PhoneNumber		:: OFF
				mEditTextPhone.setVisibility(View.GONE);
				mEditTextCompanyCode.setOnTouchListener( mTouchListener );		// CompanyNumber	:: ON
				mEditTextCompanyCode.setVisibility(View.VISIBLE);
				mEditTextCardNo.setOnTouchListener(null);						// CardNumber		:: OFF
				mEditTextCardNo.setVisibility(View.GONE);

				mEditTextSelectedTarget = mEditTextCompanyCode;				// selected Target input method
				break;
			case	CashTransactionMethod.CardNo:
				mEditTextPhone.setOnTouchListener( null);						// PhoneNumber		:: OFF
				mEditTextPhone.setVisibility(View.GONE);
				mEditTextCompanyCode.setOnTouchListener( null );				// CompanyNumber	:: OFF
				mEditTextCompanyCode.setVisibility(View.GONE);
				mEditTextCardNo.setOnTouchListener(mTouchListener);				// CardNumber		:: ON
				mEditTextCardNo.setVisibility(View.VISIBLE);

				mEditTextSelectedTarget = mEditTextCardNo;						// selected Target input method
				break;
			default:
				ApiLog.Dbg(Tag+"Unknown Cash Transaction Input Method: "+ method);
				break;
		}

	}

	private void showLocalNumberKeyboard(String mode, EditText editText)
	{
		LinearLayout kbView = mmActivity.findViewById(R.id.numeric_keyboard_layout);

		if( KeyboardHandler.isShow() ) {
			ApiLog.Dbg(Tag + "keyboard already runs");
			return;
		}
		switch(mode)
		{
			case	CashTransactionMethod.PhoneNo:
				//ApiEditTextCompanyNo.dismissKeyboard();			// ToDo:: toggle keyboard. kill company keyboard
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

	private void showDialog(String msg)
	{
		showUserMessage(msg);
	}

	//##########################################
	//  private variables
	//##########################################
	private final String Tag = String.format("[%s]", FragmentPaymentCash.class.getSimpleName() );

	private interface CashTransactionMethod{
		String PhoneNo		= "phoneNumber";
		String CompanyNo	= "companyNumber";
		String CardNo		= "cardNumber";
	}

	/*
	 *  To communicate with parent activity
	 *  #1. declare callback
	 */
	private FragmentCallbackInterface.PaymentCashToActivity mCallback;

	private View					mFragmentView;

	private static int 			mSectionNumber = -1;

	private Button					mBtnCardRead;
	private EditText				mEditTextAmount;
	private EditText				mEditTextPhone, mEditTextCompanyCode, mEditTextCardNo, mEditTextSelectedTarget;
	private ImageButton			mImgBtnSelect1, mImgBtnSelect2, mImgBtnSelect3, mImgBtnSelect4, mImgBtnSelect5;

	private boolean				mIsVanRequest = false;

	private String					mVanName;
	private String					mCashTypeSub;
	private PaymentTask			mPaymentTask;
}
