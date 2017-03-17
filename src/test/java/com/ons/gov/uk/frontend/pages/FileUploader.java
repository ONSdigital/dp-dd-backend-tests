package com.ons.gov.uk.frontend.pages;


import org.openqa.selenium.By;
import org.testng.Assert;

import java.io.File;

public class FileUploader extends BasePage {
	private By fileUpload = By.id("file");
	private By upload = By.name("submit");

	public void uploadFile(String title) throws Exception {
		File fileToUpload = new File("src/test/resources/csvs/" + title);
		String filePath = fileToUpload.getAbsolutePath();
		System.out.println("File to be uploaded ***  " + filePath);
		Thread.sleep(2000);
		navigateToUrl(getConfig().getFileuploader());
		Assert.assertTrue(isElementPresent(fileUpload), " Not able to find the element to upload the file");
		getElement(fileUpload).sendKeys(filePath);
		getElement(upload).click();

	}

}
