package com.fenghuo.suiji;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;

import android.app.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;

import android.text.TextUtils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup.LayoutParams;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
//import cn.sharesdk.framework.utils.UIHandler;
//
//import cn.sharesdk.wechat.friends.Wechat;

import com.fenghuo.utils.UtilsConstant;
import com.fenghuo.utils.UtilsTime;

public class Publish extends Activity implements PlatformActionListener,
		OnClickListener, Callback {

	private ImageView img_publish_back, img_publish_share, img_publish_add;
	private Button but_publish;
	private EditText et_publish_share;
	private Intent it1;
	private ImageView showimage;
	protected PlatformActionListener paListener;
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	private ImageView share1, share2;
	PopupWindow popWindow;

	Uri uri;
	Bitmap bitmap;
	String image, msg, photoSavePath, photoSaveName;
	PopupWindow inWindow;
	LayoutInflater inflater;
	TextView photograph, albums, setposition;
	LinearLayout cancel;
	String time, imgpath;
	AjaxParams params;
	boolean flag2;

	private LayoutParams layoutParams;
	private String title;
	private int theme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.publish);

		ShareSDK.initSDK(this);

		img_publish_back = (ImageView) findViewById(R.id.img_publish_back);
		img_publish_share = (ImageView) findViewById(R.id.img_publish_share);
		img_publish_add = (ImageView) findViewById(R.id.img_publish_add);
		but_publish = (Button) findViewById(R.id.but_publish);
		et_publish_share = (EditText) findViewById(R.id.et_publish_share);
		setposition = (TextView) findViewById(R.id.tv_setposition);

		flag2 = false;

		try {
			setposition.setText(UtilsConstant.json.getString("position"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file = new File(Environment.getExternalStorageDirectory(),
				"PublishPhoto/cache");
		if (!file.exists())
			file.mkdirs();
		photoSavePath = Environment.getExternalStorageDirectory()
				+ "/PublishPhoto/cache/";
		// photoSaveName = System.currentTimeMillis() + ".png";

		// photoSavePath = Environment.getExternalStorageDirectory()
		// + "/publishcocntent/cache/";
		// photoSaveName = System.currentTimeMillis() + ".png";

		inflater = (LayoutInflater) Publish.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// it1 = new Intent(Publish.this,Main.class);
		params = new AjaxParams();
		uri = null;
		bitmap = null;
		image = null;
		msg = "";

		img_publish_back.setOnClickListener(this);
		img_publish_add.setOnClickListener(this);
		img_publish_share.setOnClickListener(this);
		but_publish.setOnClickListener(this);

		@SuppressWarnings("deprecation")
		int height_sum = getWindowManager().getDefaultDisplay().getHeight();
		@SuppressWarnings("deprecation")
		int width_sum = getWindowManager().getDefaultDisplay().getWidth();
		layoutParams = img_publish_add.getLayoutParams();
		layoutParams.height = height_sum / 4;
		layoutParams.width = width_sum / 4;

		// 获取到地图界面传过来的theme（int），和title（String），准备在发布时传到服务器中
		Intent intent = getIntent();
		title = intent.getStringExtra("tit");
		theme = intent.getIntExtra("the", -1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_publish_back:
			finish();

			break;
		case R.id.img_publish_share:

			showPopupWindow(v);

			break;

		case R.id.img_publish_add:
			flag2 = true;

			inPopupWindow(v);

			break;

		case R.id.but_publish:

			msg = et_publish_share.getText().toString();

			Date now = new Date();

			time = UtilsTime.getTimeToString(now);

			try {
				UtilsConstant.json.put("msg", msg);

				UtilsConstant.json.put("image", image);

				UtilsConstant.json.put("time", time);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (flag2) {

			} else {
				if (TextUtils.isEmpty(msg)) {

					Toast.makeText(Publish.this, R.string.publish_is_empty, 0)
							.show();
				} else {

					// AjaxParams params2 = new AjaxParams();
					//
					// params2.put("no", UtilsConstant.json.toString());
					//
					// upload(params2);

				}
			}

			params.put("in", UtilsConstant.json.toString());

			upload(params);

//			finish();

			System.out.println(UtilsConstant.json.toString()
					+ "******************************json");

			System.out.println("@@@@@@@@@@takeaction0");
			System.out.println("@@@@@@@@@@takeaction1");

			break;
		default:
			break;
		}
	}

	protected String getAbsoluteImagePath(Uri uri) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	private void upload(AjaxParams params) {

		FinalHttp finalHttp = new FinalHttp();

		finalHttp.post(UtilsConstant.userpath, params,
				new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						// super.onSuccess(t);

						System.out.println(t + "success");

						Toast.makeText(Publish.this, t, 0).show();
						
						finish();

					}

				});
	}

	@SuppressWarnings("deprecation")
	private void inPopupWindow(View parent) {
		// TODO Auto-generated method stub
		if (inWindow == null) {
			View view = inflater.inflate(R.layout.pop_select_photo, null);

			inWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			initPop(view);
		}

		inWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		inWindow.setFocusable(true);
		inWindow.setOutsideTouchable(true);
		inWindow.setBackgroundDrawable(new BitmapDrawable());
		inWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		inWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

	}

	public void initPop(View view) {

		photograph = (TextView) view.findViewById(R.id.photograph);// 閿熸枻鎷烽敓鏂ゆ嫹
		albums = (TextView) view.findViewById(R.id.albums);// 閿熸枻鎷烽敓锟�
		cancel = (LinearLayout) view.findViewById(R.id.cancel);// 鍙栭敓鏂ゆ嫹

		photograph.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				inWindow.dismiss();
				photoSaveName = String.valueOf(System.currentTimeMillis())
						+ ".png";
				Uri uu = null;

				Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				uu = Uri.fromFile(new File(photoSavePath, photoSaveName));
				in.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				in.putExtra(MediaStore.EXTRA_OUTPUT, uu);
				startActivityForResult(in, 0);

			}
		});

		albums.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				inWindow.dismiss();
				Intent it = new Intent();
				it.setAction(Intent.ACTION_PICK);
				it.setType("image/*");
				startActivityForResult(it, 1);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				inWindow.dismiss();

			}
		});
	}

	/**
	 * 鍥剧墖閫夐敓鏂ゆ嫹閿熸枻鎷烽敓绉告枻鎷烽敓锟�
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != -1) {
			return;
		}
		Uri uri = null;
		switch (requestCode) {
		case 1:// 閿熸枻鎷烽敓锟�
			if (data == null) {
				return;
			}
			uri = data.getData();

			String[] proj = { MediaStore.Images.Media.DATA };
			@SuppressWarnings("deprecation")
			Cursor cursor = Publish.this.managedQuery(uri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			imgpath = cursor.getString(column_index);// 鍥剧墖閿熻妭纰夋嫹璺敓鏂ゆ嫹
			img_publish_add.setImageBitmap(getLoacalBitmap(imgpath));
			break;
		case 0:
			imgpath = photoSavePath + photoSaveName;

			uri = Uri.fromFile(new File(imgpath));
			// Bundle bundle = data.getExtras();
			// Bitmap bitmap = (Bitmap) bundle.get("data");
			img_publish_add.setImageURI(uri);
			break;

		default:
			break;
		}

		try {

			params.put("uploadfile", new File(imgpath));
			//
			//
			// System.out.println(getAbsoluteImagePath(uri)+"###########absolutepath");
			//
			//
			// System.out
			// .println(getAbsoluteImagePath(uri) + "888888888888888uri");

			// params.put("uploadfile", new File(getAbsoluteImagePath(uri)));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(imgpath + "$$$$$$$$$$$$$path");

		// img_publish_add.setImageBitmap(BitmapFactory.decodeFile(imgpath));

	}

	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onCancel(Platform platform, int action) {
		// 取消
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);

	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> arg2) {
		// 成功
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);

	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		// 失敗
		// 打印错误信息,print the error msg
		t.printStackTrace();
		// 错误监听,handle the error msg
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);

	}

	public boolean handleMessage(Message msg) {
		switch (msg.arg1) {
		case 1: {
			// 成功
			Toast.makeText(Publish.this, "分享成功", 10000).show();
			System.out.println("分享回调成功------------");
		}
			break;
		case 2: {
			// 失败
			Toast.makeText(Publish.this, "分享失败", 10000).show();
		}
			break;
		case 3: {
			// 取消
			Toast.makeText(Publish.this, "分享取消", 10000).show();
		}
			break;
		}

		return false;

	}

	private void shareTextToWechatFriend() throws JSONException {

		ShareParams wechat = new ShareParams();
		wechat.setTitle("随迹");
		wechat.setText(UtilsConstant.json.getString("msg"));
		wechat.setImageUrl("http://img03.sogoucdn.com/app/a/100520020/474f7f3044c966ac092f56be8a7ecf2e");
		wechat.setUrl("http://baidu.com");
		wechat.setShareType(Platform.SHARE_WEBPAGE);

		Platform weixin = ShareSDK.getPlatform(Publish.this, Wechat.NAME);
		weixin.setPlatformActionListener(Publish.this);
		weixin.share(wechat);
	}

	private void shareImageToWechatFriend() {
		ShareParams wechatMoments = new ShareParams();
		wechatMoments.setTitle("图片分享");
		wechatMoments.setText("content");
		wechatMoments.setImagePath(imgpath);
		// System.out.println(Environment.getExternalStorageDirectory()+
		// "ClipHeadPhoto/cache/1448854547294.png");
		// Toast.makeText(Publish.this,
		// Environment.getExternalStorageDirectory()+
		// "/ClipHeadPhoto/cache/1448854547294.png", 0).show();
		// wechatMoments.setUrl("http://mob.com");

		wechatMoments.setShareType(Platform.SHARE_IMAGE);
		Platform weixin = ShareSDK
				.getPlatform(Publish.this, WechatMoments.NAME);
		weixin.setPlatformActionListener(Publish.this);
		weixin.share(wechatMoments);
	}

	// private void shareMusicToWechatFriend() {
	// // 微信音乐分享到朋友圈
	// ShareParams wechatMoments = new ShareParams();
	// wechatMoments.setTitle("测试");
	// wechatMoments.setText("content");
	//
	// wechatMoments.setUrl("http://mob.com");
	// wechatMoments
	// .setImageUrl("http://img03.sogoucdn.com/app/a/100520020/474f7f3044c966ac092f56be8a7ecf2e");
	// wechatMoments
	// .setMusicUrl("http://mp3.sogou.com/tiny/song?tid=5f59a28023bd124c&query=%E7%BB%99%E6%88%91%E4%B8%80%E4%B8%AA%E7%90%86%E7%94%B1%E5%BF%98%E8%AE%B0&song_id=ss&song_name=%E7%BB%99%E6%88%91%E4%B8%80%E4%B8%AA%E7%90%86%E7%94%B1%E5%BF%98%E8%AE%B0&singer_name=A-Lin&album_name=%E5%AF%82%E5%AF%9E%E4%B8%8D%E7%97%9B");
	// wechatMoments.setShareType(Platform.SHARE_MUSIC);
	// Platform weixin = ShareSDK
	// .getPlatform(Publish.this, WechatMoments.NAME);
	// weixin.setPlatformActionListener(Publish.this);
	// weixin.share(wechatMoments);
	//
	// }

	@SuppressWarnings("deprecation")
	private void showPopupWindow(View parent) {
		if (popWindow == null) {
			View view = inflater.inflate(R.layout.pop_select_share, null);
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			initPopWindow(view);
		}
		popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	public void initPopWindow(View view) {
		share1 = (ImageView) view.findViewById(R.id.sharetofriend);
		share2 = (ImageView) view.findViewById(R.id.sharetofriends);
		share1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				// 执行分享到哪哪哪
				try {
					shareTextToWechatFriend();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		share2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				shareImageToWechatFriend();
			}
		});

	}

	private void shareImagesToWechatFriend() {
		// ArrayList<File> files = new ArrayList<File>(5);
		// File file1 = new File(path);

		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm",
				"com.tencent.mm.ui.tools.ShareToTimeLineUI");
		intent.setComponent(comp);
		intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		intent.setType("image/*");
		intent.putExtra("Kdescription", "title");
		ArrayList<Uri> imageUris = new ArrayList<Uri>();
		// for (File f : files) {
		// imageUris.add(Uri.fromFile(f));
		File f1 = new File(Environment.getExternalStorageDirectory()
				+ "/ClipHeadPhoto/cache/1448854547294.png");
		File f2 = new File(Environment.getExternalStorageDirectory()
				+ "/ClipHeadPhoto/cache/1448854547294.png");
		File f3 = new File(Environment.getExternalStorageDirectory()
				+ "/ClipHeadPhoto/cache/1448854547294.png");
		imageUris.add(Uri.fromFile(f1));
		imageUris.add(Uri.fromFile(f2));
		imageUris.add(Uri.fromFile(f3));

		// }
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
		startActivity(intent);

	}

	// @Override
	// public boolean handleMessage(Message msg) {
	// // TODO Auto-generated method stub
	// switch (msg.arg1) {
	// case 1: {
	// // 成功
	// Toast.makeText(Publish.this, "分享成功", 10000).show();
	// System.out.println("分享回调成功------------");
	// }
	// break;
	// case 2: {
	// // 失败
	// Toast.makeText(Publish.this, "分享失败", 10000).show();
	// }
	// break;
	// case 3: {
	// // 取消
	// Toast.makeText(Publish.this, "分享取消", 10000).show();
	// }
	// break;
	// }
	//
	// return false;
	// }
	//
	// @Override
	// public void onCancel(Platform arg0, int arg1) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onComplete(Platform arg0, int arg1, HashMap<String, Object>
	// arg2) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onError(Platform arg0, int arg1, Throwable arg2) {
	// // TODO Auto-generated method stub
	//
	// }

	// @Override
	// public void onCancel(Platform platform, int action) {
	// // TODO Auto-generated method stub
	// // 取消
	// Message msg = new Message();
	// msg.what = MSG_ACTION_CCALLBACK;
	// msg.arg1 = 3;
	// msg.arg2 = action;
	// msg.obj = platform;
	// UIHandler.sendMessage(msg, this);
	// }
	//
	// @Override
	// public void onComplete(Platform platform, int action, HashMap<String,
	// Object> arg2) {
	// // TODO Auto-generated method stub
	// // 成功
	// Message msg = new Message();
	// msg.what = MSG_ACTION_CCALLBACK;
	// msg.arg1 = 1;
	// msg.arg2 = action;
	// msg.obj = platform;
	// UIHandler.sendMessage(msg,this);
	// }
	//
	// @Override
	// public void onError(Platform platform, int action, Throwable t) {
	// // TODO Auto-generated method stub
	// // 失敗
	// //打印错误信息,print the error msg
	// t.printStackTrace();
	// //错误监听,handle the error msg
	// Message msg = new Message();
	// msg.what = MSG_ACTION_CCALLBACK;
	// msg.arg1 = 2;
	// msg.arg2 = action;
	// msg.obj = t;
	// UIHandler.sendMessage(msg, this);
	// }

}
