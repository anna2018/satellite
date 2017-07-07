package com.predict;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dao.SatelliteDao;
import com.dao.SatellitePreprocessingDao;
import com.dao.PredictionDao;
import com.entity.Satellite;
import com.entity.Satellite_Preprocessing;
import com.entity.Prediction;
import com.util.CreateTable;
import com.util.TableExist;
import com.util.Tmid;

import Jama.Matrix;

public class Predict {
	
	/**
	 * @param args
	 */
	public static void Prediction(String preprocessingName,String predictName,int satelliteId) {
		//DataPreprocessing.DataCompression();
		//String preprocessingName="DataPreprocessing188";
		//String predictName="prediction188_1";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date();
		int step = 20;
		while(true){
			Date d = new Date();
			//if(d.getSeconds()==0&&(d.getTime()-d1.getTime())==60000*step){
			if(d.getSeconds()==0){
				List<String> attribute = Tmid.getTmid(); // 获取属性列表
				Date now=new Date();
				try {
						Date time = new Date(now.getTime()-step*60000);
						for(int i=0;i<attribute.size();i++){
							predict(preprocessingName,attribute.get(i),step,time,predictName,satelliteId);
						}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				d1=new Date(d1.getTime()+step*60000);
			}
		}
	}
	public static void predict(String preprocessingName,String attribute,int step,Date time,String predictName,int satelliteId) {
		//DataPreprocessing.DataCompression();
		//String attribute = "vkz019";
		//int step = 22;
		DataPreprocessing.DataConversion(preprocessingName,attribute, step,time,satelliteId);
		Matrix X=new Matrix(DataPreprocessing.matrix_X);
		Matrix Y=new Matrix(DataPreprocessing.matrix_Y);
		Matrix Test=new Matrix(DataPreprocessing.Test,DataPreprocessing.Test.length);
		double[] paras=DE.DE_Kernel();
		//double[] paras={10,100};
		KELM.ELM_Kernel(paras[0],paras[1], X, Y, Test);
		SatellitePreprocessingDao mpd = new SatellitePreprocessingDao();
		//String min_max="select max("+attribute+") as vkz019,min("+attribute+") as vkz021 from (select * from mars_preprocessing order by star_time desc) where rownum<=120";
		//List<Mars_Preprocessing> list_min_max = mpd.find(min_max);
		double max = DataPreprocessing.max;
		double min = DataPreprocessing.min;
		double[] alf_bta=DE.DE_PISI();
		double predictedValue=Test.get(0, 0);
		double upper =predictedValue*(1+alf_bta[0]);
		double lower =predictedValue*(1-alf_bta[1]);
		//System.out.println("predictedValue="+predictedValue);
		//System.out.println("upper="+upper);
		//System.out.println("lower="+lower);
		Prediction p =new Prediction();
		p.setAttribute(attribute);
		p.setTime(new Date(time.getTime()+step*60000));
		p.setPredictedvalue(predictedValue);
		p.setUpper(upper);
		p.setLower(lower);
		p.setSatelliteId(satelliteId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select avg(A" + attribute + ") as A1025 from  " + preprocessingName + " where time>=to_date('"
				+ sdf.format(time) + "','yyyy-mm-dd hh24:mi:ss')" + " and time<to_date('"
				+ sdf.format(new Date(time.getTime()+step*60000)) + "','yyyy-mm-dd hh24:mi:ss') and satelliteId="+satelliteId;
		System.out.println(sql);
		List<Satellite_Preprocessing> list = mpd.find(sql);
		p.setActualvalue(list.get(0).getA1025());
		String exist_sql = "select count(*) from user_tables where table_name='" + predictName.toUpperCase()  //判断表格是否存在
		+ "'";
		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
			String create_sql = "create table " + predictName
			+ " (attribute varchar(20),time DATE, predictedvalue  NUMBER,upper  NUMBER,lower  NUMBER,actualvalue NUMBER,satelliteId NUMBER)";
			CreateTable.create(create_sql);
		}
		PredictionDao pd = new PredictionDao();
		int v = pd.insert(predictName,p);
		//System.out.println("添加"+v+"条记录");
	}
}
