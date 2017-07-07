package com.util;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateTime {
	public static boolean valiDateTimeWithLongFormat(String timeStr) {
		String format = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
				+ "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
		String format2 = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";
		Pattern pattern = Pattern.compile(format);
		Pattern pattern2 = Pattern.compile(format2);
		Matcher matcher = pattern.matcher(timeStr);
		Matcher matcher2 = pattern2.matcher(timeStr);
		boolean flag=false;
		if (matcher.matches()) {
			pattern = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");
			matcher = pattern.matcher(timeStr);
			if (matcher.matches()) {
				int y = Integer.valueOf(matcher.group(1));
				int m = Integer.valueOf(matcher.group(2));
				int d = Integer.valueOf(matcher.group(3));
				if (d > 28) {
					Calendar c = Calendar.getInstance();
					c.set(y, m-1, 1);
					int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
					return (lastDay >= d);
				}
			}
			flag=true;
		}
		if (matcher2.matches()) {
			pattern2 = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");
			matcher2 = pattern2.matcher(timeStr);
			if (matcher2.matches()) {
				int y = Integer.valueOf(matcher2.group(1));
				int m = Integer.valueOf(matcher2.group(2));
				int d = Integer.valueOf(matcher2.group(3));
				if (d > 28) {
					Calendar c = Calendar.getInstance();
					c.set(y, m-1, 1);
					int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
					return (lastDay >= d);
				}
			}
			flag=true;
		}
		return flag;
	}

}
