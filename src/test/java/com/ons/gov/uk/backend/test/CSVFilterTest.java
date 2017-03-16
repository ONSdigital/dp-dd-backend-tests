package com.ons.gov.uk.backend.test;

import com.ons.gov.uk.DimensionValues;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.frontend.test.FileChecker;
import com.ons.gov.uk.util.CSVOps;
import com.ons.gov.uk.util.RandomStringGen;
import com.opencsv.CSVReader;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class CSVFilterTest {

	public String datasetid = null;
	public String filteredFileName = null;
	Config config = new Config();
	public String originalFile = config.getFilepath();
	MetaDataEditorTest.JobCreator jobCreator = new MetaDataEditorTest.JobCreator();
	FileChecker fileChecker = new FileChecker();
	CSVOps csvOps = new CSVOps();
	HashMap <String, ArrayList <DimensionValues>> dimOptionOriginal;
	ConcurrentHashMap <String, ArrayList <DimensionValues>> filterForJob = new ConcurrentHashMap <>();
	DimensionalAPI dimensionalAPI = new DimensionalAPI();

	@BeforeTest
	public void init() throws Exception {
		originalFile = originalFile.split(".csv")[0];
		datasetid = dimensionalAPI.getDatasetid(originalFile);
		csvOps.populateDimensionFilters(config.getFilepath());
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
		fileChecker.getFile(getURL, filteredFileName);

	}

	@Test(groups = {"validate"}, dependsOnGroups = {"jobcreator"})
	public void validateFilteredCSV() throws Exception {
		for (String key : filterForJob.keySet()) {
			ArrayList <String[]> allLines = (ArrayList <String[]>) new CSVReader(new FileReader("download/" + filteredFileName)).readAll();
			allLines.remove(0);
			if (fileChecker.getLinesToRemove().size() > 0) {
				fileChecker.getLinesToRemove().removeAll(allLines);
			}
			fileChecker.getLinesToRemove().clear();
			fileChecker.checkForFilter(filterForJob.get(key), key, "download/" + filteredFileName, allLines);
			fileChecker.printMismatch(allLines);

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
		fileChecker.getFile(getURL, filteredFileName);
		ArrayList <String[]> allLines = (ArrayList <String[]>) new CSVReader(new FileReader("download/" + filteredFileName)).readAll();
		int lines_orig_file = csvOps.returnRows(config.getFilepath()) + 1;
		int lines_downloaded_file = allLines.size();
		Assert.assertTrue(lines_orig_file == lines_downloaded_file, "number of lines in the original file : " + lines_orig_file
				+ "\nnumber of lines in the downloaded file : " + lines_downloaded_file);
	}


	public ConcurrentHashMap <String, ArrayList <DimensionValues>> setUpFilters(HashMap <String, ArrayList <DimensionValues>> dimOptions, boolean noFilter)
			throws Exception {

		for (String key : dimOptions.keySet()) {
			int randomKey = 0;
			int randindex = 0;
			ArrayList <DimensionValues> tempFilter = new ArrayList <>();
			randomKey = RandomStringGen.getRandomInt(dimOptions.get(key).size() - 1);
			if (randomKey > 0) {
				randindex = RandomStringGen.getRandomInt(randomKey);
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





}
