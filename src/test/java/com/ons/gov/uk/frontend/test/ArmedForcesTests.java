package com.ons.gov.uk.frontend.test;

import com.ons.gov.uk.frontend.filters.HierarchySelector;
import com.ons.gov.uk.frontend.filters.OptionSelector;
import com.ons.gov.uk.frontend.filters.SummarySelector;
import com.ons.gov.uk.frontend.pages.ArmedForces;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class ArmedForcesTests extends BaseTest {

	private final String armedForcesFile = "AF001EW_v3_E2E_Tests.csv";
	// CHECK FOR THE DATA RESOURCE ON THE DD PAGE
	// EXISTS = TRUE
	// RUN THE TESTS
	// EXISTS = FALSE
	// CHECK IN THE METADATA EDITOR WHETHER THE FILE EXISTS
	// EXISTS = TRUE
	// CHECK FOR THE DATA RESOURCE
	// EXISTS = FALSE
	// UPLOAD THE FILE
	// EXISTS = TRUE
	// LINK THE DATA RESOURCE TO THE DATASET
	// EXISTS = FALSE
	// CREATE THE DATA RESOURCE
	// LINK THE DATA RESOURCE TO THE DATASET
	ArmedForces armedForces = new ArmedForces();
	HierarchySelector hierarchySelector = new HierarchySelector();
	SummarySelector summary = new SummarySelector();
	OptionSelector optionSelector = new OptionSelector();
	ArrayList <String> age = new ArrayList <>();
	ArrayList <String> residence = new ArrayList <>();
	ArrayList <String> sex = new ArrayList <>();
	String armedForcesDS = null, title = null;

	@BeforeTest
	public void init() throws Exception {
		setUpbeforeRun(armedForces.armedForces_dataresource);
	}

	@Test(groups = {"downloadCompleteAF"})
	public void downloadCompleteDS() throws Exception {
		System.out.println("************    Armed Forces  ***********************");
		System.out.println("Starting test.. DownloadComplete");
		checkForDS(armedForces.armedForces_link);
		basePage.click(basePage.download_complete_dataset);
		basePage.selectDownloadCSV(false);
		System.out.println("Finished test.. DownloadComplete");

	}

	@Test(groups = {"openAF"}, dependsOnGroups = {"downloadCompleteAF"})
	public void openArmedForces() throws Exception {
		System.out.println("Starting... openArmedForces");
		checkForDS(armedForces.armedForces_link);
		basePage.click(basePage.customise_data_set);
		System.out.println("openAF");
	}

	@Test(groups = "sex", dependsOnGroups = {"openAF"})
	public void customiseSexFilter() {
		System.out.println("Starting... customise Sex");
		try {
			optionSelector.optionJourney(armedForces.sex_filter);
		} catch (Exception ee) {
			ee.printStackTrace();
			org.testng.Assert.fail();
		}
		System.out.println("customised Sex Filter");
	}

	@Test(groups = "residence", dependsOnGroups = {"sex"})
	public void customiseResidenceType() throws Exception {
		System.out.println("Starting... residence");
		try {
			optionSelector.optionJourney(armedForces.residence_filter);
		} catch (Exception ee) {
			ee.printStackTrace();
			org.testng.Assert.fail();
		}
		System.out.println("customised Residence Filter");
	}

	@Test(groups = "age", dependsOnGroups = {"residence"})
	public void customiseAge() throws Exception {
		System.out.println("Starting... age");
		optionSelector.optionJourney(armedForces.age_filter);
		System.out.println("customised Age Filter");
	}

	@Test(groups = "geographyAF", dependsOnGroups = {"age"})
	public void customiseGeo() throws Exception {
		System.out.println("Starting... geographyAF");
		hierarchySelector.simpleGeoJourney(armedForces.geo_filter, armedForces.geo_search_text, true);
		System.out.println("customised Geo");
	}

	@Test(groups = {"getOptionsAF"}, dependsOnGroups = {"geographyAF"})
	public void getSelectedOptions() {
		System.out.println("Starting... getOptions");
		age = summary.selectedOptions(armedForces.age_filter, false);
		residence = summary.selectedOptions(armedForces.residence_filter, false);
		sex = summary.selectedOptions(armedForces.sex_filter, false);
		System.out.println("getOptions");
	}

	@Test(groups = {"customiseCSVAF"}, dependsOnGroups = {"getOptionsAF"})
	public void downloadCustomisedDS_CSV() {
		System.out.println("Starting... customiseCSVAF");
		basePage.selectDownloadCSV(true);
		basePage.checkDownloadedFile(age, armedForces.age_filter, false);
		basePage.checkDownloadedFile(residence, armedForces.residence_filter, false);
		basePage.checkDownloadedFile(sex, armedForces.sex_filter, false);
		System.out.println("customiseCSV");
		System.out.println("************  Completed Armed Forces Tests*************");
	}

}
