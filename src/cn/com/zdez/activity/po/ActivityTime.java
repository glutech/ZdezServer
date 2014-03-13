package cn.com.zdez.activity.po;

import java.sql.Date;

public class ActivityTime {
	public static enum ActTimeTypeEnum {
		shorttime, longtime
	}

	private int actTimeId;
	private Date actTimeBegin;
	private Date actTimeEndNullabled;
	private ActTimeTypeEnum actTimeType;

	public int getActTimeId() {
		return actTimeId;
	}

	public void setActTimeId(int actTimeId) {
		this.actTimeId = actTimeId;
	}

	public Date getActTimeBegin() {
		return actTimeBegin;
	}

	public void setActTimeBegin(Date actTimeBegin) {
		this.actTimeBegin = actTimeBegin;
	}

	public Date getActTimeEndNullabled() {
		return actTimeEndNullabled;
	}

	public void setActTimeEndNullabled(Date actTimeEndNullabled) {
		this.actTimeEndNullabled = actTimeEndNullabled;
	}

	public ActTimeTypeEnum getActTimeTypeEnum() {
		return actTimeType;
	}

	public void setActTimeTypeEnum(ActTimeTypeEnum actTimeType) {
		this.actTimeType = actTimeType;
	}

}
