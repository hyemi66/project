package net.daum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.daum.vo.MemberVO;

public interface MemberRepository extends JpaRepository<MemberVO, String> {
	
	@Query("select m from MemberVO m where m.mem_id=?1 and m.mem_name=?2")
	public MemberVO pwdFind(String id, String name); // 아이디와 회원이름을 기준으로 오라클로부터 비번을 검색
	
	@Modifying
	// @Query 애노테이션은 select문만 가능하지만 @Modifying을 이용해서 DML(insert, update, delete)문 sql 처리가 가능하게 된다
	@Query("update MemberVO m set m.mem_pwd=?1 where m.mem_id=?2")
	// JPQL(JPA에서 사용하는 Query Language이다.(Java Persistence Query Language의 약어))
	// JPQL에서는 실제 테이블명 대신 엔티티빈 클래스명을 사용하고, 실제 컬럼명 대신 엔티티빈의 변수 즉 속성명을 사용한다
	public void updatePwd(String pwd, String id); // 아이디를 기준으로 암호화된 임시비번을 수정
	
	@Query("select m from MemberVO m where m.mem_id=?1 and m.mem_state=1")
	public MemberVO loginCheck(String id);
	
	@Modifying
	@Query("update MemberVO m set m.mem_pwd=?1, m.mem_name=?2, m.mem_zip=?3, m.mem_zip2=?4, "
			+ "m.mem_addr=?5, m.mem_addr2=?6, m.mem_phone01=?7, m.mem_phone02=?8, m.mem_phone03=?9, "
			+ "m.mail_id=?10, m.mail_domain=?11 where m.mem_id=?12")
	public void updateMember(String pwd, String name, String zip, String zip2, String addr, String addr2,
			String phone01, String phone02, String phone03, String mail_id, String mail_domain, String id); // 회원정보 수정완료
}
