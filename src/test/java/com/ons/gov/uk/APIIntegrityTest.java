package com.ons.gov.uk;


import com.ons.gov.uk.core.Config;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class APIIntegrityTest {
	DimensionalAPI dimAPI = new DimensionalAPI();
	JSONParser parser = new JSONParser();
	String responseFromAPI = null;
	String csvFile = new Config().getFilepath();
	String dimUrl = null;
	ArrayList <String> optionUrls = new ArrayList <String>();
	CSVOps csvOps = new CSVOps();
	HashMap <String, ArrayList <String>> dimOptionsCSV;
	HashMap <String, ArrayList <String>> optionsFromAPI = new HashMap <String, ArrayList <String>>();
	ArrayList <String> dimFromAPI = new ArrayList <String>();

	@BeforeTest
	public void init() throws Exception {
		getCSVDimensions();
		responseFromAPI = dimAPI.waitForApiToLoad(csvFile);
		getDimensionUrl();
	}

	public void getCSVDimensions() {
		if (!dimAPI.waitForApiToLoad(csvFile).contains(csvFile)) {
			new FileUploader();
			try {
				Thread.sleep(20000);
			} catch (InterruptedException ee) {
			}
		}
		try {
			csvOps.populateDimensionFilters(csvFile);
		} catch (IOException ee) {
			ee.printStackTrace();
		}
		dimOptionsCSV = csvOps.getDimOptionsFromCSV();
	}

	public void getDimensionUrl() {
		try {
			Assert.assertTrue(dimAPI.titleExists(csvFile));
			JSONArray itemsArray = dimAPI.getItems("items");
			for (int i = 0; i < itemsArray.size(); i++) {
				JSONObject jo = (JSONObject) itemsArray.get(i);
				if (jo.get("title").equals(csvFile)) {
					dimUrl = jo.get("dimensionsUrl").toString();
					break;
				}
			}
		} catch (Exception ee) {
		}
	}

	@Test(groups = {"dimension"})
	public void getDimensions() {
		String jsonString = dimAPI.callTheLink(dimUrl);
		try {
			JSONArray dimArray = dimAPI.getValue(jsonString);
			for (int index = 0; index < dimArray.size(); index++) {
				try {
					dimFromAPI.add(((JSONObject) dimArray.get(index)).get("id").toString());
				} catch (NullPointerException ee) {
				}
				optionUrls.add(((JSONObject) dimArray.get(index)).get("url").toString());
			}
		} catch (Exception ee) {
		}
	}

	@Test(groups = {"options"}, dependsOnGroups = {"dimension"})
	public void testOptions() throws Exception {
		for (int index = 0; index < optionUrls.size(); index++) {
			JSONArray dimArray = returnDimOptions(optionUrls.get(index));
			String option = getName(optionUrls.get(index), "name");
				optionsFromAPI.put(option, populateOptionsFromAPI(dimArray));
		}
		for (String key : dimOptionsCSV.keySet()) {
			assertOptionExists(optionsFromAPI.get(key), dimOptionsCSV.get(key));
		}
	}


	public ArrayList <String> populateOptionsFromAPI(JSONArray dimArray) {
		ArrayList <String> options = new ArrayList <String>();
		for (int index = 0; index < dimArray.size(); index++) {
			String nameValue = ((JSONObject) dimArray.get(index)).get("name").toString();
			options.add(nameValue);
		}
		return options;
	}

	public String getName(String url, String name) {
		String value = null;
		try {
			String jsonString = dimAPI.callTheLink(url);
			value = ((JSONObject) new JSONParser().parse(jsonString)).get(name).toString();
		} catch (Exception ee) {
		}
		return value;
	}

	public JSONArray returnDimOptions(String url) throws Exception {
		String jsonString = dimAPI.callTheLink(url);
		JSONArray optionArray = null;
		try {
			if (!dimFromAPI.contains(((JSONObject) new JSONParser().parse(jsonString)).get("id").toString())) {
				dimFromAPI.add(((JSONObject) new JSONParser().parse(jsonString)).get("id").toString());
			}
			optionArray = dimAPI.getItems(jsonString, "options");
		} catch (Exception ee) {

		}
		return optionArray;
	}


	public ArrayList <String> getExpectedOptions(String dimension) {
		Assert.assertTrue(dimOptionsCSV.keySet().contains(dimension));
		return dimOptionsCSV.get(dimension);
	}

	public void assertOptionExists(ArrayList <String> actualOptions, ArrayList <String> expectedOptions) {
		Assert.assertEquals(actualOptions.size(), expectedOptions.size());
		for (int index = 0; index < expectedOptions.size(); index++) {
			Assert.assertTrue(expectedOptions.contains(actualOptions.get(index)),
					"Option " + actualOptions.get(index) + " is not present in the CSV");
		}

	}

}
