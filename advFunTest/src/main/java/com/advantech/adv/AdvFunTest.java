package com.advantech.adv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IHwtestService;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advantech.advfuntest.R;

import java.io.File;

public class AdvFunTest extends Activity {

    private static final String TAG = "AdvFuncTest";
    public static final int cpuCode = 0;
    public static final int powerCode = 1;
    public static final int audioCode = 2;
    public static final int memoryCode = 3;
    public static final int diskCode = 4;
    public static final int udiskCode = 5;
    public static final int ssdCode = 6;
    public static final int wifiCode = 7;
    public static final int rfidCode = 8;
    public static final int ethernetCode = 9;
    public static final int usbCode = 10;
    public static final int sdcardCode = 11;
    public static final int lcdCode = 12;
    public static final int ledCode = 13;

    public static final int CodeCount = 14;

    //public SataFun sataFun;
    //public usbFun usbFun;
    public GRG_KeyPad keypad;
    //public AudioFun audioFun;
    public Ethernet ethFun;
    private RelativeLayout rgbRelative;

    private Button autotestBtn;
    private Button testlogBtn;
    private Button singletestBtn;
    private Button burningBtn;
    private Button quitBtn;
    public FunTest funTest;
    public Device device;
    public ExecCmd execcmd;
    public int currentTestItem = -1;
    public static TextView[] ResultTextView;
    public static CheckBox[] checkBox;
    public boolean isBurning = false;
    public static String[] tinfo = new String[14];
    Toast toastStart = null;
    //SerialPort sp = new SerialPort();
    private HeadsetPlugReceiver headsetPlugReceiver;
    private IHwtestService hwtestService = null;

