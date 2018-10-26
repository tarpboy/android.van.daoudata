package com.payfun.van.daou.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.payfun.van.daou.R;
import com.payfun.van.daou.fragments.AMainFragPages;
import com.payfun.van.daou.fragments.FragmentCancelCash;
import com.payfun.van.daou.fragments.FragmentCancelCredit;
import com.payfun.van.daou.fragments.FragmentCancelSelector;
import com.payfun.van.daou.fragments.FragmentDummy;
import com.payfun.van.daou.fragments.FragmentHome;
import com.payfun.van.daou.fragments.FragmentPaymentCash;
import com.payfun.van.daou.fragments.FragmentPaymentCredit;
import com.payfun.van.daou.fragments.FragmentPrint;
import com.payfun.van.daou.fragments.FragmentReceipt;

import ginu.android.library.utils.gui.ApiFragment;
import ginu.android.van.app_daou.utils.IVanString;

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
				MainActivity.setHeaderView(null);
				fragment = new FragmentHome();
				break;
			case AMainFragPages.PaymentCreditPage:
				MainActivity.setHeaderView(IVanString.title.payment_credit);
				fragment = new FragmentPaymentCredit();
				break;
			case AMainFragPages.PaymentCashPage:
				MainActivity.setHeaderView(IVanString.title.payment_cash);
				fragment = new FragmentPaymentCash();
				break;
			case AMainFragPages.HistoryListPage:
				MainActivity.setHeaderView(null);
				fragment = new FragmentDummy();
				break;
			case AMainFragPages.CancelSelectorPage:
				MainActivity.setHeaderView(null);
				fragment = new FragmentCancelSelector();
				break;
			case AMainFragPages.CouponPage:
				MainActivity.setHeaderView(null);
				fragment = new FragmentDummy();
				break;
			case AMainFragPages.ReceiptViewPage:
				MainActivity.setHeaderView(IVanString.title.print_credit);
				fragment = new FragmentReceipt();
				break;
			case	AMainFragPages.PrintViewPage:
			//	MainActivity.setHeaderView(IVanString.title.print_credit);
				fragment = new FragmentPrint();
				break;
			case	AMainFragPages.CancelCreditPage:
				MainActivity.setHeaderView(IVanString.title.cancel_credit);
				fragment = new FragmentCancelCredit();
				break;
			case	AMainFragPages.CancelCashPage:
				MainActivity.setHeaderView(IVanString.title.cancel_cash);
				fragment = new FragmentCancelCash();
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
