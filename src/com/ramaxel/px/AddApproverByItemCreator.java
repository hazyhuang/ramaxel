package com.ramaxel.px;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileSession;
import com.agile.api.IChange;
import com.agile.api.IDataObject;
import com.agile.api.IItem;
import com.agile.api.INode;
import com.agile.api.ITable;
import com.agile.api.IUser;
import com.agile.api.ItemConstants;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.common.HazyUtil;

public class AddApproverByItemCreator implements ICustomAction {
	static Logger logger = Logger.getLogger(AddApproverByItemCreator.class);

	@Override
	public ActionResult doAction(IAgileSession arg0, INode arg1, IDataObject arg2) {
		IChange change = (IChange) arg2;
		try {
			ITable table = change.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
			@SuppressWarnings("rawtypes")
			Iterator iter = table.getReferentIterator();
			Map<Object, IUser> usersMap = new HashMap<Object, IUser>();
			while (iter.hasNext()) {
				IItem item = (IItem) iter.next();
				IUser creator = (IUser) item.getCell(ItemConstants.ATT_PAGE_TWO_CREATE_USER).getReferent();
				usersMap.put(creator.getObjectId(), creator);
			}
			change.addReviewers(change.getStatus(), HazyUtil.getAgileAPIHelper().getPureNewUser(change, usersMap), null, null, true,
					"Auto Assign Approvers from AffectItem creator");
		} catch (APIException e) {
			e.printStackTrace();
			Object code = e.getErrorCode();
			Integer dupCode = 113;
			if (dupCode.equals(code)) {
				return new ActionResult(ActionResult.STRING, e.getMessage());
			} else {
				return new ActionResult(ActionResult.EXCEPTION, e);
			}
		}
		return new ActionResult(ActionResult.STRING, "success.");
	}


}
