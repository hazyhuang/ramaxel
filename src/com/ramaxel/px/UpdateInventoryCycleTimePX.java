package com.ramaxel.px;

import java.io.FileNotFoundException;
import java.util.Collection;
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
import com.agile.api.IManufacturer;
import com.agile.api.INode;
import com.agile.api.ITable;
import com.agile.api.ItemConstants;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.common.HazyException;
import com.hazy.common.HazyUtil;
import com.ramaxel.event.LifeCycleRule;

public class UpdateInventoryCycleTimePX  implements ICustomAction {
	static Logger logger = Logger.getLogger(UpdateInventoryCycleTimePX.class);
	com.hazy.treenode.INode rootNode=null;
	@Override
	public ActionResult doAction(IAgileSession session, INode arg1, IDataObject arg2) {
		IChange change = (IChange) arg2;
		try {
			String fullPath=HazyUtil.getLinuxUtil().getExtensionPath("InventoryCycleTimeRule.xml");
			this.rootNode=HazyUtil.createTreeNodeXMLDAO(fullPath).loadXMLTreeNode();
	       
			ITable table = change.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
			@SuppressWarnings("rawtypes")
			Iterator iter = table.getReferentIterator();
			while (iter.hasNext()) {
				IItem item = (IItem) iter.next();
				if(item.getAgileClass().isSubclassOf(ItemConstants.CLASS_PARTS_CLASS)){
					updateItemInventoryCycleTime(session,  item);
				}
				
			}
		
		} catch (APIException | FileNotFoundException | HazyException e) {
		
				return new ActionResult(ActionResult.STRING, e.getMessage());
			
		}
		return new ActionResult(ActionResult.STRING, "success.");
	}
	public int updateItemInventoryCycleTime(IAgileSession session, IItem part) throws APIException, FileNotFoundException {
		System.out.println(part);
		
		String parttype=part.getValue(ItemConstants.ATT_TITLE_BLOCK_PART_TYPE).toString();
		String key=parttype.substring(0, 4);
		LifeCycleRule rule=new LifeCycleRule(rootNode,key);
		if(rule.getNode()==null){
			return 1;
		}else{
		Object type=rule.getType(key);
		if(LifeCycleRule.TYPE_SINGLE.equals(type)){
			String min=rule.getSingleMin();
			String max=rule.getSingleMax();
			rule.setMax(max);
			rule.setMin(min);
		}else if(LifeCycleRule.TYPE_TWICE.equals(type)){
			rule = getLifeCycleTime(rule,part);
		}else if(LifeCycleRule.TYPE_THRICE.equals(type)){
			rule = getLifeCycleTimeThrice(rule,part);
		}else if(LifeCycleRule.TYPE_MFR.equals(type)){
			  rule = getLifeCycleTimeMfr(rule,part);
		}
		System.out.println(rule.getMax()+":"+rule.getMin());
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put(ItemConstants.ATT_PAGE_TWO_TEXT15, rule.getMin());
		map.put(ItemConstants.ATT_PAGE_TWO_TEXT16, rule.getMax());
		part.setValues(map);
		return 0;
		}
		
	}
	
	private LifeCycleRule getLifeCycleTimeThrice(LifeCycleRule rule,IItem part) throws APIException{
		String attributeid=(String)rule.getNode().getProperty().get(LifeCycleRule.PROP_ATTR_ID_KEY);
		   Integer attr= Integer.valueOf(attributeid);
		   Object attrValue=part.getValue(attr);
		   Collection<com.hazy.treenode.INode> children=rule.getNode().getChildren();
		   for(com.hazy.treenode.INode child:children){
			   Object childValue=child.getProperty().get(LifeCycleRule.PROP_VALUE_KEY);
			   Object childType=child.getProperty().get(LifeCycleRule.PROP_VALUE_KEY);
			  if( childValue.equals(attrValue)&&LifeCycleRule.TYPE_SINGLE.equals(childType)){
				String  min=(String)child.getProperty().get(LifeCycleRule.PROP_MAX_KEY);
				String  max=(String)child.getProperty().get(LifeCycleRule.PROP_MIN_KEY);
				rule.setMax(max);
				rule.setMin(min);
			  }else if(childValue.equals(attrValue)&&LifeCycleRule.TYPE_TWICE.equals(childType)){
				  rule = getLifeCycleTime(rule,part);
				
			  }
		   }
		   return rule;
	}
	private LifeCycleRule getLifeCycleTimeMfr(LifeCycleRule rule,IItem part) throws APIException{
		ITable table=part.getTable(ItemConstants.TABLE_MANUFACTURERS);
		@SuppressWarnings("rawtypes")
		Iterator iter=table.getReferentIterator();
		IManufacturer mfr=null;
		while(iter.hasNext()){
			mfr=(IManufacturer)iter.next();
		}
	
		   Collection<com.hazy.treenode.INode> children=rule.getNode().getChildren();
		   for(com.hazy.treenode.INode child:children){
			  if( child.getProperty().get(LifeCycleRule.PROP_VALUE_KEY).equals(mfr.getName())){
				  String min=(String)child.getProperty().get(LifeCycleRule.PROP_MAX_KEY);
				  String max=(String)child.getProperty().get(LifeCycleRule.PROP_MIN_KEY);
				  rule.setMax(max);
				  rule.setMin(min);
			  }
		   }
		   
		   return rule;
	}
	private LifeCycleRule getLifeCycleTime(LifeCycleRule rule,IItem part) throws APIException{
		String attributeid=(String)rule.getNode().getProperty().get(LifeCycleRule.PROP_ATTR_ID_KEY);
		   Integer attr= Integer.valueOf(attributeid);
		   Object attrValue=part.getValue(attr);
		   Collection<com.hazy.treenode.INode> children=rule.getNode().getChildren();
		   for(com.hazy.treenode.INode child:children){
			  if( child.getProperty().get(LifeCycleRule.PROP_VALUE_KEY).equals(attrValue)){
				  String min=(String)child.getProperty().get(LifeCycleRule.PROP_MAX_KEY);
				  String max=(String)child.getProperty().get(LifeCycleRule.PROP_MIN_KEY);
				  rule.setMax(max);
				  rule.setMin(min);
			  }
		   }
		   
		   return rule;
	}


}
