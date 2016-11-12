package com.fenghuo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.smssdk.framework.utils.Data;

public class UtilsTime {

	public static String s = "yyyy-MM-dd hh:mm:ss";

	public static String getTimeToString(Date now) {

		SimpleDateFormat sdf = new SimpleDateFormat(s);
		String ff = sdf.format(now);

		return ff;
	}

	public static Date getStringToTime(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(s);
		Date now = sdf.parse(time);

		return now;
	}

}
