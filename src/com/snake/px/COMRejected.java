package com.snake.px;

import com.agile.api.APIException;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileSession;
import com.agile.api.IChange;
import com.agile.api.IDataObject;
import com.agile.api.INode;
import com.agile.api.IStatus;
import com.agile.px.ActionResult;
import com.agile.px.EventActionResult;
import com.agile.px.IEventAction;
import com.agile.px.IEventInfo;
import com.agile.px.IObjectEventInfo;
import com.hazy.common.HazyUtil;

import org.w3c.dom.events.EventException;

public class COMRejected implements IEventAction {
	public EventActionResult doAction(IAgileSession session, INode pxNOde, IEventInfo req) {
		IObjectEventInfo eventInfo = (IObjectEventInfo) req;
		try {
			IDataObject dataObject = eventInfo.getDataObject();
			IChange change = (IChange) dataObject;
			return new EventActionResult(req, execAction(change));
		} catch (APIException e) {
			e.printStackTrace();
			return new EventActionResult(req, new ActionResult(ActionResult.EXCEPTION, e));
		}
	}

	private ActionResult execAction(IChange change) throws APIException {

		String message = "";
		String define = change.getValue(ChangeConstants.ATT_PAGE_THREE_LIST04) == null ? ""
				: change.getValue(ChangeConstants.ATT_PAGE_THREE_LIST04).toString();

		System.out.println("第三页的修改后审核是==============" + define);
		IStatus currentStatu = change.getStatus();
		String status = currentStatu.getName();
		System.out.println("当前的状态============" + status);
		if (status.contains("环保工程师审核") || status.contains("体系文控审核") || status.contains("标准化审核")) {
			if ((define == null) || (define.trim().equals(""))) {
				message = "【封面】中的“修改后审核”属性不能为空";
				return new ActionResult(ActionResult.EXCEPTION, new EventException((short) 0, message));
			}
			System.out.println("当前的状态============" + currentStatu.getName() + "  \n 当前的流程"
					+ change.getValue(ChangeConstants.ATT_COVER_PAGE_CHANGE_TYPE));
			String name = change.getValue(ChangeConstants.ATT_COVER_PAGE_CHANGE_TYPE).toString();

			if ((name != null) && (name.contains("文档评审"))) {
				String typeOfEr = change.getValue(ChangeConstants.ATT_PAGE_THREE_LIST03).toString();
				System.out.println("错误类型=============" + change.getValue(ChangeConstants.ATT_PAGE_THREE_LIST03));
				if ((typeOfEr == null) || (typeOfEr.trim().equals(""))) {
					message = "【封面】中的“错误类型”属性不能为空";
					return new ActionResult(ActionResult.EXCEPTION, new EventException((short) 0, message));
				}
			}
		} else {
			System.out.println("不用拦截！");
			System.out.println("当前的状态============" + currentStatu.getName());
		}
		return new ActionResult(ActionResult.STRING, "Success");
	}

	public static void main(String[] args) {
		try {
			HazyUtil.getLogHelper().initLogger();
			IAgileSession session = HazyUtil.getLinuxUtil().getLocalSession();
			COMRejected action = new COMRejected();
			IChange change = (IChange) session.getObject(3, "NPR0000042");
			System.out.println(action.execAction(change));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}