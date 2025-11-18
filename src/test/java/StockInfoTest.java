import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.NseHomePage;
import utils.ExcelUtil;
import utils.LoggerUtil;

public class StockInfoTest extends BaseTest {

    @Test(dataProvider = "excelData")
    public void verifyStockInformation(String stockName, String expectedHigh, String expectedLow) throws InterruptedException {

        LoggerUtil.info("--------------------------------------------------------");
        LoggerUtil.info("🔍 Starting validation for stock: " + stockName);
        LoggerUtil.info("--------------------------------------------------------");

        NseHomePage nsePage = new NseHomePage(getDriver());

        LoggerUtil.info("🌐 Opening NSE website...");
        nsePage.openSite();

        LoggerUtil.info("🔎 Searching for stock: " + stockName);
        nsePage.searchStock(stockName);

        LoggerUtil.info("📥 Fetching 52-week high & low values from UI...");

        String uiHigh = nsePage.get52WeekHigh().replace(",", "").trim();
        String uiLow  = nsePage.get52WeekLow().replace(",", "").trim();

        double uiHighVal = Double.parseDouble(uiHigh);
        double uiLowVal  = Double.parseDouble(uiLow);

        LoggerUtil.info("📊 UI Values → High: " + uiHighVal + " | Low: " + uiLowVal);

        double expectedHighVal = Double.parseDouble(expectedHigh.replace(",", "").trim());
        double expectedLowVal  = Double.parseDouble(expectedLow.replace(",", "").trim());

        LoggerUtil.info("📘 Excel Values → Expected High: " + expectedHighVal + " | Expected Low: " + expectedLowVal);

        // --- Assertion Logging ---
        if (uiHighVal != expectedHighVal) {
            LoggerUtil.error("❌ HIGH value mismatch for " + stockName +
                    " | Expected: " + expectedHighVal + " | Actual: " + uiHighVal);
        } else {
            LoggerUtil.info("✅ HIGH value matched: " + uiHighVal);
        }

        if (uiLowVal != expectedLowVal) {
            LoggerUtil.error("❌ LOW value mismatch for " + stockName +
                    " | Expected: " + expectedLowVal + " | Actual: " + uiLowVal);
        } else {
            LoggerUtil.info("✅ LOW value matched: " + uiLowVal);
        }

        // --- Hard Assertions ---
        Assert.assertEquals(uiHighVal, expectedHighVal, "52 Week HIGH mismatch for: " + stockName);
        Assert.assertEquals(uiLowVal, expectedLowVal, "52 Week LOW mismatch for: " + stockName);

        LoggerUtil.info("🎉 Validation completed successfully for: " + stockName);
        LoggerUtil.info("--------------------------------------------------------");
    }

    @DataProvider(name = "excelData", parallel = true)
    public Object[][] getStockDataFromExcel() {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/StockData.xlsx";
        return ExcelUtil.getStockData(filePath, "Sheet1");
    }
}
