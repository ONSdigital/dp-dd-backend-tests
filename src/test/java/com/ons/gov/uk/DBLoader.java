package com.ons.gov.uk;

import com.ons.gov.uk.core.Config;

import java.sql.*;
import java.util.Properties;

public class DBLoader {
	// call the postgres and drop the dimensional_data_point table
	Config config = new Config();
	Connection conn;

	public void connectToDB(){
		String url = config.getPostgres();
		Properties props = new Properties();
		props.setProperty("user","data_discovery");
		props.setProperty("password","password");

		try {
			conn = DriverManager.getConnection(url, props);
		}
		catch (SQLException sqlException){
			sqlException.printStackTrace();
		}
	}



	public void deleteData(String... tableNames){
		try {
			Statement stmt = conn.createStatement();
			for(String table:tableNames) {
				String stmtToExecute = "DELETE FROM " + table;
				final int i = stmt.executeUpdate(stmtToExecute);
			}
			stmt.close();

		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}

	}
	public int rowsInTheTable(String tableName){
		int numberOfRows = 0;
		String stmtToExecute = "SELECT COUNT(*) FROM "+tableName;
		Statement stmt = null;
		ResultSet resultSet = null;
		try{
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(stmtToExecute);
			while(resultSet.next()){
				numberOfRows = Integer.parseInt(resultSet.getString(1));
			}
			stmt.close();


		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}

		return numberOfRows;
	}

	public void waitForDBUpload(String tableName){
		int rows = rowsInTheTable(tableName);
		try {
			Thread.sleep(4000);
			while(rows<rowsInTheTable(tableName)){
				rows= rowsInTheTable(tableName);
				Thread.sleep(4000);
			}
			System.out.println("****************DB Upload Finished**********");
		}
		catch (InterruptedException interruptException){
			interruptException.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DBLoader db = new DBLoader();
		db.connectToDB();
//		db.deleteData("dimensional_data_point");
		db.rowsInTheTable("dimensional_data_point");
	}
}
