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

import com.fenghuo.pojo.Content;
import com.fenghuo.suiji.R;
import com.fenghuo.utils.UtilsConstant;

public class InfoAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<Content> list;
	// JSONObject json;

	FinalBitmap bitmap;

	

	public InfoAdapter(Context context, List<Content> list) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);

		bitmap = FinalBitmap.create(context);
		bitmap.configLoadingImage(R.drawable.ic_launcher);

		// json = new JSONObject();
		this.list = list;
		// list = new ArrayList<Content>();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int po, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		viewHolder holder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.item, null);
			holder = new viewHolder();

			holder.text = (TextView) view.findViewById(R.id.tv_item);
			holder.img = (ImageView) view.findViewById(R.id.iv_item);
			holder.position = (TextView) view.findViewById(R.id.tv_position);
			holder.time = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(holder);

		} else {
			holder = (viewHolder) view.getTag();
		}

		Content c = list.get(po);

		if (c.getMsg().equals("null")) {
			holder.text.setText("");
		} else {
			holder.text.setText(c.getMsg());
		}

		holder.time.setText(c.getTime());

		holder.position.setText(c.getPosition());

		String name = c.getImage();

		String ss = UtilsConstant.imagepath + name;

		System.out.println(ss + "****************ss");

		bitmap.display(holder.img, ss);

		return view;
	}

	class viewHolder {
		TextView text;
		ImageView img;
		TextView time;
		TextView position;

	}

}
