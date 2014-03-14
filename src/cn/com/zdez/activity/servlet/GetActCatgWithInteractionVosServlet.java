package cn.com.zdez.activity.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.activity.service.ActCatgService;
import cn.com.zdez.activity.vo.ActCatgWithInteractionVo;

@SuppressWarnings("serial")
public class GetActCatgWithInteractionVosServlet extends BaseServlet {

	private ActCatgService service = new ActCatgService();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<ActCatgWithInteractionVo> voList = service
				.getAllActCatgWithInteractionVo();
		outJson(response, voList);
	}

}
