package cn.com.zdez.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import cn.com.zdez.dao.ConnectionFactory;

public class MassInsertion {

	private ConnectionFactory factory = ConnectionFactory.getInstatnce();
	private Connection conn = null;
	private PreparedStatement pstmt = null;

	public InputStream getDataInputStream(int arg1, List<Integer> arg2) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < arg2.size(); i++) {
			builder.append(arg1);
			builder.append("\t");
			builder.append(arg2.get(i));
			builder.append("\n");
		}

		byte[] bytes = builder.toString().getBytes();
		InputStream is = new ByteArrayInputStream(bytes);
		return is;
	}

	public int bulkLoadFromInputStream(String loadDataSql,
			InputStream dataStream) {
		int result = 0;

		if (dataStream == null) {
			System.out.println("InputStream is null, no data imported!");
			return 0;
		}

		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(loadDataSql);

			if (pstmt.isWrapperFor(com.mysql.jdbc.Statement.class)) {
				com.mysql.jdbc.PreparedStatement pstmt2 = pstmt
						.unwrap(com.mysql.jdbc.PreparedStatement.class);
				pstmt2.setLocalInfileInputStream(dataStream);
				result = pstmt2.executeUpdate();

				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}

		return result;
	}

	public String excuteMassInsertion(String sql, int arg1, List<Integer> arg2) {
		String str = "";

		InputStream dataStream = this.getDataInputStream(arg1, arg2);

		long beginTime = System.currentTimeMillis();
		int rows = this.bulkLoadFromInputStream(sql, dataStream);
		
		long endTime = System.currentTimeMillis();
		str = "importing " + rows + " rows data into mysql and cost "
				+ (endTime - beginTime) + " ms!";

		return str;
	}

}
