package cn.donica.slcd.launcher.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;

import cn.donica.slcd.launcher.MainActivity;
import cn.donica.slcd.launcher.R;

/**
 * Created by liangmingjie on 2016/6/29.
 */
public class MainAcitivityUnitTest extends ActivityUnitTestCase<MainActivity> {
    MainActivity mainActivity;
    TextView tv;

    public MainAcitivityUnitTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent mIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mIntent, null, null);
        mainActivity = getActivity();
    }

    public void testSeatTextView() {
        tv = (TextView) tv.findViewById(R.id.seatTv);
        tv.setText("A10");
        assertNotNull(tv);
    }
}
