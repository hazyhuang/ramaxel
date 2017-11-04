package com.ramaxel.event;


import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IItem;
import com.agile.api.INode;
import com.agile.px.ActionResult;
import com.agile.px.EventActionResult;
import com.agile.px.EventConstants;
import com.agile.px.IEventAction;
import com.agile.px.IEventInfo;
import com.agile.px.IUpdateTitleBlockEventInfo;


public class UpdateItemDescrition implements IEventAction {

	
	public EventActionResult doAction(IAgileSession session, INode actionNode,
			IEventInfo request) {
		try {

		

			IUpdateTitleBlockEventInfo info = (IUpdateTitleBlockEventInfo) request;

			if (info.getEventType() == EventConstants.EVENT_UPDATE_TITLE_BLOCK) {

				IItem part = (IItem) info.getDataObject();

				 updateItemDesc(session, info, part);
			}
			return new EventActionResult(request, new ActionResult(
					ActionResult.STRING, ""));

		} catch (APIException e) {
			e.printStackTrace();
			return new EventActionResult(request, new ActionResult(
					ActionResult.EXCEPTION, e));
		} 
	}

	private String updateItemDesc(IAgileSession session, IUpdateTitleBlockEventInfo info, IItem part) {
		// TODO Auto-generated method stub
		return null;
	}
}
