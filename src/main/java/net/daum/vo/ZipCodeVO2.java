package net.daum.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ZipCodeVO2 {
	
	private String zipcode; // 우편번호
	private String addr; // 시도 구군 길(읍면동)
	
}
