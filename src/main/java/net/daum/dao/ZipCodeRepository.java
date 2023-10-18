package net.daum.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.daum.vo.ZipCodeVO;

public interface ZipCodeRepository extends JpaRepository<ZipCodeVO, Integer> {
	
	@Query("select z from ZipCodeVO z where z.gil like ?1 and z.no > 0 order by z.no desc")
	// 쿼리 메소드 정의 -> JPQL문을 사용함
	public List<ZipCodeVO> findByGil(String dong); // 우편주소 검색
	
}
