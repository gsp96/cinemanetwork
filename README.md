# 🎬 CinemaNetwork (시네마 네트워크)

> **실시간 영화 데이터 기반의 영화 탐색, 평점·리뷰 및 소셜 커뮤니티 풀스택 플랫폼**  
> 과거 2022년 프로토타입(Spring Boot 2 + JSP + MyBatis + Oracle XE)을 **Spring Boot 3.3 (Java 17) RESTful API 백엔드와 React 18 + TypeScript + Vite 모던 프론트엔드**로 전면 재설계 및 고도화한 프로젝트입니다.

---

## 🌟 주요 핵심 기능

### 1. 실시간 박스오피스 & 영화 메타데이터 동기화 (KOBIS + TMDB)
- **일일 박스오피스 자동 수집**: 매일 아침 영화진흥위원회(KOBIS) API를 연동하여 일일 박스오피스 1~10위 데이터를 수집합니다.
- **고해상도 비주얼 매핑**: TMDB API를 결합하여 고품질 포스터, 배경 스틸컷, 줄거리, 감독/출연진 메타데이터를 통합 관리합니다.
- **회복 탄력성 (Resilience Fallback)**: 외부 API 키 부재 또는 일일 트래픽 초과 시에도 정상 작동하도록 폴백 데이터 파이프라인을 지원합니다.

### 2. 왓챠피디아 스타일의 영화 평점 & 리뷰 큐레이션
- **인터랙티브 별점 평가**: 0.5점 ~ 5.0점 단위의 부드러운 별점 인터랙션
- **통계 자동 갱신**: 사용자가 리뷰를 작성/수정/삭제할 때마다 해당 영화의 평균 평점과 리뷰 수가 트랜잭션 내에서 정밀하게 자동 재계산됩니다.
- **소셜 좋아요 토글**: 관객 리뷰에 추천(좋아요)을 누르고 인기순/최신순으로 감상할 수 있습니다.

### 3. 커뮤니티 & 1:1 고객센터 문의 게시판
- **다양한 카테고리 지원**: `영화 수다`, `자유 토론`, `공지사항`, `1:1 문의`
- **비밀글 보안 제어**: 고객센터 문의 시 비밀글 설정을 통해 **작성자와 관리자만 안전하게 열람**할 수 있습니다.
- **실시간 소통**: 게시글별 댓글 CRUD 지원

### 4. 최신 웹 보안 및 인증 (Spring Security + JWT)
- Stateless REST API에 최적화된 JWT (Access Token + Refresh Token) 체계
- BCrypt 단방향 암호화를 통한 사용자 비밀번호 보호
- Role 기반 인가 (`USER`, `ADMIN`)

---

## 🛠 기술 스택

### Backend
- **Framework**: Spring Boot 3.3.4 (Java 17 LTS)
- **Security**: Spring Security 6.x, JJWT 0.12.6 (BCrypt Password Hashing)
- **Database / ORM**: Spring Data JPA, H2 In-Memory (개발용) / MySQL 호환
- **HTTP Client**: Spring WebClient (KOBIS & TMDB 비동기/논블로킹 통신)
- **API Documentation**: Springdoc OpenAPI 2.6.0 (Swagger UI)
- **Build Tool**: Maven

### Frontend
- **Core**: React 18, TypeScript, Vite
- **Styling**: Tailwind CSS, Lucide Icons (시네마 다크모드 테마)
- **Network**: Axios (JWT 인터셉터 자동 주입)
- **Icons**: Lucide React

---

## 🚀 빠른 시작 가이드 (Quick Start)

### 1. Backend 실행
```bash
cd backend
mvn spring-boot:run
```
* **백엔드 서버**: `http://localhost:8080`
* **Swagger API 문서**: `http://localhost:8080/swagger-ui.html`
* **H2 Database 콘솔**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:cinemanetwork`, ID: `sa`, PW: 없음)

### 2. Frontend 실행
```bash
cd frontend
npm install
npm run dev
```
* **프론트엔드 웹 앱**: `http://localhost:5173`

---

## 🔑 데모 계정 안내
애플리케이션 구동 시 초기 테스트 데이터와 계정이 자동으로 생성됩니다:
| 구분 | 이메일 | 비밀번호 | 권한 |
| :--- | :--- | :--- | :--- |
| **일반 유저** | `user@cinemanetwork.com` | `user1234` | 일반 사용자 (리뷰, 커뮤니티 작성) |
| **관리자** | `admin@cinemanetwork.com` | `admin1234` | 관리자 (공지사항 작성, 비밀글 열람, 박스오피스 수동 동기화) |
*(웹 화면 우측 상단 로그인 창에서 '원클릭 데모 계정 체험' 버튼으로 비밀번호 입력 없이 즉시 로그인할 수 있습니다.)*

---

## 📂 프로젝트 구조

```text
cinemanetwork/
├── backend/                  # Spring Boot 3.3 RESTful API
│   ├── src/main/java/com/cinemanetwork/
│   │   ├── domain/           # user, movie, review, post 도메인별 Entity, Service, Controller, DTO
│   │   └── global/           # Security, JWT, ErrorHandling, OpenAPI, BaseEntity
│   └── src/main/resources/   # application.yml
│
├── frontend/                 # React 18 + TypeScript + Vite SPA
│   ├── src/
│   │   ├── api/              # Axios 인스턴스 (JWT 자동 주입)
│   │   ├── components/       # Navbar, HeroBanner, MovieSlider, MovieCard, DetailModal, Community, AuthModal
│   │   └── types/            # TypeScript 인터페이스 정의
│   ├── index.html
│   └── package.json
│
└── legacy/                   # 2022년 초기 취준 시절 JSP + MyBatis 소스 코드 원본 보관
```
