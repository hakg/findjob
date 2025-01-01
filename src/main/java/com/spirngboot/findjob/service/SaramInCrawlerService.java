package com.spirngboot.findjob.service;

import com.spirngboot.findjob.dto.JobPosting;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

@Service
public class SaramInCrawlerService {
    private static final Logger logger = LoggerFactory.getLogger(SaramInCrawlerService.class);
    private static final Random random = new Random();

    public List<JobPosting> crawlJobPostings(String keyword) {
        // Chrome 옵션 설정 (로봇 방지 우회)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 브라우저 창 숨기기
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = null;
        List<JobPosting> jobPostings = new ArrayList<>();

        try {
            // WebDriver 설정
            driver = WebDriverManager.chromedriver().capabilities(options).create();

            // 랜덤 대기 시간 추가 (로봇 탐지 방지)
            Thread.sleep(random.nextInt(2000) + 1000);

            // URL 인코딩된 키워드 사용
            String encodedKeyword = java.net.URLEncoder.encode(keyword, "UTF-8");
            String url = "https://www.saramin.co.kr/zf_user/search?search_area=main&search_done=y&search_optional_item=n&searchType=search&searchword=" + encodedKeyword;
            
            driver.get(url);

            // 명시적 대기 추가
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".item_recruit")));

            List<WebElement> jobElements = driver.findElements(By.cssSelector(".item_recruit"));

            for (WebElement element : jobElements) {
                try {
                    String title = element.findElement(By.cssSelector(".job_tit")).getText();
                    String company = element.findElement(By.cssSelector(".corp_name")).getText();
                    String link = element.findElement(By.cssSelector(".job_tit a")).getAttribute("href");
                    
                    jobPostings.add(new JobPosting(title, company, link));
                    
                    // 각 요소 사이 랜덤 대기
                    Thread.sleep(random.nextInt(500) + 200);
                } catch (Exception e) {
                    logger.error("개별 채용공고 크롤링 중 오류: {}", e.getMessage());
                }
            }

            logger.info("크롤링 완료: {} 건의 채용공고 발견", jobPostings.size());

        } catch (Exception e) {
            logger.error("크롤링 중 심각한 오류 발생: {}", e.getMessage(), e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }

        return jobPostings;
    }


    public static void main(String[] args) {
        SaramInCrawlerService service = new SaramInCrawlerService();
        List<JobPosting> jobs = service.crawlJobPostings("개발자");
        jobs.forEach(job -> {
            System.out.println("제목: " + job.getTitle());
            System.out.println("회사: " + job.getCompany());
            System.out.println("링크: " + job.getLink());
            System.out.println("---");
        });
    }
}