package com.fenghuo.suiji;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.fenghuo.adapter.MainAdapter;
import com.fenghuo.http.GetHttp;
import com.fenghuo.pojo.Show;
import com.fenghuo.utils.UtilsConstant;

public class MyActivity extends Activity implements OnItemClickListener {
	private ListView lv;
	List<Show> list;
	MainAdapter adapter;
	Intent it;
	JSONObject  json;
	MyApplication  my;
	ImageView  image_return;
	Handler hd = new Handler(new Handler.Callback() {

		@SuppressWarnings("unchecked")
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			list = (List<Show>) msg.obj;

			DisplayMetrics dm = new DisplayMetrics();

			getWindowManager().getDefaultDisplay().getMetrics(dm);

			int screenWidth = dm.widthPixels;

			// ���ڸ߶�
			int screenHeight = dm.heightPixels;

			adapter = new MainAdapter(MyActivity.this, list, screenHeight,
					screenWidth);

			System.out.println(screenWidth + "*****************width"
					+ screenHeight + "&&&&&&&&&&&&height");

			lv.setAdapter(adapter);

			return false;
		}
	});
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);

		lv = (ListView) findViewById(R.id.lv_my);

		list = new ArrayList<Show>();
		
		image_return = (ImageView) findViewById(R.id.image_return);
		
		image_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		my = (MyApplication) getApplication();
		
		json = new JSONObject();

		it = new Intent(MyActivity.this, DetailActivity.class);

		try {
			json.put("key", "myshow");
			
			json.put("uid", my.getIndex());

//			UtilsConstant.json.put("uid", 404);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		getMyThings();

		lv.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		Show s = list.get(arg2);
		int theme = s.getTheme();
		int uid = s.getUid();

		it.putExtra("theme", theme);
		it.putExtra("uid", uid);

		startActivity(it);

	}

	private void getMyThings() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				GetHttp get = new GetHttp();

				try {

					String str = get.getToTheInternet(json,
							UtilsConstant.contentpath, "input");

					list = UtilsConstant.getStringToList(str);

					Message msg = new Message();

					msg.obj = list;

					hd.sendMessage(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

}
