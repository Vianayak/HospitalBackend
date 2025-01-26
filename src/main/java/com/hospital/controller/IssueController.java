package com.hospital.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.Issue;
import com.hospital.service.IssueService;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*") // Replace with your frontend URL
public class IssueController {

	@Autowired
    private IssueService issueService;

    @GetMapping("/search")
    public ResponseEntity<List<Issue>> searchIssues(@RequestParam String keyword) {
        List<Issue> issues = issueService.getIssuesByKeyword(keyword);
        return ResponseEntity.ok(issues);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Issue>> getIssues(){
    	List<Issue> issues=issueService.getAllIssues();
		return ResponseEntity.ok(issues);
    	
    }
}

