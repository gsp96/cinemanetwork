package koseungpyo.movie.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import koseungpyo.movie.domain.User;
import koseungpyo.movie.service.UserService;

@RestController("koseungpyo.user.controller")
@RequestMapping("user")
public class UserController {
	@Autowired private UserService userService;
	
	@GetMapping("loginView")
	public ModelAndView loginView(ModelAndView mv) {
		mv.setViewName("user/login");
		return mv;
	}
	
	@PostMapping("login")
	public void login(@RequestParam("userId") String userId, @RequestParam("userPw") String pw, Model model,String idRemember, HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = userService.getUser(userId, pw);
		session.setAttribute("userId", user.getUserId());
		if(user.getUserId().equals("admin")) {
			model.addAttribute("admin",user);
		}else {
			model.addAttribute("user",user);
		}
	}
}