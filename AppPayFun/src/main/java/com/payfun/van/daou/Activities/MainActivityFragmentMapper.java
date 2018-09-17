package com.payfun.van.daou.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.payfun.van.daou.R;
import com.payfun.van.daou.fragments.AMainFragPages;
import com.payfun.van.daou.fragments.FragmentDummy;
import com.payfun.van.daou.fragments.FragmentHome;
import com.payfun.van.daou.fragments.FragmentPaymentCredit;

import ginu.android.library.utils.gui.ApiFragment;

/**
 * Created by david on 2018-07-17
 * Copyright (c) GINU Co., Ltd. All rights reserved.
 */
public class MainActivityFragmentMapper
{
	static boolean changePage(AppCompatActivity activity, @AMainFragPages int page, Bundle savedInstanceState)
	{
		Fragment fragment = null;
		boolean isBackStack = false;
		String backStackTag = null;

		switch (page)
		{
			case	AMainFragPages.MainHomePage:
				fragment = new FragmentHome();
				break;
			case AMainFragPages.PaymentCreditPage:
				fragment = new FragmentPaymentCredit();
				break;
			case AMainFragPages.PaymentCashPage:
				fragment = new FragmentDummy();
				break;
			case AMainFragPages.HistoryListPage:
				fragment = new FragmentDummy();
				break;
			case AMainFragPages.CancelPaymentPage:
				fragment = new FragmentDummy();
				break;
			case AMainFragPages.CouponPage:
				fragment = new FragmentDummy();
				break;
			case AMainFragPages.ReceiptViewPage:
				fragment = new FragmentDummy();
				break;
			default:
				Log.e("[MainFragMapper]", "Unmapped fragment page" + page);
				return false;
		}

		if( ! showFragment(activity, savedInstanceState, fragment, page, isBackStack, backStackTag) )
			return false;

		return true;
	}

	private static boolean showFragment(
			AppCompatActivity activity,
			Bundle savedInstanceState,
			final Fragment fragment,
			@AMainFragPages int position,
			boolean isBackStack,
			String backStackTag
	)
	{
		boolean ret = ApiFragment.showFragment(
				activity,							// AppCompatActivity
				savedInstanceState,					// Bundle
				R.id.main_fragment_container,		// Container View
				fragment,			// Fragment
				position,							// position
				isBackStack,				// BackStack ?
				backStackTag					// back stack fragment
		);

		return ret;
	}
}
