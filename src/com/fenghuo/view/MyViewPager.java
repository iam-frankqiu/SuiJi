package com.fenghuo.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager{
		private boolean scroll = true;
	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}



	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	public void setScroll(boolean scroll) {
		this.scroll = scroll;
	}
	
	@Override
	public void scrollTo(int x, int y) {
	// TODO Auto-generated method stub
	super.scrollTo(x, y);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		/*return false;//super.onTouchEvent(arg0);*/
		if(scroll)
			return false;
		else
			return super.onTouchEvent(arg0);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if(scroll)
			return false;
		else
			return super.onInterceptTouchEvent(arg0);
	}
	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}
	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}
}
