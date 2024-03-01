package Linker;

import GPTAPI.ForgetfulBingChat;
import GPTAPI.PersistentGPT3;
import static SeleniumControls.HelperMethods.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Sets.SetView;

public class AugmentedGPT3Linker {
	public final static String AUGMENTATION_HEADER = "You have been reforged stronger then ever before.";
	
	public static void main(String[] args) {
		PersistentGPT3 manager = new PersistentGPT3();
		WebDriver driver = manager.getDriver();
		
		while(true) {
			try {
				
				SetView<WebElement> newMessages = manager.getNewMessages();
				if(!newMessages.isEmpty() && isConversationAugmented(driver)) {
					boolean stillStreaming = newMessages.stream()
						.filter(ele -> ele.getAttribute("class").contains("result-streaming") || ele.getText().isEmpty())
						.findAny().isPresent();
					
					if(!stillStreaming) {
						newMessages.stream().forEach(ele -> {
							System.out.println("***" + ele.getText() + "***");
							getPromptFromMessage(ele.getText()).ifPresent(prompt -> {
								System.out.println("Waiting for Bing - Prompt: " + prompt);
								String reply = ForgetfulBingChat.getResponse(prompt, true);
						        System.out.println("Response: " + reply);
						        
						        WebElement promptBox = waitUntilFound(driver, driver, By.cssSelector("#prompt-textarea"));
						        promptBox.sendKeys("Primed!");
						        
						        JavascriptExecutor jse = (JavascriptExecutor)driver;
						        jse.executeScript("arguments[0].value=arguments[1];", promptBox, reply);
						        
				        		waitUntilFound(driver, driver, By.cssSelector("[data-testid=\"send-button\"]")).click();
							});
						});
						manager.clearNewMessages();
					}
				}
				
				Thread.yield();
				Thread.sleep(300);
				
			} catch (InterruptedException | StaleElementReferenceException e) {
				e.printStackTrace();
			}
		}
	}
	

    private static boolean isConversationAugmented(WebDriver driver) {
    	try {
	    	WebElement userMessage = driver
	    		.findElement(By.cssSelector("[data-testid=\"conversation-turn-2\"]"))
	    		.findElement(By.cssSelector("[data-message-author-role=\"user\"]"))
	    		.findElement(By.tagName("div"));
	    	
	    	String userText = userMessage.getText();
	    	return userText.contains(AUGMENTATION_HEADER);
    	} catch(NoSuchElementException ex) {
    		return false;
    	}
    }
}
