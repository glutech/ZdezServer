package cn.com.zdez.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.po.Announcement;

public class AnnouncementDao {
	
	private SQLExecution sqlE = new SQLExecution();
	
	public List<Announcement> getAll() {
		List<Announcement> list = new ArrayList<Announcement>();
		
		String sql = "select * from announcement order by date desc";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		
		try {
			while(rs.next()) {
				Announcement a = new Announcement();
				a.setId(rs.getInt("id"));
				a.setTitle(rs.getString("title"));
				a.setContent(rs.getString("content"));
				a.setSign(rs.getString("sign"));
				a.setDate(rs.getString("date").substring(0, 16));
				list.add(a);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public Boolean insert(Announcement a) {
		Boolean flag = false;
		
		String sql = "insert into announcement (title, content, date, sign) values (?,?,?,?)";
		Object[] params = {a.getTitle(), a.getContent(), a.getDate(), a.getSign()};
		
		flag = sqlE.execSqlWithoutRS(sql, params);
		
		return flag;
	}
	
	public int getLatestAnnoId() {
		int i = 0;
		
		String sql = "select id from announcement order by date desc limit 0,1";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				i = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return i;
	}

}
