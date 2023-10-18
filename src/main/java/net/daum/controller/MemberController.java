package net.daum.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.daum.service.MemberService;
import net.daum.vo.MemberVO;
import net.daum.vo.ZipCodeVO;
import net.daum.vo.ZipCodeVO2;
import pwdconv.PwdChange;

@Controller
public class MemberController { // 사용자 회원관리
	
	@Autowired
	private MemberService memberService;
	
	// 로그인 폼
	@GetMapping("/member_login")
	public ModelAndView member_login() {
		
		
		return new ModelAndView("member/member_Login"); // 생성자 인자값으로 뷰페이지 경로(뷰리졸브 경로)
	} // member_login()
	
	// 회원가입 폼
	@RequestMapping("/member_join")
	public ModelAndView member_join() {
		String[] phone = {"010", "011", "019"};
		String[] email = {"naver.com", "daum.net", "gmail.com", "nate.com", "직접입력"};
		
		ModelAndView jm = new ModelAndView();
		jm.addObject("phone", phone);
		jm.addObject("email", email);
		jm.setViewName("member/member_Join");
		
		return jm;
	} // member_join()
	
	// 아이디 중복 검색
	@PostMapping("/member_idcheck")
	public ModelAndView member_idcheck(String id, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		/*
			response.setContentType("text/html; charset=UTF-8");
			-> 웹브라우저 출력되는 문자와 태그, 언어코딩 타입을 설정함으로써 출력되는 한글이 안깨지고,
			html과 자바스크립트가 잘 실행되게 해준다
		*/
		
		PrintWriter out = response.getWriter(); // 출력스트림 out생성
		
		MemberVO db_id = this.memberService.idCheck(id); // 아이디에 해당하는 회원정보 검색
		
		int re = -1; // 중복 아이디가 없을 때 반환값
		if(db_id != null) { // 중복아이디가 있는 경우
			re = 1;
		}
		out.println(re); // 값 반환기능
		
		return null;
	} // member_idcheck()
	
	// 우편주소 검색 공지창
	@RequestMapping("/zip_find")
	public ModelAndView zip_find() {
		ModelAndView zm = new ModelAndView();
		zm.setViewName("member/zip_Find"); // 뷰페이지경로
		
		return zm;
	}
	
	// 우편주소 검색 결과
	@PostMapping("/zip_find_ok")
	public ModelAndView zip_find_ok(String dong) {
		List<ZipCodeVO> zlist = this.memberService.zipFind("%" + dong + "%");
		
		List<ZipCodeVO2> zlist2 = new ArrayList<>();
		
		for(ZipCodeVO z : zlist) {
			ZipCodeVO2 z2 = new ZipCodeVO2();
			
			z2.setZipcode(z.getZipcode()); // 우편번호 저장
			z2.setAddr(z.getSido() + " " + z.getGugun() + " " + z.getGil()); // 시도 구군 길 (도로명 또는 지번)
			
			zlist2.add(z2); // 컬렉션에 추가
		}
		
		ModelAndView zm = new ModelAndView("member/zip_Find");
		zm.addObject("zipcodelist", zlist2);
		zm.addObject("dong", dong);
		
		return zm;
	} // zip_find_ok()
	
	// 회원저장
	@RequestMapping("/member_join_ok")
	public ModelAndView member_join_ok(MemberVO m) {
		/*
			member_Join.jsp의 네임피라미터 이름과 MemberVO.java의 변수명이 같으면
			MemberVO m에 가입폼에서 입력한 회원정보가 저장되어 있다.
			-> 코드 라인을 줄이는 효과가 발생한다
		*/
		m.setMem_pwd(PwdChange.getPassWordToXEMD5String(m.getMem_pwd())); // 비번 암호화
		this.memberService.insertMember(m); // 회원저장
		/*
			문제.
			탈퇴 사유인 mem_delcont, 탈퇴 날짜인 mem_deldate만 빼고 나머지는 저장되게 만들기
			가입회원인 경우는 mem_state = 1, 탈퇴회원은 2
			mybatis 매퍼태그 유일 아이디명은 m_in으로 한다
		*/
		
		return new ModelAndView("redirect:/member_login");
	}
	
