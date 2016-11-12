package com.fenghuo.suiji;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.fenghuo.dao.UserDAO;
import com.fenghuo.http.GetHttp;
import com.fenghuo.utils.UtilsConstant;

public class Login extends Activity implements OnClickListener {
	UserDAO dao = new UserDAO();
	private EditText login_number, login_pass;
	private Button login_newuser, login, login_forget;
	private CheckBox remember;
	private String e1, e2;
	boolean flag = false;
	private Intent it, it1, it2;
	private String str;
	JSONObject json;
	MyApplication my;
	Handler hd = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String ss = (String) msg.obj;
			if (ss.equals(R.string.user_not_register)) {
				Toast.makeText(Login.this, ss, 0).show();
			} else {
				// UtilsConstant.index = Integer.parseInt(ss);
//				String message = "锟斤拷锟斤拷晒锟斤拷锟�;

				my.setIndex(Integer.parseInt(ss));
				if (my.getIndex() == 0) {

//					message = "锟斤拷锟斤拷失锟斤拷!";
//					message = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷耄�;
					Toast.makeText(Login.this, R.string.pass_error, 0).show();
				} else {
					flag = true;

					SharedPreferences sharedPreferences = getSharedPreferences(
							"user", Context.MODE_PRIVATE);

					Editor editor = sharedPreferences.edit();

					editor.remove("pnumber");

					editor.remove("pass");

					editor.putInt("index", my.getIndex());
					
					editor.commit();

					if (remember.isChecked()) {

						editor.putString("pnumber", login_number.getText()
								.toString());

						editor.putString("pass", login_pass.getText()
								.toString());

						editor.commit();

					}

					startActivity(it2);
				
				}
				Toast.makeText(Login.this, R.string.input_succeed, 0).show();

			}

			return false;
		}
	});

	// key: input ,forget ,register

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		login_number = (EditText) findViewById(R.id.login_number);
		login_pass = (EditText) findViewById(R.id.login_pass);
		login_newuser = (Button) findViewById(R.id.login_newuser);
		login = (Button) findViewById(R.id.login);
		remember = (CheckBox) findViewById(R.id.login_remember);
		login_forget = (Button) findViewById(R.id.login_forget);

		my = (MyApplication) getApplication();

		it2 = new Intent(Login.this, Main.class);

		it1 = new Intent(Login.this, Resetpass.class);

		it = new Intent(Login.this, Register.class);

		SharedPreferences sharedPreferences = getSharedPreferences("user",
				Context.MODE_PRIVATE);

		String s1 = sharedPreferences.getString("pnumber", "");

		String s2 = sharedPreferences.getString("pass", "");

		if (!s1.equals("") && !s2.equals("")) {
			login_number.setText(s1);
			login_pass.setText(s2);
		}

		login_newuser.setOnClickListener(this);
		login.setOnClickListener(this);

		login_forget.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_newuser:
			// 锟斤拷转锟斤拷锟斤拷锟斤拷锟斤拷

			startActivity(it);
			Login.this.overridePendingTransition(R.anim.in_leftright,R.anim.alphamove);

			break;
		case R.id.login:
			e1 = login_number.getText().toString();
			e2 = login_pass.getText().toString();

			if (TextUtils.isEmpty(e1)) {

//				str = "锟斤拷锟斤拷锟斤拷锟剿号ｏ拷";
				Toast.makeText(Login.this, R.string.number_can_not_empty, 0).show();

			} else if (TextUtils.isEmpty(e2)) {

//				str = "锟斤拷锟斤拷锟斤拷锟斤拷锟诫！";

				Toast.makeText(Login.this, R.string.input_pass, 0).show();
				
			} else {
				json = new JSONObject();

				try {
					json.put("pnumber", e1);
					json.put("pass", e2);
					json.put("key", "input");

					System.out.println(json.toString() + "&&&json");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							GetHttp http = new GetHttp();

							try {
								str = http.getToTheInternet(json,
										UtilsConstant.userpath,
										UtilsConstant.MyPoint);

								System.out.println(str + "&&&&&&&&&");

								Message mes = new Message();

								mes.obj = str;

								hd.sendMessage(mes);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}).start();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;

		case R.id.login_forget:
			// 璺宠浆鍒颁慨鏀瑰瘑鐮佺殑鐣岄潰

			startActivity(it1);
			Login.this.overridePendingTransition(R.anim.in_leftright,R.anim.alphamove);
			break;

		default:
			break;
		}
	}

}
