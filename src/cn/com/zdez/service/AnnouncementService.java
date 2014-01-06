package cn.com.zdez.service;

import java.util.List;

import cn.com.zdez.dao.AnnouncementDao;
import cn.com.zdez.po.Announcement;
import cn.com.zdez.util.ContentOperation;

public class AnnouncementService {
	
	AnnouncementDao dao = new AnnouncementDao();
	
	public List<Announcement> getAll() {
		return dao.getAll();
	}
	
	public Boolean NewAnnouncement(Announcement a, String rootPath) {
		boolean flag = false;
		if (dao.insert(a)) {
			int annoId = dao.getLatestAnnoId();
			// 将内容写入html文件
			if (new ContentOperation().SaveContent("anno", annoId, a.getContent(), rootPath)) {
				flag = true;
			}
		}
		return flag;
	}

}
