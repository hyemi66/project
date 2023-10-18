package net.daum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.daum.vo.BbsVO;

public interface BbsRepository extends JpaRepository<BbsVO, Integer> {
	/*
		1. BbsRepository는 사용자 자료실을 JPA로 실행하기 위한 Repository이다.
		2. JpaRepository 인터페이스의 부모 인터페이스 중에서 
		   PagingAndSortingRepository에서 페이징과 정렬이라는 기능을 제공한다.
		   PagingAndSortingRepository의 부모 인터페이스의 부모가 CrudRepository이다.
	*/
	
	/*
		조회수 증가 -> JPQL(JPA에서 사용하는 Query Language이며, Java Persistence Query Language의 약어이다)
		JSQL문을 사용하면 실제 테이블명 대신에 엔티티빈 클래스명을 사용하고,
		실제 테이블 컬럼명 대신 엔티티빈 클래스의 속성에 해당하는 변수명을 사용한다
	*/
	
	@Modifying // @Modifying을 이용해서 DML(insert, update, delete)문 작업 처리가 가능하다
	@Query("update BbsVO b set b.bbs_hit = ?2 where b.bbs_no = ?1")
	// ?2는 두번째로 전달되는 인자값 -> 증가 조회수 값, ?1은 첫번째로 전달되는 인자값
	public void updateBbsHit(int bbs_no, int count);
	
	@Modifying
	@Query("update BbsVO b set b.bbs_level = b.bbs_level + 1 where b.bbs_ref = ?1"
			+ " and b.bbs_level > ?2")
	public void updateLevel(int ref, int level); // 답변 레벨 증가
	
	@Modifying
	@Query("update BbsVO b set b.bbs_name = ?1, b.bbs_title = ?3, b.bbs_cont = ?4, "
			+ "b.bbs_file = ?5 where b.bbs_no = ?2")
	public void updateBbs(String name, int no, String title, String cont, String file); // 자료실 수정
}
