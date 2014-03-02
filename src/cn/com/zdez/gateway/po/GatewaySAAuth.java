package cn.com.zdez.gateway.po;

import java.sql.Timestamp;

/**
 * 远程接口授权
 * 
 * @author wu.kui2@gmail.com
 */
public class GatewaySAAuth {

	private int authId;
	private String schoolAdminUsername;
	private String authToken;
	private String createIp;
	private Timestamp createTime;

	public int getAuthId() {
		return authId;
	}

	public void setAuthId(int authId) {
		this.authId = authId;
	}

	public String getSchoolAdminUsername() {
		return schoolAdminUsername;
	}

	public void setSchoolAdminUsername(String schoolAdminUsername) {
		this.schoolAdminUsername = schoolAdminUsername;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