	// 비번찾기 공지창
	@GetMapping("/pwd_find")
	public ModelAndView pwd_find() {
		return new ModelAndView("member/pwd_find"); // 생성자 인자값으로 뷰페이지 경로
	} // pwd_find()
	
	// 비번 찾기 결과
	@RequestMapping("/pwd_find_ok")
	public ModelAndView pwd_find_ok(@RequestParam("pwd_id") String pwd_id, String pwd_name,
			HttpServletResponse response, MemberVO m) throws Exception {
		/*
			@RequestParam("pwd_id")의 뜻은 pwd_id 네임피라미터에 저장된 아이디값을 가져오는 것
			request.getParameter("pwd_id")와 같은 역할을 한다
		*/
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter(); // 출력스트림 out생성
		
		m.setMem_id(pwd_id); m.setMem_name(pwd_name);
		
		MemberVO pm = this.memberService.pwdMember(m); // 아이디와 회원이름을 기준으로 오라클로부터 회원정보를 검색
		
		if(pm == null) {
			out.println("<script>");
			out.println("alert('회원으로 검색되지 않습니다!\\n올바른 아이디와 회원이름을 입력하세요!')");
			out.println("history.go(-1);");
			out.println("</script>");
		} else {
			Random r = new Random();
			int pwd_random = r.nextInt(100000); // 0이상 10만미만 사이의 임의의 정수 숫자 난수를 발생
			String ran_pwd = Integer.toString(pwd_random); // 임시 정수숫자 비번을 문자열로 변경
			m.setMem_pwd(PwdChange.getPassWordToXEMD5String(ran_pwd)); // 임시 비번 암호화
			
			this.memberService.updateRanPwd(m);
			// 오라클 DB 비번을 암호화 된 임시비번으로 수정 -> 임시 비번으로 로그인 한 다음에 정보수정에서 정식비번으로 수정
			
			ModelAndView fm = new ModelAndView("member/pwd_find_ok");
			// 생성자 인자값으로 뷰페이지 경로를 설정
			fm.addObject("pwd_ran", ran_pwd); // pwd_ran 키이름에 암호화 되기 전 임시 비번으로 저장
			
			return fm;
		}
		
		return null;
	} // pwd_find_ok()
	
	// 가입회원인 경우는 mem_state=1일때 로그인 인증 처리(탈퇴회원은 mem_state=2라서 로그인 인증 불가)
	@PostMapping("/member_login_ok")
	public ModelAndView member_login_ok(String login_id, String login_pwd,
			HttpServletResponse response, HttpSession session) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		MemberVO m = this.memberService.loginCheck(login_id); // 아이디와 가입회원 1인 경우만 로그인 인증 처리한다
		
		if(m == null) {
			out.println("<script>");
			out.println("alert('가입 안된 회원입니다!');");
			out.println("location='member_login';");
			out.println("</script>");
		} else {
			if(!m.getMem_pwd().equals(PwdChange.getPassWordToXEMD5String(login_pwd))) {
				out.println("<script>");
				out.println("alert('비번이 다릅니다!');");
				out.println("history.go(-1);");
				out.println("</script>");
			} else {
				session.setAttribute("id", login_id); // 세션 id 키이름에 아이디를 저장
				
				return new ModelAndView("redirect:/member_login");
			}
		}
		
