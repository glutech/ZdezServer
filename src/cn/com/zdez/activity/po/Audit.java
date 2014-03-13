package cn.com.zdez.activity.po;

import java.sql.Date;

public class Audit {
	public static enum AdtStateEnum {
		pass, nopass
	}

	private int adtId;
	private Date adtTime;
	private String adtContent;
	private int adtoId;
	private int noteId;
	private int cmtId;
	private int usrId;
	private AdtStateEnum adtState;

	public int getAdtId() {
		return adtId;
	}

	public void setAdtId(int adtId) {
		this.adtId = adtId;
	}

	public Date getAdtTime() {
		return adtTime;
	}

	public void setAdtTime(Date adtTime) {
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

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public int getCmtId() {
		return cmtId;
	}

	public void setCmtId(int cmtId) {
		this.cmtId = cmtId;
	}

	public int getUsrId() {
		return usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	public AdtStateEnum getAdtState() {
		return adtState;
	}

	public void setAdtState(AdtStateEnum adtState) {
		this.adtState = adtState;
	}

}
