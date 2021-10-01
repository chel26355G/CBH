package com.jang.bbs.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jang.bbs.model.AttFileVO;
import com.jang.bbs.model.BoardLikeVO;
import com.jang.bbs.model.BoardVO;
import com.jang.bbs.model.BoardViewVO;
import com.jang.bbs.model.ReplyLikeVO;
import com.jang.bbs.model.ReplyVO;
import com.jang.bbs.model.SearchVO;
import com.jang.bbs.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;

	private String uploadPath = "C:\\upload\\";

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listPage(@ModelAttribute("searchVO") SearchVO searchVO, Model model, HttpSession session)
			throws Exception {

		List<BoardVO> blist = boardService.getBoardList(searchVO);
		model.addAttribute("boardList", blist);

		StringBuffer pageUrl = boardService.getPageUrl(searchVO);
		model.addAttribute("pageHttp", pageUrl);

		model.addAttribute("userId", session.getAttribute("userId"));
		model.addAttribute("searchVO", searchVO);

		return "board/list";
	}

	@RequestMapping(value = "/write.do", method = RequestMethod.GET)
	public String boardWrite() {
		return "/board/write";
	}

	@RequestMapping(value = "/write.do", method = RequestMethod.POST)
	public String boardWriteProc(@ModelAttribute("Board") BoardVO board, MultipartHttpServletRequest request) {
		// �� ������
		String content = board.getContent().replaceAll("\r\n", "<br/>");
		// java���� �ڵ� HTML�ٹٲٱ��
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		content = content.replaceAll("&", "&amp;");
		content = content.replaceAll("\"", "&quot;");
		board.setContent(content);
		boardService.writeArticle(board);
		int bno = board.getBno(); // ����� ������ ���۹�ȣ xml ����insert ���� keyProperty="bno" �� ���ؼ� ������
		// ÷�� ���� ����
		AttFileVO file = new AttFileVO();
		String uploadPath = "C:\\upload\\";
		List<MultipartFile> fileList = request.getFiles("file");
		for (MultipartFile mf : fileList) {
			if (!mf.isEmpty()) {
				String originFileName = mf.getOriginalFilename();
				// ���� ���� ��
				long fileSize = mf.getSize();
				// ���� ������
				System.out.println("originFileName : " + originFileName);
				System.out.println("fileSize : " + fileSize);
				file.setBno(bno);
				file.setOfilename(originFileName);
				file.setSfilename(originFileName);
				file.setFilesize(fileSize);
				boardService.insertFile(file);
				// ���̺� ȭ�� ���� ����
				String safeFile = uploadPath + originFileName;
				// ��ũ�� ���� ����
				try {
					mf.transferTo(new File(safeFile));
					// ��ũ�� ���� ����
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "redirect:list.do";
	}

	@RequestMapping("/view")
	public String boardView(@RequestParam(value = "bno", required = false, defaultValue = "0") int bno,
			HttpSession session, Model model) throws Exception {
		String userId = (String) session.getAttribute("userId");
		BoardViewVO boardViewVO = new BoardViewVO();

		// ����ȸ �̷� ��
		boardViewVO.setBno(bno);
		if (userId != null) { // ȸ���� ���

			boardViewVO.setUserId(userId);
			boardViewVO.setMem_yn('y'); // ȸ��
			if (boardService.getBoardView(boardViewVO) == 0) {

				// �ش� ��ȣ ���� ���� ����� ������

				boardService.incrementViewCnt(bno);

				// ��ȸ�� ����

				boardService.addBoardView(boardViewVO);

				// ȸ�� ��ȸ ���

			}
		} else {

			// ��ȸ���� ���

			boardViewVO.setUserId(session.getId());

			// ����id�� ȸ�� id�� ���

			boardViewVO.setMem_yn('n');

			// ��ȸ��
			if (boardService.getBoardView(boardViewVO) == 0) {
				{

					// �ش� ��ȣ ���� ���� ����� ������

					boardService.incrementViewCnt(bno);

					// ��ȸ�� ����

					boardService.addBoardView(boardViewVO);

					// ��ȸ�� ��ȸ�� ���

				}
			}
		}

		BoardVO board = boardService.getArticle(bno);

		// get selected article model
		List<ReplyVO> reply = boardService.getReplyList(bno);

		// �亯 ��� �о� ��

		List<AttFileVO> fileList = boardService.getFileList(bno);

		// ÷������ ��� �о� ����
		model.addAttribute("board", board);
		model.addAttribute("replyList", reply);
		model.addAttribute("fileList", fileList);
		return "board/view";
	}

	@RequestMapping(value = "/modify.do", method = RequestMethod.GET)
	public String boardModify(HttpServletRequest request, HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");
		int bno = Integer.parseInt(request.getParameter("bno"));
		BoardVO board = boardService.getArticle(bno);
		// <br /> tag change to new line code

		String content = board.getContent().replaceAll("<br />", "\r\n");
		board.setContent(content);
		if (!userId.equals(board.getWriterId())) {

			// ��ȸ�� �ۼ��� �Ұ�

			model.addAttribute("errCode", "1");
			model.addAttribute("bno", bno);
			return "redirect:view.do";
		} else {

			// ȸ�� �ۼ���

			List<AttFileVO> fileList = boardService.getFileList(bno);

			// ÷������ �о� ���� - list

			model.addAttribute("board", board);
			model.addAttribute("fileList", fileList);
			return "/board/modify";
		}
	}

	@RequestMapping(value = "/modify.do", method = RequestMethod.POST) // �Խ��� �� ����
	public String boardModifyProc(@ModelAttribute("Board") BoardVO board, MultipartHttpServletRequest request,
			RedirectAttributes rea) {
		String content = board.getContent().replaceAll("\r\n", "<br />");

		// java ���� �ڵ� HTML�ٹٲٱ��

		board.setContent(content);
		boardService.updateArticle(board);
		int bno = board.getBno();
		// üũ�� ������ ���̺�� ��ũ���� �����Ѵ�.

		String[] fileno = request.getParameterValues("fileno");
		if (fileno != null) {
			for (String fn : fileno) {
				int fno = Integer.parseInt(fn);
				String oFileName = boardService.getFileName(fno);
				String safeFile = uploadPath + oFileName;
				File removeFile = new File(safeFile);// remove disk uploaded file removeFile.delete();
				boardService.deleteFile(fno); // remove table uploaded file
			}
		}
		AttFileVO file = new AttFileVO();
		// ��÷�� ���� ��� �Ͼ����
		List<MultipartFile> fileList = request.getFiles("file");
		for (MultipartFile mf : fileList) {
			if (!mf.isEmpty()) {
				String originFileName = mf.getOriginalFilename(); // ��÷������ ���� ���� ��
				long fileSize = mf.getSize(); // ���� ������
				file.setBno(bno);
				file.setFilesize(fileSize);
				file.setOfilename(originFileName);
				file.setSfilename(originFileName);
				boardService.insertFile(file); // ���̺� ���� ����
				String safeFile = uploadPath + originFileName;
				try {
					mf.transferTo(new File(safeFile)); // ��ũ�� ���� ����
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		rea.addAttribute("bno", board.getBno());
		return "redirect:/board/view.do";
	}

	@RequestMapping("/delete.do")
	public String boardDelete(HttpServletRequest request, HttpSession session, RedirectAttributes rea) {
		String userId = (String) session.getAttribute("userId"); // login ���ߵ� ����

		int bno = Integer.parseInt(request.getParameter("bno"));
		BoardVO board = boardService.getArticle(bno);
		String setView = "";
		if (userId.equals(board.getWriterId())) {

			// ��� ����

			List<ReplyVO> reply = boardService.getReplyList(bno);
			if (reply.size() > 0) {
				boardService.deleteReplyBybno(bno);
			} // ÷�� ���ϸ� ����, ���� ���� ����

			List<AttFileVO> files = boardService.getFileList(bno);
			if (files.size() > 0) {

				// ����� ���� ���� ����

				for (AttFileVO filedel : files) {
					String f_stor_all = filedel.getOfilename();
					File f = new File(session.getServletContext().getRealPath("/") + f_stor_all);
					f.delete();
				}
				boardService.deleteFileBybno(bno); // ���̺��� �ش� ��ȣ �� ÷�� file ��ü ����
			}
			// board ����

			boardService.deleteArticle(bno);
			setView = "redirect:list.do";
		} else {
			rea.addAttribute("errCode", "1");// it's forbidden connection

			rea.addAttribute("bno", bno);
			setView = "redirect:view.do";
		}
		return setView;

	}

	@RequestMapping("/recommend.do")
	public String updateRecommendCnt(HttpServletRequest request, HttpSession session, RedirectAttributes rea) {
		int bno = Integer.parseInt(request.getParameter("bno"));
		String userId = (String) session.getAttribute("userId");
		if (userId == null) {
			rea.addAttribute("bno", bno);
			rea.addAttribute("errCode", "4");
			return "redirect:/board/view.do";
		}
		BoardLikeVO boardLike = new BoardLikeVO();
		boardLike.setBno(bno);
		boardLike.setUserId(userId);
		BoardVO board = boardService.getArticle(bno);
		if (board.getWriterId().equals(userId)) {
			rea.addAttribute("errCode", "3"); // ���� ���� ��õ �Ұ�

		} else {
			if (boardService.getBoardLike(boardLike) == 0) {

				// �̹� ��õ�� ����� ������

				boardService.incrementGoodCnt(bno);
				boardService.addBoardLike(boardLike);

				// ��õ ��� ���

			} else {
				rea.addAttribute("errCode", "2");

				// �̹� ��õ�ߴ� ���̸� ��õ �Ұ�

			}
		}
		rea.addAttribute("bno", bno);
		return "redirect:/board/view.do";
	}

	@RequestMapping("/writeReply.do")
	public String replyWriteProc(@ModelAttribute("reply") ReplyVO reply, RedirectAttributes rea) {
		if (reply.getContent().isEmpty()) {
			rea.addAttribute("errCode", "1");
		} else {
			String content = reply.getContent().replaceAll("<", "&lt;");
			content = reply.getContent().replaceAll(">", "&gt;");
			content = reply.getContent().replaceAll("&", "&amp;");
			content = reply.getContent().replaceAll("\"", "&quot;");
			content = reply.getContent().replaceAll("\r\n", "<br />");
			reply.setContent(content);
			boardService.writeReply(reply);
		}
		rea.addAttribute("bno", reply.getBno());
		return "redirect:view.do";
	}

	@RequestMapping("/deleteReply.do")
	public String commentDelete(HttpServletRequest request, HttpSession session, RedirectAttributes rea) {
		int rno = Integer.parseInt(request.getParameter("rno"));
		int bno = Integer.parseInt(request.getParameter("bno"));
		String userId = (String) session.getAttribute("userId");
		ReplyVO reply = boardService.getReply(rno);
		if (!userId.equals(reply.getWriterId())) {
			rea.addAttribute("errCode", "1");
		} else {
			boardService.deleteReply(rno);
		}
		rea.addAttribute("bno", bno); // move back to the article
		return "redirect:view.do";
	}

	@RequestMapping("/recommandReply.do")
	public String RecommendRely(HttpServletRequest request, HttpSession session, RedirectAttributes rea) {
		int bno = Integer.parseInt(request.getParameter("bno"));
		int rno = Integer.parseInt(request.getParameter("rno"));
		String userId = (String) session.getAttribute("userId");
		if (userId == null) {
			rea.addAttribute("bno", bno);
			rea.addAttribute("errCode", "4");
			;
			return "redirect:/board/view.do";
		}
		ReplyLikeVO replyLike = new ReplyLikeVO();
		replyLike.setRno(rno);
		replyLike.setUserId(userId);
		BoardVO board = boardService.getArticle(bno);
		if (board.getWriterId().equals(userId)) {
			rea.addAttribute("errCode", "3");
		} else {
			if (boardService.getReplyLike(replyLike) == 0) {
				boardService.incReplyGoodCnt(rno);
				boardService.addReplyLike(replyLike);
			} else {
				rea.addAttribute("errCode", "2");
			}
		}
		rea.addAttribute("bno", bno);
		return "redirect:/board/view.do";
	}

	@RequestMapping(value = "filedown")
	@ResponseBody
	public byte[] downProcess(HttpServletResponse response, @RequestParam String fileName) throws IOException {
		File file = new File("c:/upload/" + fileName);
		byte[] bytes = FileCopyUtils.copyToByteArray(file); // SPRING 5.0 �̻�
		String fn = new String(file.getName().getBytes(), "iso_8859_1");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fn + "\"");
		response.setContentLength(bytes.length);
		return bytes;
	}
	


}
