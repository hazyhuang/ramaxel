package com.ramaxel.px;

import java.util.Iterator;

import com.agile.api.APIException;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileSession;
import com.agile.api.IChange;
import com.agile.api.IDataObject;
import com.agile.api.INode;
import com.agile.api.IRow;
import com.agile.api.ITable;
import com.agile.api.IWorkflow;
import com.agile.api.ItemConstants;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.common.HazyUtil;

/*
 * 
 * 1.创建物料状态变更流程A,并与原物料认证流程B关联（关系）
 * 2.将原物料认证流程B中的物料添加至新流程A受影响物件表中，并设置生命周期为【小批量】
 * 3.将原物料认证流程B中的物料从受影响物件表中移除
 * 4.将物料状态变更流程A自动提交至【发布通知】节点
 * 5.将物料状态变更流程A中所有物料添加至物料认证流程B的受影响物件中
 * 6.提交物料认证流程B至下一节点

 */
public class AutoChangeLifeCycle implements ICustomAction {
	public static String STATUS_CHANGE_TYPE = "MCO";// 物料状态变更流程 APIName
	public static String LIFE_CYCLE_SHORT_RUN = "RM_XiaoPL";// short run
															// production's
															// APIName

	@SuppressWarnings("unchecked")
	@Override
	public ActionResult doAction(IAgileSession session, INode arg1, IDataObject dataB) {
		String mcoNumber = "";
		try {
			IChange changeA = (IChange) HazyUtil.getAgileAPIHelper().createObject(session, STATUS_CHANGE_TYPE);
			mcoNumber = changeA.getName();
			IChange changeB = (IChange) dataB;
			ITable chgBrelation = changeB.getTable(ChangeConstants.TABLE_RELATIONSHIPS);
			chgBrelation.add(changeA);
			ITable tableB = changeB.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
			ITable tableA = changeA.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
			@SuppressWarnings("rawtypes")
			Iterator iter = tableB.iterator();
			session.disableAllWarnings();
			while (iter.hasNext()) {

				IRow item = (IRow) iter.next();
				if(item.getReferent().getAgileClass().isSubclassOf(ItemConstants.CLASS_PARTS_CLASS)){
					IRow row = tableA.createRow(item.getReferent());
					row.setValue(ChangeConstants.ATT_AFFECTED_ITEMS_LIFECYCLE_PHASE, LIFE_CYCLE_SHORT_RUN);
		
				}
			}
			IWorkflow[] workflow = changeA.getWorkflows();
			changeA.setWorkflow(workflow[0]);
			changeA.changeStatus(changeA.getDefaultNextStatus(), false, "", false, false, null, null, null, null,
					false);

			
		} catch (APIException e) {
			e.printStackTrace();
			return new ActionResult(ActionResult.EXCEPTION, e);
		}finally{
			try {
				session.enableAllWarnings();
			} catch (APIException e) {
				
				e.printStackTrace();
			}
		}
		return new ActionResult(ActionResult.STRING, "LifeCycle Change Success:" + mcoNumber);
	}

}
