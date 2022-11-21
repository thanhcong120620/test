package projectSpringboot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import projectSpringboot.dto.UserDTO;
import projectSpringboot.entity.CusHistoryEntity;
import projectSpringboot.exception.CanNotFindUserException;
import projectSpringboot.service.ICusHistoryService;
import projectSpringboot.service.IUserService;

@Controller
public class TestController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICusHistoryService ChService;
	


	/*
	 * Url from landing page to cusLottery
	 * */
	@GetMapping("/CusHistory")
	public String viewCusHistoryIndex(Model model) {
		UserDTO dtoUser = new UserDTO();
		model.addAttribute("dtoUser",dtoUser);		

		return "cusLottery2";
    }
	
	/*
	 * display list of cusLottery
	 * */
	@PostMapping("/test")
	public String viewHomePage(Model model, @ModelAttribute("dtoUser") UserDTO dtoUserCl) {
		UserDTO dtoUserSer = dtoUserCl;
		System.out.println("dtoUserSer/test: "+dtoUserSer.toString());
		model.addAttribute("dtoUser",dtoUserSer);	
	 return findPaginated(1, model, dtoUserCl);  
	}
	//go to pageNo
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model, @ModelAttribute("dtoUser") UserDTO dtoUserCl) {
	    int pageSize = 2;
	    
	    System.out.println("pageNo: "+pageNo);
	    System.out.println("dtoUserCl in page no: "+pageNo+": "+dtoUserCl.toString());
	    
//	    UserDTO dto;
		try {
			UserDTO dto = userService.getUserByMailUser(dtoUserCl);
		    Page<CusHistoryEntity> page = ChService.findPaginated(dto.getId(),pageNo, pageSize);
		    List<UserDTO> listCusLottery = ChService.findAllByUser_id2(dto.getId(), pageNo, pageSize);

		    model.addAttribute("currentPage", pageNo);
		    model.addAttribute("totalPages", page.getTotalPages());
		    model.addAttribute("totalItems", page.getTotalElements());
		    model.addAttribute("listCusLottery", listCusLottery);
//		    System.out.println("run findPaginated (controller) ");
		    
		  //push data to client
			String nameUser = dto.getNameUser();
			String mailUser = dto.getMailUser();
			String phone = dto.getPhone();
			String addressUser = dto.getAddressUser();
			model.addAttribute("nameUser",nameUser);
			model.addAttribute("mailUser",mailUser);
			model.addAttribute("phone",phone);
			model.addAttribute("addressUser",addressUser);
			
			UserDTO dtoUserSer = dtoUserCl;
			System.out.println("dtoUserSer: "+dtoUserSer.toString());
			model.addAttribute("dtoUser",dtoUserSer);	
			
		    return "cusLottery2";
		} catch (CanNotFindUserException e) {
//			System.out.println("run findPaginated catch  (controller) ");
			System.out.println("Login fail");
			String s = ""+e;
			String[] output = s.split(":");
//			String errorMessage = output[1];
			String errorMessage = "Tài khoản hoặc mật khẩu không đúng";
			model.addAttribute("errorMessage",errorMessage);
			model.addAttribute("nameUser","Đăng nhập thất bại");
			System.out.println(errorMessage);
			
//			UserDTO dtoUserSer = new UserDTO();
//			model.addAttribute("dtoUser",dtoUserSer);	
			return "cusLottery2";
		}
	    
	}
	
	
	
}
