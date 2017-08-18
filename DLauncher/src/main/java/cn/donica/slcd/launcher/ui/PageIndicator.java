package cn.donica.slcd.launcher.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-04-01 16:44
 * Describe:页指示器
 */

public class PageIndicator extends LinearLayout {
    //默认indicator之间的间隔为10dp
    private final static int DEFAULT_INDICATOR_GAP = 10;

    public PageIndicator(Context context) {
        this(context, null);
    }

    public PageIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicator(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
