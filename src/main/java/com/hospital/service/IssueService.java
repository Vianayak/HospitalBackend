package com.hospital.service;

import java.util.List;

import com.hospital.model.Issue;

public interface IssueService {

	
	public List<Issue> getIssuesByKeyword(String keyword);

	List<Issue> getIssuesByIds(List<Long> issueIds);

	public List<Issue> getAllIssues();
}
