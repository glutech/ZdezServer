package cn.com.zdez.po;

import java.io.Serializable;
import java.sql.Time;

public class Student implements Serializable{
	private int id;
	private String username;
	private String password;
	private String name;
	private String gender;
	private Time admissionTime;
	private int status;
	private String staus;
	private int isTeacher;
	private int gradeId;
	private int majorId;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Time getAdmissionTime() {
		return admissionTime;
	}
	public void setAdmissionTime(Time admissionTime) {
		this.admissionTime = admissionTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStaus() {
		return staus;
	}
	public void setStaus(String staus) {
		this.staus = staus;
	}
	public int getIsTeacher() {
		return isTeacher;
	}
	public void setIsTeacher(int isTeacher) {
		this.isTeacher = isTeacher;
	}
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	public int getMajorId() {
		return majorId;
	}
	public void setMajorId(int majorId) {
		this.majorId = majorId;
	}

}
