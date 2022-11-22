package projectSpringboot.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projectSpringboot.converter.LotteryBoardConverter;
import projectSpringboot.converter.RegionConverter;
import projectSpringboot.dto.LotteryDTO;
import projectSpringboot.entity.LotteryBoardEntity;
import projectSpringboot.entity.RegionEntity;
import projectSpringboot.repository.LotteryBoardRepository;
import projectSpringboot.repository.RegionRepository;
import projectSpringboot.service.ILotteryBoardService;

@Service
@Transactional
public class LotteryBoardService implements ILotteryBoardService{

	
	@Autowired
	LotteryBoardRepository lotteryBoardRepository;
	
	@Autowired
	RegionRepository regionRepository;
	
	@Autowired
	private LotteryBoardConverter lotteryBoardConverter;
	
	@Autowired
	private RegionConverter regionConverter;
	

	@Override
	public LotteryDTO save(LotteryDTO lotteryDTO) {
		
		RegionEntity regionEntity = new RegionEntity();
		
		RegionEntity oldEntity = regionRepository.findOneByRegionCode(lotteryDTO.getRegionCode());
		
		if(oldEntity != null) {
			RegionEntity oldEntity2 = new RegionEntity();
			oldEntity2.setId(oldEntity.getId());
			oldEntity2.setNameProvince(oldEntity.getNameProvince());
			oldEntity2.setNameRegion(oldEntity.getNameRegion());
			oldEntity2.setRegionCode(oldEntity.getRegionCode());
			
			regionEntity = regionConverter.toEntity(lotteryDTO, oldEntity2);
		} else {
			regionEntity = regionConverter.toEntity(lotteryDTO);
		}
		
		regionEntity = regionRepository.save(regionEntity);
		
		
		
		LotteryBoardEntity lotteryBoardEntity = new LotteryBoardEntity();
		
		if(lotteryDTO.getId() != null) {
			System.out.println("test start: "+lotteryDTO.getId());
			LotteryBoardEntity oldEntityLB = lotteryBoardRepository.findById(lotteryDTO.getId()).get();
			
			System.out.println("oldEntityLB: "+oldEntityLB.toString());
			System.out.println("test end");
			lotteryBoardEntity = lotteryBoardConverter.toEntity(lotteryDTO, oldEntityLB);
		} else {
			lotteryBoardEntity = lotteryBoardConverter.toEntity(lotteryDTO);
		}
		RegionEntity regionEntityLB = regionRepository.findOneByRegionCode(lotteryDTO.getRegionCode());
		lotteryBoardEntity.setRegion(regionEntityLB);
		lotteryBoardEntity = lotteryBoardRepository.save(lotteryBoardEntity);
		
		return lotteryBoardConverter.toDTO(lotteryBoardEntity);
	}

	@Override
	public void deleteMany(String idList) {
		String[] output = idList.split("-");
		long[] ids = new long[output.length];
		for(int i=0;i<output.length;i++) {
			ids[i] = Long.parseLong(output[i]);
		}
		for(long item : ids) {
			lotteryBoardRepository.deleteById(item);
			//test
//			throw new  UserNotFoundException("ID: "+item+"is not existed" );
		}
		
	}
	
	@Override
	public void deleteOne(long id) {
		lotteryBoardRepository.deleteById(id);
	}

	@Override
	public LotteryDTO getLotteryBoardById(long id) {
		LotteryBoardEntity lbEntity = lotteryBoardRepository.findById(id).get();
		RegionEntity regionEntity = regionRepository.findOneByRegionCode(lbEntity.getRegionCode());
		LotteryDTO dto = lotteryBoardConverter.toDTO(lbEntity);
		dto.setId(lbEntity.getId());
		dto.setNameProvince(regionEntity.getNameProvince());
		dto.setNameRegion(regionEntity.getNameRegion());
		
		return dto;
	}
	

