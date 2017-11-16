package cn.donica.slcd.ble.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.donica.slcd.ble.R;
import cn.donica.slcd.ble.utils.DLog;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-10-31 09:44
 * Describe:
 */
public class FloatView extends RelativeLayout {
    private TextView stateTv;

    public FloatView(Context context) {
        this(context, null);
        DLog.info("FloatView1");

    }

    public FloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        DLog.info("FloatView2");
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DLog.info("FloatView3");
        LayoutInflater.from(context).inflate(R.layout.float_view, this);
        stateTv = (TextView) findViewById(R.id.stateTv);

    }

    public void setState(String state) {
        if (stateTv != null) {
            stateTv.setText(state);
        }
    }


}
