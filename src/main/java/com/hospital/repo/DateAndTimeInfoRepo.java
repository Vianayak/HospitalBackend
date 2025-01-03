package com.hospital.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.DateAndTimeInfo;
@Repository
public interface DateAndTimeInfoRepo extends JpaRepository<DateAndTimeInfo, Integer>{

}
