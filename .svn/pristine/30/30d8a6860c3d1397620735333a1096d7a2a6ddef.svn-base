DROP TABLE FRQ_ANALS;
DROP TABLE DGNSS_ERROR;
DROP TABLE DGNSS_COLUMNS_RES;
DROP TABLE DGNSS_COLUMNS;
DROP TABLE DGNSS_SAVE;
DROP TABLE DGNSS_TABLES;
DROP TABLE ANALS;
DROP TABLE MENU_AUTHOR;
DROP TABLE MENU_MANAGE;
DROP TABLE MENU_GROUP;
DROP TABLE CMMN_CODE_DETAIL;
DROP TABLE CMMN_CODE;
DROP TABLE DGNSS_DBMS;
DROP TABLE INSTT_DBMS_VER;
DROP TABLE INSTT_DBMS;
DROP TABLE `USER`;
DROP TABLE INSTT;
DROP TABLE INSTT_MANAGE;


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
	
	
-- 메뉴권한코드
CREATE TABLE MENU_AUTHOR (
	MENU_SN     CHAR(4)     NOT NULL COMMENT '메뉴 순번', -- 메뉴 순번
	AUTHOR_CODE VARCHAR(20) NOT NULL COMMENT '권한 코드', -- 권한 코드
	USE_AT      BIT         NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '메뉴권한코드';

-- 메뉴관리
ALTER TABLE MENU_AUTHOR
	ADD CONSTRAINT PK_MENU_AUTHOR -- 메뉴관리 기본키
		PRIMARY KEY (
			MENU_SN, -- 메뉴 순번
			AUTHOR_CODE -- 권한 코드
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
			INSTT_CODE -- 기관 코드
		)
		REFERENCES USER ( -- 사용자
			INSTT_CODE -- 기관 코드
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



INSERT INTO menu_group
  (menu_group_sn, menu_group_nm, use_at)
VALUES
  (1, 'PROPERTIES', 1);

INSERT INTO menu_group
  (menu_group_sn, menu_group_nm, use_at)
VALUES
  (2, 'BASIC', 1);

INSERT INTO menu_group
  (menu_group_sn, menu_group_nm, use_at)
VALUES
  (3, 'RULE', 1);

INSERT INTO menu_group
  (menu_group_sn, menu_group_nm, use_at)
VALUES
  (4, 'PROFILING', 1);

 
INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (2, 1, 1, '공통정보 관리', '#', 1, 0, 1, 'fa-tasks');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (4, 1, 3, '운영정보 관리', '/mngr/cmmnMng/cmmnManage', 1, 0, 1, 'fa-file-alt');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (5, 1, 1, '기본정보 관리', '/mngr/basicInfo/dataConnProp.do', 1, 0, 2, 'fa-info');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (6, 1, 2, '데이터연결 관리', '/mngr/basicInfo/dgnssDbmsList', 1, 0, 2, 'fa-database');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (7, 1, 1, '업무규칙 관리', '#', 1, 0, 3, 'fa-qrcode');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (8, 2, 1, '허용 값 범위 설정', '/mngr/ruleMng/ruleManageLimit.do', 1, 7, 3, '');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (9, 2, 2, '사용자정의(정규식)', '/mngr/ruleMng/ruleManagePatten.do', 1, 7, 3, '');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (10, 2, 3, '사용자정의(SQL)', '/mngr/ruleMng/ruleManageSql.do', 1, 7, 3, '');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (11, 1, 1, '패턴/지표 관리', '/mngr/analysis/analysisPatternList', 1, 0, 3, 'fa-folder');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (12, 1, 1, '스키마 구조 분석', '#', 1, 0, 4, 'fa-sitemap');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (13, 2, 1, '테이블 구조 분석', '/mngr/analysis/analysisTableList', 1, 12, 4, '');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (14, 2, 2, '컬럼 구조 분석', '/mngr/analysis/analysisColumnList', 1, 12, 4, '');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (15, 1, 2, '프로파일링 분석', '/mngr/diagnosis/form1.do', 1, 0, 4, 'fa-table');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (16, 1, 3, '프로파일링 결과', '/mngr/diagnosis/result/list.do', 1, 0, 4, 'fa-th-list');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (20, 2, 1, ' 메뉴 관리', '/mngr/menuMng/menuManage', 1, 2, 1, '');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (22, 2, 3, '공통코드 관리', '/mngr/cmmnMng/cmmnCode', 1, 2, 1, '');

INSERT INTO menu_manage
  (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, upper_menu_sn, menu_group_sn, menu_icon)
VALUES
  (23, 2, 2, '메뉴권한 관리', '/mngr/menuMng/menuAuthor', 1, 2, 1, '');


INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (2, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (2, 'ROLE_USER', 0);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (4, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (4, 'ROLE_USER', 0);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (5, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (5, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (6, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (6, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (7, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (7, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (8, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (8, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (9, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (9, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (10, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (10, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (11, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (11, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (12, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (12, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (13, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (13, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (14, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (14, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (15, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (15, 'ROLE_USER', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (16, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (16, 'ROLE_USER', 1);


INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (20, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (22, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (23, 'ROLE_ADMIN', 1);

INSERT INTO menu_author
  (menu_sn, author_code, USE_AT)
VALUES
  (23, 'ROLE_USER', 1);

 
INSERT INTO instt_dbms
  (DBMS_ID, DBMS_KND, RM, USE_AT)
VALUES
  (1, 'Oracle', NULL, 1);

INSERT INTO instt_dbms
  (DBMS_ID, DBMS_KND, RM, USE_AT)
VALUES
  (2, 'CSV', NULL, 1);


 INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '10.1.0.2', 'Oracle 10g (Release 1)10.1.0.2', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '10.1.0.5', 'Oracle 10g (Release 1)10.1.0.5', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '10.2.0.1', 'Oracle 10g (Release 2)10.2.0.1', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '10.2.0.5', 'Oracle 10g (Release 2)10.2.0.5', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '11.1.0.6', 'Oracle 11g (Release 1)11.1.0.6', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '11.1.0.7', 'Oracle 11g (Release 1)11.1.0.7', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '11.2.0.1', 'Oracle 11g (Release 2)11.2.0.1', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '11.2.0.4', 'Oracle 11g (Release 2)11.2.0.4', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '12.1.0.1', 'Oracle 12c (Release 1)12.1.0.1', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '12.1.0.2.0', 'Oracle 12c (Release 1)12.1.0.2.0', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '12.2.0.1', 'Oracle 12c (Release 2)12.2.0.1', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '12.2.0.1.0', 'Oracle 12c (Release 2)12.2.0.1.0', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '9.0.1.0', 'Oracle 9i (Release 1)9.0.1.0', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '9.0.1.4', 'Oracle 9i (Release 1)9.0.1.4', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '9.2.0.1', 'Oracle 9i (Release 2)9.2.0.1', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '9.2.0.8', 'Oracle 9i (Release 2)9.2.0.8', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (1, '00', '', '', 1);

INSERT INTO instt_dbms_ver
  (DBMS_ID, DBMS_VER_ID, DBMS_VER_INFO, RM, USE_AT)
VALUES
  (2, '10.0', 'MariaDB 10.0 Series ', '', 1);


INSERT INTO cmmn_code
  (GROUP_CODE, GROUP_CODE_NM, GROUP_CODE_DC, USE_AT)
VALUES
  ('AG000000', '분석 구분', NULL, 1);

INSERT INTO cmmn_code
  (GROUP_CODE, GROUP_CODE_NM, GROUP_CODE_DC, USE_AT)
VALUES
  ('AT000000', '분석 타입', NULL, 1);

INSERT INTO cmmn_code
  (GROUP_CODE, GROUP_CODE_NM, GROUP_CODE_DC, USE_AT)
VALUES
  ('DK000000', 'DBMS 종류', NULL, 1);

INSERT INTO cmmn_code
  (GROUP_CODE, GROUP_CODE_NM, GROUP_CODE_DC, USE_AT)
VALUES
  ('DQ_AUTHOR_CODE', '권한코드', NULL, 1);

INSERT INTO cmmn_code
  (GROUP_CODE, GROUP_CODE_NM, GROUP_CODE_DC, USE_AT)
VALUES
  ('DV000000', 'DBMS 버전', NULL, 1);

INSERT INTO cmmn_code
  (GROUP_CODE, GROUP_CODE_NM, GROUP_CODE_DC, USE_AT)
VALUES
  ('PT000000', '패턴', NULL, 1);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AG000000', 'AG000100', '기본분석', '기본분석', '0', 1, 12);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AG000000', 'AG000200', 'Value Freq.', 'Value Freq.', '0', 1, 17);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AG000000', 'AG000300', '기본패턴', '기본패턴', '0', 1, 18);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AG000000', 'AG000401', '사용자정의 패턴', '사용자정의 패턴', '0', 1, 28);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AG000000', 'AG000402', '사용자정의 SQL', '사용자정의 SQL', '0', 1, 29);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AG000000', 'AG000601', '범주-숫자', '범주-숫자', '0', 1, 30);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AG000000', 'AG000602', '범주-문자', '범주-문자', '0', 1, 31);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AG000000', 'AG000603', '범주-날짜', '범주-날짜', '0', 1, 32);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AT000000', 'AT000100', 'SQL', 'SQL', '0', 1, 34);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AT000000', 'AT000200', '패턴', '패턴', '0', 1, 35);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('AT000000', 'AT000300', '범주', '범주', '0', 1, 36);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DK000000', 'DK000101', 'Oracle', 'Oracle', '0', 1, 2);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DK000000', 'DK000201', 'MySQL', 'MySQL', '0', 1, 3);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DK000000', 'DK000301', 'MS-SQL', 'MS-SQL', '0', 1, 4);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DK000000', 'DK000401', 'Maria', 'Maria', '0', 1, 4);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DK000000', 'DK000901', 'CSV', 'CSV', '0', 1, 5);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DQ_AUTHOR_CODE', 'ROLE_ADMIN', 'admin', 'admin', '0', 1, 1);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DQ_AUTHOR_CODE', 'ROLE_USER', 'user', 'user', '0', 1, 2);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DV000000', 'DV000109', 'Oracle9', 'Oracle9', '0', 1, 7);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DV000000', 'DV000110', 'Oracle10', 'Oracle10', '0', 1, 8);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DV000000', 'DV000111', 'Oracle11', 'Oracle11', '0', 1, 9);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('DV000000', 'DV000112', 'Oracle12', 'Oracle12', '0', 1, 10);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('PT000000', 'PT000101', '날짜', '날짜', '0', 1, 19);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('PT000000', 'PT000201', '전화번호', '전화번호', '0', 1, 20);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('PT000000', 'PT000301', '주소', '주소', '0', 1, 21);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('PT000000', 'PT000401', '우편번호', '우편번호', '0', 1, 22);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('PT000000', 'PT000501', 'Email', 'Email', '0', 1, 23);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('PT000000', 'PT000601', 'IP(V4)', 'IP(V4)', '0', 1, 24);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('PT000000', 'PT000701', 'IP(V6)', 'IP(V6)', '0', 1, 25);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('PT000000', 'PT000801', 'MAC', 'MAC', '0', 1, 26);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('PT000000', 'PT000901', 'Url', 'Url', '0', 1, 27);

INSERT INTO cmmn_code_detail
  (GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR)
VALUES
  ('PT000000', 'PT001001', '시간', '시간', '0', 1, 19);


INSERT INTO anals (INSTT_CODE,ANALS_ID,ANALS_SE,ANALS_TY,PTTRN_SE,ANALS_NM,ANALS_FRMLA,BEGIN_VALUE,END_VALUE,DBMS_KND,DBMS_VER,RM,USE_AT,REGIST_DT) VALUES 
('0000003','1','AG000100','AT000100',NULL,'Row Count','SELECT COUNT(*) FROM $TABLE_NAME',NULL,NULL,'ORACLE',NULL,'Row Count : 전체 데이터 건수',1,NULL)
,('0000003','10','AG000300','AT000200','PT000601','IP(V4)','^([01]?[[:digit:]][[:digit:]]?|2[0-4][[:digit:]]|25[0-5]).([01]?[[:digit:]][[:digit:]]?|2[0-4][[:digit:]]|25[0-5]).([01]?[[:digit:]][[:digit:]]?|2[0-4][[:digit:]]|25[0-5]).([01]?[[:digit:]][[:digit:]]?|2[0-4][[:digit:]]|25[0-5])$',NULL,NULL,'ORACLE',NULL,'IP(V4) 형식을 체크 한다.',1,NULL)
,('0000003','11','AG000300','AT000200','PT000701','IP(V6)','^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))).){3}(((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))))|(([0-9A-Fa-f]{1,4}:){0,5}:((((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))).){3}(((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))))|(::([0-9A-Fa-f]{1,4}:){0,5}((((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))).){3}(((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$',NULL,NULL,'ORACLE',NULL,'IP(V6) 형식을 체크 한다.',1,NULL)
,('0000003','12','AG000300','AT000200','PT000801','MAC','^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$',NULL,NULL,'ORACLE',NULL,'MAC 형식을 체크 한다.',1,NULL)
,('0000003','13','AG000300','AT000200','PT000901','URL','^(https?://)?[[:alnum:]_-][.[[:alnum:]_-]*]*.(com|edu|org|net|int|info|eu|biz|mil|gov|aero|travel|pro|name|museum|coop|asia|[a-z][a-z])+(:[[:digit:]]+)?[/[[:alnum:]._#-?]*]*/?$',NULL,NULL,'ORACLE',NULL,'URL 형식을 체크 한다.',1,NULL)
,('0000003','19','AG000300','AT000200','PT000401','우편번호','^[[:digit:]]{5}$|^([[:digit:]]{3})([-])?([[:digit:]]{3})$',NULL,NULL,'ORACLE',NULL,'우편번호 : 구/신 우편번호를 체크 할',1,NULL)
,('0000003','2','AG000100','AT000100',NULL,'NULL 값','SELECT COUNT($COLUMN_NAME) FROM $TABLE_NAME WHERE $COLUMN_NAME IS NULL',NULL,NULL,'ORACLE',NULL,'NULL 값 : 데이터값이 NULL 여부 확인',1,NULL)
,('0000003','22','AG000300','AT000200','PT001001','시간 (HH24MISS)','^([0-1]?[[:digit:]]|2[0-4])([0-5][[:digit:]])([0-5][[:digit:]])?$',NULL,NULL,'ORACLE',NULL,'시간 (HH24MISS)',1,NULL)
,('0000003','24','AG000300','AT000200','PT001001','시간 (HHMISS)','^([[:digit:]]|1[0-2]|)([0-5][[:digit:]])([0-5][[:digit:]])?$',NULL,NULL,'ORACLE',NULL,'시간 : (HHMISS)',1,NULL)
,('0000003','29','AG000100','AT000100',NULL,'Row Count','SELECT COUNT(*) FROM $TABLE_NAME',NULL,NULL,'CSV',NULL,'Row Count : 전체 데이터 건수',1,NULL)
;
INSERT INTO anals (INSTT_CODE,ANALS_ID,ANALS_SE,ANALS_TY,PTTRN_SE,ANALS_NM,ANALS_FRMLA,BEGIN_VALUE,END_VALUE,DBMS_KND,DBMS_VER,RM,USE_AT,REGIST_DT) VALUES 
('0000003','3','AG000100','AT000200',NULL,'숫자유효성','^[[:digit:]]*$',NULL,NULL,'ORACLE',NULL,'숫자유효성 : 숫자 이외의 값이 있을때는 불일치',1,NULL)
,('0000003','30','AG000100','AT000100',NULL,'NULL 값','SELECT COUNT($COLUMN_NAME) FROM $TABLE_NAME WHERE $COLUMN_NAME IS NULL',NULL,NULL,'CSV',NULL,'NULL 값 : 데이터값이 NULL 여부 확인',1,NULL)
,('0000003','31','AG000100','AT000200',NULL,'숫자유효성','^[[:digit:]]*$',NULL,NULL,'CSV',NULL,'숫자유효성 : 숫자 이외의 값이 있을때는 불일치',1,NULL)
,('0000003','32','AG000100','AT000200',NULL,'한글유효성','^([가-힣][ ]?[가-힣]?)*$',NULL,NULL,'CSV',NULL,'한글유효성 : 한글 이외의 값이 있을때 불일치(단 스페이스 허용)',1,NULL)
,('0000003','33','AG000200','AT000100',NULL,'Value Frequency.','SELECT COUNT($COLUMN_NAME) FROM $TABLE_NAME GROUP BY $COLUMN_NAME ORDER BY 1',NULL,NULL,'CSV',NULL,'Value Frequency : 데이터 중복률 상위 10건을 그래프로 표기',1,NULL)
,('0000003','34','AG000300','AT000200','PT000101','날짜','^((19|20)[[:digit:]]{2})(/|-)?((0[1-9])|(1[0-2]))(/|-)?((0[1-9])|(1[[:digit:]])|(2[[:digit:]])|(3[0-1]))$',NULL,NULL,'CSV',NULL,'날짜 (YYYY-MM-DD)',1,NULL)
,('0000003','35','AG000300','AT000200','PT000201','전화번호','^([[:digit:]]{2,3})[-]?([[:digit:]]{3,4})[-]?([[:digit:]]{4})$',NULL,NULL,'CSV',NULL,'전화번호 (999-9999-9999)',1,NULL)
,('0000003','36','AG000300','AT000200','PT000301','주소','(([가-힣]+([[:digit:]]{1,5}|[[:digit:]]{1,5}(,|.)[[:digit:]]{1,5}|)+(읍|면|동|가|리))(^구|)(([[:digit:]]{1,5}(~|-)[[:digit:]]{1,5}|[[:digit:]]{1,5})(가|리|)|))([ ](산([[:digit:]]{1,5}(~|-)[[:digit:]]{1,5}|[[:digit:]]{1,5}))|)|(([가-힣]|([[:digit:]]{1,5}(~|-)[[:digit:]]{1,5})|[[:digit:]]{1,5})+(로|길))',NULL,NULL,'CSV',NULL,'주소 형식을 체크 한다.',1,NULL)
,('0000003','37','AG000300','AT000200','PT000501','Email','^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$',NULL,NULL,'CSV',NULL,'Email 형식을 체크 한다.',1,NULL)
,('0000003','38','AG000300','AT000200','PT000601','IP(V4)','^([01]?[[:digit:]][[:digit:]]?|2[0-4][[:digit:]]|25[0-5]).([01]?[[:digit:]][[:digit:]]?|2[0-4][[:digit:]]|25[0-5]).([01]?[[:digit:]][[:digit:]]?|2[0-4][[:digit:]]|25[0-5]).([01]?[[:digit:]][[:digit:]]?|2[0-4][[:digit:]]|25[0-5])$',NULL,NULL,'CSV',NULL,'IP(V4) 형식을 체크 한다.',1,NULL)
;
INSERT INTO anals (INSTT_CODE,ANALS_ID,ANALS_SE,ANALS_TY,PTTRN_SE,ANALS_NM,ANALS_FRMLA,BEGIN_VALUE,END_VALUE,DBMS_KND,DBMS_VER,RM,USE_AT,REGIST_DT) VALUES 
('0000003','39','AG000300','AT000200','PT000701','IP(V6)','^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))).){3}(((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))))|(([0-9A-Fa-f]{1,4}:){0,5}:((((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))).){3}(((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))))|(::([0-9A-Fa-f]{1,4}:){0,5}((((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))).){3}(((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$',NULL,NULL,'CSV',NULL,'IP(V6) 형식을 체크 한다.',1,NULL)
,('0000003','4','AG000100','AT000200',NULL,'한글유효성','^([가-힣][ ]?[가-힣]?)*$',NULL,NULL,'ORACLE',NULL,'한글유효성 : 한글 이외의 값이 있을때 불일치(단 스페이스 허용)',1,NULL)
,('0000003','40','AG000300','AT000200','PT000801','MAC','^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$',NULL,NULL,'CSV',NULL,'MAC 형식을 체크 한다.',1,NULL)
,('0000003','41','AG000300','AT000200','PT000901','URL','^(https?://)?[[:alnum:]_-][\\.[[:alnum:]_-]*]*\\.(com|edu|org|net|int|info|eu|biz|mil|gov|aero|travel|pro|name|museum|coop|asia|[a-z][a-z])+(:[[:digit:]]+)?[/[[:alnum:]\\._#-\\?]*]*/?$',NULL,NULL,'CSV',NULL,'URL 형식을 체크 한다.',1,NULL)
,('0000003','47','AG000300','AT000200','PT000401','우편번호','^[[:digit:]]{5}$|^([[:digit:]]{3})([-])?([[:digit:]]{3})$',NULL,NULL,'CSV',NULL,'우편번호 : 구/신 우편번호를 체크 할',1,NULL)
,('0000003','48','AG000300','AT000200','PT001001','시간 (HH24MISS)','^([0-1]?[0-9]|2[0-4])([0-5][0-9])([0-5][0-9])?$',NULL,NULL,'CSV',NULL,'시간 (HH24MISS)',1,NULL)
,('0000003','49','AG000300','AT000200','PT001001','시간 (HHMISS)','^([0-9]|1[0-2]|)([0-5][0-9])([0-5][0-9])?$',NULL,NULL,'CSV',NULL,'시간 (HHMISS)',1,NULL)
,('0000003','5','AG000200','AT000100',NULL,'Value Frequency','SELECT COUNT($COLUMN_NAME) FROM $TABLE_NAME GROUP BY $COLUMN_NAME ORDER BY 1',NULL,NULL,'ORACLE',NULL,'Value Frequency : 데이터 중복률 상위 10건을 그래프로 표기',1,NULL)
,('0000003','53','AG000601','AT000300','','20대 성인 기준','20 ~ 29','20','29','Oracle','12.1.0.1','20대 나이 기준 범위 설정을 한다.',1,'2019-08-02 00:00:00.000')
,('0000003','54','AG000603','AT000300','','2018년도 기준','20180101 ~ 20181231','20180101','20181231','Oracle','12.1.0.1','2018년도 기준 기간 범위를 설정한다.',1,'2019-08-02 00:00:00.000')
;
INSERT INTO anals (INSTT_CODE,ANALS_ID,ANALS_SE,ANALS_TY,PTTRN_SE,ANALS_NM,ANALS_FRMLA,BEGIN_VALUE,END_VALUE,DBMS_KND,DBMS_VER,RM,USE_AT,REGIST_DT) VALUES 
('0000003','55','AG000300','AT000200','','주민등록번호','^[[:digit:]]{6}[-]?[[:digit:]]{7}$','','','Oracle','12.1.0.1','주민번호 형식을 체크 한다.
(숫자6-숫자7, 숫자13)',1,NULL)
,('0000003','56','AG000402','AT000100','','사용자명 체크','SELECT COUNT(1) FROM DQSYS.DQ_KMI0004_DATA WHERE DQ_NAME LIKE ''user7%''','','','Oracle','12.1.0.1','사용자명이 user7으로 시작되는 사용자 ',1,'2019-08-02 00:00:00.000')
,('0000003','57','AG000300','AT000200','','Hex Color Codes','^\\#?[A-Fa-f0-9]{3}([A-Fa-f0-9]{3})?$','','','Oracle','00','HTML 또는 CSS 파일에 저굥되는 색상 코드를 정의한다.',1,NULL)
,('0000003','58','AG000300','AT000200','','시간 (24 Hour Time)','^([0-1]?[0-9]|2[0-4]):([0-5][0-9])(:[0-5][0-9])?$','','','Oracle','00','24시 시간 설정으로 HH:MM:SS 포멧의 형식을 정의한다.',1,NULL)
,('0000003','59','AG000300','AT000200','','날짜 (Date  DD MMM YYYY)','^(([0-9])|([0-2][0-9])|([3][0-1]))(\\/|-)(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)(\\/|-)([0-9]{4})$','','','Oracle','00','월을 MMM 형식으로 표기 하는 날짜 형식이다.
예)1/Feb/2007, 03-Jun-2007, 31/Dec/2007, 1/2/2007, 31-Dec-2007

',1,NULL)
,('0000003','6','AG000300','AT000200','PT000101','날짜','^((19|20)[[:digit:]]{2})(/|-)?((0[1-9])|(1[0-2]))(/|-)?((0[1-9])|(1[[:digit:]])|(2[[:digit:]])|(3[0-1]))$',NULL,NULL,'ORACLE',NULL,'날짜 (YYYY-MM-DD)',1,NULL)
,('0000003','60','AG000603','AT000300','','테스트','20190801 ~ 20190831','20190801','20190831','CSV','10.0','ㅅㄷㄴㅅ',0,'2019-08-14 20:18:47.000')
,('0000003','61','AG000300','AT000200','','색상(Hex Color Codes)','^\\#?[A-Fa-f0-9]{3}([A-Fa-f0-9]{3})?$','','','Oracle','00','HTML, CSS 파일에서 적용되는 색상표시 값 
( fff | #990000 | #cc3366 | #AAAAAA)',1,NULL)
,('0000003','62','AG000300','AT000200','','통화 (금액)','^\\\\?(([1-9],)?([0-9]{3},){0,3}[0-9]{3}|[0-9]{0,16})(원)?$','','','Oracle','00','한국 금액 표기
(\\3,000 , 3,000원, 3000, 3,000)',1,NULL)
,('0000003','63','AG000300','AT000200','','날짜 (YY-MM-DD, YY/MM/DD)','^([0-9]{2})(\\/|-)?((0[1-9])|(1[0-2]))(\\/|-)?((0[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))$','','','Oracle','00','한국형 날짜형식 패턴
(YYYYMMDD, YYYY-MM-DD, YYYY/MM/DD)',1,NULL)
;
INSERT INTO anals (INSTT_CODE,ANALS_ID,ANALS_SE,ANALS_TY,PTTRN_SE,ANALS_NM,ANALS_FRMLA,BEGIN_VALUE,END_VALUE,DBMS_KND,DBMS_VER,RM,USE_AT,REGIST_DT) VALUES 
('0000003','64','AG000300','AT000200','','날짜(Datetime yyyy/mm/dd hh:mm)','^([0-9]{4})(\\/)([0-9]{1,2})(\\/)([0-9]{1,2}) (([01][0-9]|[2][0-3]):([0-5][0-9]))$','','','Oracle','00','한국형 날짜시간형식 패턴
(2019/08/01 10:52)',1,NULL)
,('0000003','65','AG000300','AT000200','','날짜(yyyy/mm/dd hh:mm:ss)','^([0-9]{4})(\\/)([0-9]{1,2})(\\/)([0-9]{1,2}) (([01][0-9]|[2][0-3]):([0-5][0-9]):[0-5][0-9])$','','','Oracle','00','한국형 날짜시간형식 
(YYYY/MM/DD  hh:mm:ss, 2019/10/12 10:22:33)',1,NULL)
,('0000003','66','AG000300','AT000200','','날짜(ISO Date)','^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1[[:digit:]])|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((0[1,3-9])|(1[0-2]))-(29|30)))))$','','','Oracle','00','Matches ISO dates such as 2002-01-31',1,NULL)
,('0000003','67','AG000300','AT000200','','날짜(Month)','^((0[1-9])|(1[0-2]))$','','','Oracle','00','Matches month written as 2-digit numbers (e.g. ''08'', ''12'').',1,NULL)
,('0000003','68','AG000300','AT000200','','금액(Currency 16,3)','^\\$?(([1-9],)?([0-9]{3},){0,3}[0-9]{3}|[0-9]{0,16})(\\.[0-9]{0,3})?$','','','Oracle','00','Currency expression, accepts 4 commas and 4 groups of 3 numbers and 1 number before the first comma, this first number will have to be different from zero. It accepts a number of, two or three decimal. It accepts zero numbers after the point. You can change the number of groups and numbers accespts before and after the point.
(1234.23||1,234.245||1.12,12,0.00||0,123.99)',1,NULL)
,('0000003','69','AG000300','AT000200','','GPS Coordinate','^([0-9]{1,3}[\\.][0-9]*)[,]+-?([0-9]{1,3}[\\.][0-9]*)$','','','Oracle','00','Google Maps style GPS Decimal format
(40.7127837,-74.00594130000002)',1,NULL)
,('0000003','7','AG000300','AT000200','PT000201','전화번호','^([[:digit:]]{2,3})[-]?([[:digit:]]{3,4})[-]?([[:digit:]]{4})$',NULL,NULL,'ORACLE',NULL,'전화번호 (999-9999-9999)',1,NULL)
,('0000003','70','AG000300','AT000200','','Integer values','^[-+]?[1-9][[:digit:]]*\\.?[0]*$','','','Oracle','00','Matches signed or unsigned integers with or without digits. Warning, values like ''0999'' will not match.',1,NULL)
,('0000003','71','AG000300','AT000200','','여권번호(International Passport)','^[A-Z0-9<]{9}[0-9]{1}[A-Z]{3}[0-9]{7}[A-Z]{1}[0-9]{7}[A-Z0-9<]{14}[0-9]{2}$','','','Oracle','00','? 9 characters made up of a combination of numbers and/or letters. Where less than 9 characters it will be padded out to the right with chevrons (<).
? 1 number
? 3 letters
? 7 numbers
? 1 letter
? 7 numbers
? An international passport will have up to 14 characters in this field (numbers and/or letters). If none exist or where less than 14 characters exist, the field will be padded out, but only to the right, with chevrons (<). You should input the data exactly as it is shown on the actual passport field, i.e. input all chevrons to the left of the characters and do not ignore or leave these as blank spaces. Where less than 14 characters, the field will be padded out to the right with chevrons (<), i.e. Z1234567<<<<<
? 1 number
? 1 number
(1234567890ABC1234567A1234567<<<<<<<<<<<<<<12 ||| 0123456781USD5656564M0812120AS34560<<<<<<<36 ||| G0308084<<1ITY9999999Q0410056<<<<<<<<<<<<<<39ASDFER123AQWE!"%^<<<<<<<<<<<12 ||| QASASW3<<1WER3as12232<<AS ||| ABCDEF123AQQQ123456789A1<<<<<AS14)',1,NULL)
,('0000003','72','AG000300','AT000200','','Non Zero 1st Digit','^([-|+])?[1-9]','','','Oracle','00','Matches signed or unsigned numerics starting with 1 to 9 digits.
Values like ''0999'' will not match.
(Check the first digit is a non zero digit)',1,NULL)
;
INSERT INTO anals (INSTT_CODE,ANALS_ID,ANALS_SE,ANALS_TY,PTTRN_SE,ANALS_NM,ANALS_FRMLA,BEGIN_VALUE,END_VALUE,DBMS_KND,DBMS_VER,RM,USE_AT,REGIST_DT) VALUES 
('0000003','73','AG000300','AT000200','','텍스트(Blank text)','^[[:blank:]]*$','','','Oracle','00','Matches text with only white spaces
(Search for blank text)',1,NULL)
,('0000003','74','AG000300','AT000200','','텍스트(Empty text)','^[[:space:]]*$','','','Oracle','00','Will matches values with only whitespaces or tabulation character. Regexp class: [:space:]
(Identify values with only space characters)',1,NULL)
,('0000003','75','AG000300','AT000200','','텍스트(Home Row Text)','[qsdfghjklmQSDFGHJKLM]{3,}','','','Oracle','00','Matches text with random sequences at least 3 consonants such as "smqljfmj"
(Search random sequences with keys from the home row)',1,NULL)
,('0000003','76','AG000300','AT000200','','텍스트(Starts with blank)','^[[:blank:]]','','','Oracle','00','matches: ''  aa''
does not match: ''aa''
(detect data starting with blank characters)',1,NULL)
,('0000003','77','AG000300','AT000200','','텍스트(Starts with space)','^[[:space:]]','','','Oracle','00','matches: '' sklqjf'', '' dqd''
non-matches: ''dsm:kljf''
(detect whitespace at the begining of a data)
',1,NULL)
,('0000003','78','AG000300','AT000200','','텍스트(Starts with uppercase)','^[A-Z]+','','','Oracle','00','Will match when the first character of a text is an uppercase letter. For example, "Axel", "Street" will match.
But "31", "john doe", "3RD FLOOR", "street" will not match.
(identify words in uppercase)',1,NULL)
,('0000003','79','AG000300','AT000200','','텍스트(Uppercased Single Word)','^[A-Z]*$','','','Oracle','00','Will match when all characters of a word are uppercased. For example, "AXEL", "STREET" will match.
But "31", "JOHN DOE", "3RD FLOOR", "street" will not match.
(identify words in uppercase)',1,NULL)
,('0000003','8','AG000300','AT000200','PT000301','주소','(([가-힣]+([[:digit:]]{1,5}|[[:digit:]]{1,5}(,|.)[[:digit:]]{1,5}|)+(읍|면|동|가|리))(^구|)(([[:digit:]]{1,5}(~|-)[[:digit:]]{1,5}|[[:digit:]]{1,5})(가|리|)|))([ ](산([[:digit:]]{1,5}(~|-)[[:digit:]]{1,5}|[[:digit:]]{1,5}))|)|(([가-힣]|([[:digit:]]{1,5}(~|-)[[:digit:]]{1,5})|[[:digit:]]{1,5})+(로|길))',NULL,NULL,'ORACLE',NULL,'주소 형식을 체크 한다.',1,NULL)
,('0000003','9','AG000300','AT000200','PT000501','Email','^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$',NULL,NULL,'ORACLE',NULL,'Email 형식을 체크 한다.',1,NULL)
;
