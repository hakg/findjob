# FindJob - 채용 정보 크롤링 애플리케이션

## 프로젝트 개요
FindJob은 사람인 웹사이트에서 채용 정보를 자동으로 크롤링하고 수집하는 Spring Boot 기반 애플리케이션입니다.

## 주요 기능
- 🕷️ 사람인 웹사이트 채용 공고 자동 크롤링
- 📊 배치 처리를 통한 대량 데이터 수집
- ⏰ Quartz 스케줄러를 이용한 주기적 크롤링
- 🔍 REST API를 통한 수동/자동 크롤링 트리거

## 기술 스택
- **Backend**: Spring Boot 3.x
- **Java**: Java 17
- **Build Tool**: Gradle
- **Scheduling**: Quartz Scheduler
- **Logging**: SLF4J, Logback
- **API Docs**: Swagger OpenAPI

## 주요 컴포넌트
- `SaramInCrawlerService`: 사람인 웹사이트 크롤링 서비스
- `SaramInCrawlingJob`: Spring Batch 크롤링 작업
- `CrawlingScheduler`: Quartz 기반 스케줄러
- `BatchJobController`: 크롤링 작업 REST API 엔드포인트

## API 엔드포인트
- `/crawl/jobs`: 크롤링 작업 트리거
- `/api/batch/crawl`: 배치 작업 수동 실행
- `/swagger-ui.html`: Swagger UI 문서

## 설치 및 실행

### 사전 요구사항
- JDK 17
- Gradle 8.x

### 로컬 개발 환경
```bash
# 프로젝트 클론
git clone https://github.com/hakg/findjob.git

# 프로젝트 디렉토리 이동
cd findjob

# 빌드
./gradlew clean build

# 애플리케이션 실행
./gradlew bootRun