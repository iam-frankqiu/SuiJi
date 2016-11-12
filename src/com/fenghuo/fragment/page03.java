package com.fenghuo.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenghuo.http.GetHttp;
import com.fenghuo.suiji.CropActivity;

import com.fenghuo.suiji.About;
import com.fenghuo.suiji.FeedBack;
import com.fenghuo.suiji.Login;
import com.fenghuo.suiji.MyActivity;
import com.fenghuo.suiji.MyApplication;
import com.fenghuo.suiji.Register;

import com.fenghuo.suiji.R;

import com.fenghuo.utils.UtilsConstant;
import com.fenghuo.view.CustomDialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class page03 extends Fragment implements OnClickListener {
	private ImageView head;
	MyApplication my;
	private PopupWindow popWindow;
	private LayoutInflater layoutInflater;
	private TextView photograph, albums;
	private LinearLayout cancel;
	JSONObject json;

	public static final int PHOTOZOOM = 0; // 閿熸枻鎷烽敓锟介敓鏂ゆ嫹閿熸枻鎷�
	public static final int PHOTOTAKE = 1; // 閿熸枻鎷烽敓锟介敓鏂ゆ嫹閿熸枻鎷�
	public static final int IMAGE_COMPLETE = 2; // 閿熸枻鎷烽敓锟�
	public static final int CROPREQCODE = 3; // 閿熸枻鎷峰彇
	private String photoSavePath;// 閿熸枻鎷烽敓鏂ゆ嫹璺敓鏂ゆ嫹
	private String photoSaveName;// 鍥緋ian閿熸枻鎷�
	private String path;// 鍥剧墖鍏ㄨ矾閿熸枻鎷�
	GetHttp gethttp = new GetHttp();

	private TextView updatename;
	private EditText et_upname;
	private Button btn_upname, btn_quxiao;
	private CustomDialog dialog;

	Button exit;
	TextView publish, about, problems, name;
	Intent it1, it2, it3, it4;
	RelativeLayout layout;
	String[] ss;

	Handler hd = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
				String sss = (String) msg.obj;
				name.setText(sss);
			}

			String str = (String) msg.obj;

			Toast.makeText(getActivity(), str, 0).show();

			return false;
		}
	});

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String sss = (String) msg.obj;

			System.out.println(sss + "**********handler");

			name.setText(sss);

			return false;
		}
	});

	Handler hand = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub

			if (msg.what == 0) {

				head.setImageResource(R.drawable.youkeicon);

				System.out.println("00000000000000000");

			} else {
				Bitmap bit = (Bitmap) msg.obj;

				// System.out.println(bit.toString() + "************hand");

				head.setImageBitmap(bit);
			}

			return false;
		}
	});
	private TextView register, login;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.page_03, null);

		layout = (RelativeLayout) view.findViewById(R.id.rl_notinput);
		head = (ImageView) view.findViewById(R.id.head);
		updatename = (TextView) view.findViewById(R.id.updatename);
		publish = (TextView) view.findViewById(R.id.tv_publish);
		about = (TextView) view.findViewById(R.id.tv_about);
		problems = (TextView) view.findViewById(R.id.tv_problems);
		name = (TextView) view.findViewById(R.id.tv_inputname);
		register = (TextView) view.findViewById(R.id.textView1);
		login = (TextView) view.findViewById(R.id.textView3);
		exit = (Button) view.findViewById(R.id.exit);
		my = (MyApplication) getActivity().getApplication();

		json = new JSONObject();

		if (my.getIndex() != 0) {

			register.setVisibility(view.INVISIBLE);
			login.setVisibility(view.INVISIBLE);

			try {
				json.put("id", my.getIndex());
				json.put("key", "headshow");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			new Thread(new Runnable() {

				@Override
				public void run() {
					GetHttp get = new GetHttp();
					try {
						String str = get.getToTheInternet(json,
								UtilsConstant.contentpath,
								UtilsConstant.MyPoint);
						System.out.println(str + "************str");
						ss = new String[2];
						ss = str.split(",");
						Message msg = new Message();
						msg.obj = ss[1];
						System.out.println(ss[0] + "************2");

						handler.sendMessage(msg);

						if (ss[0].equals("null")) {

							hand.sendEmptyMessage(0);

							System.out.println("1111111111111111");

						} else {
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									try {
										URL url = new URL(
												UtilsConstant.headiconpath
														+ ss[0]);

										InputStream stream = url.openStream();

										Bitmap bitmap = BitmapFactory
												.decodeStream(stream);

										Message msg2 = new Message();
										msg2.what = 1;

										msg2.obj = bitmap;

										System.out.println("************1");

										hand.sendMessage(msg2);

									} catch (MalformedURLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							}).start();
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else {

			exit.setVisibility(view.INVISIBLE);
			head.setClickable(false);

		}

		exit.setOnClickListener(this);
		updatename.setOnClickListener(this);
		publish.setOnClickListener(this);
		layout.setOnClickListener(this);
		about.setOnClickListener(this);
		problems.setOnClickListener(this);
		login.setOnClickListener(this);
		register.setOnClickListener(this);

		it1 = new Intent(getActivity(), Login.class);
		it2 = new Intent(getActivity(), MyActivity.class);

		it4 = new Intent(getActivity(), About.class);

		layoutInflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		File file = new File(Environment.getExternalStorageDirectory(),
				"ClipHeadPhoto/cache");
		if (!file.exists())
			file.mkdirs();
		photoSavePath = Environment.getExternalStorageDirectory()
				+ "/ClipHeadPhoto/cache/";
		photoSaveName = System.currentTimeMillis() + ".png";

		head = (ImageView) view.findViewById(R.id.head);// 澶撮敓鏂ゆ嫹
		updatename = (TextView) view.findViewById(R.id.updatename);
		head.setOnClickListener(this);

		updatename.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.textView1:// 注册
			Intent it_register = new Intent(getActivity(), Register.class);
			startActivity(it_register);
			getActivity().overridePendingTransition(R.anim.in_leftright,
					R.anim.alphamove);
			break;
		case R.id.textView3:// 登陆
			Intent it_login = new Intent(getActivity(), Login.class);
			startActivity(it_login);
			getActivity().overridePendingTransition(R.anim.in_leftright,
					R.anim.alphamove);
			break;
		case R.id.head:// 点击头像进行更换并上传
			if (my.getIndex() == 0) {
				// startActivity(it1);
			} else {
				showPopupWindow(head);
			}
			break;
		case R.id.updatename:// 弹出更新昵称的对话框
			if (my.getIndex() == 0) {
				Toast.makeText(getActivity(), R.string.notinput, 0).show();
			} else {
				showDialog();
			}
			break;

		case R.id.rl_notinput:

			break;

		case R.id.tv_about:// 跳转到关于//打开一个对话框
			// AlertDialog.Builder builder = new Builder(getActivity());
			// builder.setTitle("关于随迹");
			// builder.setMessage("");
			// builder.setPositiveButton("确定",
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface arg0, int arg1) {
			// // TODO Auto-generated method stub
			//
			// }
			// });
			// builder.show();

			startActivity(it4);

			break;
		case R.id.tv_problems:
			// 跳转到反馈
			Intent fbintent = new Intent(getActivity(), FeedBack.class);
			startActivity(fbintent);
			break;
		case R.id.tv_publish:
			// 跳转到我的发布界面
			if (my.getIndex() == 0) {
				Toast.makeText(getActivity(), R.string.notinput, 0).show();
			} else {
				startActivity(it2);
				getActivity().overridePendingTransition(R.anim.in_leftright,
						R.anim.alphamove);
			}
			break;
		case R.id.exit:
			// 退出登陆 建议未登陆状态显示不可见
			name.setText("请登录");
			head.setImageResource(R.drawable._head);
			exit.setVisibility(View.INVISIBLE);
			register.setVisibility(View.VISIBLE);
			login.setVisibility(View.VISIBLE);
			// startActivity(it1);
			// UtilsConstant.json.remove(name.getText().toString());
			// UtilsConstant.json.remove(name)
			// json.remove("id");

			SharedPreferences sharedPreferences = getActivity()
					.getSharedPreferences("user", Context.MODE_PRIVATE);

			Editor editor = sharedPreferences.edit();
			editor.remove("index");
			editor.commit();
			my.setIndex(0);

			break;
		default:
			break;
		}
	}

	private void updatename() {
		// TODO Auto-generated method stub

		// if (UtilsConstant.index != 0) {
		// JSONObject json = new JSONObject();
		try {
			// UtilsConstant.json.put("id", 404);

			json.remove("key");

			// json.remove("name');

			json.put("key", "updatename");

			json.put("name", et_upname.getText().toString());

			System.out.println(et_upname.getText().toString()
					+ "***********name");

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						String str = gethttp.getToTheInternet(json,
								UtilsConstant.contentpath,
								UtilsConstant.MyPoint);

						Message msg = new Message();

						msg.obj = str;

						hd.sendMessage(msg);

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

	private void showDialog() {

		dialog = new CustomDialog(getActivity(), 300, 200,
				R.layout.layout_dialog, R.style.Theme_dialog);
		dialog.show();
		et_upname = (EditText) dialog.findViewById(R.id.et_upname);
		btn_upname = (Button) dialog.findViewById(R.id.btn_upname);
		btn_quxiao = (Button) dialog.findViewById(R.id.btn_quxiao);

		btn_quxiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		btn_upname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(et_upname.getText().toString())) {

					Toast.makeText(getActivity(), R.string.text_is_empty, 0)
							.show();
				} else {
					name.setText(et_upname.getText().toString());
					updatename();
					dialog.dismiss();
				}

			}
		});

	}

	@SuppressWarnings("deprecation")
	private void showPopupWindow(View parent) {
		if (popWindow == null) {
			View view = layoutInflater.inflate(R.layout.pop_select_photo, null);
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			initPop(view);
		}
		popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	public void initPop(View view) {
		photograph = (TextView) view.findViewById(R.id.photograph);// 閿熸枻鎷烽敓鏂ゆ嫹
		albums = (TextView) view.findViewById(R.id.albums);// 閿熸枻鎷烽敓锟�
		cancel = (LinearLayout) view.findViewById(R.id.cancel);// 鍙栭敓鏂ゆ嫹
		photograph.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				photoSaveName = String.valueOf(System.currentTimeMillis())
						+ ".png";
				Uri imageUri = null;
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));

				openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION,
						0);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, PHOTOTAKE);
			}
		});

		albums.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				Intent it = new Intent();
				it.setAction(Intent.ACTION_PICK);
				it.setType("image/*");
				startActivityForResult(it, PHOTOZOOM);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();

			}
		});
	}

	/**
	 * 鍥剧墖閫夐敓鏂ゆ嫹閿熸枻鎷烽敓绉告枻鎷烽敓锟�
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != -1) {
			return;
		}
		Uri uri = null;
		switch (requestCode) {
		case PHOTOZOOM:// 图库
			if (data == null) {
				return;
			}
			uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			@SuppressWarnings("deprecation")
			Cursor cursor = getActivity().managedQuery(uri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index);// 鍥剧墖閿熻妭纰夋嫹璺敓鏂ゆ嫹
			Intent intent3 = new Intent(getActivity(), CropActivity.class);
			intent3.putExtra("path", path);
			startActivityForResult(intent3, IMAGE_COMPLETE);
			System.out.println(path + "1========================");
			Toast.makeText(getActivity(), path, 0).show();
			break;
		case PHOTOTAKE:// 拍照
			path = photoSavePath + photoSaveName;
			uri = Uri.fromFile(new File(path));
			Intent intent2 = new Intent(getActivity(), CropActivity.class);
			intent2.putExtra("path", path);
			startActivityForResult(intent2, IMAGE_COMPLETE);
			System.out.println(path + "=====================");
			Toast.makeText(getActivity(), path, 0).show();
			break;
		case IMAGE_COMPLETE:// 枻鎷烽敓锟�閿熸枻鎷穊itmap閿熸枻鎷峰紡閿熻姤鍌ㄩ敓鏂ゆ嫹閿熸枻鎷疯彉閿熸枻鎷烽敓锟�
			final String temppath = data.getStringExtra("path");

			head.setImageBitmap(getLoacalBitmap(temppath));
			try {
				uploadHeadIcon(temppath);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(temppath + "===========================");
			Toast.makeText(getActivity(), temppath, 0).show();

			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * @param url
	 * @return
	 */
	//
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 上传头像到服务器
	public void uploadHeadIcon(String temppath) throws JSONException {
		Bitmap bitmap = BitmapFactory.decodeFile(temppath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
		byte[] bs = baos.toByteArray();
		String ets = Base64.encodeToString(bs, Base64.DEFAULT);

		// JSONObject js = new JSONObject();
		// try {
		// UtilsConstant.json.put("key", "headicon");
		// UtilsConstant.json.put("headicon", ets);
		// String str = gethttp.getToTheInternet(UtilsConstant.json,
		// UtilsConstant.contentpath, UtilsConstant.IPoint);
		// // 閿熷壙杈炬嫹閿熸枻鎷疯閿熸枻鎷峰彇閿熸枻鎷峰ご閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹娌￠敓鏂ゆ嫹閿熺禒d閿熸枻鎷峰悓鏃堕敓杈冭揪鎷�
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		FinalHttp finalHttp = new FinalHttp();

		AjaxParams params = new AjaxParams();

		try {
			params.put("headicon", new File(temppath));

			// UtilsConstant.json.put("id", 404);

			params.put("id", json.toString());

			System.out.println(temppath
					+ "***************file put into success!");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finalHttp.post(UtilsConstant.contentpath, params,
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						// super.onSuccess(t);
						System.out.println(t + "success");
						Toast.makeText(getActivity(), t, 0).show();
					}
				});
	}

}
