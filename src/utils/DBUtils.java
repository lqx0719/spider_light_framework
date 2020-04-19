package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBUtils {
	private static String DBURL = "jdbc:mysql://127.0.0.1:3306/spider?useUnicode=true&characterEncoding=UTF-8";
	private static String username = "root";
	private static String pwd = "lqx19960719";
	private static String dbName = "com.mysql.jdbc.Driver";

	public static Connection getConnection(){
		try {
			Class.forName(dbName);
			Connection con = DriverManager.getConnection(DBURL,username,pwd);
			System.out.println("数据库  "+dbName+"  连接成功！！");
			return con;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return null;
	}

	public static void closeConnection(Connection connection){
		try {
			connection.close();
			System.out.println("数据库  "+dbName+"  断开连接！！");
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}


	public static int executeSQL(Connection connection, List<String> sqlList){
		try {
			Statement statement = connection.createStatement();
			for(String sql:sqlList){
				statement.addBatch(sql);
			}
			statement.executeBatch();
			return sqlList.size();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return 0;
	}

//	public staic int delete(Connection connection,String sql){
//
//	}
//	public static boolean createTable(Connection connection,String tableName){
//		String sql = "DROP TABLE IF EXISTS "+tableName;
//
//		Statement statement = ;
//		return
//	}



	public static void main(String[] args){
		Connection con = DBUtils.getConnection();
//		String sql = "insert into jiuyi_spider(jiuyi_keyword,jiuyi_job,jiuyi_company,jiuyi_place,jiuyi_salary) " +
//				"values('会计','实习会计','赣州市霖润佳企业管理咨询有限公司','章贡区','4000元')";
//		System.out.println(DBUtils.executeSQL(con,sql));
		DBUtils.closeConnection(con);
	}
}
