package org.liuhe.database;

import java.sql.*;

public class JDBC_Connection {

	//mysql数据库连接类
	public static Connection getConnection(String ip,String port,String database,String username,String password){
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
		}
	    try{
	    	String url = "jdbc:mysql://"+ip+":"+port+"/"+database+"?useUnicode=true&characterEncoding=utf-8";
	    	Connection conn = (Connection)DriverManager.getConnection(url,username,password);
	    	return conn;
	    }catch(SQLException e){
	    	System.out.println("connet mysql failure");
	    	e.printStackTrace();
	    	return null;
	    }
	    
	}
	//释放数据库连接资源
	public static void releaseResource(ResultSet rs,PreparedStatement ps,Connection conn){
		try{
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
			if(conn!=null){
				conn.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}