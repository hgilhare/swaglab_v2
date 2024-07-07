package helper;

import com.beust.jcommander.Parameter;
import io.cucumber.java.*;
import io.github.bonigarcia.wdm.WebDriverManager;
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
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
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





        String command = "cmd /c start C:\\Users\\lenovo\\IdeaProjects\\swaglab_intellij\\src\\test\\java\\dockerfiles\\start_grid.bat";

        pro = Runtime.getRuntime().exec(command);
        pro.waitFor();
        Thread.sleep(10000);
        // Thread pool for browsers

        executor = Executors.newFixedThreadPool(2);

    }

    @Before
    public void setup() throws MalformedURLException {




        String browserName = prop.getProperty("browser");
        if (browserName.equalsIgnoreCase("chrome")) {

            driver = new RemoteWebDriver(new URL("http://localhost:4444"), new ChromeOptions());

        }else if(browserName.equalsIgnoreCase("firefox")) {
            driver = new RemoteWebDriver(new URL("http://localhost:4444"), new FirefoxOptions());
        }else if(browserName.equalsIgnoreCase("edge")) {
            driver = new RemoteWebDriver(new URL("http://localhost:4444"), new EdgeOptions());
        }



        driver.get(prop.getProperty("url"));
        driver.manage().window().maximize();

    }

    @AfterAll
    public static void docker_grid_stop() throws IOException, InterruptedException {


        executor.shutdown();



        String cmd = "cmd /c start C:\\Users\\lenovo\\IdeaProjects\\swaglab_intellij\\src\\test\\java\\dockerfiles\\stop_grid.bat";

        pro = Runtime.getRuntime().exec(cmd);
        Thread.sleep(5000);
        String close_cmd = "taskkill /f /im cmd.exe";

        pro = Runtime.getRuntime().exec(close_cmd);

    }

    @After
    public void teardown(Scenario s) throws IOException, InterruptedException {
        if (s.isFailed()) {
            TakesScreenshot t = (TakesScreenshot) driver;
            File src = t.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File("screenshots/" + s.getName() + ".png"));

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
