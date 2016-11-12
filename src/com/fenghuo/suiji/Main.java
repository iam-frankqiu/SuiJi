package com.fenghuo.suiji;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.fenghuo.adapter.FragAdapter;
import com.fenghuo.fragment.page01;
import com.fenghuo.fragment.page02;
import com.fenghuo.fragment.page03;
import com.fenghuo.utils.UtilsConstant;

public class Main extends FragmentActivity implements OnPageChangeListener {
	private ViewPager pager;
	ImageView tv_footprint, tv_fp, tv_me;
	MyApplication  my;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		my = (MyApplication) getApplication();

	

		System.out.println(my.getIndex()+ "*************mainindex");

		

		if (my.getIndex()!= 0) {

			try {

				UtilsConstant.json.put("uid", my.getIndex());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		pager = (ViewPager) findViewById(R.id.viewpager);
		List<Fragment> list = new ArrayList<Fragment>();

		list.add(new page01());
		list.add(new page02());
		list.add(new page03());

		FragAdapter FragAdapter = new FragAdapter(getSupportFragmentManager(),
				list);

		pager.setAdapter(FragAdapter);

		pager.setOnPageChangeListener(this);

		pager.setOffscreenPageLimit(2);

		tv_footprint = (ImageView) findViewById(R.id.tv_footprint);
		tv_fp = (ImageView) findViewById(R.id.tv_fp);
		tv_me = (ImageView) findViewById(R.id.tv_me);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		selected(arg0);
	}

	private void selected(int arg0) {
		// TODO Auto-generated method stub
		tv_footprint.setSelected(arg0 == 0);
		tv_fp.setSelected(arg0 == 1);
		tv_me.setSelected(arg0 == 2);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.tv_footprint:
			pager.setCurrentItem(0);
			break;
		case R.id.tv_fp:
			pager.setCurrentItem(1);
			break;
		case R.id.tv_me:
			pager.setCurrentItem(2);
			break;

		default:
			break;
		}
	}

	public interface MyTouchListener {
		public void onTouchEvent(MotionEvent event);
	}

	// 淇濆瓨MyTouchListener鎺ュ彛鐨勫垪琛�
	private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<Main.MyTouchListener>();

	/**
	 * 鎻愪緵缁橣ragment閫氳繃getActivity()鏂规硶鏉ユ敞鍐岃嚜宸辩殑瑙︽懜浜嬩欢鐨勬柟娉�
	 * 
	 * @param listener
	 */
	public void registerMyTouchListener(MyTouchListener listener) {
		myTouchListeners.add(listener);
	}

	/**
	 * 鎻愪緵缁橣ragment閫氳繃getActivity()鏂规硶鏉ュ彇娑堟敞鍐岃嚜宸辩殑瑙︽懜浜嬩欢鐨勬柟娉�
	 * 
	 * @param listener
	 */
	public void unRegisterMyTouchListener(MyTouchListener listener) {
		myTouchListeners.remove(listener);
	}

	/**
	 * 鍒嗗彂瑙︽懜浜嬩欢缁欐墍鏈夋敞鍐屼簡MyTouchListener鐨勬帴鍙�
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		for (MyTouchListener listener : myTouchListeners) {
			listener.onTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK&&event.getRepeatCount() ==0){
			return false;
		}
		return true;
	}
}
