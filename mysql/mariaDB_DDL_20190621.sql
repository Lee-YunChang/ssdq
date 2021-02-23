
-- 기관 정보
CREATE TABLE `INSTT` (
	`INSTT_CODE`     VARCHAR(20)  NOT NULL COMMENT '기관 코드', -- 기관 코드
	`INSTT_NM`       VARCHAR(255) NULL     COMMENT '기관 명', -- 기관 명
	`BSNS_BGNDE`     VARCHAR(8)   NULL     COMMENT '사업 시작일', -- 사업 시작일
	`BSNS_ENDDE`     VARCHAR(8)   NULL     COMMENT '사업 종료일', -- 사업 종료일
	`REPRSNT_TLPHON` VARCHAR(30)  NULL     COMMENT '대표 전화', -- 대표 전화
	`ADRES`          VARCHAR(255) NULL     COMMENT '주소', -- 주소
	`HMPG`           VARCHAR(255) NULL     COMMENT '홈페이지', -- 홈페이지
	`RM`             VARCHAR(255) NULL     COMMENT '비고', -- 비고
	`USE_AT`         VARCHAR(1)   NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '기관 정보';

-- 기관 정보
ALTER TABLE `INSTT`
	ADD CONSTRAINT `PK_INSTT` -- 기관 정보 기본키
		PRIMARY KEY (
			`INSTT_CODE` -- 기관 코드
		);

-- DBMS 정보
CREATE TABLE `INSTT_DBMS` (
	`DBMS_ID`  VARCHAR(20)  NOT NULL COMMENT 'DBMS 아이디', -- DBMS 아이디
	`DBMS_KND` VARCHAR(255) NULL     COMMENT 'DBMS 종류', -- DBMS 종류
	`RM`       VARCHAR(255) NULL     COMMENT '비고', -- 비고
	`USE_AT`   VARCHAR(1)   NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT 'DBMS 정보';

-- DBMS 정보
ALTER TABLE `INSTT_DBMS`
	ADD CONSTRAINT `PK_INSTT_DBMS` -- DBMS 정보 기본키
		PRIMARY KEY (
			`DBMS_ID` -- DBMS 아이디
		);
	
-- DBMS 버전 정보
CREATE TABLE `INSTT_DBMS_VER` (
	`DBMS_ID`       VARCHAR(20)  NOT NULL COMMENT 'DBMS 아이디', -- DBMS 아이디
	`DBMS_VER_ID`   VARCHAR(20)  NOT NULL COMMENT 'DBMS 버전 아이디', -- DBMS 버전 아이디
	`DBMS_VER_INFO` VARCHAR(255) NULL     COMMENT 'DBMS 버전 정보', -- DBMS 버전 정보
	`RM`            VARCHAR(255) NULL     COMMENT '비고', -- 비고
	`USE_AT`        VARCHAR(1)   NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT 'DBMS 버전 정보';

-- DBMS 버전 정보
ALTER TABLE `INSTT_DBMS_VER`
	ADD CONSTRAINT `PK_INSTT_DBMS_VER` -- DBMS 버전 정보 기본키
		PRIMARY KEY (
			`DBMS_ID`,     -- DBMS 아이디
			`DBMS_VER_ID`  -- DBMS 버전 아이디
		);
	
-- 분석 정보
CREATE TABLE `ANALS` (
	`INSTT_CODE`  VARCHAR(20)   NOT NULL COMMENT '기관 코드', -- 기관 코드
	`ANALS_ID`    VARCHAR(20)   NOT NULL COMMENT '분석 아이디', -- 분석 아이디
	`ANALS_SE`    VARCHAR(20)   NULL     COMMENT '분석 구분', -- 분석 구분
	`ANALS_TY`    VARCHAR(20)   NULL     COMMENT '분석 타입', -- 분석 타입
	`PTTRN_SE`    VARCHAR(20)   NULL     COMMENT '패턴 구분', -- 패턴 구분
	`ANALS_NM`    VARCHAR(255)  NULL     COMMENT '분석 명', -- 분석 명
	`ANALS_FRMLA` VARCHAR(2048) NULL     COMMENT '분석 식', -- 분석 식
	`BEGIN_VALUE` VARCHAR(255)  NULL     COMMENT '시작 값', -- 시작 값
	`END_VALUE`   VARCHAR(255)  NULL     COMMENT '종료 값', -- 종료 값
	`DBMS_KND`    VARCHAR(20)   NULL     COMMENT 'DBMS 종류', -- DBMS 종류
	`DBMS_VER`    VARCHAR(20)   NULL     COMMENT 'DBMS 버전', -- DBMS 버전
	`RM`          VARCHAR(255)  NULL     COMMENT '비고', -- 비고
	`USE_AT`      VARCHAR(1)    NULL     COMMENT '사용 여부', -- 사용 여부
	`REGIST_DT`   DATE          NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '분석 정보';

-- 분석 정보
ALTER TABLE `ANALS`
	ADD CONSTRAINT `PK_ANALS` -- 분석 정보 기본키
		PRIMARY KEY (
			`INSTT_CODE`, -- 기관 코드
			`ANALS_ID`    -- 분석 아이디
		);
		
-- 진단대상데이터베이스정보
CREATE TABLE `DGNSS_DBMS` (
	`INSTT_CODE`    VARCHAR(20)  NOT NULL COMMENT '기관 코드', -- 기관 코드
	`DGNSS_DBMS_ID` VARCHAR(20)  NOT NULL COMMENT '진단대상데이터베이스아이디', -- 진단대상데이터베이스아이디
	`DGNSS_DBMS_NM` VARCHAR(255) NULL     COMMENT '진단대상데이터베이스명', -- 진단대상데이터베이스명
	`DBMS_ID`       VARCHAR(20)  NULL     COMMENT 'DBMS 아이디', -- DBMS 아이디
	`DBMS_VER_ID`   VARCHAR(20)  NULL     COMMENT 'DBMS 버전 아이디', -- DBMS 버전 아이디
	`IP`            VARCHAR(255) NULL     COMMENT '아이피', -- 아이피
	`PORT`          VARCHAR(255) NULL     COMMENT '포트', -- 포트
	`SCHEMA`        VARCHAR(255) NULL     COMMENT '스키마', -- 스키마
	`SID`           VARCHAR(255) NULL     COMMENT 'SID', -- SID
	`DATABASE`      VARCHAR(255) NULL     COMMENT '데이터베이스', -- 데이터베이스
	`PARAMTR`       VARCHAR(255) NULL     COMMENT '파라미터', -- 파라미터
	`ID`            VARCHAR(20)  NULL     COMMENT '아이디', -- 아이디
	`PASSWORD`      VARCHAR(255) NULL     COMMENT '비밀번호', -- 비밀번호
	`RM`            VARCHAR(255) NULL     COMMENT '비고', -- 비고
	`USE_AT`        VARCHAR(1)   NULL     COMMENT '사용 여부', -- 사용 여부
	`REGIST_DT`     DATE         NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '진단대상데이터베이스정보';

-- 진단대상데이터베이스정보
ALTER TABLE `DGNSS_DBMS`
	ADD CONSTRAINT `PK_DGNSS_DBMS` -- 진단대상데이터베이스정보 기본키
		PRIMARY KEY (
			`INSTT_CODE`,    -- 기관 코드
			`DGNSS_DBMS_ID`  -- 진단대상데이터베이스아이디
		);

-- 진단 정보
CREATE TABLE `DGNSS_TABLES` (
	`INSTT_CODE`     VARCHAR(20)  NOT NULL COMMENT '기관 코드', -- 기관 코드
	`DGNSS_DBMS_ID` VARCHAR(20)  NOT NULL COMMENT '진단대상데이터베이스아이디', -- 진단대상데이터베이스아이디
	`DGNSS_INFO_ID`  VARCHAR(20)  NOT NULL COMMENT '진단 정보 아이디', -- 진단 정보 아이디
	`DGNSS_NM`       VARCHAR(255) NULL     COMMENT '진단 명', -- 진단 명
	`TABLE_NM`       VARCHAR(255) NULL     COMMENT '테이블 명', -- 테이블 명
	`ALL_CO`         INTEGER      NULL     COMMENT '전체 건수', -- 전체 건수
	`EXC_BEGIN_TIME` DATE         NULL     COMMENT '수행 시작 시간', -- 수행 시작 시간
	`EXC_END_TIME`   DATE         NULL     COMMENT '수행 종료 시간', -- 수행 종료 시간
	`EXC_STTUS`      VARCHAR(20)  NULL     COMMENT '수행 상태', -- 수행 상태
	`REGISTER_ID`    VARCHAR(20)  NULL     COMMENT '등록자 아이디', -- 등록자 아이디
	`REGIST_DT`      DATE         NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '진단 정보';

-- 진단 정보
ALTER TABLE `DGNSS_TABLES`
	ADD CONSTRAINT `PK_DGNSS_TABLES` -- 진단 정보 기본키
		PRIMARY KEY (
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`, -- 진단대상데이터베이스아이디
			`DGNSS_INFO_ID`   -- 진단 정보 아이디
		);

-- 컬럼별 진단 정보
CREATE TABLE `DGNSS_COLUMNS` (
	`INSTT_CODE`     VARCHAR(20)  NOT NULL COMMENT '기관 코드', -- 기관 코드
	`DGNSS_DBMS_ID` VARCHAR(20)  NOT NULL COMMENT '진단대상데이터베이스아이디', -- 진단대상데이터베이스아이디
	`DGNSS_INFO_ID`  VARCHAR(20)  NOT NULL COMMENT '진단 정보 아이디', -- 진단 정보 아이디
	`COLUMN_NM`      VARCHAR(255) NOT NULL COMMENT '컬럼 명', -- 컬럼 명
	`COLUMN_TY`      VARCHAR(20)  NULL     COMMENT '컬럼 타입', -- 컬럼 타입
	`COLUMN_LT`      INTEGER      NULL     COMMENT '컬럼 길이', -- 컬럼 길이
	`TRGCNT`         INTEGER      NULL     COMMENT '대상 건수', -- 대상 건수
	`REGIST_DT`      DATE         NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '컬럼별 진단 정보';

-- 컬럼별 진단 정보
ALTER TABLE `DGNSS_COLUMNS`
	ADD CONSTRAINT `PK_DGNSS_COLUMNS` -- 컬럼별 진단 정보 기본키
		PRIMARY KEY (
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`, -- 진단대상데이터베이스아이디
			`DGNSS_INFO_ID`,  -- 진단 정보 아이디
			`COLUMN_NM`       -- 컬럼 명
		);			
			
-- 컬럼별 진단 항목별 진단 정보
CREATE TABLE `DGNSS_COLUMNS_RES` (
	`INSTT_CODE`     VARCHAR(20)  NOT NULL COMMENT '기관 코드', -- 기관 코드
	`DGNSS_DBMS_ID` VARCHAR(20)  NOT NULL COMMENT '진단대상데이터베이스아이디', -- 진단대상데이터베이스아이디
	`DGNSS_INFO_ID`  VARCHAR(20)  NOT NULL COMMENT '진단 정보 아이디', -- 진단 정보 아이디
	`COLUMN_NM`      VARCHAR(255) NOT NULL COMMENT '컬럼 명', -- 컬럼 명
	`ANALS_ID`       VARCHAR(20)  NOT NULL COMMENT '분석 아이디', -- 분석 아이디
	`MTCHG_CO`       INTEGER      NULL     COMMENT '매칭 건수', -- 매칭 건수
	`REGIST_DT`      DATE         NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '컬럼별 진단 항목별 진단 정보';

-- 컬럼별 진단 항목별 진단 정보
ALTER TABLE `DGNSS_COLUMNS_RES`
	ADD CONSTRAINT `PK_DGNSS_COLUMNS_RES` -- 컬럼별 진단 항목별 진단 정보 기본키
		PRIMARY KEY (
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`, -- 진단대상데이터베이스아이디
			`DGNSS_INFO_ID`,  -- 진단 정보 아이디
			`COLUMN_NM`,      -- 컬럼 명
			`ANALS_ID`        -- 분석 아이디
		);

-- 빈도 분석 결과
CREATE TABLE `FRQ_ANALS` (
	`INSTT_CODE`     VARCHAR(20)  NOT NULL COMMENT '기관 코드', -- 기관 코드
	`DGNSS_DBMS_ID` VARCHAR(20)  NOT NULL COMMENT '진단대상데이터베이스아이디', -- 진단대상데이터베이스아이디
	`DGNSS_INFO_ID`  VARCHAR(20)  NOT NULL COMMENT '진단 정보 아이디', -- 진단 정보 아이디
	`COLUMN_NM`      VARCHAR(255) NOT NULL COMMENT '컬럼 명', -- 컬럼 명
	`SN`             INTEGER      NOT NULL COMMENT '순번', -- 순번
	`DATA_VALUE`     VARCHAR(255) NULL     COMMENT '데이터 값', -- 데이터 값
	`DATA_CO`        INTEGER      NULL     COMMENT '데이터 건수', -- 데이터 건수
	`REGIST_DT`      DATE         NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '빈도 분석 결과';

-- 빈도 분석 결과
ALTER TABLE `FRQ_ANALS`
	ADD CONSTRAINT `PK_FRQ_ANALS` -- 빈도 분석 결과 기본키
		PRIMARY KEY (
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`, -- 진단대상데이터베이스아이디
			`DGNSS_INFO_ID`,  -- 진단 정보 아이디
			`COLUMN_NM`,      -- 컬럼 명
			`SN`              -- 순번
		);
										
-- 이용자권한
CREATE TABLE `AUTHORITY` (
	`USER_ID`     VARCHAR(50) NOT NULL COMMENT '사용자 ID', -- 사용자 ID
	`AUTHOR_CODE` VARCHAR(50) NULL     COMMENT '권한 코드' -- 권한 코드
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '이용자권한';

-- 이용자권한
ALTER TABLE `AUTHORITY`
	ADD CONSTRAINT `PK_AUTHORITY` -- 이용자권한 기본키
		PRIMARY KEY (
			`USER_ID` -- 사용자 ID
		);

-- 이용자
CREATE TABLE `USER` (
	`INSTT_CODE`       VARCHAR(20)  NOT NULL COMMENT '기관코드', -- 기관코드
	`USER_ID`          VARCHAR(50)  NOT NULL COMMENT '사용자 ID', -- 사용자 ID
	-- [삭제] `AUTHOR_CODE`      VARCHAR(20)  NOT NULL COMMENT '권한 코드', -- 권한 코드
	`USER_PASSWORD`    VARCHAR(255) NOT NULL COMMENT '사용자 비밀번호', -- 사용자 비밀번호
	`USER_NM`          VARCHAR(50)  NULL     COMMENT '사용자 이름', -- 사용자 이름
	`RECENT_CONECT_DT` DATE         NULL     COMMENT '최근접속일시', -- 최근접속일시
	`RECENT_CONECT_IP` VARCHAR(255) NULL     COMMENT '최근접속아이피', -- 최근접속아이피
	`RM`               VARCHAR(255) NULL     COMMENT '비고', -- 비고
	`USE_AT`           VARCHAR(1)   NULL     COMMENT '사용여부' -- 사용여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '이용자';

-- 이용자
ALTER TABLE `USER`
	ADD CONSTRAINT `PK_USER` -- 이용자 기본키
		PRIMARY KEY (
			`INSTT_CODE`,  -- 기관코드
			`USER_ID`,     -- 사용자 ID
			-- [삭제] `AUTHOR_CODE`,   -- 권한 코드
		);

-- 공통코드
CREATE TABLE `CMMN_CODE` (
	`GROUP_CODE`    VARCHAR(300)  NOT NULL COMMENT '그룹 코드', -- 그룹 코드
	`GROUP_CODE_NM` VARCHAR(300)  NULL     COMMENT '그룹 코드 명', -- 그룹 코드 명
	`GROUP_CODE_DC` VARCHAR(2048) NULL     COMMENT '그룹 코드 설명', -- 그룹 코드 설명
	`USE_AT`        VARCHAR(1)    NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '공통코드';

-- 공통코드
ALTER TABLE `CMMN_CODE`
	ADD CONSTRAINT `PK_CMMN_CODE` -- 공통코드 기본키
		PRIMARY KEY (
			`GROUP_CODE` -- 그룹 코드
		);

-- 메뉴관리
CREATE TABLE `MENU_MANAGE` (
	`MENU_SN`       INT(11)      NOT NULL COMMENT '메뉴 일련번호', -- 메뉴 일련번호
	`MENU_GROUP_SN` INT(11)      NOT NULL COMMENT '메뉴 그룹 일련번호', -- 메뉴 그룹 일련번호
	`MENU_DP_NO`    INT(11)      NULL     COMMENT '메뉴 깊이 넘버', -- 메뉴 깊이 넘버
	`INQIRE_ORDR`   INT(11)      NULL     COMMENT '조회 순서', -- 조회 순서
	`MENU_NM`       VARCHAR(300) NULL     COMMENT '메뉴 이름', -- 메뉴 이름
	`MENU_URL`      VARCHAR(600) NULL     COMMENT '메뉴URL', -- 메뉴URL
	`USE_AT`        VARCHAR(1)   NULL     COMMENT '사용 여부', -- 사용 여부
	`UPPER_MENU_SN` INT(11)      NULL     COMMENT '상위 메뉴 순번', -- 상위 메뉴 순번
	`MENU_ICON`     VARCHAR(100) NULL     COMMENT '메뉴 아이콘' -- 메뉴 아이콘
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '메뉴관리';

-- 메뉴관리
ALTER TABLE `MENU_MANAGE`
	ADD CONSTRAINT `PK_MENU_MANAGE` -- 메뉴관리 기본키
		PRIMARY KEY (
			`MENU_SN`,       -- 메뉴 일련번호
			`MENU_GROUP_SN`  -- 메뉴 그룹 일련번호
		);

-- 메뉴권한코드
CREATE TABLE `MENU_AUTHOR` (
	`MENU_SN`       INT(11)     NOT NULL COMMENT '메뉴 일련번호', -- 메뉴 일련번호
	`AUTHOR_CODE`   VARCHAR(20) NOT NULL COMMENT '권한 코드', -- 권한 코드
	`MENU_GROUP_SN` INT(11)     NOT NULL COMMENT '메뉴 그룹 일련번호' -- 메뉴 그룹 일련번호
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '메뉴권한코드';

-- 메뉴권한코드
ALTER TABLE `MENU_AUTHOR`
	ADD CONSTRAINT `PK_MENU_AUTHOR` -- 메뉴권한코드 기본키
		PRIMARY KEY (
			`MENU_SN`,       -- 메뉴 일련번호
			`AUTHOR_CODE`,   -- 권한 코드
			`MENU_GROUP_SN`  -- 메뉴 그룹 일련번호
		);


-- 공통코드상세
CREATE TABLE `CMMN_CODE_DETAIL` (
	`GROUP_CODE`      VARCHAR(300)  NOT NULL COMMENT '그룹 코드', -- 그룹 코드
	`CMMN_CODE`       VARCHAR(4)    NOT NULL COMMENT '공통 코드', -- 공통 코드
	`CMMN_UPPER_CODE` VARCHAR(20)   NOT NULL COMMENT '공통상위코드', -- 공통상위코드
	`CMMN_CODE_NM`    VARCHAR(50)   NULL     COMMENT '공통 코드 이름', -- 공통 코드 이름
	`CMMN_CODE_DC`    VARCHAR(2048) NULL     COMMENT '공통 코드 설명', -- 공통 코드 설명
	`USE_AT`          VARCHAR(1)    NULL     COMMENT '사용 여부', -- 사용 여부
	`INQIRE_ORDR`     INT(11)       NULL     COMMENT '조회 순서' -- 조회 순서
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '공통코드상세';

-- 공통코드상세
ALTER TABLE `CMMN_CODE_DETAIL`
	ADD CONSTRAINT `PK_CMMN_CODE_DETAIL` -- 공통코드상세 기본키
		PRIMARY KEY (
			`GROUP_CODE`,      -- 그룹 코드
			`CMMN_CODE`,       -- 공통 코드
			`CMMN_UPPER_CODE`  -- 공통상위코드
		);
		


-- 메뉴그룹관리
CREATE TABLE `MENU_GROUP` (
	`MENU_GROUP_SN` INT(11)      NOT NULL COMMENT '메뉴 그룹 일련번호', -- 메뉴 그룹 일련번호
	`MENU_GROUP_NM` VARCHAR(300) NULL     COMMENT '메뉴 그룹 명', -- 메뉴 그룹 명
	`USE_AT`        VARCHAR(1)   NULL     COMMENT '사용 여부' -- 사용 여부
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '메뉴그룹관리';

-- 메뉴그룹관리
ALTER TABLE `MENU_GROUP`
	ADD CONSTRAINT `PK_MENU_GROUP` -- 메뉴그룹관리 기본키
		PRIMARY KEY (
			`MENU_GROUP_SN` -- 메뉴 그룹 일련번호
		);



-- 기관 관리
CREATE TABLE `INSTT_MANAGE` (
	`INSTT_CODE`        VARCHAR(20)  NOT NULL, -- 기관 코드
	`INSTT_NM`          VARCHAR(255) NULL,     -- 기관 명
	`LWPRT_INSTT_NM`    VARCHAR(255) NULL,     -- 하위기관 명
	`ODR`               INT(11)      NULL,     -- 차수
	`ORD`               INT(11)      NULL,     -- 서열
	`PSTINST_ODR`       INT(11)      NULL,     -- 소속기관 차수
	`SEHIGH_INSTT_CODE` VARCHAR(20)  NULL,     -- 차상위 기관 코드
	`BEST_INSTT_CODE`   VARCHAR(20)  NULL,     -- 최상위 기관 코드
	`REPRSNT_INSTT_CODE`VARCHAR(20)  NULL,     -- 대표 기관 코드
	`TY_CL_LRGE`        INT(11)      NULL,     -- 유형분류 대
	`TY_CL_MIDDL`       INT(11)      NULL,     -- 유형분류 중
	`TY_CL_SMALL`       INT(11)      NULL,     -- 유형분류 소
	`ZIP`               INTEGER      NULL,     -- 우편번호
	`LNM`               INTEGER      NULL,     -- 지번
	`ADRES`             VARCHAR(255) NULL,     -- 주소
	`TELNO`             VARCHAR(127) NULL,     -- 전화번호
	`FXNUM`             VARCHAR(127) NULL      -- 팩스번호
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '기관 관리';

-- 기관 관리
ALTER TABLE `INSTT_MANAGE`
	ADD CONSTRAINT `PK_INSTT_MANAGE` -- 기관관리 기본키
		PRIMARY KEY (
			`INSTT_CODE` -- 기관 코드
		);

-- DBMS 버전 정보
ALTER TABLE `INSTT_DBMS_VER`
	ADD CONSTRAINT `FK_INSTT_DBMS_TO_INSTT_DBMS_VER` -- DBMS 정보 -> DBMS 버전 정보
		FOREIGN KEY (
			`DBMS_ID` -- DBMS 아이디
		)
		REFERENCES `INSTT_DBMS` ( -- DBMS 정보
			`DBMS_ID` -- DBMS 아이디
		);

-- 진단 정보
ALTER TABLE `DGNSS_TABLES`
	ADD CONSTRAINT `FK_DGNSS_DBMS_TO_DGNSS_TABLES` -- 진단대상데이터베이스정보 -> 진단 정보
		FOREIGN KEY (
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`  -- 진단대상데이터베이스아이디
		)
		REFERENCES `DGNSS_DBMS` ( -- 진단대상데이터베이스정보
			`INSTT_CODE`,    -- 기관 코드
			`DGNSS_DBMS_ID`  -- 진단대상데이터베이스아이디
		);

-- 컬럼별 진단 정보
ALTER TABLE `DGNSS_COLUMNS`
	ADD CONSTRAINT `FK_DGNSS_TABLES_TO_DGNSS_COLUMNS` -- 진단 정보 -> 컬럼별 진단 정보
		FOREIGN KEY (
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`, -- 진단대상데이터베이스아이디
			`DGNSS_INFO_ID`   -- 진단 정보 아이디
		)
		REFERENCES `DGNSS_TABLES` ( -- 진단 정보
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`, -- 진단대상데이터베이스아이디
			`DGNSS_INFO_ID`   -- 진단 정보 아이디
		);
	
-- 컬럼별 진단 항목별 진단 정보
ALTER TABLE `DGNSS_COLUMNS_RES`
	ADD CONSTRAINT `FK_DGNSS_COLUMNS_TO_DGNSS_COLUMNS_RES` -- 컬럼별 진단 정보 -> 컬럼별 진단 항목별 진단 정보
		FOREIGN KEY (
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`, -- 진단대상데이터베이스아이디
			`DGNSS_INFO_ID`,  -- 진단 정보 아이디
			`COLUMN_NM`       -- 컬럼 명
		)
		REFERENCES `DGNSS_COLUMNS` ( -- 컬럼별 진단 정보
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`, -- 진단대상데이터베이스아이디
			`DGNSS_INFO_ID`,  -- 진단 정보 아이디
			`COLUMN_NM`       -- 컬럼 명
		);

-- 빈도 분석 결과
ALTER TABLE `FRQ_ANALS`
	ADD CONSTRAINT `FK_DGNSS_COLUMNS_TO_FRQ_ANALS` -- 컬럼별 진단 정보 -> 빈도 분석 결과
		FOREIGN KEY (
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`, -- 진단대상데이터베이스아이디
			`DGNSS_INFO_ID`,  -- 진단 정보 아이디
			`COLUMN_NM`       -- 컬럼 명
		)
		REFERENCES `DGNSS_COLUMNS` ( -- 컬럼별 진단 정보
			`INSTT_CODE`,     -- 기관 코드
			`DGNSS_DBMS_ID`, -- 진단대상데이터베이스아이디
			`DGNSS_INFO_ID`,  -- 진단 정보 아이디
			`COLUMN_NM`       -- 컬럼 명
		);

-- 이용자
ALTER TABLE `USER`
	ADD CONSTRAINT `FK_AUTHORITY_TO_USER` -- 이용자권한 -> 이용자
		FOREIGN KEY (
			`USER_ID` -- 사용자 ID
		)
		REFERENCES `AUTHORITY` ( -- 이용자권한
			`USER_ID` -- 사용자 ID
		);

-- 메뉴관리
ALTER TABLE `MENU_MANAGE`
	ADD CONSTRAINT `FK_MENU_GROUP_TO_MENU_MANAGE` -- 메뉴그룹관리 -> 메뉴관리
		FOREIGN KEY (
			`MENU_GROUP_SN` -- 메뉴 그룹 일련번호
		)
		REFERENCES `MENU_GROUP` ( -- 메뉴그룹관리
			`MENU_GROUP_SN` -- 메뉴 그룹 일련번호
		);
		
-- 메뉴권한코드
ALTER TABLE `MENU_AUTHOR`
	ADD CONSTRAINT `FK_MENU_MANAGE_TO_MENU_AUTHOR` -- 메뉴관리 -> 메뉴권한코드
		FOREIGN KEY (
			`MENU_SN`,       -- 메뉴 일련번호
			`MENU_GROUP_SN`  -- 메뉴 그룹 일련번호
		)
		REFERENCES `MENU_MANAGE` ( -- 메뉴관리
			`MENU_SN`,       -- 메뉴 일련번호
			`MENU_GROUP_SN`  -- 메뉴 그룹 일련번호
		);

-- 공통코드상세
ALTER TABLE `CMMN_CODE_DETAIL`
	ADD CONSTRAINT `FK_CMMN_CODE_TO_CMMN_CODE_DETAIL` -- 공통코드 -> 공통코드상세
		FOREIGN KEY (
			`GROUP_CODE` -- 그룹 코드
		)
		REFERENCES `CMMN_CODE` ( -- 공통코드
			`GROUP_CODE` -- 그룹 코드
		);

-- 진단대상데이터베이스정보
ALTER TABLE `DGNSS_DBMS`
	ADD CONSTRAINT `FK_INSTT_DBMS_VER_TO_DGNSS_DBMS` -- DBMS 버전 정보 -> 진단대상데이터베이스정보
		FOREIGN KEY (
			`DBMS_ID`,     -- DBMS 아이디
			`DBMS_VER_ID`  -- DBMS 버전 아이디
		)
		REFERENCES `INSTT_DBMS_VER` ( -- DBMS 버전 정보
			`DBMS_ID`,     -- DBMS 아이디
			`DBMS_VER_ID`  -- DBMS 버전 아이디
		);

-- 진단대상데이터베이스정보
ALTER TABLE `DGNSS_DBMS`
	ADD CONSTRAINT `FK_INSTT_TO_DGNSS_DBMS` -- 기관 정보 -> 진단대상데이터베이스정보
		FOREIGN KEY (
			`INSTT_CODE` -- 기관 코드
		)
		REFERENCES `INSTT` ( -- 기관 정보
			`INSTT_CODE` -- 기관 코드
		);

-- 컬럼별 진단 항목별 진단 정보
ALTER TABLE `DGNSS_COLUMNS_RES`
	ADD CONSTRAINT `FK_ANALS_TO_DGNSS_COLUMNS_RES` -- 분석 정보 -> 컬럼별 진단 항목별 진단 정보
		FOREIGN KEY (
			`INSTT_CODE`, -- 기관 코드
			`ANALS_ID`    -- 분석 아이디
		)
		REFERENCES `ANALS` ( -- 분석 정보
			`INSTT_CODE`, -- 기관 코드
			`ANALS_ID`    -- 분석 아이디
		);

