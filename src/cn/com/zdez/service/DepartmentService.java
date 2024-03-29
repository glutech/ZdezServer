package cn.com.zdez.service;

import java.util.List;

import cn.com.zdez.dao.DepartmentDao;
import cn.com.zdez.po.Department;

public class DepartmentService {

	DepartmentDao dao = new DepartmentDao();

	/**
	 * 根据学校id获取属于该学校的所有学院
	 * 
	 * @param schoolId
	 * @return list of departments
	 */
	public List<Department> getDepartmentsList(int schoolId) {
		return dao.getDepartmentsList(schoolId);
	}

	/**
	 * 根据学院id获取该学院的详细信息
	 * 
	 * @param departmentId
	 * @return department entity
	 */
	public Department getDepartmentById(int departmentId) {
		return dao.getDepartmentById(departmentId);
	}

	/**
	 * 根据学院id获取学院所属的学校id
	 * 
	 * @param departmentId
	 * @return
	 */
	public int getSchoolIdByDepartmentId(int departmentId) {
		return dao.getSchoolIdByDepartmentId(departmentId);
	}

	/**
	 * 获取所有学院
	 * 
	 * @return
	 */
	public List<Department> getAll() {
		return dao.getAll();
	}
	
	public List<String> getNameById(String[] department) {
		return dao.getNameById(department);
	}

}
