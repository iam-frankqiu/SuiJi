package com.fenghuo.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.baidu.mapapi.common.SysOSUtil;
import com.fenghuo.pojo.Content;
import com.fenghuo.pojo.Show;

public class UtilsConstant {
	// public static int index ;
//	public static String ip = "http://10.2.141.161:8080/Foot/";
	public static String ip = "http://fenghuoserver.nat123.net:21255/Foot/";
	public static String userpath = ip + "MyServlet";
	public static String contentpath = ip + "IServlet";
	public static String imagepath = ip + "Image/";
	public static String headiconpath = ip + "/Headicon/";
	public static final String MyPoint = "input";
	public static final String IPoint = "update";

	public static JSONObject json = new JSONObject();

	public static List<Show> getStringToList(String str) {

		String str1 = str.trim();//鍘婚櫎瀛楃涓蹭袱绔殑绌烘牸

		String str2 = str1.replace("[", ",");//鐢�鏇挎崲鎺塠
		String str3 = str2.replace("]", ",");//鐢�鏇挎崲鎺塢

		String[] ss = str3.split("[,，]");//鐢�锛屾妸瀛楃涓插垎鍓�

		int leng = ss.length - (ss.length / 9) * 2;

		List<Show> list = new ArrayList<Show>();
		String[] ss1 = new String[leng];
		// List<String> l = new ArrayList<String>();
		int j = 0;
		for (int i = 0; i < ss.length; i++) {
			if (ss[i].contains("=")) {

				ss1[j] = ss[i].trim();
				// l.add(ss[i].trim());
				j++;

			}

		}
		// int k = 0;

		String[] last = new String[leng];

		// System.out.println(leng+"$$$$$$$$$$$4");

		for (int i = 0; i < ss1.length; i++) {
			String[] ss2 = ss1[i].split("=");
			// System.out.println(ss2[1]+"^^^^^^^^^^^^");
			last[i] = ss2[1];

		}

		for (int i = 0; i < last.length; i++) {
			if (i % 7 == 0) {
				Show s = new Show();
				s.setUid(Integer.parseInt(last[i]));
				s.setName(last[i + 1]);
				s.setTheme(Integer.parseInt(last[i + 2]));
				s.setHeadicon(last[i + 3]);
				s.setTime(last[i + 4]);
				s.setPosition(last[i + 5]);
				s.setImage(last[i + 6]);

				// System.out.println(s.toString()+"************show");

				list.add(s);
			}

		}

		return list;

	}

	public static List<Content> getStringToContent(String str) {

		String str1 = str.trim();

		String str2 = str1.replace("[", ",");
		String str3 = str2.replace("]", ",");

		String[] ss = str3.split("[,，]");

		int leng = ss.length - (ss.length / 11) * 2;

		// System.out.println(leng+"%%%%%%%%%%%leng");

		List<Content> list = new ArrayList<Content>();
		String[] ss1 = new String[leng];
		// List<String> l = new ArrayList<String>();
		int j = 0;
		for (int i = 0; i < ss.length; i++) {
			if (ss[i].contains("=")) {

				ss1[j] = ss[i].trim();

				j++;

			}

		}
		// int k = 0;

		String[] last = new String[leng];
		for (int i = 0; i < ss1.length; i++) {
			String[] ss2 = ss1[i].split("=");
			last[i] = ss2[1];
			System.out.println(last[i] + "********");

		}
		for (int i = 0; i < last.length; i++) {
			if (i % 9 == 0) {
				Content c = new Content();
				c.setId(Integer.parseInt(last[i]));
				c.setUid(Integer.parseInt(last[i + 1]));
				c.setImage(last[i + 2]);
				c.setMsg(last[i + 3]);
				c.setPosition(last[i + 4]);
				c.setTime(last[i + 5]);
				c.setVideo(last[i + 6]);
				c.setTheme(Integer.parseInt(last[i + 7]));
				c.setTitle(last[i + 8]);
				list.add(c);

			}
		}
		return list;

	}
}
