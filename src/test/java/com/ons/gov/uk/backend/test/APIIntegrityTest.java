package com.ons.gov.uk.backend.test;


import com.fasterxml.jackson.core.type.TypeReference;
import com.ons.gov.uk.DimensionValues;
import com.ons.gov.uk.model.Dimension;
import com.ons.gov.uk.model.DimensionOption;
import com.ons.gov.uk.model.ItemsObj;
import com.ons.gov.uk.util.CSVOps;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class APIIntegrityTest extends TestSetup {

	String csvFile = config.getFilepath();
	String originalFile = config.getFilepath();
	String dimUrl = null;
	CSVOps csvOps = new CSVOps();
	HashMap <String, ArrayList <DimensionValues>> dimOptionsCSV;
	HashMap <String, ArrayList <DimensionValues>> optionsFromAPI = new HashMap <>();
	ArrayList <Dimension> dimensions = new ArrayList <>();
	MetaDataEditorTest.FileUploader fileUploader = new MetaDataEditorTest.FileUploader();


	@BeforeTest
	public void checkDataSetExists() throws Exception {
		csvFile = csvFile.split(".csv")[0];
		responseFromAPI = dimAPI.checkEndPoint();
		if (!responseFromAPI.contains(csvFile)) {
			fileUploader.uploadFile(originalFile);
			responseFromAPI = dimAPI.waitForApiToLoad(csvFile);
		}

	}


	@Test(groups = {"getCSVDimensions"})
	public void getDimensionFromCSV() throws Exception {
		getCSVDimensions();
	}


	@Test(groups = {"dimension"}, dependsOnGroups = {"getCSVDimensions"})
	public void getDimensionsFromAPI() throws Exception {
		getDimensionUrl();
		String jsonString = dimAPI.callTheLink(dimUrl);
		dimensions = (ArrayList) mapper.readValue(String.valueOf(jsonString), new TypeReference <List <Dimension>>() {
		});
		checkUploadComplete();
		Assert.assertTrue(dimensions.size() > 0, "****** No Dimensions obtained from API *****");
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

	public void getCSVDimensions() throws Exception {
		csvOps.populateDimensionFilters(config.getFilepath());
		dimOptionsCSV = csvOps.getDimOptionsFromCSV();
	}

	public void getDimensionUrl() throws Exception {
		Assert.assertTrue(dimAPI.titleExists(csvFile));
		JSONArray itemsArray = dimAPI.getItems("items");
		ArrayList <ItemsObj> itemsList = (ArrayList) mapper.readValue(String.valueOf(itemsArray),
				new TypeReference <List <ItemsObj>>() {
				});
		for (ItemsObj item : itemsList) {
			if (item.getTitle().contains(csvFile)) {
				dimUrl = item.getDimensionsUrl();
				break;
			}
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

	public JSONArray getName(String url, String name) throws Exception {
		JSONArray value = null;
			String jsonString = dimAPI.callTheLink(url);
		value = (JSONArray) ((JSONObject) new JSONParser().parse(jsonString)).get(name);
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

	public String getDimWithMoreOptions() {
		String dimKey = null;
		int size = 0;
		for (String key : dimOptionsCSV.keySet()) {
			if (dimOptionsCSV.get(key).size() > size) {
				size = dimOptionsCSV.get(key).size();
				dimKey = key;
			}
		}

		return dimKey;
	}

	public void checkUploadComplete() throws Exception {
		int sleepCount = 0;
		String keyToChk = getDimWithMoreOptions();
		String urlToCheck = dimUrl + "/" + keyToChk;
		JSONArray option = getName(urlToCheck, "options");
		ArrayList <DimensionOption> dimOptions = (ArrayList) mapper.readValue(String.valueOf(option),
				new TypeReference <List <DimensionOption>>() {
				});
		System.out.println(urlToCheck);
		while (dimOptions.size() != dimOptionsCSV.get(keyToChk).size()
				&& sleepCount < 20) {
			String jsonString = dimAPI.callTheLink(urlToCheck);
			dimOptions = (ArrayList) mapper.readValue(String.valueOf(jsonString),
					new TypeReference <List <DimensionOption>>() {
					});
			Thread.sleep(10000);
			sleepCount++;
		}
	}

}
