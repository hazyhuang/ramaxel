package com.ramaxel.px;

import java.util.Iterator;

import com.agile.api.APIException;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileSession;
import com.agile.api.ICell;
import com.agile.api.IChange;
import com.agile.api.IDataObject;
import com.agile.api.IItem;
import com.agile.api.INode;
import com.agile.api.IRow;
import com.agile.api.ITable;
import com.agile.api.ItemConstants;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.agile.util.AgileLoginHelper;
import com.hazy.common.HazyUtil;

public class SetPartPriority implements ICustomAction{

	@Override
	public ActionResult doAction(IAgileSession arg0, INode arg1, IDataObject arg2) {
		IChange chg=(IChange)arg2;
		try {
			ITable table =chg.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
			Iterator iter=table.getTableIterator();
			while(iter.hasNext()){
				IRow row=(IRow)iter.next();
				String newLp=HazyUtil.getAgileAPIHelper().getSingleListValue(row, ChangeConstants.ATT_AFFECTED_ITEMS_LIFECYCLE_PHASE);
			System.out.println(newLp);
				if("即将作废".equals(newLp)){
		        	IItem item=(IItem)row.getReferent();
		        	if(item.getAgileClass().isSubclassOf(ItemConstants.CLASS_PARTS_CLASS)){
						ITable tableRedline=chg.getTable(ChangeConstants.TABLE_REDLINEITEM);
						Iterator iter2=tableRedline.getTableIterator();
						while(iter2.hasNext()){
							IRow redPage2Row = (IRow)iter2.next();
							System.out.println(redPage2Row);
							if(redPage2Row.getName().equals(item.getName())){
							ICell cell = redPage2Row.getCell(ChangeConstants.ATT_REDLINE_ITEM_P2_LIST05);
							System.out.println("old value, before update: " + cell.getOldValue());
							redPage2Row.getCell(ChangeConstants.ATT_REDLINE_ITEM_P2_LIST05).setValue("3");
							}
						}
		        		//chg.setValue(ChangeConstants.ATT_REDLINE_ITEM_P2_LIST05, "3");
					}
		        }
				System.out.println("Priority:"+newLp);
			}
		} catch (APIException e) {
			
			e.printStackTrace();
			return new ActionResult(ActionResult.EXCEPTION, e );
		}
		return new ActionResult(ActionResult.STRING, "Set Priority Success" );
	}
	
	public static void main(String[] args) {

		try {
			IAgileSession session = AgileLoginHelper.getSession();
			IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "PQ0000013");//
			SetPartPriority rel = new SetPartPriority();
			System.out.println("Change:" + change);
			System.out.println(rel.doAction(session, null, change));
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
