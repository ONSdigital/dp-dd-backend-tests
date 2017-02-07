package com.ons.gov.uk.test;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.CSVOps;
import com.ons.gov.uk.DimensionValues;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.FileUploader;
import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.core.model.DimensionOption;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class APIIntegrityTest {
	DimensionalAPI dimAPI = new DimensionalAPI();
	JSONParser parser = new JSONParser();
	String responseFromAPI = null;
	String csvFile = new Config().getFilepath();
	String dimUrl = null;
	ArrayList <String> optionUrls = new ArrayList <String>();
	CSVOps csvOps = new CSVOps();
	HashMap <String, ArrayList <DimensionValues>> dimOptionsCSV;
	HashMap <String, ArrayList <DimensionValues>> optionsFromAPI = new HashMap <String, ArrayList <DimensionValues>>();
	ArrayList <String> dimFromAPI = new ArrayList <String>();
	ObjectMapper mapper = new ObjectMapper();

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
					dimFromAPI.add(((JSONObject) dimArray.get(index)).get("name").toString());
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
			JSONArray option = getName(optionUrls.get(index), "options");
			List <DimensionOption> dimOptions = mapper.readValue(String.valueOf(option), new TypeReference <List <DimensionOption>>() {
			});
			String dimension = ((JSONObject) (new JSONParser().parse(dimAPI.callTheLink(optionUrls.get(index)).toString()))).
					get("name").toString();
			optionsFromAPI.put(dimension, populateOptionsFromAPI(option));
		}
		for (String key : dimOptionsCSV.keySet()) {
			assertOptionExists(optionsFromAPI.get(key), dimOptionsCSV.get(key), key);
		}
	}


	public ArrayList <DimensionValues> populateOptionsFromAPI(JSONArray dimArray) {

		ArrayList <DimensionValues> options = new ArrayList <DimensionValues>();
		for (int index = 0; index < dimArray.size(); index++) {
			options.add(new DimensionValues(true, ((JSONObject) dimArray.get(index)).get("name").toString(),
					((JSONObject) dimArray.get(index)).get("code").toString()));
		}
		return options;
	}

	public JSONArray getName(String url, String name) {
		JSONArray value = null;
		try {
			String jsonString = dimAPI.callTheLink(url);
			value = (JSONArray) ((JSONObject) new JSONParser().parse(jsonString)).get(name);
		} catch (Exception ee) {
		}
		return value;
	}

	public JSONArray returnDimOptions(String url) throws Exception {
		String jsonString = dimAPI.callTheLink(url);
		JSONArray optionArray = null;
		try {
			if (!dimFromAPI.contains(((JSONObject) new JSONParser().parse(jsonString)).get("name").toString())) {
				dimFromAPI.add(((JSONObject) new JSONParser().parse(jsonString)).get("id").toString());
			}
			optionArray = dimAPI.getItems(jsonString, "options");
		} catch (Exception ee) {

		}
		return optionArray;
	}


	public ArrayList <DimensionValues> getExpectedOptions(String dimension) {
		Assert.assertTrue(dimOptionsCSV.keySet().contains(dimension));
		return dimOptionsCSV.get(dimension);
	}

	public void assertOptionExists(ArrayList <DimensionValues> actualOptions, ArrayList <DimensionValues> expectedOptions, String key) {
		Assert.assertEquals(actualOptions.size(), expectedOptions.size(), "Actual Size from the API: " + actualOptions.size() + "\n" +
				"Expected Size: " + expectedOptions.size());
		ArrayList <String> actualCodes = new ArrayList <>();
		ArrayList <String> expectedCodes = new ArrayList <>();
		actualCodes.removeAll(getDimOptions(expectedOptions));
		expectedCodes.removeAll(getDimOptions(actualOptions));
		Assert.assertTrue(expectedCodes.size() == actualCodes.size(),
				"Actual options in the API and CSV do not match. \n" +
						"Size of actual options and expected options for the " + key +
						"did not cancel out\n");

		}

	public ArrayList <String> getDimOptions(ArrayList <DimensionValues> dimensionValuesArrayList) {
		ArrayList <String> toReturn = new ArrayList <>();
		for (DimensionValues dimensionValues : dimensionValuesArrayList) {
			toReturn.add(dimensionValues.getCodeId());
		}
		return toReturn;
	}

}
