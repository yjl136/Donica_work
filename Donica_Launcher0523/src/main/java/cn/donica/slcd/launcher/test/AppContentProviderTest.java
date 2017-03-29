package cn.donica.slcd.launcher.test;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import cn.donica.slcd.launcher.AppsContentProvider;

/**
 * Created by liangmingjie on 2016/6/29.
 */
public class AppContentProviderTest extends ProviderTestCase2<AppsContentProvider> {
    /**
     * Constructor.
     */
    public AppContentProviderTest() {
        super(AppsContentProvider.class, AppsContentProvider.AUTHORITY);
    }

    public void testInsertOneItem() {
        ContentResolver contentResolver = getMockContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://cn.donica.slcd.launcher.contentprovider/launcher"), null, null, null, null);
        assertEquals(0, cursor.getCount());
        cursor.close();

    }
}
