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

import com.util.ReadPro;
//hibernate
/**
 * 
 * ���ݿ�Ĳ�������
 */
public class BaseDao2 {

	protected  Connection conn;
	protected  PreparedStatement stmt;
	protected  ResultSet rst;
	private static final String DRIVER_NAME;
	private static final String USER;
	private static final String PASSWORD;
	private static final String URL;
	
	
	static{
		//�������ļ��л�ȡҪ����Դ
		//�����ļ��ĸ�ʽ: name=value
		USER=ReadPro.getNewInstance().getProperty("USER");
		DRIVER_NAME=ReadPro.getNewInstance().getProperty("DRIVER_NAME");
		PASSWORD=ReadPro.getNewInstance().getProperty("PASSWORD");
		URL=ReadPro.getNewInstance().getProperty("URL");
		try {
			//��������
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡ����
	 * @return
	 * @throws SQLException
	 */
	public final void getConnection() throws SQLException{
		//this.closeAll();
		//conn=DriverManager.getConnection(URL, USER, PASSWORD);
		if(conn==null){
			conn=DriverManager.getConnection(URL, USER, PASSWORD);
		}
		else{
			String sql="select count(*) as c1 from v$session";
			stmt=this.conn.prepareStatement(sql);
			setPreparedStatement(null);
			rst=stmt.executeQuery();
			rst.next();
			int result=rst.getInt(1);
			if(result>300){
			rst.close();
			stmt.close();
			conn.close();
			}
			conn=DriverManager.getConnection(URL, USER, PASSWORD);
		}
		
	}

	/**
	 *��ȡ���ӵķ��� 
	 */
//	protected final void getConnection() throws SQLException {
//		conn = DBConnection.getConnection();
//	}
	/**
	 * �ͷ���Դ�ķ���
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
	 * ����ִ��insert��delete��udpate���ķ���
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
			this.closeAll();
			e.printStackTrace();
		} finally{
			this.closeAll();
		}
		this.closeAll();
		return count;
	}
	
	/**
	 * ִ�в�ѯ�ķ���
	 * @param sql
	 * @param params
	 * @param cla ���ڷ�װ���ݵ�ʵ����
	 * @return
	 * @throws SQLException
	 */
	protected final List executeQuery(String sql,List params,Class cla){
		//�������ݿ��в�ѯ������
		List list=new ArrayList();
		try {
			//��ȡʵ����������
			Field[] field=cla.getDeclaredFields();
			String[] names=new String[field.length];
			for(int i=0;i<names.length;i++){
				//��ȡ��������
				names[i]=field[i].getName();
			}
			this.getConnection();
			stmt=this.conn.prepareStatement(sql);
			setPreparedStatement(params);
			rst=stmt.executeQuery();
			while(rst.next()){
				//�������ʹ�������
				Object obj=cla.newInstance();
				for(int i=0;i<names.length;i++){
					String v=null;
					try {
						//����������������ͬ
						//�����������ƻ�ȡ��Ӧ�е�ֵ
						//���û�в�ѯ�����Զ�Ӧ���У������쳣�������ѭ��
						v = rst.getString(names[i]);
					} catch (Exception e) {
						continue;
					}
					if(v != null){
						//���������������
						field[i].setAccessible(true);
						//��ȡ�����Ե���������
						Class type=field[i].getType();
//						�������ͽ�����Ӧ�ĸ�ֵ
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
//				�Ѷ�����ӵ�������
				list.add(obj);
			}
			this.closeAll();
		} catch (Exception e) {
			this.closeAll();
			e.printStackTrace();
		} finally{
			this.closeAll();
		}
		this.closeAll();
		return list;
	}
	/**
	 * ��Ԥ�����sql��丳ֵ
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
