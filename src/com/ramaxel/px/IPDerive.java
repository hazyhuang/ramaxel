package com.ramaxel.px;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IDataObject;
import com.agile.api.IItem;
import com.agile.api.INode;
import com.agile.api.IQuery;
import com.agile.api.ITable;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;

public class IPDerive implements ICustomAction {
	static Logger logger = Logger.getLogger(IPDerive.class);
	public static String CLASS_APINAME = "IP";

	// 查询以编号为XXXXXXXX开头的IP，如果只有1个，则以另存为的方式创建XXXXXXXX-001，
	// 如果大于1个，则对除派生源IP的其它派生IP进行排序（以派生序号），
	// 以最后一个派生IP的序号+1进行创建新的派生IP。
	// 例如最后一个派生IP的序号为003，则新的派生IP编号为XXXXXXXX-004

	@Override
	public ActionResult doAction(IAgileSession session, INode arg1, IDataObject item) {
		String newStringName = null;

		try {
			String itemName = item.getName();
			int length2 = itemName.length();
			char aaa = itemName.charAt(length2 - 4);
			logger.debug("[-]:" + aaa);
			if ('-' == aaa) {
				itemName = itemName.substring(0, length2 - 4);
			}
			ITable table = search(session, itemName);
			int size = table.size();
			if (1 == size) {
				IItem newItem = (IItem) item.saveAs(CLASS_APINAME, itemName + "-001");
				return new ActionResult(ActionResult.STRING, "创建成功：" + newItem.getName());
			} else {
				Integer last = getMaxSeq(itemName, table);
				Integer xx = 1000 + last + 1;
				String xxStr = xx.toString();
				int le = xxStr.length();
				String newSeq = xxStr.substring(le - 3, le);
				IItem newItem = (IItem) item.saveAs(CLASS_APINAME, itemName + "-" + newSeq);
				newStringName = newItem.getName();
				return new ActionResult(ActionResult.STRING, "创建成功：" + newStringName);
			}

		} catch (APIException e) {
			e.printStackTrace();
			return new ActionResult(ActionResult.EXCEPTION, e);
		}

	}

	/**
	 * 获取尾号序列号的最大值
	 * 
	 * @param itemName
	 * @param table
	 * @return
	 * @throws APIException
	 */
	private Integer getMaxSeq(String itemName, ITable table) throws APIException {
		@SuppressWarnings("unchecked")
		Iterator<IItem> iter = table.getReferentIterator();
		Integer last = 0;
		while (iter.hasNext()) {
			IItem item2 = (IItem) iter.next();
			String itemName2 = item2.getName();
			if (!itemName.equals(itemName2)) {

				int length = itemName2.length();
				String str = itemName2.substring(length - 3, length);
				Integer seq = Integer.valueOf(str);
				if (seq > last) {
					last = seq;
				}
			}

		}
		return last;
	}

	private ITable search(IAgileSession session, String itemName) throws APIException {

		IQuery query = (IQuery) session.createObject(IQuery.OBJECT_TYPE, CLASS_APINAME);
		query.setCaseSensitive(false);
		query.setCriteria("[1001] starts with '" + itemName + "'");
		ITable results = query.execute();
		return results;

	}

}
