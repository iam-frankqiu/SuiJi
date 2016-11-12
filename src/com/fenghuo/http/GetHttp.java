package com.fenghuo.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenghuo.pojo.Show;

import android.util.Log;

public class GetHttp {

	public String getToTheInternet(JSONObject json, String path,String appoint)
			throws IOException {
		Log.e("error", "start");

//		JSONObject js = new JSONObject();

		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();

		list.add(new BasicNameValuePair(appoint, json.toString()));

		HttpGet get = new HttpGet(path + "?"
				+ URLEncodedUtils.format(list, "utf-8"));
		get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//		HttpGet get = new HttpGet(path + "?" + new UrlEncodedFormEntity(list, "utf-8"));
		

//		HttpGet get = new HttpGet(path + "?" + "input=" + json.toString());
		
		HttpClient client = new DefaultHttpClient();

		// System.out.println(client.toString()+"%%%%%%%%%client");

		// System.out.println("success");

		try {
			Log.e("error", "start1");

			HttpResponse response = client.execute(get);

			Log.e("error", "start2");

			// System.out.println(response.toString()+"@@@@@@@");

			int code = response.getStatusLine().getStatusCode();

			// System.out.println(code+"&&&&&&&&&code");

			if (code == 200) {

				HttpEntity entity = response.getEntity();

				String string = EntityUtils.toString(entity);

				return string;
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e("error", "ClientProtocolException");
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e("error", "ParseException");
			e.printStackTrace();
		}
		return null;

	}

	public String postToTheInternet(JSONObject json, String path,String parameter) throws ClientProtocolException, IOException {

		
		
		String str = null;

		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();

		list.add(new BasicNameValuePair(parameter, json.toString()));
		
		HttpPost  post = new HttpPost(path);
		
		post.setEntity(new UrlEncodedFormEntity(list,"utf-8"));
		
		
		
		HttpClient client = new DefaultHttpClient();
		
		HttpResponse response = client.execute(post);
		
		int code = response.getStatusLine().getStatusCode();
		
		System.out.println(code+"*********code");
		
		if(code==200){
			
			HttpEntity entity = response.getEntity();
			
			str = EntityUtils.toString(entity);
			
			System.out.println(str+"@@@@@@@@@@return");
			
			
		}
		
		

		return str ;
	}

}
