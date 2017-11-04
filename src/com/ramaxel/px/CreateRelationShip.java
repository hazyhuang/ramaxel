package com.ramaxel.px;

import java.util.ArrayList;
import java.util.Iterator;

import com.agile.api.APIException;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileSession;
import com.agile.api.IChange;
import com.agile.api.IDataObject;
import com.agile.api.IItem;
import com.agile.api.INode;
import com.agile.api.IRow;
import com.agile.api.ITable;
import com.agile.api.ItemConstants;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.common.HazyException;
import com.hazy.common.HazyUtil;

public class CreateRelationShip implements ICustomAction {

	@Override
	public ActionResult doAction(IAgileSession arg0, INode arg1, IDataObject arg2) {
		IChange chg = (IChange) arg2;
		try {
			ITable table = chg.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
			IItem part=null;
			ArrayList<IItem> docs=new ArrayList<IItem>();
			@SuppressWarnings("rawtypes")
			Iterator iter = table.getTableIterator();
			while (iter.hasNext()) {
				IRow row = (IRow) iter.next();
				IItem item = (IItem) row.getReferent();
				if(item.getAgileClass().isSubclassOf(ItemConstants.CLASS_DOCUMENTS_CLASS)){
					docs.add(item);
				}else if(item.getAgileClass().isSubclassOf(ItemConstants.CLASS_PARTS_CLASS)){
					part=item;
				}
				
				
			}
			createRelation(part,docs);
		} catch (APIException e) {

			e.printStackTrace();
			return new ActionResult(ActionResult.EXCEPTION, e);
		}
		return new ActionResult(ActionResult.STRING, "Create RelationShip Success");
	}

	private void createRelation(IItem part, ArrayList<IItem> docs) throws APIException {
		IItem[] items=new IItem[docs.size()];
		int i=0;
		for(IItem s:docs){
			items[i]=docs.get(i);
			i++;
		}
		if(part!=null){
			ITable table = part.getTable(ItemConstants.TABLE_RELATIONSHIPS);
			
			table.createRows(items);
		}
		
	}

	public static void main(String[] args) {

		try {
			IAgileSession session = HazyUtil.getLinuxUtil().getLocalSession();
			IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "PQ0000013");//
			CreateRelationShip rel = new CreateRelationShip();
			System.out.println("Change:" + change);
			System.out.println(rel.doAction(session, null, change));
		} catch (APIException | HazyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
