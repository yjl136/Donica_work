package cn.donica.slcd.launcher.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-04-01 17:13
 * Describe: 点view
 */

public class DotView extends View {
    private final static String TAG = "DotView";
    private Paint mPaint;
    private int radius;

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int measuredHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int min = Math.min(measuredHeight, measuredWidth);
        setMeasuredDimension(min, min);
        radius = getMeasuredHeight() / 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG, "w:" + w + "  h:" + h + "  oldw:" + oldw + " oldh:" + oldh);
        // radius = w/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "measurewidth:" + getMeasuredWidth() + "  measureheight:" + getMeasuredHeight() + "  Radius:" + radius);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, mPaint);
    }
}
