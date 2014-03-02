package cn.com.zdez.gateway.servlet;

public class Util {

	private Util() {
	}

	public static boolean isStringNullOrEmpty(String s) {
		return s == null || s.equals("");
	}

	public static boolean isStringEquals(String s1, String s2) {
		return s1 == null ? false : s1.equals(s2);
	}

	public static boolean isStringLengthEqual(String s, int length) {
		return s.length() == length;
	}

	public static boolean isStringLengthRange(String s, int min, int max) {
		int length = s.length();
		return length >= min && length <= max;
	}

}
