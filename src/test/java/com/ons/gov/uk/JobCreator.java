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

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static java.util.Collections.singleton;

public class JobCreator {
	public String fileName = null;
	Config config = new Config();
	RestAssured restAssured = new RestAssured();
	int loopCounter = 5;

	public String request(String dataSetId, HashMap <String, ArrayList <String>> filters) throws JsonProcessingException {
		CreateJob request = new CreateJob();
		List <DimensionFilter> dimensions = new ArrayList <>();
		request.setDataSetId(dataSetId);
		for (String key : filters.keySet()) {
			dimensions.add(new DimensionFilter(key, filters.get(key)));
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

	public String getJobID(String jsonStr) throws Exception {
		RestAssured.baseURI = config.getJobCreator();
		String dataSetId = null;
		Response response = given().cookies("splash", "y")
				.contentType("application/json").body(jsonStr).post("/job");
		try {
			dataSetId = ((JSONObject) new JSONParser().parse(response.asString())).get("id").toString();
		} catch (Exception ee) {
			while (loopCounter != 0) {
				loopCounter--;
				Thread.sleep(100 * loopCounter);
				dataSetId = getJobID(jsonStr);
			}
		}
		loopCounter = 10;
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
				Thread.sleep(100 * loopCounter);
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
