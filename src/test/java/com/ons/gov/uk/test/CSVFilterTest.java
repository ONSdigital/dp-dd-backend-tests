package com.ons.gov.uk.test;

import com.ons.gov.uk.CSVOps;
import com.ons.gov.uk.DimensionValues;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.JobCreator;
import com.ons.gov.uk.core.Config;
import com.opencsv.CSVReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class CSVFilterTest {

	public String datasetid = null;
	public String filteredFileName = null;
	Config config = new Config();
	public String originalFile = config.getFilepath();
	JobCreator jobCreator = new JobCreator();
	CSVOps csvOps = new CSVOps();
	HashMap <String, ArrayList <DimensionValues>> dimOptionOriginal;
	ConcurrentHashMap <String, ArrayList <DimensionValues>> filterForJob = new ConcurrentHashMap <>();
	ArrayList <String[]> linesToRemove = new ArrayList <>();
	ArrayList <String> searchRegex = new ArrayList <>();

	@BeforeTest
	public void init() throws Exception {
		setDatasetid();
		csvOps.populateDimensionFilters(originalFile);
		dimOptionOriginal = csvOps.dimAndOptions;
		setUpFilters(dimOptionOriginal, false);
	}

	@Test(groups = {"jobcreator"})
	public void createAJob() throws Exception {
		//Request a job
		String requestJson = jobCreator.request(datasetid, filterForJob);
		String jobID = jobCreator.getJobID(requestJson, false);
		Assert.assertNotNull(jobID, "*********Job ID is not created. Job creator might be down.*******");
		String getURL = jobCreator.returnCSVUrl(jobID);
		Assert.assertNotNull(getURL, "********* URL to download filtered CSV file is null. *******");
		filteredFileName = jobCreator.fileName;
		jobCreator.getFile(getURL);

	}

	@Test(groups = {"validate"}, dependsOnGroups = {"jobcreator"})
	public void validateFilteredCSV() throws Exception {
		for (String key : filterForJob.keySet()) {
			ArrayList <String[]> allLines = (ArrayList <String[]>) new CSVReader(new FileReader("download/" + filteredFileName)).readAll();
			allLines.remove(0);
			if (linesToRemove.size() > 0) {
				linesToRemove.removeAll(allLines);
			}
			linesToRemove.clear();
			checkForFilter(filterForJob.get(key), key, "download/" + filteredFileName, allLines);
			if (allLines.size() != linesToRemove.size()) {
				for (String[] strArr : allLines) {
					for (int index = 0; index < strArr.length; index++) {
						System.out.print(strArr[index] + ",");
					}
					System.out.println();
				}

			}
			Assert.assertTrue(allLines.size() == linesToRemove.size(), "Mismatch between the filter and the downloaded CSV");
		}
	}

	@Test(groups = {"makeSameFilterCall"}, dependsOnGroups = {"validate"})
	public void useSameFilterAgain() throws Exception {
		createAJob();
		validateFilteredCSV();
	}

	@Test(groups = {"downloadFail"}, dependsOnGroups = {"makeSameFilterCall"})
	public void sendEmptyFilterWithDimensions() throws Exception {
		setUpFilters(dimOptionOriginal, true);
		String requestJson = jobCreator.request(datasetid, filterForJob);
		String jobID = jobCreator.getJobID(requestJson, true);
		Assert.assertNull(jobID, "*********Job ID is not created. Job creator might be down.*******");
	}


	@Test(groups = {"downloadAll"}, dependsOnGroups = {"downloadFail"})
	public void sendEmptyFilterWithoutDimensions() throws Exception {
		setUpFilters(dimOptionOriginal, true);
		for (String key : filterForJob.keySet()) {
			filterForJob.remove(key);
		}
		String requestJson = jobCreator.request(datasetid, filterForJob);
		String jobID = jobCreator.getJobID(requestJson, false);
		Assert.assertNotNull(jobID, "*********Job ID is not created. Job creator might be down.*******");
		String getURL = jobCreator.returnCSVUrl(jobID);
		Assert.assertNotNull(getURL, "********* URL to download filtered CSV file is null. *******");
		filteredFileName = jobCreator.fileName;
		jobCreator.getFile(getURL);
		ArrayList <String[]> allLines = (ArrayList <String[]>) new CSVReader(new FileReader("download/" + filteredFileName)).readAll();
		int lines_orig_file = csvOps.returnRows(originalFile) + 1;
		int lines_downloaded_file = allLines.size();
		Assert.assertTrue(lines_orig_file == lines_downloaded_file, "number of lines in the original file : " + lines_orig_file
				+ "\nnumber of lines in the downloaded file : " + lines_downloaded_file);
	}


	public ConcurrentHashMap <String, ArrayList <DimensionValues>> setUpFilters(HashMap <String, ArrayList <DimensionValues>> dimOptions, boolean noFilter) {

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
				if (!noFilter) {
					tempFilter.add(valueFilter);
				}
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

	public boolean checkForFilter(ArrayList <DimensionValues> dimFiler, String key, String fileName, ArrayList <String[]> allLines) throws Exception {
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
						if (!linesToRemove.contains(strArr)) {
							linesToRemove.add(strArr);
							exists = true;
						}
					}

				}
			}
		}
		return exists;

	}

}
