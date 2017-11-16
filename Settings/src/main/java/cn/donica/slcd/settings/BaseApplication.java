/**
 * @author LiangMingJie
 */
package cn.donica.slcd.settings;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.internal.app.LocalePicker;

import java.util.ArrayList;
import java.util.List;

import cn.donica.slcd.settings.ui.LockPatternView;

public class BaseApplication extends Application {
    private List<LockPatternView.Cell> choosePattern;
    private static Context context;
    public static final String LOCK = "lock";
    public static final String LOCK_KEY = "lock_key";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initDefaultPattern();
        LocalePicker lp;
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 设置默认手势密码
     */
    public void initDefaultPattern() {
        SharedPreferences preferences = getSharedPreferences(BaseApplication.LOCK,
                MODE_PRIVATE);
        String patternString = preferences.getString(BaseApplication.LOCK_KEY,
                null);
        if (patternString == null) {
            choosePattern = new ArrayList<LockPatternView.Cell>();
            //1为列序号，0为行序号
            choosePattern.add(LockPatternView.Cell.of(1, 0));
            choosePattern.add(LockPatternView.Cell.of(2, 0));
            choosePattern.add(LockPatternView.Cell.of(1, 1));
            choosePattern.add(LockPatternView.Cell.of(0, 2));
            preferences.edit().putString(BaseApplication.LOCK_KEY, LockPatternView.patternToString(choosePattern)).commit();
        }
    }
}
