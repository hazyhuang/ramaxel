package com.ramaxel.event;


import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;

import org.w3c.dom.events.EventException;

import com.agile.api.APIException;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileSession;
import com.agile.api.IChange;
import com.agile.api.IItem;
import com.agile.api.INode;
import com.agile.api.ITable;
import com.agile.api.ItemConstants;
import com.agile.px.ActionResult;
import com.agile.px.EventActionResult;
import com.agile.px.EventConstants;
import com.agile.px.IEventAction;
import com.agile.px.IEventInfo;
import com.agile.px.IWFChangeStatusEventInfo;
import com.agile.px.ISignOffEventInfo;
import com.hazy.common.HazyException;
import com.hazy.common.HazyUtil;

public class ItemDescValid implements IEventAction {

	public EventActionResult doAction(IAgileSession session, INode actionNode, IEventInfo request) {

		try {

			int eventType = request.getEventType();
			String ret = "";
			if (eventType == EventConstants.EVENT_APPROVE_FOR_WORKFLOW) {
				//ISignOffEventInfo info = (ISignOffEventInfo) request;
				//IChange chg = (IChange) info.getDataObject();

				/*if ("Review2".equals(chg.getStatus().getAPIName())) {
					ret = this.itemDescValidation(session, chg);
				}*/
			} else if (eventType == EventConstants.EVENT_CHANGE_STATUS_FOR_WORKFLOW) {
				IWFChangeStatusEventInfo info = (IWFChangeStatusEventInfo) request;
				IChange chg = (IChange) info.getDataObject();
				ret = this.itemDescValidation(session, chg);
			}
			if (ret.length() > 10) {
				throw new EventException((short) 0, ret);
			}
			ActionResult actionResult = new ActionResult(ActionResult.STRING, "非卡控节点。");
			return new EventActionResult(request, actionResult);

		} catch (APIException | HazyException | SQLException  e) {
			e.printStackTrace();
			return new EventActionResult(request, new ActionResult(ActionResult.EXCEPTION, e));
		}
	}

	public String itemDescValidation(IAgileSession session, IChange chg) throws APIException, HazyException, SQLException {
	
		StringBuffer validString = new StringBuffer();
		ITable table = chg.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
		@SuppressWarnings("rawtypes")
		Iterator iter = table.getReferentIterator();
	
		while (iter.hasNext()) {
			IItem item = (IItem) iter.next();

			validString.append(validationDesc(item));
		
		}
		return validString.toString();
	}
/**
 *  如果有相同对象的描述，则返回 "对象:XXX 存在相同描述的其它对象"
 *  如果没有相同对象的描述，则返回""
 * @param item
 * @return
 * @throws APIException
 * @throws SQLException 
 * @throws HazyException 
 */
	public String validationDesc(IItem item) throws APIException, HazyException, SQLException {
		String desc=(String)item.getValue(ItemConstants.ATT_TITLE_BLOCK_DESCRIPTION);
		if(desc!=null){
		Object subclassID=item.getAgileClass().getId();
		boolean hasSameDesc=hasSameDesc(desc,subclassID);
		if (hasSameDesc) {
			return "物件:" + item.getName() + "存在相同描述的其它对象";
		} else {
			return "";
		}
		}else{
			return "";
		}

	}
	private boolean hasSameDesc(String desc,Object subclassID) throws HazyException, SQLException {
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
		
		ItemDAO ecnUserDAO=new ItemDAO(db_path,db_user,db_pwd);
		return ecnUserDAO.itemHaveSameDescFromDB(desc,subclassID);
		
	}

}
