package com.ramaxel.px;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IDataObject;
import com.agile.api.INode;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.common.HazyException;
import com.hazy.common.HazyUtil;

public class PXTest implements ICustomAction {
	
	 private static Logger logger = Logger.getLogger(PXTest.class);
	@Override
	public ActionResult doAction(IAgileSession session, INode arg1, IDataObject item) {
        

		String str2 = " \n2." + PXTest.class.getClassLoader().getResource("").toString();
		String str3 = " \n3." + ClassLoader.getSystemResource("").toString();
		String str4 = " \n4." + PXTest.class.getResource("").toString();
		String str5 = " \n5." + PXTest.class.getResource("/").toString();
		String str6 = " \n6." +System.getProperty("user.dir");
		String s = str2 + str3 + str4 + str5+str6;
		logger.info(s);
		String fullPath=HazyUtil.getLinuxUtil().getExtensionPath("conf.properties");
		String p="";
		try {
			p=(String)HazyUtil.getInstance().loadProperties(fullPath).get("agileurl");
		} catch (HazyException e) {
			e.printStackTrace();
			return new ActionResult(ActionResult.STRING, "创建失败：" + s+" [p]"+p);
		}
		
		return new ActionResult(ActionResult.STRING, "创建成功：" + fullPath+" [p]"+p);

	}
	public static void main(String arg[]) throws APIException {
		String path=System.getProperty("user.dir");
		System.out.println("PATH1."+path);
		String str2 = " 2." + PXTest.class.getClassLoader().getResource("").toString();
		String str3 = " 3." + ClassLoader.getSystemResource("").toString();
		String str4 = " 4." + PXTest.class.getResource("").toString();
		String str5 = " 5." + PXTest.class.getResource("/").toString();
		String str6 = " 6." +Thread.currentThread().getContextClassLoader().getResource("").toString();
		System.out.println("PATH2:"+str2);
		System.out.println("PATH3:"+str3);
		System.out.println("PATH4:"+str4);
		System.out.println("PATH5:"+str5);
		System.out.println("PATH6:"+str6);
		PXTest px=new PXTest();
		System.out.println(px.doAction(null, null, null));
		
	}
	

}
