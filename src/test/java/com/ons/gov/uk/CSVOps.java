package com.ons.gov.uk;


import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class CSVOps {

	public HashMap <String, ArrayList <DimensionValues>> dimAndOptions = new HashMap <String, ArrayList <DimensionValues>>();
	int numberOfLines = 0;
	CSVReader csvReader = null;
	ArrayList <DimensionValues> dimension1 = new ArrayList <DimensionValues>();
	ArrayList <DimensionValues> dimension2 = new ArrayList <DimensionValues>();
	ArrayList <DimensionValues> dimension3 = new ArrayList <DimensionValues>();
	ArrayList <DimensionValues> dimension4 = new ArrayList <DimensionValues>();
	boolean hierarchy = false;
	DimensionValues dimensionValues;

	public HashMap <String, ArrayList <DimensionValues>> getDimOptionsFromCSV() {
		return dimAndOptions;
	}

	public void readCSV(String file) {

		String localFile = "src/test/resources/csvs/" + file;

		try {
			csvReader = new CSVReader(new FileReader(localFile));
			String[] nextLine;
			while ((nextLine = csvReader.readNext()) != null) {
				if (!nextLine[0].contains("***")) {
					numberOfLines++;
				}
			}
			csvReader.close();
		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public int returnRows(String fileName) {
		int headers = 1;
		readCSV(fileName);
		return numberOfLines - headers;
	}

	public void populateDimensionFilters(String file) throws IOException {
		String localFile = "src/test/resources/csvs/" + file;
		String[] nextLine;
		String filter1 = null, filter2 = null, filter3 = null, filter4 = null;
		String dimName1 = null, dimName2 = null, dimName3 = null, dimName4 = null;
		csvReader = new CSVReader(new FileReader(localFile));
		while ((nextLine = csvReader.readNext()) != null) {
			if (!nextLine[0].contains("***")) {
				filter1 = nextLine[3];
				dimName1 = nextLine[4];
				filter2 = nextLine[6];
				dimName2 = nextLine[7];

				try {
					filter3 = nextLine[9];
					filter4 = nextLine[12];
				} catch (Exception ee) {
				}
				resetHierarchy();
				if (!nextLine[4].contains("Dimension_Name_1")) {
					if (!filter1.equals("")) {
						hierarchy = true;
					}
					dimensionValues = new DimensionValues(hierarchy, filter1, nextLine[5]);
					addDimension(dimensionValues, dimension1);
					resetHierarchy();
					if (!filter2.equals("")) {
						hierarchy = true;
					}
					dimensionValues = new DimensionValues(hierarchy, filter2, nextLine[8]);
					addDimension(dimensionValues, dimension2);
					try {
						resetHierarchy();
						if (!filter3.equals("")) {
							hierarchy = true;
						}
						dimName3 = nextLine[10];
						dimensionValues = new DimensionValues(hierarchy, filter3, nextLine[11]);
						addDimension(dimensionValues, dimension3);
						resetHierarchy();
						if (!filter4.equals("")) {
							hierarchy = true;
						}
						dimName4 = nextLine[13];
						dimensionValues = new DimensionValues(hierarchy, filter4, nextLine[14]);
						addDimension(dimensionValues, dimension4);

					} catch (Exception ee) {
					}
				}
			}


		}
		dimAndOptions.put(dimName1, dimension1);
		dimAndOptions.put(dimName2, dimension2);
		try {
			if (filter3 != null) {
				dimAndOptions.put(dimName3, dimension3);
			}
			if (filter4 != null) {
				dimAndOptions.put(dimName4, dimension4);
			}

		} catch (Exception ee) {
		}

		cleanUpHashMap();
	}

	public void resetHierarchy() {
		hierarchy = false;
	}

	public void addDimension(DimensionValues dimensionValues, ArrayList <DimensionValues> dimensions) {
		if (dimensions.size() == 0) {
			dimensions.add(dimensionValues);
		} else {
			boolean exists = false;
			for (int index = 0; index < dimensions.size(); index++) {

				if (dimensions.get(index).getCodeId().equals(dimensionValues.getCodeId())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				dimensions.add(dimensionValues);
			}
			hierarchy = false;
		}
	}
	public void cleanUpHashMap() {
		Set <String> keySet = dimAndOptions.keySet();
		for (String key : keySet) {
			if (key == null) {
				keySet.remove(dimAndOptions);
			}
		}
	}


}