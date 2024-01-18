/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package BingChatLib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Library {
	
	public static void main(String[] args) {
		System.out.println("Response: " + getResponse("How to milk a cow"));
	}
	
	private static WebDriver driver;
    public static String getResponse(String request) {
		driver = new ChromeDriver(setupChromeDriver());
		
		/*
		driver.get("https://www.bing.com/search?q=Bing+AI&showconv=1&FORM=hpcodx");
		
		try {
			waitUntilFound(driver, By.cssSelector("#bnp_btn_accept")).click();
		} catch (TimeoutException e) {
			
		}
		
		SearchContext shadowRoot1 = waitUntilFound(driver, By.cssSelector(".cib-serp-main")).getShadowRoot();
		SearchContext shadowRoot2 = waitUntilFound(shadowRoot1, By.cssSelector("#cib-action-bar-main")).getShadowRoot();
		SearchContext shadowRoot3 = waitUntilFound(shadowRoot2, By.cssSelector("cib-text-input")).getShadowRoot();
		WebElement searchBox = waitUntilFound(shadowRoot3, By.cssSelector("#searchbox"));
		
		waitOneSecond();
		
		new Actions(driver).sendKeys(searchBox, request, Keys.chord(Keys.ENTER)).build().perform();
		*/
		try {
			driver.get("https://www.bing.com/search?showconv=1&sendquery=1&q=" + URLEncoder.encode(request, StandardCharsets.UTF_8.toString()));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SearchContext shadowRoot1 = waitUntilFound(driver, By.cssSelector(".cib-serp-main")).getShadowRoot();
		SearchContext shadowRoot2 = waitUntilFound(shadowRoot1, By.cssSelector("#cib-action-bar-main")).getShadowRoot();
		SearchContext shadowRoot3 = waitUntilFound(shadowRoot2, By.cssSelector("cib-text-input")).getShadowRoot();
		WebElement searchBox = waitUntilFound(shadowRoot3, By.cssSelector("#searchbox"));
		
		waitUntilBingHasFinishedPrinting(shadowRoot2);
		
		
		SearchContext shadowRootConversation;
		SearchContext shadowRootChatTurn;
		SearchContext shadowRootBotResponse;
		SearchContext shadowRootBotMessageBox;
		WebElement response;
		
		//Repeatedly redo the tree search process, just in case the elements get stale be them being regenerated by Bing
		while(true) {
			try {
				shadowRootConversation = waitUntilFound(shadowRoot1, By.cssSelector("#cib-conversation-main")).getShadowRoot();
				shadowRootChatTurn = waitUntilFound(shadowRootConversation, By.cssSelector("cib-chat-turn")).getShadowRoot();
				shadowRootBotResponse = waitUntilFound(shadowRootChatTurn, By.cssSelector("cib-message-group[source=\"bot\"]")).getShadowRoot();
				shadowRootBotMessageBox = waitUntilFound(shadowRootBotResponse, By.cssSelector("cib-message[type=\"text\"]")).getShadowRoot();
				
				response = waitUntilFound(shadowRootBotMessageBox, By.cssSelector(".ac-textBlock"));
				
				break;
			} catch(StaleElementReferenceException e) {
			}

			waitOneSecond();
			continue;
		}
		
		String responseText = response.getText();
        driver.quit();
        
        return responseText;
    }
    
    private static void waitOneSecond() {
    	try {
			Thread.yield();
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void waitUntilBingHasFinishedPrinting(SearchContext shadowRoot) {
    	SearchContext shadowRootTypingIndicator = waitUntilFound(shadowRoot, By.cssSelector("cib-typing-indicator")).getShadowRoot();
		WebElement stopTypingButton = waitUntilFound(shadowRootTypingIndicator, By.cssSelector("#stop-responding-button"));
		
		while(!"true".equalsIgnoreCase(stopTypingButton.getAttribute("disabled"))) {
			try {
				Thread.yield();
				Thread.sleep(1000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static WebElement waitUntilFound(SearchContext toSearch, By toFind) {
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
	
	private static ChromeOptions setupChromeDriver() {
		WebDriverManager.chromedriver().setup();
		
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
		
		//chromeOptions.addArguments("--headless=new");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		//System.setProperty("webdriver.http.factory", "apache");
		
		return chromeOptions;
	}
}


