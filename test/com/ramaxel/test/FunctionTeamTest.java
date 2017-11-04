package com.ramaxel.test;

import java.util.HashMap;
import java.util.Iterator;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IDataObject;
import com.agile.api.IRow;
import com.agile.api.ITable;
import com.agile.api.IUserGroup;
import com.agile.api.UserConstants;
import com.agile.api.UserGroupConstants;
import com.hazy.agile.util.AgileLoginHelper;

public class FunctionTeamTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		try {
			IAgileSession session = AgileLoginHelper.getSession();
			HashMap map = new HashMap();

			// map.put(UserGroupConstants.ATT_GENERAL_INFO_DESCRIPTION, "QA");

			IDataObject usr0 = (IDataObject) session.getObject(UserConstants.CLASS_USER, "plm01");

			IDataObject FtObj = (IDataObject) (session.getObject(IUserGroup.OBJECT_TYPE, "BCSD17M0013"));
			System.out.println(FtObj);
			ITable jobFuncTbl = FtObj.getTable("JobFunctions");

			map.put(UserGroupConstants.ATT_JOB_FUNCTION_USERS_USERGROUPS, new Object[] { usr0 });
			map.put(UserGroupConstants.ATT_JOB_FUNCTION_NAME, "QA");
			Iterator it = jobFuncTbl.iterator();
			while (it.hasNext()) {
				IRow row = (IRow) it.next();
				System.out.println(":" + row.getValue(UserGroupConstants.ATT_JOB_FUNCTION_DESC));
			}
			IRow ft_jf_row = jobFuncTbl.createRow(map);

		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
