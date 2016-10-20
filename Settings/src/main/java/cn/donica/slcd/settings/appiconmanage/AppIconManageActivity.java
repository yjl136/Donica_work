package cn.donica.slcd.settings.appiconmanage;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.donica.slcd.settings.R;

//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;

public class AppIconManageActivity extends Activity {
    private static final String TAG = "AppIconManageActivity";
    List<String> display = new ArrayList<String>();
    List<String> hidden = new ArrayList<String>();
    Context mContext;
    ListView app_list1;
    ListView app_list2;
    TextView show_display_app_more, show_hidden_app_more;
    RelativeLayout layout_display, layout_hidden;
    ImageView show_display_app_more_icon;
    ImageView show_hidden_app_more_icon;

    AppsAdapter displayAdapters;
    AppsAdapter hiddenAdapters;
    List<ResolveInfo> mApps1;
    List<ResolveInfo> mApps2;
    List<ResolveInfo> mAppList1;
    List<ResolveInfo> mAppList2;

    private List<AppsModel> appsModelList1;
    private List<AppsModel> appsModelList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_icon_manage);
        initToolbar();
        initView();
        mContext = getBaseContext();
        loadDisplayApp();
        loadHiddenApp();
        layout_display.setOnClickListener(new View.OnClickListener() {
            boolean isOpened = false;//改变展开状态

            @Override
            public void onClick(View v) {
                if (!isOpened) {
                    app_list1.setVisibility(View.VISIBLE);
                    show_display_app_more.setText(getString(R.string.fold));
                    show_display_app_more_icon.setBackgroundResource(R.mipmap.ico_bottom);
                    isOpened = true;
                } else {
                    app_list1.setVisibility(View.GONE);
                    show_display_app_more.setText(getString(R.string.unfold));
                    show_display_app_more_icon.setBackgroundResource(R.mipmap.ico_right);
                    isOpened = false;
                }
            }
        });
        layout_hidden.setOnClickListener(new View.OnClickListener() {
            boolean isOpened = false;//改变展开状态

            @Override
            public void onClick(View v) {
                if (!isOpened) {
                    app_list2.setVisibility(View.VISIBLE);
                    show_hidden_app_more.setText(getString(R.string.fold));
                    show_hidden_app_more_icon.setBackgroundResource(R.mipmap.ico_bottom);
                    isOpened = true;
                } else {
                    app_list2.setVisibility(View.GONE);
                    show_hidden_app_more.setText(getString(R.string.unfold));
                    show_hidden_app_more_icon.setBackgroundResource(R.mipmap.ico_right);
                    isOpened = false;
                }
            }
        });
        displayAdapters = new AppsAdapter(appsModelList1, mContext, mHandler, true);
        hiddenAdapters = new AppsAdapter(appsModelList2, mContext, mHandler, false);
        app_list1.setAdapter(displayAdapters);
        app_list2.setAdapter(hiddenAdapters);
    }

    private void initView() {
        app_list1 = (ListView) findViewById(R.id.app_list);
        app_list2 = (ListView) findViewById(R.id.app_list2);
        show_display_app_more = (TextView) findViewById(R.id.show_display_app_more);
        show_hidden_app_more = (TextView) findViewById(R.id.show_hidden_app_more);

        layout_display = (RelativeLayout) findViewById(R.id.layout_display);
        layout_hidden = (RelativeLayout) findViewById(R.id.layout_hidden);

        show_display_app_more_icon = (ImageView) findViewById(R.id.show_display_app_more_icon);
        show_hidden_app_more_icon = (ImageView) findViewById(R.id.show_hidden_app_more_icon);
        show_display_app_more_icon.setBackgroundResource(R.mipmap.ico_right);
        show_hidden_app_more_icon.setBackgroundResource(R.mipmap.ico_right);

    }

    private void initToolbar() {
        String title = "<h5>" + getString(R.string.launcher_icon_display) + "</h5>";
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.show();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    int display = msg.arg2;
                    String packageName = bundle.getString("packageName");
                    DataUtil.updateDisplay(packageName, display, mContext);
                    loadDisplayApp();
                    loadHiddenApp();
                    displayAdapters.refresh(appsModelList1);
                    hiddenAdapters.refresh(appsModelList2);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 加载应用程序
     */
    void loadDisplayApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // getPackageManager()获得已安装的应用程序信息
        // queryIntentActivities()返回给定条件的所有ResolveInfo对象(本质上是Activity)，集合对象
        mApps1 = getPackageManager().queryIntentActivities(intent, 0);
        mAppList1 = new ArrayList<ResolveInfo>();
        // numActivities为真实应用总数
        int numActivities = mApps1.size();
        // 遍历设置为列表
        for (int i = 0; i < numActivities; i++) {
            ResolveInfo reinfo = mApps1.get(i);
            mAppList1.add(reinfo);
        }
        loadDisplayData(mAppList1);
    }

    /**
     * 加载数据库数据
     *
     * @param info
     */
    private void loadDisplayData(final List<ResolveInfo> info) {
        AppsModel appsModel;
        appsModelList1 = new ArrayList<AppsModel>();
        ArrayList<String> displayAppList = DataUtil.getDisplayApp(mContext);
        String packageName;
        for (int i = 0; i < displayAppList.size(); i++) {
            packageName = displayAppList.get(i);
            for (ResolveInfo resolver : info) {
                if (resolver.activityInfo.packageName.equals(packageName)) {
                    appsModel = new AppsModel();
                    appsModel.setAppIcon(resolver.loadIcon(getPackageManager()));
                    // 设置程序包名
                    appsModel.setPackageName(resolver.activityInfo.packageName);
                    // 设置应用名
                    appsModel.setAppName(resolver.activityInfo.loadLabel(getPackageManager()));
                    appsModelList1.add(appsModel);
                }
            }
        }
        appsModelList1.size();
    }

    void loadHiddenApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // getPackageManager()获得已安装的应用程序信息
        // queryIntentActivities()返回给定条件的所有ResolveInfo对象(本质上是Activity)，集合对象
        mApps2 = getPackageManager().queryIntentActivities(intent, 0);
        mAppList2 = new ArrayList<ResolveInfo>();
        // numActivities为真实应用总数
        int numActivities = mApps2.size();
        // 遍历设置为列表
        for (int i = 0; i < numActivities; i++) {
            ResolveInfo reinfo = mApps2.get(i);
            mAppList2.add(reinfo);
        }
        loadHiddenAppData(mAppList2);
    }

    /**
     * 加载数据库数据
     *
     * @param info
     */
    private void loadHiddenAppData(final List<ResolveInfo> info) {
        AppsModel appsModel;
        appsModelList2 = new ArrayList<AppsModel>();
        ArrayList<String> hiddenAppList = DataUtil.getHiddenApp(mContext);
        String packageName;
        for (int i = 0; i < hiddenAppList.size(); i++) {
            packageName = hiddenAppList.get(i);
            for (ResolveInfo resolver : info) {
                if (resolver.activityInfo.packageName.equals(packageName)) {
                    appsModel = new AppsModel();
                    appsModel.setAppIcon(resolver.loadIcon(getPackageManager()));
                    // 设置程序包名
                    appsModel.setPackageName(resolver.activityInfo.packageName);
                    // 设置应用名
                    appsModel.setAppName(resolver.activityInfo.loadLabel(getPackageManager()));
                    appsModelList2.add(appsModel);
                }
            }
        }
        appsModelList1.size();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
