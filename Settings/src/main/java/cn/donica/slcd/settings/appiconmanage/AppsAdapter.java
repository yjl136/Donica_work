package cn.donica.slcd.settings.appiconmanage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.donica.slcd.settings.R;


/**
 * Created by liangmingjie on 2016/6/7.
 */
public class AppsAdapter extends BaseAdapter {
    private String TAG = "AppAdapter";
    private AppsModel appsModel;

    private List<AppsModel> appsModelList;

    LayoutInflater inflater = null;
    Context mContext;
    private Handler mHandler;

    private boolean display;


    public AppsAdapter(List<AppsModel> info, Context context, Handler handler, boolean display) {
        this.appsModelList = info;
        mContext = context;
        inflater = LayoutInflater.from(context);
        mHandler = handler;
        this.display = display;
    }

    public void refresh(List<AppsModel> info) {
        this.appsModelList = info;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return appsModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return appsModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_cell, null);
            viewHolder = new ViewHolder();
            viewHolder.appName = (TextView) view.findViewById(R.id.list_item_title);
            viewHolder.appIcon = (ImageView) view.findViewById(R.id.list_item_icon);
            viewHolder.switchView = (SwitchView) view.findViewById(R.id.list_item_switch);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.appName.setText(appsModelList.get(position).getAppName());
        viewHolder.appIcon.setBackground(appsModelList.get(position).getAppIcon());
        final String packageName = appsModelList.get(position).getPackageName();
        if (display == true) {
            viewHolder.switchView.setChecked(true);
        } else {
            viewHolder.switchView.setChecked(false);
        }
        viewHolder.switchView.setOnChangeListener(new SwitchView.OnSwitchChangedListener() {
            @Override
            public void onSwitchChange(SwitchView switchView, boolean isChecked) {
                switchView.toggle();
                if (isChecked == true) {
                    sendMessage(position, 1, packageName);
                } else {
                    sendMessage(position, 0, packageName);
                }
            }
        });
        return view;
    }

    /**
     * 发送消息
     */
    private void sendMessage(int position, int display, String packageName) {
        // 创建Message并填充数据，通过mHandle联系Activity接收处理
        Message msg = new Message();
        msg.what = 1;
        msg.arg1 = position;
        msg.arg2 = display;
        Bundle b = new Bundle();
        b.putString("packageName", packageName);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    class ViewHolder {
        TextView appName;
        ImageView appIcon;
        SwitchView switchView;
    }
}
