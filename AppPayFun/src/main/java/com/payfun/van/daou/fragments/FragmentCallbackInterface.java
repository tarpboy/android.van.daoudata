package com.payfun.van.daou.fragments;


/**
 * Created by david_shkim on 2018-03-13.
 */

public interface FragmentCallbackInterface {
    String ARG_SECTION_NUM  = "section_number";

    /**
     *  Common Commands between Fragments and Activity
    **/
    int CommonFragToActivityCmd_ChangePage		= 10001000;
    int CommonFragToActivityCmd_ShowNumericKeyboard = 10001001;

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
        void paymentCreditToActivityCb(int cmd, Object obj);
    }

	//==================================================//
	/*		<<	==	Payment Credit Fragment	==	>>		*/
	/**
	 *  for Payment Credit Fragment Callbacks
	 **/
	// Activity to ConfigWifi Command

	// << Callback Methods  >>
	interface ActivityToReceipt{
		void activityToReceiptCb(int cmd, Object obj);
	}
	interface ReceiptToActivity{
		void receiptToActivityCb(int cmd, Object obj);
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
