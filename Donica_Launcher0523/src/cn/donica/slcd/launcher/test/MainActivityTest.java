package cn.donica.slcd.launcher.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import cn.donica.slcd.launcher.MainActivity;
import cn.donica.slcd.launcher.R;

/**
 * Created by liangmingjie on 2016/6/29.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity mainActivity;
    TextView tv;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = (MainActivity) getActivity();
    }

    public void testSeatTextView() {
        tv = (TextView) tv.findViewById(R.id.seat_num);
        tv.setText("A10");
        assertNotNull(tv);
    }
}
