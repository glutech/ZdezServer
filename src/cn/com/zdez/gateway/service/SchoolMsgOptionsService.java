package cn.com.zdez.gateway.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.zdez.po.Department;
import cn.com.zdez.po.Grade;
import cn.com.zdez.po.Major;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolSys;
import cn.com.zdez.po.Student;
import cn.com.zdez.service.SchoolAdminService;

public class SchoolMsgOptionsService {

	/*
	 * 参考通知发送页逻辑
	 * 
	 * @see cn.com.zdez.servlet.School_NewMsgHref
	 */

	private SchoolAdminService service = new SchoolAdminService();

	/**
	 * 读取所有可能的选项
	 * 
	 * @return <pre>
	 *  		{ 
	 * 		grades : [
	 * 			{ id : <int>, name : <string> }, ...
	 * 		],
	 * 		degrees : [
	 * 			{ id : <int>, name : <string> }, ...
	 * 		],
	 * 		departments : [
	 * 			{ id : <int>, name : <string> }, ....
	 * 		],
	 * 		majors : [
	 * 			{ id : <int>, name : <string>, department_id : <int>, degree_id : <int> }, ...
	 * 		],
	 * 		cc : {
	 * 			sto : [
	 * 				{ id : <int>, name : <string> }, .......
	 * 			],
	 * 			reo : [
	 * 				{ id : <int>, name : <string> }, .......
	 * 			],
	 * 			ylc : [
	 * 				{ id : <int>, name : <string> }, .......
	 * 			],
	 * 			so : [
	 * 				{ id : <int>, name : <string> }, .......
	 * 			],
	 * 		}
	 * }
	 * </pre>
	 */
	public Map<String, Object> getOptionsByAdmin(SchoolAdmin admin) {
		// 获取可用毕业年份
		List<Grade> grades = service.getGradeList();
		// 获取学业层次
		List<SchoolSys> degrees = service.getSchoolSysList(admin);
		// 获取学院列表
		List<Department> departments = service.getDepartmentList(admin);
		// 获取专业列表
		List<Major> majors = service.getMajorList(admin);
		// 获取用于抄送的学生处老师列表
		List<Student> ccSTOTeachers = service.getDptTeacherList(admin,
				"stuAffairsId");
		// 获取用于抄送的就业处老师列表
		List<Student> ccREOTeachers = service.getDptTeacherList(admin,
				"employmentId");
		// 获取用于抄送的团委老师列表
		List<Student> ccYLCTeachers = service.getDptTeacherList(admin,
				"youthCorpsCommitteeId");
		// 获取保卫处老师列表
		List<Student> ccSOTeachers = service.getDptTeacherList(admin,
				"securityDeptId");
		// 拼接结果集
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("grades", entityToList(grades, new IEntityToMap<Grade>() {
			@Override
			public void merge(Map<String, Object> map, Grade grade) {
				map.put("id", grade.getId());
				map.put("name", grade.getDescription());
			}
		}, 2));
		result.put("degrees",
				entityToList(degrees, new IEntityToMap<SchoolSys>() {
					@Override
					public void merge(Map<String, Object> map, SchoolSys degree) {
						map.put("id", degree.getId());
						map.put("name", degree.getSysName());
					}
				}, 2));
		result.put("departments",
				entityToList(departments, new IEntityToMap<Department>() {
					@Override
					public void merge(Map<String, Object> map,
							Department department) {
						map.put("id", department.getId());
						map.put("name", department.getName());
					}
				}, 2));
		result.put("majors", entityToList(majors, new IEntityToMap<Major>() {
			@Override
			public void merge(Map<String, Object> map, Major major) {
				map.put("id", major.getId());
				map.put("name", major.getName());
				map.put("department_id", major.getDepartmentId());
				map.put("degree_id", major.getSchoolSysId());
			}
		}, 4));
		Map<String, Object> ccResult = new LinkedHashMap<String, Object>(4);
		result.put("cc", ccResult);
		IEntityToMap<Student> ccSetter = new IEntityToMap<Student>() {
			@Override
			public void merge(Map<String, Object> map, Student teacher) {
				map.put("id", teacher.getId());
				map.put("name", teacher.getName());
			}
		};
		ccResult.put("sto", entityToList(ccSTOTeachers, ccSetter, 2));
		ccResult.put("reo", entityToList(ccREOTeachers, ccSetter, 2));
		ccResult.put("ylc", entityToList(ccYLCTeachers, ccSetter, 2));
		ccResult.put("so", entityToList(ccSOTeachers, ccSetter, 2));
		return result;
	}

