package net.daum.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oreilly.servlet.MultipartRequest;

import net.daum.service.BbsService;
import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Controller // @Controller 애노테이션을 설정함으로써 스프링에서 컨트롤러로 인식
public class BbsController {
	
	@Autowired
	private BbsService bbsService;
	
	// 자료실 글쓰기 폼
	@GetMapping("/bbs_write") // get으로 접근하는 매핑주소 처리, bbs_write매핑주소 등록
	public ModelAndView bbs_write(HttpServletRequest request) {
		// 페이징에서 내가 본 쪽번호로 바로 이동하는 기능 -> 책갈피 기능
		int page = 1;
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
			// get으로 전달된 쪽번호가 있는 경우 쪽번호를 정수숫자로 바꿔서 저장
		}
		
		ModelAndView wm = new ModelAndView();
		wm.addObject("page", page); // 페이징 책갈피 기능때문에 page키이름에 쪽번호 저장
		wm.setViewName("bbs/bbs_write"); // 뷰페이지 경로(뷰리졸브 경로) -> /WEB-INF/views/bbs/bbs_write.jsp
		
		return wm;
	} // bbs_write()
	
	@PostMapping("/bbs_write_ok") // post로 접근하는 매핑주소 처리
	public String bbs_write_ok(BbsVO b, HttpServletRequest request) throws Exception {
		String saveFolder=request.getRealPath("upload"); //이진 파일 업로드 서버 경로
		int fileSize=5*1024*1024; //이진파일 업로드 최대크기
		MultipartRequest multi=null; //이진파일을 가져올 참조변수
		
		multi=new MultipartRequest(request,saveFolder,fileSize,"UTF-8");   
		
		String bbs_name=multi.getParameter("bbs_name");
		String bbs_title=multi.getParameter("bbs_title");
		String bbs_pwd=multi.getParameter("bbs_pwd");
		String bbs_cont=multi.getParameter("bbs_cont");
		
		File upFile = multi.getFile("bbs_file");//첨부한 이진파일을 가져온다.
		
		if(upFile != null) {//첨부한 이진파일이 있는 경우 실행
			String fileName=upFile.getName();//첨부한 파일명
			Calendar cal=Calendar.getInstance();//칼렌더는 추상클래스로 new로 객체 생성을 못함. 년월일 시분초 값을 반환
			int year=cal.get(Calendar.YEAR);//년도값
			int month=cal.get(Calendar.MONTH)+1;//월값, +1을 한 이유는 1월이 0으로 반환 되기 때문에
			int date=cal.get(Calendar.DATE);//일값
			
			String homedir=saveFolder+"/"+year+"-"+month+"-"+date;//오늘 날짜 폴더 경로 저장
			File path01=new File(homedir);
			
			if(!(path01.exists())){
				path01.mkdir();//오늘날짜 폴더 생성
			}
			Random r=new Random();//난수를 발생시키는 클래스
			int random=r.nextInt(100000000);//0이상 1억 미만의 정수 숫자 난수 발생
		 
			/*첨부 파일 확장자를 구함*/
			int index=fileName.lastIndexOf(".");//마침표를 맨 오른쪽부터 찾아서 가장 먼저 나오는 .의 위치번호를 맨 왼쪽부터 카운터 해서 반환
			//첫문자는 0부터 시작
			String fileExtendsion=fileName.substring(index+1);//마침표 이후부터 마지막 문자까지 구함.즉 첨부파일 확장자를 구함.
			String refileName="bbs"+year+month+date+random+"."+fileExtendsion;//새로운 파일명 저장
			String fileDBName="/"+year+"-"+month+"-"+date+"/"+refileName;//데이터베이스에 저장될 레코드값
			upFile.renameTo(new File(homedir+"/"+refileName));//생성된 폴더에 변경된 파일명으로 실제 업로드
			
			b.setBbs_file(fileDBName);
		} else {//첨부파일이 없는 경우
			String fileDBName="";
			b.setBbs_file(fileDBName);
		}
		b.setBbs_name(bbs_name); b.setBbs_title(bbs_title);
		b.setBbs_pwd(bbs_pwd); b.setBbs_cont(bbs_cont);
		
		this.bbsService.insertBbs(b);//자료실 저장
		
		return "redirect:/bbs_list";
	} // bbs_write_ok()
	
	// 페이징과 검색기능이 되는 자료실 목록
	@RequestMapping("/bbs_list")
	public ModelAndView bbs_list(HttpServletRequest request, BbsVO b, PageVO p) {
		
		int page=1;
        int limit=10; // 한 페이지에 보여지는 목록 개수
        if(request.getParameter("page") != null) {
        	page=Integer.parseInt(request.getParameter("page")); // 페이지 번호를 정수숫자로 변경해서 저장         
        }
        
        // 검색과 관련된 부분
        String find_name = request.getParameter("find_name"); // 검색어
        String find_field = request.getParameter("find_field"); // 검색 필드
        p.setFind_name("%" + find_name + "%"); // sql문에서 %와일드 카드 문자는 하나이상의 임의의 모르는 문자와 매핑 대응
        p.setFind_field(find_field);
        
        int totalCount=this.bbsService.getRowCount(p);
        // 검색 전 총 레코드 개수,검색 후 레코드 개수
        
        p.setStartrow((page-1)*10+1); // 시작행 번호
        p.setEndrow(p.getStartrow()+limit-1); // 끝행 번호
        
        List<BbsVO> blist=this.bbsService.getBbsList(p); // 검색 전후 목록
        
        // 총 페이지수
        int maxpage=(int)((double)totalCount/limit+0.95);
        // 시작페이지(1,11,21 ..)
        int startpage=(((int)((double)page/10+0.9))-1)*10+1;
        // 현재 페이지에 보여질 마지막 페이지(10,20 ..)
        int endpage=maxpage;
        if(endpage>startpage+10-1) endpage=startpage+10-1;
		
		ModelAndView listM = new ModelAndView("./bbs/bbs_list"); // 생성자 인자값으로 뷰페이지
		// 경로 설정 -> /WEB-INF/views/bbs/bbs_list.jsp
		
		listM.addObject("blist",blist); // blist문자열 속성 키이름에 자료실 목록을 저장
        listM.addObject("page",page); // 쪽번호
        listM.addObject("startpage",startpage); // 시작페이지
        listM.addObject("endpage",endpage); // 마지막 페이지
        listM.addObject("maxpage",maxpage); // 최대 페이지
        listM.addObject("listcount",totalCount); // 검색전후 레코드 개수
        listM.addObject("find_field", find_field); // 검색 필드
        listM.addObject("find_name", find_name); // 검색어
		
		return listM;
	} // bbs_list()
	
	// 자료실 내용보기 + 답변폼 + 수정폼 + 삭제폼
	@RequestMapping("/bbs_cont")
	public ModelAndView bbs_cont(int bbs_no, String state, int page, BbsVO b) {
		if(state.equals("cont")) { // 내용보기일때만 조회수 증가시킴
			b = this.bbsService.getBbsCont(bbs_no);
		} else { // 답변폼, 수정폼, 삭제폼일때는 조회수 증가 안시킴
			b = this.bbsService.getBbsCont2(bbs_no);
		}
		
		String bbs_cont = b.getBbs_cont().replace("\n", "<br>");
		// textarea 입력박스에서 엔터키를 친 부분 \n을 <br>태그로 변경해서 웹 상에서 내용을 볼 때 줄바꿈해서 보여진다
		
		ModelAndView cm = new ModelAndView();
		cm.addObject("page", page); // 페이징에서 책갈피 기능 때문에 쪽번호 저장
		cm.addObject("b", b);
		cm.addObject("bbs_cont", bbs_cont);
		
		if(state.equals("cont")) { // 내용보기일때 실행할 뷰페이지 경로
			cm.setViewName("./bbs/bbs_cont");
			// 뷰리졸브 경로 : /WEB-INF/views/bbs/bbs_cont.jsp
		} else if(state.equals("reply")) { // 답변폼일때
			cm.setViewName("./bbs/bbs_reply");
		} else if(state.equals("edit")) { // 자료실 수정폼일때
			cm.setViewName("./bbs/bbs_edit");
		} else if(state.equals("del")) { // 삭제폼일때
			cm.setViewName("./bbs/bbs_del");
			/*
				setViewName() 메소드 인자값에 redirect:/가 들어가면 새로운 매핑주소가 들어간다
				여기서는 뷰페이지 경로가 들어갔다
			*/
		}
		
		return cm;
	} // bbs_cont()
	
	// 답변저장
	@PostMapping("/bbs_reply_ok")
	public String bbs_reply_ok(BbsVO b, int page) {
		/*
			BbsVO b라고 하면 bbs_reply.jsp의 네임피라미터 이름과 BbsVO.java의 변수명이 일치하면 b객체에
			bbs_reply.jsp에서 넘어온 값이 저장되어 있다. 하지만 page는 변수로 정의 안되어 있기 때문에 b객체에
			값이 없어서 int page로 별도로 가져와야 한다
		*/
		
		this.bbsService.replyBbs(b); // 답변 레벨 업과 답변 저장
		
		return "redirect:/bbs_list?page=" + page; // 목록 보기로 이동
	} // bbs_reply_ok()
	
	// 자료실 수정
	@RequestMapping(value="/bbs_edit_ok", method=RequestMethod.POST)
	public ModelAndView bbs_edit_ok(HttpServletRequest request, HttpServletResponse response, BbsVO b) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		// 웹브라우저로 출력되는 문자와 태그, 언어코딩 타입을 설정
		PrintWriter out = response.getWriter(); // 출력스트림 out생성
		String saveFolder = request.getRealPath("upload"); // 수정 첨부되는 이진파일 업로드 될 실제 경로를 구함
		int fileSize = 5 * 1024 * 1024; // 이진파일 업로드 최대크기
		MultipartRequest multi = null; // 첨부된 파일을 받을 참조변수
		
		multi = new MultipartRequest(request, saveFolder, fileSize, "UTF-8");
		
		int bbs_no = Integer.parseInt(multi.getParameter("bbs_no"));
		/*
			히든으로 전달된 자료실 번호를 정수 숫자로 변경해서 저장,
			form태그내에 enctype="multipart/form-data"가 지정되어 있으면 request.getParameter()로 못받아 온다.
			multi로 받아와야 한다
		*/
		int page = 1;
		
		if(multi.getParameter("page") != null) {
			page = Integer.parseInt(multi.getParameter("page"));
		}
		
		String bbs_name = multi.getParameter("bbs_name");
		String bbs_title = multi.getParameter("bbs_title");
		String bbs_pwd = multi.getParameter("bbs_pwd");
		String bbs_cont = multi.getParameter("bbs_cont");
		
		BbsVO db_pwd = this.bbsService.getBbsCont2(bbs_no); // 조회수가 증가되지 않는 것으로해서 오라클로부터 비번을 가져옴
		
		if(!db_pwd.getBbs_pwd().equals(bbs_pwd)) {
			out.println("<script>");
			out.println("alert('비번이 다릅니다!');");
			out.println("history.back();");
			out.println("</script>");
		} else {
			File upFile = multi.getFile("bbs_file"); // 수정 첨부된 파일을 가져옴
			if(upFile != null) { // 수정 첨부된 파일이 있는 경우
				String fileName = upFile.getName(); // 첨부된 파일명을 구함
				File delFile = new File(saveFolder + db_pwd.getBbs_file()); // 삭제할 파일객체 생성
				if(delFile.exists()) { // 기존 파일이 있다면
					delFile.delete(); // 기존 첨부파일 삭제
				}
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH) + 1;
				int date = c.get(Calendar.DATE);
				
				String homedir = saveFolder + "/" + year + "-" + month + "-" + date;
				File path01 = new File(homedir);
				if(!(path01.exists())) {
					path01.mkdir(); // 오늘 날짜 폴더 생성
				}
				
				Random r = new Random();
				int random = r.nextInt(100000000);
				
				/* 첨부파일 확장자를 구함 */
				int index = fileName.lastIndexOf("."); // 마침표 위치번호를 구함
				String fileExtendsion = fileName.substring(index + 1); // 첨부파일에서 확장자만 구함
				
				String refileName = "bbs" + year + month + date + random + "." + fileExtendsion; // 새로운 파일명 구함
				String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName; // DB에 저장될 레코드 값
				
				upFile.renameTo(new File(homedir + "/" + refileName)); // 실제 업로드
				
				b.setBbs_file(fileDBName);
			} else { // 수정 첨부파일이 없는 경우
				String fileDBName = "";
				if(db_pwd.getBbs_file() != null) { // 기존 첨부파일이 있는 경우
					b.setBbs_file(db_pwd.getBbs_file());
				} else { // 기존 파일이 없는 경우
					b.setBbs_file(fileDBName);
				}
			} // if else
			
			b.setBbs_name(bbs_name); b.setBbs_title(bbs_title);
			b.setBbs_cont(bbs_cont); b.setBbs_no(bbs_no);
			
			this.bbsService.editBbs(b); // 자료실 수정
			
			ModelAndView em = new ModelAndView("redirect:/bbs_cont");
			em.addObject("bbs_no", bbs_no);
			em.addObject("page", page);
			em.addObject("state", "cont");
			
			return em;
			/*
				브라우저 주소창에 다음과 같이 실행된다.
				bbs_cont?bbs_no=번호&page=쪽번호&state=cont 3개의 피라미터 값이 get방식으로 전달된다
				=> 쿼리스트링 방식
			*/
		} // if else
		
		return null;
	} // bbs_edit_ok()
	
	// 자료실 삭제
	@RequestMapping("/bbs_del_ok") // get or post로 전달되는 매핑주소를 처리
	public ModelAndView bbs_del_ok(int bbs_no, int page, @RequestParam("del_pwd") String del_pwd,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
		/*
			@RequestParam("del_pwd") 스프링의 애노테이션
			-> request.getParameter("del_pwd")와 같은 기능
		*/
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String delFolder = request.getRealPath("upload");
		
		BbsVO db_pwd = this.bbsService.getBbsCont2(bbs_no);
		if(!db_pwd.getBbs_pwd().equals(del_pwd)) { // 비번이 다르면
			out.println("<script>");
			out.println("alert('비번이 다릅니다!');");
			out.println("history.back();");
			out.println("</script>");
		} else { // 비번이 같으면
			this.bbsService.delBbs(bbs_no); // 자료실 삭제
			
			if(db_pwd.getBbs_file() != null) { // 기존 첨부파일 존재할때
				File delFile = new File(delFolder + db_pwd.getBbs_file()); // 삭제할 파일 객체 생성
				delFile.delete(); // 폴더는 삭제 안되고, 기존 파일만 삭제됨
			}
			
			ModelAndView dm = new ModelAndView();
			dm.setViewName("redirect:/bbs_list?page=" + page);
			
			return dm;
		}
		
		return null;
	}
	
}
