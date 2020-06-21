package LibrarySystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Borrow {
	
	//[0]회원번호[1]도서코드[2]대여날짜[3]반납날짜[4]연장횟수
	static List<String[]> borrowList = new ArrayList<String[]>();

	public static void main(String[] args) throws Exception {
		
		File file = new File("C:\\Users\\ssj50\\Desktop\\tblrent_insert.txt");
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		borrowList.clear();
		int count = 0;
		conn = DBUtil.open();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		// 현재날짜
		Calendar now = Calendar.getInstance();
		// 세팅날짜
		Calendar setting = Calendar.getInstance();
		setting.set(2018, 0, 1);
		
		//1. 대여번호 
		while(setting.getTimeInMillis()<now.getTimeInMillis())  {
			
			for (int i=1; i<=50; i++) {
				
				//2. 회원번호 1~300
				Random rndMemberNum = new Random();
				int memberNum = rndMemberNum.nextInt(300)+1;
				
				
				//3. 도서코드
				String bookcode = "";
				
				Random rndBook = new Random();
				int bookNum = rndBook.nextInt(2067)+1;

				try {
					stat = conn.createStatement();
		
					String sql = String.format("select t.r, t.c code from (select rownum r, book_code c from tblbookstate) t where t.r = %d",bookNum);
					
					rs = stat.executeQuery(sql);
					
					if (rs.next()) {
						bookcode = rs.getString("code");
					}
					
					stat.close();
					rs.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//4. 대여날짜
				String borrowDate = "";
				/*
				고려사항
				. 대여기록 2018년도부터 주말제외
				. 동일한 회원번호 반납날짜 이후부터
				. 동일한 도서코드 반납날짜 이후부터
				*/

				
				//5. 반납날짜
				String returnDate = "";
				int[] returnbuff = {1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 9, 10};
				Random returnRnd = new Random();
				/*
				고려사항
				. 최대 10일까지만 추가할예정
				 */
				
				
				//6. 연장횟수
				String extension = "";
				int[] extensionbuff = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0};
				Random extensionRnd = new Random();
				/*
				고려사항
				. 예약이 있는 경우 연장불가 -> 예약 테이블에서 연장 0 회만 뽑아아서 입력하기
				 */
				
				
				// 주말인 경우 바로 while문으로 다시 돌아감
				if(setting.get(Calendar.DAY_OF_WEEK) == 1 || setting.get(Calendar.DAY_OF_WEEK) == 7) {
					break;
				} else {
					boolean condition = true;
					
					for (int j=0; j < borrowList.size(); j++) {

						//반납날짜
						Calendar buff = Calendar.getInstance();
						String[] buffdate = borrowList.get(j)[3].split("-");
						buff.set(Integer.parseInt(buffdate[0])
								, Integer.parseInt(buffdate[1])-1
								, Integer.parseInt(buffdate[0]));
						
						//동일한 도서코드
						if(borrowList.get(j)[1].equals(bookcode)) {
							//세팅날짜보다 반납날짜가 이후인 경우 break
							if (buff.getTimeInMillis() > setting.getTimeInMillis()) {
								condition = false;
							}
						}
					}
					
					if (condition == true) {
						
						count++;
						
						borrowDate = String.format("%tF", setting);
						
						int return1 = returnbuff[returnRnd.nextInt(returnbuff.length)];
						String[] rtdatebuff = String.format("%tF", setting).split("-");
						Calendar returndatecal = Calendar.getInstance();
						returndatecal.set(Integer.parseInt(rtdatebuff[0])
								, Integer.parseInt(rtdatebuff[1])-1
								, Integer.parseInt(rtdatebuff[2]));
						returndatecal.add(Calendar.DATE, return1);
						returnDate = String.format("%tF", returndatecal);
						
						int extension1 = extensionbuff[extensionRnd.nextInt(extensionbuff.length)];
						extension = String.format("%d", extension1);
						
						String sql = "insert into tblrent values (rent_seq.nextval, "+ memberNum +", '" + bookcode + "', '" + borrowDate + "', '" + returnDate + "', " + extension+");";
						System.out.println(sql);
						writer.append(sql + "\n");
//						System.out.println(count+","+memberNum+","+bookcode+","+borrowDate+","+returnDate+","+extension);	
//						System.out.printf("%tF\n",setting);
					}
 				}
				
				
				//연체 : (대여날짜+7+(연장횟수*7)) - 반납날짜
				//공식이 양수면 연체
				
				
			}//for
			
			setting.add(Calendar.DATE, 1);
			
		}//while
		
		writer.close();
		
	}//main
}//borrow
