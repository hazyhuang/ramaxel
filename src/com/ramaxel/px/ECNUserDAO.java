package com.ramaxel.px;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.hazy.common.HazyUtil;





/**
 * @author huanghua
 * 
 */
public class ECNUserDAO {
	
	private String dburl;
	private String dbuser;
	private String dbpwd;
	
	
	
	
	public ECNUserDAO(String dburl,String dbuser,String dbpwd){
		this.dburl=dburl;
		this.dbuser=dbuser;
		this.dbpwd=dbpwd;
	}




	public  HashMap<String,String> loadUserFromDataBase(String ECNNum){
		String sqlString = "select approver from ECN_TRACK_ITEM_SHEET where ECN_NO='"+ECNNum+"'";
		String sqlString2 = "select approver from ECN_TRACK_OPTION  where ECN_NO='"+ECNNum+"'";
		 HashMap<String,String>  user=loadUserFromDB(sqlString);
		 HashMap<String,String>  user2=loadUserFromDB(sqlString2);
		 Set<String> set=user2.keySet();
		 Iterator<String> iter=set.iterator();
		 while(iter.hasNext()){
			String userid= iter.next();
			user.put(userid, userid);
		 }
		 return user;
	}
	private  HashMap<String,String> loadUserFromDB(String sqlStr){

		HashMap<String,String> list = new HashMap<String,String>();
	
		
		Connection conn = null;
		Statement sm = null;
		ResultSet result = null;
		
		try {
			conn = HazyUtil.getDBConnectionHelper().getDBConnection(dburl, dbuser, dbpwd);
			sm = conn.createStatement();
			result = sm.executeQuery(sqlStr);
			
		
			while (result.next()) {
				
				String id=result.getString("approver");
			    if(id!=null&!"".equals(id)){
				list.put(id, id);
			    }
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			HazyUtil.getDBConnectionHelper().close(conn, sm, result);
		}	
		return list;}
	
	/*public static void main(String[] args) {
		HashMap<String,String> users=loadUserFromDataBase("ECN_00000140");
		 Set<String> set=users.keySet();
		 Iterator<String> iter=set.iterator();
		 while(iter.hasNext()){
			String userid= iter.next();
			System.out.println(userid);
		 }
	}*/
}
