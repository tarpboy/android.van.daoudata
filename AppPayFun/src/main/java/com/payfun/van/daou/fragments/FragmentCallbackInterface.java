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

    /**
     *  for HomeFragment Callbacks
     **/
    //  Activity to Home Command
    int ActivityToHomeCmd_DeviceAdapter		= 20001000;
    int ActivityToHomeCmd_TxRxMsg			= 20001001;
    int ActivityToHomeCmd_RxPkSize			= 20001002;
    int ActivityToHomeCmd_ClearStatic		= 20001003;

    //  Home to Activity

    // << Callback Methods  >>
    interface ActivityToHome{
        void activityToHomeCb(int cmd, Object obj);
    }
    interface HomeToActivity{
        void homeToActivityCb(int cmd, Object obj);
    }

    /**
     *  for Payment Creadit Fragment Callbacks
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
    interface ActivityToPaymentCreadit{
        void activityToPaymentCreaditCb(int cmd, Object obj);
    }
    interface PaymentCreaditToActivity{
        void paymentCreaditToActivityCb(int cmd, Object obj);
    }
}
