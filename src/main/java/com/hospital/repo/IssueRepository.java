package com.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.Issue;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByIssueNameContainingIgnoreCase(String keyword);
    List<Issue> findByIdIn(List<Long> ids);
}
