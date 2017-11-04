package com.ramaxel.px;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileSession;
import com.agile.api.IChange;
import com.agile.api.IDataObject;
import com.agile.api.IItem;
import com.agile.api.INode;
import com.agile.api.IRow;
import com.agile.api.ITable;
import com.agile.api.ItemConstants;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.hazy.common.HazyException;
import com.hazy.common.HazyUtil;

public class FileDownloadSecond  implements ICustomAction {
	public static Logger logger = Logger.getLogger(FileDownloadSecond.class);
	@Override
	public ActionResult doAction(IAgileSession arg0, INode arg1, IDataObject arg2) {
		StringBuffer ret=new StringBuffer();
		IChange chg = (IChange) arg2;
		try {
			ITable table = chg.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
			IItem part=null;
			ArrayList<IItem> docs=new ArrayList<IItem>();
			@SuppressWarnings("rawtypes")
			Iterator iter = table.getTableIterator();
			while (iter.hasNext()) {
				IRow row = (IRow) iter.next();
				IItem item = (IItem) row.getReferent();
				if(item.getAgileClass().isSubclassOf(ItemConstants.CLASS_DOCUMENTS_CLASS)){
					ret.append(download(item));
				}
				
				
			}
		
		} catch (APIException | HazyException e) {

			e.printStackTrace();
			return new ActionResult(ActionResult.EXCEPTION, e);
		}
		return new ActionResult(ActionResult.STRING, "Download Success!");
	}

	public String download(IItem item) throws APIException, HazyException {
		StringBuffer ret=new StringBuffer();
		String conffilepath=HazyUtil.getLinuxUtil().getExtensionPath("conf.properties");
		Properties prop=HazyUtil.getInstance().loadPropertiesUTF8(conffilepath);
		FileDAO fileDAO=getFileDAO(prop);
		String filepath=prop.getProperty("secondfilepath");
		String vaultBase=prop.getProperty("AgileVaultBase");
		ITable table=item.getAttachments();
		Iterator iter=table.getTableIterator();
		while(iter.hasNext()){
			IRow row=(IRow)iter.next();
			ret.append(downloadFile(row,filepath,fileDAO,vaultBase));
		}
		return ret.toString();
		
	}
	
	private FileDAO getFileDAO(Properties prop){
	
		String db_path=prop.getProperty("db_path");
		String db_user=prop.getProperty("db_user");
		String db_pwd=prop.getProperty("db_pwd");
		
		FileDAO fileDAO=new FileDAO(db_path,db_user,db_pwd);
		return fileDAO;
	}
	
	public  String downloadFile(IRow rowj,String filepath,FileDAO fileDAO,String vaultBase) {
		StringBuffer infor = new StringBuffer();
		try {
			String filename = (String) rowj.getCell(ItemConstants.ATT_ATTACHMENTS_FILE_NAME).getValue().toString();
			//String filenameUTF=new String(filename.getBytes(),"utf-8");
			//filename = "Test" + filename;
			Object objid=rowj.getObjectId();
			logger.debug("ID"+objid);
			String fullfilename = filepath + filename;
			String vaultfilepath=fileDAO.loadFilePath(objid);
			String vaultFullFilePath=vaultBase+vaultfilepath;
		    logger.debug("fullfilepath:   "+fullfilename);
		    logger.debug(vaultFullFilePath);
			File temppath = new File(fullfilename);
			logger.debug(temppath.exists());
			if (!temppath.exists()) {
				String jnucode=System.getProperty("sun.jnu.encoding");
				infor.append("[sun.jnu.encoding]"+jnucode);
				HazyUtil.getFileHelper().copyFile(new File(vaultFullFilePath), fullfilename);
				//HazyUtil.getAgileAPIHelper().
			
			/*	InputStream tempfile = ((IAttachmentFile) rowj).getFile();
				FileOutputStream output = new FileOutputStream(fullfilename);
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = tempfile.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				tempfile.close();
				System.out.println("OK");*/
			}
			

		}catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}

		return infor.toString();
	}
	public static void main(String[] args) throws HazyException, APIException {
		double begin=HazyUtil.getTimeHelper().getBeginTime();
		HazyUtil.getLogHelper().initLogger();
		IAgileSession session =HazyUtil.getLinuxUtil().getLocalSession();
		logger.debug("[Set UP]" + HazyUtil.getTimeHelper().getTotalTimeSecond(begin) + "s");
		
		IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "DCN0000025");
		double begin2=HazyUtil.getTimeHelper().getBeginTime();
		FileDownloadSecond download=new FileDownloadSecond();
		logger.debug("[Change]:" + change);
		logger.debug(download.doAction(session, null, change));

		logger.debug(HazyUtil.getTimeHelper().getTotalTimeSecond(begin2) + "s");
	}

}
