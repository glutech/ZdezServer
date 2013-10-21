package cn.com.zdez.util;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.po.SchoolAdmin;

public class GetSchoolSys {
	
	/**
	 * 如果学业层次有改变，这个地方也需要改
	 * @param sAdmin
	 * @return
	 */
	public List<Integer> getSchoolSys(SchoolAdmin sAdmin) {
		List<Integer> list = new ArrayList<Integer>();
		int schoolId = sAdmin.getSchoolId();
		if(schoolId == 1) {
			for(int i=1, count=5; i<count; i++) {
				list.add(i);
			}
		}else if(schoolId == 2) {
			for(int i=1, count=2; i<count; i++) {
				list.add(i);
			}
		}else if(schoolId == 3) {
			list.add(2);
		}else if(schoolId == 4) {
			list.add(1);
		}else if(schoolId == 5) {
			list.add(1);
			list.add(2);
			list.add(3);
		}else if(schoolId == 6) {
			list.add(1);
		}else {
			for(int i=1, count=5; i<count; i++) {
				list.add(i);
			}
		}
		return list;
	}

}
