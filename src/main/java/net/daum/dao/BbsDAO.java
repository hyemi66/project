package net.daum.dao;

import java.util.List;

import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

public interface BbsDAO {

	void insertBbs(BbsVO b);

	int getRowCount(PageVO p);

	List<BbsVO> getBbsList(PageVO p);

	void updateHit(int bbs_no);

	BbsVO getBbsCont(int bbs_no);

	void updateLevel(BbsVO b);

	void replyBbs(BbsVO b);

	void editBbs(BbsVO b);

	void delBbs(int bbs_no);

}
