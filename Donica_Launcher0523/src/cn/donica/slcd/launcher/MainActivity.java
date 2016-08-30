package cn.donica.slcd.launcher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.donica.slcd.launcher.adapter.AppAdapters;
import cn.donica.slcd.launcher.db.SqliteHelperUtil;
import cn.donica.slcd.launcher.model.AppsModel;
import cn.donica.slcd.launcher.ui.PageControlView;
import cn.donica.slcd.launcher.ui.ScrollLayout;
import cn.donica.slcd.launcher.util.ActivityCollectorUtil;
import cn.donica.slcd.launcher.util.LogUtil;

public class MainActivity extends Activity {

    private static final int APP_PAGE_SIZE = 10;
    private List<ResolveInfo> mApps;
    List<ResolveInfo> mAppList;
    private List<AppsModel> appsModelList;
    private AppsModel appsModel;
    private SqliteHelperUtil dbHelper;

    private PageControlView pageControl;
    private DataLoading dataLoad;

    private TextView seat_num;
    public Context mContext;

    private static final String TAG = "MainActivity";

    private GridView gridView;
    String packageName;
    String activityName;

    private ScrollLayout mScrollLayout;
    private LinearLayout linearLayout;
    private Bitmap bitmap;

    /**
     * Called when the activity is first created.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollectorUtil.addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayoutTest);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        seat_num = (TextView) findViewById(R.id.seat_num);
        seat_num.setText(Config.getSeatPosition());
        mContext = getBaseContext();
        dataLoad = new DataLoading();
        loadWallpaper();
        dbHelper = new SqliteHelperUtil(this);
        if (dbHelper.getAppsSize() == 0) {
            loadApp();
        } else {
            loadDbApp();
        }
        initGridView();
    }

    /**
     * 根据指定路径加载壁纸
     */
    void loadWallpaper() {
        boolean pngFile = BaseApplication.fileIsExists(Config.pngPath);
        if (pngFile) {
            Drawable pngDrawable = loadSDCardPic(Config.pngPath);
            linearLayout.setBackground(pngDrawable);
        } else {
            boolean jpgFile = BaseApplication.fileIsExists(Config.jpgPath);
            if (jpgFile) {
                Drawable jpgDrawable = loadSDCardPic(Config.jpgPath);
                linearLayout.setBackground(jpgDrawable);
            } else {
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.wallpager1);
                linearLayout.setBackground(drawable);
            }
        }
    }

    /**
     * 把图片路径转换为Drawable对象
     *
     * @param path
     * @return
     */
    Drawable loadSDCardPic(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; //把图片长宽压缩为原来的1/2
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        Drawable drawable = new BitmapDrawable(bm);
        return drawable;
    }

    /**
     * gridView 的onItemLick响应事件
     */
    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            appsModel = (AppsModel) parent.getItemAtPosition(position);
            Intent mainIntent = mContext.getPackageManager()
                    .getLaunchIntentForPackage(appsModel.getPackage_Name());
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                // launcher the package
                mContext.startActivity(mainIntent);
            } catch (ActivityNotFoundException noFound) {
                Toast.makeText(mContext, "Package not found!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 加载应用程序
     */

    void loadApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // getPackageManager()获得已安装的应用程序信息
        // queryIntentActivities()返回给定条件的所有ResolveInfo对象(本质上是Activity)，集合对象
        mApps = getPackageManager().queryIntentActivities(intent, 0);
        mAppList = new ArrayList<ResolveInfo>();
        // numActivities为真实应用总数
        int numActivities = mApps.size();

        // 遍历设置为列表
        for (int i = 0; i < numActivities; i++) {
            ResolveInfo reinfo = mApps.get(i);
            // 通过程序入口Activity过滤程序
            // if
            // (reinfo.activityInfo.name.equals("com.android.browser.BrowserActivity"))
            // {
            // continue;
            // }
            // //通过包名过滤程序
            // if (reinfo.activityInfo.packageName.equals("com.android.music"))
            // {
            // continue;
            // }
            mAppList.add(reinfo);
        }
        checkAppIsRight(mAppList);
        appsModelList = new ArrayList<AppsModel>();
        for (ResolveInfo info : mAppList) {
            if (!info.activityInfo.packageName.equals(getPackageName())) {
                if (!dbHelper.isExistsByPackageName(info.activityInfo.name)) {
                    AppsModel model = new AppsModel();
                    // 使用Sqlite配置icon
//                    model.setActivity_Name(activityName);
//                    model.setPackage_Name(packageName);
                    // 使用系统全部图标
                    model.setActivity_Name(info.activityInfo.name);
                    // LogUtil.d(TAG, "loadApp()...activityInfo.name=" + info.activityInfo.name);
                    model.setPackage_Name(info.activityInfo.packageName);
                    //  LogUtil.d(TAG, "loadApp()...activityInfo.packageName=" + info.activityInfo.packageName);
                    dbHelper.insertAppInfo(model);
                }
            }
        }
        loadDbData(mAppList);
    }

    /**
     *
     */
    void loadDbApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // getPackageManager()获得已安装的应用程序信息
        // queryIntentActivities()返回给定条件的所有ResolveInfo对象(本质上是Activity)，集合对象
        mApps = getPackageManager().queryIntentActivities(intent, 0);
        mAppList = new ArrayList<ResolveInfo>();

        // numActivities为真实应用总数
        int numActivities = mApps.size();

        // 遍历设置为列表
        for (int i = 0; i < numActivities; i++) {
            ResolveInfo reinfo = mApps.get(i);
            // 通过程序入口Activity过滤程序
            // if
            // (reinfo.activityInfo.name.equals("com.android.browser.BrowserActivity"))
            // {
            // continue;
            // }
            // //通过包名过滤程序
            // if (reinfo.activityInfo.packageName.equals("com.android.music"))
            // {
            // continue;
            // }
            mAppList.add(reinfo);
        }
        checkAppIsRight(mAppList);
        appsModelList = new ArrayList<AppsModel>();
        for (ResolveInfo info : mAppList) {
            if (!info.activityInfo.packageName.equals(getPackageName())) {
                if (!dbHelper.isExistsByPackageName(info.activityInfo.name)) {
                    AppsModel model = new AppsModel();
                    // 使用Sqlite配置icon
                    model.setActivity_Name(activityName);
                    model.setPackage_Name(packageName);
                    // 使用系统全部图标
//                    model.setActivity_Name(info.activityInfo.name);
//                    model.setPackage_Name(info.activityInfo.packageName);
//                    dbHelper.insertAppInfo(model);
                }
            }
        }
        loadDbData(mAppList);
    }

    /**
     * 加载数据库数据
     *
     * @param info
     */
    private void loadDbData(final List<ResolveInfo> info) {
        AppsModel appsModel;
        IconUtil.init(mContext);
        Cursor allDataCursor = dbHelper.queryAllDataBySqlStrBySort();
        String mainActivityName;
        String picSign;
        String picName;
        int count = allDataCursor.getCount();
        for (int i = 0; i < count; i++) {
            allDataCursor.moveToPosition(i);
            //取出数据库中的全部activityName
            mainActivityName = allDataCursor.getString(1);
            picSign = allDataCursor.getString(4);
            picName = allDataCursor.getColumnName(6);
            for (ResolveInfo resolver : info) {
                if (resolver.activityInfo.name.equals(mainActivityName) && picSign.equals("0")) {
                    appsModel = new AppsModel();
                    // 设置程序图标
                    bitmap = Utilities.drawable2Bitmap(resolver.loadIcon(getPackageManager()));
                    appsModel.setActivity_Icon(IconUtil.getBitmapWithNoScale(mContext.getResources().getDrawable(R.drawable.background), bitmap));
                    // 设置程序主入口activity名称
                    appsModel.setActivity_Name(resolver.activityInfo.name);
                    // 设置程序包名
                    appsModel.setPackage_Name(resolver.activityInfo.packageName);
                    // 设置应用名
                    appsModel.setApp_name(resolver.activityInfo.loadLabel(getPackageManager()));
                    appsModelList.add(appsModel);
                } else if (resolver.activityInfo.name.equals(mainActivityName) && picSign.equals("1")) {
                    appsModel = new AppsModel();
                    // 设置程序图标
                    appsModel.setActivity_Icon(IconUtil.getBitmapFromExternalStorage(picName));
                    // 设置程序主入口activity名称
                    appsModel.setActivity_Name(resolver.activityInfo.name);
                    // 设置程序包名
                    appsModel.setPackage_Name(resolver.activityInfo.packageName);
                    // 设置应用名
                    appsModel.setApp_name(resolver.activityInfo.loadLabel(getPackageManager()));
                    appsModelList.add(appsModel);
                }
            }
        }
        allDataCursor.close();
    }

    /**
     * 验证系统应用程序是否数量小于数据库保存的数量，如果小于则删除不符合的数据
     *
     * @param info
     */
    private void checkAppIsRight(List<ResolveInfo> info) {
        LogUtil.d(TAG,
                "checkAppIsRight方法执行,验证系统应用程序是否数量小于数据库保存的数量，如果小于则删除不符合的数据");
        int i = 0;
        if (dbHelper.getAllName() != null) {
            String[] packageNames = dbHelper.getAllName();
            LogUtil.d(TAG, "checkAppIsRight()...packNames=" + packageNames);
            if (info.size() < dbHelper.getAppsCount()) {
                for (String packageName : packageNames) {
                    for (ResolveInfo resolver : info) {
                        if (packageName.equals(resolver.activityInfo.packageName)) {
                            i++;
                        }
                    }
                    if (i == 0) {
                        dbHelper.deleteAppByPackageName(packageName);
                    }
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollectorUtil.removeActivity(this);
    }

    /**
     * 初始化GridView
     */
    public void initGridView() {
//        final PackageManager packageManager = getPackageManager();
//        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
//        int pageNo = (int) Math.ceil(apps.size() / APP_PAGE_SIZE);
        SqliteHelperUtil sqliteHelperUtil = new SqliteHelperUtil(BaseApplication.getContext());
        int pageNo = sqliteHelperUtil.getAppsSize() / APP_PAGE_SIZE + 1;
        LogUtil.d(TAG, "pageNo:" + pageNo);
        for (int i = 0; i < pageNo; i++) {
            gridView = new GridView(mContext);
            gridView.setAdapter(new AppAdapters(this, appsModelList, i));
            gridView.setNumColumns(5);
            gridView.setHorizontalSpacing(100);
            gridView.setVerticalSpacing(100);
            gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            gridView.setOnItemClickListener(listener);
            mScrollLayout.addView(gridView);
        }
        //加载分页
        pageControl = (PageControlView) findViewById(R.id.pageControl);
        pageControl.bindScrollViewGroup(mScrollLayout);
        //加载分页数据
        dataLoad.bindScrollViewGroup(mScrollLayout);
    }

    /**
     * 分页数据
     */
    public class DataLoading {
        private int count;

        public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {
            //分页总数
            this.count = scrollViewGroup.getChildCount();
            LogUtil.d(TAG, "分页总数：" + this.count);
            scrollViewGroup.setOnScreenChangeListenerDataLoad(new ScrollLayout.OnScreenChangeListenerDataLoad() {
                public void onScreenChange(int currentIndex) {
                    // TODO Auto-generated method stub
                    //滑动监听，监听滑动到第几页
                    int pageNo = currentIndex + 1;
//                    LogUtil.d(TAG, "滑动动到第 " + pageNo + "页");
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
