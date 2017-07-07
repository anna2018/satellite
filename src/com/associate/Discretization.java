package com.associate;

import java.util.Date;
import java.util.List;

import com.dao.SatelliteDao;
import com.dao.SatellitePreprocessingDao;
import com.dao.ScoreDao;
import com.entity.Satellite;
import com.entity.Satellite_Preprocessing;
import com.entity.Score;
import com.util.CreateTable;
import com.util.TableExist;
import com.util.Tmid;

public class Discretization {
	private String tableName;
	private String discretizationTableName;
	private int fenduan;
	public Discretization(String name1,int n,String detection,String preprocessingName,int satelliteId){
		tableName=name1;
		fenduan=n;
		String exist_sql = "select count(*) from user_tables where table_name='" + tableName.toUpperCase()  //判断表格是否存在
		+ "'";
		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
			String create_sql = "create table " + tableName
					+ " (time DATE, a1025  NUMBER,a1026  NUMBER,a1027  NUMBER,a1028  NUMBER,a1029  NUMBER,a1030  NUMBER,a1031  NUMBER,a1032  NUMBER,a1033  NUMBER,a1034  NUMBER,a1035  NUMBER,a1036  NUMBER,a1048  NUMBER,a1049  NUMBER,a1050  NUMBER,a1051  NUMBER,a1052  NUMBER,a1053  NUMBER,a1054  NUMBER,a1055  NUMBER,a1056  NUMBER,a1057  NUMBER,a1058  NUMBER,a1059  NUMBER,a1060  NUMBER,a1061  NUMBER,a1062  NUMBER,a1063  NUMBER,a1075  NUMBER,a1076  NUMBER,a1077  NUMBER,a1078  NUMBER,a4201  NUMBER,a4202  NUMBER,a4203  NUMBER,a4204  NUMBER,a4205  NUMBER,a2125  NUMBER,a2126  NUMBER,a2127  NUMBER,a2128  NUMBER,a2131  NUMBER,a2132  NUMBER,a2133  NUMBER,a2136  NUMBER,a2137  NUMBER,a6220  NUMBER,a13333 NUMBER,a2140  NUMBER,a2141  NUMBER,a2142  NUMBER,a2143  NUMBER,a14346 NUMBER,a14347 NUMBER,a14348 NUMBER,a14349 NUMBER,a14350 NUMBER,a14351 NUMBER,a14373 NUMBER,a14374 NUMBER,a14375 NUMBER,a14376 NUMBER,a14377 NUMBER,a14403 NUMBER,a14404 NUMBER,a14405 NUMBER,a14406 NUMBER,a14368 NUMBER,a14371 NUMBER,a14417 NUMBER,a14418 NUMBER,a14419 NUMBER,a13395 NUMBER,a13396 NUMBER,a13397 NUMBER,a14340 NUMBER ,satelliteId NUMBER)";
			CreateTable.create(create_sql);
		}
		else{
			String delete_sql="delete from "+ tableName;
			CreateTable.create(delete_sql);
		}
//		exist_sql = "select count(*) from user_tables where table_name='" + tableName.toUpperCase()  //判断表格是否存在
//		+ "_NES'";
//		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
//			String create_sql = "create table " + tableName
//					+ "_NES (time DATE, a1025  NUMBER,a1026  NUMBER,a1027  NUMBER,a1028  NUMBER,a1029  NUMBER,a1030  NUMBER,a1031  NUMBER,a1032  NUMBER,a1033  NUMBER,a1034  NUMBER,a1035  NUMBER,a1036  NUMBER,a1048  NUMBER,a1049  NUMBER,a1050  NUMBER,a1051  NUMBER,a1052  NUMBER,a1053  NUMBER,a1054  NUMBER,a1055  NUMBER,a1056  NUMBER,a1057  NUMBER,a1058  NUMBER,a1059  NUMBER,a1060  NUMBER,a1061  NUMBER,a1062  NUMBER,a1063  NUMBER,a1075  NUMBER,a1076  NUMBER,a1077  NUMBER,a1078  NUMBER,a4201  NUMBER,a4202  NUMBER,a4203  NUMBER,a4204  NUMBER,a4205  NUMBER,a2125  NUMBER,a2126  NUMBER,a2127  NUMBER,a2128  NUMBER,a2131  NUMBER,a2132  NUMBER,a2133  NUMBER,a2136  NUMBER,a2137  NUMBER,a6220  NUMBER,a13333 NUMBER,a2140  NUMBER,a2141  NUMBER,a2142  NUMBER,a2143  NUMBER,a14346 NUMBER,a14347 NUMBER,a14348 NUMBER,a14349 NUMBER,a14350 NUMBER,a14351 NUMBER,a14373 NUMBER,a14374 NUMBER,a14375 NUMBER,a14376 NUMBER,a14377 NUMBER,a14403 NUMBER,a14404 NUMBER,a14405 NUMBER,a14406 NUMBER,a14368 NUMBER,a14371 NUMBER,a14417 NUMBER,a14418 NUMBER,a14419 NUMBER,a13395 NUMBER,a13396 NUMBER,a13397 NUMBER,a14340 NUMBER )";
//			CreateTable.create(create_sql);
//		}
//		else{
//			String delete_sql="delete from "+ tableName+"_NES";
//			CreateTable.create(delete_sql);
//		}
		ScoreDao sd=new ScoreDao();
		String sql="select avg(score)+stddev(score)*3 as score from "+detection+" where satelliteId="+satelliteId; //统计异常阈值
		List<Score> list=sd.find(sql);
		double threshold=list.get(0).getScore();
		String insert_sql="insert into "+tableName+" select "+preprocessingName+".* from "+preprocessingName+","+detection+" where "+preprocessingName+".satelliteId="+detection+ ".satelliteId and "+preprocessingName+".satelliteId="+satelliteId+" and to_char("+detection+".time,'yyyy-MM-dd hh24:mi')=to_char("+preprocessingName+".time,'yyyy-MM-dd hh24:mi') and "+detection+".score>"+threshold ;
		//System.out.println(insert_sql);
		CreateTable.create(insert_sql);
//		insert_sql="insert into "+tableName+"_NES select "+preprocessingName+".* from "+preprocessingName+","+detection+" where  to_char("+detection+".time,'yyyy-MM-dd hh24:mi')=to_char("+preprocessingName+".time,'yyyy-MM-dd hh24:mi') and "+detection+".score<="+threshold;
//		CreateTable.create(insert_sql);
		divide(tableName,n,preprocessingName,satelliteId);
	}
	public Discretization(String name1,int n,String preprocessingName,String date1,String date2,int satelliteId){
		tableName=name1;
		fenduan=n;
		String exist_sql = "select count(*) from user_tables where table_name='" + tableName.toUpperCase()  //判断表格是否存在
		+ "'";
		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
			String create_sql = "create table " + tableName
					+ " (time DATE, a1025  NUMBER,a1026  NUMBER,a1027  NUMBER,a1028  NUMBER,a1029  NUMBER,a1030  NUMBER,a1031  NUMBER,a1032  NUMBER,a1033  NUMBER,a1034  NUMBER,a1035  NUMBER,a1036  NUMBER,a1048  NUMBER,a1049  NUMBER,a1050  NUMBER,a1051  NUMBER,a1052  NUMBER,a1053  NUMBER,a1054  NUMBER,a1055  NUMBER,a1056  NUMBER,a1057  NUMBER,a1058  NUMBER,a1059  NUMBER,a1060  NUMBER,a1061  NUMBER,a1062  NUMBER,a1063  NUMBER,a1075  NUMBER,a1076  NUMBER,a1077  NUMBER,a1078  NUMBER,a4201  NUMBER,a4202  NUMBER,a4203  NUMBER,a4204  NUMBER,a4205  NUMBER,a2125  NUMBER,a2126  NUMBER,a2127  NUMBER,a2128  NUMBER,a2131  NUMBER,a2132  NUMBER,a2133  NUMBER,a2136  NUMBER,a2137  NUMBER,a6220  NUMBER,a13333 NUMBER,a2140  NUMBER,a2141  NUMBER,a2142  NUMBER,a2143  NUMBER,a14346 NUMBER,a14347 NUMBER,a14348 NUMBER,a14349 NUMBER,a14350 NUMBER,a14351 NUMBER,a14373 NUMBER,a14374 NUMBER,a14375 NUMBER,a14376 NUMBER,a14377 NUMBER,a14403 NUMBER,a14404 NUMBER,a14405 NUMBER,a14406 NUMBER,a14368 NUMBER,a14371 NUMBER,a14417 NUMBER,a14418 NUMBER,a14419 NUMBER,a13395 NUMBER,a13396 NUMBER,a13397 NUMBER,a14340 NUMBER,satelliteId NUMBER )";
			//System.out.println(create_sql);
			CreateTable.create(create_sql);
		}
		else{
			String delete_sql="delete from "+ tableName;
			CreateTable.create(delete_sql);
		}
