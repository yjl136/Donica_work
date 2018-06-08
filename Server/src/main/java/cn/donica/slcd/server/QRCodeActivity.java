package cn.donica.slcd.server;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.encode.CodeCreator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.donica.slcd.server.tools.DisplayUtils;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2018-05-09 14:36
 * Describe:
 */

public class QRCodeActivity extends Activity {
    @BindView(R.id.qrcodeIv)
    public ImageView qrcodeIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        String mac = mBluetoothAdapter.getAddress();
        Bitmap qrCodeBitmap = null;
        try {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            qrCodeBitmap = CodeCreator.createQRCode(mac, DisplayUtils.dp2px(this, 300), DisplayUtils.dp2px(this, 300), logo);
            qrcodeIv.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        startService();
        // 注册订阅者
        EventBus.getDefault().register(this);
    }

    /**
     * 启动服务
     */
    private void startService() {
        Intent intent = new Intent(this, BluetoothServerService.class);
        startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String msg) {
        Toast.makeText(this, "连接成功" + msg, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
