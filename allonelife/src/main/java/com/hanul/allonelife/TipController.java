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
import exchange.GoodVO;
import item.PointhVO;
import member.MemberVO;
import tip.TipCommentVO;
import tip.TipPageVO;
import tip.TipServiceImpl;
import tip.TipVO;

@Controller
public class TipController {

	@Autowired
	private TipPageVO page;
	@Autowired
	private TipServiceImpl service;
	@Autowired
	private CommonService common;

	// �ۻ����� ����Ʈ -50
	@RequestMapping("/pointm.ti")
	public String itemt(MemberVO vo, PointhVO pvo, HttpSession ss) {
		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.tip_pointm_update(vo);
		pvo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		pvo.setPoint(((MemberVO) ss.getAttribute("login_info")).getPoint());
		service.tip_pointm_insert(pvo);

		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.tip_pointm_update(vo);
		
		return "redirect:list.ti";
	}

	// �۾��� ����Ʈ +50
	@RequestMapping("/point.ti")
	public String itemone(MemberVO vo, PointhVO pvo, HttpSession ss) {
		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.tip_point_update(vo);

		pvo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		pvo.setPoint(((MemberVO) ss.getAttribute("login_info")).getPoint());
		service.tip_point_insert(pvo);
		
		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.tip_point_update(vo);
		   
		return "redirect:list.ti";
	}

	// �� ���ƿ� ��û
	@ResponseBody
	@RequestMapping(value = "/good.ti", produces = "text/html; charset=utf-8")
	public String good(TipVO vo, GoodVO vog, HttpSession ss, int id) {
		StringBuffer msg = new StringBuffer("<script type='text/javascript'>");
		vog.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		vog.setBoardid(id);

		if (service.tip_good_select(vog) == true) {
			vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
			vo.setId(id);
			service.tip_good_update(vo);

			vog.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
			vog.setBoardid(id);
			service.tip_good_insert(vog);
			msg.append("alert('���ƿ並 �����̽��ϴ�.'); location='detail.ti?id='+" + id);
			msg.append("</script>");
		} else {
			msg.append("alert('�̹� ���ƿ並 �����̽��ϴ�.'); location='detail.ti?id='+" + id);
			msg.append("</script>");
		}
		return msg.toString();
	}

	
	// �� ��� ��������ó�� ��û
	@ResponseBody
	@RequestMapping(value = "/tip/comment/update", produces = "application/text; charset=utf-8")
	public String comment_update(@RequestBody TipCommentVO vo) {
		return service.tip_comment_update(vo) ? "����" : "����";
	}

	// �� ��� ����ó�� ��û
	@ResponseBody
	@RequestMapping("/tip/comment/delete/{id}")
	public void comment_delete(@PathVariable int id, TipVO vo) {
		vo.setId(id);
		service.tip_stepdown_update(vo);
		service.tip_comment_delete(id);
	}

	// �� ��۸�� ��û
	@RequestMapping("/tip/comment/{pid}")
	public String comment_list(Model model, @PathVariable int pid) {
		model.addAttribute("list", service.tip_comment_list(pid));
		model.addAttribute("crlf", "\r\n");
		model.addAttribute("lf", "\n");
		return "tip/comment/list";
	}

	// �� �������ó�� ��û
	@ResponseBody
	@RequestMapping("/tip/comment/insert")
	public boolean comment_insert(TipVO vo, HttpSession ss, int pid, String content) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("content", content);
		map.put("userid", ((MemberVO) ss.getAttribute("login_info")).getUserid());
		vo.setId(pid);
		service.tip_root_step_update(vo);
		return service.tip_comment_insert(map);
	}

	// ���� ����ó�� ��û
	@RequestMapping("/update.ti")
	public String update(TipVO vo, int delete, HttpSession ss, MultipartFile file, Model model) {

		TipVO old = service.tip_detail(vo.getId());
		String uuid = ss.getServletContext().getRealPath("resources") + old.getFilepath();

		if (file.getSize() > 0) {
			// ������ ÷���ϴ� ���
			vo.setFilename(file.getOriginalFilename());
			vo.setFilepath(common.fileUpload(file, ss, "tip"));
			// ���� ÷�ε� ������ �ٲ� ÷���ϴ� ���
			// ���� ÷�ε� ������ �����Ѵ�.
			File f = new File(uuid);
			if (f.exists())
				f.delete();
		} else {
			// ����÷������ �ʴ� ���
			if (delete != 1) {
				// 1.���� ÷�ε� ������ �״�� ����ϴ� ���
				vo.setFilename(old.getFilename());
				vo.setFilepath(old.getFilepath());

			} else {
				// 2.���� ÷�ε� ������ �����ϴ� ���
				// ���� ÷�ε� ������ �����Ѵ�.
				File f = new File(uuid);
				if (f.exists())
					f.delete();
			}
		}
		service.tip_update(vo);
		model.addAttribute("id", vo.getId());
		return "tip/redirect";
	}

	// ���� ����ȭ�� ��û
	@RequestMapping("/modify.ti")
	public String modify(int id, Model model) {
		model.addAttribute("vo", service.tip_detail(id));
		return "tip/modify";
	}

	// ���� ����ó�� ��û
	@RequestMapping("/delete.ti")
	public String delete(int id) {
		service.tip_delete(id);
		return "redirect:pointm.ti";
	}

	// ÷������ �ٿ�ε� ��û
	@ResponseBody
	@RequestMapping("/download.ti")
	public File download(int id, HttpSession ss, HttpServletResponse response) {
		TipVO vo = service.tip_detail(id);
		return common.fileDownload(vo.getFilename(), vo.getFilepath(), ss, response);
	}

	// ���� ��ȭ�� ��û
	@RequestMapping("/detail.ti")
	public String detail(int id, @RequestParam(defaultValue = "false") boolean read, Model model) {
		if (read) {
			service.tip_read(id);
			model.addAttribute("id", id);
			return "tip/redirect";
		} else {
			model.addAttribute("page", page);
			model.addAttribute("vo", service.tip_detail(id));
			model.addAttribute("crlf", "\r\n");
			return "tip/detail";
		}
	}

	// ���� ����ó�� ��û
	@RequestMapping("/insert.ti")
	public String insert(TipVO vo, HttpSession ss, MultipartFile file) {
		// ÷�������� �ִ� ���
		if (file.getSize() > 0) {
			vo.setFilename(file.getOriginalFilename());
			vo.setFilepath(common.fileUpload(file, ss, "tip"));
		}
		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.tip_insert(vo);
		return "redirect:point.ti";
	}

	// ���� �ۼ� ȭ�� ��û
	@RequestMapping("/new.ti")
	public String tip() {
		return "tip/new";
	}

	// ���� ���ȭ�� ��û
	@RequestMapping("/list.ti")
	public String list(Model model, @RequestParam(defaultValue = "1") int curPage,
			@RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "") String keyword) {

		page.setCurPage(curPage);
		page.setSearch(search);
		page.setKeyword(keyword);

		model.addAttribute("page", service.tip_list(page));
		return "tip/list";
	}

}