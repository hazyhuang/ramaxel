package com.ramaxel.px;

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
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.common.HazyException;
import com.hazy.common.HazyUtil;

public class FillRevision implements ICustomAction{

	@Override
	public ActionResult doAction(IAgileSession arg0, INode arg1, IDataObject arg2) {
		IChange chg=(IChange)arg2;
		try {
			ITable table =chg.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
			Iterator iter=table.getTableIterator();
			while(iter.hasNext()){
				IRow row=(IRow)iter.next();
				IItem item=(IItem)row.getReferent();
				String revision=item.getRevision();
				try{
			    row.setValue(ChangeConstants.ATT_AFFECTED_ITEMS_REVISION, revision);
				}catch(APIException ex){
					System.out.println(ex.getErrorCode());
					if(!(new Integer(310)).equals(ex.getErrorCode())){
						throw ex;
					}
				}
				System.out.println("Rev:"+revision);
			}
		} catch (APIException e) {
			
			e.printStackTrace();
			return new ActionResult(ActionResult.EXCEPTION, e );
		}
		return new ActionResult(ActionResult.STRING, "Fill Revision Success" );
	}
	
	public static void main(String[] args) {

		try {
			IAgileSession session =HazyUtil.getLinuxUtil().getLocalSession();
			IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "PDB0000016");//
			FillRevision rel = new FillRevision();
			System.out.println("Change:" + change);
			System.out.println(rel.doAction(session, null, change));
		} catch (APIException | HazyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
