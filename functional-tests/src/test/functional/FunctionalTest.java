package test.functional;

import java.util.concurrent.TimeUnit;
import java.util.*;

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
import org.openqa.selenium.interactions.Actions;
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

    public void clickJs(String selector){
        try{
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("document.querySelector('" + selector + "').click();");
        }catch (Exception e){
        }
    }

    public void clickSelenium(String selector){
        try{
            WebElement contactLink = driver.findElement(By.cssSelector(selector));
            contactLink.click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }catch (Exception e){
        }
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


    // Test de la Story #2-jobs (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testJobs() throws Exception {
        System.out.println("JOBS");
        driver.get("https://www.meetup.com/fr-FR/careers/");
        JavascriptExecutor executor = (JavascriptExecutor) driver; // used to execute Js script

        //region punchline
        String mainPunchlineSelector = "main > div > section > div ";
        assertEquals("Join our team, find your people", driver.findElement(By.cssSelector(mainPunchlineSelector + "> div")).getText());
        //endregion

        //region button link
        WebElement aElementExplore = driver.findElement(By.cssSelector(mainPunchlineSelector + "> div + div + a"));
        assertEquals("Explore Opportunities", aElementExplore.getText());

        //region click on aElement explore with JS (because link 'not clickable')
        clickJs(mainPunchlineSelector + " > div + div + a ");
        assertEquals(driver.getCurrentUrl(), "https://www.meetup.com/fr-FR/careers/#open-positions");
        //endregion
        //endregion

        //region perks & benefits
        String perksSelector = "main > div  + div  + div + div > section > div";
        //region perks punchline
        WebElement perksPunch = driver.findElement(By.cssSelector( perksSelector  + "  > div "));
        assertEquals("Perks and benefits", perksPunch.getText());
        //endregion

        //region check if all perks have img & a text for advantage description
        //get ul nb of children
        String perksUlChilds = (String) executor.executeScript("return document.querySelector('" + perksSelector  + " > ul').childElementCount.toString();");
        int perksUlChildsInt = Integer.parseInt( perksUlChilds);

        //loop to check each child
        for (int i=1; i<=perksUlChildsInt; i++){
            Boolean isPerksCorrect = (Boolean) executor.executeScript(
                    "let selector = '" + perksSelector  + " > ul li:nth-child(" + i + ") > div > div > div' ;" +
                    "try{" +
                            "return document.querySelector(selector + ' > img ') !== null && document.querySelector(selector + ' + div ').textContent !==null;" +
                        "}" +
                    "catch(error){return false;}");
            assertEquals(isPerksCorrect, true);
        }
        //endregion
        //endregion

        //region explore section
        //region nb theme
        //get href from elementExplore (getAttribute("href") return complete URL)
        String href = aElementExplore.getAttribute("href").split("#")[1];
        String sectionNbChild = (String) executor.executeScript("return document.querySelector('#" + href + " > div + div +div > ul ').childElementCount.toString();");
        assertEquals("9", sectionNbChild); // supposed to contain 9 child on site
        //endregion

        //region click link more opportunities, go to naw page
        clickJs("#" + href + " + section > div > span > a ");
        assertEquals(driver.getCurrentUrl(), "https://www.meetup.com/fr-FR/careers/all-opportunities");
        //endregion
        //endregion

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
