package net.daum.dao;

import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.daum.vo.AdminVO;

@Repository
public class AdminDAOImpl implements AdminDAO {
	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private AdminRepository adminRepo;

	@Override
	public void insertAdmin(AdminVO ab) {
		//this.sqlSession.insert("admin_in", ab);
		
		System.out.println("\n=== JPA 관리자 정보 저장 ===");
		this.adminRepo.save(ab);
	} // 관리자 정보 저장

	@Override
	public AdminVO adminLogin(String admin_id) {
		//return this.sqlSession.selectOne("admin_login", admin_id);
		
		System.out.println("\n=== JPA 관리자 로그인 인증 ===");
		Optional<AdminVO> result = this.adminRepo.findById(admin_id);
		AdminVO admin;
		
		if(result.isPresent()) { // 관리자 아이디에 해당하는 관리자 정보가 있다면 참
			admin = result.get(); // 관리자 정보를 AdminVO 엔티티빈 타입으로 반환
		} else { // 관리자 정보가 없는 경우
			admin = null;
		}
		
		return admin;
	} // 관리자 로그인 인증
	
}
