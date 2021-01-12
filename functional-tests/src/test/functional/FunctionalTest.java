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

	private String deleteSpecialChar(String string){
	    return string.replaceAll("&*;", "");
    }

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
		//description, Warning : special char (ex: &nbsp!)
        assertEquals(deleteSpecialChar(driver.findElement(By.xpath("//meta[@name='description']")).getAttribute("content").toString()),
                deleteSpecialChar("Partagez vos passions et faites bouger votre ville ! Meetup vous aide à rencontrer des personnes près de chez vous, autour de vos centres d'intérêt."));


    }


    // Test de la Story #2-recherche (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testRecherche() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/");

    }

    // Test de la Story #2-fiche_meetup (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testFicheMeetup() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/promenades-et-randonnees/");

    }

    // Test de la Story #2-jobs (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testJobs() throws Exception {
        System.out.println("JOBS");
        driver.get("https://www.meetup.com/fr-FR/careers/");

    }

    // Test de la Story #2-pixel_perfect (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testPixelPerfect() throws Exception {
        System.out.println("PixelPerfect");
        driver.get("https://www.meetup.com/fr-FR/");

    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

}
