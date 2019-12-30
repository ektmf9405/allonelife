package common;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.mail.Folder;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
@Service
public class CommonServiceImpl implements CommonService {

	public void emailSend(String name, String email, HttpServletRequest request) {
		//1. �⺻���� �̸�������
		//simpleSend(name, email);
		
		//2.����÷�ΰ��� �̸�������
		//attachSend(name, email, request);
		
		//3.HTML �±������� �̸�������
		htmlSend(name, email, request);
	}
	
	private void htmlSend(String name, String email, HttpServletRequest request) {
		HtmlEmail mail = new HtmlEmail();
		try {
			setProperties(mail, name, email);
			mail.setSubject("����");
			StringBuffer content
					= new StringBuffer("<html>");
			content.append("<body>");
			content.append("<h2>ȸ�������� �����մϴ�.</h2>");
			content.append("<a href='http://hanuledu.co.kr'>�ѿ��������</a>");
			content.append("����Ʈ��&�� ����<br>");
			content.append("�����������մϴ�<br>");
			content.append("<input type='button' value='Ȯ��'"
					+ "onclick='alert(\"�� �ǳ�\")' />");
			content.append("</body>");
			content.append("</html>");
			mail.setHtmlMsg(content.toString());
			
			EmailAttachment file = new EmailAttachment();
			file.setPath(request.getSession().getServletContext().getRealPath("resources/img/hanul.logo.png"));
			mail.attach(file);
			mail.send();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	private void setProperties(Email mail, String name, String email) throws Exception{
		mail.setHostName("smtp.naver.com");
		mail.setCharset("utf-8");
		mail.setDebug(true);
		
		
		mail.setAuthentication("ektmf9405", "aa156248@@");
		mail.setSSLOnConnect(true);
		
		
		mail.setFrom("ektmf9405@naver.com","������");
		mail.addTo(email, name);
	}
	private void attachSend(String name, String email, HttpServletRequest request) {
		MultiPartEmail mail =  new MultiPartEmail();
		
		try {
			setProperties(mail, name, email);
			
			mail.setSubject("����Ʈ ��");
			mail.setMsg("����");
			
			EmailAttachment file = new EmailAttachment();//÷�����ϰ�ü
			file.setPath(request.getSession()
					.getServletContext()
					.getRealPath("resources/img/hanul.png"));
			mail.attach(file);
			mail.send();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void simpleSend(String name, String email) {
		SimpleEmail mail =  new SimpleEmail();
		
		try {
			setProperties(mail, name, email);
			
			mail.setSubject("����Ʈ ��");
			mail.setMsg("����");
			mail.send();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	private String makeFolder(String category, String upload) {
		StringBuffer folder = new StringBuffer(upload);
		//D://Spring/.../smart/resources/upload/notice ���� ����
		folder.append( File.separator + category);
		//D://Spring/.../smart/resources/upload/notice/2019 ���� ����
		Date now = new Date();
		folder.append(File.separator + new SimpleDateFormat("yyyy/MM/dd").format(now));
		//�ش� ������ �ִ��� Ȯ���Ͽ� ������ ��������
		File dir = new File(folder.toString());
		if( ! dir.exists()) dir.mkdirs();
		return folder.toString();
	}
	
	@Override
	public String fileUpload(MultipartFile file, HttpSession ss, String category) {
		//���ε��� ������ ������ ��ġ
		//D://Spring/.../smart/resources ���� ����
		String resources= ss.getServletContext().getRealPath("resources");
		//D://Spring/.../smart/resources/upload ���� ����
		String upload = resources + File.separator + "upload";
		
		String folder = makeFolder(category, upload);
		//�����Ѿ��̵�
		//D://Spring/.../smart/resources/upload/2019/09/05 ���� ����
		String uuid= UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		try {
			//������ ������ ���ε��� ���� �����ϱ�
			file.transferTo( new File(folder, uuid) );			
		} catch (Exception e) {
		}
		
		//D://Spring/.../smart/resources/upload/2019/09/05/dd555_abc.txt ���� ����
		return folder.substring(resources.length())
					+ File.separator + uuid;
	}

	@Override
	public File fileDownload(String name, String path, HttpSession ss, HttpServletResponse response) {
		// �ٿ�ε��� ���ϻ���
		File file = new File(ss.getServletContext().getRealPath("resources")
				+ File.separator + path);
		String mime = ss.getServletContext().getMimeType(name);
		if (mime == null) mime = "application/octet-stream";
		try {
			response.setContentType(mime);
			
			URLEncoder.encode(name, "utf-8");
			response.setHeader("content-disposition", "attachment; filename=" + name);
			ServletOutputStream out = response.getOutputStream();
			FileCopyUtils.copy(new FileInputStream(file), out);
			out.flush();
		} catch (Exception e) {
			
		}
		return file;
			
		}
}
