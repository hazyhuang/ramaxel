package com.ramaxel.test;

import java.io.File;
import java.util.Properties;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IFileFolder;
import com.hazy.common.HazyException;
import com.hazy.common.HazyUtil;

public class DownloadTest {

	public static void main(String[] args) {
		try {
			  Properties props = System.getProperties();
		        String osName= props.getProperty("os.name");
			String fullPath=null;
			
			if("Windows Server 2008 R2".equals(osName)){
			fullPath=ClassLoader.getSystemResource("").getPath()+"conf.properties";
			}else{
				fullPath=ClassLoader.getSystemResource("").getPath()
						+"../integration/sdk/extensions/conf.properties";
			}
			Properties prop=HazyUtil.getInstance().loadProperties(fullPath);
			String agileurl=prop.getProperty("agileurl");
			String agileuser=prop.getProperty("agileuser");
			String agilepwd=prop.getProperty("agilepwd");
			IAgileSession session =HazyUtil.getAgileAPIHelper().login(agileurl, agileuser, agilepwd);
			upload(session,new File("c:/cc.doc"));
		} catch (APIException | HazyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void upload(IAgileSession session, File file) throws APIException {
		IFileFolder folder=HazyUtil.getAgileAPIHelper().loadFileFolder(session, "FOLDER13199");
		HazyUtil.getAgileAPIHelper().uploadFile(session, folder, file);
		
	}

}
