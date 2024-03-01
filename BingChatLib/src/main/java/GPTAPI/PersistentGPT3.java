package GPTAPI;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import static SeleniumControls.HelperMethods.*;

import java.util.HashSet;
import java.util.List;

public class PersistentGPT3 {
	
	private static WebDriver login() throws InterruptedException {
		String startingURL = "https://www.google.com/";
		String loginURL = "https://chat.openai.com/auth/login";
		String goalURL = "https://chat.openai.com/";
		
		WebDriver driver = provideAutocompleteSelenium();
		driver.navigate().to(startingURL);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.body.innerHTML = \'\';"
				+ "var a = document.createElement('a');"
				+ "var link = document.createTextNode(\"Click here to login to ChatGPT.\");"
				+ "a.appendChild(link);"
				+ "a.href = \"" + loginURL + "\";"
				+ "a.id = \"clickMeToProgress\";"
				+ "a.style.zIndex = \"1000000\";"
				+ "a.style.position = \"fixed\";"
				+ "a.target = \"_blank\";"
				+ "document.body.prepend(a);"
				+ "window.location = '" +loginURL+ "';");
		pauseOneSecond();
		
		if(goalURL.equalsIgnoreCase(driver.getCurrentUrl())) {
			waitUntilFound(driver, driver, By.cssSelector("#prompt-textarea"));
			return driver;
		}
		
		while(driver.getWindowHandles().size() <= 1) {
			Thread.sleep(1000);
			Thread.yield();
		}
		driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[1]);
		
		while(!goalURL.equalsIgnoreCase(driver.getCurrentUrl())) {
			Thread.sleep(1000);
			Thread.yield();
		}
		
		driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[0]);
		driver.close();
		driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[0]);
		
		waitUntilFound(driver, driver, By.cssSelector("#prompt-textarea"));
		
		return driver;
	}

	private WebDriver driver;
	public WebDriver getDriver() {
		return driver;
	}
	private List<WebElement> seenMessages;
	private String oldURL = "";
	public PersistentGPT3(){
		try {
			driver = login();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clearNewMessages();
	}
	
	private List<WebElement> getGPTMessages(){
		waitUntilFound(driver, driver, By.cssSelector("[data-testid=\"conversation-turn-2\"]"));
		List<WebElement> msgs = driver.findElements(By.cssSelector("[data-message-author-role=\"assistant\"] > div"));
		return msgs;
	}
	
	public void clearNewMessages() {
		seenMessages = getGPTMessages();
	}
	public SetView<WebElement> getNewMessages() {
		if(isURLChanged(driver, oldURL)) {
			clearNewMessages();
			oldURL = driver.getCurrentUrl();
		}
		
		SetView<WebElement> diff = Sets.difference(new HashSet<WebElement>(getGPTMessages()), new HashSet<WebElement>(seenMessages));
		return diff;
	}
}


