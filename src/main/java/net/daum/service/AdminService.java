package net.daum.service;

import net.daum.vo.AdminVO;

public interface AdminService {

	void insertAdmin(AdminVO ab);

	AdminVO adminLogin(String admin_id);
	
}
