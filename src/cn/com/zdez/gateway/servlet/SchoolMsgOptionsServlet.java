package cn.com.zdez.gateway.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.gateway.po.GatewaySAAuth;
import cn.com.zdez.gateway.service.GatewaySAAuthService;
import cn.com.zdez.gateway.service.SchoolMsgOptionsService;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.service.SchoolAdminService;

@SuppressWarnings("serial")
public class SchoolMsgOptionsServlet extends BaseServlet {

	private GatewaySAAuthService service = new GatewaySAAuthService();
	private SchoolMsgOptionsService optionsService = new SchoolMsgOptionsService();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		// 取得约定参数
		String token = request.getParameter("token");
		String api = request.getParameter("api");
		// 声明结果集并设置初始值
		int resultStatus = -999;
		Map<String, Object> resultOptions = new LinkedHashMap<String, Object>();
		String resultMsg = "未知错误，请稍后重试或联系我们";
		// 参数有效性检查和读取操作
		if (Util.isStringNullOrEmpty(token) || Util.isStringNullOrEmpty(api)
				|| !Util.isStringLengthEqual(token, 32)) {
			// 参数格式错误
			resultStatus = 0;
			resultMsg = "参数格式错误";
		} else if (!Util.isStringEquals(api, "1")) {
			// API版本错误
			resultStatus = 0;
			resultMsg = "不支持的API版本";
		} else {
			GatewaySAAuth dbAuth = service.getAuthByAuthToken(token);
			if (dbAuth == null) {
				// 不存在账号
				resultStatus = -1;
				resultMsg = "授权码无效";
			} else {
				SchoolAdmin dbAdmin = new SchoolAdminService()
						.getSchoolAdminInfoFromMySQL(dbAuth
								.getSchoolAdminUsername());
				resultOptions = optionsService.getOptionsByAdmin(dbAdmin);
				resultStatus = 1;
				resultMsg = "获取参数选项成功";
			}
		}
		// 构造约定结果集
		Map<String, Object> result = new LinkedHashMap<String, Object>(3);
		result.put("status", resultStatus);
		result.put("options", resultOptions);
		result.put("msg", resultMsg);
		// 输出响应
		super.outJson(result, response);
	}

}
