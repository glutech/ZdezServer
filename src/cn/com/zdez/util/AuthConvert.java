package cn.com.zdez.util;

public class AuthConvert {
	
	public String authConvert(int i) {
		String temp = "";
		if(i == 1) {
			temp="学校级";
		}else if(i == 2) {
			temp="学院级";
		}else if(i == 3) {
			temp="专业级";
		}else {
			temp="转换错误";
		}
		return temp;
	}

}
