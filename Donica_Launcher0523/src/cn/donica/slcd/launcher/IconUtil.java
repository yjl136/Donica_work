package cn.donica.slcd.launcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.lang.ref.WeakReference;

import cn.donica.slcd.launcher.util.LogUtil;

/**
 * Created by liangmingjie on 2016/4/8.
 */
public class IconUtil {
    private static final String TAG = "IconUtil";
    public static Canvas mCanvas;
    public static Paint mPaint;

    public static void init(Context context) {
        mCanvas = new Canvas();
        mPaint = new Paint();
        mPaint.setFilterBitmap(false);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    /**/
/* 背板留出2像素边 */
    private static final int EDGE_WIDTH = 4;


    /**
     * 返回图标按背板裁剪后得Bitmap
     *
     * @param background 背板的Bitmap
     * @param icon       图标的Bitmap
     */
    public static Bitmap getBitmapWithNoScale(Drawable background, Bitmap icon) {

        Bitmap background_bit = ((BitmapDrawable) background).getBitmap();
        background_bit = background_bit.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap copy = background_bit.createScaledBitmap(background_bit, background_bit.getWidth() - EDGE_WIDTH * 2, background_bit.getHeight() - EDGE_WIDTH * 2, true);
        LogUtil.d(TAG, "icon1分辨率为:" + icon.getWidth() + "*" + icon.getHeight());
        if (icon.getWidth() < background_bit.getWidth()) {
            Bitmap tmp = null;
            try {

                tmp = Bitmap.createBitmap(background_bit.getWidth(), background_bit.getHeight(), Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                // 如果发生了OOM问题， 重新申请一次
                tmp = Bitmap.createBitmap(background_bit.getWidth(), background_bit.getHeight(), Bitmap.Config.ARGB_8888);
                e.printStackTrace();
            }
            Canvas mCanvas = new Canvas(tmp);
            mCanvas.drawBitmap(icon, (background_bit.getWidth() - icon.getWidth()) / 2, (background_bit.getHeight() - icon.getHeight()) / 2, null);
            icon = tmp;
        } else {
            if (icon.getWidth() > background.getIntrinsicWidth()) {
                icon = Bitmap.createScaledBitmap(icon, background.getIntrinsicWidth(), background.getIntrinsicHeight(),
                        true);
            } else {
                icon = icon.copy(Bitmap.Config.ARGB_8888, true);
            }
        }


        mCanvas.setBitmap(icon);

        mCanvas.drawBitmap(copy, EDGE_WIDTH, EDGE_WIDTH, mPaint);
        mCanvas.setBitmap(null);
        icon = icon.createBitmap(icon, EDGE_WIDTH, EDGE_WIDTH, background_bit.getWidth() - EDGE_WIDTH * 2,
                background_bit.getWidth() - EDGE_WIDTH * 2);
        mCanvas.setBitmap(background_bit);
        mCanvas.drawBitmap(icon, EDGE_WIDTH, EDGE_WIDTH, null);
        mCanvas.setBitmap(null);
        return background_bit;
    }


    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param min
     * @return
     */
    private Canvas createCircleImage(int min) {

        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        // 去锯齿
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        // 绘制圆形
        canvas.drawCircle(0, 0, 128, paint);

        Bitmap b = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);

        return canvas;
    }

    public static Bitmap test() {
        Bitmap canvasBitmap;
        canvasBitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(canvasBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawBitmap(canvasBitmap, 0, 0, paint);
        return canvasBitmap;
    }

    public static Bitmap getBitmapFromExternalStorage(String picName) {
        String picPath = Config.picFile + picName;
        int w = Config.picWidth;
        int h = Config.picHeight;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(picPath, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(picName, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }
}
