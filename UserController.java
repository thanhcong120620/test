package projectSpringboot.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import projectSpringboot.dto.LotteryDTO;
import projectSpringboot.dto.UserDTO;
import projectSpringboot.exception.CanNotCreateUserException;
import projectSpringboot.exception.CanNotFindUserException;
import projectSpringboot.exception.UserCanNotDeleteException;
import projectSpringboot.exception.UserCanNotUpdateAdminException;
import projectSpringboot.service.IUserService;

@Controller
public class UserController {

	@Autowired
	private IUserService userService;
	

	
	@GetMapping("/")
	public String test(Model model) {
		UserDTO dto = new UserDTO();
		model.addAttribute("dto",dto);		
		return "redirect:/login";
//		return "login";
	}
	
	
/*-----------------------------login + register + password support---------------------------------------*/	
	
	
	
	/*
	 * send new password by mail
	 * */
	@RequestMapping(value = "/passwordMail", method = RequestMethod.POST)
	public String forgetPassword(@ModelAttribute("dtoUser") UserDTO dtoUser, Model model) {
		System.out.println("Model: "+model.toString());		
		try {
			userService.changeAndSendPwdByMail(dtoUser);
			System.out.println("Send success");
			return "redirect:/login";
		} catch (CanNotFindUserException e) {
			System.out.println("Send fail");
			String s = ""+e;
			String[] output = s.split(":");
			String errorMessage = output[1];
			model.addAttribute("errorMessage",errorMessage);
			System.out.println(errorMessage);
			return "forgetPassword";
		}
	}
	
	
	
	/*
	 * create new User
	 * */
	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public String newUser(@ModelAttribute("dtoUser") UserDTO dtoUser, Model model) {
		System.out.println("Model: "+model.toString());		
		try {
			userService.createNewUserByRegister(dtoUser);
			System.out.println("create success");
			return "redirect:/login";
		} catch (CanNotCreateUserException e) {
			System.out.println("create fail");
			String s = ""+e;
			String[] output = s.split(":");
			String errorMessage = output[1];
			model.addAttribute("errorMessage",errorMessage);
			System.out.println(errorMessage);
			
			UserDTO dtoUser2 = new UserDTO();
			model.addAttribute("dtoUser",dtoUser2);	
			return "register";
		}
	}
	
	
	/*
	 * change password
	 * */
	@RequestMapping(value = "/passwordSupport", method = RequestMethod.POST)
	public String passwordSupport(@ModelAttribute("dtoUser") UserDTO dtoUser, Model model) {
		System.out.println("Model: "+model.toString());		
		try {
			userService.updateUserPasswordByMailUser(dtoUser);
			System.out.println("Change success");
			return "redirect:/login";
		} catch (CanNotFindUserException e) {
			System.out.println("Change fail");
			String s = ""+e;
			String[] output = s.split(":");
			String errorMessage = output[1];
			model.addAttribute("errorMessage",errorMessage);
			System.out.println(errorMessage);
			return "passwordSupport";
		}
	}
	
	
	/*
	 * login to data
	 * */
	@RequestMapping(value = "/loginUser", method = RequestMethod.POST)
	public String saveUser(@ModelAttribute("dtoUser") UserDTO dtoUser, Model model) {
		
		 System.out.println(dtoUser.toString());
		System.out.println("Url: /loginUser");
		//Save Lottery Board
		try {
			UserDTO dto =  userService.getUserByMailUser(dtoUser);
//			model.addAttribute("dtoUser",dto);
			System.out.println("Login success");
			
			//if admin to move admin page
			if(dto.getRoleUser().equals("admin")) {
				return "redirect:/admin";
			}		
			// if customer to move landing page
			return "landingPage";
			
		} catch (CanNotFindUserException e) {
			System.out.println("Login fail");
			String s = ""+e;
			String[] output = s.split(":");
			String errorMessage = output[1];
			model.addAttribute("errorMessage",errorMessage);
			System.out.println(errorMessage);
//			e.printStackTrace();
			return "login";
			
		}
		
		
	}
	
	
/*Move to page*/	
	
	@GetMapping("/login")
	public String viewLogin(Model model) {
		UserDTO dtoUser = new UserDTO();
		model.addAttribute("dtoUser",dtoUser);		

		return "login";
	}
	
	@GetMapping("/register")
	public String viewRegister(Model model) {
		UserDTO dtoUser = new UserDTO();
		model.addAttribute("dtoUser",dtoUser);		

		return "register";
	}
	
	@GetMapping("/passwordSupport")
	public String viewPasswordSupport(Model model) {
		UserDTO dtoUser = new UserDTO();
		model.addAttribute("dtoUser",dtoUser);		

		return "passwordSupport";
	}
	
