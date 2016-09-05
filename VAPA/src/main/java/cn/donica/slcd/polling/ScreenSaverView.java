package cn.donica.slcd.polling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;


public class ScreenSaverView extends RelativeLayout {
    private Context mContext;
    private View rootView;


    public ScreenSaverView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.screen_cover, this);
    }
}
