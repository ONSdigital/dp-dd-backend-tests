package com.ons.gov.uk.test;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.CSVOps;
import com.ons.gov.uk.DimensionValues;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.FileUploader;
import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.core.model.Dimension;
import com.ons.gov.uk.core.model.DimensionOption;
import com.ons.gov.uk.core.model.ItemsObj;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
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
	CSVOps csvOps = new CSVOps();
	HashMap <String, ArrayList <DimensionValues>> dimOptionsCSV;
	HashMap <String, ArrayList <DimensionValues>> optionsFromAPI = new HashMap <>();
	ObjectMapper mapper = new ObjectMapper();
	ArrayList <Dimension> dimensions = new ArrayList <>();
	FileUploader fileUploader = new FileUploader();

	@Test(groups = {"verifyDataSetExists"})
	public void checkDataSetExists() throws Exception {
		// If Dataset does not exist, upload it
		responseFromAPI = dimAPI.waitForApiToLoad(csvFile);
		if (!dimAPI.waitForApiToLoad(csvFile).contains(csvFile)) {
			fileUploader.uploadFile();
			Thread.sleep(30000);
		}
	}


	@Test(groups = {"getCSVDimensions"}, dependsOnGroups = {"verifyDataSetExists"})
	public void getDimensionFromCSV() throws Exception {
		getCSVDimensions();
	}

	@Test(groups = {"dimension"}, dependsOnGroups = {"getCSVDimensions"})
	public void getDimensionsFromAPI() {
		getDimensionUrl();
		String jsonString = dimAPI.callTheLink(dimUrl);
		try {
			dimensions = (ArrayList) mapper.readValue(String.valueOf(jsonString), new TypeReference <List <Dimension>>() {
			});
			Assert.assertTrue(dimensions.size() > 0, "****** No Dimensions obtained from API *****");
		} catch (Exception ee) {
		}
	}

	@Test(groups = {"options"}, dependsOnGroups = {"dimension"})
	public void testOptions() throws Exception {
		getDimAPIMap();
		for (String key : dimOptionsCSV.keySet()) {
			assertOptionExists(optionsFromAPI.get(key), dimOptionsCSV.get(key), key);
		}
	}

	@Test(groups = {"hierarchyView"}, dependsOnGroups = {"options"})
	public void hierarchyView() {
		for (Dimension dimTemp : dimensions) {
			if (dimTemp.getHierarchical()) {
				String response = dimAPI.callTheLink(dimTemp.getUrl());
				Assert.assertFalse(response.contains("error"), "Hierarchy view for the key " + dimTemp.getName());

			}
		}
	}

	public void getCSVDimensions() {
		try {
			Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
			ArrayList <ItemsObj> itemsList = (ArrayList) mapper.readValue(String.valueOf(itemsArray),
					new TypeReference <List <ItemsObj>>() {
					});
			for (ItemsObj item : itemsList) {
				if (item.getTitle().equals(csvFile)) {
					dimUrl = item.getDimensionsUrl();
					break;
				}
			}
			if (csvFile.contains("AnnualBusinessSurvey_NumberOfEnterprises_v3")) {
				int sleepCount = 0;
				Dimension dimension = new Dimension();
				String urlToCheck = dimUrl + "/Geographic_Hierarchy";
				while (dimension.getOptions().size() != dimOptionsCSV.get("Geographic_Hierarchy").size()
						&& sleepCount < 20) {
					String jsonString = dimAPI.callTheLink(urlToCheck);
					dimension = (Dimension) mapper.readValue(String.valueOf(jsonString), new TypeReference <Dimension>() {
					});
					Thread.sleep(10000);
					sleepCount++;
				}
			}
		} catch (Exception ee) {
		}
	}


	public void getDimAPIMap() throws Exception {
		for (Dimension dim : dimensions) {
			JSONArray option = getName(dim.getUrl(), "options");
			ArrayList <DimensionOption> dimOptions = (ArrayList) mapper.readValue(String.valueOf(option),
					new TypeReference <List <DimensionOption>>() {
					});
			optionsFromAPI.put(dim.getName(), populateOptionsFromAPI(dimOptions, dim.getHierarchical()));
		}
	}




	public ArrayList <DimensionValues> populateOptionsFromAPI(ArrayList <DimensionOption> dimensionOptions, boolean hierarchical) {
		ArrayList <DimensionValues> options = new ArrayList <>();
		for (DimensionOption dimOpt : dimensionOptions) {
			options.add(new DimensionValues(hierarchical, dimOpt.getName(),
					dimOpt.getCode()));
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

	public void assertOptionExists(ArrayList <DimensionValues> actualOptions, ArrayList <DimensionValues> expectedOptions, String key) {
		Assert.assertEquals(actualOptions.size(), expectedOptions.size(), "Actual Size from the API: " + actualOptions.size() + "\n" +
				"Expected Size: " + expectedOptions.size() + " for the key does not match " + key);
		ArrayList <String> actualCodes = getDimOptions(actualOptions);
		ArrayList <String> expectedCodes = getDimOptions(expectedOptions);
		for (int index = 0; index < expectedOptions.size(); index++) {
			Assert.assertTrue(expectedCodes.contains(actualCodes.get(index)),
					"Option " + actualCodes.get(index) + " is not present in the CSV");
		}
		actualCodes.removeAll(getDimOptions(expectedOptions));
		expectedCodes.removeAll(getDimOptions(actualOptions));

		Assert.assertTrue(expectedCodes.size() == actualCodes.size(),
				"Actual options in the API and CSV do not match. \n" +
						"\nSize of actual options and expected options for the Dimension " + key +
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
