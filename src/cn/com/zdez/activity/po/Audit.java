package cn.com.zdez.activity.po;

import java.sql.Timestamp;

public class Audit {
	public static enum AdtStateEnum {
		pass, nopass
	}

	private int adtId;
	private Timestamp adtTime;
	private String adtContent;
	private int adtoId;
	private Integer noteIdNullabled;
	private Integer cmtIdNullabled;
	private Integer usrIdNullabled;
	private AdtStateEnum adtState;

	public int getAdtId() {
		return adtId;
	}

	public void setAdtId(int adtId) {
		this.adtId = adtId;
	}

	public Timestamp getAdtTime() {
		return adtTime;
	}

	public void setAdtTime(Timestamp adtTime) {
		this.adtTime = adtTime;
	}

	public String getAdtContent() {
		return adtContent;
	}

	public void setAdtContent(String adtContent) {
		this.adtContent = adtContent;
	}

	public int getAdtoId() {
		return adtoId;
	}

	public void setAdtoId(int adtoId) {
		this.adtoId = adtoId;
	}

	public Integer getNoteIdNullabled() {
		return noteIdNullabled;
	}

	public void setNoteIdNullabled(Integer noteIdNullabled) {
		this.noteIdNullabled = noteIdNullabled;
	}

	public Integer getCmtIdNullabled() {
		return cmtIdNullabled;
	}

	public void setCmtIdNullabled(Integer cmtIdNullabled) {
		this.cmtIdNullabled = cmtIdNullabled;
	}

	public Integer getUsrIdNullabled() {
		return usrIdNullabled;
	}

	public void setUsrIdNullabled(Integer usrIdNullabled) {
		this.usrIdNullabled = usrIdNullabled;
	}

	public AdtStateEnum getAdtState() {
		return adtState;
	}

	public void setAdtState(AdtStateEnum adtState) {
		this.adtState = adtState;
	}

}
