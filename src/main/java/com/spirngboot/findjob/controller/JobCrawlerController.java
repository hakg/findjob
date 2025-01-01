package com.spirngboot.findjob.controller;

import com.spirngboot.findjob.dto.JobPosting;
import com.spirngboot.findjob.service.SaramInCrawlerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobCrawlerController {
    private final SaramInCrawlerService crawlerService;

    public JobCrawlerController(SaramInCrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping("/crawl/jobs")
    public List<JobPosting> crawlJobs(@RequestParam String keyword) {
        return crawlerService.crawlJobPostings(keyword);
    }
}