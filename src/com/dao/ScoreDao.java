package com.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.entity.Score;

public class ScoreDao extends BaseDao {
	/**
	 * 添加数据
	 */
	public int insert(String tableName, Score m) {
		List params = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "insert into " + tableName + " values(to_date('" + sdf.format(m.getTime())
				+ "','yyyy-mm-dd hh24:mi:ss')," + m.getScore()+","+m.getSatelliteId()+" )";
		//System.out.println(sql);
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
		return i;
	}
	/**
	 * 查询数据
	 */
	public List find(String sql) {
		List<Score> list = super.executeQuery(sql, null, Score.class);
		return list;
	}
}
