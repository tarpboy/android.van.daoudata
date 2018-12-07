package com.payfun.van.daou.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.payfun.van.daou.R;

import ginu.android.library.utils.common.ApiAux;

/**
 * Created by david on 2018-12-06
 * Copyright (c) GINU Co., Ltd. All rights reserved.
 */
public class DialogPrintOutMode extends Dialog {
	public DialogPrintOutMode(Activity activity)
	{
		super(activity);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_print_out_mode);

		mLayoutEMAIL	= findViewById(R.id.layout_dialog_print_out_mode_email);
		mLayoutSMS		= findViewById(R.id.layout_dialog_print_out_mode_sms);

		mBtnEMAIL		= findViewById(R.id.btn_dialog_print_out_mode_email);
		mBtnSMS		= findViewById(R.id.btn_dialog_print_out_mode_sms);
		mBtnPRINTER	= findViewById(R.id.btn_dialog_print_out_mode_printer);

		mEditTextEMAIL = findViewById(R.id.edt_dialog_print_out_mode_email);
		mEditTextSMS	= findViewById(R.id.edt_dialog_print_out_mode_sms);

		LinearLayout layoutFoot = findViewById(R.id.layout_dialog_print_out_foot);
		mBtnCancel		= layoutFoot.findViewById(R.id.btn_foot_cancel);
		mBtnOK			= layoutFoot.findViewById(R.id.btn_foot_confirm);
	}

	public void setButtonListener(String btnWhat, View.OnClickListener listener)
	{
		switch(btnWhat)
		{
			case	IPrintOutModeDialogButtons.cancel:
				mBtnCancel.setOnClickListener(listener);
				break;
			case	IPrintOutModeDialogButtons.confirm:
				mBtnOK.setOnClickListener(listener);
				break;
			case	IPrintOutModeDialogButtons.sms:
				mBtnSMS.setOnClickListener(listener);
				break;
			case	IPrintOutModeDialogButtons.email:
				mBtnEMAIL.setOnClickListener(listener);
				break;
			case	IPrintOutModeDialogButtons.printer:
				mBtnPRINTER.setOnClickListener(listener);
				break;
			default:
				break;
		}
	}
	public void setInputMode(String mode)
	{
		mLayoutSMS.setVisibility(View.INVISIBLE);
		mLayoutEMAIL.setVisibility(View.INVISIBLE);
		mSelectedMode = mode;
		switch(mode){
			case	IPrintOutModeDialogButtons.sms:
				mLayoutSMS.setVisibility(View.VISIBLE);
				break;
			case	IPrintOutModeDialogButtons.email:
				mLayoutEMAIL.setVisibility(View.VISIBLE);
				break;
			case	IPrintOutModeDialogButtons.printer:
				break;
		}
	}

	public String getSmsInfo()
	{
		String sms = mEditTextSMS.getText().toString();
		return sms.trim();
	}

	public String getEmailInfo()
	{
		String email = mEditTextEMAIL.getText().toString();
		return email.trim();
	}

	public String getSelectedMode()
	{
		return mSelectedMode;
	}

	public void hideSoftKeyboardOnDialog(Activity activity)
	{
		ApiAux.hideSoftKeyboard(activity, mEditTextEMAIL);
		ApiAux.hideSoftKeyboard(activity, mEditTextSMS);
	}

	public interface IPrintOutModeDialogButtons{
		String sms		= "sms";
		String email	= "email";
		String printer	= "printer";
		String cancel	= "cancel";
		String confirm	= "confirm";
	}

	//######################################
	//	Private Variables on Dialog
	//######################################
	private LinearLayout			mLayoutSMS, mLayoutEMAIL;
	private Button					mBtnSMS, mBtnEMAIL, mBtnPRINTER, mBtnCancel, mBtnOK;
	private EditText				mEditTextSMS, mEditTextEMAIL;
	private String					mSelectedMode = IPrintOutModeDialogButtons.printer;
}
