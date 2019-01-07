package com.payfun.van.daou.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.payfun.van.daou.R;

import java.lang.reflect.Type;
import java.util.Hashtable;

import ginu.android.library.receipt.ApiReceipt;
import ginu.android.library.utils.common.ApiLog;
import ginu.android.library.utils.common.ApiString;
import ginu.android.van.app_daou.BaseFragment.FragmentReceiptBase;
import ginu.android.van.app_daou.ExternalCall.ExtCallRespData;
import ginu.android.van.app_daou.cardreader.IEmvUserMessages;
import ginu.android.van.app_daou.database.VanStaticData;
import ginu.android.van.app_daou.entity.ReceiptEntity;
import ginu.android.van.app_daou.helper.AppHelper;

import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangeHeaderTitle;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangePage;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_StopAppToReturnExtCaller;
import static ginu.android.library.utils.gui.IFragmentConstant.ARG_SECTION_NUMBER;

/**
 * Created by david_shkim on 2018-03-13.
 */

public class FragmentReceipt extends FragmentReceiptBase implements FragmentCallbackInterface.ActivityToReceipt
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
			mCallback = (FragmentCallbackInterface.ReceiptToActivity) mmActivity;
		} catch (ClassCastException e) {
			throw new ClassCastException(mmActivity.toString()
					+ "U must implement CallbackListener");
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
		mmFragmentView = inflater.inflate(R.layout.fragment_receipt, container, false);

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

		showReceiptView();
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

	public void activityToReceiptCb(int cmd, Object obj) {
		switch(cmd)
		{
			//case    ActivityToHomeCmd_DeviceAdapter:
			//    mDeviceAdapter = (DeviceAdapter) obj;       //deviceAdapter;
			//   break;

			default:
				break;
        }
	}

	private void receiptToActivity(int cmd, Object obj)
    {
        mCallback.receiptToActivityCb(cmd, obj);
    }

	//##########################################
	//	Private Methods
	//##########################################
	private void setView(LayoutInflater inflater, ViewGroup container, View view) {

		//  ToDo:: set all event listeners like button on click listener if you have
		Button btn = (Button)mmFragmentView.findViewById(R.id.btn_foot_confirm);
		btn.setText( getString( R.string.receipt_button_ok) );
		btn.setOnClickListener(mButtonListener);

		btn = mmFragmentView.findViewById(R.id.btn_foot_cancel);
		btn.setText( getString( R.string.receipt_button_cancel) );
		btn.setOnClickListener(mButtonListener);
		return;
	}

	private void updateView(View view)
	{
		// ToDo: update any view element you want

	}

	private View.OnClickListener mButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case	R.id.btn_foot_cancel:
					// ToDo:: go to Home
					if( VanStaticData.getIsExternalCall() )
						receiptToActivity(CommonFragToActivityCmd_StopAppToReturnExtCaller, null);
					else
						receiptToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.MainHomePage);
					break;
				case	R.id.btn_foot_confirm:
					// ToDo:: print out
					receiptToActivity(CommonFragToActivityCmd_ChangePage, AMainFragPages.PrintViewPage);
					break;
				default:
					break;
			}
		}
	};

	private void showReceiptView()
	{
		ReceiptEntity receiptEntity = getReceiptEntityFromVanStaticData();
		String title = getReceiptHeader(receiptEntity);
		receiptToActivity(CommonFragToActivityCmd_ChangeHeaderTitle, title);
		makeReceiptData(receiptEntity);

		if( VanStaticData.getIsExternalCall() )				// added by David SH Kim. 2018/12/18
			returnToExtCaller(receiptEntity);
	}

	private void returnToExtCaller(ReceiptEntity receiptEntity)
	{
		ExtCallRespData respData = ExtCallRespData.fromReceipt(receiptEntity);
		String jsonRespData = ExtCallRespData.toJsonString(respData);
		AppHelper.AppPref.setReturnToExternalCall(jsonRespData);

		if( VanStaticData.IsAutoTestExternalCall() )				// added by David SH Kim. for Auto Test Mode.
			receiptToActivity(CommonFragToActivityCmd_StopAppToReturnExtCaller, null);
	}
	//================================
	//  private variables
	//================================
	/*
	 *  To communicate with parent activity
	 *  #1. declare callback
	 */
	private FragmentCallbackInterface.ReceiptToActivity mCallback;


	private static int mSectionNumber = -1;

	private	 ApiReceipt		mApiReceipt;
}