//		exist_sql = "select count(*) from user_tables where table_name='" + tableName.toUpperCase()  //判断表格是否存在
//		+ "_NES'";
//		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
//			String create_sql = "create table " + tableName
//					+ "_NES (time DATE, a1025  NUMBER,a1026  NUMBER,a1027  NUMBER,a1028  NUMBER,a1029  NUMBER,a1030  NUMBER,a1031  NUMBER,a1032  NUMBER,a1033  NUMBER,a1034  NUMBER,a1035  NUMBER,a1036  NUMBER,a1048  NUMBER,a1049  NUMBER,a1050  NUMBER,a1051  NUMBER,a1052  NUMBER,a1053  NUMBER,a1054  NUMBER,a1055  NUMBER,a1056  NUMBER,a1057  NUMBER,a1058  NUMBER,a1059  NUMBER,a1060  NUMBER,a1061  NUMBER,a1062  NUMBER,a1063  NUMBER,a1075  NUMBER,a1076  NUMBER,a1077  NUMBER,a1078  NUMBER,a4201  NUMBER,a4202  NUMBER,a4203  NUMBER,a4204  NUMBER,a4205  NUMBER,a2125  NUMBER,a2126  NUMBER,a2127  NUMBER,a2128  NUMBER,a2131  NUMBER,a2132  NUMBER,a2133  NUMBER,a2136  NUMBER,a2137  NUMBER,a6220  NUMBER,a13333 NUMBER,a2140  NUMBER,a2141  NUMBER,a2142  NUMBER,a2143  NUMBER,a14346 NUMBER,a14347 NUMBER,a14348 NUMBER,a14349 NUMBER,a14350 NUMBER,a14351 NUMBER,a14373 NUMBER,a14374 NUMBER,a14375 NUMBER,a14376 NUMBER,a14377 NUMBER,a14403 NUMBER,a14404 NUMBER,a14405 NUMBER,a14406 NUMBER,a14368 NUMBER,a14371 NUMBER,a14417 NUMBER,a14418 NUMBER,a14419 NUMBER,a13395 NUMBER,a13396 NUMBER,a13397 NUMBER,a14340 NUMBER )";
//			CreateTable.create(create_sql);
//		}
//		else{
//			String delete_sql="delete from "+ tableName+"_NES";
//			CreateTable.create(delete_sql);
//		}
		String insert_sql="insert into "+tableName+" select * from "+preprocessingName+" where satelliteId="+satelliteId+" and time>=(to_date('"+date1+"','yyyy-MM-dd hh24:mi:ss')) and time<=(to_date('"+date2+"','yyyy-MM-dd hh24:mi:ss')) order by time" ;
		//System.out.println(insert_sql);
		CreateTable.create(insert_sql);
