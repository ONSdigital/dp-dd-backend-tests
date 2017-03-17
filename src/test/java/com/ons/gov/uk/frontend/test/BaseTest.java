package com.ons.gov.uk.frontend.test;

import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.MetaDataSetUp;
import com.ons.gov.uk.backend.test.APIIntegrityTest;
import com.ons.gov.uk.backend.test.TestSetup;
import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.frontend.pages.BasePage;
import com.ons.gov.uk.frontend.pages.FileUploader;
import org.openqa.selenium.By;


public class BaseTest extends TestSetup {
	BasePage basePage = new BasePage();
	Config config = new Config();
	MetaDataSetUp metaDataSetUp = new MetaDataSetUp();
	DimensionalAPI dimensionalAPI = new DimensionalAPI();
	FileUploader fileUploader = new FileUploader();
	APIIntegrityTest apiIntegrityTest = new APIIntegrityTest();


	public void openPage(By dataSet) {
		basePage.navigateToUrl(config.getBaseURL());
		basePage.click(dataSet);
		basePage.switchToLatestWindow();
		basePage.click(basePage.customise_data_set);
	}

	public void checkForDS(By link) throws Exception {
		basePage.getDriver();
		String baseUrl = basePage.getConfig().getBaseURL();
		basePage.navigateToUrl(baseUrl);
		basePage.click(link);
		basePage.switchToLatestWindow();
	}

	public void checkAndCreateDataResource(String filename, String title) throws Exception {
		filename = filename.split(".csv")[0];
			String dataResName = "Test_" + filename;
			boolean dataResMapped = false;
			boolean exists = metaDataSetUp.doesDataResourceExist(dataResName);
			if (!exists) {
				metaDataSetUp.createDataResource(dataResName, title);
			}
		dataResMapped = metaDataSetUp.isTheMetaDataMapped(title);
			if (!dataResMapped) {
				metaDataSetUp.updateMetaData(filename, title, dataResName);
			}
	}


	public void checkAndUploadFile(String filename) throws Exception {
		apiIntegrityTest.checkDataSetExists();
		apiIntegrityTest.getDimensionUrl();
		apiIntegrityTest.checkUploadComplete();
	}

	public void setUpbeforeRun(String dataResource) throws Exception {
		if (!config.getBaseURL().equals("https://discovery.onsdigital.co.uk/dd")) {
			apiIntegrityTest.setCsvFile(dataResource);
			checkAndUploadFile(dataResource);
			checkAndCreateDataResource(dataResource, getTitle(dataResource));
		}
	}


}
