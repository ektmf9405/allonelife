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
import freeboard.FreeBoardCommentVO;
import freeboard.FreeBoardPageVO;
import freeboard.FreeBoardServiceImpl;
import freeboard.FreeBoardVO;
import member.MemberVO;
import recipe.RecipeVO;

@Controller
public class FreeBoardController {
	@Autowired
	private FreeBoardPageVO page;
	@Autowired
	private FreeBoardServiceImpl service;
	@Autowired
	private CommonService common;

	// �����Խ��� ���ƿ�
	@ResponseBody
	@RequestMapping(value = "/good.fb", produces = "text/html; charset=utf-8")
	public String good(FreeBoardVO vo, GoodVO vog, HttpSession ss, int id) {
		StringBuffer msg = new StringBuffer("<script type='text/javascript'>");
		vog.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		vog.setBoardid(id);

		if (service.board_good_select(vog) == true) {
			vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
			vo.setId(id);
			service.board_good_update(vo);

			vog.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
			vog.setBoardid(id);
			service.board_good_insert(vog);
			msg.append("alert('���ƿ並 �����̽��ϴ�.'); location='detail.fb?id='+" + id);
			msg.append("</script>");
		} else {
			msg.append("alert('�̹� ���ƿ並 �����̽��ϴ�.'); location='detail.fb?id='+" + id);
			msg.append("</script>");
		}
		return msg.toString();
	}

	// �����Խ��� ��ۺ�������ó�� ��û
	@ResponseBody
	@RequestMapping(value = "/freeboard/comment/update", produces = "application/text; charset=utf-8")
	public String comment_update(@RequestBody FreeBoardCommentVO vo) {// json��û�� Requestbody���
		return service.freeboard_comment_update(vo) ? "����" : "����";
	}

	// �����Խ��� ��ۻ���ó�� ��û
	@ResponseBody
	@RequestMapping("/freeboard/comment/delete/{id}")
	public void comment_delete(@PathVariable int id, FreeBoardVO vo) {
		vo.setId(id);
		service.board_stepdown_update(vo);
		service.freeboard_comment_delete(id);
	}

	// �����Խ��� ��۸�� ��û
	@RequestMapping("/freeboard/comment/{pid}")
	public String comment_list(@PathVariable int pid, Model model) {
		model.addAttribute("list", service.freeboard_comment_list(pid));
		model.addAttribute("crlf", "\r\n");
		model.addAttribute("lf", "\n");
		return "freeboard/comment/list";
	}

	// �����Խ��� �������ó�� ��û
	@ResponseBody
	@RequestMapping("/freeboard/comment/insert")
	public boolean comment_insert(FreeBoardVO vo, int pid, String content, HttpSession ss) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("content", content);
		map.put("userid", ((MemberVO) ss.getAttribute("login_info")).getUserid());
		vo.setId(pid);
		service.board_root_step_update(vo);
		return service.freeboard_comment_insert(map);
	}

	// �����Խ��� ����ó�� ��û
	@RequestMapping("/update.fb")
	public String update(int delete, FreeBoardVO vo, MultipartFile file, HttpSession ss, Model model) {
		FreeBoardVO old = service.board_detail(vo.getId());
		String uuid = ss.getServletContext().getRealPath("resources") + old.getFilepath();
		if (file.getSize() > 0) {
			// ������ ÷���ϴ� ���
			vo.setFilename(file.getOriginalFilename());
			vo.setFilepath(common.fileUpload(file, ss, "freeboard"));
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
		service.board_update(vo);
		model.addAttribute("id", vo.getId());
		return "freeboard/redirect";
	}

	// �����Խ��� ����ȭ�� ��û
	@RequestMapping("/modify.fb")
	public String modify(int id, Model model) {
		model.addAttribute("vo", service.board_detail(id));
		return "freeboard/modify";
	}

	// �����Խ��� ����ó����û
	@RequestMapping("/delete.fb")
	public String delete(int id) {
		service.board_delete(id);
		return "redirect:aa.fb";
	}

	// ÷������ �ٿ�ε� ��û
	@ResponseBody
	@RequestMapping("/download.fb")
	public File download(int id, HttpSession ss, HttpServletResponse response) {
		FreeBoardVO vo = service.board_detail(id);
		return common.fileDownload(vo.getFilename(), vo.getFilepath(), ss, response);

	}

	// �����Խ��� ��ȭ���û
	@RequestMapping("/detail.fb")
	public String detail(int id, Model model) {
		service.board_read(id);
		model.addAttribute("page", page);
		model.addAttribute("vo", service.board_detail(id));
		model.addAttribute("crlf", "\r\n");
		return "freeboard/detail";
	}

	// �ű������Խ��� ���� ó����û
	@RequestMapping("/insert.fb")
	public String insert(FreeBoardVO vo, MultipartFile file, HttpSession ss) {
		// ÷�������� �ִ� ���
		if (file.getSize() > 0) {
			vo.setFilename(file.getOriginalFilename());
			vo.setFilepath(common.fileUpload(file, ss, "board"));
		}
		vo.setUserid(((MemberVO) ss.getAttribute("login_info")).getUserid());
		service.board_insert(vo);
		return "redirect:aa.fb";
	}

	@RequestMapping("/new.fb")
	public String insert() {
		return "freeboard/new";
	}

	@RequestMapping("/aa.fb")
	public String list(Model model, @RequestParam(defaultValue = "1") int curPage,
			@RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "") String keyword) {

		page.setCurPage(curPage);
		page.setSearch(search);
		page.setKeyword(keyword);

		model.addAttribute("page", service.board_list(page));

		return "freeboard/list";
	}

}
