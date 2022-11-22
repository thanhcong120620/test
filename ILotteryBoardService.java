package projectSpringboot.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import projectSpringboot.dto.LotteryDTO;

public interface ILotteryBoardService {

	LotteryDTO save(LotteryDTO lotteryDTO);
	void deleteMany(String ids);
	void deleteOne(long id);
	List<LotteryDTO> findAllLB();
	List<LotteryDTO> searchList(LotteryDTO lotteryDTO);
	LotteryDTO getLotteryBoardById(long idList);
	List<LotteryDTO> findAll(Pageable pageable);
//	Page<LotteryDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	int totalItem();
	
	List<LotteryDTO> findAllByRegion_id(String region_id);
	
	
}
