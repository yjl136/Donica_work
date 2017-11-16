package cn.donica.slcd.settings.restore;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import java.io.IOException;

import cn.donica.slcd.settings.R;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-10-13 16:03
 * Describe:
 */

public class RestoreFactoryDialog extends DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.restore_factory, container);
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
                restoreFactory();
                break;
        }
    }

    private void restoreFactory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RecoverySystem.rebootWipeUserData(getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
