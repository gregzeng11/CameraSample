package com.example.android.camerasample.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * created by zyh
 * on 2019-09-20
 */
public class RotateImageView extends ImageView {

	private Bitmap mBitmap;
	private int dAngle = 0;
	private int mAngle = 0;
	// private boolean run = false;
	private Matrix mMatrix = new Matrix();

	// private Paint mPaint = new Paint();

	public RotateImageView(Context context) {
		super(context);
	}

	public RotateImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RotateImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Bitmap getImageBitmap(){
		
		if( mAngle%360 == 0 ){
			return mBitmap;
		}
		
		float px = mBitmap.getWidth() / 2;
		float py = mBitmap.getHeight() / 2;

		mMatrix.reset();
		mMatrix.preRotate(mAngle, px, py);
		Bitmap bmp = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), mMatrix, true);
		return bmp;
	}
	
	public void setImageBitmap(Bitmap bm) {   //防止rotate里面有调用到次函数
		mBitmap = bm;
		super.setImageBitmap(bm);
	}
	
	public void rotateLeft() {
		dAngle = -90;
		rotate();
	}

	public void rotateRight() {
		dAngle = 90;
		rotate();
	}

	private void rotate() {
		mAngle += dAngle;
		if (mBitmap != null) {
			float px = mBitmap.getWidth() / 2;
			float py = mBitmap.getHeight() / 2;

			mMatrix.reset();
			mMatrix.preRotate(mAngle, px, py);
			Bitmap bmp = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), mMatrix, true);
			setImageBitmap(bmp);
			// if (bmp != mBitmap)
			// bmp.recycle();
			
		}
	}

	public void stopRotate() {
		dAngle = 0;
	}
}
