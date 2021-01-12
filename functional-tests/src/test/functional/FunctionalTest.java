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

     /*
    // Test de la Story #2-homepage (https://trello.com/c/glufGucb/45-homepage)
	@Test
    public void testHomepage() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/");
		assertEquals("Partagez vos passions | Meetup", driver.getTitle());

        //h1
        WebElement h1 = driver.findElement(By.xpath("//h1"));
        assertEquals("Le monde vous tend les bras", h1.getText());
        // sub title;  following-sibling::div[1] == element + div
        assertEquals("Rejoignez un groupe local pour rencontrer du monde, tester une nouvelle activité ou partager vos passions.", getParent(h1, 1).findElement(By.xpath("following-sibling::div[1]")).getText());

        // button join meetup;
        WebElement buttonJoin = driver.findElement(By.xpath("//a[@id='joinMeetupButton']"));
        assertEquals("Rejoindre Meetup", buttonJoin.getText());
        assertEquals("https://www.meetup.com/fr-FR/register/", buttonJoin.getAttribute("href"));


        //Description;  not Working without JS (because of special char maybe (ex: &nbsp!))
        //WebElement pageDescription = driver.findElement(By.xpath("//meta[@name='description']")).getAttribute("content");
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        String description = executor.executeScript("return document.querySelector(\"meta[name=description]\").content").toString();
        assertEquals(deleteAllSpecialChar("Partagez vos passions et faites bouger votre ville ! Meetup vous aide à rencontrer des personnes près de chez vous, autour de vos centres d'intérêt."), deleteAllSpecialChar(description));

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
*/

    // Test de la Story #2-jobs (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testJobs() throws Exception {
        System.out.println("JOBS");
        driver.get("https://www.meetup.com/fr-FR/careers/");

        //punchline
        String selector = "main > div > section > div ";
        assertEquals("Join our team, find your people", driver.findElement(By.cssSelector(selector + "> div")).getText());

        // button link
        WebElement aElement = driver.findElement(By.cssSelector(selector + "> div + div + a"));
        assertEquals("Explore Opportunities", aElement.getText());

        //region explore section
        //region nb theme
        //aElement.click();
        String href = aElement.getAttribute("href").split("#")[1];
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        String sectionNbChild = (String) executor.executeScript("return document.querySelector('#" + href + " > div + div +div > ul ').childElementCount.toString();");
        //WebElement sectionExplore = driver.findElement(By.cssSelector("#" + aElement.getAttribute('href') + " > div + div +div > ul "));
        assertEquals("9", sectionNbChild);
        //endregion
        //region link more opportunities
        WebElement aElementMore = driver.findElement(By.cssSelector("#" + href + " + section > div > span > a "));
        System.out.println("https://www.meetup.com/fr-FR/careers/all-opportunities");
        System.out.println(aElementMore.getAttribute("href"));
        //aElementMore.click();
        //link can not hav fr-FR inhref

        assertThat( aElementMore.getAttribute("href"), containsString("https://www.meetup.com/"));
        assertThat(aElementMore.getAttribute("href"), containsString("/careers/all-opportunities"));

        //endregion
        //endregion
    }

/*
    // Test de la Story #2-pixel_perfect (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testPixelPerfect() throws Exception {
        System.out.println("PixelPerfect");
        driver.get("https://www.meetup.com/fr-FR/");

    }
*/

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

}
