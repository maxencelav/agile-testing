package test.functional;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;

public class FunctionalTest {

	private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver","/Library/Java/JUNIT/chromedriver");
		driver = new ChromeDriver();
	    	// Seems no more working in last Chrome versions
		// driver.manage().window().maximize();
  		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
     }

    // Test de la Story #2-homepage (https://trello.com/c/glufGucb/45-homepage)
	@Test
    public void testHomepage() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/");
		assertEquals(driver.getTitle(), "Partagez vos passions | Meetup");
    }

    // Test de la Story #2-homepage (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testDescription() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/");
        System.out.println(driver.findElement(By.xpath("//meta[@name='description']")).getAttribute("content"));
        System.out.println("Partagez vos passions et faites bouger votre ville ! Meetup vous aide à rencontrer des personnes près de chez vous, autour de vos centres d'intérêt.");
        assertEquals(driver.findElement(By.xpath("//meta[@name='description']")).getAttribute("content"), "Partagez vos passions et faites bouger votre ville ! Meetup vous aide à rencontrer des personnes près de chez vous, autour de vos centres d'intérêt.");
    }


    // Test de la Story #2-homepage (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testH1() throws Exception {
        
    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

}
