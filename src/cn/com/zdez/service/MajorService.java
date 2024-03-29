package cn.com.zdez.service;

import java.util.List;

import cn.com.zdez.dao.MajorDao;
import cn.com.zdez.po.Major;

public class MajorService {

	MajorDao dao = new MajorDao();

	/**
	 * 根据学院id获得该学院下所有专业
	 * 
	 * @param departmentId
	 * @return list of majors
	 */
	public List<Major> getMajorList(int departmentId) {
		return dao.getMajorList(departmentId);
	}

	/**
	 * 根据专业id获得该专业详细信息
	 * 
	 * @param majorId
	 * @return major entity
	 */
	public Major getMajorById(int majorId) {
		return dao.getMajorById(majorId);
	}

	/**
	 * 获取某一专业所属的学院
	 * 
	 * @param majorId
	 * @return
	 */
	public int getDepartmentIdByMajorId(int majorId) {
		return dao.getDepartmentIdByMajorId(majorId);
	}

	/**
	 * 获取所有学院
	 * 
	 * @return
	 */
	public List<Major> getAll() {
		return dao.getAll();
	}
	
	public List<String> getNameById(String[] major) {
		return dao.getNameById(major);
	}

}
