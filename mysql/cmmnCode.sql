-- --------------------------------------------------------
-- 호스트:                          
-- 서버 버전:                      
-- 서버 OS:                        
-- HeidiSQL 버전:                  
-- --------------------------------------------------------

-- DQ 테이블
--1.유저 테이블
CREATE TABLE `user` (
    `username` VARCHAR(20) NULL DEFAULT NULL,
    `password` VARCHAR(500) NULL DEFAULT NULL,
    `name` VARCHAR(20) NULL DEFAULT NULL,
    `isAccountNonExpired` TINYINT(1) NULL DEFAULT NULL,
    `isAccountNonLocked` TINYINT(1) NULL DEFAULT NULL,
    `isCredentialsNonExpired` TINYINT(1) NULL DEFAULT NULL,
    `isEnabled` TINYINT(1) NULL DEFAULT NULL
)
COLLATE=`utf8_general_ci`
ENGINE=InnoDB
;

--2.권한 테이블
CREATE TABLE `authority` (
    `username` VARCHAR(20) NULL DEFAULT NULL,
    `authority_name` VARCHAR(20) NULL DEFAULT NULL
)
COLLATE=`utf8_general_ci`
ENGINE=InnoDB
;

INSERT INTO `authority` (`username`, `authority_name`) VALUES
    ('admin', 'ADMIN'),
    ('user', 'USER');

-- 패스워드 :  abcd
INSERT INTO `user` (`username`, `password`, `name`, `isAccountNonExpired`, `isAccountNonLocked`, `isCredentialsNonExpired`, `isEnabled`) VALUES
    ('user', '$2a$10$zNM1N.WnfC1Sq.vkqieCnuEfE3sZ3Hwo6.ytaSBtFTyg33qr2oI2G', '사용자', 1, 1, 1, 1);

-- 패스워드 :  abcd
INSERT INTO `user` (`username`, `password`, `name`, `isAccountNonExpired`, `isAccountNonLocked`, `isCredentialsNonExpired`, `isEnabled`) VALUES
	('admin', '$2a$10$zNM1N.WnfC1Sq.vkqieCnuEfE3sZ3Hwo6.ytaSBtFTyg33qr2oI2G', '관리자', 1, 1, 1, 1); 
	
--3.메뉴 테이블
CREATE TABLE `menu_manage` (
    `menu_sn` INT PRIMARY KEY DEFAULT 0,
    `menu_dp_no` INT DEFAULT NULL,
    `inqire_ordr` INT DEFAULT NULL,
    `menu_nm` VARCHAR(300) DEFAULT NULL,
    `menu_url` VARCHAR(600) DEFAULT NULL,
    `use_at` VARCHAR(1) DEFAULT NULL,
    `regist_dt` DATETIME DEFAULT NULL,
    `regist_user_id` VARCHAR(100) DEFAULT NULL,
    `updt_dt` DATETIME DEFAULT NULL,
    `updt_user_id` VARCHAR(100) DEFAULT NULL,
    `upper_menu_sn` INT DEFAULT NULL
)
COLLATE=`utf8_general_ci`
ENGINE=InnoDB
;

ALTER TABLE `menu_manage` 
ADD COLUMN `menu_group_sn` INT(11) NOT NULL AFTER `upper_menu_sn`;


INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (1, 1, 1, 'MAIN PAGE', '/mngr/main', 'Y', NOW(), 'user', NOW(), 'user', 0, 0);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (2, 1, 1, '공통정보', '#', 'Y', NOW(), 'user', NOW(), 'user', 0, 1);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (3, 1, 1, '사용자정보', '#', 'Y', NOW(), 'user', NOW(), 'user', 0, 1);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (4, 1, 1, '운영정보', '#', 'Y', NOW(), 'user', NOW(), 'user', 0, 1);

INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (5, 1, 1, '기본정보', '#', 'Y', NOW(), 'user', NOW(), 'user', 0, 2);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (6, 1, 1, '데이터연결', '#', 'Y', NOW(), 'user', NOW(), 'user', 0, 2);

INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (7, 1, 1, '업무규칙관리', '#', 'Y', NOW(), 'user', NOW(), 'user', 0, 3);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (8, 2, 1, '허용 값 범위 설정', '#', 'Y', NOW(), 'user', NOW(), 'user', 7, 3);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (9, 2, 1, '사용자정의(정규식)', '#', 'Y', NOW(), 'user', NOW(), 'user', 7, 3);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (10, 2, 1, '사용자정의(SQL)', '#', 'Y', NOW(), 'user', NOW(), 'user', 7, 3);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (11, 1, 1, '패턴/지표 관리', '#', 'Y', NOW(), 'user', NOW(), 'user', 0, 3);

INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (12, 1, 1, '스키마 구조 분석', '#', 'Y', NOW(), 'user', NOW(), 'user', 0, 4);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (13, 2, 1, '테이블 구조 분석', '#', 'Y', NOW(), 'user', NOW(), 'user', 12, 4);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (14, 2, 1, '컬럼 구조 분석', '#', 'Y', NOW(), 'user', NOW(), 'user', 12, 4);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (15, 1, 1, '프로파일링 분석', '#', 'Y', NOW(), 'user', NOW(), 'user', 0, 4);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (16, 1, 1, '프로파일링 결과', '#', 'Y', NOW(), 'user', NOW(), 'user', 0, 4);

INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (17, 2, 1, '메뉴관리', '/mngr/menuMng/menuMange', 'Y', NOW(), 'admin', NOW(), 'user', 0, 4);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (18, 2, 2, '메뉴권한', '/mngr/menuMng/menuAuthor', 'Y', NOW(), 'admin', NOW(), 'user', 0, 4);
INSERT INTO menu_manage (menu_sn, menu_dp_no, inqire_ordr, menu_nm, menu_url, use_at, regist_dt, regist_user_id, updt_dt, updt_user_id, upper_menu_sn, menu_group_sn) VALUES (19, 2, 3, '공통코드관리', '/mngr/menuMng/menuCode', 'Y', NOW(), 'admin', NOW(), 'user', 0, 4);


CREATE TABLE `menu_author` (
    `menu_sn` INT DEFAULT 0,
    `author_code` VARCHAR(100) NULL DEFAULT NULL,
    `regist_dt` DATETIME NULL DEFAULT NULL,
    `regist_user_id` VARCHAR(100) NULL DEFAULT NULL,
    PRIMARY KEY(`menu_sn`, `author_code`)
)
COLLATE=`utf8_general_ci`
ENGINE=InnoDB
;

INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (2, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (3, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (4, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (5, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (5, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (6, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (6, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (7, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (7, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (8, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (8, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (9, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (9, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (10, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (10, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (11, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (11, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (12, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (12, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (13, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (13, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (14, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (14, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (15, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (15, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (16, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (16, 'ROLE_USER', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (17, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (18, 'ROLE_ADMIN', now(), 'admin');
INSERT INTO menu_author (menu_sn, author_code, regist_dt, regist_user_id) VALUES (19, 'ROLE_ADMIN', now(), 'admin');


CREATE TABLE `menu_group` (
    `menu_group_sn` INT PRIMARY KEY DEFAULT 0,
    `menu_group_nm` VARCHAR(300) DEFAULT NULL,
	`use_at` VARCHAR(300) DEFAULT NULL
)
COLLATE=`utf8_general_ci`
ENGINE=InnoDB
;

INSERT INTO menu_group (menu_group_sn, menu_group_nm, use_at) VALUES (1, 'PROPERTIES', 'Y');
INSERT INTO menu_group (menu_group_sn, menu_group_nm, use_at) VALUES (2, 'BASIC', 'Y');
INSERT INTO menu_group (menu_group_sn, menu_group_nm, use_at) VALUES (3, 'RULE', 'Y');
INSERT INTO menu_group (menu_group_sn, menu_group_nm, use_at) VALUES (4, 'PROFILING', 'Y');


--4.공통코드 테이블
CREATE TABLE 
dq_cmmn_code(
	GROUP_CODE VARCHAR(100) NOT NULL ,	
	GROUP_CODE_NM VARCHAR(100),
	GROUP_CODE_DC VARCHAR(2000),		
	USE_AT CHAR(1),    
	PRIMARY KEY(GROUP_CODE)
)	
	COLLATE=`utf8_general_ci`	
	ENGINE=InnoDB;
	
INSERT INTO dq_cmmn_code(GROUP_CODE ,GROUP_CODE_NM,USE_AT) VALUES('CC_CODE_TEST','테스트','Y');
INSERT INTO dq_cmmn_code(GROUP_CODE ,GROUP_CODE_NM,USE_AT) VALUES('CODE_TEST','테스트','Y');


CREATE TABLE dq_cmmn_code_detail(	
	GROUP_CODE VARCHAR(100) NOT NULL,	
	CMMN_CODE VARCHAR(4) NOT NULL,	
	CMMN_CODE_NM VARCHAR(100), 	
	CMMN_CODE_DC VARCHAR(100),	
	CMMN_UPPER_CODE VARCHAR(100) NOT NULL,	
	USE_AT CHAR(1),	
	INQIRE_ORDR INTEGER,
	PRIMARY KEY(GROUP_CODE,CMMN_CODE,CMMN_UPPER_CODE)
)	
	COLLATE=`utf8_general_ci`
	ENGINE=InnoDB;	
	
INSERT INTO dq_cmmn_code_detail(GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR) VALUES('CC_CODE_TEST','col1','테스트','테스트','1','Y','0');
INSERT INTO dq_cmmn_code_detail(GROUP_CODE, CMMN_CODE, CMMN_CODE_NM, CMMN_CODE_DC, CMMN_UPPER_CODE, USE_AT, INQIRE_ORDR) VALUES('CC_CODE_TEST','col2','테스트','테스트','1','Y','0');


--5.초기 고객사 정보 테이블
CREATE TABLE `dq_meta_admnst_agency` (
  `AGENCY_CODE` varchar(20) NOT NULL,
  `AGENCY_NAME` varchar(300) DEFAULT NULL,
  `LOWEST_AGENCY_NAME` varchar(300) DEFAULT NULL,
  `ODR` int(11) DEFAULT NULL,
  `ORD` int(11) DEFAULT NULL,
  `PSTINST_ODR` int(11) DEFAULT NULL,
  `SEHIGH_AGENCY_CODE` varchar(20) DEFAULT NULL,
  `BEST_AGENCY_CODE` varchar(20) DEFAULT NULL,
  `REPRSNT_AGENCY_CODE` varchar(20) DEFAULT NULL,
  `TYCL_LARGE` int(11) DEFAULT NULL,
  `TYCL_MEDIUM` int(11) DEFAULT NULL,
  `TYCL_SMALL` int(11) DEFAULT NULL,
  `ZIP1` int(11) DEFAULT NULL,
  `ZIP2` varchar(50) DEFAULT NULL,
  `ADRES1` varchar(500) DEFAULT NULL,
  `TELNO` varchar(50) DEFAULT NULL,
  `FAX_NO` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`AGENCY_CODE`)
) 
COLLATE=`utf8_general_ci`
ENGINE=InnoDB;


-- 5. 기초데이터 
INSERT INTO dq_meta_admnst_agency (
AGENCY_CODE, AGENCY_NAME, LOWEST_AGENCY_NAME, ODR, ORD,
PSTINST_ODR, SEHIGH_AGENCY_CODE, BEST_AGENCY_CODE, REPRSNT_AGENCY_CODE, TYCL_LARGE,
TYCL_MEDIUM, TYCL_SMALL, ZIP1, ZIP2, ADRES1,
TELNO,  FAX_NO
)
VALUES 
(
'1075150', '부마민주항쟁진상규명및관련자명예회복심의위원회', '부마민주항쟁진상규명및관련자명예회복심의위원회', '1', '79',
'0', '0', '1075150', '1075150', '1',
'1', '7', '3044', '350034', '코오롱빌딩 3층',
'02-6744-3116', '02-6744-3199'
);






--6.초기 고객사 저장 테이블
create table ADMNST_AGENCY_DATA
(
AGENCY_CODE varchar(20),
AGENCY_NAME varchar(300),
LOWEST_AGENCY_NAME varchar(300),
USERID varchar(100)
)
COLLATE=`utf8_general_ci`
ENGINE=InnoDB;









	
	