package net.daum.vo;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name="member") // member 테이블 생성
@EqualsAndHashCode(of="mem_id")
// @equals(), hashcode(), canEqual() 메소드 자동제공
public class MemberVO { // 회원관리 엔티티빈 클래스
	
	@Id
	private String mem_id; // 회원아이디
	
	private String mem_pwd; // 회원비밀번호
	private String mem_name; // 회원이름
	private String mem_zip; // 우편번호
	private String mem_zip2; // 우편번호
	private String mem_addr; // 주소
	private String mem_addr2; // 상세주소
	private String mem_phone01; // 첫번째 전화번호(010)
	private String mem_phone02; // 두번째 전화번호
	private String mem_phone03; // 세번째 전화번호
	private String mail_id; // 메일 아이디
	private String mail_domain; // 메일 도메인 주소
	
	@CreationTimestamp
	private Timestamp mem_date; // 가입날짜
	
	private int mem_state; // 가입회원이면 1, 탈퇴회원이면 2
	private String mem_delcont; // 탈퇴사유 -> 사이트 리뉴얼할 때 반영하기 위해서
	
	private Timestamp mem_deldate; // 탈퇴날짜
	
}
