package com.fenghuo.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;

import android.hardware.usb.UsbConstants;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.View.OnClickListener;

import android.view.WindowManager;

import android.view.ViewGroup.LayoutParams;

import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.fenghuo.suiji.MyApplication;
import com.fenghuo.suiji.Publish;
import com.fenghuo.suiji.R;
import com.fenghuo.utils.UtilsConstant;

public class page02 extends Fragment implements OnClickListener {

	private ImageView start, share, pause, stop;
	MapView mMapView;
	BaiduMap map;
	private LatLng currentPt;
	private String touchType;
	private List<LatLng> points;
	LocationClient mLocClient;
	public MyLocationListenner myListener;
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	private LatLng lat;
	boolean isFirstLoc = true;// 锟角凤拷锟阶次讹拷位
	boolean flag = false;
	Intent it;
	String title;
	int theme;
	private PopupWindow inWindow;
	private LayoutInflater layoutInflater;
	private TextView tv_mks;
	private EditText et_title;
	MyApplication my;
	public String addrStr;
	private String time;

	// JSONObject json;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		SDKInitializer.initialize(getActivity().getApplicationContext());

		View view = inflater.inflate(R.layout.page_02, null);

		pause = (ImageView) view.findViewById(R.id.iv_pause);
		share = (ImageView) view.findViewById(R.id.iv_share);
		start = (ImageView) view.findViewById(R.id.iv_start);
		stop = (ImageView) view.findViewById(R.id.iv_stop);
		mMapView = (MapView) view.findViewById(R.id.bmapView);

		my = (MyApplication) getActivity().getApplication();

		System.out.println(my.getIndex() + "*************page2index");

		it = new Intent(getActivity(), Publish.class);

		// json = new JSONObject();

		myListener = new MyLocationListenner();// 位置监听事件

		map = mMapView.getMap();// 获取到地图视图

		map.setMyLocationEnabled(true);
		mLocClient = new LocationClient(getActivity());
		mLocClient.registerLocationListener(myListener);// 注册监听事件
		LocationClientOption option = new LocationClientOption();

		option.setOpenGps(true);// 开启gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10000);// 设置扫描间隔 单位是毫秒
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向

		mLocClient.setLocOption(option);

		points = new ArrayList<LatLng>();// 需要标记的带坐标的位置

		mLocClient.start();//  LocationClient的start()方法用来启动定位SDK，requestLocation()方法用来请求一次定位，请求过程是异步的。
							// 调用start()方法之后，会根据你设置的定位事件间隔来请求定位。

		mCurrentMarker = null;

		mCurrentMode = LocationMode.NORMAL;
		map.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));

		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		share.setOnClickListener(this);
		pause.setOnClickListener(this);

		layoutInflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE); // 打开popupwindow需开启
		return view;
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 锟斤拷俸锟斤拷诖锟斤拷锟斤拷陆锟斤拷盏锟轿伙拷锟�
			if (location == null || mMapView == null) {
				return;
			}

			// System.out.println(location.getLatitude() + "latitude"
			// + location.getLongitude() + "*****");

			addrStr = location.getAddrStr();

			System.out.println(addrStr + "**************add");

			lat = new LatLng(location.getLatitude(), location.getLongitude());

			if (flag) {
				if (points.size() == 0) {
					points.add(lat);
				} else if (points.size() != 0) {
					points.add(lat);

					OverlayOptions ooPolyline = new PolylineOptions().width(10)
							.color(R.color.trycolor).points(points);
					map.addOverlay(ooPolyline);
					// System.out.println("锟斤拷锟斤拷锟届迹");
				}

			}

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius()).direction(100)
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			map.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				map.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	

//更新地图状态显示面板
	private void updateMapState(LatLng point) {
		// if (mStateBar == null) {
		// return;
		// }

		String state = "";
		if (currentPt == null) {
			state = "点击、长按、双击地图以获取经纬度和地图状态";
		} else {
			state = String.format(touchType + ",当前经度 ： %f 当前纬度：%f",
					currentPt.longitude, currentPt.latitude);
		}
		state += "\n";
		MapStatus ms = map.getMapStatus();
		state += String.format("zoom=%.1f rotate=%d overlook=%d", ms.zoom,
				(int) ms.rotate, (int) ms.overlook);

		BitmapDescriptor bit = BitmapDescriptorFactory
				.fromResource(R.drawable.dingwei);

		MarkerOptions options = new MarkerOptions().position(point).icon(bit)
				.animateType(MarkerAnimateType.grow);

		Marker marker = (Marker) map.addOverlay(options);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 锟斤拷activity执锟斤拷onDestroy时执锟斤拷mMapView.onDestroy()锟斤拷实锟街碉拷图锟斤拷锟斤拷锟斤拷锟节癸拷锟斤拷
		mLocClient.stop();
		// 锟截闭讹拷位图锟斤拷
		map.setMyLocationEnabled(false);

		mMapView.onDestroy();

		mMapView = null;

		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();

		mMapView.onPause();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_pause:

			touchType = "锟斤拷锟斤拷";
			currentPt = lat;
			updateMapState(lat);

			points.add(lat);
			it.putExtra("tit", title);
			it.putExtra("theme", theme);

			try {
				UtilsConstant.json.put("position", addrStr);

				System.out.println("addrstr&&&&&&&&&&&&&&success");

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			startActivityForResult(it, 200);
			getActivity().overridePendingTransition(R.anim.alphamove,
					R.anim.alphamove);

			break;

		case R.id.iv_share:

			break;
		case R.id.iv_start:
			if (my.getIndex() == 0) {
				Toast.makeText(getActivity(), R.string.notinput, 0).show();
			} else {
				try {
					UtilsConstant.json.put("uid", my.getIndex());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				flag = true;
				inPopupWindow(view);
			}

			break;
		case R.id.iv_stop:

			flag = false;
			start.setVisibility(View.VISIBLE);
			pause.setVisibility(View.INVISIBLE);
			stop.setVisibility(View.INVISIBLE);
		
			break;

		default:
			break;
		}
	}

	private void inPopupWindow(View parent) {
		// TODO Auto-generated method stub
		if (inWindow == null) {
			View view = layoutInflater.inflate(R.layout.pop_select_way, null);

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

	private void initPop(View view) {
		// TODO Auto-generated method stub
		et_title = (EditText) view.findViewById(R.id.et_title);

		et_title.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				// temp = s.length();
				if (s != null) {

					tv_mks.setEnabled(true);

				} else {

					tv_mks.setEnabled(false);

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		tv_mks = (TextView) view.findViewById(R.id.tv_mks);

		tv_mks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				System.out.println(et_title.getText().toString());

				title = et_title.getText().toString();

				if (TextUtils.isEmpty(title)) {
					Toast.makeText(getActivity(), R.string.text_is_empty, 0)
							.show();
				} else {
					inWindow.dismiss();
					try {
						theme = (int) System.currentTimeMillis();

						UtilsConstant.json.put("theme", theme);
						UtilsConstant.json.put("title", title);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					start.setVisibility(View.INVISIBLE);
					pause.setVisibility(View.VISIBLE);
					stop.setVisibility(View.VISIBLE);

				}

			}
		});

	}

}
