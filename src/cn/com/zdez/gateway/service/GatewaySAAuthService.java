package cn.com.zdez.gateway.service;

import java.sql.Timestamp;
import java.util.UUID;

import cn.com.zdez.gateway.dao.GatewaySAAuthDao;
import cn.com.zdez.gateway.po.GatewaySAAuth;
import cn.com.zdez.po.SchoolAdmin;

public class GatewaySAAuthService {

	private GatewaySAAuthDao authDao = new GatewaySAAuthDao();

	public GatewaySAAuth getAuthBySchoolUsername(String username) {
		return authDao.getAuthByAdminUsername(username);
	}

	public GatewaySAAuth getAuthByAuthToken(String authToken) {
		return authDao.getAuthByAuthToken(authToken);
	}

	/**
	 * 生成32位长的UUID <br />
	 * 示例：550E8400E29B11D4A716446655440000
	 */
	private String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public GatewaySAAuth createAuth(SchoolAdmin admin, String ip) {
		String token = uuid();
		GatewaySAAuth auth = new GatewaySAAuth();
		auth.setAuthToken(token);
		auth.setCreateIp(ip);
		auth.setCreateTime(new Timestamp(System.currentTimeMillis()));
		auth.setSchoolAdminUsername(admin.getUsername());
		if (authDao.insertAuth(auth)) {
			return authDao.getAuthByAuthId(authDao.getLastInsertAuthId());
		} else {
			return null;
		}
	}

	public boolean clearAuth(GatewaySAAuth auth) {
		return authDao.deleteAuth(auth);
	}
}
