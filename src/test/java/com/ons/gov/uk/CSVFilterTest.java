package com.ons.gov.uk;

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

public class CSVFilterTest {

	public String datasetid = null;
	public String filteredFileName = null;
	Config config = new Config();
	public String originalFile = config.getFilepath();
	JobCreator jobCreator = new JobCreator();
	CSVOps csvOps = new CSVOps();
	HashMap <String, ArrayList <String>> dimOptionOriginal;
	HashMap <String, ArrayList <String>> filterForJob = new HashMap <>();

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
		Assert.assertNotNull(getURL, "********* URL to download filtered CSV file is null Job. *******");
		filteredFileName = jobCreator.fileName;
		jobCreator.getFile(getURL);

	}

	@Test(groups = {"validate"}, dependsOnGroups = {"jobcreator"})
	public void validateFilteredCSV() throws Exception {
		for (String key : filterForJob.keySet()) {
			checkForFilter(filterForJob.get(key), key, "download/" + filteredFileName);
		}
	}

	public HashMap <String, ArrayList <String>> setUpFilters(HashMap <String, ArrayList <String>> dimOptions) {
		for (String key : dimOptions.keySet()) {
			int randindex = new Random().nextInt(dimOptions.get(key).size() - 1);
			String valueFilter = dimOptions.get(key).get(randindex);
			ArrayList <String> tempFilter = new ArrayList <>();
			tempFilter.add(valueFilter);
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

	public void checkForFilter(ArrayList <String> dimFiler, String key, String fileName) throws Exception {
		CSVReader csvReader = null;
		for (String filter : dimFiler) {
			String searchTerm = key + "," + filter;
			csvReader = new CSVReader(new FileReader(fileName));
			String[] nextLine;
			while ((nextLine = csvReader.readNext()) != null) {
				if (!nextLine[0].contains("Observation")) {
					if (nextLine[0].contains("****")) {
						System.out.println("******Last line of the CSV***");
					} else {
						String line = "repl";
						for (String lineVal : nextLine) {
							line = line + "," + lineVal;
						}
						line = line.replace("repl,", "");
						Assert.assertTrue(line.contains(searchTerm), "****The filter is not present in the file.****\n" +
								"Expected search term " + searchTerm +
								"\n Actual Line in the csv " + line);
					}
				}
			}
		}
		csvReader.close();
	}


}
