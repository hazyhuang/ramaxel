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
import com.agile.api.ItemConstants;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.common.HazyUtil;

public class UpdateAttributeInMESDocument  implements ICustomAction {
	static Logger logger = Logger.getLogger(UpdateAttributeInMESDocument.class);

	@Override
	public ActionResult doAction(IAgileSession arg0, INode arg1, IDataObject arg2) {
		IChange change = (IChange) arg2;
		try {
			ITable table = change.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
			@SuppressWarnings("rawtypes")
			Iterator iter = table.getReferentIterator();
			while (iter.hasNext()) {
				IItem item = (IItem) iter.next();
				if("C_MES".equals(item.getAgileClass().getAPIName()));
				this.updateAttribute(item);
			}
			
		} catch (APIException e) {
			e.printStackTrace();
		    return new ActionResult(ActionResult.EXCEPTION, e);
		
		}
		return new ActionResult(ActionResult.STRING, "success.");
	}

		
	public void updateAttribute(IItem item) throws APIException {
		
		IItem fg=HazyUtil.getAgileAPIHelper().getSingleListItemValue(item, ItemConstants.ATT_PAGE_THREE_LIST01);
		if(fg!=null){
			logger.debug(fg.getName());
			Object text01=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT01);
			Object text02=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT02);
			Object text03=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT03);
			Object text04=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT04);
			Object text05=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT05);
			Object text06=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT06);
			Object text07=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT07);
			Object text08=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT08);
			Object text09=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT09);
			Object text10=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT10);
			Object text11=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT11);
			Object text12=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT12);
			Object text13=fg.getValue(ItemConstants.ATT_PAGE_THREE_TEXT13);
			Map<Object,Object> values=new HashMap<Object,Object>();
			if(text01!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT01, text01);
			}
			if(text02!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT02, text02);
			}
			if(text03!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT03, text03);
			}
			if(text04!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT04, text04);
			}
			if(text05!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT05, text05);
			}
			if(text06!=null){
				item.setValue(ItemConstants.ATT_PAGE_THREE_TEXT06, text06);
			}
			if(text07!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT07, text07);
			}
			if(text08!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT08, text08);
			}
			if(text09!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT09, text09);
			}
			if(text10!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT10, text10);
			}
			if(text11!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT11, text11);
			}
			if(text12!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT12, text12);
			}
			if(text13!=null){
				values.put(ItemConstants.ATT_PAGE_THREE_TEXT13, text13);
			}
			if(!values.isEmpty()){
			item.setValues(values);
			}
		    //主控位置text01
			//封装类型 text02
			//主控研磨厚度text03
			//闪存研磨厚度text04
			//注胶厚度text05
			//封装尺寸text06
			//主控尺寸text07
			//闪存尺寸text08
			//基板厚度text09
			//焊线图编号text10
			//EOL产品图纸编号text11
			//锡球数text12
			//线数text13
				     
		}
	          // list.
	}


}
