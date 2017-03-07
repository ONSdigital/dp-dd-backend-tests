package com.ons.gov.uk.old_tests_do_not_run;

import com.ons.gov.uk.CSVOps;
import com.ons.gov.uk.DBLoader;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.core.Config;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertTrue;



public class BackendTest {
	Config config = new Config();
	CSVOps csvOps = new CSVOps();
	DBLoader dbLoader = new DBLoader();
	DimensionalAPI ep = new DimensionalAPI();
	String dimDataPoint = "dimensional_data_point";
	String dimDataSet = "dimensional_data_set";
	String dimConceptSys= "dimensional_data_set_concept_system";

	int rowsInCSV = 0;
	String tables[] = {dimDataPoint, dimConceptSys,dimDataSet};
	@BeforeTest
	public void initTest() {
// Delete the data in the DB, get the rows in the CSV and ensure the api has nothing to display
		dbLoader.connectToDB();
		dbLoader.deleteData(tables);
		int numOfRows = dbLoader.rowsInTheTable(dimDataPoint);
		assertTrue(numOfRows==0,"All rows are not deleted. Number of rows: " +numOfRows);
		rowsInCSV = csvOps.returnRows(config.getFilepath());
		System.out.println("Number of rows in CSV "+rowsInCSV);
		ep.checkEndPoint();
		assertTrue(ep.dataSetTotal() == 0,
				"There are some dataset still in the API in the DataBase " + ep.returnJson());

	}



	//Start the CSV Chopper- Feed the file from localconfig
	// Have the file under /resources/csvs
	@Test(groups = "chopper")
	public void startChopper(){
		// assertCSVChopper(chopper.startChopper());
	}

	// Wait for the dataset to load. Checks for the rows until the update has stopped
	@Test(groups = "dbtest", dependsOnGroups = "chopper")
	public void compareDBRows() throws Exception{
		dbLoader.waitForDBUpload(dimDataPoint);
		int numOfRows = dbLoader.rowsInTheTable(dimDataPoint);
		assertTrue(numOfRows==rowsInCSV,"Number of rows in CSV : " +rowsInCSV+"\n " +
				"Number of rows in Database : "+numOfRows);
	}

	// Checks for the api for the file.
	// Will add more tests as the API grows
	@Test(groups = "apitest", dependsOnGroups = "dbtest")
	public void apiTest(){
	// check for the csv file in the dataset
		boolean fileExists = false;
		ep.checkEndPoint();
		try {
			Assert.assertTrue(ep.titleExists(config.getFilepath()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void assertCSVChopper(HttpResponse chopperResp){
		try {
			String jsonResponse = EntityUtils.toString(chopperResp.getEntity());
			assertTrue(jsonResponse.contains("Your request is being processed."),
					"Expected Response: Your request is being processed.\n Actual Response: "
					+ jsonResponse);
			assertTrue(chopperResp.getStatusLine().getStatusCode() == 200,
					"Status code: " + chopperResp.getStatusLine().getStatusCode());
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}

	}



}
