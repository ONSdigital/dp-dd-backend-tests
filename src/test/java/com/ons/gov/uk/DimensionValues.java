package com.ons.gov.uk;

public class DimensionValues {
	private boolean hierarchy;
	private String codeId;
	private String hierarchyValue;

	public DimensionValues(boolean hierarchy, String hierarchyValue, String codeId) {
		setHierarchy(hierarchy);
		setCodeId(codeId);
		setHierarchyValue(hierarchyValue);
	}

	public String getHierarchyValue() {
		return hierarchyValue;
	}

	public void setHierarchyValue(String hierarchyValue) {
		this.hierarchyValue = hierarchyValue;
	}

	public boolean isHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(boolean hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

}
