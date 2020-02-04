package com.example.android.camerasample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.android.camerasample.R;

/**
 * created by zyh
 * on 2019-09-19
 */
public class CameraGridView extends View {

	public CameraGridView(Context context) {
		super(context);
	}

	public CameraGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CameraGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private boolean port = false;

	public void setPortFlag(boolean b) {
		port = b;
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		Paint paint = new Paint();
		paint.setColor(getResources().getColor(R.color.s8s_white));

		int width = getWidth();
		int height = getHeight();
		System.out.println("ww w = " + width + ",, h = " + height);

		canvas.save();

		if (port) { // 竖屏

			// 横线
			float yy = (float) (height * 0.30);
			canvas.drawLine(0, yy, width, yy, paint);
			yy = (float) (height * 0.65);
			canvas.drawLine(0, yy, width, yy, paint);

			// 竖线
			float xx = (float) (width * 0.30);
			canvas.drawLine(xx, 0, xx, height, paint);
			xx = (float) (width * 0.70);
			canvas.drawLine(xx, 0, xx, height, paint);

		} else { // 横屏

			// 横线
			/*
			 * float yy = (float) (height * 0.22); canvas.drawLine(0, yy, width, yy, paint);
			 * yy = (float) (height * 0.80); canvas.drawLine(0, yy, width, yy, paint);
			 * 
			 * // 竖线 float xx = (float) (width * 0.20); canvas.drawLine(xx, 0, xx, height,
			 * paint); xx = (float) (width * 0.72); canvas.drawLine(xx, 0, xx, height,
			 * paint);
			 */
			float yy = (float) (height * 1 / 3);
			canvas.drawLine(0, yy, width, yy, paint);
			yy = (float) (height * 2 / 3);
			canvas.drawLine(0, yy, width, yy, paint);

			// 竖线
			float xx = (float) (width * 1 / 3);
			canvas.drawLine(xx, 0, xx, height, paint);
			xx = (float) (width * 2 / 3);
			canvas.drawLine(xx, 0, xx, height, paint);
		}

		canvas.restore();
	}

}
