package com.example.android.camerasample;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.android.library.cameraview.util.ImageUtil;
import com.example.android.library.cameraview.util.TConstants;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * created by zyh
 * on 2019-09-20
 */
public class ResultActivity extends AppCompatActivity {
    private ImageView mPicIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        initView();
        new ResultActivity.LoadImageTask(this).execute();
    }

    private void initView() {
        mPicIv = (ImageView) findViewById(R.id.iv_pic);
    }

    private static class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
        private WeakReference<ResultActivity> activityWeakReference;

        public LoadImageTask(ResultActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            ResultActivity activity = activityWeakReference.get();
            if (activity == null) {
                return null;
            }
            String picPath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + File.separator + TConstants.CROP_PIC_PATH;
            Bitmap targetBitmap = ImageUtil.decodeBitmap(picPath, activity);
            return targetBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ResultActivity activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }
            if (bitmap != null) {
                activity.mPicIv.setImageBitmap(bitmap);

            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
