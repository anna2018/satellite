package com.detection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dao.SatelliteDao;
import com.dao.SatellitePreprocessingDao;
import com.entity.Satellite;
import com.entity.Satellite_Preprocessing;
import com.util.CreateTable;
import com.util.TableExist;
import com.util.Tmid;

public class DataPreprocessing {


	public static void DataCompression(String tableName, String preprocessingName,int satelliteId) {// tableName存放原始卫星数据的表名，preprocessingName存放压缩过后的卫星数据的表名
		SatelliteDao sd = new SatelliteDao();
		SatellitePreprocessingDao spd = new SatellitePreprocessingDao();
		String sql = "select min(time) as time  from " + tableName+" where satelliteId="+satelliteId; //获取数据起点
		List<Satellite> min_list = sd.find(sql);
		Date min_time = min_list.get(0).getTime();
		sql = "select max(time) as time  from " + tableName+" where satelliteId="+satelliteId; //获取数据终点
		List<Satellite> max_list = sd.find(sql);
		String exist_sql = "select count(*) from user_tables where table_name='" + preprocessingName.toUpperCase()  //判断表格是否存在
				+ "'";
		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
			String create_sql = "create table " + preprocessingName
					+ " (time DATE,a1025  NUMBER,a1026  NUMBER,a1027  NUMBER,a1028  NUMBER,a1029  NUMBER,a1030  NUMBER,a1031  NUMBER,a1032  NUMBER,a1033  NUMBER,a1034  NUMBER,a1035  NUMBER,a1036  NUMBER,a1048  NUMBER,a1049  NUMBER,a1050  NUMBER,a1051  NUMBER,a1052  NUMBER,a1053  NUMBER,a1054  NUMBER,a1055  NUMBER,a1056  NUMBER,a1057  NUMBER,a1058  NUMBER,a1059  NUMBER,a1060  NUMBER,a1061  NUMBER,a1062  NUMBER,a1063  NUMBER,a1075  NUMBER,a1076  NUMBER,a1077  NUMBER,a1078  NUMBER,a4201  NUMBER,a4202  NUMBER,a4203  NUMBER,a4204  NUMBER,a4205  NUMBER,a2125  NUMBER,a2126  NUMBER,a2127  NUMBER,a2128  NUMBER,a2131  NUMBER,a2132  NUMBER,a2133  NUMBER,a2136  NUMBER,a2137  NUMBER,a6220  NUMBER,a13333 NUMBER,a2140  NUMBER,a2141  NUMBER,a2142  NUMBER,a2143  NUMBER,a14346 NUMBER,a14347 NUMBER,a14348 NUMBER,a14349 NUMBER,a14350 NUMBER,a14351 NUMBER,a14373 NUMBER,a14374 NUMBER,a14375 NUMBER,a14376 NUMBER,a14377 NUMBER,a14403 NUMBER,a14404 NUMBER,a14405 NUMBER,a14406 NUMBER,a14368 NUMBER,a14371 NUMBER,a14417 NUMBER,a14418 NUMBER,a14419 NUMBER,a13395 NUMBER,a13396 NUMBER,a13397 NUMBER,a14340 NUMBER ,satelliteId NUMBER)";
			CreateTable.create(create_sql);
		}
		else{
			String sql_preprocessing = "select max(time) as time from " + preprocessingName+" where satelliteId="+satelliteId; //如果部分数据已处理则重新修改数据起点
			List<Satellite_Preprocessing> list2 = spd.find(sql_preprocessing);
			if (list2.get(0).getTime() == null) {
				min_time = min_list.get(0).getTime();
			} else {
				min_time = new Date(list2.get(0).getTime().getTime() + 60000);
			}
		}
		Date max_time = max_list.get(0).getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = min_time;
		List<String> attribute_list = Tmid.getTmid(); // 获取属性列表
		while (time.before(max_time)) {
			// Satellite_Preprocessing sp= new Satellite_Preprocessing();
			String insert_sql = "insert into " + preprocessingName + " values("+satelliteId+",to_date('" + sdf.format(time)
					+ "','yyyy-mm-dd hh24:mi:ss')";
			for (int i = 0; i < attribute_list.size(); i++) {
				String attribute = attribute_list.get(i);
				String sql1 = "select to_date('" + sdf.format(time)
						+ "','yyyy-mm-dd hh24:mi:ss') as time,avg(value) as value" + " from " + tableName
						+ " where time>=to_date('" + sdf.format(time) + "','yyyy-mm-dd hh24:mi:ss') and time<to_date('"
						+ sdf.format(new Date(time.getTime() + 60000)) + "','yyyy-mm-dd hh24:mi:ss') and attribute='"
						+ attribute + "'";   //获取每一个属性的数据
				//System.out.println(sql1);
				List<Satellite> list1 = sd.find(sql1);
				if (list1.size()!=0) {
					Satellite s = list1.get(0);
					insert_sql += "," + s.getValue();
				}
				list1.clear();
			}
			insert_sql += ")";
			//System.out.println(insert_sql);
			int v = spd.insert(insert_sql);
			time = new Date(time.getTime() + 60000);
		}
		for (int i = 0; i < attribute_list.size(); i++) { //将每一种属性的数据归一化
			String attribute = attribute_list.get(i);
			String sql1 = "update "+preprocessingName+" set A"+attribute+"=(A"+attribute+"-(select avg(A"+attribute+") from "+preprocessingName+"))/(select stddev(A"+attribute+") from "+preprocessingName+")";
			//System.out.println(sql1);
			int v = spd.update(sql1);
		}
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		DataPreprocessing.DataCompression("satellite188_7", "data188",188);
//	}
	
}
