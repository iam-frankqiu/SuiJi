package com.fenghuo.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.fenghuo.adapter.MainAdapter;
import com.fenghuo.http.GetHttp;
import com.fenghuo.pojo.Show;
import com.fenghuo.suiji.DetailActivity;
import com.fenghuo.suiji.Login;

import com.fenghuo.suiji.MyActivity;
import com.fenghuo.suiji.MyApplication;

import com.fenghuo.suiji.Main;

import com.fenghuo.suiji.R;
import com.fenghuo.utils.UtilsConstant;

public class page01 extends Fragment implements OnRefreshListener,
		OnItemClickListener {

	private ListView lv_main;
	private SwipeRefreshLayout refreshlayout;
	private float ver_y = 0;
	MainAdapter adapter;
	Intent it;
	Intent it1;
	List<Show> list;
	JSONObject json;
	// TextView tv_input;
	MyApplication my;
	private static final int REFRESH_COMPLETEE = 0X100;

	Handler hd = new Handler(new Handler.Callback() {

		@SuppressWarnings("unchecked")
		@Override
		public boolean handleMessage(Message msg) {

			// if(msg.what==0){
			//
			// }

			list = (List<Show>) msg.obj;

			System.out.println(list.toString() + "&&&&&&&&&&7success");

			DisplayMetrics dm = new DisplayMetrics();

			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

			int screenWidth = dm.widthPixels;

			// 窗口高度
			int screenHeight = dm.heightPixels;

			adapter = new MainAdapter(getActivity(), list, screenHeight,
					screenWidth);
			lv_main.setAdapter(adapter);

			return false;
		}
	});

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETEE:

				getThings();

				// adapter.notifyDataSetChanged();
				refreshlayout.setRefreshing(false);
				break;

			default:
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.page_01, container, false);

		// Fragment涓紝娉ㄥ唽
		// 鎺ユ敹MainActivity鐨凾ouch鍥炶皟鐨勫璞�
		// 閲嶅啓鍏朵腑鐨刼nTouchEvent鍑芥暟锛屽苟杩涜璇ragment鐨勯�杈戝鐞�

		Main.MyTouchListener myTouchListener = new Main.MyTouchListener() {
			@Override
			public void onTouchEvent(MotionEvent event) {
				// 澶勭悊鎵嬪娍浜嬩欢
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 璁板綍y鍊�
					ver_y = event.getRawY();

					break;
				case MotionEvent.ACTION_UP:
					float dis_y = event.getRawY() - ver_y;

					if (dis_y > 5) {
						System.out.println("zhixing" + dis_y);

					}
					break;

				default:
					break;
				}
			}
		};

		// 灏唌yTouchListener娉ㄥ唽鍒板垎鍙戝垪琛�
		((Main) this.getActivity()).registerMyTouchListener(myTouchListener);

		it = new Intent(getActivity(), Login.class);
		// tv_input = (TextView) view.findViewById(R.id.tv_input);
		// tv_input.setBackground(background)

		my = (MyApplication) getActivity().getApplication();

		System.out.println(my.getIndex() + "*************page1");

		// if(my.getIndex()!=0){
		// tv_input.setVisibility(view.INVISIBLE);
		// }

		lv_main = (ListView) view.findViewById(R.id.lv_quan);
		refreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.widget_srl);

		list = new ArrayList<Show>();

		json = new JSONObject();

		try {
			json.put("key", "show");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		getThings();

		it1 = new Intent(getActivity(), DetailActivity.class);

		refreshlayout.setOnRefreshListener(this);
		refreshlayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		System.out.println(list.toString() + "&&&&&&&&&&&before");
		System.out.println("after))))))))))))0");

		// tv_input.setOnClickListener(this);
		lv_main.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessageDelayed(REFRESH_COMPLETEE, 1000);
	}

	public void getThings() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				GetHttp get = new GetHttp();
				try {

					String string = get.getToTheInternet(json,
							UtilsConstant.contentpath, UtilsConstant.MyPoint);

					System.out.println(string + "&&&&&&&&&&777string");

					list = UtilsConstant.getStringToList(string);

					Message msg = new Message();

					msg.obj = list;

					// msg.what=a;

					hd.sendMessage(msg);

					System.out.println(list.toString() + "$$$$$list");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

	// public boolean ontouchEvent(MotionEvent event){
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// // 璁板綍y鍊�
	// ver_y = event.getRawY();
	//
	// break;
	// case MotionEvent.ACTION_UP:
	// float dis_y = ver_y - event.getRawY();
	// break;
	//
	// default:
	// break;
	// }
	//
	//
	//
	// return true;
	//
	// }

	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// // it = new Intent(getActivity(),Login.class);
	// startActivityForResult(it, 0);
	//
	// getActivity().overridePendingTransition(R.anim.in_leftright,R.anim.alphamove);
	//
	// }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Show s = list.get(arg2);
		int theme = s.getTheme();
		int uid = s.getUid();

		it1.putExtra("theme", theme);
		it1.putExtra("uid", uid);

		startActivity(it1);

	}

}
