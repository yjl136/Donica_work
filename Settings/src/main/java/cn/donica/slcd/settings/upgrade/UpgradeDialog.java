package cn.donica.slcd.settings.upgrade;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import cn.donica.slcd.settings.R;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-10-13 16:03
 * Describe:
 */

public class UpgradeDialog extends DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.system_upgrade, container);
        Button ok = (Button) view.findViewById(R.id.okBt);
        Button cancle = (Button) view.findViewById(R.id.cancelBt);
        ok.setOnClickListener(this);
        cancle.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelBt:
                dismiss();
                break;
            case R.id.okBt:
                upgrade();
                break;
        }
    }

    private void upgrade() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.fsl.android.ota", "com.fsl.android.ota.OtaAppActivity"));
        startActivity(intent);
        dismiss();
    }
}