	private <T> List<Map<String, Object>> entityToList(List<T> entities,
			IEntityToMap<T> setter, int mapCapacity) {
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>(
				entities.size());
		for (T entity : entities) {
			Map<String, Object> tmpMap = new LinkedHashMap<String, Object>(
					mapCapacity);
			setter.merge(tmpMap, entity);
			tmpList.add(tmpMap);
		}
		return tmpList;
	}

	private static interface IEntityToMap<T> {
		public void merge(Map<String, Object> map, T t);
	}

	public boolean validateOptions(SchoolAdmin admin, Set<Integer> grades,
			Set<Integer> departments, Set<Integer> majors, Set<Integer> cc,
			String[] msg) {
		// 比较毕业年份
		if (!grades.isEmpty()
				&& !verifyIdsInEntities(service.getGradeList(), grades,
						new IEntityPKGetter<Grade>() {
							@Override
							public int getPk(Grade t) {
								return t.getId();
							}
						})) {
			msg[0] = "毕业年份列表存在非法值";
			return false;
		}
		// 比较学院列表
		if (!departments.isEmpty()
				&& !verifyIdsInEntities(service.getDepartmentList(admin),
						departments, new IEntityPKGetter<Department>() {
							@Override
							public int getPk(Department t) {
								return t.getId();
							}
						})) {
			msg[0] = "学院列表存在非法值";
			return false;
		}
		// 比较专业列表
		if (!majors.isEmpty()
				&& !verifyIdsInEntities(service.getMajorList(admin), majors,
						new IEntityPKGetter<Major>() {
							@Override
							public int getPk(Major t) {
								return t.getId();
							}
						})) {
			msg[0] = "专业列表存在非法值";
			return false;
		}
		// 比较抄送列表
		if (!cc.isEmpty()) {
			List<Student> teachers = new LinkedList<Student>();
			teachers.addAll(service.getDptTeacherList(admin, "stuAffairsId"));
			teachers.addAll(service.getDptTeacherList(admin, "employmentId"));
			teachers.addAll(service.getDptTeacherList(admin,
					"youthCorpsCommitteeId"));
			teachers.addAll(service.getDptTeacherList(admin, "securityDeptId"));
			if (!verifyIdsInEntities(teachers, cc,
					new IEntityPKGetter<Student>() {
						@Override
						public int getPk(Student t) {
							return t.getId();
						}
					})) {
				msg[0] = "抄送列表存在非法值";
				return false;
			}
		}
		return true;
	}

	public boolean validateDepartmentAndMajorRelationship(SchoolAdmin admin,
			Set<Integer> departments, Set<Integer> majors, String[] msg) {
		if (!departments.isEmpty() && !majors.isEmpty()) {
			Map<Integer, Integer> deptRefNumMap = new HashMap<Integer, Integer>();
			for (int deptId : departments) {
				deptRefNumMap.put(deptId, 0);
			}
			Map<Integer, Major> majorIndexMap = new HashMap<Integer, Major>();
			for (Major major : service.getMajorList(admin)) {
				majorIndexMap.put(major.getId(), major);
			}
			for (int majorId : majors) {
				int refDeptId = majorIndexMap.get(majorId).getDepartmentId();
				if (!deptRefNumMap.containsKey(refDeptId)) {
					msg[0] = "专业ID错误，存在未指定学院的专业";
					return false;
				}
				deptRefNumMap.put(refDeptId, deptRefNumMap.get(refDeptId) + 1);
			}
			if (deptRefNumMap.containsValue(0)) {
				msg[0] = "学院ID错误，存在未指定专业的学院";
				return false;
			}
		}
		return true;
	}

	private <T> Boolean verifyIdsInEntities(List<T> list, Set<Integer> ids,
			IEntityPKGetter<T> getter) {
		Set<Integer> listSet = new HashSet<Integer>(list.size());
		for (T t : list) {
			listSet.add(getter.getPk(t));
		}
		Set<Integer> compareSet = new HashSet<Integer>(ids);
		compareSet.removeAll(listSet);
		return compareSet.size() == 0;
	}

	private static interface IEntityPKGetter<T> {
		public int getPk(T t);
	}
}
