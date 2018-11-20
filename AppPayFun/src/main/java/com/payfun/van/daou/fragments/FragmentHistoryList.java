package com.payfun.van.daou.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.payfun.van.daou.R;

import ginu.android.van.app_daou.history.HistoryReceiptListDay;
import ginu.android.van.app_daou.history.HistoryReceiptListMonth;
import ginu.android.van.app_daou.history.HistoryReceiptListQuarter;
import ginu.android.van.app_daou.history.HistoryReceiptListTitle;
import ginu.android.van.app_daou.history.IHistory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ginu.android.library.utils.common.ApiCalendar;
import ginu.android.library.utils.common.ApiLog;
import ginu.android.library.utils.gui.ApiDatePickerDialog;
import ginu.android.library.utils.gui.MyTaskProgress;
import ginu.android.van.app_daou.entity.CompanyEntity;
import ginu.android.van.app_daou.entity.ReceiptEntity;
import ginu.android.van.app_daou.helper.AppHelper;
import ginu.android.van.app_daou.manager.CompanyManger;
import ginu.android.van.app_daou.manager.ReceiptManager;
import ginu.android.van.app_daou.utils.HistoryReceiptListAdapter;
import ginu.android.van.app_daou.utils.MyToast;

import static ginu.android.library.utils.gui.IFragmentConstant.ARG_SECTION_NUMBER;

/**
 * Created by david_shkim on 2018-03-13.
 */

