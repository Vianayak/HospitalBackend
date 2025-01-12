package com.hospital.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.model.Users;

public interface UserRepo extends JpaRepository<Users, Integer>{

	Users findByEmail(String email);

}
