package com.example.android.camerasample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.camerasample.view.CaptureView;
import com.example.android.camerasample.view.RotateImageView;
import com.example.android.library.cameraview.util.ImageUtil;
import com.example.android.library.cameraview.util.TConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * created by zyh
 * on 2019-09-20
 */
public class EditImageActivity extends AppCompatActivity implements OnClickListener {
    // 压缩后图片大小
    public static final int OLD_SIZE = 100;
    public static final int SMALL_SIZE = 50;

    // 操作按钮
    private ImageView mRedoCamera;

    //
    private ImageView enterEditImage;

    // 旋转
    private ImageView mRotateButton;

    // 选择框
    private CaptureView mCaptureView;

    // 图片
    private RotateImageView target_image;

    private Bitmap bitmap;
    private int rx = 0;
    private int ry = 0;
    private int[] wh;

    private static final int MAX_ZOOM = 4;
    private static int height;
    private static int width;
    private Matrix matrix = new Matrix();
    private float zoomDegree = 1;
    private Toast mToast;

    private final int SHOW_TOAST = 0;
    private final int CROP_TOAST = 1;
    private final int ROTATE_TOAST = 2;

    private int cropimage_width = 0;
    private int cropimage_height = 0;

    private int smallimage_width = 0;
    private int smallimage_height = 0;

    private boolean isMore = false;

