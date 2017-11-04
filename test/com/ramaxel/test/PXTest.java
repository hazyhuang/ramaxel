package com.ramaxel.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IChange;
import com.agile.api.IItem;
import com.hazy.common.HazyException;
import com.hazy.common.HazyUtil;
import com.ramaxel.event.ItemDescValid;
import com.ramaxel.event.PartTypeValid;
import com.ramaxel.event.PartTypeValid2;
import com.ramaxel.event.UpdateInventoryCycleTime;
import com.ramaxel.px.AddApproverByItemCreator;
import com.ramaxel.px.AutoChangeLifeCycle;
import com.ramaxel.px.CreateRelationShip;
import com.ramaxel.px.FileDownload;
import com.ramaxel.px.FillReviewType;
import com.ramaxel.px.FillRevision;
import com.ramaxel.px.IPDerive;
import com.ramaxel.px.UpdateInventoryCycleTimePX;

public class PXTest {
	static Logger logger = Logger.getLogger(PXTest.class);
	AddApproverByItemCreator app = new AddApproverByItemCreator();
	UpdateInventoryCycleTime ict = new UpdateInventoryCycleTime();
	AutoChangeLifeCycle ac = new AutoChangeLifeCycle();
	IPDerive derive = new IPDerive();
	FillReviewType fillReviewType = new FillReviewType();
	PartTypeValid partTypeValid = new PartTypeValid();
	PartTypeValid2 partTypeValid2 = new PartTypeValid2();
	FillRevision fillRev = new FillRevision();
	CreateRelationShip rel = new CreateRelationShip();
	IAgileSession session;
	ItemDescValid descValid = new ItemDescValid();
	FileDownload download=new FileDownload();
	@Before
	public void setUp() throws Exception {
		double begin=HazyUtil.getTimeHelper().getBeginTime();
		HazyUtil.getLogHelper().initLogger();
		session =HazyUtil.getLinuxUtil().getLocalSession();
		logger.debug("[Set UP]" + HazyUtil.getTimeHelper().getTotalTimeSecond(begin) + "s");

	}
	
@Test
	public void testDownload() throws APIException {
		//abcd@1234
		IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "DCN0000025");
		double begin=HazyUtil.getTimeHelper().getBeginTime();
		
		logger.debug("[Change]:" + change);
		logger.debug(download.doAction(session, null, change));

