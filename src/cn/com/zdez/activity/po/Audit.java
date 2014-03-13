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
	private Integer noteId;
	private Integer cmtId;
	private Integer usrId;
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

	public Integer getNoteId() {
		return noteId;
	}

	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}

	public Integer getCmtId() {
		return cmtId;
	}

	public void setCmtId(Integer cmtId) {
		this.cmtId = cmtId;
	}

	public Integer getUsrId() {
		return usrId;
	}

	public void setUsrId(Integer usrId) {
		this.usrId = usrId;
	}

	public AdtStateEnum getAdtState() {
		return adtState;
	}

	public void setAdtState(AdtStateEnum adtState) {
		this.adtState = adtState;
	}

}
