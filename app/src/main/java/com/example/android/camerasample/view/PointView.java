package com.example.android.camerasample.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.android.camerasample.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * created by zyh
 * on 2019-09-19
 */

public class PointView extends View {
	private Paint inPaint;
	private Paint outPaint;
	private int inColor;
	private int outColor;
	private float outRaduisFinal;
	private float inRaduisfinal;
	private float inRadius;
	private float outRadius;
	private int alpha = 150;
	Timer timer;

	public PointView(Context context) {
		super(context);
		init(context);
	}

	public PointView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context) {
		inColor = 0xc82edfcf;
		outColor = 0x4c8edfcf;
		inRaduisfinal = dip2px(context, 6);
		inRadius = 1.2f*inRaduisfinal;
		outRaduisFinal = dip2px(context, 12);
		outRadius = 1.2f*outRaduisFinal;

		inPaint = new Paint();
		inPaint.setColor(inColor);
		inPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		inPaint.setAlpha(alpha);

		outPaint = new Paint();
		outPaint.setColor(outColor);
		outPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		outPaint.setAlpha(alpha-20);

		
		startTimer();
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PointView);
		if (typedArray != null) {
			inColor = typedArray.getColor(R.styleable.PointView_incolor, 0xc82edfcf);
			outColor = typedArray.getColor(R.styleable.PointView_outcolor, 0x4c8edfcf);
			inRaduisfinal = typedArray.getDimension(R.styleable.PointView_inradius, dip2px(context, 6));
			inRadius = 1.2f*inRaduisfinal;
			outRaduisFinal = typedArray.getInt(R.styleable.PointView_outradius, dip2px(context, 12));
			outRadius = 1.2f*outRaduisFinal;
			typedArray.recycle();
		}
	
		inPaint = new Paint();
		inPaint.setColor(inColor);
		inPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		inPaint.setAlpha(alpha);
		inPaint.setAntiAlias(true);

		outPaint = new Paint();
		outPaint.setColor(outColor);
		outPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		outPaint.setAlpha(alpha);
		outPaint.setAntiAlias(true);

		startTimer();
	}
	
	
	private void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (inRadius <= 0.8f * inRaduisfinal) {
					inRadius = 1.2f * inRaduisfinal;
				}
				inRadius=inRadius-0.5f;

				if (outRadius <= 0.8f * outRaduisFinal) {
					outRadius = 1.2f * outRaduisFinal;
				}
				outRadius=outRadius-0.5f;

			
				postInvalidate();
			}
		}, 10, 120);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		if(outRadius>=0.9f*outRaduisFinal&&outRadius<=1.2f*outRaduisFinal) {
			canvas.drawCircle(outRaduisFinal * 1.2f, outRaduisFinal * 1.2f, outRadius, outPaint);	
		}
//		if(inRadius>=0.9f*inRaduisfinal&&inRadius<=1.2f*inRaduisfinal) {
//			canvas.drawCircle(outRaduisFinal * 1.2f, outRaduisFinal * 1.2f, inRadius, inPaint);
//		}		
		canvas.drawCircle(outRaduisFinal * 1.2f, outRaduisFinal * 1.2f, inRadius, inPaint);
		canvas.restore();
	}

	public int getInColor() {
		return inColor;
	}

	public void setInColor(int inColor) {
		this.inColor = inColor;
	}

	public int getOutColor() {
		return outColor;
	}

	public void setOutColor(int outColor) {
		this.outColor = outColor;
	}

	public float getOutRaduisFinal() {
		return outRaduisFinal;
	}

	public void setOutRaduisFinal(float outRaduisFinal) {
		this.outRaduisFinal = outRaduisFinal;
	}

	public float getInRaduisfinal() {
		return inRaduisfinal;
	}

	public void setInRaduisfinal(float inRaduisfinal) {
		this.inRaduisfinal = inRaduisfinal;
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public void drawPointCircle(Canvas canvas, float touchX, float touchY) {
		canvas.save();
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		canvas.drawCircle(touchX, touchY, outRaduisFinal * 1.2f, outPaint);
		canvas.drawCircle(touchX, touchY, inRaduisfinal * 1.2f, inPaint);
		canvas.restore();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(timer!=null){
			timer.cancel();
		}
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		startTimer();
	}
	
}
