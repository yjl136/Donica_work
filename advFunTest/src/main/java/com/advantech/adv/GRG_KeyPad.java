package com.advantech.adv;

import android.os.Handler;
import android.app.Activity;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GRG_KeyPad {

    public class Key {
        public int keyCode;
        public boolean isPass;
        public String keyMsg;

        Key(int keyCode, boolean isPass, String msg) {
            this.keyCode = keyCode;
            this.isPass = isPass;
            this.keyMsg = msg;
        }

        Key() {
        }
    }

    public Handler mainhandler;
    private Device dev;
    private LinearLayout backBtn;
    private int keyCount;
    private Key[] key;
    int[] keyId;
    public ExecCmd execcmd;

    GRG_KeyPad() {

        dev = new Device();
        dev.keyFun = false;
        dev.keyMsg = new String("");

        //这里添加：读取jSon文件关于Udisk的配置。Udisk的数量及挂载点
        keyCount = 6;
        key = new Key[keyCount];
        keyId = new int[]{220, 221, 222, 223, 224, 225};

        execcmd = new ExecCmd();

        for (int i = 0; i < keyCount; i++) {
            key[i] = new Key(keyId[i], false, "");
        }
    }

    public void onKeyDown(int keyCode, Activity activity, TextView keyFun) {
        // TODO Auto-generated method stub
    /*	int[] kCode ={key[0].keyCode, key[1].keyCode, key[2].keyCode, key[3].keyCode, key[4].keyCode, key[5].keyCode};
        for(int i = 0; i < keyCount; i++){
			kCode[i] = key[i].keyCode;
		}*/

        //此处keyCode_* 只能定义为常量，再想办法
        int[] kCode = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < keyCount; i++) {
            //kCode[i] = key[i].keyCode;
            kCode[i] = i;
        }
        final int keyCode_0 = 220;
        final int keyCode_1 = 221;
        final int keyCode_2 = 222;
        final int keyCode_3 = 223;
        final int keyCode_4 = 224;
        final int keyCode_5 = 225;
        final int keyCode_6 = 35;
        final int keyCode_7 = 36;
        final int keyCode_8 = 37;
        final int keyCode_9 = 38;


        switch (keyCode) {
            case keyCode_0:
                Toast.makeText(activity, "Press KEYCODE_GRGBANKING_CTRL detected", Toast.LENGTH_SHORT).show();
                execcmd.writeLog("KEYCODE_GRGBANKING_CTRL");
                execcmd.writeLog("====================================\n");
                for (int i = 0; i < keyCount; i++) {
                    if (keyCode_0 == key[i].keyCode)
                        key[i].isPass = true;
                }
                break;
            case keyCode_1:
                Toast.makeText(activity, "Press KEYCODE_GRGBANKING_PRINT detected", Toast.LENGTH_SHORT).show();
                execcmd.writeLog("KEYCODE_GRGBANKING_PRINT detected");
                execcmd.writeLog("====================================\n");
                for (int i = 0; i < keyCount; i++) {
                    if (keyCode_1 == key[i].keyCode)
                        key[i].isPass = true;
                }
                break;
            case keyCode_2:
                Toast.makeText(activity, "Press KEYCODE_GRGBANKING_MODE detected", Toast.LENGTH_SHORT).show();
                execcmd.writeLog("KEYCODE_GRGBANKING_MODE detected");
                execcmd.writeLog("====================================\n");
                for (int i = 0; i < keyCount; i++) {
                    if (keyCode_2 == key[i].keyCode)
                        key[i].isPass = true;
                }
                break;
            case keyCode_3:
                Toast.makeText(activity, "Press KEYCODE_GRGBANKING_SET detected", Toast.LENGTH_SHORT).show();
                execcmd.writeLog("KEYCODE_GRGBANKING_SET detected");
                execcmd.writeLog("====================================\n");
                for (int i = 0; i < keyCount; i++) {
                    if (keyCode_3 == key[i].keyCode)
                        key[i].isPass = true;
                }
                break;
            case keyCode_4:
                Toast.makeText(activity, "Press KEYCODE_GRGBANKING_CLEAR detected", Toast.LENGTH_SHORT).show();
                execcmd.writeLog("KEYCODE_GRGBANKING_CLEAR detected");
                execcmd.writeLog("====================================\n");
                for (int i = 0; i < keyCount; i++) {
                    if (keyCode_4 == key[i].keyCode)
                        key[i].isPass = true;
                }
                break;
            case keyCode_5:
                Toast.makeText(activity, "Press KEYCODE_GRGBANKING_ACC", Toast.LENGTH_SHORT).show();
                execcmd.writeLog("KEYCODE_GRGBANKING_ACC detected");
                execcmd.writeLog("====================================\n");
                for (int i = 0; i < keyCount; i++) {
                    if (keyCode_5 == key[i].keyCode)
                        key[i].isPass = true;
                }
                break;

        }

        dev.keyFun = true;
        for (int i = 0; i < keyCount; i++) {
            if (key[i].isPass == false) {
                dev.keyFun = false;
                dev.keyMsg = dev.keyMsg + "Key:" + String.valueOf(key[i].keyCode) + "Test Failed\n";
            }
        }
        if (dev.keyFun == true) {
            keyFun.setText("PASS");
            keyFun.setTextColor(Color.GREEN);
        }
    }

}