	/*
    private BroadcastReceiver TestReceiver = new BroadcastReceiver(){
          public void onReceive(Context context, Intent intent) {  
            String text = intent.getStringExtra("test-info");  
            Log.e("broadcast receive: ", text);  
          }  
    	};
	*/

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (currentTestItem >= 0) {
                int result = msg.getData().getInt("result");
                String info = msg.getData().getString("rtinfo");
                if (result == 0) {
                    String str = "Pass!";
                    ResultTextView[currentTestItem].setText(str);
                    ResultTextView[currentTestItem].setTextColor(Color.GREEN);
                } else {
                    String str = "Failed!";
                    ResultTextView[currentTestItem].setText(str);
                    ResultTextView[currentTestItem].setTextColor(Color.RED);
                }
            }
            Log.e(TAG, String.valueOf(currentTestItem));
            for (int i = currentTestItem + 1; i < CodeCount; i++) {
                if (checkBox[i].isChecked()) {
                    currentTestItem = i;
                    Log.e("for", String.valueOf(currentTestItem));
                    startTest(i);
                    break;
                }
            }
        }
    };

    /**
     * 注册耳机插拔的广播
     */
    private void registerHeadsetPlugReceiver() {
        headsetPlugReceiver = new HeadsetPlugReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(headsetPlugReceiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advfuntest);
        execcmd = new ExecCmd();
        //isAdvantech();
        //device = new Device();
        //funTest = new FunTest();
        Log.i(TAG, "oncreate");
        toastStart = new Toast(this);
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                int index = -1;
                switch (v.getId()) {
                    case R.id.cpuFun:
                        index = cpuCode;
                        break;
                    case R.id.powerFun:
                        index = powerCode;
                        break;
                    case R.id.audioFun:
                        index = audioCode;
                        break;
                    case R.id.memoryFun:
                        index = memoryCode;
                        break;
                    case R.id.diskFun:
                        index = diskCode;
                        break;
                    case R.id.ssdFun:
                        index = ssdCode;
                        break;
                    case R.id.udiskFun:
                        index = udiskCode;
                        break;
                    case R.id.wifiFun:
                        index = wifiCode;
                        break;
                    case R.id.rfidFun:
                        index = rfidCode;
                        break;
                    case R.id.ethernetFun:
                        index = ethernetCode;
                        break;
                    case R.id.usbFun:
                        index = usbCode;
                        break;
                    case R.id.sdcardFun:
                        index = sdcardCode;
                        break;
                    case R.id.lcdFun:
                        index = lcdCode;
                        break;
                    case R.id.ledFun:
                        index = ledCode;
                        break;
                    default:
                        break;
                }
                /*
                toast = Toast.makeText(getApplicationContext(),tinfo[index], Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(toastRoot);
                                toast.show();
				*/
                Log.i(TAG, "index:" + index);
                View toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
                TextView message = (TextView) toastRoot.findViewById(R.id.message);
                message.setText(tinfo[index]);
                //Toast toastStart = new Toast(this);
                toastStart.setGravity(Gravity.CENTER, 0, 0);
                toastStart.setDuration(Toast.LENGTH_LONG);
                toastStart.setView(toastRoot);
                toastStart.show();
            }
        };

        ResultTextView = new TextView[CodeCount];
        ResultTextView[0] = (TextView) findViewById(R.id.cpuFun);
        ResultTextView[1] = (TextView) findViewById(R.id.powerFun);
        ResultTextView[2] = (TextView) findViewById(R.id.audioFun);
        ResultTextView[3] = (TextView) findViewById(R.id.memoryFun);
        ResultTextView[4] = (TextView) findViewById(R.id.diskFun);
        ResultTextView[5] = (TextView) findViewById(R.id.udiskFun);
        ResultTextView[6] = (TextView) findViewById(R.id.ssdFun);
        ResultTextView[7] = (TextView) findViewById(R.id.wifiFun);
        ResultTextView[8] = (TextView) findViewById(R.id.rfidFun);
        ResultTextView[9] = (TextView) findViewById(R.id.ethernetFun);
        ResultTextView[10] = (TextView) findViewById(R.id.usbFun);
        ResultTextView[11] = (TextView) findViewById(R.id.sdcardFun);
        ResultTextView[12] = (TextView) findViewById(R.id.lcdFun);
        ResultTextView[13] = (TextView) findViewById(R.id.ledFun);


        ResultTextView[0].setOnClickListener(listener);
        ResultTextView[1].setOnClickListener(listener);
        ResultTextView[2].setOnClickListener(listener);
        ResultTextView[3].setOnClickListener(listener);
        ResultTextView[4].setOnClickListener(listener);
        ResultTextView[5].setOnClickListener(listener);
        ResultTextView[6].setOnClickListener(listener);
        ResultTextView[7].setOnClickListener(listener);
        ResultTextView[8].setOnClickListener(listener);
        ResultTextView[9].setOnClickListener(listener);
        ResultTextView[10].setOnClickListener(listener);
        ResultTextView[11].setOnClickListener(listener);
        ResultTextView[12].setOnClickListener(listener);
        ResultTextView[13].setOnClickListener(listener);

        checkBox = new CheckBox[CodeCount];
        checkBox[0] = (CheckBox) findViewById(R.id.cpuCheckBox);
        checkBox[1] = (CheckBox) findViewById(R.id.powerCheckBox);
        checkBox[2] = (CheckBox) findViewById(R.id.audioCheckBox);
        checkBox[3] = (CheckBox) findViewById(R.id.memoryCheckBox);
        checkBox[4] = (CheckBox) findViewById(R.id.diskCheckBox);
        checkBox[5] = (CheckBox) findViewById(R.id.udiskCheckBox);
        checkBox[6] = (CheckBox) findViewById(R.id.ssdCheckBox);
        checkBox[7] = (CheckBox) findViewById(R.id.wifiCheckBox);
        checkBox[8] = (CheckBox) findViewById(R.id.rfidCheckBox);
        checkBox[9] = (CheckBox) findViewById(R.id.ethernetCheckBox);
        checkBox[10] = (CheckBox) findViewById(R.id.usbCheckBox);
        checkBox[11] = (CheckBox) findViewById(R.id.sdcardCheckBox);
        checkBox[12] = (CheckBox) findViewById(R.id.lcdCheckBox);
        checkBox[13] = (CheckBox) findViewById(R.id.ledCheckBox);


        //initial string
        int i = 0;
        for (i = 0; i < CodeCount; i++) {
            tinfo[i] = "Result:\n";
        }

        registerHeadsetPlugReceiver();

        //jinxin added
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        //jinxin added end
	
		/*	
		rgbRelative=(RelativeLayout) findViewById(R.id.rgbRelative);
		rgbRelative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// execcmd.frabuffer_rgb();
				Intent intent = new Intent(AdvFunTest.this, RGB_View.class);
				startActivity(intent);
			}
		});
		*/
        quitBtn = (Button) findViewById(R.id.quitBtn);
        quitBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AdvFunTest.this.finish();
            }

        });
        //自检程序
        autotestBtn = (Button) findViewById(R.id.autotestBtn);
        autotestBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                execcmd.cleanLog();
                currentTestItem = -1;
                int i = 0;
                for (i = 0; i < CodeCount; i++) {
                    tinfo[i] = "Result:\n";
                }
                handler.sendEmptyMessage(0);
            }

        });
        //demo演示
        testlogBtn = (Button) findViewById(R.id.testlogBtn);
        testlogBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(AdvFunTest.this, MainActivity.class);
                startActivity(intent);
            }

        });
        //单项自检
        singletestBtn = (Button) findViewById(R.id.singletestBtn);
        singletestBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.setClass(AdvFunTest.this, MainActivity.class);
                startActivity(mIntent);
            }
        });
		/*
		burningBtn=(Button)findViewById(R.id.burningBtn);
		burningBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AdvFunTest.this, LoopbackActivity.class);
				isBurning = true;
				intent.putExtra("isBurning", isBurning);
				startActivity(intent);
			}
			
		});	
		*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		/*
		case uartCode:
			TextView uartFun = (TextView) findViewById(R.id.uartFun);
			if (isBurning == false) {
				device.uartFun = data.getBooleanExtra("uartFun", false);
				device.uartMsg = data.getStringExtra("uartMsg").toString();
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("result", device.uartFun);
				msg.setData(bundle);// mes利用Bundle传递数据
				handler.sendMessage(msg);// 用activity中的handler发送消息
			}
			break;
		*/
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        unregisterReceiver(headsetPlugReceiver);
        super.onDestroy();
    }
	
