package com.ons.gov.uk.core;


import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class Config {
	private String splitter;
	private String filepath;
	private String dbLoader;
	private String datasetEndPoint;
	private String postgres;

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
		if (config.containsKey("endPoint")) {
			datasetEndPoint = (String) config.get("endPoint");
		}
		if (config.containsKey("postgres")) {
			postgres = (String) config.get("postgres");
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

	public String getDatasetEndPoint() {
		return datasetEndPoint;
	}

	public void setDatasetEndPoint(String datasetEndPoint) {
		this.datasetEndPoint = datasetEndPoint;
	}




}
