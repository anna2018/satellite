package com.dao;


import java.util.ArrayList;
import java.util.List;

import com.entity.Rule;


public class RuleDao extends BaseDao{
	public List find(String sql) {
		List<Rule> list = super.executeQuery(sql, null,Rule.class);
		//super.closeAll();
		return list;
	}
	/**
	 * Ìí¼ÓÊý¾Ý
	 */
	public int insert(String tableName, Rule m) {
		List params = new ArrayList();
		String sql = "insert into " + tableName + " values('"+m.getRule()+"'," + m.getSupport()+","+m.getConfidence()+","+m.getSatelliteId()+" )";
		//System.out.println(sql);
		int i = super.executeUpdate(sql, params);
		//super.closeAll();
		return i;
	}
	public int insertByDetection(String tableName, Rule m) {
		List params = new ArrayList();
		String sql = "insert into " + tableName + " values('"+m.getRule()+"' )";
		//System.out.println(sql);
		int i = super.executeUpdate(sql, params);
		//super.closeAll();
		return i;
	}
}
