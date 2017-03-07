package com.ons.gov.uk;

import com.ons.gov.uk.core.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;

public class FileUploader {
	public static final String OS_NAME = System.getProperty("os.name");
	private static String librariesFolder = "libraries/";
	WebDriver driver;
	private By fileUpload = By.id("file");
	private By upload = By.name("submit");


	public static String getChromeDriverFileLocation() {
		if (OS_NAME.toLowerCase().contains("mac os x")) {
			return librariesFolder + "chromedriver/" + "chromedriver_mac";
		} else if (OS_NAME.toLowerCase().contains("windows")) {
			return librariesFolder + "chromedriver/" + "chromedriver_win.exe";
		} else {
			String arch = System.getProperty("os.arch");
			if (arch.contains("64")) {
				return librariesFolder + "chromedriver/" + "chromedriver_linux_64";
			} else {
				return librariesFolder + "chromedriver/" + "chromedriver_linux_32";
			}
		}
	}

	public void setDriver() {
		File file = new File(getChromeDriverFileLocation());
		if (file.exists()) {
			file.setExecutable(true);
		}

		try {
			System.setProperty("webdriver.chrome.driver", file.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		DesiredCapabilities desiredCapabilitiesChrome = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized", "--silent");
		desiredCapabilitiesChrome.setCapability(ChromeOptions.CAPABILITY, options);
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	public void uploadFile() {
		setDriver();
		File fileToUpload = new File("src/main/resources/csvs/" + new Config().getFilepath());
		String filePath = fileToUpload.getAbsolutePath();
		System.out.println("File to be uploaded ***  " + filePath);
		driver.get(new Config().getFileuploader());
		driver.findElement(fileUpload).sendKeys(filePath);
		driver.findElement(upload).click();
		driver.close();
		driver.quit();
	}

}
