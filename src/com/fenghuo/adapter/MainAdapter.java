package com.fenghuo.adapter;


import java.util.List;

import net.tsz.afinal.FinalBitmap;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenghuo.pojo.Show;
import com.fenghuo.suiji.R;
import com.fenghuo.utils.UtilsConstant;

public class MainAdapter extends BaseAdapter {
	public int i = 0;

	private LayoutInflater mInflater;
	// private List<Content> list;
	// private List<User> list2;
	// private JSONObject json;
	private List<Show> list;

	FinalBitmap bitmap;
	FinalBitmap bitmap2;
	Context context;
	int ww, hh;

	public MainAdapter(Context context, List<Show> list, int ww, int hh) {
		// TODO Auto-generated constructor stub
		this.mInflater = LayoutInflater.from(context);

		this.ww = ww;
		this.hh = hh;

		this.context = context;

		bitmap = FinalBitmap.create(context);
		bitmap.configLoadingImage(R.drawable.ic_launcher);
		bitmap.configLoadfailImage(R.drawable.ic_launcher);
		bitmap2 = FinalBitmap.create(context);
		bitmap2.configLoadingImage(R.drawable.ic_launcher);
		bitmap2.configLoadfailImage(R.drawable.ic_launcher);
		this.list = list;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_main, null);
			viewHolder = new ViewHolder();
			viewHolder.img_icon = (ImageView) convertView
					.findViewById(R.id.img_main_01);
			viewHolder.img_content = (ImageView) convertView
					.findViewById(R.id.img_main_content);
			viewHolder.img_position = (ImageView) convertView
					.findViewById(R.id.img_main_dibiao);
			viewHolder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_main_date);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_main_name);
			viewHolder.tv_position = (TextView) convertView
					.findViewById(R.id.tv_main_position);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Show show = list.get(position);

		// String sss = UtilsConstant.headiconpath + show.getHeadicon();

		viewHolder.img_icon.setImageResource(R.drawable.youkeicon);
		// if (sss.equals(viewHolder.img_icon.getTag())) {
		// // bitmap2.display(viewHolder.img_icon, sss);
		// } else {
		// bitmap2.display(viewHolder.img_icon, sss);
		// }

		viewHolder.img_position.setImageResource(R.drawable.dibiao);
		viewHolder.tv_date.setText(show.getTime());

		// System.out.println(show.getName() + "*****************8name");

		if (show.getName().equals("null")) {

			System.out.println("%%%%%%%%%%%%δ����");

			viewHolder.tv_name.setText("����" + show.getUid() + "��");

		} else {
			viewHolder.tv_name.setText(show.getName());

			// System.out.println("whywhywhy************88");

		}

		viewHolder.tv_position.setText(show.getPosition());
		String url = UtilsConstant.imagepath + show.getImage();
		// viewHolder.img_content.setTag(url);
		// BitmapDrawable drawable = getBitmapFromMemoryCache(url);
		// if (drawable != null) {
		// viewHolder.img_content.setImageDrawable(drawable);
		// } else {
		// BitmapWorkerTask task = new BitmapWorkerTask();
		// task.execute(url);
		// }

		// String url2 =

		bitmap.display(viewHolder.img_content, url);

		bitmap2.display(viewHolder.img_icon,
				UtilsConstant.headiconpath + show.getHeadicon());
		//
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// URL url =null;
		// try {
		//
		// // System.out.println("+++++++++++imagename"+show.getImage());
		//
		// Log.e("qqq", ""+i++);
		//
		// url = new URL( UtilsConstant.headiconpath+show.getHeadicon());
		//
		// InputStream inputStream = url.openStream();
		//
		// Bitmap bit = BitmapFactory.decodeStream(inputStream);
		//
		// getImage image = new getImage();
		//
		//
		//
		// Bitmap map= image.comp(bit,hh,ww);
		//
		// Message msg = new Message();
		//
		// msg.obj = map;
		//
		// hd.sendMessage(msg);
		//
		// } catch (MalformedURLException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		//
		//
		//
		// }
		// }).start();

		return convertView;
	}

	class ViewHolder {
		ImageView img_icon;
		ImageView img_content;
		ImageView img_position;
		TextView tv_name;
		TextView tv_date;
		TextView tv_position;

	}

}
