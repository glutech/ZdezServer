package cn.com.zdez.gateway.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.gateway.po.GatewaySAAuth;
import cn.com.zdez.gateway.service.GatewaySAAuthService;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.service.SchoolAdminService;

@SuppressWarnings("serial")
public class SAAuthTokenServlet extends BaseServlet {

	private GatewaySAAuthService service = new GatewaySAAuthService();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 取得约定参数
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String api = request.getParameter("api");
		// 声明结果集并设置初始值
		int resultStatus = -999;
		String resultMsg = "未知错误，请稍后重试或联系我们";
		String resultToken = "";
		// 参数有效性检查和授权操作
		if (Util.isStringNullOrEmpty(username)
				|| Util.isStringNullOrEmpty(password)
				|| Util.isStringNullOrEmpty(api)
				|| !Util.isStringLengthRange(username, 1, 20)
				|| !Util.isStringLengthEqual(password, 32)) {
			// 参数格式错误
			resultStatus = 0;
			resultMsg = "参数格式错误";
		} else if (!Util.isStringEquals(api, "1")) {
			// API版本错误
			resultStatus = 0;
			resultMsg = "不支持的API版本";
		} else {
			SchoolAdmin dbSchoolAdmin = new SchoolAdminService()
					.getSchoolAdminInfoFromMySQL(username);
			if (!Util.isStringEquals(dbSchoolAdmin.getUsername(), username)
					|| !Util.isStringEquals(dbSchoolAdmin.getPassword(),
							password)) {
				// 账号密码错误
				resultStatus = 2;
				resultMsg = "账号密码不匹配";
			} else {
				GatewaySAAuth dbAuth = service.getAuthBySchoolUsername(username);
				if (dbAuth != null) {
					// 账号已绑定授权
					resultStatus = -1;
					resultMsg = "此账号已绑定了其它授权码，请先解除上一个授权码的绑定";
				} else {
					dbAuth = service.createAuth(dbSchoolAdmin,
							super.getIpAddress(request));
					if (dbAuth == null) {
						// 创建记录失败，内部错误
						resultStatus = 999;
						resultMsg = "内部错误，请稍后重试或联系我们";
					} else {
						// 绑定成功
						resultStatus = 1;
						resultMsg = "授权成功";
						resultToken = dbAuth.getAuthToken();
					}
				}
			}
		}
		// 构造约定结果集
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>(3);
		resultMap.put("status", resultStatus);
		resultMap.put("token", resultToken);
		resultMap.put("msg", resultMsg);
		// 输出响应
		super.outJson(resultMap, response);
	}

}
