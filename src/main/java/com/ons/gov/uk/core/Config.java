package com.ons.gov.uk.core;


import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class Config {
	private String filepath;
	private String dbLoader, fileuploader;
	private String datasetEndPointReal;
	private String datasetEndPointStub;
	private String postgres;
	private String jobCreator;
	private String metadataEditor;
	private boolean stub = true;

	public Config(){
		try {
			if (System.getProperty("env").equalsIgnoreCase("local")) {
				loadConfig("/files/local_config.yml");
			}
		} catch (Exception ee) {
			System.out.println("SET env Variable to run tests. Tests will be run against the DEVELOP env.");
			System.out.println("*******  Run : run_local.sh : to run tests locally                   ************");
			loadConfig("/files/default_config.yml");
		}
		overrideConfigFromEnvironmentVariables();
	}

	public String getFileuploader() {
		return fileuploader;
	}

	private void loadConfig(String filePath) {
		InputStream input = Config.class.getResourceAsStream(filePath);
		Yaml yaml = new Yaml();
		Map map = (Map) yaml.load(input);
		Map<String, Object> config = (Map<String, Object>) map.get("config");


		if (config.containsKey("dbloader")) {
			dbLoader = (String) config.get("dbloader");
		}
		if (config.containsKey("fileUploader")) {
			fileuploader = (String) config.get("fileUploader");
		}
		if (config.containsKey("file_path")) {
			filepath = (String) config.get("file_path");
		}
		if (config.containsKey("endPoint_real")) {
			datasetEndPointReal = (String) config.get("endPoint_real");
		}
		if (config.containsKey("endPoint_stub")) {
			datasetEndPointStub = (String) config.get("endPoint_stub");
		}
		if (config.containsKey("postgres")) {
			postgres = (String) config.get("postgres");
		}
		if (config.containsKey("jobcreator")) {
			jobCreator = (String) config.get("jobcreator");
		}
		if (config.containsKey("metadata_editor")) {
			metadataEditor = (String) config.get("metadata_editor");
		}
	}

	public void overrideConfigFromEnvironmentVariables() {
		String fileupload_value = System.getProperty("fileupload");
		if (fileupload_value != null) {
			fileuploader = fileupload_value;
		}
		String fileName = System.getProperty("filename");
		if (fileName != null) {
			filepath = fileName;
		}
		String jobCreate = System.getProperty("jobcreator");
		if (jobCreate != null) {
			jobCreator = jobCreate;
		}
		String metadata_editor = System.getProperty("metadataEditor");
		if (metadata_editor != null) {
			metadataEditor = metadata_editor;
		}

	}

	public String getPostgres() {
		return postgres;
	}

	public String getJobCreator() {
		return jobCreator;
	}

	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getDbLoader() {
		return dbLoader;
	}
	public void setDbLoader(String dbLoader) {
		this.dbLoader = dbLoader;
	}

	public String getEndPointReal() {
		return datasetEndPointReal;
	}
	public String getEndPointStub() {
		return datasetEndPointStub;
	}

	public String getMetadataEditor() {
		return metadataEditor;
	}
	public void setDatasetEndPointReal(String datasetEndPoint) {
		this.datasetEndPointReal = datasetEndPoint;
	}
	public void setDatasetEndPointStub(String datasetEndPoint) {
		this.datasetEndPointStub = datasetEndPoint;
	}

	public boolean isBackendStub() {
		return stub;
	}




}
