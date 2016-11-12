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
import android.view.Menu;
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
//<<<<<<< .mine
//=======
//import com.fenghuo.http.GetHttp;
//>>>>>>> .r42

public class Register extends Activity implements OnClickListener {

	private EditText et_register_pnum, et_register_code, et_register_passwords,
			et_register_password;
	private Button register, btn_register_getcode;
	JSONObject json;
	private String e1, e2, e3, e4;
	private int i;
	private Timer timer;
	private ImageView fanhui;
	String s;
	private static String APPKEY = "c973793c1d2c";// 463db7238681 27fe7909f8e8
	// // 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "cbf31ac1a624ff57d6a38fb52dd752f3";
	// private static String APPKEY = "c561c744c8e0";
	// private static String APPSECRET = "e358eb56917f9e5957c74b8c670b3b64";
	public String phString; // 3684fd4f0e16d68f69645af1c7f8f5bd
	UserDAO dao = new UserDAO();
	Intent it;

	Handler hd = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				i = (Integer) msg.obj;
				btn_register_getcode.setText(i + R.string.second);
				// tv_miunte.setText(i+"秒");
				if (i == 0) {
					timer.cancel();
					btn_register_getcode.setText(R.string.re_get);
					// tv_miunte.setText("请重新获取");
				}
				break;

			default:
				break;
			}

			return false;
		}
	});
	Handler hand = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String ss = (String) msg.obj;

			Toast.makeText(Register.this, ss, 0).show();

			if (s.equals(R.string.register_success)) {
				startActivity(it);
			}

			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SMSSDK.initSDK(this, APPKEY, APPSECRET);

		setContentView(R.layout.register);
		et_register_pnum = (EditText) findViewById(R.id.et_register_pnum);
		et_register_code = (EditText) findViewById(R.id.et_register_code);
		et_register_password = (EditText) findViewById(R.id.et_register_password);
		et_register_passwords = (EditText) findViewById(R.id.et_register_passwords);
		register = (Button) findViewById(R.id.register);
		btn_register_getcode = (Button) findViewById(R.id.btn_register_getcode);
		// tv_register_01 = (TextView) findViewById(R.id.tv_register_01);
		// tv_register_02 = (TextView) findViewById(R.id.tv_register_02);

		fanhui = (ImageView) findViewById(R.id.register_fanhui);

		it = new Intent(Register.this, Login.class);
		btn_register_getcode.setOnClickListener(this);
		register.setOnClickListener(this);
		fanhui.setOnClickListener(this);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register_getcode:
			e1 = et_register_pnum.getText().toString();

			if (!TextUtils.isEmpty(et_register_pnum.getText())) {
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
		case R.id.register:
			// JSONObject jsonObject = new JSONObject();
			e2 = et_register_code.getText().toString();

			e3 = et_register_password.getText().toString();

			e4 = et_register_passwords.getText().toString();

			// String str = ;

			if (i == 0) {
				Toast.makeText(Register.this, R.string.run_out_of_time, 0)
						.show();
			} else {
				if (TextUtils.isEmpty(e2)) {
					// str = ;
					Toast.makeText(this, R.string.code_can_not_is_empty, 0)
							.show();

					// <<<<<<< .mine
				} else if (TextUtils.isEmpty(e3) || TextUtils.isEmpty(e4)) {
					// str = "";
					Toast.makeText(this, R.string.pass_can_not_is_empty, 0)
							.show();
				} else if (!e3.equals(e4)) {
					// str = "";
					Toast.makeText(this, R.string.pass_is_different, 0).show();
				} else {

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
					btn_register_getcode.setText(R.string.get);
					// Toast.makeText(getApplicationContext(), "注册成功",
					// Toast.LENGTH_SHORT).show();
					json = new JSONObject();
					try {

						json.put("pnumber", e1);
						json.put("pass", e3);
						json.put("key", "register");
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub

								GetHttp http = new GetHttp();

								try {
									s = http.getToTheInternet(json,
											UtilsConstant.userpath, "input");

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
					Toast.makeText(getApplicationContext(),
							R.string.code_already_send, Toast.LENGTH_SHORT)
							.show();
				}
			} else {

				((Throwable) data).printStackTrace();
				int resId = getStringRes(Register.this, "smssdk_network_error");
				Toast.makeText(Register.this, R.string.code_error,
						Toast.LENGTH_SHORT).show();
				if (resId > 0) {
					Toast.makeText(Register.this, resId, Toast.LENGTH_SHORT)
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
