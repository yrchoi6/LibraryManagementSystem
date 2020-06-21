---------------------------------------
-- 회원
----------------------------------------
create table tblMember
(
    seq number primary key,--회원번호(pk)
    name varchar2(20),--회원이름
    tel varchar2(20),--회원전화번호
    address varchar2(100),--회원주소
    ssn varchar2(15),--회원 주민번호
    withdrawal number--탈퇴여부

);
create sequence member_seq;

drop table tblMember;
drop sequence member_seq;

insert into tblMember(seq, name, tel, address, ssn, withdrawal) values (member_seq.nextVal, '김규태', '010-4923-4056', '서울시 강남구 세곡동', '710820-1119651', 0); --테스트

select * from tblMember;


---------------------------------------
-- 도서정보
----------------------------------------

create table tblBook
(
    seq number primary key,--도서번호(pk)
    book_name varchar2(100),--책 제목
    publisher varchar2(100),--출판사
    author varchar2(100),--저자
    decimal_num number references tblDecimalCategory(num) null,--십진분류번호
    series_num number,--시리즈 번호
    delete_exist number--삭제여부

);
create sequence book_seq;

drop table tblBook;
drop sequence book_seq;

insert into tblBook (seq,book_name,publisher,author,decimal_num,series_num,delete_exist)
    values (book_seq.nextVal,'수학의정석','성지출판','홍성대',1,1,1);

delete tblBook;

select * from tblBook;

---------------------------------------
-- 관리자
----------------------------------------


create table tblAdmin
(
    seq number primary key,--관리자 번호(pk)
    admin_id VARCHAR2(40),--관리자 아이디
    admin_pw VARCHAR2(40)--관리자 비밀번호
    
);
create sequence admin_seq;

drop table tblAdmin;
drop sequence admin_seq;

insert into tblAdmin (seq,admin_id,admin_pw) values (admin_seq.nextVal,'관리자아이디','관리자비밀번호');

select * from tblAdmin;


---------------------------------------
-- 건의사항
----------------------------------------
create table tblSuggestions
(
    seq number primary key,--건의사항 번호(pk)
    member_seq number references tblMember(seq) not null,--회원번호(fk)
    title varchar2(100),--건의사항 제목
    sug_contents varchar2(1500),--건의사항 내용
    regdate Date,--작성일자
    answer VARCHAR2(1000),--답변
    delete_sug number--삭제여부
);
create sequence suggestion_seq;

drop table tblSuggestions;
drop sequence suggestion_seq;

insert into tblSuggestions (seq,member_seq,title,sug_contents,regdate,answer,delete_sug)
    values (suggestion_seq.nextVal,1,'건의사항 제목','건의사항 내용',sysdate,'답변',1);

insert into tblSuggestions values(suggestion_seq.nextVal,1,'관내 마스크착용 의무화 ',' 5/29 오후4시경 장당도서관방문함1층입구에서 열체크및 개인정보열람 동의서? 작성후들어가는데도서관관계자분같은 남성분(60대이상)1분이 마스크 미착용하고이사람저사람 모두 대화하고도서관 안밖으로 자유자재로 돌아다님1층입구열체크하시는분께저분은 마스크안해도괜찮냐고 물어봄책 다빌리고나오는데그분 여전히 1층에서 마스크미착용하고관내에 계심도서관관계자가 아니었으면 출입도 제제가 있었을거고마스크 안하고 관내에 있을수없었을거임 Cctv확인하시고 조치바랍니다모두가 조심하는 시기에공공도서관에서 있을수없는 일입니다 ',to_date('2018 1 4','yyyymmdd'),'안녕하세요 도서관 관리자 입니다. 제안해주신 사항에 대해 바로 개선하겠습니다.',0);
insert into tblSuggestions values(suggestion_seq.nextVal,1,'관내 마스크착용 의무화 ',' 5/29 오후4시경 장당도서관방문함1층입구에서 열체크및 개인정보열람 동의서? 작성후들어가는데도서관관계자분같은 남성분(60대이상)1분이 마스크 미착용하고이사람저사람 모두 대화하고도서관 안밖으로 자유자재로 돌아다님1층입구열체크하시는분께저분은 마스크안해도괜찮냐고 물어봄책 다빌리고나오는데그분 여전히 1층에서 마스크미착용하고관내에 계심도서관관계자가 아니었으면 출입도 제제가 있었을거고마스크 안하고 관내에 있을수없었을거임 Cctv확인하시고 조치바랍니다모두가 조심하는 시기에공공도서관에서 있을수없는 일입니다 ',to_date('2018 1 4','yyyymmdd'),'안녕하세요 도서관 관리자 입니다. 제안해주신 사항에 대해 바로 개선하겠습니다.',0);



