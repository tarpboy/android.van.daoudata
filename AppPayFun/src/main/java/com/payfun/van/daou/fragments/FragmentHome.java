package com.payfun.van.daou.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import com.payfun.van.daou.R;
import com.payfun.van.daou.fragments.FragmentCallbackInterface.*;

import ginu.android.library.keyboard.ApiEditTextAmount;
import ginu.android.van.app_daou.utils.MyToast;

import static com.payfun.van.daou.fragments.FragmentCallbackInterface.*;

/**
 * Created by david_shkim on 2018-03-13.
 */

public class FragmentHome extends Fragment implements FragmentCallbackInterface.ActivityToHome{

    public static FragmentHome newInstance(int sectionNumber) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUM, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHome() {}

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
            mCallback = (FragmentCallbackInterface.HomeToActivity) mActivity;
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
/*  // ToDo:: Enable, if you want to initialize essential components.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //  initialize essential components here
        initComponents();
    }
*/ // ToDo:: End of onCreate

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
        mFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

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
/*    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        ImageView iv = (ImageView) mFragmentView.findViewById(R.id.iv_product_logo);
        RotateAnim ra = new RotateAnim(mActivity, iv);
        ra.byXml();

        Button btRegister = (Button)mFragmentView.findViewById(R.id.bt_register);
        btRegister.setOnClickListener(new buttonOnClickListener());

    }
*/    // ToDo:: End of onActivityCreated

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
*/    //  ToDo:: End of onResume

    /**
     * Fragment life: step 7
     * @breif   called on removing/replaced the fragment..
     *          no user Action.
     */
/*    //  ToDo:: remove your fragment on container.
    @Override
    public void onPause()
    {
        super.onPause();
    }
*/    //  ToDo:: End of onPause

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


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if( requestCode == PROFILE_ACTIVITY_REQUEST_CODE)
		{
			HomeToActivity( HomeToActivityCmd_AttachEmvDetectionListener, null);
		}
	}

    /**
     * @brief   callback function:
     *     - parent Activity tx data to this fragment
     */
    public void activityToHomeCb(int cmd, Object obj) {
        switch(cmd)
        {
            case    ActivityToHomeCmd_DeviceAdapter:
            //    mDeviceAdapter = (DeviceAdapter) obj;       //deviceAdapter;
                break;

            default:
                break;
        }

    }

	//=================================
	// *  private methods
	//=================================
    private void HomeToActivity(int cmd, Object obj)
    {
        mCallback.homeToActivityCb(cmd, obj);
    }

    private void setView(LayoutInflater inflater, ViewGroup container, View containView) {

        //  ToDo:: update view if you need
        updateView();

        //  ToDo:: set all event listeners like button on click listener if you have
        /*  Ex]
        Button mBtPayment = (Button)mFragmentView.findViewById(R.id.bt_payment_confirm);
        mBtPayment.setOnClickListener(new buttonOnClickListener());
        */
        return;
    }

    private void updateView()
    {
        // ToDo: update any view element you want

		for(int i=1; i< mHomeButtonId.length; i++) {
			ImageButton btn = mFragmentView.findViewById( mHomeButtonId[i] );
			btn.setOnClickListener( mOnClickListener );
		}

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			switch ( view.getId() )
			{
				case	R.id.btnMMCreditCard:			// 카드결제
					HomeToActivity(CommonFragToActivityCmd_ChangePage, 1);
					break;
				case	R.id.btnMMCashCard:			// 현금결제
					// HomeToActivity(CommonFragToActivityCmd_ChangePage, 2);
					break;
				case	R.id.btnMMCancelList:			// 내역조회
					//	HomeToActivity(CommonFragToActivityCmd_ChangePage, 3);
					break;
				case	R.id.btnMMCancelPayment:		// 승인취소
					// HomeToActivity(CommonFragToActivityCmd_ChangePage, 4);
					break;
				case	R.id.btnMMCoupon:				// 쿠폰등록
					// HomeToActivity(CommonFragToActivityCmd_ChangePage, 5);
					MyToast.showToast(mActivity, "준비중입니다.");
					break;
				case	R.id.btnMMMemberShip:			// 프로파일
					//	Launch External Profile Activity in profile module
					Intent intent = null;
					try {
						intent = new Intent( mActivity,	Class.forName(PROFILE_ACTIVITY_CLASS_NAME) );

						startActivityForResult(intent, PROFILE_ACTIVITY_REQUEST_CODE);			// Launch ProfileActivity !!
						HomeToActivity(HomeToActivityCmd_DetachEmvDetectionListener, null);

					//	startActivity(intent);						// Launch ProfileActivity !!
					} catch (ClassNotFoundException e)
					{
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		}
	};
    //================================
    //  private variables
    //================================
	private final static String	PROFILE_ACTIVITY_CLASS_NAME	= "ginu.android.van.profile.ProfileActivity";
	private final static int PROFILE_ACTIVITY_REQUEST_CODE	= 9988;			// Must be a 16-bit int
    /*
     *  To communicate with parent activity
     *  #1. declare callback
     */
    private FragmentCallbackInterface.HomeToActivity mCallback;

    private Activity        mActivity;
    private View            mFragmentView;

    private int			mHomeButtonId[]	= {
    							0,					// dummy id
								R.id.btnMMCreditCard,
								R.id.btnMMCashCard,
								R.id.btnMMCancelList,
								R.id.btnMMCancelPayment,
								R.id.btnMMCoupon,
								R.id.btnMMMemberShip
							};



}
