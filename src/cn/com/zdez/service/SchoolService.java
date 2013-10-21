package cn.com.zdez.service;

import java.util.HashMap;
import java.util.List;

import cn.com.zdez.dao.SchoolDao;
import cn.com.zdez.po.School;
import cn.com.zdez.po.SchoolSys;
import cn.com.zdez.po.Student;

public class SchoolService {

	SchoolDao dao = new SchoolDao();

	/**
	 * 通过schoolId获取学校名
	 * 
	 * @param schoolId
	 * @return
	 */
	public String getSchoolNameById(int schoolId) {
		return dao.getSchoolNameById(schoolId);
	}

	/**
	 * 获取所有学校
	 * 
	 * @return
	 */
	public List<School> getAll() {
		return dao.getAll();
	}

	/**
	 * 获取某一学生的所属学校
	 * 
	 * @param stu
	 * @return
	 */
	public int getSchoolIdByStu(Student stu) {
		return dao.getSchoolIdByStu(stu);
	}

	/**
	 * 获取某一学生的所属学院
	 * 
	 * @param stu
	 * @return
	 */
	public int getDepartmentIdByStu(Student stu) {
		return dao.getDepartmentIdByStu(stu);
	}

	/**
	 * 获取所有学业层次，用于zdezMsg的发送
	 * 
	 * @return
	 */
	public List<SchoolSys> getSchoolSysAll() {
		return dao.getSchoolSysAll();
	}

	/**
	 * 获取某些学校包含的所有的学业层次
	 * 
	 * @param schoolIds
	 * @return
	 */
	public List<SchoolSys> getSchoolSysListBySchoolIds(String[] schoolIds) {
		return dao.getSchoolSysListBySchoolIds(schoolIds);
	}

	/**
	 * 根据schoolId获取学校名
	 * 
	 * @param schoolIdList
	 * @return
	 */
	public List<String> getSchoolNameById(List<Integer> schoolIdList) {
		return dao.getSchoolNameById(schoolIdList);
	}

}
