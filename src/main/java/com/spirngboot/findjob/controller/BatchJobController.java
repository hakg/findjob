package com.spirngboot.findjob.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/batch")
public class BatchJobController {
    private final JobLauncher jobLauncher;
    private final Job saramInCrawlingJob;

    public BatchJobController(JobLauncher jobLauncher, Job saramInCrawlingJob) {
        this.jobLauncher = jobLauncher;
        this.saramInCrawlingJob = saramInCrawlingJob;
    }

    @PostMapping("/crawl")
    public ResponseEntity<Map<String, String>> runCrawlingJob(
        @RequestParam(defaultValue = "java") String keyword
    ) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                .addString("keyword", keyword)
                .addDate("timestamp", new Date())
                .toJobParameters();

            jobLauncher.run(saramInCrawlingJob, jobParameters);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Crawling job started for keyword: " + keyword);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Job execution failed: " + e.getMessage());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/crawl/multiple")
    public ResponseEntity<Map<String, String>> runMultiKeywordCrawlingJob(
        @RequestParam(defaultValue = "java") String keywords
    ) {
        try {
            String[] keywordList = keywords.split(",");
            
            for (String keyword : keywordList) {
                JobParameters jobParameters = new JobParametersBuilder()
                    .addString("keyword", keyword.trim())
                    .addDate("timestamp", new Date())
                    .toJobParameters();

                jobLauncher.run(saramInCrawlingJob, jobParameters);
            }

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Crawling jobs started for keywords: " + keywords);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Job execution failed: " + e.getMessage());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}