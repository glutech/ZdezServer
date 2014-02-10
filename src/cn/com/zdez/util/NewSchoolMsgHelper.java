package cn.com.zdez.util;

public class NewSchoolMsgHelper {

	public String[] CCObjectsGenerator(String[] sa1, String[] sa2,
			String[] sa3, String[] sa4, String[] sa5) {
		if (sa1 != null) {
			sa1 = together(sa1, sa2, sa3, sa4, sa5);
		} else {
			sa1 = initialTeachers(sa2, sa3, sa4, sa5);
			sa1 = together(sa1, sa2, sa3, sa4, sa5);
		}
		return sa1;
	}

	// 用于拼接String[]
	private String[] concat(String[] a, String[] b) {
		String[] c = new String[a.length + b.length];

		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);

		return c;
	}

	// 当teachers为null是初始化teachers
	private String[] initialTeachers(String[] a, String[] b, String[] c,
			String[] d) {

		String[] result = null;

		int i = -1;
		if (a != null) {
			i = 0;
		} else if (b != null) {
			i = 1;
		} else if (c != null) {
			i = 2;
		} else if (d != null) {
			i = 3;
		}

		switch (i) {
		case 0:
			result = a;
			break;
		case 1:
			result = b;
			break;
		case 2:
			result = c;
			break;
		case 3:
			result = d;
			break;
		default:
			break;
		}

		return result;
	}

	// 拼接teachers, stuAffairsTeachers, empTeachers, yccTeachers, sdTeachers
	private String[] together(String[] sa1, String[] sa2, String[] sa3,
			String[] sa4, String[] sa5) {
		if (sa2 != null) {
			if (!isEqual(sa1, sa2)) {
				sa1 = concat(sa1, sa2);
			}
		}
		if (sa3 != null) {
			if (!isEqual(sa1, sa3)) {
				sa1 = concat(sa1, sa3);
			}
		}
		if (sa4 != null) {
			if (!isEqual(sa1, sa4)) {
				sa1 = concat(sa1, sa4);
			}
		}
		if (sa5 != null) {
			if (!isEqual(sa1, sa5)) {
				sa1 = concat(sa1, sa5);
			}
		}

		return sa1;
	}

	// 比较两个数组里的元素是否都相等
	private boolean isEqual(String[] a, String[] b) {

		boolean flag = false;

		if (a.length != b.length) {
			flag = false;
		} else {
			for (int i = 0; i < a.length; i++) {
				if (a[i].equals(b[i])) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
		}

		return flag;
	}
}
