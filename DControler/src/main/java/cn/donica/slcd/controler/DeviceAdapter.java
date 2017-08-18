package cn.donica.slcd.controler;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-08-11 18:04
 * Describe:
 */

public class DeviceAdapter extends BaseAdapter {
    private List<Device> devices;
    private Context context;
    private LayoutInflater inflater;

    public DeviceAdapter(Context context, List<Device> devices) {
        this.devices = devices;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_device, parent, false);
            ViewHoler vh = new ViewHoler();
            vh.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
            vh.macTv = (TextView) convertView.findViewById(R.id.macTv);
            convertView.setTag(vh);
        }
        ViewHoler vh = (ViewHoler) convertView.getTag();
        Device device = devices.get(position);
        vh.macTv.setText(device.getMac());
        String name = device.getName();
        if (TextUtils.isEmpty(name)) {
            name = "unkonw device";
        }
        vh.nameTv.setText(name);
        return convertView;
    }

    static class ViewHoler {
        public TextView nameTv;
        public TextView macTv;
    }
}