    private boolean bExtern = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s8s_edit_image_layout);
        initView();
        new LoadImageTask(this).execute();
    }


    private static class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
        private WeakReference<EditImageActivity> activityWeakReference;

        public LoadImageTask(EditImageActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            EditImageActivity activity = activityWeakReference.get();
            if (activity == null) {
                return null;
            }
            String picPath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + File.separator + TConstants.ORIGINAL_PIC_PATH;
            Bitmap targetBitmap = ImageUtil.decodeBitmap(picPath,activity);
            return targetBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            EditImageActivity activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }
            if (bitmap != null) {
                if (activity.mCaptureView != null) {
                    activity.mCaptureView.setVisibility(View.VISIBLE);
                }
                activity.target_image.setVisibility(View.VISIBLE);
                activity.target_image.setImageBitmap(bitmap);

            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void initView() {

        target_image = (RotateImageView) findViewById(R.id.s8s_target_image);
        target_image.setOnTouchListener(new TouchListener());

        mRedoCamera = (ImageView) findViewById(R.id.s8s_edit_redo_camera);
        mRedoCamera.setOnClickListener(this);

        enterEditImage = (ImageView) findViewById(R.id.edit_enter_image);
        enterEditImage.setOnClickListener(this);

        mRotateButton = (ImageView) findViewById(R.id.s8s_edit_rotate_image);
        mRotateButton.setOnClickListener(this);

        mCaptureView = (CaptureView) this.findViewById(R.id.s8s_capture);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.s8s_edit_redo_camera:
                finish();
                break;
            case R.id.edit_enter_image:
                cropImage(false);
                startResultActivity();
                break;
            case R.id.s8s_edit_rotate_image:
                target_image.rotateRight();
                break;
        }
    }


    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    private void startResultActivity() {
        Intent intent = new Intent(EditImageActivity.this, ResultActivity.class);
        startActivity(intent);
        finish();
    }

    private int image_degrees = 0;

    private double image_rate = 0;

    private Rect getImageRect(Bitmap target_bitmap) {

        Rect rect = new Rect();

        int width = target_image.getWidth();
        int height = target_image.getHeight();

        int image_width = target_bitmap.getWidth();
        int image_height = target_bitmap.getHeight();

        double rate_w = image_width * 1.0 / width;
        double rate_h = image_height * 1.0 / height;

        if (rate_w <= 1 && rate_h <= 1) {
            double rate = rate_w > rate_h ? rate_w : rate_h;
            rect.left = (int) ((width - image_width / rate) / 2);
            rect.top = (int) ((height - image_height / rate) / 2);

            if (rect.left < 0)
                rect.left = 0;
            if (rect.top < 0)
                rect.top = 0;

            rect.right = (int) (rect.left + image_width / rate);
            rect.bottom = (int) (rect.top + image_height / rate);

            image_rate = rate;

        } else {

            double rate = rate_w > rate_h ? rate_w : rate_h;

            rect.left = (int) ((width - image_width / rate) / 2);
            rect.top = (int) ((height - image_height / rate) / 2);

            rect.right = (int) (rect.left + image_width / rate);
            rect.bottom = (int) (rect.top + image_height / rate);

            image_rate = rate;
        }
        return rect;
    }

    private boolean cropImage(boolean all) {
        bitmap = target_image.getImageBitmap();
        if (bitmap == null) {
            showToast("请重新选择图片区域");
            return false;
        }
        Rect cropRect = mCaptureView.getCaptureRect();

        Rect cropRect2 = getImageRect(bitmap);

        if (all) {
            cropRect = cropRect2;
        }


        if (!cropRect.intersect(cropRect2)) { // 没有交集
            showToast("请重新选择图片区域");
            return false;
        }

        // 处理成相对图像的位置
        Rect dstRect = new Rect();
        dstRect.left = cropRect.left - cropRect2.left;// - rx;
        dstRect.top = cropRect.top - cropRect2.top;// - ry;

        dstRect.right = dstRect.left + cropRect.width();
        dstRect.bottom = dstRect.top + cropRect.height();

        // 映射到实际图片的位置
        int x1 = (int) (dstRect.left * image_rate);
        int y1 = (int) (dstRect.top * image_rate);
        int width = (int) (dstRect.width() * image_rate);
        int height = (int) (dstRect.height() * image_rate);

        if (x1 < 0)
            x1 = 0;
        else if (x1 > bitmap.getWidth()) {
            x1 = bitmap.getWidth();
        }

        if (y1 < 0)
            y1 = 0;
        else if (y1 > bitmap.getHeight()) {
            y1 = bitmap.getHeight();
        }

        if (x1 + width > bitmap.getWidth()) {
            width = bitmap.getWidth() - x1;
        }
        if (y1 + height > bitmap.getHeight()) {
            height = bitmap.getHeight() - y1;
        }

        // 剪切图 ocr识别用
        bitmap = Bitmap.createBitmap(bitmap, x1, y1, width, height);
     
        return compressImage(bitmap);
    }

    /**
     * 监听缩放、平移
     */
    private class TouchListener implements OnTouchListener {

        private PointF startPoint = new PointF();

        private Matrix currentMaritx = new Matrix();

        private int mode = 0;// 用于标记模式
        private static final int DRAG = 1;// 拖动
        private static final int ZOOM = 2;// 放大
        private float startDis = 0;
        private PointF midPoint;// 中心点

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mode = DRAG;
                    currentMaritx.set(target_image.getImageMatrix());// 记录ImageView当期的移动位置
                    startPoint.set(event.getX(), event.getY());// 开始点
                    break;

                case MotionEvent.ACTION_MOVE:// 移动事件
                    if (mode == DRAG) {// 图片拖动事件
                        float dx = event.getX() - startPoint.x;// x轴移动距离
                        float dy = event.getY() - startPoint.y;

                        int x = (int) (dx / 50);
                        int y = (int) (dy / 50);

                        int[] start = new int[2];
                        target_image.getLocationInWindow(start);
                        matrix.set(currentMaritx);// 在当前的位置基础上移动
                        matrix.postTranslate(dx, dy);
                        // }
                    } else if (mode == ZOOM) {// 图片放大事件
                        float endDis = distance(event);// 结束距离
                        float[] t_f = new float[9];
                        currentMaritx.getValues(t_f);
                        if (endDis > 10f) {
                            float scale = endDis / startDis;// 放大倍数
                            matrix.set(currentMaritx);
                            if (t_f[0] * scale <= MAX_ZOOM) {
                                matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            } else {
                                float n_s = MAX_ZOOM / t_f[0];
                                matrix.postScale(n_s, n_s, midPoint.x, midPoint.y);
                            }
                        }
                        ;
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    float dx = event.getX() - startPoint.x;// x轴移动距离
                    float dy = event.getY() - startPoint.y;
                    // startX -= dx / zoomDegree;
                    // startY -= dy / zoomDegree;
                    mode = 0;
                    break;
                // 有手指离开屏幕，但屏幕还有触点(手指)
                case MotionEvent.ACTION_POINTER_UP:
                    float[] f = new float[9];
                    matrix.getValues(f);
                    zoomDegree = f[0];
                    mode = 0;
                    break;
                // 当屏幕上已经有触点（手指）,再有一个手指压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = ZOOM;
                    startDis = distance(event);

                    if (startDis > 10f) {// 避免手指上有两个茧
                        midPoint = mid(event);
                        currentMaritx.set(target_image.getImageMatrix());// 记录当前的缩放倍数
                    }
                    break;
            }
            target_image.setImageMatrix(matrix);
            return true;
        }
    }

    /**
     * 两点之间的距离
     *
     * @param event
     * @return
     */
    private static float distance(MotionEvent event) {
        // 两根线的距离
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 获得屏幕中心点
     *
     * @param event
     * @return
     */
    private static PointF mid(MotionEvent event) {
        return new PointF(width / 2, height / 2);
    }

    /*
     * 压缩图片
     */
    public boolean compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

        int fsize = baos.toByteArray().length / 1024;

        long tt = System.currentTimeMillis();

        int options = 100;

        if (fsize > 3000) {
            options = 20;
        } else if (fsize > 2048) {
            options = 30;
        } else if (fsize > 1024) { // >1M
            options = 40;
        } else if (fsize > 700) { // >
            options = 50;
        } else if (fsize > 500) { // >
            options = 60;
        } else if (fsize > 300) { // >
            options = 70;
        } else {
            options = 80;
        }

        int tc = 0;
        // 用于上传显示的图片
        if (fsize > EditImageActivity.OLD_SIZE) {
            // options = 80;
            while (baos.toByteArray().length / 1024 > EditImageActivity.OLD_SIZE) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                options -= 10;// 每次都减少10
                if (options <= 10) {
                    options = 10;
                    break;
                }
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                tc++;
            }
        }

        FileOutputStream fout = null;
        String cropImagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + File.separator + TConstants.CROP_PIC_PATH;
        try {
            fout = new FileOutputStream(cropImagePath);
            if (!image.compress(Bitmap.CompressFormat.JPEG, options, fout)) {
                return false;
            }
        } catch (Exception e) {
            showToast("磁盘空间不足，无法保存图片。");
        } finally {
            if (fout != null) {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
   
}
