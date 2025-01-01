package com.spirngboot.findjob.config;

import com.spirngboot.findjob.scheduler.CrawlingScheduler;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail crawlingJobDetail() {
        return JobBuilder.newJob().ofType(CrawlingScheduler.class)
            .storeDurably()
            .withIdentity("saramInCrawlingJob")
            .withDescription("사람인 채용공고 크롤링 배치")
            .build();
    }

    @Bean
    public Trigger crawlingJobTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity("saramInCrawlingTrigger")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 9,18 * * ?")) // 매일 오전 9시, 오후 6시에 실행
            .build();
    }
}