	@Override
	public List<LotteryDTO> findAllLB() {
		List<LotteryDTO> LotteryDTOList = new ArrayList<>();
		List<LotteryBoardEntity> lbEntityList = lotteryBoardRepository.findAll();
		for(LotteryBoardEntity lbEntity : lbEntityList) {
			RegionEntity regionEntity = regionRepository.findOneByRegionCode(lbEntity.getRegionCode());
			LotteryDTO dto = lotteryBoardConverter.toDTO(lbEntity);
			dto.setId(lbEntity.getId());
			dto.setNameProvince(regionEntity.getNameProvince());
			dto.setNameRegion(regionEntity.getNameRegion());
			LotteryDTOList.add(dto);
		}
		
		return LotteryDTOList;
	}


	@Override
	public List<LotteryDTO> searchList(LotteryDTO lotteryDTO) {
		List<LotteryDTO> LotteryDTOList = new ArrayList<>();
		List<LotteryBoardEntity> lbEntityList = new ArrayList<>();
		RegionEntity regionEntityByCity = regionRepository.findOneByNameProvince(lotteryDTO.getNameProvince());
		
		System.out.println(regionEntityByCity.getRegionCode());
		//List LotteryBoard with city
		lbEntityList = lotteryBoardRepository.findByRegionCode(regionEntityByCity.getRegionCode());
		
		//Check condition with date in dto to choose with year
		for(LotteryBoardEntity lbEntity : lbEntityList) {
			String dmyLB= lbEntity.getDate();
			String[] output = dmyLB.split("-");
			String year = output[2];
			System.out.println(year);
			
			if(lotteryDTO.getYear().equals(year)) {
				RegionEntity regionEntity = regionRepository.findOneByRegionCode(lbEntity.getRegionCode());
				LotteryDTO dto = lotteryBoardConverter.toDTO(lbEntity);
				dto.setId(lbEntity.getId());
				dto.setNameProvince(regionEntity.getNameProvince());
				dto.setNameRegion(regionEntity.getNameRegion());
				LotteryDTOList.add(dto);
			} 

		}
		
		
		return LotteryDTOList;
	}

	
	
	@Override
	public List<LotteryDTO> findAll(Pageable pageable) {
		
		List<LotteryDTO> LotteryDTOList = new ArrayList<>();
		List<LotteryBoardEntity> lbEntityList = lotteryBoardRepository.findAll(pageable).getContent();
		for(LotteryBoardEntity lbEntity : lbEntityList) {
			RegionEntity regionEntity = regionRepository.findOneByRegionCode(lbEntity.getRegionCode());
			LotteryDTO dto = lotteryBoardConverter.toDTO(lbEntity);
			dto.setId(lbEntity.getId());
			dto.setNameProvince(regionEntity.getNameProvince());
			dto.setNameRegion(regionEntity.getNameRegion());
			LotteryDTOList.add(dto);
		}
		
		return LotteryDTOList;
	}

//	@Override
//	public Page<LotteryDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
//		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
//			Sort.by(sortField).descending();
//		
//		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
//		
//		return this.findAll(pageable);
//	}

	@Override
	public int totalItem() {
		return (int) lotteryBoardRepository.count();
	}

	
	/*test query native*/
	@Override
	public List<LotteryDTO> findAllByRegion_id(String region_id) {
		List<LotteryDTO> LotteryDTOList = new ArrayList<>();
		List<LotteryBoardEntity> lbEntityList = new ArrayList<>();
		
		lbEntityList = lotteryBoardRepository.findByRegion_id(region_id);
		
		for(LotteryBoardEntity lbEntity : lbEntityList) {
			RegionEntity regionEntity = regionRepository.findById(Long.parseLong(region_id)).get();
			LotteryDTO dto = lotteryBoardConverter.toDTO(lbEntity);
			dto.setId(lbEntity.getId());
			dto.setNameProvince(regionEntity.getNameProvince());
			dto.setNameRegion(regionEntity.getNameRegion());
			LotteryDTOList.add(dto);
		}
		
		
		return LotteryDTOList;
	}
	
	


	




	
	
	
}