		return null;
	} // member_login_ok()
	
	// 로그아웃
	@RequestMapping("/member_logout")
	public ModelAndView member_logout(HttpServletResponse response, HttpSession session) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		session.invalidate(); // 세션만료 -> 로그아웃
		
		out.println("<script>");
		out.println("alert('로그아웃 되었습니다!');");
		out.println("location='member_login';");
		out.println("</script>");
		
		return null;
	} // member_logout()
	
	// 회원정보 수정
	@GetMapping("/member_edit")
	public ModelAndView member_edit(HttpServletResponse response, HttpSession session) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String id = (String)session.getAttribute("id"); // 세션 아이디를 구함
		
		if(isLogin(session, response) == true) { // == true는 생략가능함
			String[] phone = {"010", "011", "019"};
			String[] email = {"naver.com", "daum.net", "gmail.com", "nate.com", "직접입력"};
			
			MemberVO em = this.memberService.getMember(id); // 아이디에 해당하는 회원정보를 구함
			
			ModelAndView m = new ModelAndView();
			m.addObject("em", em);
			m.addObject("phone", phone);
			m.addObject("email", email);
			m.setViewName("member/member_Edit"); // 뷰페이지 경로와 파일명 설정
			
			return m;
		}
		
		return null;
	} // member_edit()
	
	// 회원정보 수정완료
	@PostMapping("/member_update_ok")
	public ModelAndView member_update_ok(MemberVO m, HttpServletResponse response, 
			HttpSession session) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String id = (String)session.getAttribute("id");
		
		if(isLogin(session, response)) {
			m.setMem_id(id);
			m.setMem_pwd(PwdChange.getPassWordToXEMD5String(m.getMem_pwd())); // 정식 비번 암호화
			
			this.memberService.updateMember(m); // 정보수정
			/*
				문제.
				아이디를 기준으로 비번, 회원이름, 우편번호, 주소, 폰번호, 전자우편까지 수정되게 만들어 본다
				mybatis 유일 아이디명은 medit_ok로 한다
			*/
			
			out.println("<script>");
			out.println("alert('정보 수정했습니다!');");
			out.println("location='member_edit';");
			out.println("</script>");
		}
		
		return null;
	} // member_update_ok()
	
	// 회원 탈퇴
	@RequestMapping("/member_del")
	public ModelAndView member_del(HttpServletResponse response, HttpSession session) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String id = (String)session.getAttribute("id");
		
		if(isLogin(session, response)) {
			MemberVO dm = this.memberService.getMember(id);
			
			ModelAndView m = new ModelAndView("member/member_DeL"); // 생성자 인자값으로 뷰페이지 경로 설정
			m.addObject("dm", dm);
			
			return m;
		}
		
		return null;
	}
	
	// 회원 탈퇴 완료
	@PostMapping("/member_del_ok")
	public ModelAndView member_del_ok(String del_pwd, String del_cont,
			HttpServletResponse response, HttpSession session) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String id = (String)session.getAttribute("id");
		
		if(isLogin(session, response)) {
			del_pwd = PwdChange.getPassWordToXEMD5String(del_pwd); // 비번 암호화
			MemberVO db_pwd = this.memberService.getMember(id);
			
			if(!db_pwd.getMem_pwd().equals(del_pwd)) {
				out.println("<script>");
				out.println("alert('비번이 다릅니다!');");
				out.println("history.back()");
				out.println("</script>");
			} else {
				MemberVO dm = new MemberVO();
				dm.setMem_id(id); dm.setMem_delcont(del_cont);
				
				this.memberService.delMem(dm); // 회원 탈퇴
				
				session.invalidate(); // 세션만료 -> 로그아웃
				
				out.println("<script>");
				out.println("alert('회원 탈퇴했습니다!');");
				out.println("location='member_login';");
				out.println("</script>");
			}
		}
		
		return null;
	}
	
	// 반복적인 코드를 하나로 줄이기
	public static boolean isLogin(HttpSession session, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String id = (String)session.getAttribute("id");
		
		if(id == null) {
			out.println("<script>");
			out.println("alert('다시 로그인 하세요!');");
			out.println("location='member_login';");
			out.println("</script>");
			
			return false;
		}
		
		return true;
	}
	
}
