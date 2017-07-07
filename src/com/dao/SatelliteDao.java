package com.dao;

import java.util.List;

import com.entity.Satellite;


public class SatelliteDao extends BaseDao{
	public List find(String sql) {
		List<Satellite> list = super.executeQuery(sql, null,Satellite.class);
		//super.closeAll();
		return list;
	}
}
