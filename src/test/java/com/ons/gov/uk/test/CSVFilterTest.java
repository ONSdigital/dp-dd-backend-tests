package com.ons.gov.uk.test;

import com.ons.gov.uk.CSVOps;
import com.ons.gov.uk.DimensionValues;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.JobCreator;
import com.ons.gov.uk.core.Config;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CSVFilterTest {

	public String datasetid = null;
	public String filteredFileName = null;
	Config config = new Config();
	public String originalFile = config.getFilepath();
	JobCreator jobCreator = new JobCreator();
	CSVOps csvOps = new CSVOps();
	HashMap <String, ArrayList <DimensionValues>> dimOptionOriginal;
	HashMap <String, ArrayList <DimensionValues>> filterForJob = new HashMap <>();
	ArrayList <String[]> linesToRemove = new ArrayList <>();
	ArrayList <String[]> allLines = new ArrayList <>();
	ArrayList <String> searchRegex = new ArrayList <>();

	@BeforeTest
	public void init() throws Exception {
		setDatasetid();
		csvOps.populateDimensionFilters(originalFile);
		dimOptionOriginal = csvOps.dimAndOptions;
		setUpFilters(dimOptionOriginal);
	}

	@Test(groups = {"jobcreator"})
	public void createAJob() throws Exception {
		//Request a job
		String requestJson = jobCreator.request(datasetid, filterForJob);
		String jobID = jobCreator.getJobID(requestJson);
		Assert.assertNotNull(jobID, "*********Job ID is not created. Job creator might be down.*******");
		String getURL = jobCreator.returnCSVUrl(jobID);
		Assert.assertNotNull(getURL, "********* URL to download filtered CSV file is null. *******");
		filteredFileName = jobCreator.fileName;
		jobCreator.getFile(getURL);

	}

	@Test(groups = {"validate"}, dependsOnGroups = {"jobcreator"})
	public void validateFilteredCSV() throws Exception {
		CSVReader csvReader = new CSVReader(new FileReader("download/" + filteredFileName));
		allLines = (ArrayList <String[]>) csvReader.readAll();
		allLines.remove(0);
		for (String key : filterForJob.keySet()) {
			checkForFilter(filterForJob.get(key), key, "download/" + filteredFileName, true);
		}
		if (allLines.size() != linesToRemove.size()) {
			for (String[] strArr : allLines) {
				for (int index = 0; index < strArr.length; index++) {
					System.out.print(strArr[index] + ",");
				}
				System.out.println("----");
			}

		}
		Assert.assertTrue(allLines.size() == 0, "Mismatch between the filter and the downloaded CSV");
	}

	public HashMap <String, ArrayList <DimensionValues>> setUpFilters(HashMap <String, ArrayList <DimensionValues>> dimOptions) {

		for (String key : dimOptions.keySet()) {
			int randomKey = 0;
			int randindex = 0;
			ArrayList <DimensionValues> tempFilter = new ArrayList <>();
			try {
				randomKey = new Random().nextInt(dimOptions.get(key).size() - 1);
			} catch (IllegalArgumentException ee) {
			}
			if (randomKey > 0) {
				randindex = new Random().nextInt(randomKey);
			}
			for (int rnd = 0; rnd <= randindex; rnd++) {
				DimensionValues valueFilter = dimOptions.get(key).get(rnd);
				tempFilter.add(valueFilter);
			}
				filterForJob.put(key, tempFilter);
		}
		return filterForJob;
	}

	public void setDatasetid() throws Exception {
		DimensionalAPI api = new DimensionalAPI();
		JSONArray itemsArray = api.getItems("items");
		for (int i = 0; i < itemsArray.size(); i++) {
			JSONObject jo = (JSONObject) itemsArray.get(i);
			if (jo.get("title").equals(config.getFilepath())) {
				datasetid = jo.get("id").toString();
				break;
			}
		}
	}

	public ArrayList <String> searchTerms(String hierarchy, String code, String key) {


		String searchTerm = (!hierarchy.equals("")) ?
				"(.*)" + key + "," + hierarchy + "," + code + "(.*)" : "(.*)" + key + "," + code + "(.*)";
		searchRegex.add(searchTerm);
		return searchRegex;

	}

	public boolean checkForFilter(ArrayList <DimensionValues> dimFiler, String key, String fileName, boolean slicedFile) throws Exception {
		boolean exists = false;
		for (DimensionValues filter : dimFiler) {
			searchTerms(filter.getHierarchyValue(), filter.getCodeId(), key);
		}
		if (allLines.size() > 0) {
			for (String[] strArr : allLines) {
				String temp = "";
				for (int index = 0; index < strArr.length; index++) {
					temp += strArr[index] + ",";
				}

				for (String search : searchRegex) {
					if (temp.matches(search)) {
						linesToRemove.add(strArr);
						exists = true;
					}

				}
			}
		}
		allLines.removeAll(linesToRemove);
		StringWriter sw = new StringWriter();
		CSVWriter writer = new CSVWriter(sw);
		writer.writeAll(allLines);
		return exists;

	}

}
