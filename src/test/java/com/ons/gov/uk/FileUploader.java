package com.ons.gov.uk;

import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.frontend.pages.BasePage;
import org.openqa.selenium.By;

import java.io.File;

public class FileUploader extends BasePage {
	public static final String OS_NAME = System.getProperty("os.name");
	private static String librariesFolder = "libraries/";

	private By fileUpload = By.id("file");
	private By upload = By.name("submit");



	public void uploadFile() {

		File fileToUpload = new File("src/main/resources/csvs/" + new Config().getFilepath());
		String filePath = fileToUpload.getAbsolutePath();
		System.out.println("File to be uploaded ***  " + filePath);
		getDriver().get(new Config().getFileuploader());
		getDriver().findElement(fileUpload).sendKeys(filePath);
		getDriver().findElement(upload).click();
		getDriver().close();
		getDriver().quit();
	}

}
