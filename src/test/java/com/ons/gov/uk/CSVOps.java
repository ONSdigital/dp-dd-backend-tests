package com.ons.gov.uk;


import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class CSVOps {

	int numberOfLines = 0;
	CSVReader csvReader = null;
	HashMap <String, ArrayList <String>> dimAndOptions = new HashMap <String, ArrayList <String>>();
	ArrayList <String> dimension1 = new ArrayList <String>();
	ArrayList <String> dimension2 = new ArrayList <String>();
	ArrayList <String> dimension3 = new ArrayList <String>();

	public static void main(String[] args) throws Exception {
		CSVOps cv = new CSVOps();
		cv.populateDimensionFilters("Open-Data-new-format.csv");
	}

	public HashMap <String, ArrayList <String>> getDimOptionsFromCSV() {
		return dimAndOptions;
	}

	public void readCSV(String file) {

		String localFile = "src/main/resources/csvs/" + file;

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
		String localFile = "src/main/resources/csvs/" + file;
		String[] nextLine;
		String filter1 = null, filter2 = null, filter3 = null;
		csvReader = new CSVReader(new FileReader(localFile));
		while ((nextLine = csvReader.readNext()) != null) {
			if (!nextLine[0].contains("***")) {
				filter1 = nextLine[10];
				filter2 = nextLine[12];
				try {
					filter3 = nextLine[14];
				} catch (Exception ee) {
				}
				if (!nextLine[10].contains("Dimension_1")) {
					if (!dimension1.contains(nextLine[11])) {
						dimension1.add(nextLine[11]);
					}
					if (!dimension2.contains(nextLine[13])) {
						dimension2.add(nextLine[13]);
					}
					try {
						if (!dimension3.contains(nextLine[15])) {
							dimension3.add(nextLine[15]);
						}
					} catch (Exception ee) {
					}
				}
			}


		}
		dimAndOptions.put(filter1, dimension1);
		dimAndOptions.put(filter2, dimension2);
		try {
			if (filter3 != null) {
				dimAndOptions.put(filter3, dimension3);
			}

		} catch (Exception ee) {
		}

		cleanUpHashMap();
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