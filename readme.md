
<div align="center">
  <h1>🌊MAVVE - Backend</h1>
  <p>EFUB 5기 SWS 3팀 "Mavve" 프로젝트 백엔드 레포지토리입니다.</p>
  <img width="600" alt="mavve-preview" src="https://github.com/user-attachments/assets/65266682-ae49-447b-af9d-8c872395382a" />
  <a href="https://api.mavve.p-e.kr"><img src="https://img.shields.io/badge/Mavve%20BE%20Server-1C9AD6?style=flat-square&logoColor=white&link=https://api.mavve.p-e.kr"/></a>
</div>

## 🎸Mavve는 어떤 프로젝트일까요?

> 음악을 통한 공유 경험, Mavve!

`Mavve`는 음악을 통해 감정을 실시간으로 공유하고,  
몰입감을 높이는 협업 환경을 제공하는 서비스입니다.  
음악 플레이리스트, 한줄 일기를 기반으로 다른 유저와 교감할 수 있어요.

## 🗓️ 개발 기간
- 2025.07.07 ~

## 💡 주요 기능

## 🔧 아키텍처

## 🔨 기술 스택
**Develop**

<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/MYSQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens"> <img src="https://img.shields.io/badge/REDIS-FF4438?style=for-the-badge&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/Spotify%20Web%20API-1ED760?style=for-the-badge&logo=spotify&logoColor=white">

**Deploy**

<img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> ![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white) <img src="https://img.shields.io/badge/github%20actions-181717?style=for-the-badge&logo=github%20actions&logoColor=white">

## 🖥️ ERD
<img width="1740" height="1432" alt="mavve (1)" src="https://github.com/user-attachments/assets/16780a7e-518b-4d5f-891a-1875cb9374fb" />

## 👩‍💻 팀원
<table>
  <tr>
    <td align="center"><img src="https://github.com/9rin.png" width="100" /></td>
    <td align="center"><img src="https://github.com/jettieb.png" width="100" /></td>
    <td align="center"><img src="https://github.com/yepot.png" width="100" /></td>
    <td align="center"><img src="https://github.com/topograp2.png" width="100" /></td>
  </tr>
  <tr>
    <td align="center"><strong>@9rin</strong></td>
    <td align="center"><strong>@jettieb</strong></td>
    <td align="center"><strong>@yepot</strong></td>
    <td align="center"><strong>@topograp2</strong></td>
  </tr>
  <tr>
    <td align="center">김규린</td>
    <td align="center">복지희</td>
    <td align="center">양은서</td>
    <td align="center">홍지형</td>
  </tr>
  <tr>
    <td align="center">방 CRUD api 개발, 한줄일기 CRUD api 개발</td>
    <td align="center">방 진입, 실시간 음악 재생 관련 웹소켓 api 개발</td>
    <td align="center">플레이리스트 CRUD api 개발, 배포</td>
    <td align="center">유저 관련 api 개발, 곡 api 개발, 배포</td>
  </tr>
</table>



## 📁 프로젝트 구조
```
📂
├── .github/
│   └── ISSUE_TEMPLATE / deveop-issue
│   └── workflows / deploy.yaml
│
├──mavve/
│   ├── scripts/
|   │   └──deploy.sh
│   ├── src/main/
│   │   ├── java/com/mavve/
│   │   │   ├── MavveApplication.java
│   │   │
│   │   │   ├── auth/                     # 🔐 인증 도메인
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── dto/
│   │   │   │   └── entity/
│   │   │
│   │   │   ├── board/                    # 📌 게시글 도메인
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── dto/
│   │   │   │   └── entity/
│   │   │
│   │   │   ├── comment/                  # 💬 댓글 도메인
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── dto/
│   │   │   │   └── entity/
│   │   │
│   │   │   ├── like/                     # ❤️ 좋아요 도메인
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   └── entity/
│   │   │
│   │   │   ├── tag/                      # 🏷️ 태그 도메인
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   └── entity/
│   │   │
│   │   │   ├── image/                    # 🖼️ 이미지 업로드
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   └── util/
│   │   │
│   │   │   ├── member/                   # 👤 사용자 도메인
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── dto/
│   │   │   │   └── entity/
│   │   │
│   │   │
│   │   │   └── global/
│   │   │       ├── S3Image/
│   │   │       ├── config/
│   │   │       ├── convertor/                          
│   │   │       ├── exception/           
│   │   │       ├── response/             
│   │   │       └── handler/                 
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       └── static/
│   └── test/
│       └── java/com/mavve/
│           └── ...                      # 테스트 코드들
└── README.md

```

