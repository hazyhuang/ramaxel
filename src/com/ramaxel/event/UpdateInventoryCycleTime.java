package com.ramaxel.event;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IItem;
import com.agile.api.IManufacturer;
import com.agile.api.INode;
import com.agile.api.ITable;
import com.agile.api.ItemConstants;
import com.agile.px.ActionResult;
import com.agile.px.EventActionResult;
import com.agile.px.EventConstants;
import com.agile.px.ICreateEventInfo;
import com.agile.px.IEventAction;
import com.agile.px.IEventInfo;
import com.hazy.common.HazyException;
import com.hazy.common.HazyUtil;

public class UpdateInventoryCycleTime implements IEventAction{

	@Override
	public EventActionResult doAction(IAgileSession session, INode actionNode,
			IEventInfo request) {
		int ret=-1;
		try {
           int eventType= request.getEventType();
	        if(eventType==EventConstants.EVENT_CREATE_OBJECT){
	        	ICreateEventInfo info = (ICreateEventInfo)request;
	     
				IItem part = (IItem) info.getDataObject();
			
				ret= updateItemInventoryCycleTime(session, part);
			}
	         if(ret ==1){
	        	return new EventActionResult(request, new ActionResult(
						ActionResult.STRING, "this type of part is not in configration file!"));
	        }else{
	        	
	    			return new EventActionResult(request, new ActionResult(
	    					ActionResult.STRING, "Update Inventory Cycle Time Success!"));
	    	      
	        }

		} catch (APIException | FileNotFoundException | HazyException e) {
			e.printStackTrace();
			return new EventActionResult(request, new ActionResult(
					ActionResult.EXCEPTION, e));
		} 
	
	}
    
	public int updateItemInventoryCycleTime(IAgileSession session, IItem part) throws APIException, FileNotFoundException, HazyException {
		System.out.println(part);
		String fullPath=HazyUtil.getLinuxUtil().getExtensionPath("InventoryCycleTimeRule.xml");
		com.hazy.treenode.INode rootNode=HazyUtil.createTreeNodeXMLDAO(fullPath).loadXMLTreeNode();
        String parttype=part.getValue(ItemConstants.ATT_TITLE_BLOCK_PART_TYPE).toString();
		//String name=part.getName();
        
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
		Map map=new HashMap();
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
