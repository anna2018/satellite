package com.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.dao.DBConnection;


public class SqlTableModel extends DefaultTableModel {
	private Connection con = null;
    private ResultSet rs = null;
    private ResultSetMetaData rsmd = null;

	public SqlTableModel() {
		
	}

	public SqlTableModel(String sqlStr, String[] name) {
		//System.out.println("执行的集合查询为 :" + sqlStr);
        try{
        	con = DBConnection.getConnection();
            rs = con.prepareStatement(sqlStr).executeQuery();
            rsmd = rs.getMetaData();
            for(int i=0; i<rsmd.getColumnCount(); i++) {
            	addColumn(name[i]);
            }
            while(rs.next()){
                Vector vdata = new Vector();
                for ( int i = 1 ; i <= rsmd.getColumnCount() ; i ++){
                    vdata.addElement(rs.getObject(i));
                }
                addRow(vdata);
            }
            rs.close();
            con.close();
        }catch(java.sql.SQLException sql){
        	JOptionPane.showMessageDialog(null, "连接数据库失败!!!", "系统提示",
	                JOptionPane.ERROR_MESSAGE);
            sql.printStackTrace();
        }finally {
        	try {
				con.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "输入的信息不正确,请重新输入!!!", "系统提示",
		                JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
        }
	}

}
