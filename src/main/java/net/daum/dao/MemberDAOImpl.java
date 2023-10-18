package net.daum.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.daum.vo.MemberVO;
import net.daum.vo.ZipCodeVO;

@Repository
public class MemberDAOImpl implements MemberDAO {
	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private ZipCodeRepository zipcodeRepo;

	@Override
	public MemberVO idCheck(String id) {
		//return this.sqlSession.selectOne("m_idcheck", id);
		
		System.out.println("\n=== JPA 아이디 중복 검색  ===");
		Optional<MemberVO> result = this.memberRepo.findById(id);
		MemberVO member;
		
		if(result.isPresent()) { // 아이디에 해당하는 회원정보가 있다면 참
			member = result.get(); // MemberVO 엔티티 타입 객체를 구함
		} else { // 회원정보가 없다면
			member = null;
		}
		
		return member;
	} // 아이디 중복 검색

	@Override
	public List<ZipCodeVO> zipFind(String dong) {
		//return this.sqlSession.selectList("m_zip", dong);
		
		System.out.println("\n=== JPA 우편주소 검색 ===");
		List<ZipCodeVO> zlist = this.zipcodeRepo.findByGil(dong);
		
		return zlist;
	} // 우편주소 검색

	@Override
	public void insertMember(MemberVO m) {
		//this.sqlSession.insert("m_in", m);
		
		System.out.println("\n=== JPA 회원저장 ===");
		m.setMem_state(1); // 가입회원일때 1 저장
		this.memberRepo.save(m);
	} // 회원저장

	@Override
	public MemberVO pwdMember(MemberVO m) {
		//return this.sqlSession.selectOne("p_find", m);
		
		System.out.println("\n=== JPA 회원의 아이디와 이름 기준으로 비번 찾기 ===");
		MemberVO pm = this.memberRepo.pwdFind(m.getMem_id(), m.getMem_name());
		
		return pm;
	} // 비번찾기 -> 아이디와 회원이름을 기준으로 회원정보를 검색
	
	@Transactional
	@Override
	public void updateRanPwd(MemberVO m) {
		//this.sqlSession.update("p_edit", m);
		
		System.out.println("\n=== JPA 암호화된 임시비번으로 수정 ===");
		this.memberRepo.updatePwd(m.getMem_pwd(), m.getMem_id());
	} // 암호화 된 임시 비번으로 수정

	@Override
	public MemberVO loginCheck(String login_id) {
		//return this.sqlSession.selectOne("m_loginCheck", login_id);
		
		System.out.println("\n=== JPA 가입회원 1인 경우와 아이디로 로그인 인증처리 ===");
		MemberVO lm = this.memberRepo.loginCheck(login_id);
		
		return lm;
	} // 가입회원 1인 경우와 아이디로 로그인 인증처리

	@Override
	public MemberVO getMember(String id) {
		//return this.sqlSession.selectOne("m_EditMember", id);
		
		System.out.println("\n=== JPA 아이디에 해당하는 회원정보 읽어오기"
				+ "\n(수정폼에서 활용 : 이 경우 로그인 된 상태에서 실행하기 때문에 반드시 DB회원정보 레코드가 있는 경우이다." 
				+ "\n이런 경우는 getReferenceById() 내장 메소드를 사용하면 된다.)");
		MemberVO m = this.memberRepo.getReferenceById(id);
		
		return m;
	} // 아이디에 해당하는 회원정보 읽어오기
	
	@Transactional
	@Override
	public void updateMember(MemberVO m) {
		//this.sqlSession.update("medit_ok", m);
		
		System.out.println("\n=== JPA 회원정보 수정완료 ===");
		this.memberRepo.updateMember(m.getMem_pwd(), m.getMem_name(), m.getMem_zip(), m.getMem_zip2(),
				m.getMem_addr(), m.getMem_addr2(), m.getMem_phone01(), m.getMem_phone02(), m.getMem_phone03(),
				m.getMail_id(), m.getMail_domain(), m.getMem_id());
	} // 회원정보 수정완료

	@Override
	public void delMem(MemberVO dm) {
		//this.sqlSession.update("mDel_ok", dm);
		
		System.out.println("\n=== JPA 회원 탈퇴 완료 ===");
		Optional<MemberVO> result = this.memberRepo.findById(dm.getMem_id());
		MemberVO member;
		
		if(result.isPresent()) { // 아이디에 해당하는 회원정보가 있다면 참
			member = result.get(); // MemberVO 엔티티 타입객체를 구함
			
			member.setMem_delcont(dm.getMem_delcont()); // 탈퇴 사유 저장
			member.setMem_state(2); // 탈퇴 회원일때 구분값 2 저장, 가입회원이면 1
			member.setMem_deldate(new Timestamp(System.currentTimeMillis())); // 탈퇴 날짜 기록
			
			this.memberRepo.save(member);
		}
	
	} // 회원 탈퇴 완료
	
}
