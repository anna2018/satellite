package com.util;

public class DateChange {
	public static String DateChanged(String str)
	{
		char[] temp = str.toCharArray();
		char[] temp1= new char[10];
		temp1[9]=temp[7];
		temp1[8]=temp[6];
		temp1[7]='-';
		temp1[6]=temp[5];
		temp1[5]=temp[4];
		temp1[4]='-';
		temp1[3]=temp[3];
		temp1[2]=temp[2];
		temp1[1]=temp[1];
		temp1[0]=temp[0];
		String s = null;
		s=s.copyValueOf(temp1);
		return s;
	}
}
