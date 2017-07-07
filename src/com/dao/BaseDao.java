package com.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.util.ReadPro;
//hibernate
/**
 * 
 * 数据库的操作基类
 */
public class BaseDao {

	private static Connection conn;
	private static  PreparedStatement stmt;
	private static  ResultSet rst;
	
	/**
	 * 获取连接
	 * @return
	 * @throws SQLException
	 */
	public final synchronized void getConnection() throws SQLException{
		if(conn==null)
			conn=DBConnection.getConnection();
	}

	/**
	 *获取连接的方法 
	 */
//	protected final void getConnection() throws SQLException {
//		conn = DBConnection.getConnection();
//	}
	/**
	 * 释放资源的方法
	 */
	protected  final void closeAll(){
		if(rst != null){
			try {
				rst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				rst=null;
			}
		}
		if(stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				stmt=null;
			}
		}
		if(stmt != null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				conn=null;
			}
		}
	}
	/**
	 * 用于执行insert、delete、udpate语句的方法
	 * @param sql
	 * @param params
	 * @return
	 */
	protected final int executeUpdate(String sql, List params) {
		int count = 0;
		try {
			this.getConnection();
			stmt=this.conn.prepareStatement(sql);
			setPreparedStatement(params);
			count=stmt.executeUpdate();
			this.closeAll();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "连接数据库失败!!!", "系统提示",
	                JOptionPane.ERROR_MESSAGE);
			this.closeAll();
			e.printStackTrace();
		} finally{
			this.closeAll();
		}
		//this.closeAll();
		return count;
	}
	
	/**
	 * 执行查询的方法
	 * @param sql
	 * @param params
	 * @param cla 用于封装数据的实体类
	 * @return
	 * @throws SQLException
	 */
	protected final List executeQuery(String sql,List params,Class cla){
		//保存数据库中查询的数据
		List list=new ArrayList();
		try {
			//获取实体类中属性
			Field[] field=cla.getDeclaredFields();
			String[] names=new String[field.length];
			for(int i=0;i<names.length;i++){
				//获取属性名称
				names[i]=field[i].getName();
			}
			this.getConnection();
			stmt=this.conn.prepareStatement(sql);
			setPreparedStatement(params);
			rst=stmt.executeQuery();
			while(rst.next()){
				//根据类型创建对象
				Object obj=cla.newInstance();
				for(int i=0;i<names.length;i++){
					String v=null;
					try {
						//属性名称与列名相同
						//根据属性名称获取对应列的值
						//如果没有查询该属性对应的列，则发生异常提过当次循环
						v = rst.getString(names[i]);
					} catch (Exception e) {
						continue;
					}
					if(v != null){
						//设置属性允许访问
						field[i].setAccessible(true);
						//获取该属性的数据类型
						Class type=field[i].getType();
//						根据类型进行相应的赋值
						if(type==int.class || type==Integer.class){
							field[i].set(obj, Integer.valueOf(v));
						}else if(type==double.class||type==Double.class){
							field[i].set(obj, Double.valueOf(v));
						}else if(type==long.class||type==Long.class){
							field[i].set(obj, Long.valueOf(v));
						}else if(type==Date.class){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							field[i].set(obj, sdf.parse(v));
						}else{
							field[i].set(obj, v);
						}
					}
				}
//				把对象添加到集合中
				list.add(obj);
			}
			this.closeAll();
		} catch (Exception e) {
			this.closeAll();
			JOptionPane.showMessageDialog(null, "连接数据库失败!!!", "系统提示",
	                JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} finally{
			this.closeAll();
		}
		//this.closeAll();
		return list;
	}
	/**
	 * 给预编译的sql语句赋值
	 * @param params
	 * @throws SQLException
	 */
	private void setPreparedStatement(List params) throws SQLException {
		if(params !=null && params.size()>0){
			for(int i=0;i<params.size();i++){
				stmt.setObject(i+1, params.get(i));
			}
		}
	}
}
