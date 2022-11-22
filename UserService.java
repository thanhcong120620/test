package projectSpringboot.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projectSpringboot.converter.UserConverter;
import projectSpringboot.dto.UserDTO;
import projectSpringboot.entity.UserEntity;
import projectSpringboot.exception.CanNotCreateUserException;
import projectSpringboot.exception.CanNotFindUserException;
import projectSpringboot.exception.UserCanNotDeleteException;
import projectSpringboot.exception.UserCanNotUpdateAdminException;
import projectSpringboot.model.MD5PassWord;
import projectSpringboot.model.RandomString;
import projectSpringboot.repository.CusHistoryRepository;
import projectSpringboot.repository.UserRepository;
import projectSpringboot.service.IUserService;

@Service
@Transactional
public class UserService implements IUserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	CusHistoryRepository CHRepository;

	@Autowired
	private UserConverter userConverter;
	
    @Autowired
    MailService mailService;
	
	
	
	@Override
	public UserDTO changeAndSendPwdByMail(UserDTO userDTO) throws CanNotFindUserException {
		UserDTO dto = new UserDTO();
		
		// step1: Find user by mail
		UserEntity entity = userRepository.findByMailUser(userDTO.getMailUser());
		
		if (entity == null) {
			throw new CanNotFindUserException("Fail, Mail user is incorrect or not existed !");
		} else {
			
			//Set new password
			// Random new password
			int numberOfCharactor = 10;
			RandomString rand = new RandomString();
			String newPassword = rand.randomPassword(numberOfCharactor);//this is new password to send mail
			System.out.println("Random password: " + newPassword);

			// Hash new password with MD5
			MD5PassWord MG5pass = new MD5PassWord();
			String newHashPassword = MG5pass.setMd5(newPassword);//this is new hash password to save in database
			System.out.println("Hash new password: " + newHashPassword);
			
			//save new password to data base
			entity.setPasswordUser(newHashPassword);
			entity = userRepository.save(entity);
				
			//send mail with new pass word
			String nameUser = entity.getNameUser();
			String mailUser = entity.getMailUser();
			String passwordUser = newPassword;
			System.out.println(passwordUser +"," + nameUser +"," + mailUser);
			mailService.sendMail(nameUser,passwordUser,mailUser);
			
			//set up to back controller
			dto = userConverter.toDTO(entity);
			System.out.println("change success");
			
		}
		
		return dto;
	}
	
	
	
	@Override
	public UserDTO createNewUserByRegister(UserDTO userDTO) throws CanNotCreateUserException {
		UserDTO dto = new UserDTO();
		UserEntity oldEntity = userRepository.findByMailUser(userDTO.getMailUser());
		
		if (oldEntity != null) {
			throw new CanNotCreateUserException("Fail, your user mail is existed !");
		} else {
			UserEntity entity = new UserEntity();
				// transfer password with MD5
				MD5PassWord MG5pass = new MD5PassWord();
				String hashPasswordCU = MG5pass.setMd5(userDTO.getPasswordUser());
				System.out.println("pass: "+ userDTO.getPasswordUser());
				System.out.println("hash pass: "+ hashPasswordCU);
				
				entity = userConverter.toEntity(userDTO);
				entity.setPasswordUser(hashPasswordCU);
				entity = userRepository.save(entity);
				
				dto = userConverter.toDTO(entity);
			
		}
		
		return dto;
	}
	
	
	@Override
	public UserDTO updateUserPasswordByMailUser(UserDTO userDTO) throws CanNotFindUserException {
		UserDTO dto = new UserDTO();
		UserEntity entity = userRepository.findByMailUser(userDTO.getMailUser());
		
		if (entity == null) {
//			System.out.println("Fail, check your mail user again !");
			throw new CanNotFindUserException("Fail, check your mail user again !");
		} else {
			MD5PassWord MG5pass = new MD5PassWord();
			String hashOldPasswordSP = MG5pass.setMd5(userDTO.getOldPassword());
			
			if (!entity.getPasswordUser().equalsIgnoreCase(hashOldPasswordSP)) {
//				System.out.println("Fail, check your phone again !");
				throw new CanNotFindUserException("Fail, check your old password again !");
			} else {
				
				// transfer password with MD5
//				MD5PassWord MG5pass = new MD5PassWord();
				String hashPasswordSP = MG5pass.setMd5(userDTO.getPasswordUser());
				System.out.println("pass: "+ userDTO.getPasswordUser());
				System.out.println("hash pass: "+ hashPasswordSP);
				entity.setPasswordUser(hashPasswordSP);
				entity = userRepository.save(entity);
				
				dto = userConverter.toDTO(entity);
				System.out.println("change success");
			}
		}
		
		System.out.println("dto: " + dto.toString());
		System.out.println("end test: ");
		return dto;
	}
	

	public UserDTO getUserByMailUser(UserDTO userDTO) throws CanNotFindUserException {
		UserDTO dto = new UserDTO();
//		System.out.println("start test with mail user: " + userDTO.getMailUser());
		UserEntity entity = userRepository.findByMailUser(userDTO.getMailUser());
		
		if (entity == null) {
			throw new CanNotFindUserException("Sign in fail, check your mail user and password again !");
		} else {
			// transfer password with MD5
			MD5PassWord MG5pass = new MD5PassWord();
			String hashPasswordLogin = MG5pass.setMd5(userDTO.getPasswordUser());
//			System.out.println("pass: "+ userDTO.getPasswordUser());
//			System.out.println("hash pass: "+ hashPasswordLogin);
			
			if (entity.getMailUser().isEmpty() || !entity.getPasswordUser().equalsIgnoreCase(hashPasswordLogin)) {
//				System.out.println("Sign in fail, check your mail user and password again !");
				throw new CanNotFindUserException("Sign in fail, check your mail user and password again !");
			} else {
				dto = userConverter.toDTO(entity);
//				System.out.println("login success");
			}
		}
		
//		System.out.println("dto: " + dto.toString());
//		System.out.println("end test: ");
		return dto;
	}

	@Override
	public UserDTO save(UserDTO userDTO) throws UserCanNotUpdateAdminException {
		UserEntity userEntity = new UserEntity();
		System.out.println("id1234:" + userDTO.getId());
		if (userDTO.getId() != null) {

			UserEntity oldEntity = userRepository.findById(userDTO.getId()).get();
			UserEntity oldEntity2 = new UserEntity();

			oldEntity2.setAddressUser(oldEntity.getAddressUser());
			oldEntity2.setNameUser(oldEntity.getNameUser());
			oldEntity2.setGenderUser(oldEntity.getGenderUser());
			oldEntity2.setBirthDay(oldEntity.getBirthDay());
			oldEntity2.setPhone(oldEntity.getPhone());
			oldEntity2.setMailUser(oldEntity.getMailUser());
			oldEntity2.setPasswordUser(oldEntity.getPasswordUser());
			oldEntity2.setRoleUser(oldEntity.getRoleUser());
			oldEntity2.setId(oldEntity.getId());

			System.out.println("new entity: " + oldEntity2.toString());
//			userEntity = userConverter.toEntity(userDTO, oldEntity2);
			System.out.println("oldEntity2: " + oldEntity2.toString());

			String oldRole = oldEntity2.getRoleUser();
			String newRole = userDTO.getRoleUser();
			System.out.println("old Role: " + oldRole);
			System.out.println("new Role: " + newRole);
			// User can not updated to admin
			if (oldRole.equals("customer") && newRole.equals("admin")) {
				System.out.println("run if");
				throw new UserCanNotUpdateAdminException("Không thể cập nhật user thành admin");
			} else {
				System.out.println("Run else !");
				userEntity = userConverter.toEntity(userDTO, oldEntity2);
				System.out.println(userDTO.toString());
			}
		} else {
			System.out.println("run else");
			userEntity = userConverter.toEntity(userDTO);
		}
		System.out.println("entity: ");
		System.out.println(userEntity.toString());
		userEntity = userRepository.save(userEntity);

		return userConverter.toDTO(userEntity);
	}

	@Override
	public void deleteMany(String idList) throws UserCanNotDeleteException {

		String[] output = idList.split("-");
		long[] ids = new long[output.length];
		for (int i = 0; i < output.length; i++) {
			ids[i] = Long.parseLong(output[i]);
		}

		for (long item : ids) {
			UserEntity entity = userRepository.findById(item).get();
			String role = entity.getRoleUser();
			System.out.println("role: " + role);

			if (role.equals("admin")) {
				System.out.println("run exception !");
				throw new UserCanNotDeleteException("Không thể xóa Admin " + item);
			}
		}

		for (long item : ids) {

//			UserEntity entity = userRepository.findOneById(item);
			UserEntity entity = userRepository.findById(item).get();
			String role = entity.getRoleUser();
			System.out.println("role: " + role);

			if (role.equals("customer")) {
				System.out.println("run delete ");
				// Step 1: Delete cusHistory
//				CHRepository.deleteUser_id(id);

				// step 2: delete User
				userRepository.deleteById(item);
			}

			else if (role.equals("admin")) {
				System.out.println("run exception !");
				throw new UserCanNotDeleteException("Không thể xóa Admin " + item);
			}

		}

	}

	@Override
	public void deleteOne(long id) throws UserCanNotDeleteException {
//		UserEntity entity = userRepository.findOneById(id);
		UserEntity entity = userRepository.findById(id).get();
		String role = entity.getRoleUser();
		System.out.println("role: " + role);

		if (role.equals("customer")) {
			System.out.println("run delete ");
			// Step 1: Delete cusHistory
//			CHRepository.deleteUser_id(id);
			// step 2: delete User
			userRepository.deleteById(id);
		} else if (role.equals("admin")) {
			System.out.println("run exception !  ");
			throw new UserCanNotDeleteException("message");
		}

	}

	
	@Override
	public List<UserDTO> findAllUser() {
		List<UserDTO> UserDTOList = new ArrayList<>();
		List<UserEntity> userEntityList = userRepository.findAll();

		for (UserEntity item : userEntityList) {
			UserDTO dto = new UserDTO();
			dto = userConverter.toDTO(item);
			UserDTOList.add(dto);
		}

		return UserDTOList;
	}

	@Override
	public List<UserDTO> searchList(UserDTO userDTO) {
		List<UserDTO> UserDTOList = new ArrayList<>();
		List<UserEntity> userListNoRole = new ArrayList<>();

		System.out.println(userDTO.toString());

		// Step 1: Check phone or userName
		String input = userDTO.getUserNameOrPhone();
		String userNameDTO = "";
		String phoneDTO = "";
		String output = "";

		if (input.contains("@")) {
			userNameDTO = input;
			System.out.println("userNameDTO: " + userNameDTO);
		} else if (!input.contains("@")) {
			phoneDTO = input;
			System.out.println("phoneDTO: " + phoneDTO);
		} else {
			output = null;
			System.out.println(output);
		}

		// Step 2: Query User by role to get List with phone/userName and address
		userListNoRole = userRepository.findByRoleUser(userDTO.getRoleUser());
		System.out.println("Check userListNoRole: ");
//		for(UserEntity item : userListNoRole) {
//			System.out.println(item.toString());
//		}

		System.out.println(userDTO.getAddressUser());
		// Step 3: Use address with if and else to get List with phone/userName
		// Convert to new list if address not null
		List<UserEntity> userListNoRoleAddress = new ArrayList<>();

		if (!userDTO.getAddressUser().isEmpty()) {
			for (UserEntity item : userListNoRole) {
				if (item.getAddressUser().equals(userDTO.getAddressUser())) {
					userListNoRoleAddress.add(item);
				}
			}
			// check ok -----

			// Use phone/userName with if and else to get final List (Step 4)
			// check with userName
			if (!userNameDTO.equals("")) {
				System.out.println("---> username run");
				for (UserEntity item : userListNoRoleAddress) {
					if (item.getMailUser().equals(input)) {
						UserDTO dto = new UserDTO();
						dto = userConverter.toDTO(item);
						UserDTOList.add(dto);
					}
				}
				System.out.println("check userName: ");
				for (UserDTO item : UserDTOList) {
					System.out.println(item.toString());
				}
			}
			// check with phone
			else if (!phoneDTO.equals("")) {
				System.out.println("phone run");
				for (UserEntity item : userListNoRoleAddress) {
					if (item.getPhone().equals(input)) {
						UserDTO dto = new UserDTO();
						dto = userConverter.toDTO(item);
						UserDTOList.add(dto);
					}
				}
			}
			// No: phone, user name
			else {
				for (UserEntity item : userListNoRoleAddress) {
					UserDTO dto = new UserDTO();
					dto = userConverter.toDTO(item);
					UserDTOList.add(dto);
				}
			}
		}

		// Address null
		else {
			// Step 4: Use phone/userName with if and else to get final List
			// check with userName
			if (!userNameDTO.equals("")) {
				for (UserEntity item : userListNoRole) {
					if (item.getMailUser().equals(input)) {
						UserDTO dto = new UserDTO();
						dto = userConverter.toDTO(item);
						UserDTOList.add(dto);
					}
				}
			}
			// check with phone
			else if (!phoneDTO.equals("")) {
				for (UserEntity item : userListNoRole) {
					if (item.getPhone().equals(input)) {
						UserDTO dto = new UserDTO();
						dto = userConverter.toDTO(item);
						UserDTOList.add(dto);
					}
				}
				// No: Phone, user name, address
			} else {
				for (UserEntity item : userListNoRole) {
					UserDTO dto = new UserDTO();
					dto = userConverter.toDTO(item);
					UserDTOList.add(dto);
				}
			}

		}

		return UserDTOList;
	}

	@Override
	public UserDTO getUserById(long id) {
		UserEntity userEntity = userRepository.findById(id).get();
		String user_id = "" + id;
		System.out.println("user_id = " + user_id);

		UserDTO dto = userConverter.toDTO(userEntity);

		return dto;
	}

	@Override
	public UserDTO resetPassword(long id) {
		UserDTO dto = new UserDTO();

		// Random new password
		int numberOfCharactor = 10;
		RandomString rand = new RandomString();
		String newPassword = rand.randomPassword(numberOfCharactor);
		System.out.println("Random password: " + newPassword);

		// Hash new password with MD5
		MD5PassWord MG5pass = new MD5PassWord();
		String newHashPassword = MG5pass.setMd5(newPassword);
		System.out.println("Hash new password: " + newHashPassword);

		// save new entity and convert dto
		UserEntity oldEntity = userRepository.findById(id).get();
		UserEntity oldEntity2 = new UserEntity();
		oldEntity2.setAddressUser(oldEntity.getAddressUser());
		oldEntity2.setNameUser(oldEntity.getNameUser());
		oldEntity2.setGenderUser(oldEntity.getGenderUser());
		oldEntity2.setBirthDay(oldEntity.getBirthDay());
		oldEntity2.setPhone(oldEntity.getPhone());
		oldEntity2.setMailUser(oldEntity.getMailUser());
		oldEntity2.setPasswordUser(newHashPassword);
		oldEntity2.setRoleUser(oldEntity.getRoleUser());
		oldEntity2.setId(oldEntity.getId());

		System.out.println("oldEntity2: " + oldEntity2.toString());

		oldEntity2 = userRepository.save(oldEntity2);
		dto = userConverter.toDTO(oldEntity2);

		return dto;
	}


	/*
	 * Query with Pagination 
	 * */

	
	


}
