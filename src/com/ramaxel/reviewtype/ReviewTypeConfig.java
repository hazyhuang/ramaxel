package com.ramaxel.reviewtype;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import com.agile.api.APIException;
import com.agile.api.IChange;
import com.hazy.common.HazyUtil;
import com.ramaxel.poi.POIHelper;

public class ReviewTypeConfig {
	
	
	
	private ArrayList<ReviewTypeSet> list=null;
	private IChange chg=null;
	
	public ReviewTypeConfig(String fullPath,IChange chg) throws FileNotFoundException{
		this.list=ReviewTypeConfig.loadConfig(fullPath);
		this.chg=chg;
	}
	//set_review_type.xls
	public static ArrayList<ReviewTypeSet> loadConfig(String fullPath) throws FileNotFoundException{
		POIHelper excelReader = new POIHelper();
		 
		InputStream is2 = HazyUtil.getFileHelper().getInputStreamByFile(fullPath);
		ArrayList<ReviewTypeSet> list= excelReader.readExcelContent(is2);
		return list;
	}
	public void setValue(String workflow,String status) throws APIException{
		System.out.println("input="+workflow+":"+status);
		ReviewTypeSet rts=getCondition(workflow,status);
		System.out.println("config="+rts);
		if(rts!=null){
		String attr=rts.getConditionAttr();
		System.out.println("attr="+attr);
		if(attr==""||attr==null||attr.length()<5){
			
			ReviewTypeSet rtvalue=getValueFromConfig(workflow,status);
			System.out.println("value="+rtvalue);
			if(rtvalue!=null){
			setValue(rtvalue);
			}
		}else{
		
			String conditionValue=getConditionValueFromAgile(getInteger(attr));
			ReviewTypeSet rtvalue=getValueFromConfig(workflow,status,conditionValue);
			if(rtvalue!=null){
			setValue(rtvalue);
			}
		}}
	}
	Integer getInteger(String attr){
		
		int start=attr.indexOf('[');
		int end=attr.indexOf(']');
		System.out.println(start+"="+end);
		String attrInt=attr.substring(start+1, end);
		  System.out.println("attrInteger1:"+attrInt);
	    Integer attrInteger=Integer.valueOf(attrInt);
	    System.out.println("attrInteger:"+attrInteger);
	    return attrInteger;
	}
	private ReviewTypeSet getValueFromConfig(String workflow2, String status2) {
		for(ReviewTypeSet s:list){
			if(workflow2.equals(s.getWorkflow())){
				if(status2.equals(s.getStatus())){
					return s;
				}
			}
		}
		return null;
	}

	private void setValue(ReviewTypeSet value) throws APIException {
		System.out.println(value.getValueAttr()+":"+value.getValue());
		HazyUtil.getAgileAPIHelper().setListValue(chg, this.getInteger(value.getValueAttr()), value.getValue());
		System.out.println(value.getValueAttr()+":"+value.getValue());
	}

	private ReviewTypeSet getValueFromConfig(String workflow2, String status2, String conditionValue) {
		for(ReviewTypeSet s:list){
			if(workflow2.equals(s.getWorkflow())){
				if(status2.equals(s.getStatus())){
					if(conditionValue.equals(s.getCondition())){
						return s;
					}
				}
			}
		}
		return null;
	}

	private String getConditionValueFromAgile(Integer attr) throws APIException {
		return HazyUtil.getAgileAPIHelper().getSingleListValue(chg, attr);
		
	}

	private ReviewTypeSet getCondition(String workflow,String status){
		for(ReviewTypeSet s:list){
			if(workflow.equals(s.getWorkflow())){
				if(status.equals(s.getStatus())){
					return s;
				}
			}
		}
		return null;
	}
}
