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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fenghuo.adapter.InfoAdapter;
import com.fenghuo.http.GetHttp;
import com.fenghuo.pojo.Content;
import com.fenghuo.utils.UtilsConstant;

public class DetailActivity extends Activity implements OnClickListener {
	ImageView back, share;
	TextView title;
	ListView lv;
	InfoAdapter adapter;
	Intent it;
	int uid, theme;
	JSONObject json;
	List<Content>  list;
	Handler  hd = new Handler(new Handler.Callback() {
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			list = (List<Content>) msg.obj;
			
			System.out.println(list.toString()+"$$$$$$$$$$$$contentlist");
			
			String t= list.get(0).getTitle();
			
			title.setText(t);
			
			adapter = new InfoAdapter(DetailActivity.this, list);
			
			lv.setAdapter(adapter);
			
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		back = (ImageView) findViewById(R.id.iv_back);
		share = (ImageView) findViewById(R.id.iv_share);
		title = (TextView) findViewById(R.id.tv_title);
		lv = (ListView) findViewById(R.id.lv_detail);
		
		list = new ArrayList<Content>();
		
		

		it = getIntent();

		uid = it.getIntExtra("uid", -1);

		theme = it.getIntExtra("theme", -1);

		json = new JSONObject();

		try {
			json.put("key", "content");

			json.put("uid", uid);
			
			json.put("theme", theme);
			
			
			System.out.println(json.toString()+"&&&&&&&&&&&&&");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				GetHttp get = new GetHttp();
				try {
					String str = get.getToTheInternet(json, UtilsConstant.contentpath,UtilsConstant.MyPoint);
					
					System.out.println(str+"ajfjewjpjfdsjf");
					
					list = UtilsConstant.getStringToContent(str);
					
					System.out.println(list.toString()+"$$$$$$$$$$$content");
					
					Message msg = new Message();
					
					msg.obj = list;
					
					hd.sendMessage(msg);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}).start();
		
		
		
		
		// adapter = new InfoAdapter(DetailActivity.this);

		back.setOnClickListener(this);
		share.setOnClickListener(this);

		

	}



	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.iv_back:

			finish();

			break;
		case R.id.iv_share:

			break;

		default:
			break;
		}
	}

}
