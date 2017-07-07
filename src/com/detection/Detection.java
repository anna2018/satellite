package com.detection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.dao.KnnNeighborDao;
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

public class Detection {
	public static Date[] getDate(String preprocessingName,int satelliteId){
		SatellitePreprocessingDao spd = new SatellitePreprocessingDao();
		String sql = "select min(time) as time  from " + preprocessingName+" where satelliteId="+satelliteId; //获取数据起点
		List<Satellite> min_list = spd.find(sql);
		Date min_time = min_list.get(0).getTime();
		sql = "select max(time) as time  from " + preprocessingName+" where satelliteId="+satelliteId;  //获取数据终点
		List<Satellite> max_list = spd.find(sql);
		Date max_time = max_list.get(0).getTime();
		Date[] d=new Date[2];
		d[0]=min_time;
		d[1]=max_time;
		return d;
	}
	public static int[] sort(double[] distance,int k){
		HashMap map = new HashMap();
		int[] position=new int[k+1];
        for (int i = 0; i < distance.length; i++) {
            map.put(distance[i], i); // 将值和下标存入Map
        }
 
        // 排列
        List list = new ArrayList();
        Arrays.sort(distance); // 升序排列
        for (int i = 0; i < distance.length; i++) {
            list.add(distance[i]);
        }
        // 查找原始下标
        for (int i = 0; i < k+1; i++) {
            position[i]=(int) map.get(distance[i]);
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
	public static HashMap<Date, List<Satellite_Preprocessing>> getKNN(String preprocessingName,String knnName,int satelliteId){//preprocessingName存储预处理之后的数据，knnName存储k近邻数据
		//List<String> attribute_list=Tmid.getTmid(); // 获取属性列表
		Date[] d=getDate(preprocessingName,satelliteId);
		SatellitePreprocessingDao spd = new SatellitePreprocessingDao();
		KnnNeighborDao knd=new KnnNeighborDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String exist_sql = "select count(*) from user_tables where table_name='" + knnName.toUpperCase()  //判断存储k近邻的表格是否存在
		+ "'";
		
        if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
        	String create_sql = "create table " + knnName
			+ " (satelliteId NUMBER, time DATE, neighbor_time DATE )";
        	CreateTable.create(create_sql);
        }
        else{
        	String sql_preprocessing = "select max(time) as time from " + knnName+" where satelliteId="+satelliteId; //如果部分数据已完成构建k近邻操作则重新修改数据起点
			List<Satellite_Preprocessing> list2 = knd.find(sql_preprocessing);
			if (list2.get(0).getTime() != null) {
				d[0] = new Date(list2.get(0).getTime().getTime() + 60000);
			}
        }
        Date time = new Date(d[0].getTime());
		int k=500;
		int s=250;
		int num=1440;
		int i=0;
		HashMap<Date,List<Satellite_Preprocessing>> knn_map=new HashMap<Date,List<Satellite_Preprocessing>>();
        while (time.before(d[1])) {
        	Date start_date = d[0];
        	if(time.getTime()-num*6000>start_date.getTime()){
        		start_date=new Date(time.getTime()-num*6000);
        	}
            Date end_date = d[1];
            if(time.getTime()+num*6000<end_date.getTime()){
            	end_date=new Date(time.getTime()+num*6000);
            }
			String sql1 = "select * from " + preprocessingName
			+ " where time>=to_date('" + sdf.format(start_date) + "','yyyy-mm-dd hh24:mi:ss') and time<to_date('"
			+ sdf.format(end_date) + "','yyyy-mm-dd hh24:mi:ss') and satelliteId="+satelliteId+" order by time";   //获取每一个属性的数据
	        List<Satellite_Preprocessing> list1 = spd.find(sql1);
	        List<Satellite_Preprocessing> neighbor=top_K_neighbors(list1.get(i),list1,k);//获取K近邻
	        knn_map.put(time, neighbor);
	        i=i++;
	        for(int j=0;j<neighbor.size();j++){
	        	KNN_Neighbor m=new KNN_Neighbor();
		        m.setTime(time);
		        m.setNeighbor_time(neighbor.get(j).getTime());
	        	knd.insert(knnName, m);
	        }
	        time = new Date(time.getTime() + 60000);
//	        double[][] new_data=new double[list1.size()][attribute_list.size()];
//	        for(int i=0;i<list1.size();i++){
//	        	int j=0;
//	        	new_data[i][j]=list1.get(i).getA1025();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1026();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1027();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1028();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1029();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1030();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1031();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1032();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1033();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1034();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1035();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1036();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1048();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1049();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1050();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1051();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1052();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1053();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1054();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1055();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1056();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1057();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1058();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1059();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1060();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1061();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1062();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1063();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1075();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1076();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1077();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA1078();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA4201();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA4202();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA4203();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA4204();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA4205();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2125();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2126();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2127();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2128();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2131();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2132();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2133();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2136();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2137();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA6220();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA13333();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2140();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2141();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2142();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA2143();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14346();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14347();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14348();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14349();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14350();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14351();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14373();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14374();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14375();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14376();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14377();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14403();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14404();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14405();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14406();  								
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14368();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14371();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14417();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14418();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14419();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA13395();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA13396();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA13397();
//	        	j=j++;
//	        	new_data[i][j]=list1.get(i).getA14340();
//	        }
		}
        return knn_map;
	}
	
	public static int common_nearest_neighbors(List<KNN_Neighbor> list1,List<KNN_Neighbor> list2){
		int count=0;
		for(int i=0;i<list1.size();i++){
			for(int j=0;j<list2.size();j++){
				if(list1.get(i).getNeighbor_time()==list2.get(j).getNeighbor_time()){
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
//	public static HashMap<Date,Satellite_Preprocessing> getData(String preprocessingName){
//		Date[] d=getDate(preprocessingName);
//		SatellitePreprocessingDao spd = new SatellitePreprocessingDao();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String sql="select * from " + preprocessingName
//				+ " where time>=to_date('" + sdf.format(d[0]) + "','yyyy-mm-dd hh24:mi:ss') and time<to_date('"
//				+ sdf.format(d[1]) + "','yyyy-mm-dd hh24:mi:ss') ";;
//		List<Satellite_Preprocessing> data_list=spd.find(sql);
//		HashMap<Date,Satellite_Preprocessing> map=new HashMap<Date,Satellite_Preprocessing>();
//		for(int i=0;i<data_list.size();i++){
//			map.put(data_list.get(i).getTime(), data_list.get(i));
//		}
//		return map;
//	}
	public static List<KNN_Neighbor> getNeighbor(String tableName,Date time,int satelliteId){//根据时间查询近邻集合
		KnnNeighborDao knd=new KnnNeighborDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql1 = "select * from " + tableName
				+ " where time=to_date('" + sdf.format(time) + "','yyyy-mm-dd hh24:mi:ss') and satelliteId="+satelliteId+" order by neighbor_time";   //获取每一个属性的数据
		List<KNN_Neighbor> list1 = knd.find(sql1);
		return list1;
	}
	public static List<Satellite_Preprocessing> getData(String tableName,Date time,int satelliteId){//根据时间查询数据记录
		KnnNeighborDao knd=new KnnNeighborDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql1 = "select * from " + tableName
				+ " where time=to_date('" + sdf.format(time) + "','yyyy-mm-dd hh24:mi:ss') and satelliteId="+satelliteId;   //获取每一个属性的数据
		List<Satellite_Preprocessing> list1 = knd.find(sql1);
		return list1;
	}
	public static HashMap<Date, List<Satellite_Preprocessing>> getSNN(String preprocessingName,String knnName,String snnName,String meanName,int satelliteId){//构造S近邻集合 ,meanName相关数据集中心表
		HashMap<Date, List<Satellite_Preprocessing>> knn_map=getKNN(preprocessingName,knnName,satelliteId);
		Date[] d=getDate(preprocessingName,satelliteId);
		KnnNeighborDao knd = new KnnNeighborDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String exist_sql = "select count(*) from user_tables where table_name='" + snnName.toUpperCase()  //判断存储S近邻的表格是否存在
		+ "'";
		
        if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
        	String create_sql = "create table " + snnName
			+ " (satelliteId NUMBER,time DATE, neighbor_time DATE )";
        	CreateTable.create(create_sql);
        }
        else{
        	String sql_preprocessing = "select max(time) as time from " + snnName+" where satelliteId="+satelliteId; //如果部分数据已完成构建k近邻操作则重新修改数据起点
			List<Satellite_Preprocessing> list2 = knd.find(sql_preprocessing);
			if (list2.get(0).getTime() != null) {
				d[0] = new Date(list2.get(0).getTime().getTime() + 60000);
			}
        }
        Date time = new Date(d[0].getTime());
		int s=250;
		int i=0;
		HashMap<Date, List<Satellite_Preprocessing>> snn_map=new HashMap<Date, List<Satellite_Preprocessing>>();
        while (time.before(d[1])) {
			List<KNN_Neighbor> list1=getNeighbor(knnName,time,satelliteId);//首先得到K近邻
			List<List<KNN_Neighbor>> neighbor_list=new ArrayList<List<KNN_Neighbor>>();
			for(int j=0;j<list1.size();j++){
				List<KNN_Neighbor> temp=getNeighbor(knnName,list1.get(j).getTime(),satelliteId);//得到K近邻集合中每一条数据的近邻
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
	        			+ " (satelliteId NUMBER,time DATE, a1025  NUMBER,a1026  NUMBER,a1027  NUMBER,a1028  NUMBER,a1029  NUMBER,a1030  NUMBER,a1031  NUMBER,a1032  NUMBER,a1033  NUMBER,a1034  NUMBER,a1035  NUMBER,a1036  NUMBER,a1048  NUMBER,a1049  NUMBER,a1050  NUMBER,a1051  NUMBER,a1052  NUMBER,a1053  NUMBER,a1054  NUMBER,a1055  NUMBER,a1056  NUMBER,a1057  NUMBER,a1058  NUMBER,a1059  NUMBER,a1060  NUMBER,a1061  NUMBER,a1062  NUMBER,a1063  NUMBER,a1075  NUMBER,a1076  NUMBER,a1077  NUMBER,a1078  NUMBER,a4201  NUMBER,a4202  NUMBER,a4203  NUMBER,a4204  NUMBER,a4205  NUMBER,a2125  NUMBER,a2126  NUMBER,a2127  NUMBER,a2128  NUMBER,a2131  NUMBER,a2132  NUMBER,a2133  NUMBER,a2136  NUMBER,a2137  NUMBER,a6220  NUMBER,a13333 NUMBER,a2140  NUMBER,a2141  NUMBER,a2142  NUMBER,a2143  NUMBER,a14346 NUMBER,a14347 NUMBER,a14348 NUMBER,a14349 NUMBER,a14350 NUMBER,a14351 NUMBER,a14373 NUMBER,a14374 NUMBER,a14375 NUMBER,a14376 NUMBER,a14377 NUMBER,a14403 NUMBER,a14404 NUMBER,a14405 NUMBER,a14406 NUMBER,a14368 NUMBER,a14371 NUMBER,a14417 NUMBER,a14418 NUMBER,a14419 NUMBER,a13395 NUMBER,a13396 NUMBER,a13397 NUMBER,a14340 NUMBER )";
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
		        m.setNeighbor_time(neighbor.get(j).getTime());
	        	snd.insert(snnName, m);//保存到表格中
	        }
	        time = new Date(time.getTime() + 60000);
        }
        knn_map.clear();
        return snn_map;
	}
	
	public static void calculatePCos(String preprocessingName,String knnName,String snnName,String meanName,String scoreName,int satelliteId){//scoreName异常得分表格
		HashMap<Date, List<Satellite_Preprocessing>> snn_map=getSNN(preprocessingName,knnName,snnName,meanName,satelliteId);
		double e=10^-5;
		double cta=0.45;
		ScoreDao sd=new ScoreDao();
		Date[] date=getDate(preprocessingName,satelliteId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String exist_sql = "select count(*) from user_tables where table_name='" + scoreName.toUpperCase()  //判断存储S近邻的表格是否存在
		+ "'";
		
        if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
        	String create_sql = "create table " + scoreName
			+ " (satelliteId NUMBER,time DATE, pcos double )";
        	CreateTable.create(create_sql);
        }
        else{
        	String sql_preprocessing = "select max(time) as time from " + scoreName+" where satelliteId="+satelliteId; //如果部分数据已完成构建k近邻操作则重新修改数据起点
			List<Satellite_Preprocessing> list2 = sd.find(sql_preprocessing);
			if (list2.get(0).getTime() != null) {
				date[0] = new Date(list2.get(0).getTime().getTime() + 60000);
			}
        }
        SatellitePreprocessingDao spd = new SatellitePreprocessingDao();
		String sql="select * from " + preprocessingName
				+ " where time>=to_date('" + sdf.format(date[0]) + "','yyyy-mm-dd hh24:mi:ss') and time<to_date('"
				+ sdf.format(date[1]) + "','yyyy-mm-dd hh24:mi:ss') and satelliteId="+satelliteId+" order by time";;
		List<Satellite_Preprocessing> data_list=spd.find(sql);
        double[][] PCos=new double[data_list.size()][76];   //存放数据的角度余弦值
        int[][] S=new int[data_list.size()][76];//存放是否选择属性标记
        double[] Score=new double[data_list.size()];   //存放数据的异常得分值
        for(int i=0;i<data_list.size();i++){
        	Satellite_Preprocessing s1=data_list.get(i);  //当前的一条数据记录
        	String q_sql="select * from "+meanName+" where satelliteId="+satelliteId+" time=to_date('" + sdf.format(data_list.get(i).getTime()) + "','yyyy-mm-dd hh24:mi:ss')";
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
        		for(int k=0;k<l.length;k++){
        			if(k!=j){
        				totalsum=totalsum+(Math.abs(l[j])/Math.sqrt(l[j]*l[j]+l[k]*l[k])); //计算角度余弦值
        			}
        		}
        		PCos[i][j]=totalsum/(l.length-1); //计算角度余弦值的平均值
        	}
        	double G=(1+cta)*sum(PCos[i])/l.length;  //计算筛选属性的阈值
        	for(int j=0;j<l.length;j++){
        		if(PCos[i][j]>G){
        			S[i][j]=1;  //标记为选择属性
        		}
        	}
        	int d=sum(S[i]);
        	double[][] x=new double[1][d];
        	double[][] y=new double[1][d];
        	int ii=0;
        	for(int k=0;k<p.length;k++){
        		if(S[i][k]==1){
        			x[0][ii]=p[k];
        			y[0][ii]=q[k];
        			ii++;
        		}
        	}
        	List<Satellite_Preprocessing> rp=snn_map.get(data_list.get(i).getTime());
        	double[][] rp1=new double[rp.size()][76];
        	for(int k=0;k<rp.size();k++){
        		rp1[i]=rp.get(k).getData();
        	}
        	Matrix rp2=new Matrix(rp1);
        	Matrix C=cov(rp2);
        	Matrix matrix=Inverse.getInverse(C);
        	Matrix matrix_x=new Matrix(x);
        	Matrix matrix_y=new Matrix(y);
        	double L=matrix_x.minus(matrix_y).times(matrix).times(matrix_x.minus(matrix_y).inverse()).getArray()[0][0];
        	if(d==0){
        		Score[i]=0;
        	}
        	else{
        		Score[i]=Math.sqrt(L)/d;
        	}
        	Score detection_score=new Score();
        	detection_score.setTime(data_list.get(i).getTime());
        	detection_score.setScore(Score[i]);
        	sd.insert(scoreName, detection_score);   //将异常得分写入异常得分表中
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
					 for(int k=0;k<dd.length;k++){
						 new_dd[k][1]=dd[k]-avg;
					 }
					 double[] dd_j=rp2.getMatrix(0, row-1,j,j).getColumnPackedCopy();  //取第j列
					 double avg_j=getAverage(dd_j);
					 double[][] new_dd_j=new double[dd_j.length][1];
					 for(int k=0;k<dd_j.length;k++){
						 new_dd_j[k][1]=dd_j[k]-avg_j;
					 }
					 Matrix temp=new Matrix(new_dd).arrayTimes(new Matrix(new_dd_j));
					 double[][] temp1=temp.getArray();
					 int total_sum=0;
					 for(int k=0;k<temp1.length;k++){
						 total_sum+=sum(temp1[k]);
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

}
