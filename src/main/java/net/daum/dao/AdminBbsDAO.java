package net.daum.dao;

import java.util.List;

import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

public interface AdminBbsDAO {

	int getListCount(PageVO p);

	List<BbsVO> getAdminBbsList(PageVO p);

	void adminInsertBbs(BbsVO b);

	BbsVO getAdminBbsCont(int no);

	void adminUpdateBbs(BbsVO b);

	void adminBbsDel(int no);

}
