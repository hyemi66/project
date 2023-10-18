package net.daum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import net.daum.dao.BbsDAO;
import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Service
public class BbsServiceImpl implements BbsService {
	
	/*
		스프링 MVC에서 서비스의 역할
		1. 컨트롤러와 모델 DAO를 연결하는 중간 매개체 역할
		2. 고객의 추가요구 사항을 반영하는 곳
		3. 스프링의 AOP를 통한 트랜잭션 적용함으로써 데이터 불일치 현상을 제거 -> 데이터 일관성 유지
	*/
	
	@Autowired
	private BbsDAO bbsDao;

	@Override
	public void insertBbs(BbsVO b) {
		this.bbsDao.insertBbs(b);
	}

	@Override
	public int getRowCount(PageVO p) {
		return this.bbsDao.getRowCount(p);
	}

	@Override
	public List<BbsVO> getBbsList(PageVO p) {
		return this.bbsDao.getBbsList(p);
	}
	
	// 내용보기 및 조회수 증가 -> 스프링의 AOP를 통한 트랜잭션 적용
	@Transactional(isolation = Isolation.READ_COMMITTED)
	// 트랜잭션 격리(트랜잭션이 적용되는 중간에 외부간섭을 배제하는 것)
	@Override
	public BbsVO getBbsCont(int bbs_no) {
		this.bbsDao.updateHit(bbs_no); // 조회수 증가
		BbsVO bc = this.bbsDao.getBbsCont(bbs_no);
		
		bc.setBbs_hit(bc.getBbs_hit() + 1); // 실제 가져오는 조회수가 실제 레코드보다 하나 적기에 +1(JPA 실행시에만 추가 코드)
		
		return bc;
	}

	@Override
	public BbsVO getBbsCont2(int bbs_no) {
		return this.bbsDao.getBbsCont(bbs_no);
	} // 답변폼, 수정폼, 삭제폼일때는 조회수는 증가안되고 내용보기만 가능하다

	// 답변 레벨 증가 + 답변 저장(update + insert -> AOP를 통한 트랜잭션 적용 대상)
	@Transactional // 트랜잭션 적용
	@Override
	public void replyBbs(BbsVO b) {
		this.bbsDao.updateLevel(b); // 답변 레벨 증가
		this.bbsDao.replyBbs(b); // 답변 저장
	}

	@Override
	public void editBbs(BbsVO b) {
		this.bbsDao.editBbs(b);
	}

	@Override
	public void delBbs(int bbs_no) {
		this.bbsDao.delBbs(bbs_no);
	}
	
}
