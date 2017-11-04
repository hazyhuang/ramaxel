package com.ramaxel.px;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.hazy.common.HazyUtil;

public class FileDAO {
	
	private String dburl;
	private String dbuser;
	private String dbpwd;
	
	
	
	
	public FileDAO(String dburl,String dbuser,String dbpwd){
		this.dburl=dburl;
		this.dbuser=dbuser;
		this.dbpwd=dbpwd;
	}





	public  String loadFilePath(Object file_id) throws SQLException{
		String sqlStr="select * from file_info where file_id="+file_id;
		Connection conn = null;
		Statement sm = null;
		ResultSet result = null;
		String file_path="";
		try {
			conn = HazyUtil.getDBConnectionHelper().getDBConnection(dburl, dbuser, dbpwd);
			sm = conn.createStatement();
			result = sm.executeQuery(sqlStr);
			
		
			while (result.next()) {
				
				file_path=result.getString("ifs_filepath");
			   
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			HazyUtil.getDBConnectionHelper().close(conn, sm, result);
		}	
		return file_path;
		}
}
