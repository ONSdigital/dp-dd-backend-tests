package com.ons.gov.uk.core.model;

public class DatasetMetadata {

	private String jsonMetadata;
	private String datasetId;
	private String majorVersion;
	private String minorVersion;
	private String revisionNotes;
	private String revisionReason;

	public String getRevisionNotes() {
		return revisionNotes;
	}

	public DatasetMetadata setRevisionNotes(String revisionNotes) {
		this.revisionNotes = revisionNotes;
		return this;
	}

	public String getRevisionReason() {
		return revisionReason;
	}

	public DatasetMetadata setRevisionReason(String revisionReason) {
		this.revisionReason = revisionReason;
		return this;
	}

	public String getJsonMetadata() {
		return jsonMetadata;
	}

	public DatasetMetadata setJsonMetadata(String jsonMetadata) {
		this.jsonMetadata = jsonMetadata;
		return this;
	}

	public String getDatasetId() {
		return datasetId;
	}

	public DatasetMetadata setDatasetId(String datasetId) {
		this.datasetId = datasetId;
		return this;
	}

	public String getMajorVersion() {
		return majorVersion;
	}

	public DatasetMetadata setMajorVersion(String majorVersion) {
		this.majorVersion = majorVersion;
		return this;
	}

	public String getMinorVersion() {
		return minorVersion;
	}

	public DatasetMetadata setMinorVersion(String minorVersion) {
		this.minorVersion = minorVersion;
		return this;
	}


}
