package cn.com.zdez.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.zdez.cache.UserCache;
import cn.com.zdez.dao.SchoolAdminDao;
import cn.com.zdez.po.Department;
import cn.com.zdez.po.Grade;
import cn.com.zdez.po.Major;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolSys;
import cn.com.zdez.po.Student;
import cn.com.zdez.util.GetSchoolSys;
import cn.com.zdez.util.ParseXmlService;
import cn.com.zdez.vo.SchoolAdminVo;

public class SchoolAdminService {
	SchoolAdminDao dao = new SchoolAdminDao();

	/**
	 * 通过用户名获取用户详细信息（从缓存中取）
	 * 
	 * @param username
	 * @return schoolAdmin entity
	 */
	public SchoolAdmin getSchoolAdminInfo(String username) {
//		return dao.getSchoolAdminInfo(username);
		return new UserCache().getInfoFromCache(username);
	}

	/**
	 * 通过用户名获取用户详细信息（从数据库中取）
	 * 
	 * @param username
	 * @return schoolAdmin entity
	 */
	public SchoolAdmin getSchoolAdminInfoFromMySQL(String username) {
		return dao.getSchoolAdminInfo(username);
//		return new UserCache().getInfoFromCache(username);
	}

	/**
	 * 学校管理员用户修改密码
	 * 
	 * @param sAdmin
	 * @return true or false
	 */
	public boolean modifyPassword(SchoolAdmin sAdmin) {
		return dao.modifyPassword(sAdmin);
	}

	/**
	 * 根据学校管理员的权限等信息获得该管理员账户下对应的所有学院
	 * 
	 * @param sAdmin
	 * @return list of departments
	 */
	public List<Department> getDepartmentList(SchoolAdmin sAdmin) {
		List<Department> list = new ArrayList<Department>();
		DepartmentService departService = new DepartmentService();
		int auth = sAdmin.getAuth();
		if (auth == 1) {
			// 学校管理员权限为1级（学校级）权限时，可向全学校内的学院发送通知
			list = departService.getDepartmentsList(sAdmin.getSchoolId());
		} else if (auth == 2 || auth == 3) {
			// 学校管理员权限为2级（学院级）或3级（专业级）权限时，只可向管理员所在学院发送通知
			Department depart = new Department();
			depart = departService.getDepartmentById(sAdmin.getDepartmentId());
			list.add(depart);
		} else {
			System.out
					.println("getDepartmentList Error in SchoolAdminService!");
		}
		return list;
	}

	/**
	 * 根据学校管理员权限等信息获取该管理员账户下对应的专业
	 * 
	 * @param sAdmin
	 * @return list of majors
	 */
	public List<Major> getMajorList(SchoolAdmin sAdmin) {
		List<Major> list = new ArrayList<Major>();
		MajorService mService = new MajorService();
		int auth = sAdmin.getAuth();
		if (auth == 1) {
			//学校管理员账户为1级（学校级）权限时，可向学校内所有专业发送通知
			DepartmentService dService = new DepartmentService();
			List<Department> dList = new ArrayList<Department>();
			dList = dService.getDepartmentsList(sAdmin.getSchoolId());
			for (int i = 0, count = dList.size(); i < count; i++) {
				List<Major> tempList = mService.getMajorList(dList.get(i)
						.getId());
				for (int q = 0, tempCount = tempList.size(); q < tempCount; q++) {
					list.add(tempList.get(q));
				}
			}
		} else if (auth == 2) {
			//学校管理员账户为2级（学院级）权限时，可以向学院内所有专业发送通知
			list = mService.getMajorList(sAdmin.getDepartmentId());
		} else if (auth == 3) {
			//学校管理员账户为3级（专业级）权限时，只可向本专业一个专业发送通知
			Major major = new Major();
			major = mService.getMajorById(sAdmin.getMajorId());
			list.add(major);
		} else {
			System.out.println("getMajorList Error in SchoolAdminService!");
		}
		return list;
	}

	/**
	 * 根据学校管理员账户的权限等具体信息获得对应的教师列表
	 * 以便学校管理员在发送校园通知的时候可以选择是否发送给这些老师
	 * @param sAdmin
	 * @return list of teachers(student entity)
	 */
	public List<Student> getTeacherList(SchoolAdmin sAdmin) {
		return dao.getTeacherList(sAdmin);
	}
	
	/**
	 * 获取数据库中学校管理员的数量，用于分页
	 * @return
	 */
	public int getSchoolAdminCount() {
		return dao.getSchoolAdminCount();
	}
	
