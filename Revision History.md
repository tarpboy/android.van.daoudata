# Daoudata Revision History

## Tag v1.1.11

Date:	**2019/02/27**

By:	David SH Kim.

### [CN1]

- description

  App 시작시 Bluetooth Enable 처리

  Bluetooth이 disable되어 있으면 local service를 등록하고 난뒤, bluetooth enable 요청하고, user confirm을 onActivityResult()로 수신하면 scan을 시작할 수 있도록 변경.

- source

file: Activity/MainActivity.java

```java
private void enableBluetooth()
	{
		BluetoothManager btMng = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
		BluetoothAdapter btAdapter = btMng.getAdapter();
		if (btAdapter != null) {
			if (!btAdapter.isEnabled())
			{	//	ToDo:: Not Enable, set Bluetooth Enable, wait onActivityResult.
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
			else
			{	//	ToDo:: Enabled, start turning on EmvReader
				waitTurnOnBTReader();
				ExternalCallPayment();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if( requestCode == REQUEST_ENABLE_BT)
		{
			if(resultCode == RESULT_OK)
			{	//	ToDo:: User accept to enable Bluetooth, start turning on EmvReader
				waitTurnOnBTReader();
				ExternalCallPayment();
			}
			else
			{	//	ToDo:: User deny to enable Bluetooth, terminate this app.
				showAppFinish("블루투스 사용거절하였습니다. \n 앱을 종료합니다.", false);
			}
		}
	}
```



### [CN2]

- description

  ApiLog 디버깅 enable/disable을 app level에서 결정짓도록 추가.

- source

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		//
		//	TODO:: Add user code here
		//
		mActivity = this;

		ApiLog.enableLog(true);
		
		...... 중략 .....
}
```

