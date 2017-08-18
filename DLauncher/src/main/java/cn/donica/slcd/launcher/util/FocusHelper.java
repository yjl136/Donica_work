package cn.donica.slcd.launcher.util;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;

import cn.donica.slcd.launcher.ui.ScrollLayout;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-03-31 14:16
 * Describe:
 */
public class FocusHelper {
    /**
     * 处理按键抬起翻页
     *
     * @param view
     * @param keyCode
     * @param event
     * @return
     */
    public static boolean handleKeyUp(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            ScrollLayout v = (ScrollLayout) view;
            int index = v.getCurScreen();
            int next = 0;
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (index < v.getChildCount() - 1) {
                        next = index + 1;
                    } else {
                        next = index;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (index > 0) {
                        next = index - 1;
                    } else {
                        next = index;
                    }
                    break;
            }
            v.snapToScreen(next);
        }
        return true;
    }

    /**
     * 处理翻页
     *
     * @param view
     * @param event
     * @return
     */
    public static void handlePaging(View view, KeyEvent event) {
        ScrollLayout v = (ScrollLayout) view;
        GridView gv = (GridView) v.getChildAt(v.getCurScreen());
        int positon = gv.getSelectedItemPosition();
        int count = gv.getCount();
        int numColumns = gv.getNumColumns();
        Log.i("FocusHelper", "postion:" + positon + "  count:" + count + " numColumns:" + numColumns);
        if (event.getAction() == KeyEvent.ACTION_DOWN && positon != GridView.INVALID_POSITION) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                positon += 1;
                if ((positon == count || positon % numColumns == 0)) {
                    v.clearFocus();
                }
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (positon % numColumns == 0) {
                    v.clearFocus();
                }
            }
        }
    }
}
