package cn.com.zdez.activity.po;

import java.sql.Date;

public class Comment {
	public static enum CmtTypeEnum {
		ok, delete, cancle
	}

	private int cmtId;
	private Date cmtTime;
	private String cmtContent;
	private int cmtParentIdNullabled;
	private int actId;
	private int usrId;
	private CmtTypeEnum cmtType;

	public int getCmtId() {
		return cmtId;
	}

	public void setCmtId(int cmtId) {
		this.cmtId = cmtId;
	}

	public Date getCmtTime() {
		return cmtTime;
	}

	public void setCmtTime(Date cmtTime) {
		this.cmtTime = cmtTime;
	}

	public String getCmtContent() {
		return cmtContent;
	}

	public void setCmtContent(String cmtContent) {
		this.cmtContent = cmtContent;
	}

	public int getCmtParentIdNullabled() {
		return cmtParentIdNullabled;
	}

	public void setCmtParentIdNullabled(int cmtParentIdNullabled) {
		this.cmtParentIdNullabled = cmtParentIdNullabled;
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	public int getUsrId() {
		return usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	public CmtTypeEnum getCmtTypeEnum() {
		return cmtType;
	}

	public void setCmtTypeEnum(CmtTypeEnum cmtType) {
		this.cmtType = cmtType;
	}

}
