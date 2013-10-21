package cn.com.zdez.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	public String toMD5String(String str) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] md5Bytes = md.digest(str.getBytes());
			for (int i = 0; i < md5Bytes.length; i++) {
				sb.append(Integer.toHexString(md5Bytes[i]&0xff));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
