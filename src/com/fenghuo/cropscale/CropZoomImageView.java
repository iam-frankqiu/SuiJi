package com.fenghuo.cropscale;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class CropZoomImageView extends ImageView implements
OnScaleGestureListener, OnTouchListener,
ViewTreeObserver.OnGlobalLayoutListener{

	private GestureDetector mGestureDetector;
	private boolean isAutoScale;
	private final Matrix mScaleMatrix = new Matrix();

	private int lastPointerCount;
	private float mLastX;
	private float mLastY;

	private int  mTouchSlop;

	private boolean isCanDrag;


	private ScaleGestureDetector mscaleGestureDetector= null;
	/**
	 * 用于存放矩阵的9个值
	 */
	private final float[] matrixValues = new float[9];

	public static float SCALE_MAX = 4.0f;
	private static float SCALE_MID = 2.0f;

	/**
	 * 水平方向与View的边距
	 */
	private int mHorizontalPadding;//——屏幕边缘离截图区的宽度
	/**
	 * 垂直方向与View的边距
	 */
	private int mVerticalPadding;//——屏幕顶部离截图区的高度

	/**
	 * 初始化时的缩放比例，如果图片宽或高大于屏幕，此值将小于0
	 */
	private float initScale = 1.0f;
	private boolean once = true;





	public CropZoomImageView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public CropZoomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setScaleType(ScaleType.MATRIX);
		mGestureDetector = new GestureDetector(context,new SimpleOnGestureListener(){
			@Override
			public boolean onDoubleTap(MotionEvent e)
			{
				if (isAutoScale == true)
					return true;

				float x = e.getX();
				float y = e.getY();
				if (getScale() < SCALE_MID)
				{
					CropZoomImageView.this.postDelayed(
							new AutoScaleRunnable(SCALE_MID, x, y), 16); 

					isAutoScale = true;
				} else
				{
					CropZoomImageView.this.postDelayed(
							new AutoScaleRunnable(initScale, x, y), 16);
					isAutoScale = true;
				}

				return true;
			}
		});
		mscaleGestureDetector = new ScaleGestureDetector(context, this);
		this.setOnTouchListener(this);
	}

	//	zheshi neibulei ?
	/**
	 * 自动缩放的任务
	 * 
	 * 
	 * 
	 */
	private class AutoScaleRunnable implements Runnable{

		static final float BIGGER = 1.07f;
		static final float SMALLER = 0.93f;
		private float mTargetScale;
		private float tmpScale;

		/**
		 * 缩放的中心
		 */
		private float x;
		private float y;

		/**
		 * 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小
		 * 
		 * @param targetScale
		 */

		/**
		 * 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小
		 * 
		 * @param targetScale
		 */
		public AutoScaleRunnable(float targetScale, float x, float y){

			this.mTargetScale = targetScale;
			this.x = x;
			this.y = y;

			if(getScale()<mTargetScale)
			{
				tmpScale = BIGGER;
			}else {
				tmpScale = SMALLER;
			}
		}


		@Override
		public void run(){
			// 进行缩放
			System.out.println("run=============");
			mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
			checkBorder();
			setImageMatrix(mScaleMatrix);
			float currentscale = getScale();
			// 如果值在合法范围内，继续缩放
			if(((tmpScale > 1f)&&currentscale < mTargetScale)||((tmpScale < 1f)&&currentscale < mTargetScale)){

				CropZoomImageView.this.postDelayed(this, 16);

			}else{
				// 设置为目标的缩放比例
				float deltascale = mTargetScale/currentscale;
				mScaleMatrix.postScale(deltascale, deltascale, x, y);
				checkBorder();
				setImageMatrix(mScaleMatrix);
				isAutoScale = false;
			}



		}

	}

	public boolean onScale(ScaleGestureDetector detector){

		float scale = getScale();
		float factor = detector.getScaleFactor();

		if (getDrawable() == null)
			return true;
		/**
		 * 缩放的范围控制
		 */

		if((scale<SCALE_MAX &&factor>1.0f)||(scale > SCALE_MID && factor < 1.0f)){

			/**
			 * 最大值最小值判断
			 */
			if (factor * scale < initScale)
			{
				factor = initScale / scale;
			}
			if (factor * scale > SCALE_MAX)
			{
				factor = SCALE_MAX / scale;
			}
			/**
			 * 设置缩放比例
			 */
			mScaleMatrix.postScale(factor, factor, detector.getFocusX(), detector.getFocusY());
			checkBorder();
			setImageMatrix(mScaleMatrix);

		}

		return true;



	}



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		if(mGestureDetector.onTouchEvent(event)){


			return true;
		}
		mscaleGestureDetector.onTouchEvent(event);

		float x = 0;
		float y = 0;

		int pointerCount = event.getPointerCount();

		for(int i = 0; i < pointerCount;i++){

			x += event.getX();
			y += event.getY();

		}
		x = x/pointerCount;
		y = y/pointerCount;
		/**
		 * 每当触摸点发生变化时，重置mLasX , mLastY
		 */
		if (pointerCount != lastPointerCount)
		{
			isCanDrag = false;
			mLastX = x;
			mLastY = y;
		}

		lastPointerCount = pointerCount;	switch (event.getAction())
		{
		case MotionEvent.ACTION_MOVE:
			float dx = x - mLastX;
			float dy = y - mLastY;

			if (!isCanDrag)
			{
				isCanDrag = isCanDrag(dx, dy);
			}
			if (isCanDrag)
			{
				if (getDrawable() != null)
				{

					RectF rectF = getMatrixRectF();
					// 如果宽度小于屏幕宽度，则禁止左右移动
					if (rectF.width() <= getWidth() - mHorizontalPadding * 2)
					{
						dx = 0;
					}
					// 如果高度小雨屏幕高度，则禁止上下移动
					if (rectF.height() <= getHeight() - mVerticalPadding * 2)
					{
						dy = 0;
					}
					mScaleMatrix.postTranslate(dx, dy);
					checkBorder();
					setImageMatrix(mScaleMatrix);
				}
			}
			mLastX = x;
			mLastY = y;
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			lastPointerCount = 0;
			break;
		}




		return true;
	}


	@Override
	public boolean onScaleBegin(ScaleGestureDetector arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onScaleEnd(ScaleGestureDetector arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 根据当前图片的Matrix获得图片的范围
	 * 
	 * @return
	 */
	private RectF getMatrixRectF()
	{
		Matrix matrix = mScaleMatrix;
		RectF rect = new RectF();
		Drawable d = getDrawable();
		if (null != d)
		{
			rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(rect);
		}
		return rect;
	}


	/**
	 * 是否是拖动行为
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */
	private boolean isCanDrag(float dx, float dy)
	{
		return Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
	}

	public void setHorizontalPadding(int mHorizontalPadding)
	{ 
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 获得当前的缩放比例
	 * 
	 * @return
	 */
	public final float getScale()
	{
		mScaleMatrix.getValues(matrixValues);
		return matrixValues[Matrix.MSCALE_X];
	}
	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		//		当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时，所要调用的回调函数的接口类
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	/**
	 * 边界检测
	 */
	private void checkBorder(){

		RectF rect = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int height = getHeight();

		if(rect.width()+0.01>=width-2*mHorizontalPadding){
			if(rect.left>mHorizontalPadding){

				deltaX = -rect.left+mHorizontalPadding;
			}if(rect.right<width-mVerticalPadding){

				deltaX = width-rect.right-mHorizontalPadding;
			}

		}
		if (rect.height() + 0.01 >= height - 2 * mVerticalPadding)
		{
			if (rect.top > mVerticalPadding)
			{
				deltaY = -rect.top + mVerticalPadding;
			}
			if (rect.bottom < height - mVerticalPadding)
			{
				deltaY = height - mVerticalPadding - rect.bottom;
			}
		}
		mScaleMatrix.postTranslate(deltaX, deltaY);



	}
	public void onGlobalLayout(){
		if(once)
		{
			Drawable d = getDrawable();
			if(d==null){
				return;
			}
			// 垂直方向的边距
						mVerticalPadding = (getHeight() - (getWidth() - 2 * mHorizontalPadding)) / 2;
						int width = getWidth();
						int height = getHeight();
						// 拿到图片的宽和高
						int dw = d.getIntrinsicWidth();
						int dh = d.getIntrinsicHeight();
						float scale = 1.0f;
						if (dw <= getWidth() - mHorizontalPadding * 2&& dh >=getHeight() - mVerticalPadding * 2)
						{//如果图片的宽度<截图区宽度，并且图片高度>截图区高度时
							scale = (getWidth() * 1.0f - mHorizontalPadding * 2) / dw;
						}
						if (dh <= getHeight() - mVerticalPadding * 2&& dw >= getWidth() - mHorizontalPadding * 2)
						{//如果图片的高度<截图区高度，并且图片宽度>截图区宽度时
							scale = (getHeight() * 1.0f - mVerticalPadding * 2) / dh;
						}
						if (dw <= getWidth() - mHorizontalPadding * 2&& dh <= getHeight() - mVerticalPadding * 2)
						{//如果图片的高度<截图区高度，并且图片宽度<截图区宽度时
							float scaleW = (getWidth() * 1.0f - mHorizontalPadding * 2)/ dw;
							float scaleH = (getHeight() * 1.0f - mVerticalPadding * 2) / dh;
							scale = Math.max(scaleW, scaleH);
						}
						initScale = scale;
						SCALE_MID = initScale * 2;
						SCALE_MAX = initScale * 4;
						mScaleMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);//平移至屏幕中心
						mScaleMatrix.postScale(scale, scale, getWidth() / 2,getHeight() / 2);//设置缩放比例
						// 图片移动至屏幕中心
						setImageMatrix(mScaleMatrix);
						once = false;
		}
		
		
	}
	/**
	 * 剪切图片，返回剪切后的bitmap对象
	 * 
	 * @return
	 */
	public Bitmap crop()
	{
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		//若为裁剪为方形区域，则去掉getCircleBitmap方法即可
		return getCircleBitmap(Bitmap.createBitmap(bitmap, mHorizontalPadding,
				mVerticalPadding, getWidth() - 2 * mHorizontalPadding,
				getWidth() - 2 * mHorizontalPadding));
	}


	/**
	 * 将方形bitmap转换为圆形bitmap
	 * @param bitmap
	 * @return
	 */
	private Bitmap getCircleBitmap(Bitmap bitmap) {  
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output); 
        final int color = 0xff424242; 
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        Paint paint =new Paint();
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color); 
        int x = bitmap.getWidth(); 
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint); 
        return output;
	}

}

