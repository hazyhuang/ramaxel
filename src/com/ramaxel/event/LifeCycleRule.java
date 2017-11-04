package com.ramaxel.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hazy.treenode.INode;

public class LifeCycleRule {
	
	private static Object PROP_NUMBER_KEY="number";
	public static Object PROP_TYPE_KEY="type";
	public static Object PROP_VALUE_KEY="value";
	public static Object PROP_MAX_KEY="max";
	public static Object PROP_MIN_KEY="min";
	public static Object PROP_ATTR_ID_KEY="attributeid";
	public static Object TYPE_SINGLE="Single";
	public static Object TYPE_TWICE="Twice";
	public static Object TYPE_THRICE="Thrice";
	public static Object TYPE_MFR="Manufacturer";
	
	private INode rootNode=null;
	private INode node=null;
	private String min=null;
	private String max=null;
	private Map<String,INode> nodeMap= new HashMap<String,INode>();
	

	public LifeCycleRule(INode rootNode,String key){
		this.rootNode=rootNode;
		this.initMap();
		this.node=this.nodeMap.get(key);
		
	}
	
	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getSingleMax(){
		return (String)node.getProperty().get(PROP_MAX_KEY);
	}
	public String getSingleMin(){
		return (String)node.getProperty().get(PROP_MIN_KEY);
	}
	
	
	public INode getNode(){
		return node;
	}
	
	public Object getType(String key){
		return node.getProperty().get(PROP_TYPE_KEY);
	}
	
	private void initMap(){
		Collection<INode> children=  rootNode.getChildren();
		Iterator<INode> iter=children.iterator();
		while(iter.hasNext()){
			INode child=iter.next();
			String number=(String)child.getProperty().get(PROP_NUMBER_KEY);
			String[] keys=number.split(";");
			for(int i=0;i<keys.length;i++){
			
				this.nodeMap.put(keys[i],child);
			}
		}
	}
	


}
