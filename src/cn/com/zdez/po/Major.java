package cn.com.zdez.po;

import java.io.Serializable;

public class Major implements Serializable{
	private int id;
	private String name;
	private int departmentId;
	private int schoolSysId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public int getSchoolSysId() {
		return schoolSysId;
	}
	public void setSchoolSysId(int schoolSysId) {
		this.schoolSysId = schoolSysId;
	}

}
