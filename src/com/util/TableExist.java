package com.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dao.DBConnection;

public class TableExist {


    public static boolean exist(String sql)
    {
        int result=0;
        try {
        	Connection conn=DBConnection.getConnection();
    		Statement stmt=conn.createStatement();
    		ResultSet rs=stmt.executeQuery(sql);
    		rs.next();
    		result=rs.getInt(1);
    		rs.close();
    		stmt.close();
    		conn.close();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			if (result>0)
			{
				//System.out.println("1");
			    return true;
			}
			else
			{
				//System.out.println("0");
			    return false;
			}
    }
    
}
