-- --------------------------------------------------------
-- 호스트:                          
-- 서버 버전:                      
-- 서버 OS:                        
-- HeidiSQL 버전:                  
-- --------------------------------------------------------

-- DQ 테이블
--1.라이프사이클 분석 정보 테이블
CREATE TABLE `lifecycle_anals` (
  `INSTT_CODE` char(7) NOT NULL COMMENT '기관 코드',
  `ANALS_ID` char(4) NOT NULL COMMENT '분석 순번',
  `ANALS_SE` varchar(20) DEFAULT NULL COMMENT '분석 구분',
  `ANALS_TY` varchar(20) DEFAULT NULL COMMENT '분석 타입',
  `PTTRN_SE` varchar(20) DEFAULT NULL COMMENT '패턴 구분',
  `FIELD_NM` varchar(20) DEFAULT NULL COMMENT '항목명',
  `PERIOD_CL` varchar(5) DEFAULT NULL COMMENT '기간 구분',
  `PERIOD` int(4) DEFAULT NULL COMMENT '기간',
  `RM` text COMMENT '비고',
  `USE_AT` char(1) DEFAULT NULL COMMENT '사용 여부',
  `REGIST_DT` datetime DEFAULT NULL COMMENT '등록 일시',
  PRIMARY KEY (`INSTT_CODE`,`ANALS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='라이프사이클 분석 정보'

--2.라이프사이클  진단 정보 테이블
CREATE TABLE `lifecycle_tables` (
  `DGNSS_DBMS_ID` char(4) NOT NULL COMMENT '진단대상데이터베이스순번',
  `INSTT_CODE` char(7) NOT NULL COMMENT '기관 코드',
  `DGNSS_INFO_ID` varchar(20) NOT NULL COMMENT '진단 정보 아이디',
  `DGNSS_NM` varchar(80) DEFAULT NULL COMMENT '진단 명',
  `SCHEMA_NM` varchar(80) DEFAULT NULL COMMENT '스키마 명',
  `ALL_CO` int(11) DEFAULT NULL COMMENT '테이블 수',
  `EXC_BEGIN_TIME` datetime DEFAULT NULL COMMENT '수행 시작 시간',
  `EXC_END_TIME` datetime DEFAULT NULL COMMENT '수행 종료 시간',
  `EXC_STTUS` char(1) DEFAULT NULL COMMENT '수행 상태',
  `REGIST_DT` datetime DEFAULT NULL COMMENT '등록 일시',
  PRIMARY KEY (`DGNSS_DBMS_ID`,`INSTT_CODE`,`DGNSS_INFO_ID`),
  KEY `FK_USER_TO_LIFECYCLE_TABLES` (`INSTT_CODE`),
  CONSTRAINT `FK_DGNSS_DBMS_TO_LIFECYCLE_TABLES` FOREIGN KEY (`DGNSS_DBMS_ID`, `INSTT_CODE`) REFERENCES `dgnss_dbms` (`DGNSS_DBMS_ID`, `INSTT_CODE`),
  CONSTRAINT `FK_USER_TO_LIFECYCLE_TABLES` FOREIGN KEY (`INSTT_CODE`) REFERENCES `user` (`INSTT_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='라이프사이클  진단 정보'
	
--3.테이블별 진단 항목별 진단 정보 테이블
CREATE TABLE `lifecycle_tables_res` (
  `INSTT_CODE` char(7) NOT NULL COMMENT '기관 코드',
  `DGNSS_DBMS_ID` char(4) NOT NULL COMMENT '진단대상데이터베이스순번',
  `DGNSS_INFO_ID` varchar(20) NOT NULL COMMENT '진단 정보 아이디',
  `TABLE_NM` varchar(80) NOT NULL COMMENT '테이블 명',
  `ANALS_ID` char(4) NOT NULL COMMENT '분석 순번',
  `coulumn_nm` varchar(80) DEFAULT NULL COMMENT '컬럼명',
  `total_co` int(11) DEFAULT NULL COMMENT '전체 건수',
  `MTCHG_CO` int(11) DEFAULT NULL COMMENT '매칭 건수',
  `MISS_CO` int(11) DEFAULT NULL COMMENT '초과 건수',
  `REGIST_DT` datetime DEFAULT NULL COMMENT '등록 일시',
  PRIMARY KEY (`INSTT_CODE`,`DGNSS_DBMS_ID`,`DGNSS_INFO_ID`,`TABLE_NM`,`ANALS_ID`),
  KEY `FK_LIFECYCLE_ANALS_TO_LIFECYCLE_TABLES_RES` (`INSTT_CODE`,`ANALS_ID`),
  CONSTRAINT `FK_LIFECYCLE_ANALS_TO_LIFECYCLE_TABLES_RES` FOREIGN KEY (`INSTT_CODE`, `ANALS_ID`) REFERENCES `lifecycle_anals` (`INSTT_CODE`, `ANALS_ID`),
  CONSTRAINT `FK_LIFECYCLE_TABLES_TO_LIFECYCLE_TABLES_RES` FOREIGN KEY (`INSTT_CODE`, `DGNSS_DBMS_ID`, `DGNSS_INFO_ID`) REFERENCES `lifecycle_tables` (`INSTT_CODE`, `DGNSS_DBMS_ID`, `DGNSS_INFO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='테이블별 진단 항목별 진단 정보'

--4.라이프사이클 오류 정보 테이블
CREATE TABLE `lifecycle_tables_error` (
  `INSTT_CODE` char(7) NOT NULL COMMENT '기관 코드',
  `DGNSS_DBMS_ID` char(4) NOT NULL COMMENT '진단대상데이터베이스순번',
  `DGNSS_INFO_ID` varchar(20) NOT NULL COMMENT '진단 정보 아이디',
  `COLUMN_NM` varchar(80) DEFAULT NULL COMMENT '컬럼 명',
  `ANALS_ID` char(4) DEFAULT NULL COMMENT '분석 아이디',
  `DGNSS_ERROR_ID` varchar(20) NOT NULL COMMENT '진단오류 아이디',
  `SCHEMA_NM` varchar(50) DEFAULT NULL COMMENT '스키마 명',
  `TABLE_NM` varchar(80) DEFAULT NULL COMMENT '테이블 명',
  `ERROR_NM` text COMMENT '오류 명',
  `ERROR_CONTENT` text COMMENT '오류 내용',
  `REGIST_DT` datetime DEFAULT NULL COMMENT '등록 일시',
  PRIMARY KEY (`DGNSS_ERROR_ID`),
  KEY `FK_LIFECYCLE_TABLES_TO_LIFECYCLE_TABLES_ERROR` (`INSTT_CODE`,`DGNSS_DBMS_ID`,`DGNSS_INFO_ID`),
  CONSTRAINT `FK_LIFECYCLE_TABLES_TO_LIFECYCLE_TABLES_ERROR` FOREIGN KEY (`INSTT_CODE`, `DGNSS_DBMS_ID`, `DGNSS_INFO_ID`) REFERENCES `lifecycle_tables` (`INSTT_CODE`, `DGNSS_DBMS_ID`, `DGNSS_INFO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='라이프사이클 오류 정보'


5.라이프사이클 메뉴 url정보
데이터 Lifecycle 관리 |-  Lifecycle 항목 관리 -> /mngr/lifecycle/lifecycleManageList.do
                           |-  Lifecycle 분석 -> /mngr/lifecycle/lifecycleAnalysis.do
                           |-  Lifecycle 분석 결과 -> /mngr/lifecycle/lifecycleResultList.do