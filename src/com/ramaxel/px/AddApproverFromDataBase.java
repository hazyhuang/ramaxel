package com.ramaxel.px;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IChange;
import com.agile.api.IDataObject;
import com.agile.api.INode;
import com.agile.api.ISignoffReviewer;
import com.agile.api.IUser;
import com.agile.api.WorkflowConstants;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.common.HazyException;
import com.hazy.common.HazyUtil;

public class AddApproverFromDataBase implements ICustomAction {
	
	static Logger logger = Logger.getLogger(AddApproverFromDataBase.class);
	
	private ArrayList<IUser> getPureNewUser(IChange change,HashMap<String,String> usersMap) throws APIException{
		ISignoffReviewer[] exitUsers = change.getAllReviewers(change.getStatus(), WorkflowConstants.USER_APPROVER);
		for (int i = 0; i < exitUsers.length; i++) {
			String userid = exitUsers[i].getReviewer().getName();
			logger.debug("exist users in this status:" + userid);
		}
		ArrayList<IUser> list = new ArrayList<IUser>();
		
		for (int i = 0; i < exitUsers.length; i++) {
			String userid = exitUsers[i].getReviewer().getName();
			usersMap.remove(userid);
		}
		Set<String> set = usersMap.keySet();
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			String userid = iter.next();
			System.out.println(userid);

			IUser user = HazyUtil.getAgileAPIHelper().loadUser(change.getSession(), userid);
			list.add(user);
		}
		return list;
	}

	private HashMap<String, String> loadUserFromDB(String ECNNum) throws HazyException{
		  Properties props = System.getProperties();
	        String osName= props.getProperty("os.name");
		String fullPath=null;
		
		if("Windows Server 2008 R2".equals(osName)){
		fullPath=ClassLoader.getSystemResource("").getPath()+"conf.properties";
		}else{
			fullPath=ClassLoader.getSystemResource("").getPath()
					+"../integration/sdk/extensions/conf.properties";
		}
		Properties prop=HazyUtil.getInstance().loadProperties(fullPath);
		String db_path=prop.getProperty("db_path");
		String db_user=prop.getProperty("db_user");
		String db_pwd=prop.getProperty("db_pwd");
		
		ECNUserDAO ecnUserDAO=new ECNUserDAO(db_path,db_user,db_pwd);
		return ecnUserDAO.loadUserFromDataBase(ECNNum);
		
	}
	
	@Override
	public ActionResult doAction(IAgileSession session, INode arg1, IDataObject arg2) {
		
		
		try {
			IChange change = (IChange) arg2;
			String ECNNum = change.getName();
			HashMap<String, String> usersMap = this.loadUserFromDB(ECNNum);
			// Map(userid,userid)
			change.addReviewers(change.getStatus(), this.getPureNewUser(change, usersMap), null, null, true, "Auto Assign Approvers from DataBase");

		} catch (APIException e) {
			e.printStackTrace();
			return new ActionResult(ActionResult.EXCEPTION, e);

		} catch (HazyException e) {
			return new ActionResult(ActionResult.EXCEPTION, e);
		}
		return new ActionResult(ActionResult.STRING, "success.");
	}

	public static void main(String[] args) {
	    Properties props = System.getProperties();
        String osName= props.getProperty("os.name");
		try {
			String fullPath=null;
		
			if("Windows Server 2008 R2".equals(osName)){
			fullPath=ClassLoader.getSystemResource("").getPath()+"conf.properties";
			}else{
				fullPath=ClassLoader.getSystemResource("").getPath()
						+"../integration/sdk/extensions/conf.properties";
			}
			Properties prop=HazyUtil.getInstance().loadProperties(fullPath);
			String agileurl=prop.getProperty("agileurl");
			String agileuser=prop.getProperty("agileuser");
			String agilepwd=prop.getProperty("agilepwd");
		IAgileSession session = HazyUtil.getAgileAPIHelper().login(agileurl, agileuser,agilepwd);
			IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "ECN_00000154");//
			AddApproverFromDataBase rel = new AddApproverFromDataBase();
			System.out.println("Change:" + change);
			System.out.println(rel.doAction(session, null, change));
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HazyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
