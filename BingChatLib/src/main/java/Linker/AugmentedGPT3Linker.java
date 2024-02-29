package Linker;

import GPTAPI.PersistentGPT3;
import static SeleniumControls.HelperMethods.*;

import org.openqa.selenium.WebDriver;

public class AugmentedGPT3Linker {
	public static void main(String[] args) {
		PersistentGPT3 manager = new PersistentGPT3();
		
		WebDriver driver = manager.getDriver();
		String currentURL = "";
		
		while(true) {
			if(isURLChanged(driver, currentURL)) {
				currentURL = driver.getCurrentUrl();
				System.out.println("URL Changed.");
				if(isConversationAugmented(driver)) {
					System.out.println("This is the one.");
				}
			}
			
			Thread.yield();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
