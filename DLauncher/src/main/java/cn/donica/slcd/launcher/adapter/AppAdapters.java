package cn.donica.slcd.launcher.adapter;

/**
 * Created by liangmingjie on 2016/4/7.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.donica.slcd.launcher.R;
import cn.donica.slcd.launcher.model.AppsModel;
import cn.donica.slcd.launcher.util.LogUtil;

public class AppAdapters extends BaseAdapter {
    private static final String TAG = "AppAdapters";
    private Context mContext;
    public static final int APP_PAGE_SIZE = 10;
    private PackageManager pm;
    private AppsModel appsModel;

    private List<AppsModel> appsModelList;


    public AppAdapters(Context context, List<AppsModel> info, int page) {
        this.mContext = context;
        this.appsModelList = info;
        pm = context.getPackageManager();
        appsModelList = new ArrayList<AppsModel>();
        int i = page * APP_PAGE_SIZE;
        int iEnd = i + APP_PAGE_SIZE;
        while ((i < info.size()) && (i < iEnd)) {
            appsModelList.add(info.get(i));
            i++;
        }
    }

    public int getCount() {
        // TODO Auto-generated method stub
        // return mList.size();
        LogUtil.d(TAG, "appsModelList.size()：" + appsModelList.size());
        return appsModelList.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return mList.get(position);
        return appsModelList.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        if (convertView == null) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.app_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mAppIcon = (ImageView) v.findViewById(R.id.ivAppIcon);
            viewHolder.mAppName = (TextView) v.findViewById(R.id.tvAppName);
            v.setTag(viewHolder);
            convertView = v;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        appsModel = appsModelList.get(position);
        viewHolder.mAppIcon.setImageBitmap(appsModel.getActivity_Icon());
        viewHolder.mAppName.setText(appsModel.getApp_name().toString());
        LogUtil.d(TAG, "AppName:" + appsModel.getApp_name().toString());
        return convertView;
    }

    /**
     * 每个应用显示的内容，包括图标和名称
     *
     * @author Yao.GUET
     */
    public class ViewHolder {
        ImageView mAppIcon;
        TextView mAppName;
    }
}