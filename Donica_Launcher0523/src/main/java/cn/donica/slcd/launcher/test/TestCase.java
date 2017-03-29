package cn.donica.slcd.launcher.test;

/**
 * Created by liangmingjie on 2016/5/25.
 */

import android.test.AndroidTestCase;

public class TestCase extends AndroidTestCase {

    private String tag = "TestCase";

//    public void testInsert(){
//
//        //另外一个程序的uri
//        Uri uri = Uri.parse("content://com.wzw.sqllitedemo.providers.PersonContentProvider/person/insert");
//        //获取内容提供者访问对象
//        ContentResolver resolver = getContext().getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put("name","meimei");
//        values.put("age", 18);
//        Log.i(tag, "uri"+uri);
//        uri = resolver.insert(uri, values);
//        long id = ContentUris.parseId(uri);
//        Log.i(tag, "插入的id"+id);
//    }

//    public void testDelete(){
//
//        //另外一个程序的uri
//        Uri uri = Uri.parse("content://com.wzw.sqllitedemo.providers.PersonContentProvider/person/delete");
//        //获取内容提供者访问对象
//        ContentResolver resolver = getContext().getContentResolver();
//        String where = "_id=?";
//        String[] selectionArgs={"8"};
//        int count = resolver.delete(uri, where, selectionArgs);
//        Log.i(tag, "删除了"+count);
//    }

    public void tesUpdate() {

        //另外一个程序的uri
//        Uri uri = Uri.parse("content://cn.donica.slcd.launcher.contentprovider/launcher");
//        //获取内容提供者访问对象
//        ContentResolver resolver = getContext().getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put("display", 0);
//        resolver.update(uri, values, "packageName = ?", new String[]{"com.android.camera"});
        System.out.println("HelloWorld");

    }

//    public static  void main(String[] args){
//        TestCase testCase = new TestCase();
//        testCase.tesUpdate();
//    }


//    public void testQueryAll(){
//
//        //另外一个程序的uri
//        Uri uri = Uri.parse("content://com.wzw.sqllitedemo.providers.PersonContentProvider/person/queryAll");
//        //获取内容提供者访问对象
//        ContentResolver resolver = getContext().getContentResolver();
//        Cursor cursor = resolver.query(uri, new String[]{"_id","name","age"}, null, null, "_id");
//        if(cursor!=null&&cursor.getCount()>0){
//            int id;
//            String name;
//            int age;
//            while(cursor.moveToNext()){
//                id=cursor.getInt(0);
//                name=cursor.getString(1);
//                age=cursor.getInt(2);
//                Log.i(tag, "id:"+id+"name:"+name+"age:"+age);
//            }
//            cursor.close();
//        }
//    }
}