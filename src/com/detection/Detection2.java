package com.detection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.dao.BaseDao;
import com.dao.KnnNeighborDao;
import com.dao.SatelliteDao;
import com.dao.SatellitePreprocessingDao;
import com.dao.ScoreDao;
import com.entity.KNN_Neighbor;
import com.entity.Satellite;
import com.entity.Satellite_Preprocessing;
import com.entity.Score;
import com.util.CreateTable;
import com.util.Inverse;
import com.util.TableExist;
import com.util.Tmid;

import Jama.Matrix;

public class Detection2 {
	public static int k=10;
	public static int s=5;
	public static int num=14;
	public static void DataCompression(String tableName, String preprocessingName,Date[] d,int satelliteId) {// tableName存放原始卫星数据的表名，preprocessingName存放压缩过后的卫星数据的表名
		SatelliteDao sd = new SatelliteDao();
		SatellitePreprocessingDao spd = new SatellitePreprocessingDao();
		
		String exist_sql = "select count(*) from user_tables where table_name='" + preprocessingName.toUpperCase()  //判断表格是否存在
				+ "'";
		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
			String create_sql = "create table " + preprocessingName
					+ " (time DATE, a1025  NUMBER,a1026  NUMBER,a1027  NUMBER,a1028  NUMBER,a1029  NUMBER,a1030  NUMBER,a1031  NUMBER,a1032  NUMBER,a1033  NUMBER,a1034  NUMBER,a1035  NUMBER,a1036  NUMBER,a1048  NUMBER,a1049  NUMBER,a1050  NUMBER,a1051  NUMBER,a1052  NUMBER,a1053  NUMBER,a1054  NUMBER,a1055  NUMBER,a1056  NUMBER,a1057  NUMBER,a1058  NUMBER,a1059  NUMBER,a1060  NUMBER,a1061  NUMBER,a1062  NUMBER,a1063  NUMBER,a1075  NUMBER,a1076  NUMBER,a1077  NUMBER,a1078  NUMBER,a4201  NUMBER,a4202  NUMBER,a4203  NUMBER,a4204  NUMBER,a4205  NUMBER,a2125  NUMBER,a2126  NUMBER,a2127  NUMBER,a2128  NUMBER,a2131  NUMBER,a2132  NUMBER,a2133  NUMBER,a2136  NUMBER,a2137  NUMBER,a6220  NUMBER,a13333 NUMBER,a2140  NUMBER,a2141  NUMBER,a2142  NUMBER,a2143  NUMBER,a14346 NUMBER,a14347 NUMBER,a14348 NUMBER,a14349 NUMBER,a14350 NUMBER,a14351 NUMBER,a14373 NUMBER,a14374 NUMBER,a14375 NUMBER,a14376 NUMBER,a14377 NUMBER,a14403 NUMBER,a14404 NUMBER,a14405 NUMBER,a14406 NUMBER,a14368 NUMBER,a14371 NUMBER,a14417 NUMBER,a14418 NUMBER,a14419 NUMBER,a13395 NUMBER,a13396 NUMBER,a13397 NUMBER,a14340 NUMBER,satelliteId NUMBER)";
			CreateTable.create(create_sql);
			create_sql = "create index " + preprocessingName
					+ "_index on "+preprocessingName+"(time)";
			CreateTable.create(create_sql);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = d[0];
		List<String> attribute_list = Tmid.getTmid(); // 获取属性列表
		while (time.before(d[1])) {
			time.setSeconds(0);
			// Satellite_Preprocessing sp= new Satellite_Preprocessing();
			String insert_sql = "insert into " + preprocessingName + " values( to_date('" + sdf.format(time)
					+ "','yyyy-mm-dd hh24:mi:ss')";
			for (int i = 0; i < attribute_list.size(); i++) {
				String attribute = attribute_list.get(i);
				String sql1 = "select to_date('" + sdf.format(time)
						+ "','yyyy-mm-dd hh24:mi:ss') as time,avg(value) as value" + " from " + tableName
						+ " where time>=to_date('" + sdf.format(time) + "','yyyy-mm-dd hh24:mi:ss') and time<to_date('"
						+ sdf.format(new Date(time.getTime() + 60000)) + "','yyyy-mm-dd hh24:mi:ss') and attribute='"
						+ attribute + "'";
				sql1+=" and satelliteId="+satelliteId;   //获取每一个属性的数据
				//System.out.println(sql1);
				List<Satellite> list1 = sd.find(sql1);
				if (list1.size()!=0) {
					Satellite s = list1.get(0);
					insert_sql += "," + s.getValue();
				}
				list1.clear();
			}
			insert_sql += ","+satelliteId+")";
			//System.out.println(insert_sql);
			int v = spd.insert(insert_sql);
			time = new Date(time.getTime() + 60000);
		}
//		for (int i = 0; i < attribute_list.size(); i++) { //将每一种属性的数据归一化
//			String attribute = attribute_list.get(i);
//			String sql1 = "update "+preprocessingName+" set A"+attribute+"=(A"+attribute+"-(select avg(A"+attribute+") from "+preprocessingName+"))/(select stddev(A"+attribute+") from "+preprocessingName+")";
//			//System.out.println(sql1);
//			int v = spd.update(sql1);
//		}
	}
	public static Date[] getDate(String tableName,int satelliteId){
		SatelliteDao spd = new SatelliteDao();
		String sql = "select min(time) as time  from " + tableName+" where satelliteId="+satelliteId; //获取数据起点
		//String sql = "select min(time) as time  from " + tableName;
		List<Satellite> min_list = spd.find(sql);
		Date min_time = min_list.get(0).getTime();
		sql = "select max(time) as time  from " + tableName+ " where satelliteId="+satelliteId;  //获取数据终点
		//sql = "select max(time) as time  from " + tableName; 
		List<Satellite> max_list = spd.find(sql);
		Date max_time = max_list.get(0).getTime();
		min_time.setSeconds(0);
		max_time.setSeconds(0);
		Date[] d=new Date[2];
		d[0]=min_time;
		d[1]=max_time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			d[0]=sdf.parse("2014-07-01 00:00:00");
			d[1]=sdf.parse("2014-09-01 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}
	public static int[] sort(double[] ary,int k){
		int[] index = new int[ary.length];
		for (int i = 0; i < index.length; i++) {
			index[i] = i;
		}
		for (int i = 0; i < ary.length-1; i++) {
			for (int j = i+1; j < ary.length; j++) {
				if(ary[i]>ary[j]){
					double temp = ary[i];
					int p = index[i];
					ary[i] = ary[j];
					index[i] = index[j];
					ary[j] = temp;
					index[j] = p; 
				}
			}
		}
		int[] position=new int[k+1];
		for(int i=0;i<k+1;i++){
			position[i]=index[i];
		}
        return position;
	}
	
	public static List<Satellite_Preprocessing> top_K_neighbors(Satellite_Preprocessing data,List<Satellite_Preprocessing> list,int k){//获取K近邻
		List<Satellite_Preprocessing> neighbors=new ArrayList<Satellite_Preprocessing>();
		double[] distance=new double[list.size()];
		for(int i=0;i<list.size();i++){//计算数据之间的距离
			distance[i]=data.getDistance(list.get(i));
		}
		int[] position=sort(distance,k);//排序并返回
		for(int i=1;i<position.length;i++){
			neighbors.add(list.get(position[i]));
		}
		return neighbors;
	}
	public static HashMap<Date, List<Satellite_Preprocessing>> getKNN(String preprocessingName,String knnName,Date[] d,int satelliteId){//preprocessingName存储预处理之后的数据，knnName存储k近邻数据
		SatellitePreprocessingDao spd = new SatellitePreprocessingDao();
		KnnNeighborDao knd=new KnnNeighborDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String exist_sql = "select count(*) from user_tables where table_name='" + knnName.toUpperCase()  //判断存储k近邻的表格是否存在
		+ "'";
        if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
        	String create_sql = "create table " + knnName
			+ " ( time DATE, neighbor_time DATE,satelliteId NUMBER)";
        	CreateTable.create(create_sql);
        	create_sql = "create index " + knnName
        			+ "_index on "+knnName+"(time)";
            CreateTable.create(create_sql);
        }
        Date time = new Date(d[0].getTime());
		int i=0;
		HashMap<Date,List<Satellite_Preprocessing>> knn_map=new HashMap<Date,List<Satellite_Preprocessing>>();
		Date start_date = start_date=new Date(time.getTime()-num*60000);
        Date end_date = time;
        while (time.before(d[1])) {
        	time.setSeconds(0);
        	if (time.getTime()<=d[0].getTime()+num*60000){
        		start_date = d[0];
                end_date = new Date(d[0].getTime()+num*60000);
        	}
        	else{
        		start_date = start_date=new Date(time.getTime()-num*60000);
        		end_date = time;
        	}
			String sql1 = "select * from " + preprocessingName
			+ " where time>=to_date('" + sdf.format(start_date) + "','yyyy-mm-dd hh24:mi:ss') and time<to_date('"
			+ sdf.format(end_date) + "','yyyy-mm-dd hh24:mi:ss') and satelliteId="+satelliteId+" order by time";   //获取每一个属性的数据
	        List<Satellite_Preprocessing> list1 = spd.find(sql1);
	        Satellite_Preprocessing s1=getData(preprocessingName,time,satelliteId).get(0);
	        List<Satellite_Preprocessing> neighbor=top_K_neighbors(s1,list1,k);//获取K近邻
	        knn_map.put(time, neighbor);
	        i=i++;
	        for(int j=0;j<neighbor.size();j++){
	        	KNN_Neighbor m=new KNN_Neighbor();
		        m.setTime(time);
		        m.setNeighbor_time(neighbor.get(j).getTime());
		        m.setSatelliteId(satelliteId);
	        	knd.insert(knnName, m);
	        }
	        time = new Date(time.getTime() + 60000);
		}
        return knn_map;
	}
	
	public static int common_nearest_neighbors(List<KNN_Neighbor> list1,List<KNN_Neighbor> list2){
		int count=0;
		for(int i=0;i<list1.size();i++){
			for(int j=0;j<list2.size();j++){
				if(list1.get(i).getNeighbor_time().getTime()==list2.get(j).getNeighbor_time().getTime()){
					count++;
				}
			}
		}
		return count;
	}
	public static List<KNN_Neighbor> top_S_neighbors(List<KNN_Neighbor> list1,List<List<KNN_Neighbor>> neighbor_list,int s){//获取K近邻
		List<KNN_Neighbor> neighbors=new ArrayList<KNN_Neighbor>();
		double[] distance=new double[neighbor_list.size()];
		for(int i=0;i<neighbor_list.size();i++){
			distance[i]=common_nearest_neighbors(list1,neighbor_list.get(i));
		}
		int[] position=sort(distance,s);//排序并返回
		for(int i=1;i<position.length;i++){
			neighbors.add(list1.get(position[i]));
		}
		return neighbors;
	}
	public static List<KNN_Neighbor> getNeighbor(String tableName,Date time,int satelliteId){//根据时间查询近邻集合
		KnnNeighborDao knd=new KnnNeighborDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql1 = "select * from " + tableName
				+ " where time=to_date('" + sdf.format(time) + "','yyyy-mm-dd hh24:mi:ss') and satelliteId="+satelliteId+" order by neighbor_time";   //获取每一个属性的数据
		List<KNN_Neighbor> list1 = knd.find(sql1);
		return list1;
	}
	public static List<Satellite_Preprocessing> getData(String tableName,Date time,int satelliteId){//根据时间查询数据记录
		SatellitePreprocessingDao spd=new SatellitePreprocessingDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql1 = "select * from " + tableName
				+ " where satelliteId="+satelliteId+" and time>=to_date('" + sdf.format(new Date(time.getTime()-1000)) + "','yyyy-mm-dd hh24:mi:ss') and time<=to_date('" + sdf.format(new Date(time.getTime()+1000)) + "','yyyy-mm-dd hh24:mi:ss')";   //获取每一个属性的数据
		List<Satellite_Preprocessing> list1 = spd.find(sql1);
		return list1;
	}
	public static HashMap<Date, List<Satellite_Preprocessing>> getSNN(String preprocessingName,String knnName,String snnName,String meanName,Date[] d,int satelliteId){//构造S近邻集合 ,meanName相关数据集中心表
		HashMap<Date, List<Satellite_Preprocessing>> knn_map=getKNN(preprocessingName,knnName,d,satelliteId);
		KnnNeighborDao knd = new KnnNeighborDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String exist_sql = "select count(*) from user_tables where table_name='" + snnName.toUpperCase()  //判断存储S近邻的表格是否存在
		+ "'";
		
        if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
        	String create_sql = "create table " + snnName
			+ " ( time DATE, neighbor_time DATE ,satelliteId NUMBER)";
        	CreateTable.create(create_sql);
        	create_sql = "create index " + snnName
        			+ "_index on "+snnName+"(time)";
            CreateTable.create(create_sql);
        }
        Date time = new Date(d[0].getTime());
		int i=0;
		HashMap<Date, List<Satellite_Preprocessing>> snn_map=new HashMap<Date, List<Satellite_Preprocessing>>();
        while (time.before(d[1])) {
        	time.setSeconds(0);
			List<KNN_Neighbor> list1=getNeighbor(knnName,time,satelliteId);//首先得到K近邻
			List<List<KNN_Neighbor>> neighbor_list=new ArrayList<List<KNN_Neighbor>>();
			for(int j=0;j<list1.size();j++){
				List<KNN_Neighbor> temp=getNeighbor(knnName,list1.get(j).getNeighbor_time(),satelliteId);//得到K近邻集合中每一条数据的近邻
				neighbor_list.add(temp);
			}
	        List<KNN_Neighbor> neighbor=top_S_neighbors(list1,neighbor_list,s);//获取S近邻
	        List<Satellite_Preprocessing> snn_list=new ArrayList<Satellite_Preprocessing>();
	        List<Satellite_Preprocessing> knn_list=knn_map.get(time);
	        for(int j=0;j<neighbor.size();j++){
	        	for(int l=0;l<knn_list.size();l++){
	        		if(knn_list.get(l).getTime()==neighbor.get(j).getNeighbor_time()){
	        			snn_list.add(knn_list.get(l));
	        		}
	        	}
	        }
	        Satellite_Preprocessing meanq=new Satellite_Preprocessing();//获取相关数据集中点
	        meanq.setTime(time);
	        for(int j=0;j<snn_list.size();j++){
	        	meanq.setSatelliteId(satelliteId);
	        	meanq.setA1025(meanq.getA1025()+snn_list.get(j).getA1025());
	        	meanq.setA1026(meanq.getA1026()+snn_list.get(j).getA1026());
	        	meanq.setA1027(meanq.getA1027()+snn_list.get(j).getA1027());
	        	meanq.setA1028(meanq.getA1028()+snn_list.get(j).getA1028());
	        	meanq.setA1029(meanq.getA1029()+snn_list.get(j).getA1029());
	        	meanq.setA1030(meanq.getA1030()+snn_list.get(j).getA1030());
	        	meanq.setA1031(meanq.getA1031()+snn_list.get(j).getA1031());
	        	meanq.setA1032(meanq.getA1032()+snn_list.get(j).getA1032());
	        	meanq.setA1033(meanq.getA1033()+snn_list.get(j).getA1033());
	        	meanq.setA1034(meanq.getA1034()+snn_list.get(j).getA1034());
	        	meanq.setA1035(meanq.getA1035()+snn_list.get(j).getA1035());
	        	meanq.setA1036(meanq.getA1036()+snn_list.get(j).getA1036());
	        	meanq.setA1048(meanq.getA1048()+snn_list.get(j).getA1048());
	        	meanq.setA1049(meanq.getA1049()+snn_list.get(j).getA1049());
	        	meanq.setA1050(meanq.getA1050()+snn_list.get(j).getA1050());
	        	meanq.setA1051(meanq.getA1051()+snn_list.get(j).getA1051());
	        	meanq.setA1052(meanq.getA1052()+snn_list.get(j).getA1052());
	        	meanq.setA1053(meanq.getA1053()+snn_list.get(j).getA1053());
	        	meanq.setA1054(meanq.getA1054()+snn_list.get(j).getA1054());
	        	meanq.setA1055(meanq.getA1055()+snn_list.get(j).getA1055());
	        	meanq.setA1056(meanq.getA1056()+snn_list.get(j).getA1056());
	        	meanq.setA1057(meanq.getA1057()+snn_list.get(j).getA1057());
	        	meanq.setA1058(meanq.getA1058()+snn_list.get(j).getA1058());
	        	meanq.setA1059(meanq.getA1059()+snn_list.get(j).getA1059());
	        	meanq.setA1060(meanq.getA1060()+snn_list.get(j).getA1060());
	        	meanq.setA1061(meanq.getA1061()+snn_list.get(j).getA1061());
	        	meanq.setA1062(meanq.getA1062()+snn_list.get(j).getA1062());
	        	meanq.setA1063(meanq.getA1063()+snn_list.get(j).getA1063());
	        	meanq.setA1075(meanq.getA1075()+snn_list.get(j).getA1075());
	        	meanq.setA1076(meanq.getA1076()+snn_list.get(j).getA1076());
	        	meanq.setA1077(meanq.getA1077()+snn_list.get(j).getA1077());
	        	meanq.setA1078(meanq.getA1078()+snn_list.get(j).getA1078());
	        	meanq.setA4201(meanq.getA4201()+snn_list.get(j).getA4201());
	        	meanq.setA4202(meanq.getA4202()+snn_list.get(j).getA4202());
	        	meanq.setA4203(meanq.getA4203()+snn_list.get(j).getA4203());
	        	meanq.setA4204(meanq.getA4204()+snn_list.get(j).getA4204());
	        	meanq.setA4205(meanq.getA4205()+snn_list.get(j).getA4205());
	        	meanq.setA2125(meanq.getA2125()+snn_list.get(j).getA2125());
	        	meanq.setA2126(meanq.getA2126()+snn_list.get(j).getA2126());
	        	meanq.setA2127(meanq.getA2127()+snn_list.get(j).getA2127());
	        	meanq.setA2128(meanq.getA2128()+snn_list.get(j).getA2128());
	        	meanq.setA2131(meanq.getA2131()+snn_list.get(j).getA2131());
	        	meanq.setA2132(meanq.getA2132()+snn_list.get(j).getA2132());
	        	meanq.setA2133(meanq.getA2133()+snn_list.get(j).getA2133());
	        	meanq.setA2136(meanq.getA2136()+snn_list.get(j).getA2136());
	        	meanq.setA2137(meanq.getA2137()+snn_list.get(j).getA2137());
	        	meanq.setA6220(meanq.getA6220()+snn_list.get(j).getA6220());
	        	meanq.setA13333(meanq.getA13333()+snn_list.get(j).getA13333());
	        	meanq.setA2140(meanq.getA2140()+snn_list.get(j).getA2140());
	        	meanq.setA2141(meanq.getA2141()+snn_list.get(j).getA2141());
	        	meanq.setA2142(meanq.getA2142()+snn_list.get(j).getA2142());
	        	meanq.setA2143(meanq.getA2143()+snn_list.get(j).getA2143());
	        	meanq.setA14346(meanq.getA14346()+snn_list.get(j).getA14346());
	        	meanq.setA14347(meanq.getA14347()+snn_list.get(j).getA14347());
	        	meanq.setA14348(meanq.getA14348()+snn_list.get(j).getA14348());
	        	meanq.setA14349(meanq.getA14349()+snn_list.get(j).getA14349());
	        	meanq.setA14350(meanq.getA14350()+snn_list.get(j).getA14350());
	        	meanq.setA14351(meanq.getA14351()+snn_list.get(j).getA14351());
	        	meanq.setA14373(meanq.getA14373()+snn_list.get(j).getA14373());
	        	meanq.setA14374(meanq.getA14374()+snn_list.get(j).getA14374());
	        	meanq.setA14375(meanq.getA14375()+snn_list.get(j).getA14375());
	        	meanq.setA14376(meanq.getA14376()+snn_list.get(j).getA14376());
	        	meanq.setA14417(meanq.getA14417()+snn_list.get(j).getA14417());
	        	meanq.setA14418(meanq.getA14418()+snn_list.get(j).getA14418());
	        	meanq.setA14419(meanq.getA14419()+snn_list.get(j).getA14419());
	        	meanq.setA13395(meanq.getA13395()+snn_list.get(j).getA13395());
	        	meanq.setA13396(meanq.getA13396()+snn_list.get(j).getA13396());
	        	meanq.setA13397(meanq.getA13397()+snn_list.get(j).getA13397());
	        	meanq.setA14340(meanq.getA14340()+snn_list.get(j).getA14340());
	        }
	        String exist_sql2 = "select count(*) from user_tables where table_name='" + meanName.toUpperCase()  //判断存储相关数据集中点的表格是否存在
			+ "'";
	        if (!TableExist.exist(exist_sql2)) {//表格不存在，则建立表格
	        	String create_sql2 = "create table " + meanName
	        			+ " ( time DATE, a1025  NUMBER,a1026  NUMBER,a1027  NUMBER,a1028  NUMBER,a1029  NUMBER,a1030  NUMBER,a1031  NUMBER,a1032  NUMBER,a1033  NUMBER,a1034  NUMBER,a1035  NUMBER,a1036  NUMBER,a1048  NUMBER,a1049  NUMBER,a1050  NUMBER,a1051  NUMBER,a1052  NUMBER,a1053  NUMBER,a1054  NUMBER,a1055  NUMBER,a1056  NUMBER,a1057  NUMBER,a1058  NUMBER,a1059  NUMBER,a1060  NUMBER,a1061  NUMBER,a1062  NUMBER,a1063  NUMBER,a1075  NUMBER,a1076  NUMBER,a1077  NUMBER,a1078  NUMBER,a4201  NUMBER,a4202  NUMBER,a4203  NUMBER,a4204  NUMBER,a4205  NUMBER,a2125  NUMBER,a2126  NUMBER,a2127  NUMBER,a2128  NUMBER,a2131  NUMBER,a2132  NUMBER,a2133  NUMBER,a2136  NUMBER,a2137  NUMBER,a6220  NUMBER,a13333 NUMBER,a2140  NUMBER,a2141  NUMBER,a2142  NUMBER,a2143  NUMBER,a14346 NUMBER,a14347 NUMBER,a14348 NUMBER,a14349 NUMBER,a14350 NUMBER,a14351 NUMBER,a14373 NUMBER,a14374 NUMBER,a14375 NUMBER,a14376 NUMBER,a14377 NUMBER,a14403 NUMBER,a14404 NUMBER,a14405 NUMBER,a14406 NUMBER,a14368 NUMBER,a14371 NUMBER,a14417 NUMBER,a14418 NUMBER,a14419 NUMBER,a13395 NUMBER,a13396 NUMBER,a13397 NUMBER,a14340 NUMBER,satelliteId NUMBER )";
				CreateTable.create(create_sql2);
				create_sql2 = "create index " + meanName
	        			+ "_index on "+meanName+"(time)";
				CreateTable.create(create_sql2);
	        }
	        SatellitePreprocessingDao spd=new SatellitePreprocessingDao();
	        spd.insert(meanName,meanq);   //将相关数据集中点添加到表中
	        snn_map.put(time, snn_list);
	        i=i++;
	        KnnNeighborDao snd=new KnnNeighborDao();
	        for(int j=0;j<neighbor.size();j++){
	        	KNN_Neighbor m=new KNN_Neighbor();
		        m.setTime(time);
		        m.setNeighbor_time(neighbor.get(j).getNeighbor_time());
		        m.setSatelliteId(satelliteId);
	        	snd.insert(snnName, m);//保存到表格中
	        }
	        time = new Date(time.getTime() + 60000);
        }
        knn_map.clear();
        return snn_map;
	}
	