public class FragmentHistoryList extends Fragment implements
		FragmentCallbackInterface.ActivityToHistory
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
		// U can get parent activity
		mActivity = null;
		if (context instanceof Activity)
			mActivity = (Activity) context;

		/*
		 *  To communicate with parent activity
		 *  #2. assign callback function
		 */

		// U make sure that the container hsa implemented the callback interface.
		try {
			mCallback = (FragmentCallbackInterface.HistoryToActivity) mActivity;
		} catch (ClassCastException e) {
			throw new ClassCastException(mActivity.toString()
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
		mFragmentView = inflater.inflate(R.layout.fragment_history, container, false);

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

	public void activityToHistoryCb(int cmd, Object obj) {
		switch(cmd)
		{
			//case    ActivityToHomeCmd_DeviceAdapter:
			//    mDeviceAdapter = (DeviceAdapter) obj;       //deviceAdapter;
			//   break;

			default:
				break;
        }
	}

	private void historyToActivity(int cmd, Object obj)
    {
        mCallback.historyToActivityCb(cmd, obj);
    }

	public IHistory.HistoryCallbackMethods mHistoryCallbackMethods = new IHistory.HistoryCallbackMethods() {
		@Override
		public void showReceiptList(ArrayList<ReceiptEntity> receiptList) {
			showReceiptListView(receiptList);
		}
		@Override
		public void setReceiptListTitleValues(int totalNumber, int totalAmount){
			mHistoryReceiptListTitle.setTitleValues(totalNumber, totalAmount);
		}
	};

	private void showReceiptListView(final ArrayList<ReceiptEntity> list){
		new Runnable(){
			public void run(){
				mReceiptListAdapter = new HistoryReceiptListAdapter(mActivity, list);
				mHistoryReceiptListView.setAdapter(mReceiptListAdapter);
				mReceiptListAdapter.notifyDataSetChanged();
			}

		}.run();
	}
    //##########################################
	//	Default (Access Modifier) Methods
	//##########################################


	//##########################################
	//	Private Methods
	//##########################################
	private void initComponents()
	{
		mReceiptArrayList = new ArrayList<>();
	}

	private void setView(LayoutInflater inflater, ViewGroup container, View view)
	{
		/*
		 *  ToDo:: set all event listeners like button on click listener if you have
		 */

		//	ToDo:: Row 1:: Mode Section Buttons
		mBtDay = mFragmentView.findViewById(R.id.bt_history_each_day);
		mBtDay.setOnClickListener( mModeSelectingButtonOnClickListener );
		mBtMonth = mFragmentView.findViewById(R.id.bt_history_each_month);
		mBtMonth.setOnClickListener( mModeSelectingButtonOnClickListener);
		mBtQuarter = mFragmentView.findViewById(R.id.bt_history_each_quarter);
		mBtQuarter.setOnClickListener( mModeSelectingButtonOnClickListener);

		//	ToDo:: Row 2:: term selector each mode
		mHistoryReceiptListDay = new HistoryReceiptListDay(					// History Daily
				mActivity,
				mFragmentView,
				R.id.fragment_history_day_selector,
				mHistoryCallbackMethods,
				mReceiptArrayList);
		mHistoryReceiptListDay.setView();

		mHistoryReceiptListMonth = new HistoryReceiptListMonth(				// History Monthly
				mActivity,
				mFragmentView,
				R.id.fragment_history_month_selector,
				mHistoryCallbackMethods,
				mReceiptArrayList);
		mHistoryReceiptListMonth.setView();

		mHistoryReceiptListQuarter = new HistoryReceiptListQuarter(		// History Quarterly
				mActivity,
				mFragmentView,
				R.id.fragment_history_quarter_selector,
				mHistoryCallbackMethods,
				mReceiptArrayList);

		mHistoryReceiptListQuarter.setView();

		//	ToDo:: Row 3:: list title
		mHistoryReceiptListTitle = new HistoryReceiptListTitle(
				mActivity,
				mFragmentView,
				R.id.fragment_history_list_title,
				mHistoryCallbackMethods,
				mReceiptArrayList);
		mHistoryReceiptListTitle.setView();

		//	ToDo:: Row 4:: list view
		mHistoryReceiptListView = mFragmentView.findViewById(R.id.fragment_history_list_view);

		//	ToDo:: set the initial mode Day
		mBtDay.setSelected(true);
		//mHistoryDay.mLayoutSelectDay.setVisibility(View.VISIBLE);

		return;
	}

	private void updateView(View view)
	{
		// ToDo: update any view element you want
		mHistoryReceiptListTitle.updateView();
		mHistoryReceiptListDay.updateView();
		mHistoryReceiptListMonth.updateView();
		mHistoryReceiptListQuarter.updateView();


	}

	private View.OnClickListener mModeSelectingButtonOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			mBtDay.setSelected(false);
			mBtMonth.setSelected(false);
			mBtQuarter.setSelected(false);

			mHistoryReceiptListDay.setHistoryModeLayoutVisibility(View.GONE);
			mHistoryReceiptListMonth.setHistoryModeLayoutVisibility(View.GONE);
			mHistoryReceiptListQuarter.setHistoryModeLayoutVisibility(View.GONE);
			switch( v.getId() )
			{
				case	R.id.bt_history_each_day:
					mBtDay.setSelected(true);
					mHistoryReceiptListDay.setHistoryModeLayoutVisibility(View.VISIBLE);
					mHistoryReceiptListDay.refreshView();
					mHistoryReceiptListTitle.refreshView();
					break;
				case	R.id.bt_history_each_month:
					mBtMonth.setSelected(true);
					mHistoryReceiptListMonth.setHistoryModeLayoutVisibility(View.VISIBLE);
					mHistoryReceiptListMonth.refreshView();
					mHistoryReceiptListTitle.refreshView();
					break;
				case	R.id.bt_history_each_quarter:
					mBtQuarter.setSelected(true);
					mHistoryReceiptListQuarter.setHistoryModeLayoutVisibility(View.VISIBLE);
					mHistoryReceiptListQuarter.refreshView();
					mHistoryReceiptListTitle.refreshView();
					break;
			}
		}
	};

	//##########################################
	//  Private Variables
	//##########################################
	private String Tag=String.format("[%s]",FragmentHistoryList.class.getSimpleName());
	/*
	 *  To communicate with parent activity
	 *  #1. declare callback
	 */
	private FragmentCallbackInterface.HistoryToActivity mCallback;

	private Activity        mActivity;
	private View            mFragmentView;

	private static int mSectionNumber = -1;

	private Button							mBtDay, mBtMonth, mBtQuarter;
	private HistoryReceiptListDay			mHistoryReceiptListDay;
	private HistoryReceiptListMonth		mHistoryReceiptListMonth;
	private HistoryReceiptListQuarter		mHistoryReceiptListQuarter;
	private HistoryReceiptListTitle		mHistoryReceiptListTitle;


	private ListView						mHistoryReceiptListView;
	private HistoryReceiptListAdapter		mReceiptListAdapter;
	private ArrayList<ReceiptEntity>		mReceiptArrayList;
}