/*	
	@Override
	public boolean onKeyDown(int key_Code, KeyEvent event) {
		// TODO Auto-generated method stub
		if(checkBox[keyCode].isChecked()){
			keypad.onKeyDown(key_Code, this, ResultTextView[keyCode]);
		}
		return super.onKeyDown(key_Code, event);
	}
*/

    void isAdvantech() {
		
/*		int value1 = 1, value2 = 1, value3 = 1;
		File file = new File("/sys/class/gpio/gpio1/value");
		if (file.exists())
			value1 = Integer.parseInt(execcmd.resultExeCmd("cat /sys/class/gpio/gpio1/value"));
		Log.e("+++yixuan+++",String.valueOf(value1));
		file = new File("/sys/class/gpio/gpio2/value");
		if (file.exists())
			value2 = Integer.parseInt(execcmd.resultExeCmd("cat /sys/class/gpio/gpio2/value"));
		Log.e("+++yixuan+++",String.valueOf(value2));
		file = new File("/sys/class/gpio/gpio3/value");
		if (file.exists())
			value3 = Integer.parseInt(execcmd.resultExeCmd("cat /sys/class/gpio/gpio3/value"));
		Log.e("+++yixuan+++",String.valueOf(value1));
		file = new File("/sys/class/gpio/gpio4/value");
		if(value1 == 0 && value2 == 0 && value3 ==0 && (file.exists() == false))
			return;
		this.finish();*/
		/*		execcmd.flash_to_file();
		String str = execcmd.resultExeCmd("busybox hexdump -C /data/data/com.advantech.advfuntest/flag");
		Log.e("+++yixuan+++", str);
		String a[] = str.split("aa");
		String b[] = a[1].split("dd");
		Log.e("+++yixuan+++", b[0]);
		*/

        int value1 = 1, value2 = 1, value3 = 1;
        File file1 = new File("/sys/class/gpio/gpio1/value");
        File file2 = new File("/sys/class/gpio/gpio2/value");
        File file3 = new File("/sys/class/gpio/gpio3/value");
        File file4 = new File("/sys/class/gpio/gpio4/value");
        if ((file1.exists() == true) && (file2.exists() == true) && (file3.exists() == true) && (file4.exists() == false))
            return;
        this.finish();
    }

    byte wifi_test() {
        String cSummary = null;
        //wifi state
        /**
         * Listing 16-14: Accessing the Wi-Fi Manager
         */
        String service = Context.WIFI_SERVICE;
        final WifiManager wifi = (WifiManager) getSystemService(service);

        /**
         * Listing 16-15: Monitoring and changing Wi-Fi state
         */
        if (!wifi.isWifiEnabled())
            if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                wifi.setWifiEnabled(true);

        /**
         * Listing 16-16: Querying the active network connection
         */
        WifiInfo info = wifi.getConnectionInfo();
        if (info.getBSSID() != null) {
            int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
            int speed = info.getLinkSpeed();
            String units = WifiInfo.LINK_SPEED_UNITS;
            String ssid = info.getSSID();

            cSummary = String.format("Connected to %s at %s%s. " +
                            "Strength %s/5",
                    ssid, speed, units, strength);
            Log.d(TAG, cSummary);
            tinfo[wifiCode] += cSummary;
            return 0;
        } else {
            cSummary = String.format("wifi not connected");
            Log.d(TAG, "wifi not connected");
            tinfo[wifiCode] += cSummary;
            return 1;
        }
    }

    int getvalueofled(int index) {
        int ret = -1;
        byte len;
        byte strinfo[] = new byte[256];
        String info;
        try {
            ret = hwtestService.get_led(index, strinfo);
        } catch (RemoteException e) {
            Log.e("AdvFunTest", "Remote Exception.");
        }
        if (ret == 0) {
            len = strinfo[0];
            info = new String(strinfo, 1, len);
            ret = Integer.parseInt(info);
            return ret;
        }
        return -1;
    }

    byte test_led() {
        int led1, led2, led3;
        try {
            hwtestService.set_led(1, 0);
            hwtestService.set_led(2, 0);
            hwtestService.set_led(3, 0);
        } catch (RemoteException e) {
            Log.e("AdvFunTest", "Remote Exception.");
        }
        led1 = getvalueofled(1);
        led2 = getvalueofled(2);
        led3 = getvalueofled(3);
        if ((led1 != 0) || (led2 != 0) || (led3 != 0)) {
            return -1;
        }

        SystemClock.sleep(2000);

        try {
            hwtestService.set_led(1, 1);
            hwtestService.set_led(2, 1);
            hwtestService.set_led(3, 1);
        } catch (RemoteException e) {
            Log.e("AdvFunTest", "Remote Exception.");
        }
        led1 = getvalueofled(1);
        led2 = getvalueofled(2);
        led3 = getvalueofled(3);
        if ((led1 != 1) || (led2 != 1) || (led3 != 1)) {
            return -1;
        }
        return 0;
    }

    void startTest(int TestItem) {
        byte ret = 0;
        Message msg = new Message();
        Bundle bundle = new Bundle();
        byte strinfo[] = new byte[256];
        switch (TestItem) {
            case cpuCode:
                //
                try {
                    int funcid = 1;
                    ret = hwtestService.get_cpu(funcid, strinfo);
                    byte len = strinfo[0];
                    //String info = new String(strinfo, "UTF8");
                    String info = new String(strinfo, 1, len);
                    tinfo[TestItem] += "cpu loading: ";
                    tinfo[TestItem] += info;
                    tinfo[TestItem] += "%\n";
                    ret += hwtestService.get_temperature(strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    tinfo[TestItem] += "cpu temperature: ";
                    tinfo[TestItem] += info;
                    tinfo[TestItem] += "Degress\n";
                    bundle.putString("rtinfo", tinfo[TestItem]);
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;
            case powerCode:
                try {
                    byte len;
                    ret = hwtestService.get_vcc(0, strinfo);
                    len = strinfo[0];
                    String info = new String(strinfo, 1, len);
                    tinfo[TestItem] += "VCC: ";
                    tinfo[TestItem] += info;
                    tinfo[TestItem] += "mV\n";

                    ret = hwtestService.get_vcc(1, strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    tinfo[TestItem] += "V1: ";
                    tinfo[TestItem] += info;
                    tinfo[TestItem] += "mV\n";

                    ret = hwtestService.get_vcc(2, strinfo);
                    len = strinfo[0];
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    tinfo[TestItem] += "V2: ";
                    tinfo[TestItem] += info;
                    tinfo[TestItem] += "mV\n";

                    ret = hwtestService.get_vcc(3, strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    tinfo[TestItem] += "V3: ";
                    tinfo[TestItem] += info;
                    tinfo[TestItem] += "mV\n";
                    bundle.putString("rtinfo", tinfo[TestItem]);
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;
            case audioCode:
                try {
                    byte len;
                    String info;
                    ret = hwtestService.test_ntsc(strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    tinfo[TestItem] += "video id: ";
                    tinfo[TestItem] += info;
                    tinfo[TestItem] += "\n";
                    Log.e(TAG, "ntsc test info : " + info);

                    ret = hwtestService.get_ntsc_status(strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    tinfo[TestItem] += "ntsc status: ";
                    tinfo[TestItem] += info;
                    tinfo[TestItem] += "\n";
                    Log.e(TAG, "ntsc status : " + info);
                    ret = hwtestService.test_audio_signal(strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    Log.e(TAG, "audio debug 1 : " + info);
                    ret += hwtestService.test_audio_headset(strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    Log.e(TAG, "audio debug 2 : " + info);

                    ret += hwtestService.test_audio_chip(strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    Log.e(TAG, "audio debug 3 : " + info);

                    tinfo[TestItem] += "audio id: ";
                    tinfo[TestItem] += info;

                    bundle.putString("rtinfo", tinfo[TestItem]);
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;
            case memoryCode:
                try {
                    int funcid = 1;
                    String allinfo = "All:";
                    ret = hwtestService.get_memory(funcid, strinfo);
                    byte len = strinfo[0];
                    //String info = new String(strinfo, "UTF8");
                    String allmem = new String(strinfo, 1, len);
                    allinfo += allmem;
                    allinfo += "kb;\nUsed:";

                    //
                    funcid = 2;
                    ret = hwtestService.get_memory(funcid, strinfo);
                    len = strinfo[0];
                    String used = new String(strinfo, 1, len);
                    allinfo += used;
                    allinfo += "kb;\nfree:";

                    //
                    funcid = 3;
                    ret = hwtestService.get_memory(funcid, strinfo);
                    len = strinfo[0];
                    String free = new String(strinfo, 1, len);
                    allinfo += free;
                    allinfo += "kb;";


                    bundle.putString("rtinfo", allinfo);
                    tinfo[TestItem] += allinfo;
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;
            case diskCode:
			/*
			Intent intent = new Intent(AdvFunTest.this, LoopbackActivity.class);
			startActivityForResult(intent, uartCode);
			*/
                try {

                    //for  sd card
                    int funcid = 1;
                    byte len;
                    String allinfo = "All:";
                    ret = hwtestService.get_disk_sdcard(funcid, strinfo);
                    len = strinfo[0];
                    //String info = new String(strinfo, "UTF8");
                    String allmem = new String(strinfo, 1, len);
                    allinfo += allmem;
                    allinfo += "kb;\nUsed:";

                    funcid = 2;
                    ret = hwtestService.get_disk_sdcard(funcid, strinfo);
                    len = strinfo[0];
                    String used = new String(strinfo, 1, len);
                    allinfo += used;
                    allinfo += "kb;\nfree:";

                    funcid = 3;
                    ret = hwtestService.get_disk_sdcard(funcid, strinfo);
                    len = strinfo[0];
                    String free = new String(strinfo, 1, len);
                    allinfo += free;
                    allinfo += "kb;";

                    bundle.putString("rtinfo", allinfo);
                    tinfo[TestItem] += "[sdcard]\n";
                    tinfo[TestItem] += allinfo;
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;

            case ssdCode:
			/*
			Intent intent = new Intent(AdvFunTest.this, LoopbackActivity.class);
			startActivityForResult(intent, uartCode);
			*/
                try {

                    //for  sd card
                    int funcid = 1;
                    byte len;
                    String allinfo = "All:";
                    ret = hwtestService.get_disk_ssd(funcid, strinfo);
                    len = strinfo[0];
                    //String info = new String(strinfo, "UTF8");
                    String allmem = new String(strinfo, 1, len);
                    allinfo += allmem;
                    allinfo += "kb;\nUsed:";

                    //
                    funcid = 2;
                    ret = hwtestService.get_disk_ssd(funcid, strinfo);
                    len = strinfo[0];
                    String used = new String(strinfo, 1, len);
                    allinfo += used;
                    allinfo += "kb;\nfree:";

                    //
                    funcid = 3;
                    ret = hwtestService.get_disk_ssd(funcid, strinfo);
                    len = strinfo[0];
                    String free = new String(strinfo, 1, len);
                    allinfo += free;
                    allinfo += "kb;";
                    bundle.putString("rtinfo", allinfo);
                    tinfo[TestItem] += "[ssd]\n";
                    tinfo[TestItem] += allinfo;
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;

            case udiskCode:
			/*
			Intent intent = new Intent(AdvFunTest.this, LoopbackActivity.class);
			startActivityForResult(intent, uartCode);
			*/
                try {

                    //for  sd card
                    int funcid = 1;
                    byte len;
                    String allinfo = "All:";
                    ret = hwtestService.get_disk_usb(funcid, strinfo);
                    len = strinfo[0];
                    //String info = new String(strinfo, "UTF8");
                    String allmem = new String(strinfo, 1, len);
                    allinfo += allmem;
                    allinfo += "kb;\nUsed:";
                    //
                    funcid = 2;
                    ret = hwtestService.get_disk_usb(funcid, strinfo);
                    len = strinfo[0];
                    String used = new String(strinfo, 1, len);
                    allinfo += used;
                    allinfo += "kb;\nfree:";
                    //
                    funcid = 3;
                    ret = hwtestService.get_disk_usb(funcid, strinfo);
                    len = strinfo[0];
                    String free = new String(strinfo, 1, len);
                    allinfo += free;
                    allinfo += "kb;";


                    bundle.putString("rtinfo", allinfo);
                    tinfo[TestItem] += "[udisk]\n";
                    tinfo[TestItem] += allinfo;
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;

            case wifiCode:
                //ret = wifi_test();
                //bundle.putString("rtinfo", tinfo[TestItem]);
                try {
                    byte len;
                    String info;
                    ret = hwtestService.test_wifi(strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    Log.e(TAG, "wifi debug 1 : " + info + "ret = " + ret);
                    ret += hwtestService.test_bluetooth(strinfo);
                    Log.e(TAG, "bluetooth debug 1 : " + info + "ret = " + hwtestService.test_bluetooth(strinfo));
                    tinfo[TestItem] += "none\n";
                    bundle.putString("rtinfo", tinfo[TestItem]);
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                //bundle.putString("rtinfo", "rfid");
                break;
            case rfidCode:
                try {
                    int funcid = 1;
                    byte len;
                    String info;
                    ret = hwtestService.test_rfid(funcid, strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    Log.e(TAG, "rfid debug 1 : " + info);
                    tinfo[TestItem] += "none\n";

                    bundle.putString("rtinfo", tinfo[TestItem]);
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                //bundle.putString("rtinfo", "rfid");
                break;
            case ethernetCode:

                try {
                    //connected
                    int funcid = 1;
                    String allinfo = "eth0:";
                    ret = hwtestService.get_ethernet(funcid, strinfo);
                    byte len = strinfo[0];
                    //String info = new String(strinfo, "UTF8");
                    String bcon = new String(strinfo, 1, len);
                    if (bcon.equals("1"))
                        allinfo += "connected;\nspeed:";
                    else
                        allinfo += "not connected;\nspeed:";

                    funcid = 2;
                    ret = hwtestService.get_ethernet(funcid, strinfo);
                    len = strinfo[0];
                    String speed = new String(strinfo, 1, len);
                    allinfo += speed;
                    allinfo += ";\nduplex:";
                    funcid = 3;
                    ret = hwtestService.get_ethernet(funcid, strinfo);
                    len = strinfo[0];
                    String duplex = new String(strinfo, 1, len);
                    allinfo += duplex;
                    allinfo += ";\nip:";

                    funcid = 4;
                    ret = hwtestService.get_ethernet(funcid, strinfo);
                    len = strinfo[0];
                    String ip = new String(strinfo, 1, len);
                    allinfo += ip;
                    allinfo += ";";

                    bundle.putString("rtinfo", allinfo);
                    tinfo[TestItem] += allinfo;
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;
            case usbCode:
                try {
                    int funcid = 1;
                    ret = hwtestService.test_usb(funcid, strinfo);
                    byte len = strinfo[0];
                    String info = new String(strinfo, 1, len);
                    tinfo[TestItem] += info;

                    bundle.putString("rtinfo", tinfo[TestItem]);
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;
            case sdcardCode:
                try {
                    int funcid = 1;
                    ret = hwtestService.test_sdcard(funcid, strinfo);
                    byte len = strinfo[0];
                    //String info = new String(strinfo, "UTF8");
                    String info = new String(strinfo, 1, len);
                    tinfo[TestItem] += info;
                    bundle.putString("rtinfo", tinfo[TestItem]);
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;
            case lcdCode:
                try {
                    byte len;
                    String info;
                    ret = hwtestService.test_lcd(strinfo);
                    len = strinfo[0];
                    info = new String(strinfo, 1, len);
                    Log.e(TAG, "lcd debug 1 : " + info);
                    tinfo[TestItem] += "none\n";

                    bundle.putString("rtinfo", tinfo[TestItem]);
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;
            case ledCode:
                try {
                    int saveled1, saveled2, saveled3;
                    //enter
                    saveled1 = getvalueofled(1);
                    saveled2 = getvalueofled(2);
                    saveled3 = getvalueofled(3);

                    //test led
                    ret = test_led();

                    //exit
                    hwtestService.set_led(1, saveled1);
                    hwtestService.set_led(2, saveled2);
                    hwtestService.set_led(3, saveled3);
                    if (ret == 0)
                        tinfo[TestItem] += "led test ok\n";
                    else
                        tinfo[TestItem] += "led test failed\n";

                    bundle.putString("rtinfo", tinfo[TestItem]);
                } catch (RemoteException e) {
                    Log.e("AdvFunTest", "Remote Exception.");
                }
                break;
            default:
                bundle.putString("rtinfo", "error code");
                break;
        }
        bundle.putInt("result", ret);
        msg.setData(bundle);// mes利用Bundle传递数据
        handler.sendMessage(msg);
    }
}
