package GPTAPI;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import static SeleniumControls.HelperMethods.*;

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
	public PersistentGPT3(){
		try {
			driver = login();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new PersistentGPT3();
	}

	public WebDriver getDriver() {
		return driver;
	}
}


