package com.jang.bbs.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jang.bbs.HomeController;
import com.jang.bbs.model.CustomerVO;
import com.jang.bbs.model.UserVO;
import com.jang.bbs.service.CustomerService;
import com.jang.bbs.service.LoginService;
import com.jang.bbs.utils.BCrypt;

@Controller
@RequestMapping("/mobile/*")
public class MobileController {
//https://www.glyphicons.com/ ������������
	@Autowired
	private LoginService loginService;
	@Autowired
	private CustomerService customerService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	// ȸ������
	@RequestMapping(value = "/m.joinForm.do", method = RequestMethod.GET)
	public String joinForm(Model model) {
		model.addAttribute("userVO", new UserVO());
		return "/mobile/mjoinForm";
	}

	@RequestMapping(value = "/checkid.do", method = RequestMethod.GET)
	public String dupCheckId(@RequestParam("userId") String userId, Model model) { // ��� -join d

		String message = "";
		int reDiv = 0;
		System.out.println(userId);
		UserVO loginuser = this.loginService.getUser(userId);
		System.out.println(loginuser.getUserId());

		if (loginuser.getUserId().equals(userId)) {
			message = "�̹� ���� ���̵� �Դϴ�";
			userId = "";
			reDiv = 1;
		} else {
			reDiv = 1;
			message = "��� ������ ���̵� �Դϴ�";
		}
		model.addAttribute("message", message);
		model.addAttribute("reDiv", reDiv);
		model.addAttribute("userId", userId);
		return "/mobile/mjoinSuccess";
	}

	@RequestMapping(value = "/m.joinForm.do", method = RequestMethod.POST)
	public String joinSubmit(@Valid UserVO userVO, BindingResult result, Model model) throws Exception {
		if (result.hasErrors()) {
			model.addAllAttributes(result.getModel());
			return "/mobile/joinForm";
		} // passwd ��ȣȭ
		String hashPass = BCrypt.hashpw(userVO.getPasswd(), BCrypt.gensalt(12));
		userVO.setPasswd(hashPass); // ��ȣȭ�� passwd �� ����
		System.out.println(userVO.toString());
		if (this.loginService.insertUser(userVO) != 0) {
			model.addAttribute("userVO", userVO);
			model.addAttribute("errCode", 3);// ��ϼ���
			return "/mobile/login";
		} else {
			model.addAttribute("errCode", 5);// ��Ͻ���
			return "/mobile/mjoinForm";
		}
	}

	// �α���
	@RequestMapping(value = "/m.login.do", method = RequestMethod.GET)
	public String login() {
		return "/mobile/mlogin";
	}

	@RequestMapping(value = "/m.loginSuccess.do", method = RequestMethod.GET)
	public String loginProc() {
		return "/mobile/mloginSuccess";
	}

	@RequestMapping(value = "/m.loginSuccess.do", method = RequestMethod.POST)
	public String loginProc(@Valid UserVO userVO, BindingResult result, Model model, HttpSession session) {

		if (result.hasFieldErrors("userId") || result.hasFieldErrors("passwd")) {
			model.addAllAttributes(result.getModel());
			return "/mobile/mlogin";
		}

		UserVO loginUser = this.loginService.getUser(userVO.getUserId());
		userVO.setPasswd(BCrypt.hashpw(userVO.getPasswd(), BCrypt.gensalt(12)));
		if (loginUser == null) {
			model.addAttribute("userId", "");
			model.addAttribute("errCode", "1");
			return "/mobile/mlogin";

		} else if (BCrypt.checkpw(userVO.getPasswd(), loginUser.getPasswd())) { // ��
			model.addAttribute("loginUser", loginUser);
			session.setAttribute("userId", loginUser.getUserId());
			session.setAttribute("passwd", loginUser.getPasswd());
			session.setAttribute("dev_no", loginUser.getDev_no());
			session.setAttribute("userName", loginUser.getName()); // ���ǿ� �������
			return "/mobile/mloginSuccess";
		} else {
			model.addAttribute("passwd", "");
			model.addAttribute("errCode", 4);// �н����� ����ġ
			session.setAttribute("userId", loginUser.getUserId());
			session.setAttribute("passwd", loginUser.getPasswd());
			session.setAttribute("dev_no", loginUser.getDev_no());
			session.setAttribute("userName", loginUser.getName()); // ���ǿ� �������
			// return "/member/login";
			return "/mobile/mloginSuccess";
		}
	} // �����ؾߵ�

