package cn.donica.slcd.ble.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.litepal.tablemanager.Connector;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-07 18:11
 * Describe:
 */

public class SlcdProvider extends ContentProvider {
    private final static String AUTHORITY = "cn.donica.slcd.provider";
    private final static String TABLE_MONITOR = "monitor";
    private final static String TABLE_CONFIG = "config";
    private final static int CODE_VA = 1;
    private final static int CODE_PA = 2;
    private final static int CODE_IP = 3;
    private final static int CODE_SEAT = 4;
    private final static int CODE_SEATBACK = 5;
    private UriMatcher matcher;
    private SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        db = Connector.getDatabase();
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "monitor/va", CODE_VA);
        matcher.addURI(AUTHORITY, "monitor/pa", CODE_PA);
        matcher.addURI(AUTHORITY, "monitor/seatback", CODE_SEATBACK);
        matcher.addURI(AUTHORITY, "config/ip", CODE_IP);
        matcher.addURI(AUTHORITY, "config/seat", CODE_SEAT);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int code = matcher.match(uri);
        Cursor cursor = null;
        switch (code) {
            case CODE_PA:
                cursor = db.query(TABLE_MONITOR, null, "name=?", new String[]{"pa"}, null, null, null);
                break;
            case CODE_VA:
                cursor = db.query(TABLE_MONITOR, null, "name=?", new String[]{"va"}, null, null, null);
                break;
            case CODE_SEATBACK:
                cursor = db.query(TABLE_MONITOR, null, "name=?", new String[]{"seatback"}, null, null, null);
                break;
            case CODE_IP:
                cursor = db.query(TABLE_CONFIG, null, "name=?", new String[]{"ip"}, null, null, null);
                break;
            case CODE_SEAT:
                cursor = db.query(TABLE_CONFIG, null, "name=?", new String[]{"seat"}, null, null, null);
                break;
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int code = matcher.match(uri);
        switch (code) {
            case CODE_IP:
            case CODE_SEAT:
                db.insert(TABLE_CONFIG, null, values);
                break;
            case CODE_SEATBACK:
                db.insert(TABLE_MONITOR, null, values);
                break;
        }
        return uri;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int code = matcher.match(uri);
        int id = 0;
        switch (code) {
            case CODE_IP:
                id = db.update(TABLE_CONFIG, values, "name=?", new String[]{"ip"});
                break;
            case CODE_SEAT:
                id = db.update(TABLE_CONFIG, values, "name=?", new String[]{"seat"});
                break;
            case CODE_SEATBACK:
                id = db.update(TABLE_MONITOR, values, "name=?", new String[]{"seatback"});
                getContext().getContentResolver().notifyChange(uri, null);
                break;
        }

        return id;
    }

}
