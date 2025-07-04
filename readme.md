## MAVVE-Backend
<img width="1920" alt="mainpage" src="https://github.com/user-attachments/assets/b2f8ae50-2890-42df-b2bc-f92c70d104bb" />

## 📌 Commit Convention
* {Tag}: 작업 내용 #이슈번호

| 태그         | 설명                          |
| ---------- | --------------------------- |
| `feat`     | 새로운 기능 추가                   |
| `fix`      | 버그 수정                       |
| `docs`     | 문서 수정 (README 등)            |
| `style`    | 코드 포맷, 세미콜론 등 변경 (기능 변경 없음) |
| `refactor` | 리팩토링 (기능 변경 없음)             |
| `test`     | 테스트 코드 추가/수정                |
| `chore`    | 빌드 설정, 패키지 설치 등 기타 잡일       |
| `perf`     | 성능 개선                       |
| `hotfix`   | 급한 버그 수정                    |

### 👩‍💻 예시
```
feat: 회원가입 API 구현 #23
fix: 로그인 시 토큰 누락 버그 수정 #12
docs: README 커밋 규칙 추가 #1
```

## 👥 협업 규칙
### 1. 브랜치 전략 (Git Flow 기반)
| 브랜치명          | 용도           |
| ------------- | ------------ |
| `main`        | 최종 배포용       |
| `develop`         | 개발 통합 브랜치    |
| `feature/기능명` | 각 기능 개발용 브랜치 |
| `fix/버그명`     | 버그 수정 브랜치    |
* 예시
```
feature/signup-api
fix/token-expired-bug
```

### 2. PR 규칙
* develop 브랜치로 PR 생성
* PR 제목 예시: [feat] 회원가입 API 구현 #12
* PR 템플릿을 기반으로 상세 내용 작성
* PR 리뷰 후 Approve 1명 이상 시 Merge

###  3. 코드 컨벤션
* 클래스명은 `UpperCamelCase`, 변수/메서드는 `lowerCamelCase`
* Controller, Service, DTO, Entity 명확하게 분리

## 📚 개발 중 규칙
* 작업 시작 전 이슈 등록 → 브랜치 생성 → 작업 후 PR
* 커밋 단위는 너무 작거나 크지 않게 적절히
