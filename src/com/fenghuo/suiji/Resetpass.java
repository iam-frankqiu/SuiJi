package com.fenghuo.suiji;

import static cn.smssdk.framework.utils.R.getStringRes;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.fenghuo.dao.UserDAO;
import com.fenghuo.http.GetHttp;
import com.fenghuo.utils.UtilsConstant;

public class Resetpass extends Activity implements OnClickListener {
	Handler hand = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String ss = (String) msg.obj;

			Toast.makeText(Resetpass.this, ss, 0).show();

			if (s.equals(R.string.reset_succeed)) {
				startActivity(it);
			}

			return false;
		}
	});
	private EditText reset_number, reset_code, reset_password, reset_passwords;
	private Button reset, reset_getcode;
	private String e1, e2, e3, e4;
	UserDAO dao = new UserDAO();
	JSONObject json;
	String str, s;
	boolean flag;
	private Intent it;
	private int i;
	private Timer timer;
	private static String APPKEY = "c973793c1d2c";// 463db7238681 27fe7909f8e8
	// // 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "cbf31ac1a624ff57d6a38fb52dd752f3";
	// private static String APPKEY = "c561c744c8e0";
	// private static String APPSECRET = "e358eb56917f9e5957c74b8c670b3b64";
	public String phString; // 3684fd4f0e16d68f69645af1c7f8f5bd

	Handler hd = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub

			if (flag) {
				switch (msg.what) {
				case 0:
					i = (Integer) msg.obj;
					reset_getcode.setText(i + R.string.second);
					// tv_miunte.setText(i+"秒");
					if (i == 0) {
						timer.cancel();
						reset_getcode.setText(R.string.re_get);
						// tv_miunte.setText("请重新获取");
					}
					break;

				default:
					break;
				}
			}

			return false;
		}
	});
	Handler hd2 = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			str = (String) msg.obj;

			System.out.println(str + "%%%%%%first");

			Toast.makeText(Resetpass.this, str, 0).show();

			if (str.equals(R.string.sending_code)) {
				flag = true;
			}

			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		setContentView(R.layout.resetpass);
		ImageView fanhui = (ImageView) findViewById(R.id.register_fanhui);
		reset_number = (EditText) findViewById(R.id.reset_number);
		reset_code = (EditText) findViewById(R.id.reset_code);
		reset_password = (EditText) findViewById(R.id.reset_password);
		reset_passwords = (EditText) findViewById(R.id.reset_passwords);
		reset = (Button) findViewById(R.id.reset);
		reset_getcode = (Button) findViewById(R.id.reset_getcode);

		reset.setOnClickListener(this);
		reset_getcode.setOnClickListener(this);

		fanhui.setOnClickListener(this);

		str = null;

		flag = false;

		it = new Intent(Resetpass.this, Login.class);

		EventHandler eh = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {

				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}

		};
		SMSSDK.registerEventHandler(eh);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reset_getcode:
			e1 = reset_number.getText().toString();
			json = new JSONObject();
			try {
				json.put("pnumber", e1);
				json.put("pass", "");
				json.put("key", "reset");
			} catch (JSONException e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					GetHttp get = new GetHttp();

					try {
						String s1 = get.getToTheInternet(json,
								UtilsConstant.userpath, UtilsConstant.MyPoint);

						// System.out.println(s1 + "!!!!!!!!s1");

						Message msg = new Message();

						msg.obj = s1;

						// System.out.println(msg.toString() + "!!!!!!obj");

						hd2.sendMessage(msg);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();

			if (!TextUtils.isEmpty(reset_number.getText())) {
				i = 60;
				// 累计一分钟
				timer = new Timer();

				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						i--;

						Message msg = new Message();

						msg.obj = i;

						msg.what = 0;

						hd.sendMessage(msg);
					}
				}, 0, 1000);
				// 获取验证码
				SMSSDK.getVerificationCode("86", e1);

			} else {
				Toast.makeText(this, R.string.input_error, 0).show();
			}

			break;
		case R.id.reset:

			e2 = reset_code.getText().toString();
			e3 = reset_password.getText().toString();
			e4 = reset_passwords.getText().toString();

//			String str = "已经超时";

			if (i == 0) {
				Toast.makeText(Resetpass.this, R.string.run_out_of_time, 0).show();
			} else {
				if (TextUtils.isEmpty(e2)) {
//					str = "验证码不能为空";
					Toast.makeText(this, R.string.code_can_not_is_empty, 0).show();

				} else if (TextUtils.isEmpty(e3) || TextUtils.isEmpty(e4)) {
//					str = "密码不能为空";
					Toast.makeText(this, R.string.pass_can_not_is_empty, 0).show();
				} else if (!e3.equals(e4)) {
//					str = "两次密码输入不一致";
					Toast.makeText(this, R.string.pass_is_different, 0).show();
				} else {

					// Toast.makeText(this, str, 0).show();

					SMSSDK.submitVerificationCode("86", e1, e2);
				}
			}

			break;
		case R.id.register_fanhui:
			finish();
			break;

		default:
			break;
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// 根据发送过来的消息判断验证码是否发送以及验证是否成功
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event=" + event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					// 验证成功取消timer计时器，并且是时间显示取消
					timer.cancel();
					// tv_miunte.setVisibility(View.GONE);
					// 提交验证码成功
					reset_getcode.setText(R.string.get);
					// Toast.makeText(getApplicationContext(), "注册成功",
					// Toast.LENGTH_SHORT).show();
					json = new JSONObject();
					try {

						json.put("pnumber", e1);
						json.put("pass", e3);
						json.put("key", "other");
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub

								GetHttp http = new GetHttp();

								try {
									s = http.getToTheInternet(json,
											UtilsConstant.userpath,
											UtilsConstant.MyPoint);

									Message msg = new Message();

									msg.obj = s;

									hand.sendMessage(msg);

									// System.out.println(s);

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}).start();

						// System.out.println(json.toString()+"********");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Toast.makeText(getApplicationContext(), R.string.code_already_send,
							Toast.LENGTH_SHORT).show();
				}
			} else {

				((Throwable) data).printStackTrace();
				int resId = getStringRes(Resetpass.this, "smssdk_network_error");
				Toast.makeText(Resetpass.this, R.string.code_error, Toast.LENGTH_SHORT)
						.show();
				if (resId > 0) {
					Toast.makeText(Resetpass.this, resId, Toast.LENGTH_SHORT)
							.show();
				}
			}

		}

	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}

}
