package cn.donica.slcd.settings.lang;

import android.app.ActivityManagerNative;
import android.app.DialogFragment;
import android.app.IActivityManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

import cn.donica.slcd.settings.R;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-10-09 15:17
 * Describe:
 */

public class LocaleDialog extends DialogFragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
    private RadioButton chineseRb, englishRb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.locale_layout, container);
        chineseRb = (RadioButton) view.findViewById(R.id.ChineseRb);
        englishRb = (RadioButton) view.findViewById(R.id.englishRb);
        String lang = getResources().getConfiguration().locale.getLanguage();
        if ("zh".equals(lang)) {
            chineseRb.setChecked(true);
        } else {
            englishRb.setChecked(true);
        }
        mRadioGroup = (RadioGroup) view.findViewById(R.id.localeGroup);
        mRadioGroup.setOnCheckedChangeListener(this);
        return view;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        Locale locale = null;
        switch (checkedId) {
            case R.id.ChineseRb:
            case R.id.taiwanRb:
                locale = Locale.CHINA;
                break;
            case R.id.englishRb:
                locale = Locale.ENGLISH;
                break;
        }
        setLocale(locale);
        dismiss();
    }

    // locale = Locale.CHINA 或者 Locale.ENGLISH;
    private void setLocale(Locale locale) {
        try {
            IActivityManager am = ActivityManagerNative.getDefault();
            Configuration config = am.getConfiguration();
            config.locale = locale;
            am.updateConfiguration(config);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
