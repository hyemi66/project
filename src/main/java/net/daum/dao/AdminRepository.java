package net.daum.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.daum.vo.AdminVO;

public interface AdminRepository extends JpaRepository<AdminVO, String> {
	
}
