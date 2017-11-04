package com.ramaxel.reviewtype;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import com.hazy.common.HazyUtil;
import com.ramaxel.poi.POIHelper;

public class ReviewTypeSet {
	private String workflow;
	private String status;
	private String conditionAttr;
	private String condition;
	private String valueAttr;
	private String value;
    public String toString(){
    	return workflow+","+status+","+conditionAttr+","+condition+","+valueAttr+","+value;
    }
	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConditionAttr() {
		return conditionAttr;
	}

	public void setConditionAttr(String conditionAttr) {
		this.conditionAttr = conditionAttr;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}


	public String getValueAttr() {
		return valueAttr;
	}

	public void setValueAttr(String valueAttr) {
		this.valueAttr = valueAttr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static void main(String[] args) throws FileNotFoundException {

		// 对读取Excel表格标题测试
		InputStream is = HazyUtil.getFileHelper().getInputStreamByFile("set_review_type.xls");
		POIHelper excelReader = new POIHelper();
		String[] title = excelReader.readExcelTitle(is);
		System.out.println("获得Excel表格的标题:");
		for (String s : title) {
			System.out.print(s + " ");
		}

		// 对读取Excel表格内容测试
		InputStream is2 =  HazyUtil.getFileHelper().getInputStreamByFile("set_review_type.xls");
		ArrayList<ReviewTypeSet> list= excelReader.readExcelContent(is2);
		System.out.println("\n获得Excel表格的内容:");
		for (ReviewTypeSet s:list) {
			System.out.println(s.toString());
		}

	}

}
