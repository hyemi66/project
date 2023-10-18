package net.daum.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oreilly.servlet.MultipartRequest;

import net.daum.service.AdminBbsService;
import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Controller
public class AdminBbsController {
	
	@Autowired
	private AdminBbsService adminBbsService;
	
	// 관리자 자료실 목록
	@RequestMapping("/admin_bbs_list")
	public ModelAndView admin_bbs_list(BbsVO b, HttpServletResponse response,
			HttpServletRequest request, HttpSession session, PageVO p) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String admin_id = (String)session.getAttribute("admin_id"); // 관리자 세션 아이디를 구함
		if(admin_id == null) {
			out.println("<script>");
			out.println("alert('관리자로 로그인 하세요!')");
			out.println("location='admin_login';");
			out.println("</script>");
		} else {
			int page=1; // 쪽번호
			int limit=7; // 한페이지에 보여지는 목록개수
			
			if(request.getParameter("page") != null) {
				page=Integer.parseInt(request.getParameter("page"));         
			}
			
			String find_name=request.getParameter("find_name"); // 검색어
			String find_field=request.getParameter("find_field"); // 검색 필드
			p.setFind_field(find_field);
			p.setFind_name("%"+find_name+"%");
			// %는 sql문에서 검색 와일드 카드 문자로서 하나이상의 임의의 모르는 문자와 매핑 대응
			// 하나의 모르는 문자와 매핑 대응하는 와일드 카드문자는 _
			
			int listcount=this.adminBbsService.getListCount(p);
			// 검색전 전체 레코드 개수 또는 검색후 레코드 개수
			// System.out.println("총 게시물수 : " + listcount + "개");
			
			p.setStartrow((page - 1) * 7 + 1); // 시작행번호
			p.setEndrow(p.getStartrow() + limit - 1); // 끝행번호
			
			List<BbsVO> blist=this.adminBbsService.getAdminBbsList(p);//검색 전후 목록
			
			// 총페이지수
			int maxpage=(int)((double)listcount/limit + 0.95);
			// 현재 페이지에 보여질 시작페이지 수(1,11,21)
			int startpage=(((int)((double)page/10+0.9))-1)*10+1;
			// 현재 페이지에 보여줄 마지막 페이지 수(10,20,30)
			int endpage=maxpage;
			
			if(endpage > startpage+10-1) endpage=startpage+10-1;
			
			ModelAndView listM=new ModelAndView();
			
			listM.addObject("blist",blist); // blist 키이름에 값 저장
			listM.addObject("page",page);
			listM.addObject("startpage",startpage);
			listM.addObject("endpage",endpage);
			listM.addObject("maxpage",maxpage);
			listM.addObject("listcount",listcount);   
			listM.addObject("find_field",find_field);
			listM.addObject("find_name", find_name);
			
			listM.setViewName("admin/admin_bbs_list");//뷰페이지 경로
			
			return listM;
		}
		
