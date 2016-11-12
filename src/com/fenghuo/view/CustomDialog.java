package com.fenghuo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class CustomDialog extends Dialog {
	private static int default_width = 180;
	private static int default_height = 160;
	public CustomDialog(Context context,int layout,int style) {
		this(context,default_width,default_height,layout,style);
		// TODO Auto-generated constructor stub
	}
	
	public CustomDialog(Context context,int width,int height,int layout,int style){
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		
		float density = getDensity(context);
		params.width = (int) (width*density);
		params.height = (int) (height*density);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
	}

	private float getDensity(Context context) {
		// TODO Auto-generated method stub
		
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		
		return dm.density;
	}

}
