CREATE TABLE DGNSS_SAVE (
	INSTT_CODE    CHAR(7)     NOT NULL COMMENT '기관 코드', -- 기관 코드
	DGNSS_SAVE_ID VARCHAR(20) NOT NULL COMMENT '분석설정저장아이디', -- 분석설정저장아이디
	DGNSS_DBMS_ID CHAR(4)     NOT NULL COMMENT '진단대상데이터베이순버', -- 진단대상데이터베이순버
	DGNSS_SAVE_NM VARCHAR(20) NULL     COMMENT '분석설정저장명', -- 분석설정저장명
	TABLE_NM      VARCHAR(80) NULL     COMMENT '테이블 명', -- 테이블 명
	COLUMN_NM_DC  TEXT        NULL     COMMENT '컬럼명 설명', -- 컬럼명 설명
	ANALS_ID_DC   TEXT        NULL     COMMENT '분석 아이디 설명', -- 분석 아이디 설명
	REGIST_DT     DATETIME    NULL     COMMENT '등록일시', -- 등록일시
	SCHEMA_NM     VARCHAR(50) NULL     COMMENT '데이터베이스 명', -- 데이터베이스 명
	PRIMARY KEY (`INSTT_CODE`, `DGNSS_SAVE_ID`)
)
COMMENT='분석설정저장'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;