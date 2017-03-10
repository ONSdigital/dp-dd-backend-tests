package com.ons.gov.uk.frontend.pages;


import org.openqa.selenium.By;

import java.io.File;

public class FileUploader extends BasePage {
	private By fileUpload = By.id("file");
	private By upload = By.name("submit");

	public void uploadFile(String title) {
		File fileToUpload = new File("src/main/resources/csvs/" + title);
		String filePath = fileToUpload.getAbsolutePath();
		System.out.println("File to be uploaded ***  " + filePath);
		navigateToUrl(getConfig().getFileuploader());
		getElement(fileUpload).sendKeys(filePath);
		getElement(upload).click();
		getDriver().close();
	}

}
