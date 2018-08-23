package com.payfun.van.daou.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.payfun.van.daou.R;
import com.payfun.van.daou.fragments.FragmentCallbackInterface.ActivityToPaymentCredit;
import com.payfun.van.daou.fragments.FragmentCallbackInterface.PaymentCreditToActivity;

import java.util.Hashtable;

import ginu.android.library.emv.bbdevice.EmvReader;
import ginu.android.library.utils.common.ApiDate;
import ginu.android.library.utils.common.ApiLog;
import ginu.android.library.utils.common.ApiString;
import ginu.android.van.app_daou.BaseFragment.FragmentPaymentBase;
import ginu.android.van.app_daou.cardreader.EmvUtils;
import ginu.android.van.app_daou.cardreader.IEmvUserMessages;
import ginu.android.van.app_daou.daou.CreditCard;
import ginu.android.van.app_daou.daou.DaouDataContants;
import ginu.android.van.app_daou.daou.PaymentBase;
import ginu.android.van.app_daou.database.IVanSpecification;
import ginu.android.van.app_daou.database.VanStaticData;
import ginu.android.van.app_daou.entity.EncPayInfo;
import ginu.android.van.app_daou.entity.ReceiptEntity;
import ginu.android.van.app_daou.helper.AppHelper;
import ginu.android.van.app_daou.helper.CalculateHelper;
import ginu.android.van.app_daou.utils.PaymentTask;

import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangePage;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowNumericKeyboard;
import static ginu.android.library.utils.common.ApiDate.getDatePreferenceBasicFormat;

/**
 * Created by david_shkim on 2018-03-13.
 */

