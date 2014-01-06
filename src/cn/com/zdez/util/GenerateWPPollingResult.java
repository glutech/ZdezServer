package cn.com.zdez.util;

import java.util.List;

import cn.com.zdez.vo.SchoolMsgVo;

public class GenerateWPPollingResult {

	public String generateWPPollingResult(List<SchoolMsgVo> list) {
		String result = "";

		int count = list.size();
		int lastMsgId = 0;
		String lastMsgTitle = "";
		if (count > 0) {
			lastMsgId = list.get(list.size() - 1).getSchoolMsgId();
			lastMsgTitle = list.get(list.size() - 1).getTitle();
			lastMsgTitle = this.escape_xml_characters(lastMsgTitle);
		}

		result = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<NoticePool>\n<ToReceiveNum>"
				+ count
				+ "</ToReceiveNum>\n<LastItemId>"
				+ lastMsgId
				+ "</LastItemId>\n<LastItemTitle>"
				+ lastMsgTitle
				+ "</LastItemTitle>\n</NoticePool>";
		
		System.out.println(result);

		return result;
	}
	
	/**
     * XML内容域的特殊字符转义
     */
    private String escape_xml_characters(String source) {
        // &    =>    &amp;
        // <    =>    &lt;
        // >    =>    &gt;
        // ‘    =>    &apos;
        // “    =>    &quot;
        char[] sourceChars = source.toCharArray();
        StringBuilder result = new StringBuilder(sourceChars.length);
        for(char c : sourceChars) {
            switch(c) {
                case '&' : result.append("&amp;");  break;
                case '<' : result.append("&lt;");   break;
                case '>' : result.append("&gt;");   break;
                case '\'': result.append("&apos;"); break;
                case '"' : result.append("&quot;"); break;
                default: result.append(c);
            }
        }
        return result.toString();
    }	

}
