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
import ginu.android.library.keyboard.ApiEditTextCompanyNo;
import ginu.android.library.keyboard.ApiEditTextPhoneNo;
import ginu.android.library.keyboard.KeyboardHandler;
import ginu.android.library.utils.common.ApiLog;
import ginu.android.library.utils.common.ApiString;
import ginu.android.van.app_daou.BaseFragment.FragmentPaymentBase;
import ginu.android.van.app_daou.cardreader.IEmvUserMessages;
import ginu.android.van.app_daou.database.IVanSpecification;
import ginu.android.van.app_daou.helper.AppHelper;

import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangePage;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowCompanyNumericKeyboard;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowNumericKeyboard;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowPhoneNumericKeyboard;
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

		}

		public void doUserTransactionResult(String result)
		{

		}

		public void doEmvCardDataResult(boolean isSuccess)
		{
			//	ToDo:: Nothing in here
		}
	}

	///================================
	// *  private methods
	//=================================
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
		mEditTextPhone.setOnTouchListener( mTouchListener);
		//ApiEditTextCompanyNo.setTextChangeListener(mEditTextPhoneOrCompanyCode);

		mEditTextCompanyCode = mFragmentView.findViewById(R.id.fragment_cash_input_company_code);
		mEditTextCompanyCode.setOnTouchListener( mTouchListener);

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
		selectPhoneOrCompanyNoView("phone");
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
						showLocalNumberKeyboard("phone", mEditTextPhone);
						//paymentCashToActivity(CommonFragToActivityCmd_ShowPhoneNumericKeyboard, mEditTextPhone);
					break;
				case R.id.fragment_cash_input_company_code:
					if( event.getAction() == MotionEvent.ACTION_DOWN)
						showLocalNumberKeyboard("company",mEditTextCompanyCode);
						//paymentCashToActivity(CommonFragToActivityCmd_ShowCompanyNumericKeyboard, mEditTextCompanyCode);
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
					paymentCashToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.MainHomePage);
					break;
				case R.id.fragment_cash_btn_card_read:
					//	ToDo:: start reading card
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
			switch ( cashCategoryId )
			{
				case	R.id.fragment_cash_image_btn_select1:		// ToDo:: 현금매출
					selectPhoneOrCompanyNoView("phone");
					selectCategory( cashCategoryId );
					break;
				case R.id.fragment_cash_image_btn_select2:			// ToDo:: 일반영수증
					selectPhoneOrCompanyNoView("phone");
					selectCategory( cashCategoryId );
					break;
				case R.id.fragment_cash_image_btn_select3:			// ToDo:: 소득공제영수증
					selectPhoneOrCompanyNoView("phone");
					selectCategory( cashCategoryId );
					break;
				case R.id.fragment_cash_image_btn_select4:			// ToDo:: 사업자지출증빙영수증
					selectPhoneOrCompanyNoView("company");
					selectCategory( cashCategoryId );
					break;
				case R.id.fragment_cash_image_btn_select5:			// ToDo:: 자진발급
					selectPhoneOrCompanyNoView("phone");
					selectCategory( cashCategoryId );
					break;
				default:
					break;
			}
		}
	};

	private void setCardReaderOnClick(boolean enable)
	{
		if(true) {
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

	private void selectPhoneOrCompanyNoView(String mode)
	{
		if("phone".equalsIgnoreCase(mode)){
			mEditTextPhone.setVisibility(View.VISIBLE);
			mEditTextCompanyCode.setVisibility(View.GONE);
		}else{
			mEditTextPhone.setVisibility(View.GONE);
			mEditTextCompanyCode.setVisibility(View.VISIBLE);
		}
	}

	private void showLocalNumberKeyboard(String mode, EditText editText)
	{
		LinearLayout kbView = mmActivity.findViewById(R.id.numeric_keyboard_layout);

		if( KeyboardHandler.isShow() ) {
			ApiLog.Dbg(Tag + "keyboard already runs");
			return;
		}
		if("phone".equalsIgnoreCase(mode)) {
			//ApiEditTextCompanyNo.dissmissKeyboard();			// ToDo:: toggle keyboard. kill company keyboard
			ApiEditTextPhoneNo.disableShowSoftInput(editText);
			ApiEditTextPhoneNo.showKeyboard(mmActivity, kbView, editText);

			ApiEditTextPhoneNo.setTextChangeListener(editText);
		} else {
			//ApiEditTextPhoneNo.dissmissKeyboard();				// ToDo:: toggle keyboard. kill Phone keyboard.
			ApiEditTextCompanyNo.disableShowSoftInput(editText);
			ApiEditTextCompanyNo.showKeyboard(mmActivity, kbView, editText);

			ApiEditTextCompanyNo.setTextChangeListener(editText);
		}
	}


	//##########################################
	//  private variables
	//##########################################
	private final String Tag = String.format("[%s]", FragmentPaymentCash.class.getSimpleName() );
	/*
	 *  To communicate with parent activity
	 *  #1. declare callback
	 */
	private FragmentCallbackInterface.PaymentCashToActivity mCallback;

	private View            mFragmentView;

	private static int mSectionNumber = -1;

	private Button				mBtnCardRead;
	private EditText			mEditTextPhone, mEditTextCompanyCode, mEditTextAmount;
	private ImageButton		mImgBtnSelect1, mImgBtnSelect2, mImgBtnSelect3, mImgBtnSelect4, mImgBtnSelect5;

}
