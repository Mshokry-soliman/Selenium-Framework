package tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import cucumber.api.testng.AbstractTestNGCucumberTests;
import pages.CheckOutPage;
import pages.ComparePage;
import pages.EmailToFriendPage;
import pages.HomePage;
import pages.LoginPage;
import pages.OrderDetailsPage;
import pages.ProductDetailsPage;
import pages.ProductReviewPage;
import pages.SearchPage;
import pages.ShoppingCartPage;
import pages.UserRegisterationPage;

public class TestBase extends AbstractTestNGCucumberTests{

	public static WebDriver driver;
	public static String downloadPath = System.getProperty("user.dir") + "\\Downloads";
	public HomePage homeObject;
	public SearchPage searchObject;
	public ProductDetailsPage productObject;
	public ShoppingCartPage cartObject;
	public UserRegisterationPage regestierObject;
	public LoginPage loginObject;
	public ComparePage compareObject;
	public ProductReviewPage reviewObject;
	public EmailToFriendPage emailObject;
	public CheckOutPage checkoutObject;
	public OrderDetailsPage orderObject;
	String searchTxt = "MacB";

	public static ChromeOptions chromeoption() {

		ChromeOptions options = new ChromeOptions();
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default.content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadPath);
		options.setExperimentalOption("prefs", chromePrefs);
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		return options;
	}

	public static FirefoxOptions firefoxOption() {

		FirefoxOptions option = new FirefoxOptions();
		option.addPreference("browser.download.folderList", 2);
		option.addPreference("browser.download.dir", downloadPath);
		option.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf,application/octet-stream");
		option.addPreference("browser.download.manager.showWhenStarting", false);
		option.addPreference("pdfjs.disabled", true);
		return option;
	}

	@BeforeSuite
	@Parameters({ "browser" })
	public void startDriver(@Optional("chrome") String browserName) {

		if (browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");
			driver = new ChromeDriver(chromeoption());
		}

		else if (browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/drivers/geckodriver.exe");
			driver = new FirefoxDriver(firefoxOption());
		}

		else if (browserName.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "/drivers/IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}

		driver.manage().window().maximize();
		driver.navigate().to("https://demo.nopcommerce.com/");
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
	}

	@BeforeClass
	public void initializationObjects() {
		homeObject = new HomePage(driver);
		searchObject = new SearchPage(driver);
		productObject = new ProductDetailsPage(driver);
		cartObject = new ShoppingCartPage(driver);
		regestierObject = new UserRegisterationPage(driver);
		loginObject = new LoginPage(driver);
		compareObject = new ComparePage(driver);
		reviewObject = new ProductReviewPage(driver);
		emailObject = new EmailToFriendPage(driver);
		checkoutObject = new CheckOutPage(driver);
		orderObject = new OrderDetailsPage(driver);
	}

	@AfterSuite
	public void stopDriver() {
		driver.quit();
	}

	// take screenshot when the test case fail and add it in the screenshot folder
	@AfterMethod
	public void screenshotOnFailure(ITestResult result) throws IOException {

		if (ITestResult.FAILURE == result.getStatus()) {
			System.out.println("Failed");
			System.out.println("Taking Screenshot...");
			TakesScreenshot screenshot = (TakesScreenshot) driver;
			File source = screenshot.getScreenshotAs(OutputType.FILE);
			File destination = new File("./ScreenShots/" + result.getName() + ".png");
			FileUtils.copyFile(source, destination);
			// Helper.captureScreenshot(driver, result.getTestName());
		}
	}
}
