package jschoi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class main {

	public static void main(String[] args) {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		try {
			conn = util.open();

			String sql = String.format("update tblsuggestions set seq=seq+1");

			
			stat = conn.createStatement();

			stat.executeUpdate(sql);

			System.out.println("실행완료");
			stat.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}//main
}

