package net.daum.service;

import java.util.List;

import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

public interface BbsService {

	void insertBbs(BbsVO b);

	int getRowCount(PageVO p);

	List<BbsVO> getBbsList(PageVO p);

	BbsVO getBbsCont(int bbs_no);

	BbsVO getBbsCont2(int bbs_no);

	void replyBbs(BbsVO b);

	void editBbs(BbsVO b);

	void delBbs(int bbs_no);

}