	@GetMapping("/forgetPassword")
	public String viewForgetPassword(Model model) {
		UserDTO dtoUser = new UserDTO();
		model.addAttribute("dtoUser",dtoUser);		

		return "forgetPassword";
	}
	
//	@GetMapping("/cusLottery")
//	public String viewCusHistory(Model model) {
//		UserDTO dtoUser = new UserDTO();
//		model.addAttribute("dtoUser",dtoUser);		
//
//		return "cusLottery";
//	}
	
	
	
	
/*-----------------------------------------User data----------------------------------------------------*/	
	
	/*
	 * Reset password
	 * */
	@RequestMapping(value = "/resetPassword/{id}", method = RequestMethod.GET)
	public String resetPassword(@PathVariable(name="id") long id, Model model) {
		UserDTO dto = userService.resetPassword(id);
		model.addAttribute("dtoUser",dto);
		System.out.println("oke id: "+id+"  & "+dto.getPasswordUser());
		
		return "userUpdate";
	}
	
	
	
	/*
	 * Save User into database
	 * */
	@RequestMapping(value = "/updateAndSave", method = RequestMethod.POST)
	public String updateAndSave(@ModelAttribute("dtoUser") UserDTO dto, Model model) {
		
		System.out.println("Id in save: "+dto.getId());
		
//		userService.save(dto);
//		return "redirect:/UserManager";
		try {
			System.out.println("run try !");
			userService.save(dto);
			return "redirect:/UserManager";
		} catch (UserCanNotUpdateAdminException e) {
			System.out.println("run catch !");
			UserDTO dto2 = dto;
			dto2.setRoleUser("customer");
			model.addAttribute("dtoUser",dto2);
			
			//Convert error information
			String s = ""+e;
			String[] output = s.split(":");
			String errorMessage = output[1];
			model.addAttribute("errorMessage",errorMessage);
			System.out.println(errorMessage);
			
			return "userUpdate";
		}
		
	}
	
	
	
	
	/* 
	 * UpdateSave Lottery into database
	 * */
	@GetMapping("/editUser/{id}")
	public String showLotteryById(@PathVariable(name="id") long id, Model model) {
		UserDTO dto = userService.getUserById(id);
		System.out.println("Id in edit: "+dto.getId());
		model.addAttribute("dtoUser",dto);
		return "userUpdate";
	}
	
	
	
	/*
	 * Search User--------------------------------------------------------------------------
	 * */
	@RequestMapping(value = "/searchUser/{pageNumber}", method = RequestMethod.POST)
	public String searchUser(HttpServletRequest request, 
			@PathVariable int pageNumber,@ModelAttribute("dtoUser") UserDTO dtoReturn, Model model) {
//		List<LotteryDTO> dtoList = new ArrayList<>();
		List<UserDTO> list = userService.searchList(dtoReturn);
//		String errorMessage = "Kết quả được hiển thị";

		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("userlist");	
		int pagesize = 3;	
		pages = new PagedListHolder<>(list);	
		pages.setPageSize(pagesize);	
			
		final int goToPage = pageNumber - 1;	
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);	
		}	
		request.getSession().setAttribute("userlist", pages);	
		int current = pages.getPage() + 1;	
		int begin = Math.max(1, current - list.size());	
		int end = Math.min(begin + 5, pages.getPageCount());	
		int totalPageCount = pages.getPageCount();	
		String baseUrl = "/UserManager/page/";	
		model.addAttribute("beginIndex", begin);	
		model.addAttribute("endIndex", end);	
		model.addAttribute("currentIndex", current);	
		model.addAttribute("totalPageCount", totalPageCount);	
		model.addAttribute("baseUrl", baseUrl);	
		model.addAttribute("dtoUserList", pages);


