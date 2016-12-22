package com.ons.gov.uk;




import com.ons.gov.uk.core.Config;
import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class CSVOps {

	public int returnRows(String fileName) {
		int numberOfLines = 0;
		int headers = 1;
		String localFile = "src/main/resources/csvs/" + fileName;
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader(localFile));
			String[] nextLine;
			while ((nextLine = csvReader.readNext()) != null) {
				if (!nextLine[0].contains("***")) {
					numberOfLines++;
				}
			}
		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return numberOfLines-headers;
	}


}
