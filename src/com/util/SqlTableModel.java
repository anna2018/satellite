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
		//System.out.println("ִ�еļ��ϲ�ѯΪ :" + sqlStr);
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
        	JOptionPane.showMessageDialog(null, "�������ݿ�ʧ��!!!", "ϵͳ��ʾ",
	                JOptionPane.ERROR_MESSAGE);
            sql.printStackTrace();
        }finally {
        	try {
				con.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "�������Ϣ����ȷ,����������!!!", "ϵͳ��ʾ",
		                JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
        }
	}

}
