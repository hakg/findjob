package com.spirngboot.findjob.scheduler;

import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CrawlingScheduler extends QuartzJobBean {
    private final Job saramInCrawlingJob;
    private final JobLauncher jobLauncher;

    public CrawlingScheduler(Job saramInCrawlingJob, JobLauncher jobLauncher) {
        this.saramInCrawlingJob = saramInCrawlingJob;
        this.jobLauncher = jobLauncher;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                .addDate("timestamp", new Date())
                .toJobParameters();

            jobLauncher.run(saramInCrawlingJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}