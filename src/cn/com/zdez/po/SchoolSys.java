package cn.com.zdez.po;

import java.io.Serializable;

public class SchoolSys implements Serializable{
	private int id;
	private String sysName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

}
