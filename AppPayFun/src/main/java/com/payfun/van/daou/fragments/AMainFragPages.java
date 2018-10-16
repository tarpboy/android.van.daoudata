package com.payfun.van.daou.fragments;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by david on 2018-07-17
 * Copyright (c) GINU Co., Ltd. All rights reserved.
 */
@IntDef(
		{
				AMainFragPages.MainHomePage,
				AMainFragPages.PaymentCreditPage,
				AMainFragPages.PaymentCashPage,
				AMainFragPages.HistoryListPage,
				AMainFragPages.CancelSelectorPage,
				AMainFragPages.CouponPage,
				AMainFragPages.ReceiptViewPage,
				AMainFragPages.PrintViewPage,
				AMainFragPages.CancelCreditPage,
				AMainFragPages.CancelCashPage
		}
)

@Retention(RetentionPolicy.SOURCE)

public @interface AMainFragPages {
	int	MainHomePage			= 0;
	int PaymentCreditPage	= 1;
	int PaymentCashPage		= 2;
	int HistoryListPage		= 3;
	int CancelSelectorPage	= 4;
	int CouponPage			= 5;
	int ReceiptViewPage		= 6;
	int PrintViewPage			= 7;
	int CancelCreditPage		= 8;
	int CancelCashPage		= 9;
}
