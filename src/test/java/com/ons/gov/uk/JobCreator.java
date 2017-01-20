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

	public String getJobID(String jsonStr) {
		RestAssured.baseURI = config.getJobCreator();
		String dataSetId = null;
		Response response = given()
				.contentType("application/json").body(jsonStr).post("/job");
		try {
			System.out.println(response.asString());
			System.out.println(((JSONObject) new JSONParser().parse(response.asString())).get("id").toString());
			dataSetId = ((JSONObject) new JSONParser().parse(response.asString())).get("id").toString();
		} catch (Exception ee) {
		}
		return dataSetId;
	}

	public String returnCSVUrl(String jobID) throws Exception {
		String urlToDownloadFile = null;
		RestAssured.baseURI = config.getJobCreator();
		Response response = given().contentType("application/json").get("/job/" + jobID);
		int counter = 10;
		try {
			if (((JSONObject) new JSONParser().parse(response.asString())).get("status").
					toString().equalsIgnoreCase("Complete")) {
				JSONArray getFiles = (JSONArray) ((JSONObject) new JSONParser().parse(response.asString())).get("files");
				for (int i = 0; i < getFiles.size(); i++) {
					JSONObject jo = (JSONObject) getFiles.get(i);
					if (jo.get("url").toString() != null) {
						urlToDownloadFile = jo.get("url").toString();
						fileName = jo.get("name").toString();
						break;
					}
				}

			}
		} catch (Exception ee) {
			while (counter != 0) {
				Thread.sleep(5000);
				returnCSVUrl(jobID);
				counter--;
			}
		}

		return urlToDownloadFile;
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
				fout.write(20);
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
