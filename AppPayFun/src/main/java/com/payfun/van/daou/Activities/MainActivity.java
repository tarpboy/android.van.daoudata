package com.payfun.van.daou.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.payfun.van.daou.R;
import com.payfun.van.daou.fragments.FragmentPaymentCreadit;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.ConnectionResult;

import com.payfun.van.daou.fragments.FragmentCallbackInterface;
import com.payfun.van.daou.fragments.FragmentDummy;
import com.payfun.van.daou.fragments.FragmentHome;

import ginu.android.library.keyboard.ApiEditTextAmount;
import ginu.android.library.utils.common.ApiLog;


import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ChangePage;
import static com.payfun.van.daou.fragments.FragmentCallbackInterface.CommonFragToActivityCmd_ShowNumericKeyboard;

public class MainActivity extends AppCompatActivity
    implements FragmentCallbackInterface.HomeToActivity,
    FragmentCallbackInterface.PaymentCreaditToActivity
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    //=========================
    //  option menu
    //=========================
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
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
            default:
                break;
        }
    }

    public void paymentCreaditToActivityCb(int cmd, Object obj)
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

    //==========================
    //  Fragment Page Adapter
    //==========================
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment;
            switch(position)
            {
                case 0:
                    //return FragmentHome.newInstance(position + 1);
                    fragment = FragmentHome.newInstance(position+1);
                    mActivityToHome = (FragmentCallbackInterface.ActivityToHome) fragment;
                //    mActivityToHome.activityToHomeCb(ActivityToHomeCmd_DeviceAdapter, mDeviceAdapter);
                    return fragment;
                case 1:
                    fragment = FragmentPaymentCreadit.newInstance(position + 1);
                    mActivityToPaymentCreadit = (FragmentCallbackInterface.ActivityToPaymentCreadit) fragment;
                    return fragment;
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    private void showNumericKeyboard(EditText editTextAmount)
	{
		LinearLayout kbView = findViewById(R.id.numeric_keyboard_layout);

		ApiEditTextAmount.disableShowSoftInput(editTextAmount);
		ApiEditTextAmount.showKeyboard(this, kbView, editTextAmount);
	}
    /**========================
     *  Private Methods
     **=========================*/
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

    /*
     *  change Fragment page
     */
    private void changePage(int page)
    {
        mViewPager.setCurrentItem(page);
    }

    //======================
    //  Public Variables
    //======================

    //======================
    //  Private variables
    //======================
    private FragmentCallbackInterface.ActivityToHome        mActivityToHome;
    private FragmentCallbackInterface.ActivityToPaymentCreadit  mActivityToPaymentCreadit;
}
