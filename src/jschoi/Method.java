package jschoi;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;

public class Method {
	//update, delete method return Boolean(if work success then true nor false)
	//select method return ArrayList<String[]>(about multiple row) or String[](about single row)
	//sql 문 옆의 숫자는 suggestionsprocedure.txt에서 procedure 번호
	/**
	 * 건의사항 정보를 최근 날짜순으로 가져옵니다.
	 * @param boolean true일 경우 건의사항 모든 정보, false일 경우 미답변 상태의 건의사항 정보를 가져옵니다.
	 * @return ArrayList<String[]> [0]글번호 [1]제목 [2]이름 [3]날짜 [4]건의사항 [5]답변 [6]전화번호
	 */
	public ArrayList<String[]> procgetsuggestions(boolean flag){
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		ArrayList<String[]> row=new ArrayList<String[]>();
		try {
			String sql="";
			if(flag) {
				sql = "{call procgetsuggestions(?)}";//1. 건의사항 정보 가져오기
			}else {
				sql = "{call procgetsuggestionsnoanswer(?)}";//3.답변이 안된 건의사항 조회하기
			}
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			stat.executeQuery();
			
			rs=(ResultSet)stat.getObject(1);
			while(rs.next()) {
				String[] str= {rs.getString("글번호")
							  ,rs.getNString("제목")
							  ,rs.getNString("이름")
							  ,rs.getNString("날짜")
							  ,rs.getNString("건의사항")
							  ,rs.getNString("답변")
							  ,rs.getNString("전화번호")};
				row.add(str);
			}
			
			
			stat.close();
			conn.close();
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("데이터를 가져오는데 실패했습니다.");
		}
		
		return row;
	}//procgetsuggestions()
	
	/**
	 * 회원번호이 작성한 건의사항을 최근 순서로 가져옵니다.
	 * @param pmember_seq 회원번호
	 * @return ArrayList<String[]> [0]글번호 [1]제목 [2]이름 [3]날짜 [4]건의사항 [5]답변 [6]전화번호
	 */
	public ArrayList<String[]> procgetsuggestions(int pmember_seq){
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		ArrayList<String[]> row=new ArrayList<String[]>();
		try {
			String sql = "{call procgetsuggestionsbymem_seq(?,?)}";//2. 회원번호를 받아서(로그인 정보를 통해) 자신의 건의사항 확인하기
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			stat.setInt(2, pmember_seq);
			stat.executeQuery();
			
			rs=(ResultSet)stat.getObject(1);
			while(rs.next()) {
				String[] str= {rs.getString("글번호")
							  ,rs.getNString("제목")
							  ,rs.getNString("이름")
							  ,rs.getNString("날짜")
							  ,rs.getNString("건의사항")
							  ,rs.getNString("답변")
							  ,rs.getNString("전화번호")};
				row.add(str);
			}
			
			
			stat.close();
			conn.close();
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("데이터를 가져오는데 실패했습니다.");
		}
		
		return row;
	}//procgetsuggestions(int member_seq)
	
	/**
	 * 키워드를 입력받아 제목과 건의사항 내용에서 일치하는 키워드를 가진 정보를 돌려줍니다.
	 * @param keyWord 제목과 건의사항 내용에서 검색하고자 하는 키워드입니다.
	 * @return ArrayList<String[]> [0]글번호 [1]제목 [2]이름 [3]날짜 [4]건의사항 [5]답변 [6]전화번호
	 */
	public ArrayList<String[]> procgetsuggestions(String keyWord){
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		ArrayList<String[]> row=new ArrayList<String[]>();
		try {
			String sql = "{call procgetsuggestionsbykeyword(?,?)}";//4. 키워드를 입력받아 제목과 건의사항 내용 검색하기
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			stat.setString(2, keyWord);
			stat.executeQuery();
			
			rs=(ResultSet)stat.getObject(1);
			while(rs.next()) {
				String[] str= {rs.getString("글번호")
							  ,rs.getNString("제목")
							  ,rs.getNString("이름")
							  ,rs.getNString("날짜")
							  ,rs.getNString("건의사항")
							  ,rs.getNString("답변")
							  ,rs.getNString("전화번호")};
				row.add(str);
			}
			
			
			stat.close();
			conn.close();
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("데이터를 가져오는데 실패했습니다.");
		}
		
		return row;
	}//procgetsuggestions(String keyWord)
	
	public boolean procsetsuggestion() {
		Scanner scan=new Scanner(System.in);
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		
		Statement onOff=null;// 질문할 부분, set define off를 사용하려 하는데 안됨.ORA-00922: missing or invalid option
		String button="set define off";
		
		
		System.out.print("회원번호");// 추후 삭제할것
		int pmember_seq=scan.nextInt();//로그인 정보에서 회원번호를 가져온다.
		scan.nextLine();
		String ptitle="";
		String pcontents="";
		while(true) {//제목 유효성 검사 100byte 이하
			System.out.print("제목 입력: ");
			ptitle=scan.nextLine();
			if(ptitle.getBytes().length<100) {
				break;
			}else{
				System.out.println("제목 길이 초과입니다.");
				System.out.printf("제한 : 100byte, 입력 : %dbyte",ptitle.getBytes().length);
			}
		}//while 제목 유효성 검사
		while(true) {//내용 유효성 검사 4000byte 이하
			System.out.print("건의사항 입력: ");
			pcontents=scan.nextLine();
			if(ptitle.getBytes().length<4000) {
				break;
			}else{
				System.out.println("제목 길이 초과입니다.");
				System.out.printf("제한 : 4000byte, 입력 : %dbyte",ptitle.getBytes().length);
			}
		}//while 제목 유효성 검사
		try {
			
			String sql = "{call procsetsuggestion(?,?,?)}";//5.건의사항 작성하기
			conn = util.open();

			//set define off를 통해 특수문자 입력
			onOff= conn.createStatement();
			onOff.executeUpdate(button);
			onOff.close();
			System.out.println("@");
			stat = conn.prepareCall(sql);
			System.out.println("@@");

			stat.setInt(1, pmember_seq);
			stat.executeQuery();

			stat.close();
			
			button="set define on";
			onOff= conn.createStatement();
			onOff.executeUpdate(button);
			onOff.close();
			stat.close();
			conn.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}//procsetsuggestion
}


