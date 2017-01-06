package com.ons.gov.uk.core;


import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class Config {
	private String splitter;
	private String filepath;
	private String dbLoader;
	private String datasetEndPointReal;
	private String datasetEndPointStub;
	private String postgres;
	private boolean stub = true;

	public Config(){
		loadConfig("/files/local_config.yml");
	}
	private void loadConfig(String filePath) {
		InputStream input = Config.class.getResourceAsStream(filePath);
		Yaml yaml = new Yaml();
		Map map = (Map) yaml.load(input);
		Map<String, Object> config = (Map<String, Object>) map.get("config");

		if (config.containsKey("splitter")) {
			splitter = (String) config.get("splitter");
		}
		if (config.containsKey("dbloader")) {
			dbLoader = (String) config.get("dbloader");
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
		if (config.containsKey("backend")) {
			stub = ((String) config.get("backend")).equalsIgnoreCase("stub");
		}
	}

	public String getPostgres() {
		return postgres;
	}
	public String getSplitter() {
		return splitter;
	}

	public void setSplitter(String splitter) {
		this.splitter = splitter;
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
