package com.dao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.entity.Prediction;


public class PredictionDao extends BaseDao{
	/**
	 * 添加数据
	 */
	public int insert(String tableName,Prediction p){
		//String sql="insert into Mars_Preprocessing(star_time,vkz019,vkz021,vkz022,vkz026) values(to_date('?','yyyy-mm-dd hh24:mi:ss'),?,?,?,?)";
		List params=new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		params.add(sdf.format(m.getStar_time()));
//		params.add(m.getVkz019());
//		params.add(m.getVkz021());
//		params.add(m.getVkz022());
//		params.add(m.getVkz026());
		String sql="insert into "+tableName+"(attribute,time,predictedvalue,upper,lower,actualvalue,satelliteId) values('"+p.getAttribute()+"',to_date('"+sdf.format(p.getTime())+"','yyyy-mm-dd hh24:mi:ss'),"+p.getPredictedvalue()+","+p.getUpper()+","+p.getLower()+","+p.getActualvalue()+","+p.getSatelliteId()+")";
		//System.out.println(sql);
		int i=super.executeUpdate(sql, params);
		//super.closeAll();
		return i;
	}
	/**
	 * 查询数据
	 */
	public List find(String sql) {
		List<Prediction> list = super.executeQuery(sql, null,Prediction.class);
		//super.closeAll();
		return list;
	}
	public String getError(String sql) {
		List<Prediction> list = super.executeQuery(sql, null,Prediction.class);
		double error=0;
		for(int i=0;i<list.size();i++){
			Prediction p=new Prediction();
			p=list.get(i);
			if(p.getActualvalue()>p.getUpper()){
				error+=(p.getActualvalue()-p.getUpper())/(p.getUpper()-p.getLower());
			}
			else if(p.getActualvalue()<p.getLower()){
				error+=(p.getLower()-p.getActualvalue())/(p.getUpper()-p.getLower());
			}
			else{
				error+=(p.getPredictedvalue()-p.getActualvalue())/(p.getActualvalue());
			}
		}
		error=(error/list.size())*1000;
        return String.format("%.2f",error);
	}
}
