package cn.donica.slcd.ble.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import cn.donica.slcd.ble.R;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-08 15:37
 * Describe:
 */

public class PaLayout extends RelativeLayout {
    public PaLayout(Context context) {
        this(context, null);
    }

    public PaLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.pa_view, this);
    }


}
