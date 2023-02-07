package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import utility.FolderCreationUtility;
import utility.GetScreenshot;
import utility.PropertyFileOperation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class BaseClass {



protected WebDriver driver;


    public PropertyFileOperation propertyFileOperation;
    public PropertyFileOperation dataVerificationFile;

    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extent;
    public ExtentTest extentTest;
    public String environment ;
    public String executionBrowser;

    @BeforeSuite
    public void reportingFolderCreation() {
        FolderCreationUtility.createFolderIfNotExist(
                System.getProperty("user.dir") + "/ExtentReports/Script");

    }


    @BeforeTest
    public void startReport() throws UnknownHostException {

        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/ExtentReports/Script/report.html");
        System.out.println("Reporting path:----------"+htmlReporter);
        // Create an object of Extent Reports
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Host Name", InetAddress.getLocalHost().getHostName());
        extent.setSystemInfo("Environment", environment);
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        htmlReporter.config().setDocumentTitle("ExtentReport for Sample");
        // Name of the report
        htmlReporter.config().setReportName("Execution Report");
        // Dark Theme
        htmlReporter.config().setTheme(Theme.STANDARD);

    }
//    @BeforeMethod()
//    public void a(Method method)
//    {
//
//        String browser=null;
//        if(System.getProperty("browserName")!=null){
//            browser=System.getProperty("browserName");
//        }else
//            browser = propertyFileOperation.getPropertyValue("browserName");
//        WebDriver driverInstance = bf.launchBrowser(browser,propertyFileOperation);
//        DriverFactory.getInstance().setDriver(driverInstance);
//        driver = DriverFactory.getInstance().getDriver();
//        if(environment.equalsIgnoreCase("SWISS"))
//            driver.get(propertyFileOperation.getPropertyValue("SWISS_URL"));
//        else if(environment.equalsIgnoreCase("US"))
//            driver.get(propertyFileOperation.getPropertyValue("US_URL"));
//        extentTest = extent.createTest(method.getName());
//        ExtentFactory.getInstance().setExtent(extentTest);
//    }



    @AfterMethod
    public void tearDown(ITestResult result, Method m) throws IOException {
        System.out.println(result.getStatus());
        if (result.getStatus() == ITestResult.FAILURE) {
            // MarkupHelper is used to display the output in different colors
            extentTest.log(Status.FAIL,
                    MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
            extentTest.log(Status.FAIL,
                    MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
            // To capture screenshot path and store the path of the screenshot in the string
            // "screenshotPath"
            // We do pass the path captured by this method in to the extent reports using
            // "logger.addScreenCapture" method.
            // String Scrnshot=TakeScreenshot.captuerScreenshot(driver,"TestCaseFailed");
            String screenshotPath = GetScreenshot.getScreenShot(driver, result.getName());
            // To add it in the extent report
            extentTest.fail("Test Case Failed Snapshot is below " + extentTest.addScreenCaptureFromPath(screenshotPath));
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.log(Status.SKIP,
                    MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(Status.PASS,
                    MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
        }

        System.out.println("--------------------------------------End:- " + m.getName()
                + "----------------------------------------- ");
        System.out.println();
        extent.flush();
//        DriverFactory.getInstance().removeDriver();
    }



    @BeforeMethod
    public void launchBrowser(Method method){
        String browserToLaunch;
        System.out.println(System.getProperty("browser"));
        if(System.getProperty("browser")==null){
            browserToLaunch=PropertyFileOperation.getPropertyValue("browser");
        }
        else
            browserToLaunch=System.getProperty("browser");

        launchInstance(browserToLaunch);
        driver.get(PropertyFileOperation.getPropertyValue("Url"));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        extentTest = extent.createTest(method.getName());
        ExtentFactory.getInstance().setExtent(extentTest);
    }

    private void launchInstance(String browserToLaunch) {
        if(browserToLaunch.equalsIgnoreCase("Chrome")){
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }
        else if(browserToLaunch.equalsIgnoreCase("Firefox")){
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browserToLaunch.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver =  new EdgeDriver();
        }
    }

    public static String nameWithDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh-mm-ss");
        String format = dateFormat.format(date);
        return format;
    }
}
