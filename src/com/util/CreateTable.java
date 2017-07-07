package com.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.dao.DBConnection;

public class CreateTable {


    

	//´´½¨±í
    public static boolean create(String sql)
    {
        try {
        	Connection conn=DBConnection.getConnection();
    		Statement stmt=conn.createStatement();
			if (stmt.execute(sql))
			{
				stmt.close();
				conn.close();
			    return false;
			}
			else
			{
				stmt.close();
				conn.close();
			    return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return false;
    }
}
