package cn.com.zdez.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ContentOperation {

	/**
	 * 把news、zdezMsg、schoolMsg中的content存为单独的HTML文件，便于在网页端显示
	 * @param msgType
	 * @param msgId
	 * @param content
	 * @param rootPath
	 * @return
	 */
	public boolean SaveContent(String msgType, int msgId, String content,
			String rootPath) {
		boolean flag = false;
		String fileName = msgType + Integer.toString(msgId) + ".html";
		String pathName = rootPath + "html/" + fileName;

		String msgContent = "<html><head></head><body>" + content + "</body></html>";
		File f = new File(pathName);

		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f);
			byte[] b = msgContent.getBytes("utf-8");
			fos.write(b);
			fos.flush();
			fos.close();
			flag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

}
