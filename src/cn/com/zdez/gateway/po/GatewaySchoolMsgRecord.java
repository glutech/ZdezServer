package cn.com.zdez.gateway.po;

import java.sql.Timestamp;

/**
 * 通过远程接口发布的校园通知记录
 * 
 * @author wu.kui2@gmail.com
 */
public class GatewaySchoolMsgRecord {

	private int recordId;
	private Integer authIdNullabled;
	private int schoolMsgId;
	private String postIp;
	private Timestamp postTime;

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public Integer getAuthIdNullabled() {
		return authIdNullabled;
	}

	public void setAuthIdNullabled(Integer authIdNullabled) {
		this.authIdNullabled = authIdNullabled;
	}

	public int getSchoolMsgId() {
		return schoolMsgId;
	}

	public void setSchoolMsgId(int schoolMsgId) {
		this.schoolMsgId = schoolMsgId;
	}

	public String getPostIp() {
		return postIp;
	}

	public void setPostIp(String postIp) {
		this.postIp = postIp;
	}

	public Timestamp getPostTime() {
		return postTime;
	}

	public void setPostTime(Timestamp postTime) {
		this.postTime = postTime;
	}

}