	// �α׾ƿ�
	@RequestMapping("/m.logout.do")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/mobile/m.login.do";

	}

	// ����������
	@RequestMapping(value = "/m.mypage.do", method = RequestMethod.GET)
	public String mypage(HttpServletRequest request, HttpSession session, Model model) {

		String userId = session.getAttribute("userId").toString();
		UserVO loginUser = this.loginService.getUser(userId);

		if (loginUser == null) {
			model.addAttribute("userId", "");
			model.addAttribute("errCode", 1);// ��ϵ������� ���̵�
			return "/mobile/mloginSuccess";
		}
		model.addAttribute("userVO", loginUser);
		session.setAttribute("userName", loginUser.getName());
		System.out.println(loginUser.toString());
		return "/mobile/mmypage";
	}

	// ����Ͽ��� �α��� �ƴ��� Ȯ��

	@RequestMapping(value = "/mypageTest.do", method = RequestMethod.GET)
	public String mypageTest(HttpServletRequest request, HttpSession session, Model model) {

		try {
			String userId = session.getAttribute("userId").toString();
			UserVO loginUser = this.loginService.getUser(userId);

			if (userId == null) {
				model.addAttribute("userId", "");
				model.addAttribute("errCode", 1);// ��ϵ������� ���̵�
				return "/mobile/mlogin";
			} else
				model.addAttribute("userVO", loginUser);
			session.setAttribute("userName", loginUser.getName());
			System.out.println(loginUser.toString());
			return "/mobile/mmypage";

		} catch (Exception e) {
			// TODO: handle exception

		}
		return "/mobile/mlogin";
	}

	// ����ڵ� ����
	@RequestMapping(value = "/m.updateLoc.do", method = RequestMethod.GET)
	public String updateLoc(HttpServletRequest request, HttpSession session, Model model) {

		String userId = session.getAttribute("userId").toString();
		UserVO loginUser = this.loginService.getUser(userId);

		if (loginUser == null) {
			model.addAttribute("userId", "");
			model.addAttribute("errCode", 1);// ��ϵ������� ���̵�
			return "/mobile/mmypage";
		}
		model.addAttribute("userVO", loginUser);
		System.out.println(loginUser.toString());
		return "/mobile/mupdateLoc";
	}

	@RequestMapping(value = "/m.updateLoc.do", method = RequestMethod.POST)
	public String updateLocOn(@Valid UserVO userVO, BindingResult result, Model model) throws Exception {
		if (result.hasErrors()) {
			model.addAllAttributes(result.getModel());
			return "/mobile/mupdateLoc";
		}
		System.out.println(userVO.getPasswd());
		userVO.setPasswd(BCrypt.hashpw(userVO.getPasswd(), BCrypt.gensalt(12)));
		System.out.println(userVO.getName() + userVO.getUserId());
		System.out.println(userVO.getPasswd());

		if (this.loginService.updateLoc(userVO) != 0) {
			model.addAttribute("userVO", userVO);
			model.addAttribute("errCode", 3);// ��ϼ���
			model.addAttribute("message", "������ ���� ����� ������ �����Ͽ����ϴ�.");
			return "/mobile/mloginSuccess";
		} else {
			model.addAttribute("errCode", 5);// ��Ͻ���
			model.addAttribute("errMsg", "��������� ������ �����Ͽ����ϴ�.");
			return "/mobile/mupdateLoc";
		}
	}

	// ���� ����
	@RequestMapping(value = "/m.edituser.do", method = RequestMethod.GET)
	public String toUserEditView(HttpServletRequest request, HttpSession session, Model model) {

		String userId = session.getAttribute("userId").toString();
		UserVO loginUser = this.loginService.getUser(userId);

		if (loginUser == null) {
			model.addAttribute("userId", "");
			model.addAttribute("errCode", 1);// ��ϵ������� ���̵�
			return "/mobile/mlogin";
		}
		model.addAttribute("userVO", loginUser);
		System.out.println(loginUser.toString());
		return "/mobile/meditForm";
	}

	@RequestMapping(value = "/m.edituser.do", method = RequestMethod.POST)
	public String onEditSave(@Valid UserVO userVO, BindingResult result, Model model) throws Exception {
		if (result.hasErrors()) {
			model.addAllAttributes(result.getModel());
			return "/mobile/meditForm";
		}
		System.out.println(userVO.getPasswd());
		userVO.setPasswd(BCrypt.hashpw(userVO.getPasswd(), BCrypt.gensalt(12)));
		System.out.println(userVO.getName() + userVO.getUserId());
		System.out.println(userVO.getPasswd());

		if (this.loginService.updateUser(userVO) != 0) {
			model.addAttribute("userVO", userVO);
			model.addAttribute("errCode", 3);// ��ϼ���
			model.addAttribute("message", "������ ���� ����� ������ �����Ͽ����ϴ�.");
			return "/mobile/mloginSuccess";
		} else {
			model.addAttribute("errCode", 5);// ��Ͻ���
			model.addAttribute("errMsg", "��������� ������ �����Ͽ����ϴ�.");
			return "/mobile/meditForm";
		}
	}

	// ��ȣ�ڴ���� ���� ����
	@RequestMapping(value = "/m.updateLocUser.do", method = RequestMethod.GET)
	public String updateLocUser(HttpServletRequest request, HttpSession session, Model model) {

		String userId = session.getAttribute("userId").toString();
		UserVO loginUser = this.loginService.getUser(userId);

		if (loginUser == null) {
			model.addAttribute("userId", "");
			model.addAttribute("errCode", 1);// ��ϵ������� ���̵�
			return "/mobile/mmypage";
		}
		model.addAttribute("userVO", loginUser);
		System.out.println(loginUser.toString());
		return "/mobile/mupdateLocUser";
	}

	@RequestMapping(value = "/m.updateLocUser.do", method = RequestMethod.POST)
	public String updateLocUserOn(@Valid UserVO userVO, BindingResult result, Model model) throws Exception {
		if (result.hasErrors()) {
			model.addAllAttributes(result.getModel());
			return "/mobile/mupdateLocUser";
		}
		System.out.println(userVO.getPasswd());
		userVO.setPasswd(BCrypt.hashpw(userVO.getPasswd(), BCrypt.gensalt(12)));
		System.out.println(userVO.getName() + userVO.getUserId());
		System.out.println(userVO.getPasswd());

		if (this.loginService.updateLocUser(userVO) != 0) {
			model.addAttribute("userVO", userVO);
			model.addAttribute("errCode", 3);// ��ϼ���
			model.addAttribute("message", "������ ���� ����� ������ �����Ͽ����ϴ�.");
			return "/mobile/mloginSuccess";
		} else {
			model.addAttribute("errCode", 5);// ��Ͻ���
			model.addAttribute("errMsg", "��������� ������ �����Ͽ����ϴ�.");
			return "/mobile/mupdateLocUser";
		}
	}

	// id ã��
	@RequestMapping(value = "/m.findId.do", method = RequestMethod.GET)
	public String findId(Model model) {
		return "/mobile/mfindId";
	}

	@RequestMapping(value = "/m.findId.do", method = RequestMethod.POST)
	public String findId(@Valid UserVO userVO, BindingResult result, Model model, HttpSession session) {

		if (result.hasFieldErrors("name") || result.hasFieldErrors("email")) {
			model.addAllAttributes(result.getModel());
			return "/mobile/mfindId";
		}
		UserVO loginUser = this.loginService.findId(userVO);
		if (loginUser == null) {
			model.addAttribute("errCode", 1);
			return "/mobile/mfindId";
		} else {
			model.addAttribute("userId", loginUser.getUserId());
			model.addAttribute("userName", loginUser.getName());
			model.addAttribute("errCode", 4);
			return "/mobile/mfindIdSuccess";
		}
	}

	// ��� ã��
	@RequestMapping(value = "/m.findPass.do", method = RequestMethod.GET)
	public String findPass(Model model) {
		return "/mobile/mfindPass";
	}

	@RequestMapping(value = "/m.editPass.do", method = RequestMethod.GET)
	public String onEditpass(@Valid UserVO userVO, BindingResult result, Model model) throws Exception {
		if (result.hasFieldErrors("userId") || result.hasFieldErrors("name") || result.hasFieldErrors("email")) {
			model.addAllAttributes(result.getModel());
			return "/mobile/mfindPass";
		}

		UserVO loginuser = this.loginService.findPass(userVO);
		if (loginuser == null) {
			model.addAttribute("errCode", 1);
			model.addAttribute("userId");
			return "/mobile/mfindPass";
		} else {
			model.addAttribute("userId", loginuser.getUserId());
			model.addAttribute("email", loginuser.getEmail());
			model.addAttribute("name", loginuser.getName());
			System.out.println(loginuser.toString());
			return "/mobile/meditPass";
		}

	}

	@RequestMapping(value = "/m.editPass.do", method = RequestMethod.POST)
	public String onEditpassSave(@Valid UserVO userVO, BindingResult result, Model model) throws Exception {
		if (result.hasFieldErrors("userId") || result.hasFieldErrors("email")) {
			model.addAllAttributes(result.getModel());
			System.out.println("result ����!");
			return "/mobile/meditPass";
		}
		userVO.setPasswd(BCrypt.hashpw(userVO.getPasswd(), BCrypt.gensalt(12)));
		try {
			loginService.updatePass(userVO);
			System.out.println(userVO.toString());
			return "/mobile/meditPass";
		} catch (DataAccessException e) {
			result.reject("error.duplicate.user");
			model.addAllAttributes(result.getModel());
			return "/mobile/meditPass";
		}
	} // ��ĥ���

	// ���ȸ��
	@RequestMapping(value = "/m.userlist.do", method = RequestMethod.GET)
	public String userlist(@ModelAttribute("UserVO") UserVO userVO, Model model, HttpSession session) throws Exception {

		List<UserVO> user = loginService.getuserlist(userVO);
		model.addAttribute("UserVO", user);

		return "mobile/muserlist";
	}

	// ���ȸ�� �󼼺���
	@RequestMapping(value = "/m.userlistdata.do", method = RequestMethod.GET)
	public String userlistdata(@ModelAttribute("UserVO") UserVO userVO, Model model, HttpSession session)
			throws Exception {

		List<UserVO> user = loginService.getuserlistdata(userVO);
		model.addAttribute("UserVO", user);

		return "mobile/muserlistdata";
	}
	
	@RequestMapping(value = "/m.home", method = RequestMethod.GET)
	public String mhome(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "mobile/mhome";
	}
	
	// ������ ��ȸ
	@RequestMapping(value = "/m.customer.do", method = RequestMethod.GET)
	public String mcustomer(@ModelAttribute("CustomerVO") CustomerVO customerVO, Model model, HttpSession session)
			throws Exception {

		List<CustomerVO> cus = customerService.getcustomer(customerVO);
		model.addAttribute("CustomerVO", cus);

		return "mobile/mcustomer";
	}

	// ������
	@RequestMapping(value = "/m.insertcustomer.do", method = RequestMethod.GET)
	public String minsertcustomer(@ModelAttribute("customerVO") CustomerVO customerVO, HttpSession session,
			HttpServletRequest request) throws Exception {

		return "mobile/mhome";
	}

	@RequestMapping(value = "/m.insertcustomer.do", method = RequestMethod.POST)
	public String minsertcustomerpost(@ModelAttribute("customerVO") CustomerVO customerVO, HttpSession session,
			HttpServletRequest request) throws Exception {
		this.customerService.insertcustomer(customerVO);

		return "mobile/mhome";
	}

}
