package cn.com.zdez.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	/**
	 * 特别注意：这并不是标准MD5算法 <br />
	 * 由于历史原因，数据库的密码字段已使用此错误算法进行加密 <br />
	 * 而此算法无法反向得到原始密码，后续逻辑都只能使用此算法了 <br />
	 * 这意味着，外部不应该考虑提交标准MD5处理后的密码，仅能提交原始密码来使用此错误密码进行处理 <br />
	 * @author wu.kui2@gmail.com
	 */
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
