package jschoi;

import java.sql.Connection;
import java.sql.DriverManager;

//JAVA DOC 주석
/**
 * 
 * @author 아무개
 * 오라클 연결 클래스입니다
 * 
 */
public class DBUtil {

	private Connection conn=null;
	/**
	 * 서버에 연결합니다.
	 * @return 연결 객체를 반환합니다.
	 */
	public Connection open() {
		
		//String url="jdbc:oracle:thin:@211.63.89.36:1521:xe";//쌍용학원
		String url="jdbc:oracle:thin:@localhost:1521:xe";//집
		//String url="jdbc:oracle:thin:@172.30.1.17:1521:xe";//허슬버슬
	
		String id="ims";
		String pw="java1234";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn=DriverManager.getConnection(url, id, pw);//접속 버튼과 동일
			return conn;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}//open
	
	/**
	 * 서버에 연결합니다.
	 * @param host 서버 주소
	 * @param id 계정명
	 * @param pw 비밀번호
	 * @return 연결 객체를 반환합니다.
	 */
	public Connection open(String host, String id, String pw) {
		
		String url="jdbc:oracle:thin:@"+host+":1521:xe";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn=DriverManager.getConnection(url, id, pw);//접속 버튼과 동일
			return conn;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}//open

	/**
	 * 연결을 종료합니다.
	 */
	public void close() {
		try {
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}//close
}//DBUtil
