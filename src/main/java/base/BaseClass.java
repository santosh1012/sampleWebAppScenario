package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeMethod;
import utility.PropertyFileOperation;

import java.time.Duration;

public class BaseClass {



protected WebDriver driver;
    @BeforeMethod
    public void launchBrowser(){
        String browserToLaunch;
        System.out.println(System.getProperty("browser"));
        if(System.getProperty("browser")==null){
            browserToLaunch=PropertyFileOperation.getPropertyValue("browser");
        }
        else
            browserToLaunch=System.getProperty("browser");

        launchInstance(browserToLaunch);
        driver.get(PropertyFileOperation.getPropertyValue("Url"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    private void launchInstance(String browserToLaunch) {
        if(browserToLaunch.equalsIgnoreCase("Chrome")){
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }
        else if(browserToLaunch.equalsIgnoreCase("Firefox")){
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }
    }
}
