package com.ramaxel.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hazy.common.HazyUtil;

public class ItemDAO {

	static Logger logger = Logger.getLogger(ItemDAO.class);
		
		private String dburl;
		private String dbuser;
		private String dbpwd;
		
		
		
		
		public ItemDAO(String dburl,String dbuser,String dbpwd){
			this.dburl=dburl;
			this.dbuser=dbuser;
			this.dbpwd=dbpwd;
		}



		public static String replaceStrAll(String str) {
			if (str.indexOf('\'') > -1) {
				str = str.replaceAll("'", "''");
			}
		
			
			return str;
		}


		public  boolean itemHaveSameDescFromDB(String desc,Object id) throws SQLException{

			
		  desc=replaceStrAll(desc);
			String sqlStr="select count(item_number) from Item where subclass="+id+" and description='"+desc.trim()+"'";
			Connection conn = null;
			PreparedStatement sm = null;
			ResultSet result = null;
			
			try {
				conn = HazyUtil.getDBConnectionHelper().getDBConnection(dburl, dbuser, dbpwd);
				sm = conn.prepareStatement(sqlStr);
				logger.debug(sm.toString());
				logger.debug(sqlStr);
				//sm.setString(1, desc);
				result = sm.executeQuery();
				
			    //if(result.getFetchSize())
				while (result.next()) {
					int count=result.getInt(1);
					if(count>1){
						return true;
					}else{
						return false;
					}
				
				}
              
			} catch (SQLException ex) {
				ex.printStackTrace();
				throw ex;
			} finally {
				HazyUtil.getDBConnectionHelper().close(conn, sm, result);
			}
			  return false;
		}
		

	}