package com.payfun.van.daou.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payfun.van.daou.R;

import ginu.android.van.app_daou.entity.CompanyEntity;
import ginu.android.van.app_daou.utils.IVanString;
import ginu.android.van.app_daou.utils.MyTypeFace;

import static ginu.android.van.app_daou.daou.DaouDataContants.VAL_TERMINAL_DIVISION_GENERAL;
import static ginu.android.van.app_daou.daou.DaouDataContants.VAL_TERMINAL_DIVISION_MULTI_VENDOR;
import static ginu.android.van.app_daou.daou.DaouDataContants.VAL_TERMINAL_DIVISION_OFFLINE_PC;

/**
 * Created by david on 2018-10-26
 * Copyright (c) GINU Co., Ltd. All rights reserved.
 */
public class ShowFragmentTopView {
	public static void setFragmentTopView(Activity activity, View topView, CompanyEntity companyEntity)
	{
		LinearLayout llTopView = (LinearLayout)topView;
		if(companyEntity == null)
			return;

		MyTypeFace.setTypeFace(activity, topView);											// ToDo:: setTypeFace

		TextView textView = llTopView.findViewById(R.id.txtCard_reading_CompanyName);	// Company Name
		textView.setText( companyEntity.getCompanyName());

		textView = llTopView.findViewById(R.id.txtCard_reading_CompanyOwnerName);		// OwnerName
		textView.setText( companyEntity.getCompanyOwnerName() );

		textView = llTopView.findViewById(R.id.txtCard_reading_CompanyNo);				// Company Number
		textView.setText( companyEntity.getCompanyNo() );

		textView = llTopView.findViewById(R.id.txtCard_reading_VanName);					// VanName
		textView.setText( companyEntity.getVanName() );

		textView = llTopView.findViewById(R.id.txtCard_terminal_devision);				// Terminal Mode
		switch( companyEntity.getPhoneCode() )
		{
			case	VAL_TERMINAL_DIVISION_GENERAL:
				textView.setText(IVanString.termainalDevision.generalMode);
				break;
			case	VAL_TERMINAL_DIVISION_OFFLINE_PC:
				textView.setText(IVanString.termainalDevision.offlinePG);
				break;
			case	VAL_TERMINAL_DIVISION_MULTI_VENDOR:
				textView.setText(IVanString.termainalDevision.multiVendor);
				break;
			default:
				textView.setText(IVanString.termainalDevision.undefined);
				break;
		}
	}
}
