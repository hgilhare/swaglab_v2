package helper;

import com.beust.jcommander.Parameter;
import io.cucumber.java.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import net.rcarz.jiraclient.JiraException;
import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Parameters;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class base {
    public static WebDriver driver;
    public static Properties prop;
    public static Process pro;
    public static ExecutorService executor;






    static {

        FileInputStream file;
        try {
            file = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/resources/data.properties");

            prop = new Properties();

            prop.load(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @BeforeAll
    public static void docker_grid_start() throws IOException, InterruptedException {

        System.out.println("working");





//        String command = "cmd /c start C:\\Users\\lenovo\\IdeaProjects\\swaglab_intellij\\src\\test\\java\\dockerfiles\\start_grid.bat";
//
//        pro = Runtime.getRuntime().exec(command);
//        pro.waitFor();
//        Thread.sleep(10000);
//        // Thread pool for browsers
//
//        executor = Executors.newFixedThreadPool(2);

    }

    @Before
    public void setup() throws MalformedURLException {




        String browserName = prop.getProperty("browser");
        if (browserName.equalsIgnoreCase("chrome")) {
            ChromeOptions options =new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-extensions");
            options.addArguments("--proxy-server='direct://'");
            options.addArguments("--proxy-bypass-list=*");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-infobars");

            driver = new ChromeDriver(options);



        }else if(browserName.equalsIgnoreCase("firefox")) {
            MutableCapabilities capabilities = new MutableCapabilities();
            HashMap<String, Object> bstackOptions = new HashMap<String, Object>();
            capabilities.setCapability("browserName", "Firefox");
            bstackOptions.put("os", "Windows");
            bstackOptions.put("osVersion", "11");
            bstackOptions.put("browserVersion", "latest");
            bstackOptions.put("seleniumVersion", "4.22.0");
            bstackOptions.put("projectName", "Himanshu-Browserstack");
            bstackOptions.put("buildName", "Build v2");
            bstackOptions.put("sessionName", "swaglab");
            bstackOptions.put("debug", "true");
            bstackOptions.put("networkLogs", "true");
            capabilities.setCapability("bstack:options", bstackOptions);
            String url="http://"+"himanshugilhare_v9TRrY"+":"+"uyt1osxQ37RpBemjpBme"+"@hub.browserstack.com/wd/hub/";

            driver = new RemoteWebDriver(new URL(url), capabilities);
        }else if(browserName.equalsIgnoreCase("edge")) {
            driver = new RemoteWebDriver(new URL("http://localhost:4444"), new EdgeOptions());
        }



        driver.get(prop.getProperty("url"));
        driver.manage().window().maximize();

    }

    @AfterAll
    public static void docker_grid_stop() throws IOException, InterruptedException {

        System.out.println("finish");


//        executor.shutdown();
//
//
//
//        String cmd = "cmd /c start C:\\Users\\lenovo\\IdeaProjects\\swaglab_intellij\\src\\test\\java\\dockerfiles\\stop_grid.bat";
//
//        pro = Runtime.getRuntime().exec(cmd);
//        Thread.sleep(5000);
//        String close_cmd = "taskkill /f /im cmd.exe";
//
//        pro = Runtime.getRuntime().exec(close_cmd);

    }

    @After
    public void teardown(Scenario s) throws IOException, InterruptedException, JiraException {
        if (s.isFailed()) {
            TakesScreenshot t = (TakesScreenshot) driver;
            File src = t.getScreenshotAs(OutputType.FILE);
            byte[] src_allure=t.getScreenshotAs(OutputType.BYTES);
//            FileUtils.copyFile(src, new File("screenshots/" + s.getName() + ".png"));
            Allure.addAttachment(s.getName(), new ByteArrayInputStream(src_allure));

            String projectname="HA";
            String issuetype="Bug";
            String token="ATATT3xFfGF0LuaF4jM-NUIGb1UNOGwlMTcLGrSky2y-15sMIvmkjcZ3iirzVs_oZpDJF4-3iqmcjKqexv4drXP6gW8M5xYW81ZcvPLni746Sn1PQio7RWBHShdlojGA2grF9lh1OrlXvfKwIUI26TE1VRxtUsiCAL-NGOozBlEXsLvnmYYwJ1g=023D6515";

            JiraServiceProvider jsp= new JiraServiceProvider("https://himanshu-gilhare-automation.atlassian.net/",
                    "himanshugilhare08@gmail.com",token
            ,projectname);

            String sname=s.getName();
            String issueSummary= sname+ " got failed due to some assertion error or exception";
            String issueDescription= "this Issue has been caught by Automation";
            jsp.CreateJiraTicket(issuetype,issueSummary,issueDescription);

        }

        driver.quit();

    }

    public void selectbyvisibletext(By location, String value) {
        WebElement ele = driver.findElement(location);
        Select s = new Select(ele);
        s.selectByVisibleText(value);
    }

    public void selectbyindex(By location, int value) {
        WebElement ele = driver.findElement(location);
        Select s = new Select(ele);
        s.selectByIndex(value);
    }

    public void selectbyvalue(By location, String value) {
        WebElement ele = driver.findElement(location);
        Select s = new Select(ele);
        s.selectByValue(value);
    }

    public void mousehover(By abc) {
        WebElement ele = driver.findElement(abc);
        Actions a = new Actions(driver);
        a.moveToElement(ele).build().perform();

    }

    public void normalclick(By location) {

        WebElement ele = driver.findElement(location);
        ele.click();
    }

    public void clickbyexecutor(By location) {
        WebElement ele = driver.findElement(location);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", ele);
    }

    public void alertaccepted() {

        Alert a = driver.switchTo().alert();
        a.accept();
    }

    public void alertdismiss() {

        Alert a = driver.switchTo().alert();
        a.dismiss();
    }

    public void alertsendkey(String value) {
        Alert a = driver.switchTo().alert();
        a.sendKeys(value);
        a.accept();

    }

    public WebElement waitforexpectedelement(By by) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void takesnap() throws IOException {

        TakesScreenshot t = (TakesScreenshot) driver;
        File src = t.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src, new File("screenshots/test.png"));

    }


}