select * from tblSuggestions;

---------------------------------------
-- 대여
----------------------------------------
create table tblRent
(
    seq number primary key,--대여 번호
    member_seq number references tblMember(seq) not null,--회원번호(fk)
    book_code varchar2(100) references tblBookState(book_code) not null,--도서코드(fk)
    rent_date date,--대여날짜
    return_date date,--반납날짜
    extension_count number--연장횟수
);
create sequence rent_seq;

drop table tblRent;
drop sequence rent_seq;

insert into tblRent (seq,member_seq,book_code,rent_date,return_date,extension_count)
    values (rent_seq.nextVal,1,'도서코드',sysdate,sysdate,3);
    
select * from tblRent;

---------------------------------------
-- 연체회원
----------------------------------------
create table tblBlack
(
    seq number primary key,--연체회원번호
    member_seq number references tblMember(seq) not null,--회원번호(fk)
    black_year varchar2(10)--년도

);
create sequence black_seq;

drop table tblBlack;
drop sequence black_seq;

insert into tblBlack (seq,member_seq,black_year) values (black_seq.nextVal,1,'2020'); 

select * from tblBlack;

---------------------------------------
-- 예약
----------------------------------------

create table tblReservation
(
    seq number primary key,--예약번호
    member_seq number references tblMember(seq) not null,--회원번호(fk)
    book_seq number references tblBook(seq) not null,--도서번호(fk)
    res_date Date--예약신청 날짜

);
create sequence res_seq;


drop table tblReservation;
drop sequence res_seq;

insert into tblReservation (seq,member_seq,book_seq,res_date) values (res_seq.nextVal,1,1,sysdate);
insert into tblReservation (seq,member_seq,book_seq,res_date) values(res_seq.nextVal,회원번호,도서번호,예약신청날짜);

select * from tblReservation;

---------------------------------------
-- 도서
----------------------------------------
create table tblBookState
(
    book_code VARCHAR2(100) primary key,--도서코드
    book_seq number references tblBook(seq) not null,--도서번호(fk)
    book_location VARCHAR2(10),--도서위치
    delete_exist number --삭제여부

);

create sequence bookstate_seq;

drop table tblBookState;
drop sequence bookstate_seq;

insert into tblBookState (book_code,book_seq,book_location,delete_exist)
    values ('도서코드',1,'위치',1);
    
select * from tblBookState;

---------------------------------------
-- 십진분류
----------------------------------------

create table tblDecimalCategory
(
    num number primary key,--십진분류 번호
    large_num number references tblLargeCategory(num) not null,--대분류번호(fk)
    medium_num number references tblMediumCategory(num) not null,--중분류번호(fk)
    small_num number references tblSmallCategoty(num) not null--소분류번호(fk)
    

);

drop table tblDecimalCategory;

select * from tblDecimalCategory;

insert into tblDecimalCategory values(1,1,1,1);

---------------------------------------
-- 대분류
----------------------------------------

create table tblLargeCategory
(
    num number primary key,--대분류 번호(pk)
    category VARCHAR2(20)--항목

);


drop table tblLargeCategory;
select * from tblLargeCategory;

insert into tblLargeCategory values (1,'항목');


---------------------------------------
-- 중분류
----------------------------------------

create table tblMediumCategory
(
    num number primary key,--중분류 번호(pk)
    large_num number references tblLargeCategory(num) not null,--대분류번호(fk)
    category VARCHAR2(20)--항목

);

drop table tblMediumCategory;

insert into tblMediumCategory values (1,1,'중분류항목');

---------------------------------------
-- 소분류
----------------------------------------

create table tblSmallCategoty
(
    num number primary key,--소분류 번호(pk)
    medium_num number references tblMediumCategory(num) not null,--중분류번호(fk)
    category VARCHAR2(20)--항목
);

drop table tblSmallCategoty;

insert into tblSmallCategoty values (1,1,'소분류항목');






