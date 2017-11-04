package com.ramaxel.px;

import java.io.FileNotFoundException;

import com.agile.api.APIException;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileSession;
import com.agile.api.IChange;
import com.agile.api.IDataObject;
import com.agile.api.INode;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.common.HazyUtil;
import com.ramaxel.reviewtype.ReviewTypeConfig;

public class FillReviewType implements ICustomAction{

	@Override
	public ActionResult doAction(IAgileSession arg0, INode arg1, IDataObject arg2) {
		IChange chg=(IChange)arg2;
		try {
			String fullPath= HazyUtil.getLinuxUtil().getExtensionPath("set_review_type.xls");
			String chgType=HazyUtil.getAgileAPIHelper().getSingleListValue(chg, ChangeConstants.ATT_COVER_PAGE_CHANGE_TYPE);
	        String status=chg.getStatus().getName();
	        System.out.println(chgType+":"+status);
	        ReviewTypeConfig rtc=new ReviewTypeConfig(fullPath,chg);
	        rtc.setValue(chgType, status);
		} catch (APIException | FileNotFoundException e) {
			
			e.printStackTrace();
			return new ActionResult(ActionResult.EXCEPTION, e );
		}
		return new ActionResult(ActionResult.STRING, "Fill Review Type Success" );
	}

}
