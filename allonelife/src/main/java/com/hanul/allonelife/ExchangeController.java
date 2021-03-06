package com.hanul.allonelife;

import java.io.File;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import common.CommonService;
import exchange.ExchangeCommentVO;
import exchange.ExchangePageVO;
import exchange.ExchangeServiceImpl;
import exchange.ExchangeVO;
import exchange.GoodVO;
import item.PointhVO;
import member.MemberVO;

@Controller
public class ExchangeController {

	@Autowired
	private ExchangePageVO page;
	@Autowired
	private ExchangeServiceImpl service;
	@Autowired
	private CommonService common;

	
	// 글삭제시 포인트 -50
	@RequestMapping("/pointm.ex")
	public String itemt(MemberVO vo, PointhVO pvo, HttpSession ss) {
		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.exchange_pointm_update(vo);

		pvo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		pvo.setPoint(((MemberVO) ss.getAttribute("login_info")).getPoint());
		service.exchange_pointm_insert(pvo);

		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.exchange_pointm_update(vo);
		
		return "redirect:list.ex";
	}

	// 글쓰면 포인트 +50
	@RequestMapping("/point.new")
	public String itemone(MemberVO vo, PointhVO pvo, HttpSession ss) {
		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.exchange_point_update(vo);

		pvo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		pvo.setPoint(((MemberVO) ss.getAttribute("login_info")).getPoint());
		service.exchange_point_insert(pvo);
		
		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.exchange_point_update(vo);
		   
		return "redirect:list.ex";
	}

	// 물물교환 좋아요 요청
	@ResponseBody
	@RequestMapping(value = "/good.ex", produces = "text/html; charset=utf-8")
	public String good(ExchangeVO vo, GoodVO vog, HttpSession ss, int id) {
		StringBuffer msg = new StringBuffer("<script type='text/javascript'>");
		vog.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		vog.setBoardid(id);

		if (service.exchange_good_select(vog) == true) {
			vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
			vo.setId(id);
			service.exchange_good_update(vo);

			vog.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
			vog.setBoardid(id);
			service.exchange_good_insert(vog);
			msg.append("alert('좋아요를 누르셨습니다.'); location='detail.ex?id='+" + id);
			msg.append("</script>");
		} else {
			msg.append("alert('이미 좋아요를 누르셨습니다.'); location='detail.ex?id='+" + id);
			msg.append("</script>");
		}
		return msg.toString();
	}

	// 물물교환안내 댓글 변경저장처리 요청
	@ResponseBody
	@RequestMapping(value = "/exchange/comment/update", produces = "application/text; charset=utf-8")
	public String comment_update(@RequestBody ExchangeCommentVO vo) {
		return service.exchange_comment_update(vo) ? "성공" : "실패";
	}

	// 물물교환안내 댓글 삭제처리 요청
	@ResponseBody
	@RequestMapping("/exchange/comment/delete/{id}")
	public void comment_delete(@PathVariable int id, ExchangeVO vo) {
		vo.setId(id);
		service.exchange_stepdown_update(vo);
		service.exchange_comment_delete(id);
	}

	// 물물교환안내 댓글목록 요청
	@RequestMapping("/exchange/comment/{pid}")
	public String comment_list(Model model, @PathVariable int pid) {
		model.addAttribute("list", service.exchange_comment_list(pid));
		model.addAttribute("crlf", "\r\n");
		model.addAttribute("lf", "\n");
		return "exchange/comment/list";
	}

	// 물물교환안내 댓글저장처리 요청
	@ResponseBody
	@RequestMapping("/exchange/comment/insert")
	public boolean comment_insert(ExchangeVO vo, HttpSession ss, int pid, String content) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("content", content);
		map.put("userid", ((MemberVO) ss.getAttribute("login_info")).getUserid());
		vo.setId(pid);
		service.exchange_root_step_update(vo);
		return service.exchange_comment_insert(map);
	}

	// 물물교환글 수정처리 요청
	@RequestMapping("/update.ex")
	public String update(ExchangeVO vo, int delete, HttpSession ss, MultipartFile file, Model model) {

		ExchangeVO old = service.exchange_detail(vo.getId());
		String uuid = ss.getServletContext().getRealPath("resources") + old.getFilepath();

		if (file.getSize() > 0) {
			// 파일을 첨부하는 경우
			vo.setFilename(file.getOriginalFilename());
			vo.setFilepath(common.fileUpload(file, ss, "exchange"));
			// 원래 첨부된 파일을 바꿔 첨부하는 경우
			// 원래 첨부된 파일을 삭제한다.
			File f = new File(uuid);
			if (f.exists())
				f.delete();
		} else {
			// 파일첨부하지 않는 경우
			if (delete != 1) {
				// 1.원래 첨부된 파일을 그대로 사용하는 경우
				vo.setFilename(old.getFilename());
				vo.setFilepath(old.getFilepath());

			} else {
				// 2.원래 첨부된 파일을 삭제하는 경우
				// 원래 첨부된 파일을 삭제한다.
				File f = new File(uuid);
				if (f.exists())
					f.delete();
			}
		}
		service.exchange_update(vo);
		model.addAttribute("id", vo.getId());
		return "exchange/redirect";
	}

	// 물물교환글 수정화면 요청
	@RequestMapping("/modify.ex")
	public String modify(int id, Model model) {
		model.addAttribute("vo", service.exchange_detail(id));
		return "exchange/modify";
	}

	// 물물교환글 삭제처리 요청
	@RequestMapping("/delete.ex")
	public String delete(int id) {
		service.exchange_delete(id);
		return "redirect:pointm.ex";
	}

	// 첨부파일 다운로드 요청
	@ResponseBody
	@RequestMapping("/download.ex")
	public File download(int id, HttpSession ss, HttpServletResponse response) {
		ExchangeVO vo = service.exchange_detail(id);
		return common.fileDownload(vo.getFilename(), vo.getFilepath(), ss, response);
	}

	// 물물교환 상세화면 요청
	@RequestMapping("/detail.ex")
	public String detail(int id, @RequestParam(defaultValue = "false") boolean read, Model model) {
		if (read) {
			service.exchange_read(id);
			model.addAttribute("id", id);
			return "exchange/redirect";
		} else {
			model.addAttribute("page", page);
			model.addAttribute("vo", service.exchange_detail(id));
			model.addAttribute("crlf", "\r\n");
			return "exchange/detail";
		}
	}

	// 물물교환안내 저장처리 요청
	@RequestMapping("/insert.ex")
	public String insert(ExchangeVO vo, HttpSession ss, MultipartFile file) {
		// 첨부파일이 있는 경우
		if (file.getSize() > 0) {
			vo.setFilename(file.getOriginalFilename());
			vo.setFilepath(common.fileUpload(file, ss, "exchange"));
		}
		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.exchange_insert(vo);
		return "redirect:point.new";
	}

	// 물물교환작성 화면 요청
	@RequestMapping("/new.ex")
	public String exchange() {
		return "exchange/new";
	}

	// 물물교환목록화면 요청
	@RequestMapping("/list.ex")
	public String list(Model model, @RequestParam(defaultValue = "1") int curPage,
			@RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "") String keyword) {

		page.setCurPage(curPage);
		page.setSearch(search);
		page.setKeyword(keyword);

		model.addAttribute("page", service.exchange_list(page));
		return "exchange/list";
	}

}
