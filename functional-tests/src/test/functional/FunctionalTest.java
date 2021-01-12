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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.*;

public class FunctionalTest {

	private WebDriver driver;

	//region helpers function
	private String deleteHtmlEntities(String string){
	    return string.replaceAll("<[^>]*>", "").replaceAll("&nbsp;", " ").replace("\\u00a0", " ");
    }

    private String deleteAllSpecialChar(String string){
        return string.replaceAll("[^a-zA-Z0-9]", "");
    }

    private WebElement getParent(WebElement element, int parentNb){
	    String path = "";
        for (int i=0; i<parentNb; i++){
            path += (i==0 ? ".." : "/..");
        }
        return element.findElement(By.xpath(path));
    }
    //endregion

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
		//description,

        //h1
        WebElement h1 = driver.findElement(By.xpath("//h1"));
        assertEquals(h1.getText(), "Le monde vous tend les bras");
        // sub title;  following-sibling::div[1] == element + div
        assertEquals(getParent(h1, 1).findElement(By.xpath("following-sibling::div[1]")).getText(), "Rejoignez un groupe local pour rencontrer du monde, tester une nouvelle activité ou partager vos passions.");

        // button join meetup;
        WebElement buttonJoin = driver.findElement(By.xpath("//a[@id='joinMeetupButton']"));
        assertEquals(buttonJoin.getText(), "Rejoindre Meetup");
        assertEquals(buttonJoin.getAttribute("href"), "https://www.meetup.com/fr-FR/register/");


        //Description;  not Working Warning : special char (ex: &nbsp!)
        WebElement pageDescription = driver.findElement(By.xpath("//meta[@name='description']"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        String test = executor.executeScript("return document.querySelector(\"meta[name=description]\").content").toString();
        assertEquals(deleteAllSpecialChar(test), deleteAllSpecialChar("Partagez vos passions et faites bouger votre ville ! Meetup vous aide à rencontrer des personnes près de chez vous, autour de vos centres d'intérêt."));
            //driver.findElement(By.xpath("//meta[@name='description']")).getAttribute("content")
	}

/*
    // Test de la Story #2-recherche (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testRecherche() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/");

    }

    // Test de la Story #2-fiche_meetup (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testFicheMeetup() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/");

    }


    // Test de la Story #2-jobs (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testJobs() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/");

    }

    // Test de la Story #2-pixel_perfect (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testPixelPerfect() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/");

    }
*/

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

}
