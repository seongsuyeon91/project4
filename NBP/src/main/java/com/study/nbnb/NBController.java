package com.study.nbnb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.study.nbnb.dao.AdDao;
import com.study.nbnb.dao.B1Dao;
import com.study.nbnb.dao.B2Dao;
import com.study.nbnb.dao.BuserDao;
import com.study.nbnb.dao.ChatRoomDao;
import com.study.nbnb.dao.CommentDao;
import com.study.nbnb.dao.GoodDao;
import com.study.nbnb.dao.LikeDao;
import com.study.nbnb.dao.PlayDao;
import com.study.nbnb.dao.RDao;
import com.study.nbnb.dao.SearchDao;
import com.study.nbnb.dao.ShopDao;
import com.study.nbnb.dto.AdDto;
import com.study.nbnb.dto.B1Dto;
import com.study.nbnb.dto.B2Dto;
import com.study.nbnb.dto.BuserDto;
import com.study.nbnb.dto.ChatRoomDto;
import com.study.nbnb.dto.GoodDto;
import com.study.nbnb.dto.LikeDto;
import com.study.nbnb.dto.PlayDto;
import com.study.nbnb.dto.RankDto;
import com.study.nbnb.dto.ShopDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class NBController {

	@Autowired
	B1Dao b1dao;
	@Autowired
	B2Dao b2dao;
	@Autowired
	PlayDao playdao;
	@Autowired
	CommentDao cmtdao;
	@Autowired
	LikeDao likedao;
	@Autowired
	BuserDao buserDao;
	@Autowired
	SearchDao searchDao;
	@Autowired
	ChatRoomDao crdao;
	@Autowired
	AdDao addao;
	@Autowired
	RDao rdao;
	@Autowired
	ShopDao shopDao;
	@Autowired
	GoodDao gooddao;

	@Value("${upload.directory}")
	private String uploadDirectory;

	@RequestMapping("/")
	public String root() throws Exception {
		// MyBatis : SimpleBBS
		return "redirect:main";
	}

	@RequestMapping("/mypage")
	public String mypageview(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		BuserDto bdto = (BuserDto) session.getAttribute("login");
		model.addAttribute("login", bdto);
		return "mypage/mypage_view";
	}

	@RequestMapping("/main")
	public String mainview() {
		return "main_view";
	}

	@RequestMapping("/map")
	public String mapview(Model model) {
		List<BuserDto> members = buserDao.getAllMembers();
		model.addAttribute("members", members);
		return "map_view";
	}

	@RequestMapping("/rpage")
	public String showb1Ranking(HttpServletRequest request, Model model) {
		List<RankDto> b1rankingList = rdao.getb1Ranking();
		List<RankDto> b2rankingList = rdao.getb2Ranking();
		List<RankDto> plrankingList = rdao.getplRanking();
		List<RankDto> userb1RankingList = rdao.getb1UserRanking();
		List<RankDto> userb2RankingList = rdao.getb2UserRanking();
		List<RankDto> userplRankingList = rdao.getplUserRanking();
		List<RankDto> userRankingList = rdao.getUserRanking();
		model.addAttribute("b1rankingList", b1rankingList);
		model.addAttribute("b2rankingList", b2rankingList);
		model.addAttribute("plrankingList", plrankingList);
		model.addAttribute("userb1RankingList", userb1RankingList);
		model.addAttribute("userb2RankingList", userb2RankingList);
		model.addAttribute("userplRankingList", userplRankingList);
		model.addAttribute("userRankingList", userRankingList);
		return "bbang_rank";
	}

/////////////////////////////////mypage//////////////////////////////////////

	@RequestMapping("/mypage_shop")
	public String mypageshopview(HttpSession session, HttpServletRequest request, Model model) {
		BuserDto login = (BuserDto) session.getAttribute("login");
		int m_number = login.getM_NUMBER();

		model.addAttribute("user", buserDao.selectUser(m_number));
		return "mypage/mypage_shop";
	}

	@GetMapping("/goodpost")
	public String getgoodpost(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		BuserDto bdto = (BuserDto) session.getAttribute("login");
		int m_number = bdto.getM_NUMBER();
		int t_number = m_number;
		List<GoodDto> getgoodpost = gooddao.getgoodpost(t_number);

		model.addAttribute("getgoodpost", getgoodpost);
		return "mypage/mypage_good";
	}

	@RequestMapping("/mypage_popup")
	public String shopPopup(HttpServletRequest request, Model model) {
		int t_count = Integer.parseInt(request.getParameter("t_count"));
		int t_price = Integer.parseInt(request.getParameter("t_price"));

		ShopDto a = shopDao.selectDao3();
		if (a == null) {

			a = new ShopDto();
			a.setBuy_number(1);

		}
		a.setT_count(t_count);
		a.setT_price(t_price);

		model.addAttribute("shopdto", a);
		return "mypage/shop_popup";
	}

	@RequestMapping("/success")
	public String successshopview(HttpServletRequest request) {
		return "mypage/success";
	}

	@RequestMapping("/fail")
	public String failshopview(HttpServletRequest request) {
		return "mypage/fail";
	}

	@RequestMapping("/shopping_list")
	public String shoppinglist(HttpServletRequest request, Model model) {

		int m_number = Integer.parseInt(request.getParameter("m_number"));
		model.addAttribute("shoplist", shopDao.listDao(m_number));
		return "mypage/shopping_list";
	}

	@RequestMapping("/cancelPurchase")
	public String cancelPurchase(HttpServletRequest request) {

		int buy_number = Integer.parseInt(request.getParameter("buy_number"));
		int m_number = Integer.parseInt(request.getParameter("m_number"));
		shopDao.approveDao(buy_number, m_number);

		return "redirect:shopping_list?m_number=" + m_number;

	}

	@RequestMapping("/1/profile")
	public String profile(HttpServletRequest request, Model model) {
		BuserDto bdto = (BuserDto) (request.getSession().getAttribute("login"));
		model.addAttribute("user", buserDao.selectUser(bdto.getM_NUMBER()));
		return "mypage/mypage_profile";
	}

	@RequestMapping("/1/profile/modify")
	public String profile_modify(HttpServletRequest request) {
		int m_number = Integer.parseInt(request.getParameter("m_number"));

		String PHONENUMBER = request.getParameter("phone1") + "-" + request.getParameter("phone2") + "-"
				+ request.getParameter("phone3");

		String pw1 = request.getParameter("PASSWORD");
		String pw2 = request.getParameter("pw2");

		if (pw1.equals(pw2)) {
			buserDao.updateUser2(request.getParameter("ID"), request.getParameter("NAME"),
					request.getParameter("ADDRESS"), request.getParameter("EMAIL"), PHONENUMBER,
					request.getParameter("NICKNAME"), request.getParameter("BBANG"), m_number);
		} else {
			String encoded = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pw1);
			String password = encoded.substring(8);
			buserDao.updateUser(request.getParameter("ID"), password, request.getParameter("NAME"),
					request.getParameter("ADDRESS"), request.getParameter("EMAIL"), PHONENUMBER,
					request.getParameter("NICKNAME"), request.getParameter("BBANG"), m_number);
		}
		return "redirect:/";
	}

	@RequestMapping("/mpchat")
	public String myPageChatview(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		BuserDto a = (BuserDto) session.getAttribute("login");
		List<ChatRoomDto> cr = crdao.listroomDao(a.getNICKNAME());
		model.addAttribute("chat", cr);

		return "mypage/mypage_talk";
	}

	@RequestMapping("/ticketuse")
	public void ticketuse(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		BuserDto a = (BuserDto) session.getAttribute("login");
		int useT = a.getM_NUMBER();
		crdao.useTicket(useT);
	}

	@RequestMapping("/getTicketCount")
	public void getTicketCount(HttpServletRequest request) {
		BuserDto a = (BuserDto) request.getSession().getAttribute("login");
		buserDao.ticketCount(a.getTICKET());
	}
