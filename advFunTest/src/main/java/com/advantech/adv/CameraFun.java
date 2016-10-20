package com.advantech.adv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.advantech.advfuntest.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class CameraFun extends Activity {
    public Intent intent;
    public ExecCmd execcmd;
    private Resources res;
    public int isautotest = 0;

    SurfaceView sView;
    SurfaceHolder surfaceHolder;
    int screenWidth, screenHeight;
    int cameraPosition = 0; // 0代表后置摄像头，1代表前置摄像头
    int cameraCount = 0; // 摄像头数量
    // 定义系统所用的照相机
    Camera camera;
    // 是否在预览中
    boolean isPreview = false;
    ImageButton camera_shutter, camera_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        execcmd = new ExecCmd();
        res = getResources();
        isautotest = intent.getIntExtra("isautotest", 0);
        // 取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 加载布局
        setContentView(R.layout.camerafun);

        //判断摄像头的个数
        cameraCount = Camera.getNumberOfCameras();
        if (cameraCount <= 0) {
            //没有摄像头，结束当前页面
            Toast.makeText(CameraFun.this, "There is no camera on this device.", Toast.LENGTH_LONG).show();
            resultRquest(false);
        } else {
            // 获取窗口管理器
            WindowManager wm = getWindowManager();
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            // 获取屏幕的宽和高
            display.getMetrics(metrics);
            screenWidth = metrics.widthPixels;
            screenHeight = metrics.heightPixels;
            // 获取界面中SurfaceView组件
            sView = (SurfaceView) findViewById(R.id.sview);
            // 设置该SurfaceView,自己不维护缓冲
            sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            // 获得SurfaceView的SurfaceHolder
            surfaceHolder = sView.getHolder();
            // 为surfaceHolder添加一个回调监听器
            surfaceHolder.addCallback(new Callback() {
                @Override
                public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
                }

                @Override
                public void surfaceCreated(SurfaceHolder arg0) {
                    // 初始化摄像头
                    initCamera();
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder arg0) {
                    // 如果camera不为null ,释放摄像头
                    if (camera != null) {
                        if (isPreview)
                            camera.stopPreview();
                        camera.release();
                        camera = null;
                    }
                }
            });

            // 设置sView单击自动对焦
            sView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    camera.autoFocus(null);
                }
            });

            // 设置sView长按弹出对话框
            sView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View arg0) {
                    showDialog();
                    return false;
                }
            });

            // 设置拍照按钮并弹出保存对话框
            camera_shutter = (ImageButton) findViewById(R.id.camera_shutter);
            camera_shutter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (camera != null) {
                        // 控制摄像头自动对焦后才拍照,实现自动对焦后的回调函数
                        camera.autoFocus(new AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean arg0, Camera arg1) {
                                if (arg0) {
                                    // takePicture()方法需要传入三个监听器参数
                                    // 第一个监听器：当用户按下快门时激发该监听器
                                    // 第二个监听器：当相机获取原始照片时激发该监听器
                                    // 第三个监听器：当相机获取JPG照片时激发该监听器
                                    camera.takePicture(null, null, myJpegCallback);
                                }
                            }
                        });
                    }
                }
            });

            // 设置切换摄像头
            camera_switch = (ImageButton) findViewById(R.id.camera_switch);
            camera_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // 切换前后摄像头
                    CameraInfo cameraInfo = new CameraInfo();
                    for (int i = 0; i < cameraCount; i++) {
                        Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
                        if (cameraPosition == 0) {
                            // 现在是后置，变更为前置
                            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                                // CAMERA_FACING_BACK后置
                                camera.stopPreview();// 停掉原来摄像头的预览
                                camera.release();// 释放资源
                                camera = null;// 取消原来摄像头
                                camera = Camera.open(i);// 打开当前选中的摄像头
                                try {
                                    camera.setPreviewDisplay(surfaceHolder);// 通过surfaceview显示取景画面
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                camera.startPreview();// 开始预览
                                cameraPosition = 1;
                                break;
                            }
                        } else {
                            // 现在是前置， 变更为后置
                            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                                // CAMERA_FACING_BACK后置
                                camera.stopPreview();// 停掉原来摄像头的预览
                                camera.release();// 释放资源
                                camera = null;// 取消原来摄像头
                                camera = Camera.open(i);// 打开当前选中的摄像头
                                try {
                                    camera.setPreviewDisplay(surfaceHolder);// 通过surfaceview显示取景画面
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                camera.startPreview();// 开始预览
                                cameraPosition = 0;
                                break;
                            }
                        }

                    }

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 如果camera不为null ,释放摄像头
        if (camera != null) {
            if (isPreview)
                camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialog();
            return true;
        }
        return false;
    }

    // 初始化摄像头方法实现
    private void initCamera() {
        if (!isPreview) {
//			if(hasBackFacingCamera()){
//				Log.i("camera","hasBackFacingCamera");
//				camera = Camera.open(0);
//			}else if(hasFrontFacingCamera()){
//				Log.i("camera","hasFrontFacingCamera");
//				camera = Camera.open(0);
//			}
            camera = Camera.open(0);
            //此处可以传入参数，打开前置摄像头，默认打开后置摄像头
            //camera.setDisplayOrientation(0);
            Log.i("camera", "摄像头Open完成");
        }
        if (camera != null && !isPreview) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                // 设置预览照片的大小
                parameters.setPreviewSize(screenWidth, screenHeight);
                // 每秒显示多少帧的范围
                parameters.setPreviewFpsRange(4, 10);
                ;
                // 设置图片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                // 设置JPG照片的质量
                parameters.set("jpeg-quality", 85);
                // 设置照片的大小
                parameters.setPictureSize(screenWidth, screenHeight);
                camera.setParameters(parameters);
                // 通过SurfaceView显示取景画面
                camera.setPreviewDisplay(surfaceHolder);
                // 开始预览
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isPreview = true;
        }
    }

    // 当相机获取JPG照片时回调方法实现
    PictureCallback myJpegCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // 根据拍照所得的数据创建位图
            final Bitmap bm = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
            // 加载/layout/save.xml文件对应的布局资源
            View saveDialog = getLayoutInflater().inflate(R.layout.save, null);
            final EditText photoName = (EditText) saveDialog.findViewById(R.id.phone_name);
            photoName.setText("Advantech"
                    + new DateFormat().format(("yyyyMMdd_hhmmss"), Calendar.getInstance(Locale.CHINA)));
            photoName.setFocusable(true);

            // 获取saveDialog对话框上的ImageView组件
            ImageView show = (ImageView) saveDialog.findViewById(R.id.show);
            // 显示刚刚拍到的照片
            show.setImageBitmap(bm);
            // 使用对话框显示saveDialog组件
            new AlertDialog.Builder(CameraFun.this).setView(saveDialog)
                    .setPositiveButton("保存", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // 创建一个SD卡上的文件
                            File file = new File(Environment
                                    .getExternalStorageDirectory(), photoName
                                    .getText().toString() + ".jpg");
                            FileOutputStream outStream = null;
                            try {
                                // 打开指定文件对应的输出流
                                outStream = new FileOutputStream(file);
                                // 将位图输出到指定文件中
                                bm.compress(CompressFormat.JPEG, 100, outStream);
                                // 关闭输出流
                                outStream.close();
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("取消", null).show();
            // 重新浏览
            camera.stopPreview();
            camera.startPreview();
            isPreview = true;
        }
    };

    private void showDialog() {
        new AlertDialog.Builder(CameraFun.this)
                .setTitle(res.getString(R.string.camera_title))
                .setMessage(res.getString(R.string.camera_msg))
                .setCancelable(false)
                .setPositiveButton(res.getString(R.string.yes), dialogClick)
                .setNegativeButton(res.getString(R.string.no), dialogClick)
                .create()
                .show();
    }

    private android.content.DialogInterface.OnClickListener dialogClick = new android.content.DialogInterface.OnClickListener() {
        // @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    resultRquest(true);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    resultRquest(false);
                    break;
                default:
                    break;
            }
        }
    };

    private void resultRquest(boolean flag) {
        if (flag == true) {
            execcmd.writeLog("Camera Test: PASS\n");
        } else {
            execcmd.writeLog("Camera Test: FAILED\n");
        }
        if (1 == isautotest)
            intent.putExtra("isauto", 1);
        else
            intent.putExtra("isauto", 0);
        intent.putExtra("camera", flag);
        intent.putExtra("funcode", 7);
        // 设置返回数据
        setResult(0, intent);
        finish();
    }

    private boolean checkCameraFacing(final int facing) {
        CameraInfo info = new CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    public boolean hasBackFacingCamera() {
        final int CAMERA_FACING_BACK = 0;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    public boolean hasFrontFacingCamera() {
        final int CAMERA_FACING_BACK = 1;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

}