		return null;
	} // admin_bbs_list()
	
	// 관리자 자료실 글쓰기
	@GetMapping("/admin_bbs_write")
	public ModelAndView admin_bbs_write(HttpServletResponse response, HttpSession session, 
			HttpServletRequest request) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		
		if(isAdminLogin(session, response)) { // true인 경우 실행, == true가 생략됨
			int page = 1; 
			
			if(request.getParameter("page") != null) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			
			ModelAndView wm = new ModelAndView("admin/admin_bbs_write");
			wm.addObject("page", page); // 페이징에서 책갈피 기능으로 추가
			
			return wm;
		}
		
		return null;
	} // admin_bbs_write()
	
	// 관리자 자료실 글쓰기 저장
	@PostMapping("/admin_bbs_write_ok")
	public ModelAndView admin_bbs_write_ok(HttpSession session, HttpServletResponse response,
			HttpServletRequest request, BbsVO b) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		
		if(isAdminLogin(session, response)) {
			String saveFolder=request.getRealPath("upload"); // 이진파일 업로드 서버경로
			int fileSize=5*1024*1024; // 이진파일 업로드 최대크기
			MultipartRequest multi = null; // 이진파일을 받을 참조변수
			
			multi=new MultipartRequest(request,saveFolder, fileSize,"UTF-8");
			String bbs_name = multi.getParameter("bbs_name");
			String bbs_title = multi.getParameter("bbs_title");
			String bbs_pwd = multi.getParameter("bbs_pwd");
			String bbs_cont = multi.getParameter("bbs_cont");
			
			File UpFile = multi.getFile("bbs_file"); // 첨부한 이진파일을 받아옴.
			if(UpFile != null) { // 첨부한 이진파일이 있다면
				String fileName = UpFile.getName(); // 첨부한 파일명
				Calendar c = Calendar.getInstance(); // 칼렌더는 추상
				// 클래스로 new로 객체 생성을 못함. 년월일 시분초 값을 반환
				int year = c.get(Calendar.YEAR); // 년도값
				int month = c.get(Calendar.MONTH)+1; // 월값. +1을 한 이유가 1월이 0으로 반환 되기 때문이다.
				int date=c.get(Calendar.DATE);//일값
				
				String homedir=saveFolder+"/"+year+"-"+month+"-"+date; // 오늘 날짜 폴더 경로 저장
				
				File path1=new File(homedir);
				if(!(path1.exists())) {
					path1.mkdir(); // 오늘날짜 폴더경로를 생성
				}
				
				Random r = new Random();
				int random = r.nextInt(100000000);
			
				/* 첨부 파일 확장자 구함 */
				int index = fileName.lastIndexOf("."); // 마침표 위치 번호를 구함
				String fileExtension = fileName.substring(index+1); // 마침표 이후부터 마지막 문자까지 구함.첨부파일 확장자를 구함
				String refileName = "bbs" + year + month + date + random + "." + fileExtension; // 새로운 이진파일명 저장
				String fileDBName="/"+year+"-"+month+"-"+date+"/"+ refileName; // 디비에 저장될 레코드값
				UpFile.renameTo(new File(homedir + "/" + refileName)); // 바뀌어진 이진파일로 업로드
				
				b.setBbs_file(fileDBName);
			} else { // mybatis에서는 컬럼에 null을 insert하지 못함.
				String fileDBName = "";
				b.setBbs_file(fileDBName); // 첨부하지 않았을때 빈공백을 저장
			}
			
			b.setBbs_name(bbs_name); b.setBbs_title(bbs_title);
			b.setBbs_pwd(bbs_pwd); b.setBbs_cont(bbs_cont);
			
			this.adminBbsService.adminInsertBbs(b); // 자료실 저장
			
			return new ModelAndView("redirect:/admin_bbs_list");
		}
		
		return null;
	} // admin_bbs_write_ok()
	
	// 관리자 자료실 상세정보 + 수정폼
	@RequestMapping("/admin_bbs_cont")
	public ModelAndView admin_bbs_cont(int no, int page, String state,
			HttpServletResponse response, HttpSession session) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		
		if(isAdminLogin(session, response)) {
			BbsVO bc = this.adminBbsService.getAdminBbsCont(no);
			String bbs_cont = bc.getBbs_cont().replace("\n", "<br>");
			// textarea에서 엔터키를 친 부분을 줄바꿈 처리해서 내용보기 한다
			
			ModelAndView cm = new ModelAndView();
			cm.addObject("b", bc);
			cm.addObject("bbs_cont", bbs_cont);
			cm.addObject("page", page);
			
			if(state.equals("cont")) { // 관리자 자료실 상세정보 보기
				cm.setViewName("./admin/admin_bbs_cont");
			} else if(state.equals("edit")) { // 관리자 자료실 수정폼
				cm.setViewName("./admin/admin_bbs_edit");
			}
			
			return cm;
		}
		
		return null;
	} // admin_bbs_cont()
	
	// 관리자 자료실 수정 완료
	@PostMapping("/admin_bbs_edit_ok")
	public ModelAndView admin_bbs_edit_ok(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, BbsVO b) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		
		if(isAdminLogin(session, response)) {
			String saveFolder = request.getRealPath("upload");
			int fileSize = 5*1024*1024;
			
			MultipartRequest multi = null;
			multi = new MultipartRequest(request,saveFolder,fileSize,"UTF-8");
			
			int bbs_no = Integer.parseInt(multi.getParameter("bbs_no"));
			int page = 1;
			if(multi.getParameter("page") != null) {
				page = Integer.parseInt(multi.getParameter("page"));
			}
			String bbs_name = multi.getParameter("bbs_name");
			String bbs_title = multi.getParameter("bbs_title");
			String bbs_cont = multi.getParameter("bbs_cont");
			
			BbsVO db_File = this.adminBbsService.getAdminBbsCont(bbs_no); // DB로 부터 기존 첨부파일명을 구함
			File upFile = multi.getFile("bbs_file");
			
			if(upFile != null) { // 수정 첨부된 파일이 있는 경우 실행
				String fileName = upFile.getName(); // 수정 첨부된 파일명을 구함
				File delFile = new File(saveFolder+db_File.getBbs_file()); // 삭제할 파일 객체 생성
				
				if(delFile.exists()) {
					delFile.delete(); // 기존첨부파일을 삭제            
				}
				
				Calendar cal=Calendar.getInstance();
				int year = cal.get(Calendar.YEAR); // 년도값
				int month = cal.get(Calendar.MONTH)+1; // 월값
				int date = cal.get(Calendar.DATE); // 일값
				
				String homedir = saveFolder + "/" + year + "-" + month + "-" + date;
				File path01 = new File(homedir);
				
				if(!(path01.exists())) { // 오늘 날짜 폴더 경로가 없으면
					path01.mkdir(); // 오늘 날짜 폴더를 생성
				}
				
				Random r = new Random();
				int random = r.nextInt(100000000);
				
				/* 첨부 파일 확장자를 구함 */
				int index = fileName.lastIndexOf(".");
				String fileExtendsion = fileName.substring(index+1); // .이후 부터 마지막 문자 까지 구함.즉 첨부파일 확장자를 구함.
				String refileName = "bbs" + year + month + date + random + "." + fileExtendsion; // 변경된 첨부파일명
				String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName; // db에 저장될 레코드 값
				upFile.renameTo(new File(homedir+"/"+refileName)); // 오늘 날짜 폴더 경로에 변경된 파일로 실제 업로드
				b.setBbs_file(fileDBName);
			} else { // 수정 펌부되지 않았을 때 실행
				String fileDBName = "";
				
				if(db_File.getBbs_file() != null) { // 기존 첨부파일이 있는 경우
					b.setBbs_file(db_File.getBbs_file());
				} else {
					b.setBbs_file(fileDBName);
				}
			} // if else
			
			b.setBbs_no(bbs_no); b.setBbs_name(bbs_name);
			b.setBbs_title(bbs_title); b.setBbs_cont(bbs_cont);
			
			this.adminBbsService.adminUpdateBbs(b); // 관리자 자료실 수정
			/*
				문제)
				mybatis로 번호를 기준으로 글쓴이, 글제목, 글내용, 첨부파일을 수정되게 한다(개발자 테스트까지 해보기)
			*/
			
			ModelAndView em = new ModelAndView("redirect:/admin_bbs_list?page=" + page);
			/*
				ModelAndView 스프링 API 생성자 인자값으로 2가지가 들어간다
				1. 화면에 보이는 뷰페이지 경로와 파일명
				2. redirect:/매핑주소 경로 -> 새로운 매핑주소로 이동
			*/
			
			return em; // admin_bbs_list?page=쪽번호 가 get으로 전달된다.
		}
		
		return null;
	} // admin_bbs_edit_ok()
	
	// 관리자 자료실 삭제
	@GetMapping("/admin_bbs_del")
	public ModelAndView admin_bbs_del(int no, int page, HttpServletResponse response,
			HttpSession session, HttpServletRequest request) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		
		if(isAdminLogin(session, response)) {
			String saveFolder = request.getRealPath("upload");
			BbsVO db_file = this.adminBbsService.getAdminBbsCont(no);
			
			if(db_file.getBbs_file() != null) { // 기존 첨부 파일이 있는 경우
				File delFile = new File(saveFolder + db_file.getBbs_file()); // 삭제할 파일 객체 생성
				delFile.delete(); // 폴더는 그대로, 기존 첨부 파일만 삭제
			}
			
			this.adminBbsService.adminBbsDel(no); // 번호 기준으로 삭제
			
			ModelAndView dm = new ModelAndView();
			dm.setViewName("redirect:/admin_bbs_list");
			dm.addObject("page", page);
			
			return dm;
			// admin_bbs_list?page=쪽번호로 주소창에 노출되는 get방식으로 관리자 목록보기 매핑주소로 이동(새로운 매핑주소로 이동)
		}
		
		return null;
	} // admin_bbs_del()
	
	// 반복적인 관리자 로그인을 안하기 위한 코드 추가
	public static boolean isAdminLogin(HttpSession session, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String admin_id = (String)session.getAttribute("admin_id"); // 관리자 세션 아이디를 구함
		if(admin_id == null) { // 관리자 로그아웃 되었을 때 실행
			out.println("<script>");
			out.println("alert('관리자로 로그인 하세요!');");
			out.println("location='admin_login';");
			out.println("</script>");
			
			return false;
		}
		
		return true;
	} // isAdminLogin()
	
} // AdminBbsContorller.java