////////////////////////////////////LogIn////////////////////////////////////////////////////////////

	@RequestMapping("/joinView")
	public String joinView() {
		return "login/join_view";
	}

	@RequestMapping("/userJoin")
	public String userJoin(HttpServletRequest request) {
		String PHONENUMBER = request.getParameter("phone1") + "-" + request.getParameter("phone2") + "-"
				+ request.getParameter("phone3");

		String encoded = PasswordEncoderFactories.createDelegatingPasswordEncoder()
				.encode(request.getParameter("PASSWORD"));
		String password = encoded.substring(8);
		String bbang = "ROLE_" + request.getParameter("BBANG");
		buserDao.writeDao(request.getParameter("NAME"), request.getParameter("ID"), password,
				request.getParameter("ADDRESS"), request.getParameter("EMAIL"), PHONENUMBER,
				request.getParameter("NICKNAME"), bbang);
		return "redirect:loginView";
	}

	@RequestMapping("/loginView")
	public String loginView() {
		return "login/login_view";
	}

	@RequestMapping("/mailView")
	public String mailView() {
		return "login/mail";
	}

	@RequestMapping("/search_id")
	public String search_id() {
		return "login/search_id";
	}

	@RequestMapping("/search_pw")
	public String search_pw() {
		return "login/search_pw";
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////

	@RequestMapping("/member/b1view")
	public String view(HttpServletRequest request, Model model) {
		int b1_number = Integer.parseInt(request.getParameter("b1_number"));
		int check_b = Integer.parseInt(request.getParameter("check_b"));
		HttpSession session = request.getSession();
		session.setAttribute("b1dto", b1dao.viewDao(b1_number));
		model.addAttribute("dto", b1dao.viewDao(b1_number));

		model.addAttribute("commentview", cmtdao.viewDao(check_b, b1_number));
		session.setAttribute("commentview", cmtdao.viewDao(check_b, b1_number));
		return "b1board/b1view";

	}

	@RequestMapping("/member/b1replywrite")
	public String b1CmtStore(HttpServletRequest request, Model model) {
		int check_b = Integer.parseInt(request.getParameter("check_b"));
		int m_number = Integer.parseInt(request.getParameter("m_number"));
		int t_number = Integer.parseInt(request.getParameter("t_number"));
		String nickname = request.getParameter("nickname");
		HttpSession session = request.getSession();
		session.setAttribute("b1dto", b1dao.viewDao(t_number));
		model.addAttribute("dto", b1dao.viewDao(t_number));
		String cmt = request.getParameter("cmt");
		
		if (cmt == null || cmt.isEmpty()) {
			return "redirect:b1view?b1_number=" + t_number + "&check_b=1";

		}
		
		cmtdao.writeDao(check_b, m_number, nickname, cmt, t_number);

		return "redirect:b1view?b1_number=" + t_number + "&check_b=1";
	}

	@RequestMapping("/member/b1replydelete")
	public String b1Delete(HttpServletRequest request, Model model) {
		int t_number = Integer.parseInt(request.getParameter("t_number"));
		int c_number = Integer.parseInt(request.getParameter("c_number"));
		cmtdao.deleteDao(c_number);
		return "redirect:b1view?b1_number=" + t_number + "&check_b=1";

	}

	@RequestMapping("/member/b1writeform")
	public String writeForm(HttpServletRequest request, Model model) {
		int m_number = Integer.parseInt(request.getParameter("m_number"));
		model.addAttribute("member", buserDao.selectUser(m_number));
		return "b1board/b1writeform";
	}

	@RequestMapping("/member/b1write")
	public String write(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
			@RequestParam("file3") MultipartFile file3, @RequestParam("m_number") int m_number,
			HttpServletRequest request, Model model, @Valid @ModelAttribute("b1Board") B1Dto b1Board,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			// validation 실패
			System.out.println("validation에 실패했습니다.");
			return "redirect:b1page?page=1&Searchdata=&Searchfield=";
		}
		try {
			String writer = b1Board.getWriter();
			String title = b1Board.getTitle();
			String content = b1Board.getContent();

			String imageURL1 = "";
			if (file1 != null && !file1.isEmpty()) {
				imageURL1 = uploadFile(file1);
			} else {
				imageURL1 = "/img/yb.png";
			}

			String imageURL2 = "";
			if (file2 != null && !file2.isEmpty()) {
				imageURL2 = uploadFile(file2);
			} else {
				imageURL2 = "/img/yb.png";
			}

			String imageURL3 = "";
			if (file3 != null && !file3.isEmpty()) {
				imageURL3 = uploadFile(file3);
			} else {
				imageURL3 = "/img/yb.png";
			}

			b1dao.writeDao(writer, title, content, imageURL1, imageURL2, imageURL3, m_number);

			int b1_number = b1dao.selectDao();
			return "redirect:b1view?b1_number=" + b1_number + "&check_b=1";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:member/b1page?page=1&Searchdata=&Searchfield=";
	}

	private String uploadFile(MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
			return "/uploads/" + fileName;
		}
		return null;
	}

	@RequestMapping("/member/b1modifyform")
	public String modifyForm(int b1_number, HttpServletRequest request, Model model) {
		// String b1_number = request.getParameter("b1_number");

		model.addAttribute("dto", b1dao.viewDao(b1_number));
		return "b1board/b1modifyform";
	}

	@RequestMapping("/member/b1modify")
	public String modify(@RequestParam("b1_number") int b1_number, @RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3,
			HttpServletRequest request, Model model) {
		try {
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			B1Dto existingDTO = b1dao.viewDao(b1_number);

			String imageURL1 = file1.isEmpty() ? existingDTO.getImageurl1() : uploadFile(file1);
			String imageURL2 = file2.isEmpty() ? existingDTO.getImageurl2() : uploadFile(file2);
			String imageURL3 = file3.isEmpty() ? existingDTO.getImageurl3() : uploadFile(file3);
			String b1_number1 = Integer.toString(b1_number);

			Map<String, String> parameters = new HashMap<>();
			parameters.put("title", title);
			parameters.put("content", content);
			parameters.put("imageurl1", imageURL1);
			parameters.put("imageurl2", imageURL2);
			parameters.put("imageurl3", imageURL3);
			parameters.put("b1_number", b1_number1);

			b1dao.modifyDao(parameters);

			return "redirect:b1view?b1_number=" + request.getParameter("b1_number") + "&check_b=1";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:b1view?b1_number=" + request.getParameter("b1_number") + "&check_b=1";
		}
	}

	@RequestMapping("/member/b1delete")
	public String delete(HttpServletRequest request, Model model) {
		b1dao.deleteDao(request.getParameter("b1_number"));
		return "redirect:/member/b1page?page=1&Searchdata=&Searchfield=";
	}

	@RequestMapping("/member/b1like")
	@ResponseBody
	public ResponseEntity<?> b1Like(@RequestParam("check_b") int check_b, @RequestParam("t_number") int t_number,
			@RequestParam("m_number") int m_number, @RequestParam("l_or_dl") int l_or_dl) {
		List<LikeDto> list = likedao.listDao(check_b, t_number, m_number);
		List<LikeDto> list2 = likedao.listDao2(check_b, t_number, m_number, l_or_dl);

		if (list.size() == 0) {

			if (l_or_dl == 1) {
				likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
				b1dao.likelyDao(t_number);
			} else if (l_or_dl == -1) {
				likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
				b1dao.dislikelyDao(t_number);
			}
		} else {
			if (list2.size() == 0) {
				likedao.deleteDao(check_b, t_number, m_number);
				b1dao.dislikeDropDao(t_number);
				if (l_or_dl == 1) {
					likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
					b1dao.likelyDao(t_number);

				}
			} else {
				likedao.deleteDao(check_b, t_number, m_number);
				b1dao.likeDropDao(t_number);
				if (l_or_dl == -1) {
					likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
					b1dao.dislikelyDao(t_number);
				}
			}
		}
		int likeCount = likedao.likecountDao(check_b, t_number).size();
		int dislikeCount = likedao.dislikecountDao(check_b, t_number).size();
		Map<String, Object> response = new HashMap<>();
		response.put("b1_like", likeCount);
		response.put("b1_dislike", dislikeCount);

		return ResponseEntity.ok(response);
		// return "redirect:b1view?b1_number=" + request.getParameter("t_number") +
		// "&check_b=1";
	}

	@RequestMapping("/member/b1page")
	public String b1listpage(HttpServletRequest request, Model model) {

		String kw1 = request.getParameter("Searchdata");
		String bd = request.getParameter("Searchfield");
		if (bd.equals(null)) {
			bd = "";
			kw1 = "";
		}
		String kw = "%" + kw1 + "%";
		if (bd.equals("b1Title")) {
			int total = b1dao.titleCountDao(kw).size();
			int pageSize = 8;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			List<B1Dto> list = b1dao.titlesearchDao(kw, nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);

		} else if (bd.equals("b1Content")) {
			int total = b1dao.contentCountDao(kw).size();
			int pageSize = 8;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			List<B1Dto> list = b1dao.contentsearchDao(kw, nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);
		} else if (bd.equals("b1Writer")) {
			int total = b1dao.writerCountDao(kw).size();
			int pageSize = 8;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			List<B1Dto> list = b1dao.writersearchDao(kw, nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);
		} else {
			int total = b1dao.listCountDao().size();
			int pageSize = 8;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			List<B1Dto> list = b1dao.pageDao(page, pageSize);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			list = b1dao.pageDao(nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);
		}

		return "b1board/b1list";
	}

	////////////////////////////// b2
	////////////////////////////// board///////////////////////////////////////////////////////////

	@RequestMapping("/member/b2view")
	public String b2view(HttpServletRequest request, Model model) {
		int b2_number = Integer.parseInt(request.getParameter("b2_number"));
		int check_b = Integer.parseInt(request.getParameter("check_b"));
		HttpSession session = request.getSession();
		session.setAttribute("b2dto", b2dao.viewDao(b2_number));
		model.addAttribute("dto", b2dao.viewDao(b2_number));
		model.addAttribute("commentview", cmtdao.viewDao(check_b, b2_number));
		return "b2board/b2view";

	}

	@RequestMapping("/member/b2replywrite")
	public String b2CmtStore(HttpServletRequest request, Model model) {

		int check_b = Integer.parseInt(request.getParameter("check_b"));
		int m_number = Integer.parseInt(request.getParameter("m_number"));
		int t_number = Integer.parseInt(request.getParameter("t_number"));
		String nickname = request.getParameter("nickname");
		HttpSession session = request.getSession();
		session.setAttribute("b2dto", b2dao.viewDao(t_number));
		model.addAttribute("dto", b2dao.viewDao(t_number));
		String cmt = request.getParameter("cmt");
		if (cmt == null || cmt.isEmpty()) {
			return "redirect:b2view?b2_number=" + t_number + "&check_b=2";
		}
		cmtdao.writeDao(check_b, m_number, nickname, cmt, t_number);
		return "redirect:b2view?b2_number=" + t_number + "&check_b=2";
	}

	@RequestMapping("/member/b2replydelete")
	public String b2Delete(HttpServletRequest request, Model model) {
		int t_number = Integer.parseInt(request.getParameter("t_number"));
		int c_number = Integer.parseInt(request.getParameter("c_number"));
		cmtdao.deleteDao(c_number);
		return "redirect:b2view?b2_number=" + t_number + "&check_b=2";

	}

	@RequestMapping("/member/b2writeform")
	public String b2writeForm(HttpServletRequest request, Model model) {
		int m_number = Integer.parseInt(request.getParameter("m_number"));
		model.addAttribute("member", buserDao.selectUser(m_number));
		return "b2board/b2writeform";
	}

	@RequestMapping("/member/b2write")
	public String b2write(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
			@RequestParam("file3") MultipartFile file3, @RequestParam("m_number") int m_number,
			HttpServletRequest request, Model model, @Valid @ModelAttribute("b2Board") B2Dto b2Board,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// validation 실패
			System.out.println("validation에 실패했습니다.");
			return "redirect:b2page?page=1&Searchdata=&Searchfield=";
		}
		try {
			String writer = request.getParameter("writer");
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			String imageURL1 = "";
			if (file1 != null && !file1.isEmpty()) {
				imageURL1 = uploadFile(file1);
			} else {
				imageURL1 = "/img/nb.png";
			}

			String imageURL2 = "";
			if (file2 != null && !file2.isEmpty()) {
				imageURL2 = uploadFile(file2);
			} else {
				imageURL2 = "/img/nb.png";
			}

			String imageURL3 = "";
			if (file3 != null && !file3.isEmpty()) {
				imageURL3 = uploadFile(file3);
			} else {
				imageURL3 = "/img/nb.png";
			}

			b2dao.writeDao(writer, title, content, imageURL1, imageURL2, imageURL3, m_number);
			int b2_number = b2dao.selectDao();

			return "redirect:b2view?b2_number=" + b2_number + "&check_b=2";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:member/b2page?page=1&Searchdata=&Searchfield=";
	}

	private String b2uploadFile(MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
			return "/uploads/" + fileName;
		}
		return null;
	}

	@RequestMapping("/member/b2modifyform")
	public String b2modifyForm(int b2_number, HttpServletRequest request, Model model) {
		// String b1_number = request.getParameter("b1_number");

		model.addAttribute("dto", b2dao.viewDao(b2_number));
		return "b2board/b2modifyform";
	}

	@RequestMapping("/member/b2modify")
	public String b2modify(@RequestParam("b2_number") int b2_number, @RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3,
			HttpServletRequest request, Model model) {
		try {
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			B2Dto existingDTO = b2dao.viewDao(b2_number);

			String imageURL1 = file1.isEmpty() ? existingDTO.getImageurl1() : b2uploadFile(file1);
			String imageURL2 = file2.isEmpty() ? existingDTO.getImageurl2() : b2uploadFile(file2);
			String imageURL3 = file3.isEmpty() ? existingDTO.getImageurl3() : b2uploadFile(file3);
			String b2_number1 = Integer.toString(b2_number);

			Map<String, String> parameters = new HashMap<>();
			parameters.put("title", title);
			parameters.put("content", content);
			parameters.put("imageurl1", imageURL1);
			parameters.put("imageurl2", imageURL2);
			parameters.put("imageurl3", imageURL3);
			parameters.put("b2_number", b2_number1);

			b2dao.modifyDao(parameters);

			return "redirect:b2view?b2_number=" + request.getParameter("b2_number") + "&check_b=2";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:b2page?b2_number=" + request.getParameter("b2_number") + "&check_b=2";
		}
	}

	@RequestMapping("/member/b2delete")
	public String b2delete(HttpServletRequest request, Model model) {
		b2dao.deleteDao(request.getParameter("b2_number"));
		return "redirect:/member/b2page?page=1&Searchdata=&Searchfield=";
	}

	@RequestMapping("/member/b2like")
	@ResponseBody
	public ResponseEntity<?> b2Like(@RequestParam("check_b") int check_b, @RequestParam("t_number") int t_number,
			@RequestParam("m_number") int m_number, @RequestParam("l_or_dl") int l_or_dl) {
		List<LikeDto> list = likedao.listDao(check_b, t_number, m_number);
		List<LikeDto> list2 = likedao.listDao2(check_b, t_number, m_number, l_or_dl);

		if (list.size() == 0) {

			if (l_or_dl == 1) {
				likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
				b2dao.likelyDao(t_number);
			} else if (l_or_dl == -1) {
				likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
				b2dao.dislikelyDao(t_number);
			}
		} else {
			if (list2.size() == 0) {
				likedao.deleteDao(check_b, t_number, m_number);
				b2dao.dislikeDropDao(t_number);
				if (l_or_dl == 1) {
					likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
					b2dao.likelyDao(t_number);

				}
			} else {
				likedao.deleteDao(check_b, t_number, m_number);
				b2dao.likeDropDao(t_number);
				if (l_or_dl == -1) {
					likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
					b2dao.dislikelyDao(t_number);
				}
			}
		}
		int likeCount = likedao.likecountDao(check_b, t_number).size();
		int dislikeCount = likedao.dislikecountDao(check_b, t_number).size();
		Map<String, Object> response = new HashMap<>();
		response.put("b2_like", likeCount);
		response.put("b2_dislike", dislikeCount);

		return ResponseEntity.ok(response);
		// return "redirect:b1view?b1_number=" + request.getParameter("t_number") +
		// "&check_b=1";
	}

	@RequestMapping("/member/b2page")
	public String b2listpage(HttpServletRequest request, Model model) {

		String kw1 = request.getParameter("Searchdata");
		String bd = request.getParameter("Searchfield");
		if (bd.equals(null)) {
			bd = "";
			kw1 = "";
		}
		String kw = "%" + kw1 + "%";
		if (bd.equals("b2Title")) {
			int total = b2dao.titleCountDao(kw).size();
			int pageSize = 8;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			List<B2Dto> list = b2dao.titlesearchDao(kw, nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);

		} else if (bd.equals("b2Content")) {
			int total = b2dao.contentCountDao(kw).size();
			int pageSize = 8;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			List<B2Dto> list = b2dao.contentsearchDao(kw, nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);
		} else if (bd.equals("b2Writer")) {
			int total = b2dao.writerCountDao(kw).size();
			int pageSize = 8;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			List<B2Dto> list = b2dao.writersearchDao(kw, nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);
		} else {
			int total = b2dao.listCountDao().size();
			int pageSize = 8;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			List<B2Dto> list = b2dao.pageDao(page, pageSize);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			list = b2dao.pageDao(nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);
		}

		return "b2board/b2list";
	}

	////////////////////////////////// play board
	///////////////////////////////////////////////////////////////////////////////////////////

	@RequestMapping("/playlist")
	public String playListPage(Model model) {
		model.addAttribute("playlist", playdao.plistDao());
		return "playboard/playlist";
	}

	@RequestMapping("/member/playview")
	public String playView(HttpServletRequest request, Model model) {
		int f_number = Integer.parseInt(request.getParameter("f_number"));
		int check_b = Integer.parseInt(request.getParameter("check_b"));
		HttpSession session = request.getSession();
		session.setAttribute("playdto", playdao.viewDao(f_number));
		model.addAttribute("playview", playdao.viewDao(f_number));
		model.addAttribute("commentview", cmtdao.viewDao(check_b, f_number));
		return "playboard/playview";
	}

	@RequestMapping("/member/plreplywrite")
	public String playCmtStore(HttpServletRequest request, Model model) {

		int check_b = Integer.parseInt(request.getParameter("check_b"));
		int m_number = Integer.parseInt(request.getParameter("m_number"));
		int t_number = Integer.parseInt(request.getParameter("t_number"));
		String nickname = request.getParameter("nickname");
		HttpSession session = request.getSession();
		session.setAttribute("playdto", playdao.viewDao(t_number));
		model.addAttribute("playview", playdao.viewDao(t_number));
		
		String cmt = request.getParameter("cmt");
		if (cmt == null || cmt.isEmpty()) {
			return "redirect:playview?f_number=" + t_number + "&check_b=3";
		}

		cmtdao.writeDao(check_b, m_number, nickname, cmt, t_number);

		return "redirect:playview?f_number=" + t_number + "&check_b=3";
	}

	@RequestMapping("/member/replydelete")
	public String replyDelete(HttpServletRequest request, Model model) {
		int t_number = Integer.parseInt(request.getParameter("t_number"));
		int c_number = Integer.parseInt(request.getParameter("c_number"));
		cmtdao.deleteDao(c_number);
		return "redirect:playview?f_number=" + t_number + "&check_b=3";

	}

	@RequestMapping("/member/playwriteform")
	public String playWriteForm(HttpServletRequest request, Model model) {
		int m_number = Integer.parseInt(request.getParameter("m_number"));
		model.addAttribute("member", buserDao.selectUser(m_number));
		return "playboard/playwriteform";
	}

	@RequestMapping("/member/playwrite")
	public String playWrite(@RequestParam("file") MultipartFile file, @RequestParam("m_number") int m_number,
			HttpServletRequest request, Model model, @Valid @ModelAttribute("playBoard") PlayDto playBoard,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// validation 실패
			System.out.println("validation에 실패했습니다.");
			return "redirect:playpage?page=1&Searchdata=&Searchfield=";
		}
		try {
			String writer = playBoard.getWriter();
			String title = playBoard.getTitle();
			String content = playBoard.getContent();

			String imageURL = "";
			if (file != null && !file.isEmpty()) {
				imageURL = uploadFile(file);
			} else {
				imageURL = "/images/play.png";
			}

			playdao.writeDao(writer, title, content, imageURL, m_number);

			int f_number = playdao.selectDao();

			return "redirect:playview?f_number=" + f_number + "&check_b=3";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:member/playpage?page=1&Searchdata=&Searchfield=";
	}

	@RequestMapping("/member/playmodifyview")
	public String playModifyView(HttpServletRequest request, Model model) {
		int pId = Integer.parseInt(request.getParameter("f_number"));
		model.addAttribute("playmodify", playdao.viewDao(pId));
		return "playboard/playmodify";
	}

	@RequestMapping("/member/playmodify")
	public String playModify(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model) {

		try {

			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int f_number = Integer.parseInt(request.getParameter("f_number"));

			PlayDto existingDTO = playdao.viewDao(f_number);
			String imageURL = file.isEmpty() ? existingDTO.getImageurl() : uploadFile(file);

			playdao.modifyDao(title, content, imageURL, f_number);
			return "redirect:playview?f_number=" + request.getParameter("f_number") + "&check_b=3";

		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:playview?f_number=" + request.getParameter("f_number") + "&check_b=3";
		}

	}

	@RequestMapping("/member/playdelete")
	public String playDelete(HttpServletRequest request, Model model) {
		int f_number = Integer.parseInt(request.getParameter("f_number"));
		playdao.deleteDao(f_number);
		return "redirect:/member/playpage?page=1&Searchdata=&Searchfield=";
	}

	@RequestMapping("/member/playlike")
	@ResponseBody
	public ResponseEntity<?> playLike(@RequestParam("check_b") int check_b, @RequestParam("t_number") int t_number,
			@RequestParam("m_number") int m_number, @RequestParam("l_or_dl") int l_or_dl) {
		List<LikeDto> list = likedao.listDao(check_b, t_number, m_number);
		List<LikeDto> list2 = likedao.listDao2(check_b, t_number, m_number, l_or_dl);

		if (list.size() == 0) {

			if (l_or_dl == 1) {
				likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
				playdao.likelyDao(t_number);
			} else if (l_or_dl == -1) {
				likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
				playdao.dislikelyDao(t_number);
			}
		} else {
			if (list2.size() == 0) {
				likedao.deleteDao(check_b, t_number, m_number);
				playdao.dislikeDropDao(t_number);
				if (l_or_dl == 1) {
					likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
					playdao.likelyDao(t_number);

				}
			} else {
				likedao.deleteDao(check_b, t_number, m_number);
				playdao.likeDropDao(t_number);
				if (l_or_dl == -1) {
					likedao.likeClickDao(check_b, t_number, m_number, l_or_dl);
					playdao.dislikelyDao(t_number);
				}
			}
		}
		int likeCount = likedao.likecountDao(check_b, t_number).size();
		int dislikeCount = likedao.dislikecountDao(check_b, t_number).size();
		Map<String, Object> response = new HashMap<>();
		response.put("p_like", likeCount);
		response.put("p_dislike", dislikeCount);

		return ResponseEntity.ok(response);
		// return "redirect:b1view?b1_number=" + request.getParameter("t_number") +
		// "&check_b=1";
	}

	@RequestMapping("/member/playpage")
	public String playlistpage(HttpServletRequest request, Model model) {

		String kw1 = request.getParameter("Searchdata");
		String bd = request.getParameter("Searchfield");
		if (bd.equals(null)) {
			bd = "";
			kw1 = "";
		}
		String kw = "%" + kw1 + "%";
		if (bd.equals("pTitle")) {
			int total = playdao.titleCountDao(kw).size();
			int pageSize = 15;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			List<PlayDto> list = playdao.titlesearchDao(kw, nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);

		} else if (bd.equals("pContent")) {
			int total = playdao.contentCountDao(kw).size();
			int pageSize = 15;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			List<PlayDto> list = playdao.contentsearchDao(kw, nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);
		} else if (bd.equals("pWriter")) {
			int total = playdao.writerCountDao(kw).size();
			int pageSize = 15;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			List<PlayDto> list = playdao.writersearchDao(kw, nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);
		} else {
			int total = playdao.listCountDao().size();
			int pageSize = 15;

			int totalPage = total / pageSize;

			if (total % pageSize > 0) {
				totalPage++;
			}

			String sPage = request.getParameter("page");
			int page = sPage == null ? 1 : Integer.parseInt(sPage);

			List<PlayDto> list = playdao.pageDao(page, pageSize);

			int nStart = (page - 1) * pageSize + 1;
			int nEnd = (page - 1) * pageSize + pageSize;

			list = playdao.pageDao(nEnd, nStart);
			model.addAttribute("list", list);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw1);
			model.addAttribute("bd", bd);
		}

		return "playboard/playlist";
	}

	/////////////////////////////////////////////////////////////////////////////////////////

	@RequestMapping("/admin/adminbd")
	public String adAllBoards(HttpServletRequest request, Model model) {

		int total = addao.listCountDao().size();
		int pageSize = 16;

		int totalPage = total / pageSize;

		if (total % pageSize > 0) {
			totalPage++;
		}

		String sPage = request.getParameter("page");
		int page = sPage == null ? 1 : Integer.parseInt(sPage);

		int nStart = (page - 1) * pageSize + 1;
		int nEnd = (page - 1) * pageSize + pageSize;

		List<AdDto> list = addao.pageDao(nEnd, nStart);

		model.addAttribute("list", list);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("page", page);

		return "adminboard/adminbd";

	}

	@RequestMapping("/admin/bddelete")
	public String deletead(HttpServletRequest request, Model model) {
		String bn = (String) request.getParameter("boardname");
		String no = (String) request.getParameter("boardno");
		String wb = "";
		if (bn.equals("playboard")) {
			addao.adDelete(bn, wb = "f_number", no);
		} else if (bn.equals("b1board")) {
			addao.adDelete(bn, wb = "b1_number", no);
		} else if (bn.equals("b2board")) {
			addao.adDelete(bn, wb = "b2_number", no);
		}

		return "redirect:adminbd";
	}

	@RequestMapping("/admin/member")
	public String admin_member(HttpServletRequest request, Model model) {
		String field = request.getParameter("Searchfield");
		String search = request.getParameter("Search");
		int total = 0;
		if (search == null) {
			total = buserDao.listDao().size();
		} else {
			total = buserDao.searchUser(field, search).size();
		}

		int pageSize = 10;

		int totalPage = total / pageSize;

		if (total % pageSize > 0) {
			totalPage++;
		}

		String sPage = request.getParameter("page");
		int page = sPage == null ? 1 : Integer.parseInt(sPage);
		int nStart = (page - 1) * pageSize + 1;
		int nEnd = (page - 1) * pageSize + pageSize;

		List<BuserDto> list;

		if (field == null) {
			list = buserDao.pageDao(nEnd, nStart);
		} else {
			list = buserDao.pSU(field, search, nEnd, nStart);
		}

		model.addAttribute("field", field);
		model.addAttribute("search", search);
		model.addAttribute("userList", list);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("page", page);

		return "adminboard/adminmember";
	}

	@RequestMapping("/admin/member_profile")
	public String member_modify1(HttpServletRequest request, Model model) {
		int m_number = Integer.parseInt(request.getParameter("m_number"));
		model.addAttribute("user", buserDao.selectUser(m_number));
		return "adminboard/adminmember_profile";
	}

	@RequestMapping("/admin/member_modify")
	public String member_modify(HttpServletRequest request) {
		try {
			int m_number = Integer.parseInt(request.getParameter("M_NUMBER"));
			String PHONENUMBER = request.getParameter("phone1") + "-" + request.getParameter("phone2") + "-"
					+ request.getParameter("phone3");

			String dateString = request.getParameter("S_DATE");
			Timestamp S_DATE = null;
			if (dateString.equals("")) {
				if (request.getParameter("PASSWORD").equals(request.getParameter("pw2"))) {
					buserDao.updateUser5(request.getParameter("ID"), request.getParameter("NAME"),
							request.getParameter("ADDRESS"), request.getParameter("EMAIL"), PHONENUMBER,
							request.getParameter("NICKNAME"), request.getParameter("BBANG"),
							request.getParameter("S_COMMENT"), m_number);
				} else {
					String encoded = PasswordEncoderFactories.createDelegatingPasswordEncoder()
							.encode(request.getParameter("PASSWORD"));
					String password = encoded.substring(8);

					buserDao.updateUser6(request.getParameter("ID"), password, request.getParameter("NAME"),
							request.getParameter("ADDRESS"), request.getParameter("EMAIL"), PHONENUMBER,
							request.getParameter("NICKNAME"), request.getParameter("BBANG"),
							request.getParameter("S_COMMENT"), m_number);

				}
			} else {
				String pattern = "yyyy-MM-dd";

				SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
				Date parsedDate = dateFormat.parse(dateString);

				S_DATE = new Timestamp(parsedDate.getTime());

				if (request.getParameter("PASSWORD").equals(request.getParameter("pw2"))) {
					buserDao.updateUser3(request.getParameter("ID"), request.getParameter("NAME"),
							request.getParameter("ADDRESS"), request.getParameter("EMAIL"), PHONENUMBER,
							request.getParameter("NICKNAME"), request.getParameter("BBANG"),
							request.getParameter("S_COMMENT"), S_DATE, m_number);
				} else {
					String encoded = PasswordEncoderFactories.createDelegatingPasswordEncoder()
							.encode(request.getParameter("PASSWORD"));
					String password = encoded.substring(8);

					buserDao.updateUser4(request.getParameter("ID"), password, request.getParameter("NAME"),
							request.getParameter("ADDRESS"), request.getParameter("EMAIL"), PHONENUMBER,
							request.getParameter("NICKNAME"), request.getParameter("BBANG"),
							request.getParameter("S_COMMENT"), S_DATE, m_number);
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/admin/member?page=1";
	}

	@RequestMapping("/admin/adminshop")
	public String adminshopView(HttpServletRequest request, Model model) {
		String sPage = request.getParameter("page");
		int page = sPage == null ? 1 : Integer.parseInt(sPage);
		try {

			String field = request.getParameter("Searchfield");
			String search = request.getParameter("Searchdata");

			if (field.equals("buy_number")) {

				return "redirect:buysearch?Searchdata=" + search + "&Searchfield=" + field + "&page=" + page;
			} else if (field.equals("m_number")) {

				return "redirect:membersearch?Searchdata=" + search + "&Searchfield=" + field + "&page=" + page;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		int total = shopDao.listCountDao().size();
		int pageSize = 8;

		int totalPage = total / pageSize;

		if (total % pageSize > 0) {
			totalPage++;

		}
		int nStart = (page - 1) * pageSize + 1;
		int nEnd = (page - 1) * pageSize + pageSize;

		List<ShopDto> list1 = shopDao.pageDao(nEnd, nStart);
		model.addAttribute("list", list1);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("page", page);

		model.addAttribute("shoplist", shopDao.listDao2());

		return "adminboard/adminshop";
	}

	@RequestMapping("/admin/approveCancel")
	public String approveCancel(HttpServletRequest request, Model model) {

		int buy_number = Integer.parseInt(request.getParameter("buy_number"));
		int m_number = Integer.parseInt(request.getParameter("m_number"));
		int t_count = Integer.parseInt(request.getParameter("t_count"));

		shopDao.deleteDao(buy_number, m_number);
		buserDao.minusTicket(t_count, m_number);

		return "redirect:adminshop?m_number=" + m_number;

	}

	@RequestMapping("/admin/refuseCancel")
	public String refuseCancel(HttpServletRequest request, Model model) {

		int buy_number = Integer.parseInt(request.getParameter("buy_number"));
		int m_number = Integer.parseInt(request.getParameter("m_number"));
		shopDao.refuseDao(buy_number, m_number);
		return "redirect:adminshop?m_number=" + m_number;

	}

	@RequestMapping("/admin/buysearch")
	public String shopSearch(HttpServletRequest request, Model model) {

		String kw1 = request.getParameter("Searchdata");
		String kw = "%" + kw1 + "%";
		int total = shopDao.buyCountDao(kw).size();
		int pageSize = 8;

		int totalPage = total / pageSize;

		if (total % pageSize > 0) {
			totalPage++;
		}

		String sPage = request.getParameter("page");
		int page = sPage == null ? 1 : Integer.parseInt(sPage);

		int nStart = (page - 1) * pageSize + 1;
		int nEnd = (page - 1) * pageSize + pageSize;

		List<ShopDto> list = shopDao.buysearchDao(kw, nEnd, nStart);
		model.addAttribute("list", list);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("page", page);
		model.addAttribute("shoplist", shopDao.listDao2());
		
		return "adminboard/adminshop";

	}

	@RequestMapping("/admin/membersearch")
	public String shopSearch2(HttpServletRequest request, Model model) {

		String kw1 = request.getParameter("Searchdata");
		String kw = "%" + kw1 + "%";

		int total = shopDao.memberCountDao(kw).size();
		int pageSize = 8;

		int totalPage = total / pageSize;

		if (total % pageSize > 0) {
			totalPage++;
		}

		String sPage = request.getParameter("page");
		int page = sPage == null ? 1 : Integer.parseInt(sPage);

		int nStart = (page - 1) * pageSize + 1;
		int nEnd = (page - 1) * pageSize + pageSize;

		List<ShopDto> list = shopDao.membersearchDao(kw, nEnd, nStart);

		model.addAttribute("list", list);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("page", page);
		model.addAttribute("shoplist", shopDao.listDao2());

		return "adminboard/adminshop";
	}

}