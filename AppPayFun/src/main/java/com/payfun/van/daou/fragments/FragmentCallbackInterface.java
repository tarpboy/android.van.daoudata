package com.payfun.van.daou.fragments;


/**
 * Created by david_shkim on 2018-03-13.
 */

public interface FragmentCallbackInterface {
    String ARG_SECTION_NUM  = "section_number";

    /**
     *  Common Commands between Fragments and Activity
    **/
    int CommonFragToActivityCmd_ChangePage						= 10001000;
    int CommonFragToActivityCmd_ShowNumericKeyboard			= 10001001;
	int CommonFragToActivityCmd_ShowPhoneNumericKeyboard		= 10001002;		// only for cash payment
	int CommonFragToActivityCmd_ShowCompanyNumericKeyboard	= 10001003;		// only for cash payment

	int CommonFragToActivityCmd_HideSoftKeyboard				= 10001005;
	int CommonFragToActivityCmd_ChangeHeaderTitle				= 10001100;

	int CommonFragToActivityCmd_ConnectBleDongle			= 10001010;
	int CommonFragToActivityCmd_ConnectBlePrinter			= 10001011;
	int CommonFragToActivityCmd_ConnectEarjackDongle		= 10001012;

	int CommonFragToActivityCmd_StopAppToReturnExtCaller		=  10001111;	// added by David SH Kim. 2018/12/18. for External caller

	//==================================================//
	/*		<<	==		Home Fragment		==	>>		*/
    /**
     *  for HomeFragment Callbacks
     **/
    //  Activity to Home Command
    int ActivityToHomeCmd_DeviceAdapter		= 20001000;

    //  Home to Activity
    int HomeToActivityCmd_DetachEmvDetectionListener		= 20001100;
    int HomeToActivityCmd_AttachEmvDetectionListener	= 20001101;

    // << Callback Methods  >>
    interface ActivityToHome{
        void activityToHomeCb(int cmd, Object obj);
    }
    interface HomeToActivity{
        void homeToActivityCb(int cmd, Object obj);
    }

    //==================================================//
    /*		<<	==	Payment Credit Fragment	==	>>		*/
    /**
     *  for Payment Credit Fragment Callbacks
     **/
    // Activity to ConfigWifi Command
    int ActivityToConfigWifiCmd_DeviceInfo  = 20002000;
    // ConfigWifi to Activity Command
    int ConfigWifiToActivityCmd_BackHome    = 20002100;
    int ConfigWifiToActivityCmd_SaveSSID    = 20002101;
    int ConfigWifiToActivityCmd_SavePWD     = 20002102;
    int ConfigWifiToActivityCmd_SaveMODE    = 20002103;
    int ConfigWifiToActivityCmd_SaveURL     = 20002104;

    // << Callback Methods  >>
    interface ActivityToPaymentCredit{
        void activityToPaymentCreditCb(int cmd, Object obj);
    }
    interface PaymentCreditToActivity{
        void paymentCreditToActivityCb(int cmd, Object obj, Object listener);
    }

	//=================================================//
	/*		<<	==	Payment Cash Fragment	==	>>			*/
	/**
	 *  for Payment Cash Fragment Callbacks
	 **/
	// << Callback Methods  >>
	interface ActivityToPaymentCash{
		void activityToPaymentCashCb(int cmd, Object obj);
	}
	interface PaymentCashToActivity{
		void paymentCashToActivityCb(int cmd, Object obj);
	}

	//=================================================//
	/*		<<	==	Receipt Fragment	==	>>			*/
	/**
	 *  for Receipt Fragment Callbacks
	 **/
	// << Callback Methods  >>
	interface ActivityToReceipt{
		void activityToReceiptCb(int cmd, Object obj);
	}
	interface ReceiptToActivity{
		void receiptToActivityCb(int cmd, Object obj);
	}
	//=================================================//
	/*		<<	==	Print Fragment	==	>>				*/
	// Activity to ConfigWifi Command
	int ActivityToPrintCmd_Connected		= 20005000;
	int ActivityToPrintCmd_DoOnActionResult		= 20005001;
	// ConfigWifi to Activity Command
	int PrintToActivityCmd_PrinterBluetoothConnected		= 20005100;
	int PrintToActivityCmd_PrinterBluetoothDisconnected	= 20005101;
	/**
	 *  for Print Fragment Callbacks
	 **/
	// << Callback Methods  >>
	interface ActivityToPrint{
		void activityToPrintCb(int cmd, Object... obj);
	}
	interface PrintToActivity{
		void printToActivityCb(int cmd, Object obj);
	}

	//=================================================//
	/*		<<	==	Cancel Selector Fragment	==	>>	*/
	/**
	 *  for Cancel Selector Fragment Callbacks
	 **/
	// << Callback Methods  >>
	interface ActivityToCancelSelector{
		void activityToCancelSelectorCb(int cmd, Object obj);
	}
	interface CancelSelectorToActivity{
		void cancelSelectorToActivityCb(int cmd, Object obj);
	}

	//=================================================//
	/*		<<	==	Cancel Credit Fragment	==	>>	*/
	/**
	 *  for Cancel Credit Fragment Callbacks
	 **/
	// << Callback Methods  >>
	interface ActivityToCancelCredit{
		void activityToCancelCreditCb(int cmd, Object obj);
	}
	interface CancelCreditToActivity{
		void cancelCreditToActivityCb(int cmd, Object obj);
	}

	//=================================================//
	/*		<<	==	Cancel Cash Fragment	==	>>		*/
	/**
	 *  for Cancel Cach Fragment Callbacks
	 **/
	// << Callback Methods  >>
	interface ActivityToCancelCash{
		void activityToCancelCashCb(int cmd, Object obj);
	}
	interface CancelCashToActivity{
		void cancelCashToActivityCb(int cmd, Object obj);
	}

	//=================================================//
	/*		<<	==	History Fragment	==	>>			*/
	/**
	 *  for History Fragment Callbacks
	 **/
	// << Callback Methods  >>
	interface ActivityToHistory{
		void activityToHistoryCb(int cmd, Object obj);
	}
	interface HistoryToActivity{
		void historyToActivityCb(int cmd, Object obj);
	}

	//==================================================//
	/*		<<	==	Default Dummy Fragment	==	>>		*/
	/**
	 *	for Dummy Fragment Callbacks
	 */
	// << Callback Methods  >>
	interface ActivityToDummy{			// implements @ fragment
		void activityToDummyCb(int cmd, Object obj);
    }
    interface DummyToActivity{			// implements @ Activity
		void dummyToActivityCb(int cmd, Object obj);
	}
}
