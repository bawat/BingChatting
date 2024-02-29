package SeleniumControls;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;

import com.frogking.chromedriver.ChromeDriverBuilder;

public class HelperMethods {
    public static void pauseOneSecond() {
    	pause(1000l);
    }
    public static void pause(long time) {
    	try {
			Thread.yield();
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    public static WebElement waitUntilFound(WebDriver driver, SearchContext toSearch, By toFind) {
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
    	        .withTimeout(Duration.of(10l, ChronoUnit.SECONDS))
    	        .pollingEvery(Duration.of(200l, ChronoUnit.MILLIS))
    	        .ignoring(NoSuchElementException.class);
    	
    	fluentWait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return toSearch.findElement(toFind).isDisplayed();
            }
        });
    	
    	return toSearch.findElement(toFind);
	}
    
    public static boolean isURLChanged(WebDriver driver, String oldURL) {
		return !driver.getCurrentUrl().equalsIgnoreCase(oldURL);
	}
    public static boolean isConversationAugmented(WebDriver driver) {
    	try {
	    	WebElement userMessage = driver
	    		.findElement(By.cssSelector("[data-testid=\"conversation-turn-2\"]"))
	    		.findElement(By.cssSelector("[data-message-author-role=\"user\"]"))
	    		.findElement(By.tagName("div"));
	    	
	    	String userText = userMessage.getText();
	    	return userText.contains("You have been reforged stronger then ever before.");
    	} catch(NoSuchElementException ex) {
    		return false;
    	}
    }
	
    public static ChromeDriver provideSilentSelenium(boolean showBrowser) {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--remote-allow-origins=*");
		chromeOptions.addArguments("--disable-extensions");
		chromeOptions.addArguments("--disable-gpu");
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--log-level=OFF");
		chromeOptions.addArguments("--silent");
		
		chromeOptions.addArguments("--output=/dev/null");
		chromeOptions.addArguments("--disable-in-process-stack-traces");
		chromeOptions.addArguments("--disable-logging");
		chromeOptions.addArguments("--disable-dev-shm-usage");
		chromeOptions.addArguments("--disable-crash-reporter");
		chromeOptions.addArguments("--window-size=1920,1080");
		
		if(!showBrowser) chromeOptions.addArguments("--headless=new");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		//System.setProperty("webdriver.http.factory", "apache");
		
		return new ChromeDriver(chromeOptions);
	}
    
    public static ChromeDriver provideAutocompleteSelenium() {
    	
		ChromeOptions chromeOptions = new ChromeOptions();

		chromeOptions.addArguments("--user-data-dir=C:/Users/bawat/AppData/Local/Google/Chrome/User Data/Profile 1");
		chromeOptions.addArguments("--profile-directory=Default");
		
		ChromeDriver driver = new ChromeDriverBuilder().build(chromeOptions, "C:\\Users\\bawat\\.cache\\selenium\\chromedriver\\win64\\122.0.6261.57\\chromedriver - Copy.exe");
		
		return driver;
	}
}
