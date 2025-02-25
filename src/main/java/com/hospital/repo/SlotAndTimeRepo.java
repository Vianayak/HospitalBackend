package com.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.SlotAndTimeModel;


@Repository
public interface SlotAndTimeRepo extends JpaRepository<SlotAndTimeModel, Integer>{
	
	List<SlotAndTimeModel> findByTabletId(int tabletId);


}
