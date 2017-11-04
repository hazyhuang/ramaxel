package com.ramaxel.event;


import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IItem;
import com.agile.api.INode;
import com.agile.api.ISignoffReviewer;
import com.agile.px.ActionResult;
import com.agile.px.EventActionResult;
import com.agile.px.EventConstants;
import com.agile.px.IEventAction;
import com.agile.px.IEventInfo;
import com.agile.px.IUpdateTitleBlockEventInfo;
import com.agile.px.IWFChangeStatusEventInfo;


public class ChangeStatusEvent implements IEventAction {

	
	public EventActionResult doAction(IAgileSession session, INode actionNode,
			IEventInfo request) {
		 
		try {

			 int eventType= request.getEventType();
			

			

			if (eventType == EventConstants.EVENT_UPDATE_TITLE_BLOCK) {
				IUpdateTitleBlockEventInfo info = (IUpdateTitleBlockEventInfo) request;
				IItem part = (IItem) info.getDataObject();

				 updateItemDesc(session, info, part);
			}else if(eventType == EventConstants.EVENT_CHANGE_STATUS_FOR_WORKFLOW){
				IWFChangeStatusEventInfo info=(IWFChangeStatusEventInfo) request; 
				ISignoffReviewer[] re=info.getApprovers();
				StringBuffer sb=new StringBuffer();
				for(int i=0;i<re.length;i++){
					sb.append("["+re[i]+":"+re[i].getReviewer().getName()+"]");
				}
				return new EventActionResult(request, new ActionResult(
						ActionResult.STRING, "Event Change Status"+sb.toString()));
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