	/**
	 * 查询时用，获取某一个关键字下对应的学校帐号的数量，用于分页
	 * @param keyword
	 * @return
	 */
	public int getSchoolAdminCount(String keyword) {
		return dao.getSchoolAdminCount(keyword);
	}
	
	/**
	 * 根据页码获取对应的管理员信息
	 * @param start
	 * @param end
	 * @return
	 */
	public List<SchoolAdminVo> getSchoolAdminByPage(int start, int end) {
		return dao.getSchoolAdminByPage(start, end);
	}
	
	/**
	 * 按照分页和关键字查询要显示的学校管理员账户，用于搜索
	 * @param start
	 * @param end
	 * @param keyword
	 * @return
	 */
	public List<SchoolAdminVo> getSchoolAdminByPage(int start, int end, String keyword) {
		return dao.getSchoolAdminByPage(start, end, keyword);
	}
	
	/**
	 * 删除学校管理员帐号
	 * @param username
	 * @return
	 */
	public boolean deleteSchoolAdmin(String username) {
		return dao.deleteSchoolAdmin(username);
	}

	/**
	 * 根据学业层次id获取学业层次相关信息
	 * @param schoolSysIdList
	 * @return
	 */
	public List<SchoolSys> getSchoolSysList(SchoolAdmin sAdmin) {
		List<Integer> schoolSysIdList = new ArrayList<Integer>();
		schoolSysIdList = new GetSchoolSys().getSchoolSys(sAdmin);
		return dao.getSchoolSysList(schoolSysIdList);
	}
	
	/**
	 * 根据当前年份，获取毕业年度
	 * 返回当前年份再加之后4年
	 * @return
	 */
	public List<Grade> getGradeList() {
		return dao.getGradeList();
	}
	
	/**
	 * 根据用户名，获取所属学校id
	 * @param username
	 * @return
	 */
	public int  getSchoolIdByUsername(String username) {
		return dao.getSchoolIdByUsername(username);
	}
	
	/**
	 * 根据用户名，获取所属学院
	 * @param username
	 * @return
	 */
	public int  getDepartmentIdByUsername(String username) {
		return dao.getDepartmentIdByUsername(username);
	}
	
	/**
	 * 通过用户名，获取所属专业id
	 * @param username
	 * @return
	 */
	public int  getMajorIdByUsername(String username) {
		return dao.getMajorIdByUsername(username);
	}
	
	/**
	 * 新增学校管理员
	 * @param sAdmin
	 * @return
	 */
	public boolean newSchoolAdmin(SchoolAdmin sAdmin) {
		return dao.newSchoolAdmin(sAdmin);
	}

	public HashMap<Integer,List<Student>> getDepartmentStudentByAuth(SchoolAdmin sAdmin){
		return dao.getDepartmentStudent(sAdmin);
	}

	/*
	 * 获取该schooladmin所在的学校的学生处、就业处、团委、保卫处老师列表
	 */
	public List<Student> getDptTeacherList(SchoolAdmin sAdmin, String teacherType) {
		List<Student> stuList = new ArrayList<Student>();
		
		int majorId = this.getStuAffairsId(sAdmin, teacherType);
		
		stuList = new StudentService().getTeacherByMajor(majorId);
		
		return stuList;
	}
	
	private int getStuAffairsId(SchoolAdmin sAdmin, String teacherType) {
		int i = 0;
		
		HashMap<String, String> temp = this.getCCDepartmentMap(sAdmin.getSchoolId());
		String majorIdStr = temp.get(teacherType);
		
		i = Integer.parseInt(majorIdStr);
		
		return i;
	}
	
	private HashMap<String, String> getCCDepartmentMap(int schoolId) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		// 通过xml和sAdmin.getSchoolId获得该学校的学生处Id
		InputStream is = this.getCCDepartmentXMLIS();
		ParseXmlService pxService = new ParseXmlService();
		HashMap<String, HashMap<String, String>> hashmap = new HashMap<String, HashMap<String,String>>();

		try {
			hashmap = pxService.parseCCDptXml(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 通过学生处Id（majorId）获取stuList
		result = hashmap.get(Integer.toString(schoolId));
		return result;
	}
	
	private InputStream getCCDepartmentXMLIS() {

		InputStream is = SchoolAdminService.class.getClassLoader().getResourceAsStream("CCDepartment.xml");

		return is;
	}
}
