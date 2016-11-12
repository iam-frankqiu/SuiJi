package com.fenghuo.suiji;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class FeedBack extends Activity implements OnClickListener {
	private ImageButton fb_back;
	private EditText fb_contact, fb_advise;
	private Button fb_commit;
	private String email;
	String contact;
	String advise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);

		fb_back = (ImageButton) findViewById(R.id.fb_back);
		fb_contact = (EditText) findViewById(R.id.fb_contact);
		fb_advise = (EditText) findViewById(R.id.fb_advise);
		fb_commit = (Button) findViewById(R.id.fb_commit);

		fb_commit.setOnClickListener(this);
		fb_back.setOnClickListener(this);

	}

	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fb_back:
			finish();
			break;
		case R.id.fb_commit:
			// 鎶婂唴瀹规彁浜ゅ埌閭涓紝骞舵樉绀烘椂闂�
			contact = fb_contact.getText().toString();

			advise = fb_advise.getText().toString();

			break;
		default:
			break;
		}
	}
}
