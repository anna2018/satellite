package com.predict;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dao.SatelliteDao;
import com.dao.SatellitePreprocessingDao;
import com.entity.Satellite;
import com.entity.Satellite_Preprocessing;

public class DataPreprocessing {
	public static double[][] matrix_X;
	public static double[][] matrix_Y;
	public static double[] Test;
	public static double min;
	public static double max;
	// public static void DataCompression() {
	// MarsDao md = new MarsDao();
	// MarsPreprocessingDao mpd = new MarsPreprocessingDao();
	// String sql = "select min(star_time) as star_time,max(star_time) as
	// receiving_time from mars";
	// List<Mars> list = md.find(sql);
	// String sql_preprocessing = "select max(star_time) as star_time from
	// mars_preprocessing";
	// List<Mars_Preprocessing> list2 = mpd.find(sql_preprocessing);
	// Date min_time;
	// if (list2.get(0).getStar_time().equals(null) {
	// min_time = list.get(0).getStar_time();
	// } else {
	// min_time = new Date(list2.get(0).getStar_time().getTime() + 60000);
	// }
	// Date max_time = list.get(0).getReceiving_time();
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// Date time = min_time;
	// while (time.before(max_time)) {
	// String sql1 = "select to_date('"
	// + sdf.format(time)
	// + "','yyyy-mm-dd hh24:mi:ss') as star_time,avg(vkz019) as
	// vkz019,avg(vkz021) as vkz021,avg(vkz022) as vkz022,avg(vkz026) as vkz026"
	// + " from mars " + " where star_time>=to_date('"
	// + sdf.format(time)
	// + "','yyyy-mm-dd hh24:mi:ss') and star_time<to_date('"
	// + sdf.format(new Date(time.getTime() + 60000))
	// + "','yyyy-mm-dd hh24:mi:ss')";
	// System.out.println(sql1);
	// List<Mars> list1 = md.find(sql1);
	// if (list1.get(0).getVkz019() != 0) {
	// Mars m = list1.get(0);
	// int v = mpd.insert(m);
	// }
	// list1.clear();
	// time = new Date(time.getTime() + 60000);
	// }
	// }

