# HamApp - 아마추어 무선 교신 로그(QSO Log) 관리 앱 📻

**HamApp**은 아마추어 무선(HAM Radio) 동호인 및 운용자를 위한 안드로이드용 교신 기록(QSO Contact Log) 관리 애플리케이션입니다.
상대방의 호출부호(Callsign), 교신 일시, 주파수(MHz), 신호 리포트(RST), 위치(QTH) 및 메모를 손쉽게 기록하고 검색 및 관리할 수 있습니다.

---

## 📱 주요 기능 (Key Features)

- 📝 **교신 로그 등록 (Add Contact Log)**
  - 상대방 호출부호(Callsign), 교신 날짜 및 시간, 주파수(MHz), 신호 리포트(RST Sent/Rcvd), 위치(QTH), 메모 입력 기능
  - DatePicker & TimePicker 다이얼로그를 통한 용이한 교신 일시 선택
  - 주파수 입력 시 소수점 3자리 자동 포맷팅 및 유효 범위(10.0 ~ 9999.999 MHz) 검증

- 📋 **실시간 교신 로그 목록 (Contact Log List)**
  - RecyclerView를 통한 교신 기록 목록 시각화
  - Kotlin Flow 및 Room DB 연동으로 데이터 변경 시 UI 자동 갱신

- 🔍 **호출부호 검색 (Search Callsign)**
  - 상단 검색창(SearchView)을 통한 실시간 호출부호 검색 기능 제공

- 🔎 **상세 보기 및 수정/삭제 (Detail View, Edit & Delete)**
  - 목록의 항목 클릭 시 상세 정보 팝업
  - 상세 화면 내에서 데이터 수정 모드 전환 및 실시간 업데이트 지원
  - 불필요한 교신 로그 삭제 기능

---

## 🛠 Tech Stack (기술 스택)

| 분류 | 기술 / 라이브러리 |
| --- | --- |
| **언어 (Language)** | Kotlin |
| **최소 / 타겟 SDK** | Min SDK: 24 (Android 7.0) / Target SDK: 37 |
| **아키텍처/패턴** | Android Jetpack, ViewBinding |
| **데이터베이스** | Room DB (`androidx.room`) + KSP |
| **비동기 처리** | Kotlin Coroutines, Flow |
| **UI 컴포넌트** | Material Design 3, RecyclerView, ConstraintLayout, AlertDialog |

---

## 📁 프로젝트 구조 (Project Structure)

```
com.example.hamapp
├── AppDatabase.kt          # Room Database 인스턴스 관리 (Singleton)
├── ContactLog.kt           # 교신 로그 Entity 데이터 클래스
├── ContactLogDao.kt        # Room DAO (Data Access Object - Insert, Query, Update, Delete)
├── ContactLogAdapter.kt    # RecyclerView Adapter 및 ViewHolder
├── DateConverters.kt       # Room TypeConverter (Date <-> Long Timestamp)
└── MainActivity.kt         # 메인 화면 (목록 표시, 검색, 추가/수정/삭제 다이얼로그 제어)
```

---

## 💾 데이터 구조 (Data Schema)

`ContactLog` 엔티티는 다음과 같은 필드로 구성됩니다:

| 필드명 | 타입 | 설명 |
| --- | --- | --- |
| `id` | `Long` (PK, AutoGenerate) | 고유 식별 ID |
| `callsign` | `String` | 상대방 호출부호 (예: `HL1XYZ`) |
| `dateUtc` | `Date` | 교신 날짜 및 시간 |
| `frequencyMhz` | `String` | 교신 주파수 (MHz 단위, 예: `145.000`) |
| `rstSent` | `String?` | 보낸 신호 리포트 (예: `59`, `599`) |
| `rstRcvd` | `String?` | 받은 신호 리포트 (예: `59`, `599`) |
| `qth` | `String?` | 상대방 위치 / 지역명 |
| `notes` | `String?` | 메모 사항 |
| `isUse` | `Boolean` | 사용 여부 플래그 |

---

## 🚀 실행 방법 (Getting Started)

1. **프로젝트 클론 또는 열기**
   - Android Studio에서 본 프로젝트 폴더를 열어줍니다.
2. **Gradle 동기화**
   - Gradle Sync를 수행하여 필요 라이브러리(Room, KSP, Coroutines 등)를 동기화합니다.
3. **앱 실행**
   - 안드로이드 에뮬레이터 또는 디바이스(Android 7.0 이상)를 연결하여 앱을 실행합니다.
