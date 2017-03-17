package com.ons.gov.uk.backend.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.frontend.pages.FileUploader;
import com.ons.gov.uk.frontend.test.FileChecker;
import com.ons.gov.uk.util.CSVOps;
import com.ons.gov.uk.util.PropertyReader;
import org.json.simple.parser.JSONParser;


public class TestSetup {
	DimensionalAPI dimAPI = new DimensionalAPI();
	JSONParser parser = new JSONParser();
	String responseFromAPI = null;
	Config config = new Config();
	PropertyReader propertyReader = new PropertyReader();
	CSVOps csvOps = new CSVOps();
	ObjectMapper mapper = new ObjectMapper();
	FileUploader fileUploader = new FileUploader();
	FileChecker fileChecker = new FileChecker();

	public String getCsvFile() {
		return config.getFilepath();
	}

	public void setCsvFile(String fileName) {
		config.setFilepath(fileName);
	}

	public String getTitle(String fileName) {
		String title = null;
		if (fileName.contains("AF001")) {
			title = propertyReader.getValue("armed_forces_linkText");
		} else if (fileName.contains("Open-Data")) {
			title = propertyReader.getValue("cpi_linkText");
		} else if (fileName.contains("AnnualBusinessSurvey_UKBusinessValue")) {
			title = propertyReader.getValue("annual_business_survey_linkText");
		}
		return title;
	}

}
