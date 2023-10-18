package net.daum.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@SequenceGenerator( // 오라클 시퀀스 생성기
	name = "zip_no_seq_gename", // 시퀀스 제너레이터 이름
	sequenceName = "zip_no_seq", // 시퀀스 이름
	initialValue = 1, // 시퀀스 시작값
	allocationSize = 1 // 시퀀스 증가값
)
@Table(name="zipcode") // zipcode 테이블명 생성
@EqualsAndHashCode(of="no")
public class ZipCodeVO { // 우편번호, 주소등 저장할 엔티티빈 클래스
	
	@Id
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE, // 사용할 전략을 시퀀스로 선택
		generator = "zip_no_seq_gename" // 시퀀스 제너레이터 이름
	)
	private int no;
	
	private String zipcode; // 우편번호
	private String sido; // 시도
	private String gugun; // 구군
	private String gil; // 길
	private String bunji; // 나머지 주소
	
}
