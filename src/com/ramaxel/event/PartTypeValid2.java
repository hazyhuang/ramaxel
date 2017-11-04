package com.ramaxel.event;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;

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

import com.hazy.common.HazyUtil;
import com.ramaxel.valid.PartValidation;

public class PartTypeValid2 implements IEventAction {

	public EventActionResult doAction(IAgileSession session, INode actionNode, IEventInfo request) {

		try {

			int eventType = request.getEventType();
			String ret = "";
			if (eventType == EventConstants.EVENT_APPROVE_FOR_WORKFLOW) {
				ISignOffEventInfo info = (ISignOffEventInfo) request;
				IChange chg = (IChange) info.getDataObject();

				if ("Review2".equals(chg.getStatus().getAPIName())) {
					ret = this.partTypeValidation(session, chg);
				}
			} else if (eventType == EventConstants.EVENT_CHANGE_STATUS_FOR_WORKFLOW) {
				IWFChangeStatusEventInfo info = (IWFChangeStatusEventInfo) request;
				IChange chg = (IChange) info.getDataObject();
				ret = this.partTypeValidation(session, chg);
			}
			if (ret.length() > 10) {
				throw new EventException((short) 0, ret);
			}
			ActionResult actionResult = new ActionResult(ActionResult.STRING, "非卡控节点。");
			return new EventActionResult(request, actionResult);

		} catch (APIException | FileNotFoundException e) {
			e.printStackTrace();
			return new EventActionResult(request, new ActionResult(ActionResult.EXCEPTION, e));
		}
	}

	public String partTypeValidation(IAgileSession session, IChange chg) throws APIException, FileNotFoundException {
		String fullPath = HazyUtil.getLinuxUtil().getExtensionPath("part_type_valid2.xls");
		String apiName = chg.getStatus().getAPIName();
		System.out.println(apiName);
		StringBuffer validString = new StringBuffer();
		Map<String, String> partTypeMap = PartValidation.loadConfig2(fullPath);
		ITable table = chg.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
		@SuppressWarnings("rawtypes")
		Iterator iter = table.getReferentIterator();
		System.out.println(table);
		while (iter.hasNext()) {
			IItem item = (IItem) iter.next();

			String partType = HazyUtil.getAgileAPIHelper().getSingleListValue(item,
					ItemConstants.ATT_TITLE_BLOCK_PART_TYPE);
			String valid = partTypeMap.get(partType);
			System.out.println(partType + ":" + valid);
			if ("是".equals(valid)) {
				String ret = validationMfr(item);
				validString.append(ret);
			}
		}
		return validString.toString();
	}

	private String validationMfr(IItem item) throws APIException {
		ITable table = item.getTable(ItemConstants.TABLE_MANUFACTURERS);
		if (table.size() == 0) {
			return "物料:" + item.getName() + "没有添加制造商";
		} else {
			return "";
		}

	}
}
