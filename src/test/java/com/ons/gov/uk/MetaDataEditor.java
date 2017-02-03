package com.ons.gov.uk;

import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.core.model.DatasetMetadata;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;

import static io.restassured.RestAssured.given;

/**
 * Created by giridharvasudevan on 02/02/2017.
 */
public class MetaDataEditor {
	public Config config = new Config();
	private String datasetId;
	private DatasetMetadata datasetMetadata = new DatasetMetadata();

	public static void main(String[] args) {
		MetaDataEditor mm = new MetaDataEditor();
		mm.datasetMetadata("0ea4f065-1969-46e4-9e88-5996a8a79106",
				"1", "1", "{\n" +
						"\t\"name\": \"Wales\",\n" +
						"\t\"code\": \"AM\",\n" +
						"\t\"signature_count\": 656\n" +
						"}");
		mm.callMetaDataEditor();
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public DatasetMetadata getDatasetMetadata() {
		return datasetMetadata;
	}

	public DatasetMetadata datasetMetadata(String datasetId, String majorVersion, String minorVersion, String jsonMetaData) {
		setDatasetId(datasetId);
		datasetMetadata.setDatasetId(datasetId);
		datasetMetadata.setMajorVersion(majorVersion);
		datasetMetadata.setMinorVersion(minorVersion);
		datasetMetadata.setJsonMetadata(jsonMetaData);
		return datasetMetadata;
	}

	public ResponseBody callMetaDataEditor() {
		RestAssured.baseURI = config.getMetadataEditor();
		ResponseBody responseBody = given().cookies("splash", "y")
				.contentType("application/x-www-form-urlencoded").formParam("datasetId", datasetMetadata.getDatasetId()).
						formParam("majorVersion", datasetMetadata.getMajorVersion())
				.formParam("minorVersion", datasetMetadata.getMinorVersion())
				.formParam("jsonMetadata", datasetMetadata.getJsonMetadata()).post("/");
		return responseBody;
	}
}
