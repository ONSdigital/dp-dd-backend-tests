package com.ons.gov.uk.frontend.test;


import com.ons.gov.uk.DimensionValues;
import com.ons.gov.uk.frontend.pages.BasePage;
import org.testng.Assert;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class FileChecker {

	ArrayList <String[]> linesToRemove = new ArrayList <>();
	ArrayList <String> searchRegex = new ArrayList <>();

	public static void main(String[] args) {
		BasePage bp = new BasePage();
		ArrayList <String> prod = new ArrayList <>();
		ArrayList <String> test = new ArrayList <>();
		prod.add("1103 - Manufacture of cider and other fruit wines");
		prod.add("1200 - Manufacture of tobacco products");
		FileChecker ff = new FileChecker();
		bp.checkFile(prod, "NACE", true);
	}

	public void getFile(String url, String fileName) throws Exception {
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

	public ArrayList <String> searchTerms(boolean hierarchy, String code, String key) {
		String searchTerm = hierarchy ?
				"(.*)" + key + "," + "(.*)" + "," + code + "(.*)" : "(.*)" + key + "," + code + "(.*)";
		searchRegex.add(searchTerm);
		return searchRegex;

	}

	public void printMismatch(ArrayList <String[]> allLines) {
		if (allLines.size() != linesToRemove.size()) {
			for (String[] strArr : allLines) {
				for (int index = 0; index < strArr.length; index++) {
					System.out.print(strArr[index] + ",");
				}
				System.out.println();
			}

		}
	}

	public ArrayList <String> searchTerms(String hierarchy, String code, String key) {
		String searchTerm = (!hierarchy.equals("")) ?
				"(.*)" + key + "," + hierarchy + "," + code + "(.*)" : "(.*)" + key + "," + code + "(.*)";
		searchRegex.add(searchTerm);
		return searchRegex;

	}

	public boolean checkForFilter(ArrayList <DimensionValues> dimFiler, String key, String fileName, ArrayList <String[]> allLines) throws Exception {

		for (DimensionValues filter : dimFiler) {
			searchTerms(filter.getHierarchyValue(), filter.getCodeId(), key);
		}
		boolean exists = validateFile(allLines);
		Assert.assertTrue(allLines.size() == linesToRemove.size(), "Mismatch between the filter and the downloaded CSV");
		return exists;

	}

	public boolean checkForFilter(ArrayList <String> dimFiler, String key, String fileName, boolean hierarchy, ArrayList <String[]> allLines)
			throws Exception {
		if (linesToRemove.size() > 0) {
			linesToRemove.removeAll(allLines);
		}
		linesToRemove.clear();
		boolean exists = false;
		for (String filter : dimFiler) {
			searchTerms(hierarchy, filter, key);
		}
		Assert.assertTrue(validateFile(allLines), "All the search options do not exist in the filtered file");
		printMismatch(allLines);
		return exists;

	}

	public ArrayList <String[]> getLinesToRemove() {
		return linesToRemove;
	}

	public boolean validateFile(ArrayList <String[]> allLines) {
		boolean exists = false;
		if (allLines.size() > 0) {
			for (String[] strArr : allLines) {
				String temp = "";
				for (int index = 0; index < strArr.length; index++) {
					temp += strArr[index] + ",";
				}

				for (String search : searchRegex) {
					if (temp.matches(search)) {
						if (!linesToRemove.contains(strArr)) {
							linesToRemove.add(strArr);
							exists = true;
						}
					}

				}
			}
		}
		return exists;

	}
}
