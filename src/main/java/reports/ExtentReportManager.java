package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance("test-output/ExtentReport.html");
        }
        return extent;
    }

    public static ExtentReports createInstance(String fileName) {

        ExtentSparkReporter spark = new ExtentSparkReporter(fileName);

        // ---- UI CONFIG IMPROVED ----
        spark.config().setTheme(Theme.STANDARD);   // you can change to DARK later
        spark.config().setDocumentTitle("Automation Test Report");
        spark.config().setReportName("NSE Stock Info Test Report");
        spark.config().setEncoding("UTF-8");
        spark.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        // ---- SYSTEM INFO ----
        extent.setSystemInfo("Project", "NSE Automation");
        extent.setSystemInfo("Tester", "Hayat Basha");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Browsers", "Chrome | Firefox | Edge");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));

        return extent;
    }
}
