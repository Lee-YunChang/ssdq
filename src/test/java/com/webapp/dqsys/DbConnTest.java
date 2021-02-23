package com.webapp.dqsys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

class DbConnTest {

	@Test
	void testConnect() {
		System.out.println("[DB CONNECT TEST START]\n");
		//System.out.println("[1. ORACLE DB CONNECT TEST START]\n");
		//testOracleDbConnection();
		//System.out.println("[2. MYSQL DB CONNECT TEST START]\n");
		//testMySqlDbConnection();
		//System.out.println("[3. TIBERO DB CONNECT TEST START]\n");
		//testTiberoDbConnection();
		//System.out.println("[4. MSSQL DB CONNECT TEST START]\n");
		//testMsSqlDbConnection();
//		System.out.println("[5. MARIA DB CONNECT TEST START]\n");
//		testMariaDbConnection();
	}

	void testOracleDbConnection() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String driverName = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:Oracle:thin:@125.7.207.6:1522:ORCL";
		String user = "dqsys";
		String password = "sangs#1234";

		try {
			System.out.println("[연결 시작]\n");
			// ① 로드
			Class.forName(driverName);

			// ② 연결
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("[연결 성공]\n");
		} catch (ClassNotFoundException e) {
			System.out.println("[로드 오류]\n" + e.getStackTrace());
		} catch (SQLException e) {
			System.out.println("[연결 오류]\n" + e.getStackTrace());
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}

				if (statement != null) {
					statement.close();
				}

				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				System.out.println("[닫기 오류]\n" + e.getStackTrace());
			}
			System.out.println("[연결 종료]\n");
		}

	}

	void testMySqlDbConnection() {
		Connection connection = null;
		Statement st = null;
		String driverName = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://125.7.207.6:1527/dqsys";
		String user = "dqsys";
		String password = "dqsys#1234";

		try {
			System.out.println("[연결 시작]\n");
			// ① 로드
			Class.forName(driverName);
			// ② 연결
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("[연결 성공]\n");

		} catch (SQLException se1) {
			se1.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException se2) {
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			System.out.println("[연결 종료]\n");
		}
	}
	
	void testTiberoDbConnection() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String driverName = "com.tmax.tibero.jdbc.TbDriver";
		String url = "jdbc:tibero:thin:@125.7.207.6:8630:tibero";
		String user = "sys";
		String password = "sangs#1234";

		try {
			System.out.println("[연결 시작]\n");
			// ① 로드
			Class.forName(driverName);

			// ② 연결
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("[연결 성공]\n");
		} catch (ClassNotFoundException e) {
			System.out.println("[로드 오류]\n" + e.getStackTrace());
		} catch (SQLException e) {
			System.out.println("[연결 오류]\n" + e.getStackTrace());
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}

				if (statement != null) {
					statement.close();
				}

				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				System.out.println("[닫기 오류]\n" + e.getStackTrace());
			}
			System.out.println("[연결 종료]\n");
		}

	}
	
	void testMsSqlDbConnection() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url = "jdbc:sqlserver://125.7.207.6:1433;databaseName=master";
		String user = "sa";
		String password = "sangs#1234";

		try {
			System.out.println("[연결 시작]\n");
			// ① 로드
			Class.forName(driverName);

			// ② 연결
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("[연결 성공]\n");
		} catch (ClassNotFoundException e) {
			System.out.println("[로드 오류]\n" + e.getMessage() );
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("[연결 오류]\n" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}

				if (statement != null) {
					statement.close();
				}

				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				System.out.println("[닫기 오류]\n" + e.getStackTrace());
			}
			System.out.println("[연결 종료]\n");
		}

	}

	void testMariaDbConnection() {
		Connection connection = null;
		Statement st = null;
		String driverName = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://localhost:3308/dq_database";
		String user = "root";
		String password = "";
		
		try {
			System.out.println("[연결 시작]\n");
			// ① 로드
			Class.forName(driverName);
			// ② 연결
			connection = DriverManager.getConnection(url, user, password);
			
			System.out.println("[연결 성공]\n");

		} catch (SQLException se1) {
			se1.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException se2) {
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			System.out.println("[연결 종료]\n");
		}
	}
}