//		model.addAttribute("dtoList",dtoList);
//		model.addAttribute("errorMessage",errorMessage);


		LotteryDTO dto = new LotteryDTO();
		model.addAttribute("dtoUser",dto);
		
		return "userMng";
	}
	
	
	
	
	
	/* 
	 * delete one User to database-----------------------------------------------------------------
	 * */
	@GetMapping("/deleteUser/{pageNumber}/{id}")
	public String deleteOne(@PathVariable(name="id") long id, Model model, 
			HttpServletRequest request, @PathVariable int pageNumber) {
		
		try {
			userService.deleteOne(id);
			System.out.println("run try !");
			//get back data
			
			return "redirect:/UserManager";
			
		} catch (UserCanNotDeleteException e) {
			System.out.println("run catch !");
			//get back data
			PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("userlist");
			int pagesize = 3;
			List<UserDTO> list =(List<UserDTO>) userService.findAllUser();
			
			System.out.println(list.size());
			if (pages == null) {
				pages = new PagedListHolder<>(list);
				pages.setPageSize(pagesize);
			} else {
				final int goToPage = pageNumber - 1;
				if (goToPage <= pages.getPageCount() && goToPage >= 0) {
					pages.setPage(goToPage);
				}
			}
			request.getSession().setAttribute("userlist", pages);
			int current = pages.getPage() + 1;
			int begin = Math.max(1, current - list.size());
			int end = Math.min(begin + 5, pages.getPageCount());
			int totalPageCount = pages.getPageCount();
			String baseUrl = "/UserManager/page/";

			model.addAttribute("beginIndex", begin);
			model.addAttribute("endIndex", end);
			model.addAttribute("currentIndex", current);
			model.addAttribute("totalPageCount", totalPageCount);
			model.addAttribute("baseUrl", baseUrl);
			model.addAttribute("dtoUserList", pages);

			
			LotteryDTO dto = new LotteryDTO();
			model.addAttribute("dtoUser",dto);
			
			
			model.addAttribute("errorMessage", "Không thể xóa admin: "+id);
//			return "redirect:/UserManager";
			return "userMng";
		}
		
	}
	
	
	/* 
	 * delete many User into database---------------------------------------------------------------
	 * */
	@RequestMapping(value = "/deleteManyUser/{pageNumber}", method = RequestMethod.POST)
	public String deleteMany(@ModelAttribute("deleteTotalUser") String deleteTotalUser, Model model, 
			HttpServletRequest request, @PathVariable int pageNumber) {
		
		try {
			userService.deleteMany(deleteTotalUser);
			System.out.println("run try !");
			//get back data
			
			return "redirect:/UserManager";
			
		} catch (UserCanNotDeleteException e) {
			
			System.out.println("run catch !");
			//Convert error information
			String s = ""+e;
			
			String[] output = s.split(":");
			String errorMessage = output[1];
	
			
			//get back data
			PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("userlist");
			int pagesize = 3;
			List<UserDTO> list =(List<UserDTO>) userService.findAllUser();
			
			System.out.println(list.size());
			if (pages == null) {
				pages = new PagedListHolder<>(list);
				pages.setPageSize(pagesize);
			} else {
				final int goToPage = pageNumber - 1;
				if (goToPage <= pages.getPageCount() && goToPage >= 0) {
					pages.setPage(goToPage);
				}
			}
			request.getSession().setAttribute("userlist", pages);
			int current = pages.getPage() + 1;
			int begin = Math.max(1, current - list.size());
			int end = Math.min(begin + 5, pages.getPageCount());
			int totalPageCount = pages.getPageCount();
			String baseUrl = "/UserManager/page/";

			model.addAttribute("beginIndex", begin);
			model.addAttribute("endIndex", end);
			model.addAttribute("currentIndex", current);
			model.addAttribute("totalPageCount", totalPageCount);
			model.addAttribute("baseUrl", baseUrl);
			model.addAttribute("dtoUserList", pages);

			
			LotteryDTO dto = new LotteryDTO();
			model.addAttribute("dtoUser",dto);
			
			
			model.addAttribute("errorMessage", errorMessage);
//			return "redirect:/UserManager";
			return "userMng";
			
		}
//		return "redirect:/UserManager";
	}
	
	
	/*
	 * View page 1 ----------------------------------------------------------------------------
	 * */
	@GetMapping("/UserManager")
	public String viewAdminIndex(Model model,HttpServletRequest request
			,RedirectAttributes redirect) {
		request.getSession().setAttribute("userlist", null);
		
		if(model.asMap().get("success") != null)
			redirect.addFlashAttribute("success",model.asMap().get("success").toString());

		return "redirect:/UserManager/page/1";
    }
	
	@GetMapping("/UserManager/page/{pageNumber}")
	public String showEmployeePage(HttpServletRequest request, 
			@PathVariable int pageNumber, Model model) {
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("userlist");
		int pagesize = 3;
		List<UserDTO> list =(List<UserDTO>) userService.findAllUser();
		
		System.out.println(list.size());
		if (pages == null) {
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pagesize);
		} else {
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}
		request.getSession().setAttribute("userlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/UserManager/page/";

		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("dtoUserList", pages);
		
		LotteryDTO dto = new LotteryDTO();
		model.addAttribute("dtoUser",dto);
		
		return "userMng";
	}
	
	
	
	
	
	
}
