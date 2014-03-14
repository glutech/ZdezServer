package cn.com.zdez.activity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.zdez.activity.bo.ActInteractionBo;
import cn.com.zdez.activity.dao.ActivityCategoryDao;
import cn.com.zdez.activity.po.ActivityCategory;
import cn.com.zdez.activity.vo.ActCatgWithInteractionVo;

public class ActCatgService {

	private ActivityCategoryDao dao = new ActivityCategoryDao();

	public ActivityCategory getActCatgById(int catgId) {
		return dao.getActCatgById(catgId);
	}

	public ActCatgWithInteractionVo getActCatgWithInteractionVoById(int catgId) {
		ActivityCategory po = dao.getActCatgById(catgId);
		ActInteractionBo bo = dao.getActInteractionBoByCatgId(catgId);
		return toActCatgWithInteractionVo(po, bo);
	}

	public List<ActCatgWithInteractionVo> getAllActCatgWithInteractionVo() {
		List<ActivityCategory> poList = dao.getAllActCatgs();
		Map<Integer, ActInteractionBo> boMap = dao
				.getAllActInteractionBos(poList);
		List<ActCatgWithInteractionVo> r = new ArrayList<ActCatgWithInteractionVo>(
				poList.size());
		for (ActivityCategory po : poList) {
			r.add(toActCatgWithInteractionVo(po, boMap.get(po.getCatgId())));
		}
		return r;
	}

	private ActCatgWithInteractionVo toActCatgWithInteractionVo(
			ActivityCategory po, ActInteractionBo bo) {
		ActCatgWithInteractionVo vo = new ActCatgWithInteractionVo();
		vo.setCatgId(po.getCatgId());
		vo.setCatgName(po.getCatgName());
		vo.setActInteractionBo(bo);
		return vo;
	}

}