		logger.debug(HazyUtil.getTimeHelper().getTotalTimeSecond(begin) + "s");

		
	}
	
	
	//@Test
	public void testDownloadItem() throws APIException, HazyException {
		
		IItem item = HazyUtil.getAgileAPIHelper().loadItem(session, "MSW0000234");
		double begin=HazyUtil.getTimeHelper().getBeginTime();
		
		logger.debug("[Item]:" + item);
		download.download(item);

		logger.debug(HazyUtil.getTimeHelper().getTotalTimeSecond(begin) + "s");

		
	}

	//@Test
	public void testAddApproverByItemCreator() throws APIException {
		
		IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "ECN_00000154");
		double begin=HazyUtil.getTimeHelper().getBeginTime();
		
		logger.debug("[Change]:" + change);
		logger.debug("[AddApproverByItemCreator]:" + app.doAction(session, null, change));

		logger.debug("[AddApproverByItemCreator]" + HazyUtil.getTimeHelper().getTotalTimeSecond(begin) + "s");

		
	}

	// @Test
	public void testAutoChangeLifeCycle() throws APIException {
		IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "PQ0000005");//
		System.out.println("jTest Change:" + change);
		double begin = System.currentTimeMillis();
		System.out.println("jTest:" + ac.doAction(session, null, change));
		double d = (System.currentTimeMillis() - begin) / 1000;
		System.out.println("AutoChangeLifeCycle:" + d + "s");
	}

	 //@Test
	public void testUpdateInventoryCycleTime() throws APIException, FileNotFoundException, HazyException {
	
		IItem part = HazyUtil.getAgileAPIHelper().loadItem(session, "1809-00025");//
		double begin = System.currentTimeMillis();
		int ret=ict.updateItemInventoryCycleTime(session,  part);
		 org.junit.Assert.assertEquals(0, ret);
		double end = System.currentTimeMillis();
		double d = (end - begin) / 1000;
		System.out.println("Run Time:" + d + "s");
	}
	
	// @Test
	public void testGetItem() throws APIException, FileNotFoundException, HazyException {
	
		IItem part = HazyUtil.getAgileAPIHelper().loadItem(session, "1809-00025");//
		double begin = System.currentTimeMillis();
		System.out.println(part.getAgileClass());
		System.out.println(part.getAgileClass().getId());
		double end = System.currentTimeMillis();
		double d = (end - begin) / 1000;
		System.out.println("Run Time:" + d + "s");
	}
	// @Test
	public void testUpdateInventoryCycleTime2() throws APIException, FileNotFoundException, HazyException {
		//HazyUtil.getLogHelper().initLogger();
		IItem part = HazyUtil.getAgileAPIHelper().loadItem(session, "1301-80001");//
		double begin = System.currentTimeMillis();
		int ret=ict.updateItemInventoryCycleTime(session,  part);
		assertEquals(0, ret);
		double end = System.currentTimeMillis();
		double d = (end - begin) / 1000;
		System.out.println("Run Time:" + d + "s");
	}
	UpdateInventoryCycleTimePX up=new UpdateInventoryCycleTimePX();
	// @Test
		public void testUpdateInventoryCycleTimePX() throws APIException, FileNotFoundException {
			//HazyUtil.getLogHelper().initLogger();
			IChange part = HazyUtil.getAgileAPIHelper().loadChange(session, "ECN_00000154");//
			double begin = System.currentTimeMillis();
			up.doAction(session, null, part);
			
			double end = System.currentTimeMillis();
			double d = (end - begin) / 1000;
			System.out.println("Run Time:" + d + "s");
		}

	//@Test
	public void testLoadXML() throws APIException, FileNotFoundException, HazyException {
		double begin=HazyUtil.getTimeHelper().getBeginTime();


		String fullPath=HazyUtil.getLinuxUtil().getExtensionPath("InventoryCycleTimeRule.xml");
		HazyUtil.createTreeNodeXMLDAO(fullPath).loadXMLTreeNode();
		
		logger.debug("[testLoadXML]" + HazyUtil.getTimeHelper().getTotalTimeSecond(begin) + "s");
	}

	//@Test
	public void testIPDerive() throws APIException {
		
		IItem item =HazyUtil.getAgileAPIHelper().loadItem(session, "IP0000001");
		logger.debug("[Item]:" + item);
		
		double begin=HazyUtil.getTimeHelper().getBeginTime();
		logger.debug("[IPDerive]"+derive.doAction(session, null, item));
		
		logger.debug("[IPDerive]" + HazyUtil.getTimeHelper().getTotalTimeSecond(begin) + "s");
	}

	//@Test
	public void testFillReviewType() throws APIException {
		IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "CDR0000002");//
		System.out.println("Change:" + change);
		System.out.println(this.fillReviewType.doAction(session, null, change));
	}

	//@Test
	public void testPartType() throws APIException, FileNotFoundException {
		IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "NPR0000042");//
		System.out.println("Change:" + change);
		System.out.println(this.partTypeValid.partTypeValidation(session, change));
	}
	

	//@Test
	public void testPartType2() throws APIException, FileNotFoundException {
		IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "PQ0000027");//
		System.out.println("Change:" + change);
		System.out.println(this.partTypeValid2.partTypeValidation(session, change));
	}
	
	

	 //@Test
	public void testFillRev() throws APIException {
		IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "PDB0000015");//
		System.out.println("Change:" + change);
		System.out.println(this.fillRev.doAction(session, null, change));
	}

	// @Test
	public void testCreateRelationShip() throws APIException {
		IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "PQ0000013");//
		System.out.println("Change:" + change);
		System.out.println(this.rel.doAction(session, null, change));
	}

}
