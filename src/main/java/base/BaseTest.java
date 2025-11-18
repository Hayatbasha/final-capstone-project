package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;

import org.apache.commons.io.FileUtils;
import utils.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

public class BaseTest {

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driver.get();
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional String browserParam, Method method, Object[] data) {

        String browser = (browserParam == null || browserParam.isEmpty())
                ? ConfigReader.get("browser")           // read from config
                : browserParam;                         // read from XML if passed

        boolean headless = ConfigReader.getBoolean("headless");
        int implicitWait = ConfigReader.getInt("implicitWait");

        WebDriver webDriver;

        System.out.println("\n======================================================");
        System.out.println("🚀 Starting Test: " + method.getName() + " | Data: " + Arrays.toString(data));
        System.out.println("Browser in Use: " + browser.toUpperCase());
        System.out.println("Headless Mode: " + headless);
        System.out.println("======================================================");

        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver();
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver();
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();

                if (headless) {
                    options.addArguments("--headless=new");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--window-size=1920,1080");
                }

                // CI environments
                if (System.getenv("CI") != null) {
                    options.addArguments("--headless=new");
                    options.addArguments("--disable-dev-shm-usage");
                }

                webDriver = new ChromeDriver(options);
                break;
        }

        driver.set(webDriver);
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        System.out.println("======================================================");
        System.out.println("✅ Completed Test: " + result.getMethod().getMethodName() +
                " | Status: " + (result.isSuccess() ? "PASSED" : "FAILED"));
        System.out.println("======================================================\n");

        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }

    public String takeScreenshot(String fileName) {
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String destPath = System.getProperty("user.dir") + "/screenshots/" + fileName + ".png";
        try {
            FileUtils.copyFile(srcFile, new File(destPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destPath;
    }
}
