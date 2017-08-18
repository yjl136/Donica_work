package cn.donica.slcd.launcher.db;

public class DbHelperColumn {

    public static final String TAG = "DbHelperColumn";
    /**
     * Sqlite数据库文件名词
     */
    public static final String DATABASENAME = "launcher.db";
    /**
     * Sqlite数据库表名
     */
    public static final String TABLE_NAME = "launcher";
    /**
     * Sqlite数据库版本号
     */
    public static final int DATABASE_VERSION = 1;
    /**
     * Sqlite数据库_id字段
     */
    public static final String APP_ID = "_id";
    /**
     * Sqlite数据库mainActivityName字段，为应用的主入口Activity名称
     */
    public static final String MAIN_ACTIVITY_NAME = "mainActivityName";
    /**
     * Sqlite数据库packageName字段，为应用的包名
     */
    public static final String PACKAGENAME = "packageName";
    /**
     * Sqlite数据库number字段，为应用的显示优先顺序，数字越小，就越排在前面显示，数字相同，则通过_id字段从小到大排序
     */
    public static final String NUMBER = "number";
    /**
     * Sqlite数据库picName字段,为图片的文件名，eg:settings.jpg。
     * 新增应用如果有图标，则为此应用适配图标
     * 图标放在指定的目录下，此目录的路径+图片名即为图片的绝对路径
     */
    public static final String PIC_NAME = "picName";
    /**
     * Sqlite中picSign字段，0为不需要额外适配图标，1为需要额外适配图标
     * 是否通过添加图片来适配图标
     */
    public static final String PIC_SIGN = "picSign";
    /**
     * Sqlite中adaptation字段，0为未适配图标，1为适配好图标
     * 是否已经默认适配图标
     */
    public static final String ADAPTATION = "adaptation";
    /**
     * Sqlite中display字段，0为隐藏，1为显示
     * 设置显示或隐藏图标，
     */
    public static final String DISPLAY = "display";


    /**
     * 查询所有数据
     */
    public static final String[] SQLITE_ALL_PROJECTION = {APP_ID,
            MAIN_ACTIVITY_NAME, PACKAGENAME};

    /**
     * 查询包名和对应的ID
     */
    public static final String[] SQLITE_PACKAGENAME_PROJECTION = {APP_ID,
            MAIN_ACTIVITY_NAME, PACKAGENAME};

    /**
     * 查询类别和ID
     */
    public static final String[] SQLITE_QUERY_CATEGORY_PROJECTION = {
            APP_ID};
    //每一页的程序个数
    public static final int PAGESIZE = 10;
}
