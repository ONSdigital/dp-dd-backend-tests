package com.ons.gov.uk.backend.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.core.Config;
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

	public void setCsvFile(String fileName) {
		if (config.getFilepath() == null) {
			config.setFilepath(fileName);
		}
	}


}
