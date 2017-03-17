package com.ons.gov.uk.backend.test;


import org.testng.annotations.Test;

public class TestSuites extends TestSetup {

	APIIntegrityTest apiIntegrityTest = new APIIntegrityTest();
	APITest apiTest;
	CSVFilterTest csvFilterTest = new CSVFilterTest();

	public TestSuites() throws Exception {
		apiTest = new APITest();
	}

	@Test
	public void openData() throws Exception {
		setCsvFile("Open-Data-v3_E2E_Tests.csv");
		testsToRun();
	}

	@Test
	public void armedForces() throws Exception {
		setCsvFile("AF001EW_v3_E2E_Tests.csv");
		testsToRun();
	}


	public void testsToRun() throws Exception {
//		apiIntegrityTest.checkDataSetExists(config.getFilepath());
//		apiIntegrityTest.getDimensionFromCSV(config.getFilepath());
//		apiIntegrityTest.getDimensionsFromAPI(config.getFilepath());
//		apiIntegrityTest.testOptions();
//		apiIntegrityTest.hierarchyView();
//		apiTest.assertNotNullFields();
//		apiTest.assertDataSetCount();
//		csvFilterTest.init(config.getFilepath());
//		csvFilterTest.createAJob();
//		csvFilterTest.validateFilteredCSV();
//		csvFilterTest.useSameFilterAgain();
//		csvFilterTest.sendEmptyFilterWithoutDimensions();
	}

}
