package net.daum.vo;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter // setter() 메소드 자동제공
@Getter // getter() 메소드 자동제공
@ToString // toString() 메소드 자동제공
@Entity // 엔티티빈 의미 JPA
/*@SequenceGenerator( // @SquenceGenerator 시퀀스 생성기 애노테이션
	name = "bbs_no_seq_gename", // 시퀀스 제너레이터 이름
	sequenceName = "bbs_no_seq", // 시퀀스 이름 -> bbs_no_seq라는 시퀀스가 생성됨
	initialValue = 1, // 시퀀스 번호 시작값
	allocationSize = 1 // 증가값, 기본값은 50
)*/
@Table(name="bbs") // bbs테이블을 생성
@EqualsAndHashCode(of="bbs_no") // equal(), hashcode(), canEqual() 메소드 자동 생성
public class BbsVO { // bbs 자료실 테이블 생성과 중간 자료 저장하는 BbsVO 엔티티빈 클래스 -> JPA
	
	@Id // 기본키 컬럼 -> 식별키
	/*@GeneratedValue(
		strategy = GenerationType.SEQUENCE, // 사용할 전략을 시퀀스로 선택
		generator = "bbs_no_seq_gename" // 시퀀스 생성기에 설정해 놓은 시퀀스 제너레이터 이름
	)*/
	
	private int bbs_no; // 자료실 번호 -> JPA를 통해서 bbs_no컬럼이 생성이 되고 primary key 즉 기본키가 된다
	
	private String bbs_name; // 글쓴이
	private String bbs_title; // 글제목
	private String bbs_pwd; // 비번
	private String bbs_cont; // 글내용
	private String bbs_file; // 첨부파일 경로와 파일명
	private int bbs_hit; // 조회수
	
	// 계층형 계단형 자료실 기능을 만들기 위해서 -> 관리자 답변글 가능 추가 컬럼
	private int bbs_ref; // 글 그룹번호(답변글) -> 원본글과 답변글을 묶어주는 역할
	private int bbs_step;
	/*
		원본글과 답변글을 구분하는 번호값이면서 몇번째 답변글인지 알려주는 역할.
		원본글이면 0, 첫번째 관리자 답변글이면 1, 두번째 답변글이면 2, ...
	*/
	private int bbs_level; // 답변글 정렬순서
	
	@CreationTimestamp // ORM 하이버네이트 프레임워크의 특별한 기능으로 자료실 등록시점 날짜값 등록
	// MyBatis에서는 작동이 안됨. JPA를 통해서 작동됨
	private Timestamp bbs_date; // 등록날짜
	
}
