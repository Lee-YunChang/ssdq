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
	REGIST_DT      DATETIME    NULL     COMMENT '등록 일시', -- 등록 일시
	PRIMARY KEY (`INSTT_CODE`, `DGNSS_INFO_ID`, `DGNSS_ERROR_ID`)
)
COMMENT='분석오류'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;