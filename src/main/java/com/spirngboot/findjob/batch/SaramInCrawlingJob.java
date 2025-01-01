package com.spirngboot.findjob.batch;

import com.spirngboot.findjob.dto.JobPosting;
import com.spirngboot.findjob.service.SaramInCrawlerService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class SaramInCrawlingJob {
    private final SaramInCrawlerService crawlerService;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public SaramInCrawlingJob(
        SaramInCrawlerService crawlerService,
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) {
        this.crawlerService = crawlerService;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job saramInCrawlingJob() {
        return new JobBuilder("saramInCrawlingJob", jobRepository)
            .start(saramInCrawlingStep())
            .build();
    }

    @Bean
    public Step saramInCrawlingStep() {
        return new StepBuilder("saramInCrawlingStep", jobRepository)
            .<String, List<JobPosting>>chunk(1, transactionManager)
            .reader(() -> "개발자")
            .processor(jobPostingProcessor())
            .writer(jobPostingWriter())
            .build();
    }

    @Bean
    public ItemProcessor<String, List<JobPosting>> jobPostingProcessor() {
        return keyword -> {
            System.out.println("Processing keyword: " + keyword);
            return crawlerService.crawlJobPostings(keyword);
        };
    }

    @Bean
    public ItemWriter<List<JobPosting>> jobPostingWriter() {
        return chunks -> {
            for (List<JobPosting> chunk : chunks) {
                System.out.println("Writing " + chunk.size() + " job postings");
                chunk.forEach(jobPosting -> {
                    System.out.println("Job Posting: " + jobPosting.getTitle() + 
                                       " at " + jobPosting.getCompany());
                });
            }
        };
    }
}