package cn.com.zdez.gateway.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cn.com.zdez.gateway.po.GatewaySAAuth;
import cn.com.zdez.gateway.po.GatewaySchoolMsgRecord;
import cn.com.zdez.gateway.service.GatewaySAAuthService;
import cn.com.zdez.gateway.service.GatewaySchoolMsgRecordService;
import cn.com.zdez.gateway.service.SchoolMsgOptionsService;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolMsg;
import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.service.SchoolMsgService;

@SuppressWarnings("serial")
public class PostSchoolMsgServlet extends BaseServlet {

	private GatewaySAAuthService service = new GatewaySAAuthService();
	private SchoolMsgOptionsService optionsService = new SchoolMsgOptionsService();
	private SchoolMsgService schoolMsgService = new SchoolMsgService();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 取得约定参数
		String token = request.getParameter("token");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String grades = request.getParameter("grades");
		String departments = request.getParameter("departments");
		String majors = request.getParameter("majors");
		String cc = request.getParameter("cc");
		String debug = request.getParameter("debug");
		String api = request.getParameter("api");
		// 需转换的约定参数
		Set<Integer> setGrade = null;
		Set<Integer> setDepartment = null;
		Set<Integer> setMajor = null;
		Set<Integer> setCC = null;
		Boolean bDebug = null;
		// 声明结果集并设置初始值
		int resultStatus = -999;
		String resultMsg = "未知错误，请稍后重试或联系我们";
		// 参数有效性检查和读取操作
		if (Util.isStringNullOrEmpty(token) || Util.isStringNullOrEmpty(title)
				|| Util.isStringNullOrEmpty(content)
				|| Util.isStringNullOrEmpty(grades)
				|| Util.isStringNullOrEmpty(departments)
				|| Util.isStringNullOrEmpty(majors)
				|| Util.isStringNullOrEmpty(cc)
				|| Util.isStringNullOrEmpty(debug)
				|| Util.isStringNullOrEmpty(api)
				|| !Util.isStringLengthEqual(token, 32)
				|| !Util.isStringLengthRange(title, 4, 64)
				|| !Util.isStringLengthRange(content, 2, 65535)) {
			// 参数格式错误
			resultStatus = 0;
			resultMsg = "参数格式错误";
		} else if (!Util.isStringEquals(api, "1")) {
			// API版本错误
			resultStatus = 0;
			resultMsg = "不支持的API版本";
		} else {
			boolean isConvertSuccess = false;
			try {
				setGrade = jsonToIntSet(grades);
				setDepartment = jsonToIntSet(departments);
				setMajor = jsonToIntSet(majors);
				setCC = jsonToIntSet(cc);
				bDebug = Boolean.parseBoolean(debug);
				isConvertSuccess = true;
			} catch (Exception e) {
				// 参数格式错误
				resultStatus = 0;
				resultMsg = "参数格式错误";
			}
			if (isConvertSuccess) {
				if (setGrade.isEmpty() || setDepartment.isEmpty()
						|| setMajor.isEmpty()) {
					// 参数为空
					resultStatus = 2;
					resultMsg = "毕业年度、学院列表、专业列表参数不能为空数组";
				} else {
					GatewaySAAuth dbAuth = service.getAuthByAuthToken(token);
					if (dbAuth == null) {
						// 不存在账号
						resultStatus = -1;
						resultMsg = "授权码无效";
					} else {
						SchoolAdmin dbAdmin = new SchoolAdminService()
								.getSchoolAdminInfo(dbAuth
										.getSchoolAdminUsername());
						String[] validateMsg = new String[1];
						if (!optionsService.validateOptions(dbAdmin, setGrade,
								setDepartment, setMajor, setCC, validateMsg)) {
							// 列表值错误
							resultStatus = 3;
							resultMsg = validateMsg[0];
						} else if (!optionsService
								.validateDepartmentAndMajorRelationship(
										dbAdmin, setDepartment, setMajor,
										validateMsg)) {
							// 关系错误
							resultStatus = 4;
							resultMsg = validateMsg[0];
						} else {
							if (bDebug.booleanValue()) {
								// 调试模式
								resultStatus = 11;
								resultMsg = "调试模式下发布成功。标题为：" + title;
							} else {
								SchoolMsg msg = new SchoolMsg();
								msg.setTitle(title);
								msg.setContent(content);
								msg.setSchoolAdminUsername(dbAuth
										.getSchoolAdminUsername());
								String rootPath = getServletContext()
										.getRealPath("/") + "attached/";
								if (schoolMsgService.newSchoolMsg(msg,
										intSetToStringArr(setGrade),
										intSetToStringArr(setDepartment),
										intSetToStringArr(setMajor),
										intSetToStringArr(setCC), dbAdmin,
										rootPath)) {
									msg = schoolMsgService
											.getSchoolMsgById(schoolMsgService
													.getLatestSchoolMsgId());
									GatewaySchoolMsgRecord record = new GatewaySchoolMsgRecord();
									record.setAuthIdNullabled(dbAuth
											.getAuthId());
									record.setSchoolMsgId(msg.getId());
									record.setPostIp(getIpAddress(request));
									record.setPostTime(new Timestamp(System
											.currentTimeMillis()));
									if (new GatewaySchoolMsgRecordService()
											.insertRecord(record)) {
										resultStatus = 1;
										resultMsg = "发布成功";
									} else {
										resultStatus = 999;
										resultMsg = "内部错误，请稍后重试或联系我们";
									}
								} else {
									resultStatus = 999;
									resultMsg = "内部错误，请稍后重试或联系我们";
								}
							}
						}
					}
				}
			}
		}
		// 构造约定结果集
		Map<String, Object> result = new LinkedHashMap<String, Object>(3);
		result.put("status", resultStatus);
		result.put("msg", resultMsg);
		// 输出响应
		super.outJson(result, response);
	}

	private static Set<Integer> jsonToIntSet(String json) {
		int[] arr = new Gson().fromJson(json, int[].class);
		Set<Integer> r = new HashSet<Integer>();
		for (int i : arr) {
			r.add(i);
		}
		return r;
	}

	private static String[] intSetToStringArr(Set<Integer> set) {
		List<String> r = new ArrayList<String>(set.size());
		for (Integer integer : set) {
			r.add(String.valueOf(integer));
		}
		return r.toArray(new String[0]);
	}

}
