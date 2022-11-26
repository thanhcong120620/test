package Security.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import Security.entity.User;

@Controller
public class TestController {
	
//	@Autowired
//	User user;
	
	@GetMapping("/")
	public String test(Model model) {
		model.addAttribute("dto","Oke");		
		return "hello";
	}
	
	@GetMapping("/admin")
	public String admin(Model model, @ModelAttribute("userDTO") User userDTO, Principal principal, Authentication authentication, HttpServletRequest request) {
//		User loginedUser = (User) ((Authentication) principal).getPrincipal();

//		String userInfo = WebUtils.toString(loginedUser);
//		System.out.println("loginedUser: "+loginedUser.toString());
		 Principal principal1 = request.getUserPrincipal();
	        System.out.println("principal.getName: "+principal1.getName());
//	    if(principal1.getName() == null) {
//	    	model.addAttribute("errorMessage","Tài khoản không tồn tại");
//	    	System.out.println("Tài khoản không tồn tại");
//	    	return "login";
//	    } 
	    
		User user = new User();
		model.addAttribute("userDTO",user);
		System.out.println("userDTO: "+userDTO.toString());
		return "admin";
	}

	@GetMapping("/user")
	public String user() {
		return "user";
	}

	@GetMapping("/all")
	public String all() {
		return "hello";
	}
	@GetMapping("/login")
	public String login(Model model, @ModelAttribute("userDTO") User userDTO, 
			@RequestParam(value = "error", required = false) boolean error) {    
	        
		
    if(error) {
    	model.addAttribute("errorMessage","Tài khoản không tồn tại");
    	System.out.println("Tài khoản không tồn tại");
    	return "login";
    } 
		
		User user = new User();
		model.addAttribute("userDTO",user);
//		System.out.println("userDTO: "+userDTO.toString());
		return "login";
	}
	

    @GetMapping("/admin/adminHome")
    public String adminHome() {
	
        return "adminHome";
    }
}
