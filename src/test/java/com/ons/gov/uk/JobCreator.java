package com.ons.gov.uk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.core.model.CreateJob;
import com.ons.gov.uk.core.model.DimensionFilter;
import com.ons.gov.uk.core.model.FileFormat;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.restassured.RestAssured.given;
import static java.util.Collections.singleton;

public class JobCreator {
	private final int SLEEP_TIMER = 1000;
	public String fileName = null;
	Config config = new Config();
	RestAssured restAssured = new RestAssured();
	int loopCounter = 30;

	public String request(String dataSetId, ConcurrentHashMap <String, ArrayList <DimensionValues>> filters) throws JsonProcessingException {
		CreateJob request = new CreateJob();
		List <DimensionFilter> dimensions = new ArrayList <>();
		request.setDataSetId(dataSetId);
		for (String key : filters.keySet()) {
			List <DimensionValues> dimensionValues = filters.get(key);
			ArrayList <String> codes = new ArrayList <>();
			for (DimensionValues dim : dimensionValues) {
				codes.add(dim.getCodeId());
			}
			dimensions.add(new DimensionFilter(key, codes));
		}

		Set <FileFormat> formats = singleton(FileFormat.CSV);
		request.setDimensions(dimensions);
		request.setFileFormats(formats);

		try {
			System.out.println(new ObjectMapper().writeValueAsString(request));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ObjectMapper().writeValueAsString(request);
	}

	public String getJobID(String jsonStr, boolean failFast) throws Exception {
		RestAssured.baseURI = config.getJobCreator();
		String dataSetId = null;
		Response response = given().cookies("splash", "y")
				.contentType("application/json").body(jsonStr).post("/job");
		if (failFast) {
			Assert.assertTrue("Response should fail with an error " + response.getStatusCode(), response.getStatusCode() >= 400);
		}
		try {
			dataSetId = ((JSONObject) new JSONParser().parse(response.asString())).get("id").toString();
		} catch (Exception ee) {
			if (!failFast) {
				while (loopCounter != 0) {

					Thread.sleep(SLEEP_TIMER);
					loopCounter--;
					dataSetId = getJobID(jsonStr, false);
				}
			}
		}
		return dataSetId;
	}

	public String returnCSVUrl(String jobID) throws Exception {
		String urlToDownloadFile = null;
		RestAssured.baseURI = config.getJobCreator();
		Response response = given().cookies("splash", "y").contentType("application/json").get("/job/" + jobID);
		System.out.println("from returncsv " + response.asString());
		String status = ((JSONObject) new JSONParser().parse(response.asString())).get("status").toString();
		try {
			if (status.equalsIgnoreCase("Complete")) {
				JSONArray getFiles = (JSONArray) ((JSONObject) new JSONParser().parse(response.asString())).get("files");
				for (int i = 0; i < getFiles.size(); i++) {
					JSONObject jo = (JSONObject) getFiles.get(i);
					if (jo.get("url").toString() != null) {
						urlToDownloadFile = jo.get("url").toString();
						fileName = jo.get("name").toString();
						break;
					} else if (fileName == null) {
						System.out.println("Filename is null");
						urlToDownloadFile = waitForURL(jobID);
					}
				}

			} else {
				System.out.println("status is not complete");
				urlToDownloadFile = waitForURL(jobID);
			}
		} catch (Exception ee) {
			System.out.println(ee.getCause());
			ee.printStackTrace();
		}

		return urlToDownloadFile;
	}

	public String waitForURL(String jobID) {
		try {
			while (loopCounter != 0) {
				loopCounter--;
				Thread.sleep(SLEEP_TIMER);
				return returnCSVUrl(jobID);

			}
		} catch (Exception ee) {
		}
		return null;
	}
	public void getFile(String url) throws Exception {
		String dirname = "download/";
		File dir = new File(dirname);
		dir.mkdir();
		for (File file : dir.listFiles()) {
			if (file.getName().contains(".csv")) {
				file.delete();
			}
		}
		File csvFile = new File(dirname + fileName);
		csvFile.createNewFile();
		BufferedInputStream in = null;
		OutputStream fout = null;
		try {
			in = new BufferedInputStream(new URL(url).openStream());
			try {
				fout = new FileOutputStream(csvFile, true);
			} catch (FileNotFoundException ee) {

			}
			byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null)
				in.close();
			if (fout != null)
				fout.close();
		}
	}


}
