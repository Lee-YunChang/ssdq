-- 도메인
CREATE TABLE DOMAIN (
	DOMAIN_ID CHAR(4)      NOT NULL COMMENT '도메인 순번', -- 도메인 순번
	DOMAIN_CL VARCHAR(40)  NOT NULL COMMENT '도메인 분류', -- 도메인 분류
	DOMAIN_NM VARCHAR(40)  NOT NULL COMMENT '도메인명', -- 도메인명
	DOMAIN_TY VARCHAR(20)  NOT NULL COMMENT '도메인 타입', -- 도메인 타입
	DOMAIN_LT CHAR(4)      NULL     COMMENT '도메인 길이', -- 도메인 길이
	RM        TEXT         NULL     COMMENT '비고', -- 비고
	USE_AT    BIT          NULL     COMMENT '사용 여부', -- 사용 여부
	REGIST_DT DATETIME     NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '도메인';

-- 도메인
ALTER TABLE `DOMAIN`
	ADD CONSTRAINT PK_DOMAIN -- 도메인 기본키
		PRIMARY KEY (
			DOMAIN_ID     -- 도메인 순번
		);
		
-- 표준 용어
CREATE TABLE WORD (
	WORD_ID     CHAR(4)      NOT NULL COMMENT '표준용어 순번', -- 표준 용어 순번
	WORD_NM     VARCHAR(40)  NOT NULL COMMENT '표준용어명', -- 표준용어명
	WORD_ENG_NM VARCHAR(80)  NULL     COMMENT '표준용어 영문명', -- 표준용어 영문명
	WORD_ENG_AB VARCHAR(40)  NULL     COMMENT '표준용어 영문약어', -- 표준용어 영문약어
	WORD_CL     CHAR(1)      NOT NULL COMMENT '표준용어 구분', -- 표준용어 구분
	RM          TEXT         NULL     COMMENT '비고', -- 비고
	USE_AT      BIT          NULL     COMMENT '사용 여부', -- 사용 여부
	REGIST_DT   DATETIME     NULL     COMMENT '등록 일시' -- 등록 일시
)
collate = `utf8_general_ci` ENGINE = InnoDB 
COMMENT '표준 용어';

-- 표준 용어
ALTER TABLE `WORD`
	ADD CONSTRAINT PK_WORD -- 표준 용어 기본키
		PRIMARY KEY (
			WORD_ID     -- 표준 용어 순번
		);
		