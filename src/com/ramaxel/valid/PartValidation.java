package com.ramaxel.valid;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import com.hazy.common.HazyUtil;
import com.ramaxel.poi.POIHelper;

public class PartValidation {

	
	//part_type_valid.xls
	public static Map<String,String> loadConfig(String config1_path) throws FileNotFoundException{
		POIHelper excelReader = new POIHelper();
		InputStream is2 = HazyUtil.getFileHelper().getInputStreamByFile(config1_path);
		Map<String,String> partTypeValidMap= excelReader.readExcelPartType(is2);
		return partTypeValidMap;
	}
	//"part_type_valid2.xls"
	public static Map<String,String> loadConfig2(String config2_path) throws FileNotFoundException{
		POIHelper excelReader = new POIHelper();
		InputStream is2 = HazyUtil.getFileHelper().getInputStreamByFile(config2_path);
		Map<String,String> partTypeValidMap= excelReader.readExcelPartType(is2);
		return partTypeValidMap;
	}

}
