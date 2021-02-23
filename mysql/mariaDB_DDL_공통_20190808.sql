-- 사용자
CREATE TABLE `USER` (
	INSTT_CODE       CHAR(7)      NOT NULL COMMENT '기관 코드', -- 기관 코드
	USER_ID          VARCHAR(20)  NOT NULL COMMENT '사용자 아이디', -- 사용자 아이디
	AUTHOR_CODE      VARCHAR(20)  NOT NULL COMMENT '권한 코드', -- 권한 코드
	USER_PASSWORD    VARCHAR(255) NOT NULL COMMENT '사용자 비밀번호', -- 사용자 비밀번호
	USER_NM          VARCHAR(50)  NULL     COMMENT '사용자 이름', -- 사용자 이름
	CHRG_DEPT        VARCHAR(100) NULL     COMMENT '담당부서', -- 담당부서
	RECENT_CONECT_DT DATETIME     NULL     COMMENT '최근접속 일시', -- 최근접속 일시
	RECENT_CONECT_IP VARCHAR(50)  NULL     COMMENT '최근접속 아이피', -- 최근접속 아이피
	RM               TEXT         NULL     COMMENT '비고', -- 비고
	USE_AT           BIT          NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '사용자';

-- 사용자
ALTER TABLE `USER`
	ADD CONSTRAINT PK_USER -- 사용자 기본키
		PRIMARY KEY (
			INSTT_CODE, -- 기관 코드
			USER_ID     -- 사용자 아이디
		);

-- 메뉴권한코드
CREATE TABLE MENU_AUTHOR (
	MENU_SN     CHAR(4)     NOT NULL COMMENT '메뉴 순번', -- 메뉴 순번
	AUTHOR_CODE VARCHAR(20) NOT NULL COMMENT '권한 코드', -- 권한 코드
	USE_AT      BIT         NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '메뉴권한코드';


-- 메뉴관리
CREATE TABLE MENU_MANAGE (
	MENU_GROUP_SN CHAR(4)      NOT NULL COMMENT '메뉴 그룹 순번', -- 메뉴 그룹 순번
	MENU_SN       CHAR(4)      NOT NULL COMMENT '메뉴 순번', -- 메뉴 순번
	MENU_DP_NO    CHAR(4)      NULL     COMMENT '메뉴 깊이 넘버', -- 메뉴 깊이 넘버
	INQIRE_ORDR   CHAR(4)      NULL     COMMENT '조회 순서', -- 조회 순서
	MENU_NM       VARCHAR(50)  NULL     COMMENT '메뉴 이름', -- 메뉴 이름
	MENU_URL      VARCHAR(600) NULL     COMMENT '메뉴URL', -- 메뉴URL
	USE_AT        BIT          NULL     COMMENT '사용 여부', -- 사용 여부
	UPPER_MENU_SN CHAR(4)      NULL     COMMENT '상위 메뉴 순번', -- 상위 메뉴 순번
	MENU_ICON     VARCHAR(100) NULL     COMMENT '메뉴 아이콘' -- 메뉴 아이콘
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '메뉴관리';

-- 메뉴관리
ALTER TABLE MENU_MANAGE
	ADD CONSTRAINT PK_MENU_MANAGE -- 메뉴관리 기본키
		PRIMARY KEY (
			MENU_SN -- 메뉴 순번
		);

-- 메뉴그룹관리
CREATE TABLE MENU_GROUP (
	MENU_GROUP_SN CHAR(4)     NOT NULL COMMENT '메뉴 그룹 순번', -- 메뉴 그룹 순번
	MENU_GROUP_NM VARCHAR(50) NULL     COMMENT '메뉴 그룹 명', -- 메뉴 그룹 명
	USE_AT        BIT         NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '메뉴그룹관리';

-- 메뉴그룹관리
ALTER TABLE MENU_GROUP
	ADD CONSTRAINT PK_MENU_GROUP -- 메뉴그룹관리 기본키
		PRIMARY KEY (
			MENU_GROUP_SN -- 메뉴 그룹 순번
		);

-- 공통코드
CREATE TABLE CMMN_CODE (
	GROUP_CODE    VARCHAR(20) NOT NULL COMMENT '그룹 코드', -- 그룹 코드
	GROUP_CODE_NM VARCHAR(50) NULL     COMMENT '그룹 코드 명', -- 그룹 코드 명
	GROUP_CODE_DC TEXT        NULL     COMMENT '그룹 코드 설명', -- 그룹 코드 설명
	USE_AT        BIT         NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '공통코드';

-- 공통코드
ALTER TABLE CMMN_CODE
	ADD CONSTRAINT PK_CMMN_CODE -- 공통코드 기본키
		PRIMARY KEY (
			GROUP_CODE -- 그룹 코드
		);

-- 공통코드상세
CREATE TABLE CMMN_CODE_DETAIL (
	GROUP_CODE      VARCHAR(20) NOT NULL COMMENT '그룹 코드', -- 그룹 코드
	CMMN_CODE       VARCHAR(20) NOT NULL COMMENT '공통 코드', -- 공통 코드
	CMMN_UPPER_CODE VARCHAR(20) NOT NULL COMMENT '공통상위코드', -- 공통상위코드
	CMMN_CODE_NM    VARCHAR(50) NULL     COMMENT '공통 코드 이름', -- 공통 코드 이름
	CMMN_CODE_DC    TEXT        NULL     COMMENT '공통 코드 설명', -- 공통 코드 설명
	USE_AT          BIT         NULL     COMMENT '사용 여부', -- 사용 여부
	INQIRE_ORDR     INT(11)     NULL     COMMENT '조회 순서' -- 조회 순서
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '공통코드상세';

-- 공통코드상세
ALTER TABLE CMMN_CODE_DETAIL
	ADD CONSTRAINT PK_CMMN_CODE_DETAIL -- 공통코드상세 기본키
		PRIMARY KEY (
			GROUP_CODE, -- 그룹 코드
			CMMN_CODE   -- 공통 코드
		);

-- 기관 정보
CREATE TABLE INSTT (
	INSTT_CODE      CHAR(7)      NOT NULL COMMENT '기관 코드', -- 기관 코드
	INSTT_NM        VARCHAR(80)  NULL     COMMENT '기관 명', -- 기관 명
	BSNS_BGNDE      VARCHAR(8)   NULL     COMMENT '사업 시작일', -- 사업 시작일
	BSNS_ENDDE      VARCHAR(8)   NULL     COMMENT '사업 종료일', -- 사업 종료일
	REPRSNT_TLPHON  VARCHAR(30)  NULL     COMMENT '대표 전화', -- 대표 전화
	ADRES           VARCHAR(255) NULL     COMMENT '주소', -- 주소
	HMPG            VARCHAR(255) NULL     COMMENT '홈페이지', -- 홈페이지
	CHARGER_NM      VARCHAR(50)  NULL     COMMENT '담당자 명', -- 담당자 명
	CHARGER_EMAIL   VARCHAR(40)  NULL     COMMENT '담당자 이메일', -- 담당자 이메일
	CHARGER_TELNO   VARCHAR(30)  NULL     COMMENT '담당자 전화번호', -- 담당자 전화번호
	CHARGER_MBTLNUM VARCHAR(30)  NULL     COMMENT '담당자 휴대폰번호', -- 담당자 휴대폰번호
	RM              TEXT         NULL     COMMENT '비고', -- 비고
	USE_AT          BIT          NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '기관 정보';

-- 기관 정보
ALTER TABLE INSTT
	ADD CONSTRAINT PK_INSTT -- 기관 정보 기본키
		PRIMARY KEY (
			INSTT_CODE -- 기관 코드
		);

-- 진단대상데이터베이스정보
CREATE TABLE DGNSS_DBMS (
	DGNSS_DBMS_ID CHAR(4)      NOT NULL COMMENT '진단대상데이터베이스순번', -- 진단대상데이터베이스순번
	INSTT_CODE    CHAR(7)      NOT NULL COMMENT '기관 코드', -- 기관 코드
	DBMS_ID       CHAR(4)      NULL     COMMENT 'DBMS 순번', -- DBMS 순번
	DBMS_VER_ID   VARCHAR(20)  NULL     COMMENT 'DBMS 버전 ID', -- DBMS 버전 ID
	DGNSS_DBMS_NM VARCHAR(80)  NULL     COMMENT '진단대상데이터베이스명', -- 진단대상데이터베이스명
	IP            VARCHAR(50)  NULL     COMMENT '아이피', -- 아이피
	PORT          CHAR(4)      NULL     COMMENT '포트', -- 포트
	`SCHEMA`        VARCHAR(50)  NULL     COMMENT '스키마', -- 스키마
	SID           VARCHAR(50)  NULL     COMMENT 'SID', -- SID
	`DATABASE`      VARCHAR(50)  NULL     COMMENT '데이터베이스', -- 데이터베이스
	PARAMTR       VARCHAR(255) NULL     COMMENT '파라미터', -- 파라미터
	ID            VARCHAR(20)  NULL     COMMENT '아이디', -- 아이디
	PASSWORD      VARCHAR(255) NULL     COMMENT '비밀번호', -- 비밀번호
	RM            TEXT         NULL     COMMENT '비고', -- 비고
	USE_AT        BIT          NULL     COMMENT '사용 여부', -- 사용 여부
	REGIST_DT     DATETIME     NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '진단대상데이터베이스정보';

-- 진단대상데이터베이스정보
ALTER TABLE DGNSS_DBMS
	ADD CONSTRAINT PK_DGNSS_DBMS -- 진단대상데이터베이스정보 기본키
		PRIMARY KEY (
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			INSTT_CODE     -- 기관 코드
		);

-- DBMS 정보
CREATE TABLE INSTT_DBMS (
	DBMS_ID  CHAR(4)     NOT NULL COMMENT 'DBMS 순번', -- DBMS 순번
	DBMS_KND VARCHAR(50) NULL     COMMENT 'DBMS 종류', -- DBMS 종류
	RM       TEXT        NULL     COMMENT '비고', -- 비고
	USE_AT   BIT         NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT 'DBMS 정보';

-- DBMS 정보
ALTER TABLE INSTT_DBMS
	ADD CONSTRAINT PK_INSTT_DBMS -- DBMS 정보 기본키
		PRIMARY KEY (
			DBMS_ID -- DBMS 순번
		);

-- DBMS 버전 정보
CREATE TABLE INSTT_DBMS_VER (
	DBMS_ID       CHAR(4)     NOT NULL COMMENT 'DBMS 순번', -- DBMS 순번
	DBMS_VER_ID   VARCHAR(20) NOT NULL COMMENT 'DBMS 버전 ID', -- DBMS 버전 ID
	DBMS_VER_INFO VARCHAR(50) NULL     COMMENT 'DBMS 버전 정보', -- DBMS 버전 정보
	RM            TEXT        NULL     COMMENT '비고', -- 비고
	USE_AT        BIT         NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT 'DBMS 버전 정보';

-- DBMS 버전 정보
ALTER TABLE INSTT_DBMS_VER
	ADD CONSTRAINT PK_INSTT_DBMS_VER -- DBMS 버전 정보 기본키
		PRIMARY KEY (
			DBMS_ID,     -- DBMS 순번
			DBMS_VER_ID  -- DBMS 버전 ID
		);

-- 진단 정보
CREATE TABLE DGNSS_TABLES (
	DGNSS_DBMS_ID  CHAR(4)     NOT NULL COMMENT '진단대상데이터베이스순번', -- 진단대상데이터베이스순번
	INSTT_CODE     CHAR(7)     NOT NULL COMMENT '기관 코드', -- 기관 코드
	DGNSS_INFO_ID  VARCHAR(20) NOT NULL COMMENT '진단 정보 아이디', -- 진단 정보 아이디
	DGNSS_NM       VARCHAR(80) NULL     COMMENT '진단 명', -- 진단 명
	TABLE_NM       VARCHAR(80) NULL     COMMENT '테이블 명', -- 테이블 명
	ALL_CO         CHAR(4)     NULL     COMMENT '데이터 건수', -- 데이터 건수
	EXC_BEGIN_TIME DATETIME    NULL     COMMENT '수행 시작 시간', -- 수행 시작 시간
	EXC_END_TIME   DATETIME    NULL     COMMENT '수행 종료 시간', -- 수행 종료 시간
	EXC_STTUS      CHAR(1)     NULL     COMMENT '수행 상태', -- 수행 상태
	USER_ID        VARCHAR(20) NOT NULL COMMENT '사용자 아이디', -- 사용자 아이디
	REGIST_DT      DATETIME    NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '진단 정보';

-- 진단 정보
ALTER TABLE DGNSS_TABLES
	ADD CONSTRAINT PK_DGNSS_TABLES -- 진단 정보 기본키
		PRIMARY KEY (
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			INSTT_CODE,    -- 기관 코드
			DGNSS_INFO_ID  -- 진단 정보 아이디
		);

-- 컬럼별 진단 정보
CREATE TABLE DGNSS_COLUMNS (
	INSTT_CODE    CHAR(7)     NOT NULL COMMENT '기관 코드', -- 기관 코드
	DGNSS_DBMS_ID CHAR(4)     NOT NULL COMMENT '진단대상데이터베이스순번', -- 진단대상데이터베이스순번
	DGNSS_INFO_ID VARCHAR(20) NOT NULL COMMENT '진단 정보 아이디', -- 진단 정보 아이디
	COLUMN_NM     VARCHAR(80) NOT NULL COMMENT '컬럼 명', -- 컬럼 명
	COLUMN_TY     VARCHAR(20) NULL     COMMENT '컬럼 타입', -- 컬럼 타입
	COLUMN_LT     CHAR(4)     NULL     COMMENT '컬럼 길이', -- 컬럼 길이
	TRGCNT        CHAR(4)     NULL     COMMENT '대상 건수', -- 대상 건수
	REGIST_DT     DATETIME    NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '컬럼별 진단 정보';

-- 컬럼별 진단 정보
ALTER TABLE DGNSS_COLUMNS
	ADD CONSTRAINT PK_DGNSS_COLUMNS -- 컬럼별 진단 정보 기본키
		PRIMARY KEY (
			INSTT_CODE,    -- 기관 코드
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			DGNSS_INFO_ID, -- 진단 정보 아이디
			COLUMN_NM      -- 컬럼 명
		);

-- 컬럼별 진단 항목별 진단 정보
CREATE TABLE DGNSS_COLUMNS_RES (
	INSTT_CODE    CHAR(7)     NOT NULL COMMENT '기관 코드', -- 기관 코드
	DGNSS_DBMS_ID CHAR(4)     NOT NULL COMMENT '진단대상데이터베이스순번', -- 진단대상데이터베이스순번
	DGNSS_INFO_ID VARCHAR(20) NOT NULL COMMENT '진단 정보 아이디', -- 진단 정보 아이디
	COLUMN_NM     VARCHAR(80) NOT NULL COMMENT '컬럼 명', -- 컬럼 명
	ANALS_ID      CHAR(4)     NOT NULL COMMENT '분석 순번', -- 분석 순번
	MTCHG_CO      CHAR(4)     NULL     COMMENT '매칭 건수', -- 매칭 건수
	REGIST_DT     DATETIME    NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '컬럼별 진단 항목별 진단 정보';

-- 컬럼별 진단 항목별 진단 정보
ALTER TABLE DGNSS_COLUMNS_RES
	ADD CONSTRAINT PK_DGNSS_COLUMNS_RES -- 컬럼별 진단 항목별 진단 정보 기본키
		PRIMARY KEY (
			INSTT_CODE,    -- 기관 코드
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			DGNSS_INFO_ID, -- 진단 정보 아이디
			COLUMN_NM,     -- 컬럼 명
			ANALS_ID       -- 분석 순번
		);

-- 분석 정보
CREATE TABLE ANALS (
	INSTT_CODE  CHAR(7)     NOT NULL COMMENT '기관 코드', -- 기관 코드
	ANALS_ID    CHAR(4)     NOT NULL COMMENT '분석 순번', -- 분석 순번
	ANALS_SE    VARCHAR(20) NULL     COMMENT '분석 구분', -- 분석 구분
	ANALS_TY    VARCHAR(20) NULL     COMMENT '분석 타입', -- 분석 타입
	PTTRN_SE    VARCHAR(20) NULL     COMMENT '패턴 구분', -- 패턴 구분
	ANALS_NM    VARCHAR(50) NULL     COMMENT '분석 명', -- 분석 명
	ANALS_FRMLA TEXT        NULL     COMMENT '분석 식', -- 분석 식
	BEGIN_VALUE VARCHAR(20) NULL     COMMENT '시작 값', -- 시작 값
	END_VALUE   VARCHAR(20) NULL     COMMENT '종료 값', -- 종료 값
	DBMS_KND    VARCHAR(20) NULL     COMMENT 'DBMS 종류', -- DBMS 종류
	DBMS_VER    VARCHAR(20) NULL     COMMENT 'DBMS 버전', -- DBMS 버전
	RM          TEXT        NULL     COMMENT '비고', -- 비고
	USE_AT      BIT         NULL     COMMENT '사용 여부', -- 사용 여부
	REGIST_DT   DATETIME    NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '분석 정보';

-- 분석 정보
ALTER TABLE ANALS
	ADD CONSTRAINT PK_ANALS -- 분석 정보 기본키
		PRIMARY KEY (
			INSTT_CODE, -- 기관 코드
			ANALS_ID    -- 분석 순번
		);

-- 빈도 분석 결과
CREATE TABLE FRQ_ANALS (
	INSTT_CODE    CHAR(7)      NOT NULL COMMENT '기관 코드', -- 기관 코드
	DGNSS_DBMS_ID CHAR(4)      NOT NULL COMMENT '진단대상데이터베이스순번', -- 진단대상데이터베이스순번
	DGNSS_INFO_ID VARCHAR(20)  NOT NULL COMMENT '진단 정보 아이디', -- 진단 정보 아이디
	COLUMN_NM     VARCHAR(80)  NOT NULL COMMENT '컬럼 명', -- 컬럼 명
	SN            CHAR(4)      NOT NULL COMMENT '순번', -- 순번
	DATA_VALUE    VARCHAR(255) NULL     COMMENT '데이터 값', -- 데이터 값
	DATA_CO       INT(11)      NULL     COMMENT '데이터 건수', -- 데이터 건수
	REGIST_DT     DATETIME     NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '빈도 분석 결과';

-- 빈도 분석 결과
ALTER TABLE FRQ_ANALS
	ADD CONSTRAINT PK_FRQ_ANALS -- 빈도 분석 결과 기본키
		PRIMARY KEY (
			INSTT_CODE,    -- 기관 코드
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			DGNSS_INFO_ID, -- 진단 정보 아이디
			COLUMN_NM,     -- 컬럼 명
			SN             -- 순번
		);

-- 기관 관리
CREATE TABLE INSTT_MANAGE (
	INSTT_CODE         CHAR(7)      NOT NULL COMMENT '기관 코드', -- 기관 코드
	INSTT_NM           VARCHAR(80)  NULL     COMMENT '기관 명', -- 기관 명
	LWPRT_INSTT_NM     VARCHAR(40)  NULL     COMMENT '하위기관 명', -- 하위기관 명
	ODR                CHAR(4)      NULL     COMMENT '차수', -- 차수
	ORD                CHAR(4)      NULL     COMMENT '서열', -- 서열
	PSTINST_ODR        CHAR(4)      NULL     COMMENT '소속기관 차수', -- 소속기관 차수
	SEHIGH_INSTT_CODE  CHAR(7)      NULL     COMMENT '차상위 기관 코드', -- 차상위 기관 코드
	BEST_INSTT_CODE    CHAR(7)      NULL     COMMENT '최상위 기관 코드', -- 최상위 기관 코드
	REPRSNT_INSTT_CODE CHAR(7)      NULL     COMMENT '대표 기관 코드', -- 대표 기관 코드
	TY_CL_LRGE         CHAR(4)      NULL     COMMENT '유형분류 대', -- 유형분류 대
	TY_CL_MIDDL        CHAR(4)      NULL     COMMENT '유형분류 중', -- 유형분류 중
	TY_CL_SMALL        CHAR(4)      NULL     COMMENT '유형분류 소', -- 유형분류 소
	ZIP                CHAR(6)      NULL     COMMENT '우편번호', -- 우편번호
	LNM                VARCHAR(10)  NULL     COMMENT '지번', -- 지번
	ADRES              VARCHAR(255) NULL     COMMENT '주소', -- 주소
	TELNO              VARCHAR(30)  NULL     COMMENT '전화번호', -- 전화번호
	FXNUM              VARCHAR(30)  NULL     COMMENT '팩스번호' -- 팩스번호
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '기관 관리';

-- 기관 관리
ALTER TABLE INSTT_MANAGE
	ADD CONSTRAINT PK_INSTT_MANAGE -- 기관 관리 기본키
		PRIMARY KEY (
			INSTT_CODE -- 기관 코드
		);

-- 컬럼별진단대상설정정보
CREATE TABLE DGNSS_SAVE (
	INSTT_CODE    CHAR(7)     NOT NULL COMMENT '기관 코드', -- 기관 코드
	DGNSS_SAVE_ID VARCHAR(20) NOT NULL COMMENT '분석설정저장아이디', -- 분석설정저장아이디
	DGNSS_DBMS_ID CHAR(4)     NOT NULL COMMENT '진단대상데이터베이순버', -- 진단대상데이터베이순버
	DGNSS_SAVE_NM VARCHAR(20) NULL     COMMENT '분석설정저장명', -- 분석설정저장명
	TABLE_NM      VARCHAR(80) NULL     COMMENT '테이블 명', -- 테이블 명
	COLUMN_NM_DC  TEXT        NULL     COMMENT '컬럼명 설명', -- 컬럼명 설명
	ANALS_ID_DC   TEXT        NULL     COMMENT '분석 아이디 설명', -- 분석 아이디 설명
	REGIST_DT     DATETIME    NULL     COMMENT '등록일시', -- 등록일시
	REGISTER_ID   VARCHAR(20) NULL     COMMENT '새 컬럼', -- 새 컬럼
	schema_nm     VARCHAR(50) NULL     COMMENT '데이터베이스 명' -- 데이터베이스 명
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '컬럼별진단대상설정정보';

-- 컬럼별진단대상설정정보
ALTER TABLE DGNSS_SAVE
	ADD CONSTRAINT PK_DGNSS_SAVE -- 컬럼별진단대상설정정보 기본키
		PRIMARY KEY (
			INSTT_CODE,    -- 기관 코드
			DGNSS_SAVE_ID, -- 분석설정저장아이디
			DGNSS_DBMS_ID  -- 진단대상데이터베이순버
		);


-- 진단 오류 정보
CREATE TABLE DGNSS_ERROR (
	INSTT_CODE     CHAR(7)     NOT NULL COMMENT '기관 코드', -- 기관 코드
	DGNSS_DBMS_ID  CHAR(4)     NOT NULL COMMENT '진단대상데이터베이스순번', -- 진단대상데이터베이스순번
	DGNSS_INFO_ID  VARCHAR(20) NOT NULL COMMENT '진단 정보 아이디', -- 진단 정보 아이디
	COLUMN_NM      VARCHAR(80) NOT NULL COMMENT '컬럼 명', -- 컬럼 명
	ANALS_ID       CHAR(4)     NOT NULL COMMENT '분석 아이디', -- 분석 아이디
	DGNSS_ERROR_ID VARCHAR(20) NULL     COMMENT '진단오류 아이디', -- 진단오류 아이디
	SCHEMA_NM      VARCHAR(50) NULL     COMMENT '스키마 명', -- 스키마 명
	TABLE_NM       VARCHAR(80) NULL     COMMENT '테이블 명', -- 테이블 명
	ERROR_NM       TEXT        NULL     COMMENT '오류 명', -- 오류 명
	ERROR_CONTENT  TEXT        NULL     COMMENT '오류 내용', -- 오류 내용
	REGIST_DT      DATETIME    NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '진단 오류 정보';

-- 진단 오류 정보
ALTER TABLE DGNSS_ERROR
	ADD CONSTRAINT PK_DGNSS_ERROR -- 진단 오류 정보 기본키
		PRIMARY KEY (
			INSTT_CODE,    -- 기관 코드
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			DGNSS_INFO_ID, -- 진단 정보 아이디
			COLUMN_NM,     -- 컬럼 명
			ANALS_ID       -- 분석 아이디
		);

-- 사용자
ALTER TABLE `USER`
	ADD CONSTRAINT FK_INSTT_TO_USER -- 기관 정보 -> 사용자
		FOREIGN KEY (
			INSTT_CODE -- 기관 코드
		)
		REFERENCES INSTT ( -- 기관 정보
			INSTT_CODE -- 기관 코드
		);

-- 메뉴권한코드
ALTER TABLE MENU_AUTHOR
	ADD CONSTRAINT FK_MENU_MANAGE_TO_MENU_AUTHOR -- 메뉴관리 -> 메뉴권한코드
		FOREIGN KEY (
			MENU_SN -- 메뉴 순번
		)
		REFERENCES MENU_MANAGE ( -- 메뉴관리
			MENU_SN -- 메뉴 순번
		);

-- 메뉴관리
ALTER TABLE MENU_MANAGE
	ADD CONSTRAINT FK_MENU_GROUP_TO_MENU_MANAGE -- 메뉴그룹관리 -> 메뉴관리
		FOREIGN KEY (
			MENU_GROUP_SN -- 메뉴 그룹 순번
		)
		REFERENCES MENU_GROUP ( -- 메뉴그룹관리
			MENU_GROUP_SN -- 메뉴 그룹 순번
		);

-- 공통코드상세
ALTER TABLE CMMN_CODE_DETAIL
	ADD CONSTRAINT FK_CMMN_CODE_TO_CMMN_CODE_DETAIL -- 공통코드 -> 공통코드상세
		FOREIGN KEY (
			GROUP_CODE -- 그룹 코드
		)
		REFERENCES CMMN_CODE ( -- 공통코드
			GROUP_CODE -- 그룹 코드
		);

-- 기관 정보
ALTER TABLE INSTT
	ADD CONSTRAINT FK_INSTT_MANAGE_TO_INSTT -- 기관 관리 -> 기관 정보
		FOREIGN KEY (
			INSTT_CODE -- 기관 코드
		)
		REFERENCES INSTT_MANAGE ( -- 기관 관리
			INSTT_CODE -- 기관 코드
		);

-- 진단대상데이터베이스정보
ALTER TABLE DGNSS_DBMS
	ADD CONSTRAINT FK_INSTT_DBMS_VER_TO_DGNSS_DBMS -- DBMS 버전 정보 -> 진단대상데이터베이스정보
		FOREIGN KEY (
			DBMS_ID,     -- DBMS 순번
			DBMS_VER_ID  -- DBMS 버전 ID
		)
		REFERENCES INSTT_DBMS_VER ( -- DBMS 버전 정보
			DBMS_ID,     -- DBMS 순번
			DBMS_VER_ID  -- DBMS 버전 ID
		);

-- 진단대상데이터베이스정보
ALTER TABLE DGNSS_DBMS
	ADD CONSTRAINT FK_INSTT_TO_DGNSS_DBMS -- 기관 정보 -> 진단대상데이터베이스정보
		FOREIGN KEY (
			INSTT_CODE -- 기관 코드
		)
		REFERENCES INSTT ( -- 기관 정보
			INSTT_CODE -- 기관 코드
		);

-- DBMS 버전 정보
ALTER TABLE INSTT_DBMS_VER
	ADD CONSTRAINT FK_INSTT_DBMS_TO_INSTT_DBMS_VER -- DBMS 정보 -> DBMS 버전 정보
		FOREIGN KEY (
			DBMS_ID -- DBMS 순번
		)
		REFERENCES INSTT_DBMS ( -- DBMS 정보
			DBMS_ID -- DBMS 순번
		);

-- 진단 정보
ALTER TABLE DGNSS_TABLES
	ADD CONSTRAINT FK_DGNSS_DBMS_TO_DGNSS_TABLES -- 진단대상데이터베이스정보 -> 진단 정보
		FOREIGN KEY (
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			INSTT_CODE     -- 기관 코드
		)
		REFERENCES DGNSS_DBMS ( -- 진단대상데이터베이스정보
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			INSTT_CODE     -- 기관 코드
		);

-- 진단 정보
ALTER TABLE DGNSS_TABLES
	ADD CONSTRAINT FK_USER_TO_DGNSS_TABLES -- 사용자 -> 진단 정보
		FOREIGN KEY (
			INSTT_CODE, -- 기관 코드
			USER_ID     -- 사용자 아이디
		)
		REFERENCES USER ( -- 사용자
			INSTT_CODE, -- 기관 코드
			USER_ID     -- 사용자 아이디
		);

-- 컬럼별 진단 정보
ALTER TABLE DGNSS_COLUMNS
	ADD CONSTRAINT FK_DGNSS_TABLES_TO_DGNSS_COLUMNS -- 진단 정보 -> 컬럼별 진단 정보
		FOREIGN KEY (
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			INSTT_CODE,    -- 기관 코드
			DGNSS_INFO_ID  -- 진단 정보 아이디
		)
		REFERENCES DGNSS_TABLES ( -- 진단 정보
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			INSTT_CODE,    -- 기관 코드
			DGNSS_INFO_ID  -- 진단 정보 아이디
		);

-- 컬럼별 진단 항목별 진단 정보
ALTER TABLE DGNSS_COLUMNS_RES
	ADD CONSTRAINT FK_DGNSS_COLUMNS_TO_DGNSS_COLUMNS_RES -- 컬럼별 진단 정보 -> 컬럼별 진단 항목별 진단 정보
		FOREIGN KEY (
			INSTT_CODE,    -- 기관 코드
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			DGNSS_INFO_ID, -- 진단 정보 아이디
			COLUMN_NM      -- 컬럼 명
		)
		REFERENCES DGNSS_COLUMNS ( -- 컬럼별 진단 정보
			INSTT_CODE,    -- 기관 코드
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			DGNSS_INFO_ID, -- 진단 정보 아이디
			COLUMN_NM      -- 컬럼 명
		);

-- 컬럼별 진단 항목별 진단 정보
ALTER TABLE DGNSS_COLUMNS_RES
	ADD CONSTRAINT FK_ANALS_TO_DGNSS_COLUMNS_RES -- 분석 정보 -> 컬럼별 진단 항목별 진단 정보
		FOREIGN KEY (
			INSTT_CODE, -- 기관 코드
			ANALS_ID    -- 분석 순번
		)
		REFERENCES ANALS ( -- 분석 정보
			INSTT_CODE, -- 기관 코드
			ANALS_ID    -- 분석 순번
		);

-- 빈도 분석 결과
ALTER TABLE FRQ_ANALS
	ADD CONSTRAINT FK_DGNSS_COLUMNS_TO_FRQ_ANALS -- 컬럼별 진단 정보 -> 빈도 분석 결과
		FOREIGN KEY (
			INSTT_CODE,    -- 기관 코드
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			DGNSS_INFO_ID, -- 진단 정보 아이디
			COLUMN_NM      -- 컬럼 명
		)
		REFERENCES DGNSS_COLUMNS ( -- 컬럼별 진단 정보
			INSTT_CODE,    -- 기관 코드
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			DGNSS_INFO_ID, -- 진단 정보 아이디
			COLUMN_NM      -- 컬럼 명
		);

-- 컬럼별진단대상설정정보
ALTER TABLE DGNSS_SAVE
	ADD CONSTRAINT FK_DGNSS_DBMS_TO_DGNSS_SAVE -- 진단대상데이터베이스정보 -> 컬럼별진단대상설정정보
		FOREIGN KEY (
			DGNSS_DBMS_ID, -- 진단대상데이터베이순버
			INSTT_CODE     -- 기관 코드
		)
		REFERENCES DGNSS_DBMS ( -- 진단대상데이터베이스정보
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			INSTT_CODE     -- 기관 코드
		);

-- 진단 오류 정보
ALTER TABLE DGNSS_ERROR
	ADD CONSTRAINT FK_DGNSS_COLUMNS_RES_TO_DGNSS_ERROR -- 컬럼별 진단 항목별 진단 정보 -> 진단 오류 정보
		FOREIGN KEY (
			INSTT_CODE,    -- 기관 코드
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			DGNSS_INFO_ID, -- 진단 정보 아이디
			COLUMN_NM,     -- 컬럼 명
			ANALS_ID       -- 분석 아이디
		)
		REFERENCES DGNSS_COLUMNS_RES ( -- 컬럼별 진단 항목별 진단 정보
			INSTT_CODE,    -- 기관 코드
			DGNSS_DBMS_ID, -- 진단대상데이터베이스순번
			DGNSS_INFO_ID, -- 진단 정보 아이디
			COLUMN_NM,     -- 컬럼 명
			ANALS_ID       -- 분석 순번
		);