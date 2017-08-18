package cn.donica.slcd.launcher.test;

import android.test.ApplicationTestCase;

import cn.donica.slcd.launcher.BaseApplication;

/**
 * Created by liangmingjie on 2016/6/29.
 */
public class BaseApplicationTest extends ApplicationTestCase<BaseApplication> {

    private BaseApplication application;

    // 调用父类构造函数，且构造函中传递的参数为被测试的类
    public BaseApplicationTest() {
        super(BaseApplication.class);
    }

    /*
     * 初始化application
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // 获取application之前必须调用的方法
        createApplication();
        application = getApplication();
    }
}