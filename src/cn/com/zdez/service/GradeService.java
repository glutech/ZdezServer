package cn.com.zdez.service;

import java.util.List;

import cn.com.zdez.dao.GradeDao;
import cn.com.zdez.po.Grade;

public class GradeService {
	
	GradeDao dao = new GradeDao();
	
	public Grade getGradeById(int gradeId) {
		return dao.getGradeById(gradeId);
	}
	
	public List<Grade> getAll() {
		return dao.getAll();
	}

}
