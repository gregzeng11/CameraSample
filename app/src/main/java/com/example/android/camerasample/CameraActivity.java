package com.example.android.camerasample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.camerasample.view.CameraGridView;
import com.example.android.library.cameraview.CameraView;
import com.example.android.library.cameraview.TakePhoto;
import com.example.android.library.cameraview.model.TContextWrap;
import com.example.android.library.cameraview.model.TResult;
import com.example.android.library.cameraview.permission.PermissionManager;
import com.example.android.library.cameraview.util.TConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * created by zyh
 * on 2019-09-17
 */
public class CameraActivity extends AppCompatActivity implements View.OnClickListener, TakePhoto.TakeResultListener {
    private static final String TAG = CameraActivity.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private CameraView mCameraView;
    private TextView mPromptTv;
    private TextView mScanTv;
    private ImageView mPhotoRectangleIv;
    private CameraGridView mCameraGridView;
    private ImageView mFlashIv;
    private RadioGroup mPicTypeRg;
    private RadioButton mCropRb;
    private RadioButton mScanRb;
    private RadioButton mPointRb;
    private ImageView mPointIv;
    private ImageView mCameraIv;
    private Handler mBackgroundHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        initView();
        initListener();
    }

    private void initView() {
        mCameraView = (CameraView) findViewById(R.id.camera_view);
        mPromptTv = (TextView) findViewById(R.id.prompt_camera_text);
        mScanTv = (TextView) findViewById(R.id.prompt_scan_text);
        mPhotoRectangleIv = (ImageView) findViewById(R.id.img_rectangel);
        mCameraGridView = (CameraGridView) findViewById(R.id.s8seditor_camera_gridview);
        mFlashIv = (ImageView) findViewById(R.id.s8seditor_camera_ledbtn);
        mPicTypeRg = (RadioGroup) findViewById(R.id.rg_statue_choose);
        mCropRb = (RadioButton) findViewById(R.id.rb_paizhaoquti);
        mScanRb = (RadioButton) findViewById(R.id.rb_saodanti);
        mPointRb = (RadioButton) findViewById(R.id.rb_paizhaodianti);
        mPointIv = (ImageView) findViewById(R.id.sao_btn);
        mCameraIv = (ImageView) findViewById(R.id.take_pic_btn);
    }

    private void initListener() {
        mFlashIv.setOnClickListener(this);
        mPointIv.setOnClickListener(this);
        mCameraIv.setOnClickListener(this);

        mPicTypeRg.check(R.id.rb_paizhaoquti);
        mPicTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rb_paizhaoquti) {
                    cropPicQs();
                } else if (checkedId == R.id.rb_saodanti) {
                    scanPicQs();
                } else if (checkedId == R.id.rb_paizhaodianti) {
                    pointPicQs();
                }
            }
        });

        mCameraView.addCallback(mCallback);
    }


    private void cropPicQs() {

    }

    private void scanPicQs() {

    }

    private void pointPicQs() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this));
        if (PermissionManager.TPermissionType.GRANTED.equals(type)) {
            mCameraView.start();
        }
    }

    private CameraView.Callback mCallback = new CameraView.Callback() {
        @Override
        public void onCameraOpened(CameraView cameraView) {
            super.onCameraOpened(cameraView);
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            super.onPictureTaken(cameraView, data);
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TConstants.ORIGINAL_PIC_PATH);
                    file.delete();
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();
                        Log.w(TAG, "onPictureTaken filepath " + file.getPath());
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                    
                    startEditImageActivity();
                }
            });
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            super.onCameraClosed(cameraView);
        }

        @Override
        public void onCameraError(CameraView cameraView) {
            super.onCameraError(cameraView);
        }
    };

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.s8seditor_camera_ledbtn:
                break;
            case R.id.sao_btn:
                break;
            case R.id.take_pic_btn:
                mCameraView.takePicture();
                break;
        }
    }


    private void startEditImageActivity() {
        Intent intent = new Intent(CameraActivity.this, EditImageActivity.class);
        startActivity(intent);
    }

    @Override
    public void takeSuccess(TResult result) {

    }


    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public void takePermissionGranted() {
        mCameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mCameraView.stop();
        } catch (Exception e) {
            Log.e(TAG, "stop camera fail", e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

}
