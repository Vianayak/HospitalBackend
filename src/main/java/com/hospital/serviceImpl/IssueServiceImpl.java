package com.hospital.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import com.hospital.model.Issue;
import com.hospital.repo.IssueRepository;
import com.hospital.service.IssueService;

@Service
public class IssueServiceImpl implements IssueService {
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Override
	public List<Issue> getIssuesByKeyword(String keyword) {
        return issueRepository.findByIssueNameContainingIgnoreCase(keyword);
    }
	@Override
	public List<Issue> getIssuesByIds(List<Long> issueIds) {
        return issueRepository.findAllById(issueIds);
    }
	@Override
	public List<Issue> getAllIssues() {
		// TODO Auto-generated method stub
		return issueRepository.findAll();
	}

}
