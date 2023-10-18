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
@Table(name="admin") // admin 테이블 생성
@EqualsAndHashCode(of="admin_id")
/* equals(), hashCode(), canEqual() 메소드 자동 제공 */
public class AdminVO { // 관리자 엔티티빈 클래스
	
	private int admin_no; // 번호
	
	@Id // 기본키 컬럼(구분키, 식별키로 활용)
	private String admin_id; // 관리자 아이디
	private String admin_pwd; // 관리자 비번
	private String admin_name; // 관리자 이름
	
	@CreationTimestamp // 등록 시점 날짜값 기록, mybatis로 실행하면 구동 안됨
	private Timestamp admin_date; // 등록날짜 -> JPA로 레코드 저장시 실행되어서 등록시점 날짜값이 기록됨
	
}
