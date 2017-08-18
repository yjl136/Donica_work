package cn.donica.slcd.launcher.ui;

import android.widget.LinearLayout;

/**
 * Created by liangmingjie on 2016/4/7.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.donica.slcd.launcher.R;
import cn.donica.slcd.launcher.ui.ScrollLayout.OnScreenChangeListener;


public class PageControlView extends LinearLayout {
    private Context context;
    private int count;

    public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {
        this.count = scrollViewGroup.getChildCount();
        generatePageControl(scrollViewGroup.getCurrentScreenIndex());
        scrollViewGroup.setOnScreenChangeListener(new OnScreenChangeListener() {
            public void onScreenChange(int currentIndex) {
                generatePageControl(currentIndex);
            }
        });
    }

    public PageControlView(Context context) {
        super(context);
        this.init(context);
    }

    public PageControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    /**
     * @param currentIndex 当前page index
     */
    private void generatePageControl(int currentIndex) {
        removeAllViews();
        //总的页数

        int totalPage = this.count;
        if (totalPage == 1) {
            return;
        }
        //当前页
        int currentPage = currentIndex + 1;
        for (int i = 1; i <= totalPage; i++) {
            ImageView iv = new ImageView(context);
            if (currentPage == i) {
                iv.setBackgroundResource(R.drawable.lin_hig);
            } else {
                iv.setBackgroundResource(R.drawable.line_an);
            }
            addView(iv);
        }
    }
}

