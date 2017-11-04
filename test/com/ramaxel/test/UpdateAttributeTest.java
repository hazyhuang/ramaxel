package com.ramaxel.test;

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
import com.ramaxel.px.UpdateAttributeInMESDocument;

public class UpdateAttributeTest {
	static Logger logger = Logger.getLogger(DescValidTest.class);
	IAgileSession session;
	UpdateAttributeInMESDocument updateAttr = new UpdateAttributeInMESDocument();
	@Before
	public void setUp() throws Exception {
		double begin=HazyUtil.getTimeHelper().getBeginTime();
		HazyUtil.getLogHelper().initLogger();
		session =HazyUtil.getLinuxUtil().getLocalSession();
		logger.debug("[Set UP]" + HazyUtil.getTimeHelper().getTotalTimeSecond(begin) + "s");

	}
	
	//@Test
	public void testDescValid() throws APIException, HazyException, SQLException {
		IChange change = HazyUtil.getAgileAPIHelper().loadChange(session, "MIS0000001");//
	
		double begin=HazyUtil.getTimeHelper().getBeginTime();
		
		System.out.println("Change:" + change);
		System.out.println(this.updateAttr.doAction(session, null,change));
		logger.debug("[DescValid]" + HazyUtil.getTimeHelper().getTotalTimeSecond(begin) + "s");
	}
	
	@Test
	public void testDescValidDB() throws APIException, HazyException, SQLException {
		IItem part = HazyUtil.getAgileAPIHelper().loadItem(session, "MES0000003");//
		double begin=HazyUtil.getTimeHelper().getBeginTime();
		
		System.out.println("Item:" + part);
		this.updateAttr.updateAttribute(part);
		logger.debug("[DescValid]" + HazyUtil.getTimeHelper().getTotalTimeSecond(begin) + "s");
	}
}
