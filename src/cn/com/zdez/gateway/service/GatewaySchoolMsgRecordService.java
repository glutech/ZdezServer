package cn.com.zdez.gateway.service;

import cn.com.zdez.gateway.dao.GatewaySchoolMsgRecordDao;
import cn.com.zdez.gateway.po.GatewaySchoolMsgRecord;

public class GatewaySchoolMsgRecordService {

	private GatewaySchoolMsgRecordDao recordDao = new GatewaySchoolMsgRecordDao();

	public boolean insertRecord(GatewaySchoolMsgRecord record) {
		return recordDao.insertRecord(record);
	}
}
