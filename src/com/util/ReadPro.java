package com.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 用户读取.properties这样的属性文件
 * @author new
 *
 */
public class ReadPro extends Properties {

	private static ReadPro read;
	
	private ReadPro(){
		//读取src下db.properties文件
		InputStream in=ReadPro.class.getResourceAsStream("/db.properties");
		try {
			//加载输入流
			load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ReadPro getNewInstance(){
		if(read==null){
			read=new ReadPro();
		}
		return read;
	}
	
}
