# 🚀 1단계 MVP 최종 정리


**회원가입/로그인 API** (DDD 구조)

| API | Method | URL |
|-----|--------|-----|
| 회원가입 | POST | `/api/v1/users/signup` |
| 로그인 | POST | `/api/v1/users/login` |

---

## 파일 구조 (총 15개)

```
src/main/java/com/example/demo/
│
├── domain/                          # 비즈니스 로직 (순수 Java)
│   └── user/
│       ├── model/
│       │   ├── Email.java           # 이메일 Value Object
│       │   ├── Password.java        # 비밀번호 Value Object
│       │   └── User.java            # 사용자 Entity
│       └── repository/
│           └── UserRepository.java  # Repository 인터페이스
│
├── application/                     # 유스케이스 (비즈니스 흐름)
│   └── user/
│       ├── UserService.java         # 회원가입/로그인 서비스
│       └── dto/
│           ├── SignUpCommand.java   # 회원가입 요청 데이터
│           ├── LoginCommand.java    # 로그인 요청 데이터
│           └── UserResponse.java    # 응답 데이터
│
├── infrastructure/                  # 기술 구현 (JPA, 설정)
│   ├── persistence/
│   │   └── user/
│   │       ├── UserEntity.java      # JPA Entity
│   │       ├── JpaUserRepository.java    # Spring Data JPA
│   │       └── UserRepositoryImpl.java   # Repository 구현체
│   └── config/
│       └── SecurityConfig.java      # 보안 설정
│
└── presentation/                    # HTTP 요청/응답
    └── api/
        ├── UserController.java      # API 컨트롤러
        └── request/
            ├── SignUpRequest.java   # 회원가입 요청 DTO
            └── LoginRequest.java    # 로그인 요청 DTO

src/main/resources/
└── application.yml                  # 설정 파일

src/test/java/com/example/demo/
├── application/user/
│   └── UserServiceTest.java         # 단위 테스트
└── presentation/api/
    └── UserControllerTest.java      # 통합 테스트
```

---

## DDD 4계층 흐름

```
[HTTP 요청]
     ↓
┌─────────────────────────────────────────────────────────┐
│  Presentation Layer (표현 계층)                          │
│  - UserController: HTTP 요청 받음                        │
│  - SignUpRequest → SignUpCommand 변환                   │
└─────────────────────────────────────────────────────────┘
     ↓
┌─────────────────────────────────────────────────────────┐
│  Application Layer (응용 계층)                           │
│  - UserService: 비즈니스 흐름 조합                        │
│  - 중복체크 → 암호화 → 저장 → 응답                        │
└─────────────────────────────────────────────────────────┘
     ↓
┌─────────────────────────────────────────────────────────┐
│  Domain Layer (도메인 계층)                              │
│  - User: 핵심 비즈니스 객체                              │
│  - Email, Password: 값 검증                             │
│  - UserRepository: 저장소 인터페이스                     │
└─────────────────────────────────────────────────────────┘
     ↓
┌─────────────────────────────────────────────────────────┐
│  Infrastructure Layer (인프라 계층)                      │
│  - UserEntity: DB 테이블 매핑                           │
│  - UserRepositoryImpl: 실제 저장 구현                    │
│  - JpaUserRepository: Spring Data JPA                   │
└─────────────────────────────────────────────────────────┘
     ↓
[Database]
```

---

## 각 파일의 역할

### Domain Layer (비즈니스 핵심)

| 파일 | 역할 |
|-----|------|
| `Email.java` | 이메일 형식 검증 (생성 시 자동) |
| `Password.java` | 비밀번호 저장 + 8자 검증 메서드 |
| `User.java` | 사용자 객체 생성/복원, 비밀번호 매칭 |
| `UserRepository.java` | "저장해줘, 찾아줘" 인터페이스 |

### Application Layer (흐름 조합)

| 파일 | 역할 |
|-----|------|
| `SignUpCommand.java` | 회원가입에 필요한 데이터 묶음 |
| `LoginCommand.java` | 로그인에 필요한 데이터 묶음 |
| `UserResponse.java` | 응답 데이터 (id, email, country) |
| `UserService.java` | 회원가입/로그인 비즈니스 로직 |

### Infrastructure Layer (기술 구현)

| 파일 | 역할 |
|-----|------|
| `UserEntity.java` | DB 테이블과 매핑, 변환 메서드 |
| `JpaUserRepository.java` | Spring Data JPA (쿼리 자동 생성) |
| `UserRepositoryImpl.java` | Domain ↔ Entity 변환하며 저장 |
| `SecurityConfig.java` | PasswordEncoder 빈 등록 |

### Presentation Layer (HTTP 처리)

| 파일 | 역할 |
|-----|------|
| `SignUpRequest.java` | HTTP 요청 검증 (@NotBlank, @Email) |
| `LoginRequest.java` | HTTP 요청 검증 |
| `UserController.java` | API 엔드포인트 정의 |

---

## 회원가입 전체 흐름 예시

```
1. POST /api/v1/users/signup 요청
   {
     "email": "test@example.com",
     "password": "password123",
     "country": "Korea",
     "phoneNumber": "010-1234-5678"
   }

2. [UserController]
   - @Valid로 요청 검증
   - SignUpRequest → SignUpCommand 변환
   - userService.signUp(command) 호출

3. [UserService]
   - new Email("test@example.com") → 이메일 형식 검증
   - userRepository.existsByEmail() → 중복 체크
   - Password.validateRawPassword() → 8자 이상 검증
   - passwordEncoder.encode() → 비밀번호 암호화
   - User.create() → 도메인 객체 생성
   - userRepository.save(user) → 저장 요청

4. [UserRepositoryImpl]
   - UserEntity.from(user) → Domain → Entity 변환
   - jpaUserRepository.save(entity) → DB 저장
   - entity.toDomain() → Entity → Domain 변환

5. [UserService]
   - UserResponse.from(savedUser) → 응답 생성

6. [UserController]
   - ResponseEntity.status(201).body(response)

7. HTTP 응답
   {
     "id": 1,
     "email": "test@example.com",
     "country": "Korea"
   }
```

