package cn.donica.slcd.launcher.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import cn.donica.slcd.launcher.db.DbHelperColumn;

//数据恢复与备份只需要调用注释的代码即可
//// 数据恢复
//private void dataRecover() {
//  // TODO Auto-generated method stub
//  new DbBackupUtil(this).execute("restroeDatabase");
//}
//
//// 数据备份
//private void dataBackup() {
//  // TODO Auto-generated method stub
//  new DbBackupUtil(this).execute("backupDatabase");
//}
public class DbBackupUtil extends AsyncTask<String, Void, Integer> {
    private static final String COMMAND_BACKUP = "backupDatabase";
    private static final String COMMAND_RESTORE = "restroeDatabase";
    private static final String TAG = "DbBackupUtil";
    private Context mContext;

    public DbBackupUtil(Context context) {
        this.mContext = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        // TODO Auto-generated method stub
        //dbDir为内存卡根目录的launcher.db
        File sdDbDir = new File(Environment.getExternalStorageDirectory(), DbHelperColumn.DATABASENAME);
        //appDbDir为应用私有的files目录
        // /data/data/cn.donica.component/files
        File appDbDir = mContext.getFilesDir();
        File backup = new File(appDbDir, sdDbDir.getName());
        String command = params[0];
        if (command.equals(COMMAND_BACKUP)) {
            try {
                backup.createNewFile();
                fileCopy(sdDbDir, backup);
                return Log.d(TAG, "backup ok");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return Log.d(TAG, "backup fail");
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                fileCopy(backup, sdDbDir);
                return Log.d(TAG, "restore ok");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return Log.d(TAG, "restore fail");
            }
        } else {
            return null;
        }
    }

    /**
     * @param dbFile 原来db文件的路径
     * @param backup 复制到的路径
     * @throws IOException
     */
    @SuppressWarnings("resource")
    private void fileCopy(File dbFile, File backup) throws IOException {

        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 从accets文件夹复制launcher.db到sd卡根目录
     *
     * @param fileName
     * @throws IOException
     */
    public void AssetsToSD(String fileName) throws IOException {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(fileName);
        myInput = mContext.getAssets().open("launcher.db");
        ;
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
}
