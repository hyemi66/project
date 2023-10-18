package net.daum.dao;

import net.daum.vo.AdminVO;

public interface AdminDAO {

	void insertAdmin(AdminVO ab);

	AdminVO adminLogin(String admin_id);
	
}