public class FragmentPaymentCredit extends FragmentPaymentBase implements
		ActivityToPaymentCredit
{

    public FragmentPaymentCredit() {}

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
            mCallback = (PaymentCreditToActivity) mmActivity;
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
        //if(savedInstanceState != null)
        //    mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);

       //  allocate the dummy fragment onto container
        mFragmentView = inflater.inflate(R.layout.fragment_payment_creadit, container, false);

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
/*    //  ToDo:: display your fragment on container.
    @Override
    public void onStart()
    {
        super.onStart();
    }
*/    // ToDo: End of onStart

    /**
     * Fragment life: step 6
     * @breif   called on display a view by system completely..
     *          no user Action.
     */
/*    //  ToDo:: display your fragment on container.
    @Override
    public void onResume()
    {
        super.onResume();

    }
*/   //  ToDo:: End of onResume

    /**
     * Fragment life: step 7
     * @breif   called on removing/replaced the fragment..
     *          no user Action.
     */
    //  ToDo:: remove your fragment on container.
/*	@Override
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
/*    //  ToDo:: stop your fragment on container.
    @Override
    public void onStop()
    {
        super.onStop();
    }
*/    //  ToDo:: End of onStop

    /**
     * Fragment life: step 9
     * @breif   called on the fragment is destroyed.
     *          no user Action.
     */
/*    //  ToDo:: Destroy your fragment on container.
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }
*/    //  ToDo:: End of onDestroyView

    /**
     * Fragment life: step 10
     * @breif   called on the fragment destroyed completely.
     *          no user Action.
     */
/*    //  ToDo:: Destroy your fragment on container.
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
*/    //  ToDo:: End of onDestroy

    /**
     * Fragment life: step 11
     * @breif   called on the fragment is detached completely.
     *          no user Action.
     */
/*    //  ToDo:: Detach your fragment on container.
    @Override
    public void onDetach()
    {
        super.onDetach();
    }
*/    //  ToDo:: End of onDetach

    /**
     * Fragment life: step 12
     * @breif   called on the fragment disappeared and activity disappeared simultaneously.
     *          save the status onto Bundle and get it when return.
     * @param savedInstanceState
     */
/*    //  ToDo:: save status of fragment onto Bundle.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {

        super.onSaveInstanceState(savedInstanceState);
    }
*/    //  ToDo:: End of onSaveInstanceState


    /**
     * @brief   callback function:
     *     - parent Activity tx data to this fragment
     */
    public void activityToPaymentCreditCb(int cmd, Object obj) {
        switch(cmd)
        {
            //case    ActivityToHomeCmd_DeviceAdapter:
            //    mDeviceAdapter = (DeviceAdapter) obj;       //deviceAdapter;
            //   break;

            default:
                break;
        }

    }

    private void PaymentCreditToActivity(int cmd, Object obj)
    {
        mCallback.paymentCreditToActivityCb(cmd, obj);
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
			value.put(IEmvUserMessages.UserKeys.amount, mEditTextAmount.getText().toString() );
			return ret;
		}

		/**
		 * do check card as a request of device
		 */
		public void doCheckCard(String strCheckCardMode)
		{

			//	startCheckCard( checkCardMode );
			startCheckCard(strCheckCardMode);
		}

		public void doStartEmv(String strCheckCardMode)
		{

			String amount = ApiString.requireText(mEditTextAmount);
			startEmv(strCheckCardMode, amount);
		}

		public void endCardReader(Hashtable<String, Object> result)
		{
			boolean finalResult = (boolean) result.get(IEmvUserMessages.UserKeys.finalResult);

			if(finalResult)
				doOnLineProgress();
			else
				showDialog("카드리드오류");
		}
	}

	private void showDialog(String msg)
	{

	}

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

		doPaymentCredit();
	}

	private void doPaymentCredit()
	{
		//updateDialogMsg(R.string.msg_pay_step_2_make_packet);
		String amount = mEditTextAmount.getText().toString().replace(",", "");
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

		if( VanStaticData.mmPayTypeSub == IVanSpecification.CreditSubType.ICC_SWIPE	||
			VanStaticData.mmPayTypeSub == IVanSpecification.CreditSubType.GIFT)
			mmReceiptEntity.setCardNo(mmBankCardData);
        else
			mmReceiptEntity.setCardNo("");

		mmReceiptEntity.setVanName(mVanName);
		mmReceiptEntity.setCompanyNo( mmCompanyEntity.getCompanyNo() );
		mmReceiptEntity.setMachineCode( mmCompanyEntity.getMachineCode() );
		mmReceiptEntity.setTypeSub( VanStaticData.mmPayTypeSub );
		mmReceiptEntity.setType( IVanSpecification.PaymentType.Credit );
		mmReceiptEntity.setReciptImage("");
		mmReceiptEntity.setCardInputMethod(VanStaticData.mmCardInputMethod);
		mmReceiptEntity.setStaffName(AppHelper.AppPref.getCurrentUserName());
		mmReceiptEntity.setUserID(AppHelper.AppPref.getCurrentUserID());
		mmReceiptEntity.setRequestDate(ApiDate.getYYYYMMDD());			//DateHelper.getCurrentDateFull()//
		mmReceiptEntity.setMonth( ApiString.appendZeroNumber(mmDiviMonth, 2));
		mmReceiptEntity.setApprovalCode("");
		resetCardNo();

		ApiLog.Dbg("receipt to pay:" + mmReceiptEntity.toString());

		doSendVanServer(mmReceiptEntity);
	}

	private void doSendVanServer(final ReceiptEntity receiptEntity) {
		final String cardToCancel = receiptEntity.getCardNo();
		VanStaticData.mmPaymentSuccess = IVanSpecification.SuccessMsg.ForPayment.PaymentSuccess_Credit;

		PaymentTask.CallBackMethod callBackMethod = new PaymentTask.CallBackMethod() {
			@Override
			public String run() {
				mPaymentTask.updateCDialog(R.drawable.progress_sending);

				String result = setReceiptPayment(receiptEntity);
				return null;
			}

			@Override
			public boolean res(String result) {
				return false;
			}
		};
		mPaymentTask = new PaymentTask(mmActivity, callBackMethod, 0, R.drawable.progress_ing);
		mPaymentTask.execute();

	}

	private String setReceiptPayment(ReceiptEntity receiptEntity) {
		String result = null;
		ApiLog.Dbg("Vanname:" + mVanName);

		mmPaymentEmv = new CreditCard();
		mmPaymentMsr = new CreditCard();

		String signData = "";
		if( (mmSignImageByte != null) && (mmSignImageByte.length > 0) )
		{
			try {
				signData = new String(mmSignImageByte);
			}catch (Exception ex){
				ex.printStackTrace();
				signData="";
			}
		}

		String emvData = EmvUtils.getEmvData();
		EncPayInfo encPayInfo = new EncPayInfo("", emvData, signData);

		if (receiptEntity.getCardInputMethod().equals(DaouDataContants.VAL_WCC_IC))
			result = mmPaymentEmv.payEmv(receiptEntity, encPayInfo);
		else
			result = mmPaymentMsr.pay(receiptEntity, encPayInfo);


		// ????????????? added by David SH Kim. to debug. Must remove this.
		onlineProgressResp(OnlineProgressReturnType.SIMPLE_RESP,null,null);
		return result;
	}


    //================================
    // *  private methods
    //=================================
	private void initComponents()
	{
//		mEmvReaderHelper = new EmvReaderHelper(mmActivity);
//		mEmvReader = AppHelper.getEmvReaderInService();


		super.setEmvReaderFragCB( new UserEmvReaderFragCb() );

		mReceiptEntity = new ReceiptEntity();
	}

    private void setView(LayoutInflater inflater, ViewGroup container, View containView) {

        //	ToDo:: attach listeners

        //  ToDo:: set all event listeners like button on click listener if you have
        Button btn = (Button)mFragmentView.findViewById(R.id.btn_foot_confirm);
        btn.setOnClickListener(mButtonListener);

        btn = mFragmentView.findViewById(R.id.btn_foot_cancel);
        btn.setOnClickListener(mButtonListener);


		/**
		 * Month Division Spinner
		 */
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mmActivity, R.array.list_month, R.layout.spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerDiviMonth = (Spinner) mFragmentView.findViewById(R.id.edCreditMonth);
		mSpinnerDiviMonth.setAdapter(adapter);
		mSpinnerDiviMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					mmDiviMonth = "00";
				} else {
					mmDiviMonth = mSpinnerDiviMonth.getSelectedItem().toString();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void updateView(View view)
    {
        // ToDo: update any view element you want

		/*
		 *	link the numeric keyboard to Amount EditText Field
		 */

		final LinearLayout amountLayout = mFragmentView.findViewById(R.id.fragment_creadit_input_money_layout);
		mEditTextAmount = amountLayout.findViewById(R.id.edInputAmount);

		mEditTextAmount.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			    switch( event.getAction() ){
					case	MotionEvent.ACTION_DOWN:
						PaymentCreditToActivity(CommonFragToActivityCmd_ShowNumericKeyboard, mEditTextAmount);
						break;
					default:
						break;
				}
				return true;
			}

		});


    }

    private View.OnClickListener mButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case	R.id.btn_foot_cancel:
					activityToPaymentCreditCb(CommonFragToActivityCmd_ChangePage, null);
					break;
				case	R.id.btn_foot_confirm:
					String amount = ApiString.requireText(mEditTextAmount);
					// startReadCard(amount);
					startCheckCard( IEmvUserMessages.CheckCardMode.SWIPE_OR_INSERT );
					break;
				default:
					break;
			}
		}
	};


    //##########################################
    //  private variables
    //##########################################
    /*
     *  To communicate with parent activity
     *  #1. declare callback
     */
    private PaymentCreditToActivity			mCallback;

    private View							mFragmentView;
   // private LinearLayout					mKbView;
    private EditText						mEditTextAmount;

 //   private EmvReader						mEmvReader;

	private PaymentTask						mPaymentTask;
    private ReceiptEntity					mReceiptEntity;
    private Spinner 						mSpinnerDiviMonth;

	private String							mVanName;

}