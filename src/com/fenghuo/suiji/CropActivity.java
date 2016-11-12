package com.fenghuo.suiji;

import java.io.File;

import com.fenghuo.cropscale.CropImageLayout;
import com.fenghuo.suiji.R;
import com.fenghuo.dao.ImageTools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CropActivity extends Activity{
	private CropImageLayout mCropImageLayout;
	private String path;
	private ProgressDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crop_image);
		//这步必须要加
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog=new ProgressDialog(this);
        loadingDialog.setTitle("请稍后...");
		path=getIntent().getStringExtra("path");
		if(TextUtils.isEmpty(path)||!(new File(path).exists())){
			Toast.makeText(this, "图片加载失败",Toast.LENGTH_SHORT).show();
			return;
		}
		Bitmap bitmap=ImageTools.convertToBitmap(path, 600,600);
		if(bitmap==null){
			Toast.makeText(this, "图片加载失败",Toast.LENGTH_SHORT).show();
			return;
		}
		mCropImageLayout = (CropImageLayout) findViewById(R.id.id_clipImageLayout);
		mCropImageLayout.setBitmap(bitmap);
		((Button)findViewById(R.id.id_action_clip)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadingDialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						Bitmap bitmap = mCropImageLayout.clip();
						String path= Environment.getExternalStorageDirectory()+"/ClipHeadPhoto/cache/"+System.currentTimeMillis()+ ".png";
						ImageTools.savePhotoToSDCard(bitmap,path);
						loadingDialog.dismiss();
						Intent intent = new Intent();
						intent.putExtra("path",path);
						setResult(RESULT_OK, intent);
						finish();
					}
				}).start();
			}
		});
	}
	
	
	
}
