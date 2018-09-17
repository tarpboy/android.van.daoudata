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
				AMainFragPages.CancelPaymentPage,
				AMainFragPages.CouponPage,
				AMainFragPages.ReceiptViewPage
		}
)

@Retention(RetentionPolicy.SOURCE)

public @interface AMainFragPages {
	int	MainHomePage			= 0;
	int PaymentCreditPage		= 1;
	int PaymentCashPage		= 2;
	int HistoryListPage		= 3;
	int CancelPaymentPage		= 4;
	int CouponPage				= 5;
	int ReceiptViewPage		= 6;
}
