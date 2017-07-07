package com.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.entity.Satellite;
import com.entity.Satellite_Preprocessing;

public class SatellitePreprocessingDao extends BaseDao {
	/**
	 * 添加数据
	 */
	public int insert(String tableName, Satellite_Preprocessing m) {
		List params = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "insert into " + tableName + " values(to_date('" + sdf.format(m.getTime())
				+ "','yyyy-mm-dd hh24:mi:ss')," + m.getA1025() + "," + m.getA1026() + "," + m.getA1027() + ","
				+ m.getA1028() +","+ m.getA1029() + "," + m.getA1030() + "," + m.getA1031() + ","
						+ m.getA1032()+","+ m.getA1033()+","+ m.getA1034()+","+ m.getA1035()+","
				+ m.getA1036()+","+ m.getA1048()+","+ m.getA1049()+","+ m.getA1050()+","
						+ m.getA1051()+","+ m.getA1052()+","+ m.getA1053()+","+ m.getA1054()+","
				+ m.getA1055()+","+ m.getA1056()+","+ m.getA1057()+","+ m.getA1058()+","
						+ m.getA1059()+","+ m.getA1060()+","+ m.getA1061()+","+ m.getA1062()+","
				+ m.getA1063()+","+ m.getA1075()+","+ m.getA1076()+","+ m.getA1077()+","
						+ m.getA1078()+","+ m.getA4201()+","+ m.getA4202()+","+ m.getA4203()+","
				+ m.getA4204()+","+ m.getA4205()+","+ m.getA2125()+","+ m.getA2126()+","
						+ m.getA2127()+","+ m.getA2128()+","+ m.getA2131()+","+ m.getA2132()+","
				+ m.getA2133()+","+ m.getA2136()+","+ m.getA2137()+","+ m.getA6220()+","
						+ m.getA13333()+","+ m.getA2140()+","+ m.getA2141()+","+ m.getA2142()+","
				+ m.getA2143()+","+ m.getA14346()+","+ m.getA14347()+","+ m.getA14348()+","
						+ m.getA14349()+","+ m.getA14350()+","+ m.getA14351()+","+ m.getA14373()+","
				+ m.getA14374()+","+ m.getA14375()+","+ m.getA14376()+","+ m.getA14377()+","
						+ m.getA14403()+","+ m.getA14404()+","+ m.getA14405()+","+ m.getA14406()+","
				+ m.getA14368()+","+ m.getA14371()+","+ m.getA14417()+","+ m.getA14418()+","
						+ m.getA14419()+","+ m.getA13395()+","+ m.getA13396()+","+ m.getA13397()+","+ m.getA14340()+","+m.getSatelliteId()+")";
		//System.out.println(sql);
		int i = super.executeUpdate(sql, params);
		//super.closeAll();
		return i;
	}
	public int insert(String sql) {
		List params = new ArrayList();
		int i = super.executeUpdate(sql, params);
		//super.closeAll();
		return i;
	}

	/**
	 * 修改数据
	 */
	public int update(String sql){
		List params=new ArrayList();
		int i=super.executeUpdate(sql, params);
		//super.closeAll();
		return i;
	}
	/**
	 * 查询数据
	 */
	public List find(String sql) {
		List<Satellite_Preprocessing> list = super.executeQuery(sql, null, Satellite_Preprocessing.class);
		super.closeAll();
		return list;
	}
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		String str="select * from DataPreprocessing5564";
//		SatellitePreprocessingDao dao=new SatellitePreprocessingDao();
//		List list=dao.find(str);
//		System.out.println(list.size());
//		Satellite_Preprocessing s=(Satellite_Preprocessing) list.get(1);
//		System.out.println(s.getTime());
//	}
}