	public static void DataConversion(String preprocessingName, String attribute, int step, Date time,int satelliteId) {
		SatellitePreprocessingDao mpd = new SatellitePreprocessingDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select A" + attribute + " from  " + preprocessingName + " where time<=to_date('"
				+ sdf.format(time) + "','yyyy-mm-dd hh24:mi:ss')" + " and time>=to_date('"
				+ sdf.format(new Date(time.getTime() - 60 * 60000)) + "','yyyy-mm-dd hh24:mi:ss') and satelliteId="+satelliteId
				+ " order by time asc";
		System.out.println(sql);
		List<Satellite_Preprocessing> list = mpd.find(sql);
		String min_max = "select max(A" + attribute + ") as A1025,min(A" + attribute + ") as A1026 from (select * from "
				+ preprocessingName + " where time<=to_date('" + sdf.format(time) + "','yyyy-mm-dd hh24:mi:ss')"
				+ " and time>=to_date('" + sdf.format(new Date(time.getTime() - 60 * 60000))
				+ "','yyyy-mm-dd hh24:mi:ss')  and satelliteId="+satelliteId + " order by time asc)";
		List<Satellite_Preprocessing> list_min_max = mpd.find(min_max);
		max = list_min_max.get(0).getA1025();
		min = list_min_max.get(0).getA1026();
		double[] error = new double[10];
		for (int m = 1; m <= 10; m++) {// 构造转换矩阵
			int k = 0, l = 0, rownum = 0;
			System.out.println(list.size());
			matrix_X = new double[list.size() - step - m + 1][m];
			matrix_Y = new double[list.size() - step - m + 1][1];
			for (int i = 0; i < list.size() - step - m + 1; i++) {
				for (int j = 0; j < m; j++) {
					if (attribute.equals("1025")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz019()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1025();
					} else if (attribute.equals("1026")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1026();
					} else if (attribute.equals("1027")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1027();
					} else if (attribute.equals("1028")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1028();
					} else if (attribute.equals("1029")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1029();
					} else if (attribute.equals("1030")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1030();
					} else if (attribute.equals("1031")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1031();
					} else if (attribute.equals("1032") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1032();
					} else if (attribute.equals("1033")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1033();
					} else if (attribute.equals("1034")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1034();
					} else if (attribute.equals("1035")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1035();
					} else if (attribute.equals("1036") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1036();
					} else if (attribute.equals("1048")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1048();
					} else if (attribute.equals("1049")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1049();
					} else if (attribute.equals("1050")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1050();
					} else if (attribute.equals("1051")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1051();
					} else if (attribute.equals("1052") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1052();
					} else if (attribute.equals("1053") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1053();
					} else if (attribute.equals("1054")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1054();
					} else if (attribute.equals("1055") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1055();
					} else if (attribute.equals("1056")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1056();
					} else if (attribute.equals("1057") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1057();
					} else if (attribute.equals("1058") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1058();
					} else if (attribute.equals("1059")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1059();
					} else if (attribute.equals("1060") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1060();
					} else if (attribute.equals("1061") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1061();
					} else if (attribute.equals("1062") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1062();
					} else if (attribute.equals("1063") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1063();
					} else if (attribute.equals("1075")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1075();
					} else if (attribute.equals("1076") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1076();
					} else if (attribute.equals("1077") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1077();
					} else if (attribute.equals("1078")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz021()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA1078();
					} else if (attribute.equals("4201") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz022()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA4201();
					} else if (attribute.equals("4202") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA4202();
					} else if (attribute.equals("4205") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA4205();
					} else if (attribute.equals("2125")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2125();
					} else if (attribute.equals("2126")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2126();
					} else if (attribute.equals("2127") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2127();
					} else if (attribute.equals("2128")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2128();
					} else if (attribute.equals("2131") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2131();
					} else if (attribute.equals("2132") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2132();
					} else if (attribute.equals("2133")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2133();
					} else if (attribute.equals("2136") ){
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2136();
					} else if (attribute.equals("2137")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2137();
					} else if (attribute.equals("6220")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA6220();
					} else if (attribute.equals("13333")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA13333();
					} else if (attribute.equals("2140")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2140();
					} else if (attribute.equals("2141")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2141();
					} else if (attribute.equals("2142")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2142();
					} else if (attribute.equals("2143")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA2143();
					} else if (attribute.equals("14346")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14346();
					} else if (attribute.equals("14347")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14347();
					} else if (attribute.equals("14348")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14348();
					} else if (attribute.equals("14349")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14349();
					} else if (attribute.equals("14350")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14350();
					} else if (attribute.equals("14351")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14351();
					} else if (attribute.equals("14373")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14373();
					} else if (attribute.equals("14374")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14374();
					} else if (attribute.equals("14375")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14375();
					} else if (attribute.equals("14376")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14376();
					} else if (attribute.equals("14377")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14377();
					} else if (attribute.equals("14403")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14403();
					} else if (attribute.equals("14404")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14404();
					} else if (attribute.equals("14405")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14405();
					} else if (attribute.equals("14406")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14406();
					} else if (attribute.equals("14368")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14368();
					} else if (attribute.equals("14371")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14371();
					} else if (attribute.equals("14417")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14417();
					} else if (attribute.equals("14418")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14418();
					} else if (attribute.equals("14419")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14419();
					} else if (attribute.equals("13395")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA13395();
					} else if (attribute.equals("13396")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA13396();
					} else if (attribute.equals("13397")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA13397();
					} else if (attribute.equals("14340")) {
						// matrix_X[i][j] =
						// ((list.get(k).getVkz026()-min)/(max-min));
						matrix_X[i][j] = list.get(k).getA14340();
					}
					k++;
				}
//				for(int ii=0;ii<matrix_X.length;ii++){
//					for(int jj=0;jj<matrix_X[0].length;jj++){
//						System.out.print(matrix_X[ii][jj]+" ");
//					}
//					System.out.println();
//				}
				if (attribute.equals("1025")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1025();
				} else if (attribute.equals("1026")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1026();
				} else if (attribute.equals("1027")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1027();
				} else if (attribute.equals("1028")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1028();
				}
				else if (attribute.equals("1029")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1029();
				} else if (attribute.equals("1030")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1030();
				} else if (attribute.equals("1031")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1031();
				} else if (attribute.equals("1032")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1032();
				}
				else if (attribute.equals("1033")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1033();
				} else if (attribute.equals("1034")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1034();
				} else if (attribute.equals("1035")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1035();
				} else if (attribute.equals("1036")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1036();
				}
				else if (attribute.equals("1048")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1048();
				} else if (attribute.equals("1049")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1049();
				} else if (attribute.equals("1050")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1050();
				} else if (attribute.equals("1051")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1051();
				}
				else if (attribute.equals("1052")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1052();
				} else if (attribute.equals("1053")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1053();
				} else if (attribute.equals("1054")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1054();
				} else if (attribute.equals("1055")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1055();
				}
				else if (attribute.equals("1056")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1056();
				} else if (attribute.equals("1057")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1057();
				} else if (attribute.equals("1058")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1058();
				} else if (attribute.equals("1059")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1059();
				}
				else if (attribute.equals("1060")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1060();
				} else if (attribute.equals("1061")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1061();
				} else if (attribute.equals("1062")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1062();
				} else if (attribute.equals("1063")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1063();
				}
				else if (attribute.equals("1075")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1075();
				} else if (attribute.equals("1076")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1076();
				} else if (attribute.equals("1077")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1077();
				} else if (attribute.equals("1078")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA1078();
				}
				else if (attribute.equals("4201")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA4201();
				} else if (attribute.equals("4202")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA4202();
				} else if (attribute.equals("4203")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA4203();
				} else if (attribute.equals("4204")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA4204();
				}
				else if (attribute.equals("4205")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA4205();
				} else if (attribute.equals("2125")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2125();
				} else if (attribute.equals("2126")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2126();
				} else if (attribute.equals("2127")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2127();
				}
				else if (attribute.equals("2128")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2128();
				} else if (attribute.equals("2131")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2131();
				} else if (attribute.equals("2132")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2132();
				} else if (attribute.equals("2133")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2133();
				}
				else if (attribute.equals("2136")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2136();
				} else if (attribute.equals("2137")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2137();
				} else if (attribute.equals("6220")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA6220();
				} else if (attribute.equals("13333")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA13333();
				}
				else if (attribute.equals("2140")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2140();
				} else if (attribute.equals("2141")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2141();
				} else if (attribute.equals("2142")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2142();
				} else if (attribute.equals("2143")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA2143();
				}
				else if (attribute.equals("14346")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14346();
				} else if (attribute.equals("14347")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14347();
				} else if (attribute.equals("14348")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14348();
				} else if (attribute.equals("14349")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14349();
				}
				else if (attribute.equals("14350")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14350();
				} else if (attribute.equals("14351")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14351();
				} else if (attribute.equals("14373")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14373();
				} else if (attribute.equals("14374")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14374();
				}
				else if (attribute.equals("14375")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14375();
				} else if (attribute.equals("14376")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14376();
				} else if (attribute.equals("14377")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14377();
				} else if (attribute.equals("14403")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14404();
				}
				else if (attribute.equals("14405")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14405();
				} else if (attribute.equals("14406")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14406();
				} else if (attribute.equals("14368")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14368();
				} else if (attribute.equals("14371")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14371();
				}
				else if (attribute.equals("14417")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14417();
				} else if (attribute.equals("14418")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14418();
				} else if (attribute.equals("14419")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14419();
				} else if (attribute.equals("13395")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz026()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA13395();
				}
				else if (attribute.equals("13396")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz019()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA13396();
				} else if (attribute.equals("13397")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz021()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA13397();
				} else if (attribute.equals("14340")) {
					// matrix_Y[i][0] =
					// ((list.get(k+21).getVkz022()-min)/(max-min));
					matrix_Y[i][0] = list.get(k + step - 1).getA14340();
				} 
				l++;
				k = l;
				rownum++;
			} // for i
			error[m - 1] = KELM.getError(matrix_X, matrix_Y);
		} // for m
		int sign = 0;
		double min_error = error[0];
		for (int i = 1; i < error.length; i++) {// 获得误差最小的m
			if (error[i] < min_error) {
				min_error = error[i];
				sign = i;
			}
		}
		int m = sign + 1;
		int k = 0, l = 0, rownum = 0;
		matrix_X = new double[list.size() - step - m + 1][m];
		matrix_Y = new double[list.size() - step - m + 1][1];
		for (int i = 0; i < list.size() - step - m + 1; i++) {// 重新构造转换矩阵
			for (int j = 0; j < m; j++) {
				if (attribute.equals("1025")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1025();
				} else if (attribute.equals("1026")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1026();
				} else if (attribute.equals("1027")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1027();
				} else if (attribute.equals("1028")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1028();
				}
				else if (attribute.equals("1029")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1029();
				} else if (attribute.equals("1030")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1030();
				} else if (attribute.equals("1031")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1031();
				} else if (attribute.equals("1032")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1032();
				}
				else if (attribute.equals("1033")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1033();
				} else if (attribute.equals("1034")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1034();
				} else if (attribute.equals("1035")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1035();
				} else if (attribute.equals("1036")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1036();
				}
				else if (attribute.equals("1048")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1048();
				} else if (attribute.equals("1049")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1049();
				} else if (attribute.equals("1050")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1050();
				} else if (attribute.equals("1051")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1051();
				}
				else if (attribute.equals("1052")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1052();
				} else if (attribute.equals("1053")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1053();
				} else if (attribute.equals("1054")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1054();
				} else if (attribute.equals("1055")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1055();
				}
				else if (attribute.equals("1056")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1056();
				} else if (attribute.equals("1057")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1057();
				} else if (attribute.equals("1058")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1058();
				} else if (attribute.equals("1059")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1059();
				}
				else if (attribute.equals("1060")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1060();
				} else if (attribute.equals("1061")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1061();
				} else if (attribute.equals("1062")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1062();
				} else if (attribute.equals("1063")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1063();
				}
				else if (attribute.equals("1075")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1075();
				} else if (attribute.equals("1076")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1076();
				} else if (attribute.equals("1077")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1077();
				} else if (attribute.equals("1078")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA1078();
				}
				else if (attribute.equals("4201")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA4201();
				} else if (attribute.equals("4202")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA4202();
				} else if (attribute.equals("4203")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA4203();
				} else if (attribute.equals("4204")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA4204();
				}
				else if (attribute.equals("4205")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA4205();
				} else if (attribute.equals("2125")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2125();
				} else if (attribute.equals("2126")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2126();
				} else if (attribute.equals("2127")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2127();
				}
				else if (attribute.equals("2128")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2128();
				} else if (attribute.equals("2131")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2131();
				} else if (attribute.equals("2132")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2132();
				} else if (attribute.equals("2133")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2133();
				}
				else if (attribute.equals("2136")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2136();
				} else if (attribute.equals("2137")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2137();
				} else if (attribute.equals("6220")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA6220();
				} else if (attribute.equals("13333")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA13333();
				}
				else if (attribute.equals("2140")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2140();
				} else if (attribute.equals("2141")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2141();
				} else if (attribute.equals("2142")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2142();
				} else if (attribute.equals("2143")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA2143();
				}
				else if (attribute.equals("14346")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14346();
				} else if (attribute.equals("14347")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14347();
				} else if (attribute.equals("14348")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14348();
				} else if (attribute.equals("14349")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14349();
				}
				else if (attribute.equals("14350")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14350();
				} else if (attribute.equals("14351")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14351();
				} else if (attribute.equals("14373")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14373();
				} else if (attribute.equals("14374")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14374();
				}
				else if (attribute.equals("14375")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14375();
				} else if (attribute.equals("14376")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14376();
				} else if (attribute.equals("14377")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14377();
				} else if (attribute.equals("14403")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14403();
				}
				else if (attribute.equals("14404")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14404();
				} else if (attribute.equals("14405")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14405();
				} else if (attribute.equals("14406")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14406();
				} else if (attribute.equals("14368")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14368();
				}
				else if (attribute.equals("14371")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14371();
				} else if (attribute.equals("14351")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14351();
				} else if (attribute.equals("14417")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14417();
				} else if (attribute.equals("14418")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14418();
				}
				else if (attribute.equals("14419")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz019()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14419();
				} else if (attribute.equals("13395")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz021()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA13395();
				} else if (attribute.equals("13396")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz022()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA13396();
				} else if (attribute.equals("13397")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA13397();
				}else if (attribute.equals("14340")) {
					// matrix_X[i][j] =
					// ((list.get(k).getVkz026()-min)/(max-min));
					matrix_X[i][j] = list.get(k).getA14340();
				}
//				for(int ii=0;ii<matrix_X.length;ii++){
//					for(int jj=0;jj<matrix_X[0].length;jj++){
//						System.out.print(matrix_X[ii][jj]+" ");
//					}
//					System.out.println();
//				}
				k++;
			}
			if (attribute.equals("1025")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1025();
			} else if (attribute.equals("1026")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1026();
			} else if (attribute.equals("1027")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1027();
			} else if (attribute.equals("1028")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1028();
			}
			else if (attribute.equals("1029")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1029();
			} else if (attribute.equals("1030")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1030();
			} else if (attribute.equals("1031")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1031();
			} else if (attribute.equals("1032")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1032();
			}
			else if (attribute.equals("1033")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1033();
			} else if (attribute.equals("1034")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1034();
			} else if (attribute.equals("1035")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1035();
			} else if (attribute.equals("1036")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1036();
			}
			else if (attribute.equals("1048")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1048();
			} else if (attribute.equals("1049")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1049();
			} else if (attribute.equals("1050")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1050();
			} else if (attribute.equals("1051")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1051();
			}
			else if (attribute.equals("1052")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1052();
			} else if (attribute.equals("1053")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1053();
			} else if (attribute.equals("1054")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1054();
			} else if (attribute.equals("1055")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1055();
			}
			else if (attribute.equals("1056")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1056();
			} else if (attribute.equals("1057")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1057();
			} else if (attribute.equals("1058")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1058();
			} else if (attribute.equals("1059")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1059();
			}
			else if (attribute.equals("1060")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1060();
			} else if (attribute.equals("1061")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1061();
			} else if (attribute.equals("1062")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1062();
			} else if (attribute.equals("1063")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1063();
			}
			else if (attribute.equals("1075")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1075();
			} else if (attribute.equals("1076")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1076();
			} else if (attribute.equals("1077")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1077();
			} else if (attribute.equals("1078")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA1078();
			}
			else if (attribute.equals("4201")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA4201();
			} else if (attribute.equals("4202")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA4202();
			} else if (attribute.equals("4203")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA4203();
			} else if (attribute.equals("4204")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA4204();
			}
			else if (attribute.equals("4205")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA4205();
			} else if (attribute.equals("2125")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2125();
			} else if (attribute.equals("2126")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2126();
			} else if (attribute.equals("2127")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2127();
			}
			else if (attribute.equals("2128")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2128();
			} else if (attribute.equals("2131")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2131();
			} else if (attribute.equals("2132")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2132();
			} else if (attribute.equals("2133")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2133();
			}
			else if (attribute.equals("2136")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2136();
			} else if (attribute.equals("2137")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2137();
			} else if (attribute.equals("6220")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA6220();
			} else if (attribute.equals("13333")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA13333();
			}
			else if (attribute.equals("2140")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2140();
			} else if (attribute.equals("2141")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2141();
			} else if (attribute.equals("2142")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2142();
			} else if (attribute.equals("2143")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA2143();
			}
			else if (attribute.equals("14346")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14346();
			} else if (attribute.equals("14347")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14347();
			} else if (attribute.equals("14348")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14348();
			} else if (attribute.equals("14349")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14349();
			}
			else if (attribute.equals("14350")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14350();
			} else if (attribute.equals("14351")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14351();
			} else if (attribute.equals("14373")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14373();
			} else if (attribute.equals("14374")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14374();
			}
			else if (attribute.equals("14375")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14375();
			} else if (attribute.equals("14376")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14376();
			} else if (attribute.equals("14377")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14377();
			} else if (attribute.equals("14403")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14404();
			}
			else if (attribute.equals("14405")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14405();
			} else if (attribute.equals("14406")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14406();
			} else if (attribute.equals("14368")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14368();
			} else if (attribute.equals("14371")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14371();
			}
			else if (attribute.equals("14417")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14417();
			} else if (attribute.equals("14418")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14418();
			} else if (attribute.equals("14419")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14419();
			} else if (attribute.equals("13395")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz026()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA13395();
			}
			else if (attribute.equals("13396")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz019()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA13396();
			} else if (attribute.equals("13397")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz021()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA13397();
			} else if (attribute.equals("14340")) {
				// matrix_Y[i][0] =
				// ((list.get(k+21).getVkz022()-min)/(max-min));
				matrix_Y[i][0] = list.get(k + step - 1).getA14340();
			} 
			l++;
			k = l;
			rownum++;
		} // for i
		Test = new double[m];
		for (int i = 0; i < m; i++) {
			Test[i] = matrix_Y[matrix_Y.length - m + i][0];
		}
//		for(int ii=0;ii<matrix_X.length;ii++){
//			for(int jj=0;jj<matrix_X[0].length;jj++){
//				System.out.print(matrix_X[ii][jj]+" ");
//			}
//			System.out.println();
//		}
//		for(int ii=0;ii<matrix_Y.length;ii++){
//			for(int jj=0;jj<matrix_Y[0].length;jj++){
//				System.out.print(matrix_Y[ii][jj]+" ");
//			}
//			System.out.println();
//		}
	}
}