	public static void calculateScore(String tableName,String preprocessingName,String knnName,String snnName,String meanName,String scoreName,int satelliteId){//scoreName异常得分表格
		double e=10^-5;
		double cta=0.45;
		//DetectionScoreDao psd = new DetectionScoreDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SatellitePreprocessingDao spd = new SatellitePreprocessingDao();
		KnnNeighborDao knd=new KnnNeighborDao();
        double[] PCos=new double[76];   //存放数据的角度余弦值
        int[] S=new int[76];//存放是否选择属性标记
        double Score1=0;   //存放数据的异常得分值
        ScoreDao sd=new ScoreDao();
        SatelliteDao std=new SatelliteDao();
        Date[] date=getDate(tableName,satelliteId);
        Date min_time=date[0];
        String exist_sql = "select count(*) from user_tables where table_name='" + preprocessingName.toUpperCase()  //判断表格是否存在
		+ "'";
        if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
        	String create_sql = "create table " + preprocessingName
			+ " ( time DATE, a1025  NUMBER,a1026  NUMBER,a1027  NUMBER,a1028  NUMBER,a1029  NUMBER,a1030  NUMBER,a1031  NUMBER,a1032  NUMBER,a1033  NUMBER,a1034  NUMBER,a1035  NUMBER,a1036  NUMBER,a1048  NUMBER,a1049  NUMBER,a1050  NUMBER,a1051  NUMBER,a1052  NUMBER,a1053  NUMBER,a1054  NUMBER,a1055  NUMBER,a1056  NUMBER,a1057  NUMBER,a1058  NUMBER,a1059  NUMBER,a1060  NUMBER,a1061  NUMBER,a1062  NUMBER,a1063  NUMBER,a1075  NUMBER,a1076  NUMBER,a1077  NUMBER,a1078  NUMBER,a4201  NUMBER,a4202  NUMBER,a4203  NUMBER,a4204  NUMBER,a4205  NUMBER,a2125  NUMBER,a2126  NUMBER,a2127  NUMBER,a2128  NUMBER,a2131  NUMBER,a2132  NUMBER,a2133  NUMBER,a2136  NUMBER,a2137  NUMBER,a6220  NUMBER,a13333 NUMBER,a2140  NUMBER,a2141  NUMBER,a2142  NUMBER,a2143  NUMBER,a14346 NUMBER,a14347 NUMBER,a14348 NUMBER,a14349 NUMBER,a14350 NUMBER,a14351 NUMBER,a14373 NUMBER,a14374 NUMBER,a14375 NUMBER,a14376 NUMBER,a14377 NUMBER,a14403 NUMBER,a14404 NUMBER,a14405 NUMBER,a14406 NUMBER,a14368 NUMBER,a14371 NUMBER,a14417 NUMBER,a14418 NUMBER,a14419 NUMBER,a13395 NUMBER,a13396 NUMBER,a13397 NUMBER,a14340 NUMBER,satelliteId NUMBER )";
        	CreateTable.create(create_sql);
        	create_sql = "create index " + preprocessingName
        			+ "_index on "+preprocessingName+"(time)";
        	CreateTable.create(create_sql);
        	Date[] d1=new Date[2];
        	d1[0]=date[0];
        	d1[1]=new Date(date[0].getTime()+num*60000);
        	DataCompression(tableName, preprocessingName,d1,satelliteId);
        }
        exist_sql = "select count(*) from user_tables where table_name='" + scoreName.toUpperCase()  //判断存储S近邻的表格是否存在
		+ "'";
        if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
        	String create_sql = "create table " + scoreName
			+ " ( time DATE, score number,satelliteId NUMBER )";
        	CreateTable.create(create_sql);
        	create_sql = "create index " + scoreName
        			+ "_index on "+scoreName+"(time)";
                	CreateTable.create(create_sql);
        	Date[] d1=new Date[2];
        	d1[0]=date[0];
        	d1[1]=new Date(date[0].getTime()+num*60000);
        	getKNN(preprocessingName,knnName,d1,satelliteId);
        }
        else{
        	String sql_preprocessing = "select max(time) as time from " + scoreName+" where satelliteId="+satelliteId; //如果部分数据已完成异常检测则重新修改数据起点
			List<Score> list2 = sd.find(sql_preprocessing);
			if (list2.get(0).getTime() != null) {
				min_time = new Date(list2.get(0).getTime().getTime() + 60000);
			}
        }
        Date time = min_time;
		int i=0;
		Date start_date = start_date=new Date(time.getTime()-num*60000);
        Date end_date = time;
        List<String> attribute_list = Tmid.getTmid(); // 获取属性列表
		while (time.before(date[1])) {
			time.setSeconds(0);
			System.out.println(time.getTime());
			System.out.println(date[0].getTime()+num*60000);
			if (time.getTime()<date[0].getTime()+num*60000){
	        	
        	}
	        else{
	        	String insert_sql = "insert into " + preprocessingName + " values(to_date('" + sdf.format(time)
				+ "','yyyy-mm-dd hh24:mi:ss')";
				for (int ii = 0; ii < attribute_list.size(); ii++) {
					String attribute = attribute_list.get(ii);
					String sql1 = "select to_date('" + sdf.format(time)
					+ "','yyyy-mm-dd hh24:mi:ss') as time,avg(value) as value" + " from " + tableName
					+ " where time>=to_date('" + sdf.format(time) + "','yyyy-mm-dd hh24:mi:ss') and time<to_date('"
					+ sdf.format(new Date(time.getTime() + 60000)) + "','yyyy-mm-dd hh24:mi:ss') and attribute='"
					+ attribute + "' and satelliteId="+satelliteId;   //获取每一个属性的数据
					//System.out.println(sql1);
					List<Satellite> list1 = std.find(sql1);
					if (list1.size()!=0) {
						Satellite s = list1.get(0);
						insert_sql += "," + s.getValue();
					}
					list1.clear();
				}
				insert_sql +=","+satelliteId+ ")";
				System.out.println(insert_sql);
	        	int v = spd.insert(insert_sql);
	        }
			
        	Satellite_Preprocessing s1=getData(preprocessingName,time,satelliteId).get(0);  //当前的一条数据记录
        	if (time.getTime()<date[0].getTime()+num*60000){
        		start_date = date[0];
                end_date = new Date(date[0].getTime()+num*60000);
        	}
        	else{
        		start_date = start_date=new Date(time.getTime()-num*60000);
        		end_date = time;
        	}
            
        	exist_sql = "select count(*) from user_tables where table_name='" + knnName.toUpperCase()  //判断存储相关数据集中点的表格是否存在
			+ "'";
	        if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
	        	String create_sql = "create table " + knnName
	        			+ " ( time DATE, neighbor_time DATE,satelliteId NUMBER )";
	            CreateTable.create(create_sql);
	            create_sql = "create index " + knnName
	        			+ "_index on "+knnName+"(time)";
	            CreateTable.create(create_sql);
	        }
	        String sql1 = "select * from " + preprocessingName
        			+ " where satelliteId="+satelliteId+" and time>=to_date('" + sdf.format(start_date) + "','yyyy-mm-dd hh24:mi:ss') and time<to_date('"
        			+ sdf.format(end_date) + "','yyyy-mm-dd hh24:mi:ss') order by time";   //获取每一个属性的数据
        	List<Satellite_Preprocessing> list1 = spd.find(sql1);
        	List<Satellite_Preprocessing> neighbor=top_K_neighbors(s1,list1,k);//获取K近邻
        	List<KNN_Neighbor> my_neighbor_list=new ArrayList<KNN_Neighbor>();
        	for(int j=0;j<neighbor.size();j++){  //保存K近邻
	        	KNN_Neighbor m=new KNN_Neighbor();
		        m.setTime(time);
		        m.setNeighbor_time(neighbor.get(j).getTime());
		        if (time.getTime()<date[0].getTime()+num*60000){
		        	
	        	}
		        else{
		        	knd.insert(knnName, m);
		        }
	        	my_neighbor_list.add(m);
	        }
        	List<List<KNN_Neighbor>> neighbor_list=new ArrayList<List<KNN_Neighbor>>();
			for(int j=0;j<my_neighbor_list.size();j++){
				List<KNN_Neighbor> temp=getNeighbor(knnName,my_neighbor_list.get(j).getNeighbor_time(),satelliteId);//得到K近邻集合中每一条数据的近邻
				neighbor_list.add(temp);
			}
			List<KNN_Neighbor> snn_neighbor=top_S_neighbors(my_neighbor_list,neighbor_list,s);//获取S近邻
			List<Satellite_Preprocessing> snn_list=new ArrayList<Satellite_Preprocessing>();
	        List<Satellite_Preprocessing> knn_list=neighbor;
	        for(int j=0;j<snn_neighbor.size();j++){
	        	for(int l=0;l<knn_list.size();l++){
	        		if(knn_list.get(l).getTime()==snn_neighbor.get(j).getNeighbor_time()){
	        			snn_list.add(knn_list.get(l));
	        		}
	        	}
	        }
			Satellite_Preprocessing meanq=new Satellite_Preprocessing();//获取相关数据集中点
	        meanq.setTime(time);
	        for(int j=0;j<snn_list.size();j++){
	        	meanq.setSatelliteId(satelliteId);
	        	meanq.setA1025(meanq.getA1025()+snn_list.get(j).getA1025()/snn_list.size());
	        	meanq.setA1026(meanq.getA1026()+snn_list.get(j).getA1026()/snn_list.size());
	        	meanq.setA1027(meanq.getA1027()+snn_list.get(j).getA1027()/snn_list.size());
	        	meanq.setA1028(meanq.getA1028()+snn_list.get(j).getA1028()/snn_list.size());
	        	meanq.setA1029(meanq.getA1029()+snn_list.get(j).getA1029()/snn_list.size());
	        	meanq.setA1030(meanq.getA1030()+snn_list.get(j).getA1030()/snn_list.size());
	        	meanq.setA1031(meanq.getA1031()+snn_list.get(j).getA1031()/snn_list.size());
	        	meanq.setA1032(meanq.getA1032()+snn_list.get(j).getA1032()/snn_list.size());
	        	meanq.setA1033(meanq.getA1033()+snn_list.get(j).getA1033()/snn_list.size());
	        	meanq.setA1034(meanq.getA1034()+snn_list.get(j).getA1034()/snn_list.size());
	        	meanq.setA1035(meanq.getA1035()+snn_list.get(j).getA1035()/snn_list.size());
	        	meanq.setA1036(meanq.getA1036()+snn_list.get(j).getA1036()/snn_list.size());
	        	meanq.setA1048(meanq.getA1048()+snn_list.get(j).getA1048()/snn_list.size());
	        	meanq.setA1049(meanq.getA1049()+snn_list.get(j).getA1049()/snn_list.size());
	        	meanq.setA1050(meanq.getA1050()+snn_list.get(j).getA1050()/snn_list.size());
	        	meanq.setA1051(meanq.getA1051()+snn_list.get(j).getA1051()/snn_list.size());
	        	meanq.setA1052(meanq.getA1052()+snn_list.get(j).getA1052()/snn_list.size());
	        	meanq.setA1053(meanq.getA1053()+snn_list.get(j).getA1053()/snn_list.size());
	        	meanq.setA1054(meanq.getA1054()+snn_list.get(j).getA1054()/snn_list.size());
	        	meanq.setA1055(meanq.getA1055()+snn_list.get(j).getA1055()/snn_list.size());
	        	meanq.setA1056(meanq.getA1056()+snn_list.get(j).getA1056()/snn_list.size());
	        	meanq.setA1057(meanq.getA1057()+snn_list.get(j).getA1057()/snn_list.size());
	        	meanq.setA1058(meanq.getA1058()+snn_list.get(j).getA1058()/snn_list.size());
	        	meanq.setA1059(meanq.getA1059()+snn_list.get(j).getA1059()/snn_list.size());
	        	meanq.setA1060(meanq.getA1060()+snn_list.get(j).getA1060()/snn_list.size());
	        	meanq.setA1061(meanq.getA1061()+snn_list.get(j).getA1061()/snn_list.size());
	        	meanq.setA1062(meanq.getA1062()+snn_list.get(j).getA1062()/snn_list.size());
	        	meanq.setA1063(meanq.getA1063()+snn_list.get(j).getA1063()/snn_list.size());
	        	meanq.setA1075(meanq.getA1075()+snn_list.get(j).getA1075()/snn_list.size());
	        	meanq.setA1076(meanq.getA1076()+snn_list.get(j).getA1076()/snn_list.size());
	        	meanq.setA1077(meanq.getA1077()+snn_list.get(j).getA1077()/snn_list.size());
	        	meanq.setA1078(meanq.getA1078()+snn_list.get(j).getA1078()/snn_list.size());
	        	meanq.setA4201(meanq.getA4201()+snn_list.get(j).getA4201()/snn_list.size());
	        	meanq.setA4202(meanq.getA4202()+snn_list.get(j).getA4202()/snn_list.size());
	        	meanq.setA4203(meanq.getA4203()+snn_list.get(j).getA4203()/snn_list.size());
	        	meanq.setA4204(meanq.getA4204()+snn_list.get(j).getA4204()/snn_list.size());
	        	meanq.setA4205(meanq.getA4205()+snn_list.get(j).getA4205()/snn_list.size());
	        	meanq.setA2125(meanq.getA2125()+snn_list.get(j).getA2125()/snn_list.size());
	        	meanq.setA2126(meanq.getA2126()+snn_list.get(j).getA2126()/snn_list.size());
	        	meanq.setA2127(meanq.getA2127()+snn_list.get(j).getA2127()/snn_list.size());
	        	meanq.setA2128(meanq.getA2128()+snn_list.get(j).getA2128()/snn_list.size());
	        	meanq.setA2131(meanq.getA2131()+snn_list.get(j).getA2131()/snn_list.size());
	        	meanq.setA2132(meanq.getA2132()+snn_list.get(j).getA2132()/snn_list.size());
	        	meanq.setA2133(meanq.getA2133()+snn_list.get(j).getA2133()/snn_list.size());
	        	meanq.setA2136(meanq.getA2136()+snn_list.get(j).getA2136()/snn_list.size());
	        	meanq.setA2137(meanq.getA2137()+snn_list.get(j).getA2137()/snn_list.size());
	        	meanq.setA6220(meanq.getA6220()+snn_list.get(j).getA6220()/snn_list.size());
	        	meanq.setA13333(meanq.getA13333()+snn_list.get(j).getA13333()/snn_list.size());
	        	meanq.setA2140(meanq.getA2140()+snn_list.get(j).getA2140()/snn_list.size());
	        	meanq.setA2141(meanq.getA2141()+snn_list.get(j).getA2141()/snn_list.size());
	        	meanq.setA2142(meanq.getA2142()+snn_list.get(j).getA2142()/snn_list.size());
	        	meanq.setA2143(meanq.getA2143()+snn_list.get(j).getA2143()/snn_list.size());
	        	meanq.setA14346(meanq.getA14346()+snn_list.get(j).getA14346()/snn_list.size());
	        	meanq.setA14347(meanq.getA14347()+snn_list.get(j).getA14347()/snn_list.size());
	        	meanq.setA14348(meanq.getA14348()+snn_list.get(j).getA14348()/snn_list.size());
	        	meanq.setA14349(meanq.getA14349()+snn_list.get(j).getA14349()/snn_list.size());
	        	meanq.setA14350(meanq.getA14350()+snn_list.get(j).getA14350()/snn_list.size());
	        	meanq.setA14351(meanq.getA14351()+snn_list.get(j).getA14351()/snn_list.size());
	        	meanq.setA14373(meanq.getA14373()+snn_list.get(j).getA14373()/snn_list.size());
	        	meanq.setA14374(meanq.getA14374()+snn_list.get(j).getA14374()/snn_list.size());
	        	meanq.setA14375(meanq.getA14375()+snn_list.get(j).getA14375()/snn_list.size());
	        	meanq.setA14376(meanq.getA14376()+snn_list.get(j).getA14376()/snn_list.size());
	        	meanq.setA14417(meanq.getA14417()+snn_list.get(j).getA14417()/snn_list.size());
	        	meanq.setA14418(meanq.getA14418()+snn_list.get(j).getA14418()/snn_list.size());
	        	meanq.setA14419(meanq.getA14419()+snn_list.get(j).getA14419()/snn_list.size());
	        	meanq.setA13395(meanq.getA13395()+snn_list.get(j).getA13395()/snn_list.size());
	        	meanq.setA13396(meanq.getA13396()+snn_list.get(j).getA13396()/snn_list.size());
	        	meanq.setA13397(meanq.getA13397()+snn_list.get(j).getA13397()/snn_list.size());
	        	meanq.setA14340(meanq.getA14340()+snn_list.get(j).getA14340()/snn_list.size());
	        }
	        exist_sql = "select count(*) from user_tables where table_name='" + snnName.toUpperCase()  //判断存储S近邻的表格是否存在
			+ "'";
			
	        if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
	        	String create_sql = "create table " + snnName
				+ " ( time DATE, neighbor_time DATE,satelliteId NUMBER )";
	        	CreateTable.create(create_sql);
	        	create_sql = "create index " + snnName
	    				+ "_index on "+snnName+"(time)";
	    	    CreateTable.create(create_sql);
	        }
	        for(int j=0;j<snn_neighbor.size();j++){
	        	KNN_Neighbor m=new KNN_Neighbor();
		        m.setTime(time);
		        m.setNeighbor_time(snn_neighbor.get(j).getNeighbor_time());
		        m.setSatelliteId(satelliteId);
	        	knd.insert(snnName, m);//保存到表格中
	        }
	        String exist_sql2 = "select count(*) from user_tables where table_name='" + meanName.toUpperCase()  //判断存储相关数据集中点的表格是否存在
			+ "'";
	        if (!TableExist.exist(exist_sql2)) {//表格不存在，则建立表格
	        	String create_sql2 = "create table " + meanName
	        			+ " (time DATE, a1025  NUMBER,a1026  NUMBER,a1027  NUMBER,a1028  NUMBER,a1029  NUMBER,a1030  NUMBER,a1031  NUMBER,a1032  NUMBER,a1033  NUMBER,a1034  NUMBER,a1035  NUMBER,a1036  NUMBER,a1048  NUMBER,a1049  NUMBER,a1050  NUMBER,a1051  NUMBER,a1052  NUMBER,a1053  NUMBER,a1054  NUMBER,a1055  NUMBER,a1056  NUMBER,a1057  NUMBER,a1058  NUMBER,a1059  NUMBER,a1060  NUMBER,a1061  NUMBER,a1062  NUMBER,a1063  NUMBER,a1075  NUMBER,a1076  NUMBER,a1077  NUMBER,a1078  NUMBER,a4201  NUMBER,a4202  NUMBER,a4203  NUMBER,a4204  NUMBER,a4205  NUMBER,a2125  NUMBER,a2126  NUMBER,a2127  NUMBER,a2128  NUMBER,a2131  NUMBER,a2132  NUMBER,a2133  NUMBER,a2136  NUMBER,a2137  NUMBER,a6220  NUMBER,a13333 NUMBER,a2140  NUMBER,a2141  NUMBER,a2142  NUMBER,a2143  NUMBER,a14346 NUMBER,a14347 NUMBER,a14348 NUMBER,a14349 NUMBER,a14350 NUMBER,a14351 NUMBER,a14373 NUMBER,a14374 NUMBER,a14375 NUMBER,a14376 NUMBER,a14377 NUMBER,a14403 NUMBER,a14404 NUMBER,a14405 NUMBER,a14406 NUMBER,a14368 NUMBER,a14371 NUMBER,a14417 NUMBER,a14418 NUMBER,a14419 NUMBER,a13395 NUMBER,a13396 NUMBER,a13397 NUMBER,a14340 NUMBER,satelliteId NUMBER )";
				CreateTable.create(create_sql2);
				create_sql2 = "create index " + meanName
	        			+ "_index on "+meanName+"(time)";
				CreateTable.create(create_sql2);
	        }
	        spd.insert(meanName,meanq);   //将相关数据集中点添加到表中
        	String q_sql="select * from "+meanName+" where satelliteId="+satelliteId+" and time=to_date('" + sdf.format(time) + "','yyyy-mm-dd hh24:mi:ss')";
        	List<Satellite_Preprocessing> list=spd.find(q_sql);
        	Satellite_Preprocessing s2=list.get(0);//当前数据记录对应的中心表
        	double[] p=s1.getData();
        	double[] q=s2.getData();
        	double[] l=p;
        	for(int j=0;j<l.length;j++){
        		l[j]=l[j]-q[j];
        		if(l[j]==0){
        			l[j]=e;
        		}
        		double totalsum=0;
        		for(int kk=0;kk<l.length;kk++){
        			if(kk!=j){
        				totalsum=totalsum+(Math.abs(l[j])/Math.sqrt(l[j]*l[j]+l[kk]*l[kk])); //计算角度余弦值
        			}
        		}
        		PCos[j]=totalsum/(l.length-1); //计算角度余弦值的平均值
        	}
        	double G=(1+cta)*sum(PCos)/l.length;  //计算筛选属性的阈值
        	for(int j=0;j<l.length;j++){
        		if(PCos[j]>G){
        			S[j]=1;  //标记为选择属性
        		}
        	}
        	int d=sum(S);
        	double[][] x=new double[1][d];
        	double[][] y=new double[1][d];
        	int ii=0;
        	for(int kk=0;kk<p.length;kk++){
        		if(S[kk]==1){
        			x[0][ii]=p[kk];
        			y[0][ii]=q[kk];
        			ii++;
        		}
        	}
        	List<Satellite_Preprocessing> rp=snn_list;
        	double[][] rpl=new double[rp.size()][76];
        	double[][] rp1=new double[rp.size()][d];
        	
        	for(int kk=0;kk<rp.size();kk++){
        		rpl[kk]=rp.get(kk).getData();
        		ii=0;
        		for(int kk2=0;kk2<rpl[kk].length;kk2++){
            		if(S[kk2]==1){
            			rp1[kk][ii]=rpl[kk][kk2];
            			ii++;
            		}
            	}
        	}
        	
        	Matrix rp2=new Matrix(rp1);
        	Matrix C=cov(rp2);
        	//Matrix speye = new Matrix(d,d);//生成一个单位矩阵
        	//Matrix matrix=C.solveTranspose(speye);
        	Matrix matrix=Inverse.getInverse(C);
        	Matrix matrix_x=new Matrix(x);
        	Matrix matrix_y=new Matrix(y);
        	double L=matrix_x.minus(matrix_y).times(matrix).times(matrix_x.minus(matrix_y).transpose()).getArray()[0][0];
        	if(d==0){
        		Score1=0;
        	}
        	else{
        		Score1=Math.sqrt(Math.abs(L))/d;
        	}
        	Score detection_score=new Score();
        	detection_score.setTime(time);
        	detection_score.setScore(Score1);
        	detection_score.setSatelliteId(satelliteId);
        	sd.insert(scoreName, detection_score);   //将异常得分写入异常得分表中
        	time = new Date(time.getTime() + 60000);
        	i++;
        }
	}
	 public static double sum(double[] array){//数组求和
		 double sum=0;
		 for(int i=0;i<array.length;i++){
			 sum+=array[i];
		 }
		 return sum;
	 }
	 public static int sum(int[] array){//数组求和
		 int sum=0;
		 for(int i=0;i<array.length;i++){
			 sum+=array[i];
		 }
		 return sum;
	 }
	 public static int getCount(double[] inputData) {//计算数组元素个数
		  if (inputData == null)
		   return -1;

		  return inputData.length;
		 }
	 public static double getAverage(double[] inputData) {//计算均值
		  if (inputData == null || inputData.length == 0)
		   return -1;
		  int len = inputData.length;
		  double result;
		  result = sum(inputData) / len;
		  
		  return result;
		 }
	 public static double getSquareSum(double[] inputData) {//计算平方和
		  if(inputData==null||inputData.length==0)
		      return -1;
		     int len=inputData.length;
		  double sqrsum = 0.0;
		  for (int i = 0; i <len; i++) {
		   sqrsum = sqrsum + inputData[i] * inputData[i];
		  }

		  
		  return sqrsum;
		 }
	 public static double getVariance(double[] inputData) {//计算方差
		  int count = getCount(inputData);
		  double sqrsum = getSquareSum(inputData);
		  double average = getAverage(inputData);
		  double result;
		  result = (sqrsum - count * average * average) / count;

		     return result; 
		 }
	 public static Matrix cov(Matrix rp2){
		 int row=rp2.getRowDimension();
		 int col=rp2.getColumnDimension();
		 double[][] rp1=rp2.getArray();
		 double[][] C=new double[col][col];
		 for(int i=0;i<col;i++){
			 for(int j=0;j<=i;j++){
				 double[] dd=rp2.getMatrix(0, row-1,i,i).getColumnPackedCopy();  //取第i列
				 if(i==j){
					 C[i][j]=getVariance(dd);
				 }
				 else{
					 double avg=getAverage(dd);
					 double[][] new_dd=new double[dd.length][1];
					 for(int kk=0;kk<dd.length;kk++){
						 new_dd[kk][0]=dd[kk]-avg;
					 }
					 double[] dd_j=rp2.getMatrix(0, row-1,j,j).getColumnPackedCopy();  //取第j列
					 double avg_j=getAverage(dd_j);
					 double[][] new_dd_j=new double[dd_j.length][1];
					 for(int kk=0;kk<dd_j.length;kk++){
						 new_dd_j[kk][0]=dd_j[kk]-avg_j;
					 }
					 Matrix temp=new Matrix(new_dd).arrayTimes(new Matrix(new_dd_j));
					 double[][] temp1=temp.getArray();
					 int total_sum=0;
					 for(int kk=0;kk<temp1.length;kk++){
						 total_sum+=sum(temp1[kk]);
					 }
					 C[i][j]=total_sum/(row-1);
					 C[j][i]=C[i][j];
					 
				 }
			 }
		 }
		 return new Matrix(C);
		 
	 }
	
	public static void getDetectionTime(String tableName,int satelliteId){
		ScoreDao sd=new ScoreDao();
		String sql="select avg(score)+stddev(score)*3 as score from "+tableName+" where satelliteId="+satelliteId; //统计异常阈值
		List<Score> list=sd.find(sql);
		double threshold=list.get(0).getScore();
		String select_sql="select * from "+tableName+" where score>"+threshold+" and satelliteId="+satelliteId;  //统计超过阈值的异常时间
		List<Score> result=sd.find(select_sql);
		
	}
	public static void Detection2(String tableName) {
		// TODO Auto-generated method stub
		//String tableName="satellite188_1";
		String preprocessingName="DataPreprocessing";
		String knnName="KnnNeighbor3";
		String snnName="SnnNeighbor3";
		String meanName="MeanNeighbor3";
		String scoreName="DetectionScore3";
		int satelliteId=188;
		calculateScore(tableName,preprocessingName,knnName,snnName,meanName,scoreName,satelliteId);
	}

}