//		insert_sql="insert into "+tableName+"_NES select "+preprocessingName+".* from "+preprocessingName+","+detection+" where  to_char("+detection+".time,'yyyy-MM-dd hh24:mi')=to_char("+preprocessingName+".time,'yyyy-MM-dd hh24:mi') and "+detection+".score<="+threshold;
//		CreateTable.create(insert_sql);
		divide(tableName,n,preprocessingName,satelliteId);
	}
	public void divide(String tableName,int n,String preprocessingName,int satelliteId){
		List<String> list=Tmid.getTmid();
//		String exist_sql = "select count(*) from user_tables where table_name='DISCRETIZATEATTRIBUTE'";
//		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
//			String create_sql = "create table discretizateAttribute (attribute varchar(20))";
//			CreateTable.create(create_sql);
//		}
//		else{
//			String delete_sql="delete from discretizateAttribute";
//			CreateTable.create(delete_sql);
//		}
//		exist_sql = "select count(*) from user_tables where table_name='DISCRETIZATE'";
//		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
//			String create_sql = "create table discretizate (attribute varchar(20))";
//			CreateTable.create(create_sql);
//		}
//		else{
//			String delete_sql="delete from "+ tableName;
//			CreateTable.create(delete_sql);
//		}
//		exist_sql = "select count(*) from user_tables where table_name='DISCRETIZATE_NES'";
//		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
//			String create_sql = "create table discretizate_nes (attribute varchar(20))";
//			CreateTable.create(create_sql);
//		}
//		else{
//			String delete_sql="delete from "+ tableName;
//			CreateTable.create(delete_sql);
//		}
		for(String attribute:list){
			String max_minsql="select max(A"+attribute+") as A1025,min(A"+attribute+") as A1026 from "+preprocessingName+" where satelliteId="+satelliteId;
			SatellitePreprocessingDao spd = new SatellitePreprocessingDao();
			List<Satellite_Preprocessing> l = spd.find(max_minsql);
			double max = l.get(0).getA1025();
			double min=l.get(0).getA1026();
			double[] array=new double[n+1];
			double step=(max-min)/n;
			array[0]=min;
			array[n]=max;
			for(int i=1;i<n;i++){
				array[i]=min+step*i;
			}
//			for(int i=1;i<=n;i++){
//				String insert="insert into discretizateAttribute values('A"+attribute+" "+i+"')";
//				CreateTable.create(insert);
//			}
			for(int i=0;i<n;i++){
				String update="";
				if(i==0){
					update="update "+tableName+" set A"+attribute+"="+(i+1)+" where satelliteId="+satelliteId+" and A"+attribute+">="+array[i]+" and A"+attribute+"<="+array[i+1];
				}
				else{
					update="update "+tableName+" set A"+attribute+"="+(i+1)+" where satelliteId="+satelliteId+" and A"+attribute+">="+array[i]+" and A"+attribute+"<="+array[i+1];
				}
				//System.out.println(update);
				CreateTable.create(update);
//				if(i==0){
//					update="update "+tableName+"_NES set A"+attribute+"="+(i+1)+" where A"+attribute+">="+array[i]+" and A"+attribute+"<="+array[i+1];
//				}
//				else{
//					update="update "+tableName+"_NES set A"+attribute+"="+(i+1)+" where A"+attribute+">="+array[i]+" and A"+attribute+"<="+array[i+1];
//				}
//				CreateTable.create(update);
			}
		}
	}
//	public static void main(String args[]){
//		String detection="detectionscore";
//		String preprocessingName="dataPreprocessing188";
//		Discretization d=new Discretization("a",3,detection,preprocessingName,188);
//	}
}
