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
    

    // Test de la Story #2-recherche (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testRecherche() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/find/outdoors-adventure/");

        // Le titre de la page d'accueil et du h1 contiennent tous les deux *Nature et Aventure*
        assertThat(driver.getTitle(), contains("Nature et Aventure"));
        WebElement h1 = driver.findElement(By.cssSelector("h1"));
        assertThat(h1.getText(), contains("Nature et aventure"));


        // La page de recherche contient un bandeau de recherche avec le champ de recherche, le rayon de recherche, la ville de recherche, un choix d'affichage de la liste entre Groupe et Calendrier.

        // Le tri par défaut est le tri par pertinence.

        // Il y a 4 tri possibles: *pertinence, plus récents, nombre de membres, proximité*

        // Quand je clique sur le choix d'affichage calendrier, la liste se met à jour et affiche des événements jour par jour ainsi qu'un calendrier.

        // Quand je clique sur le 21 du mois courant du calendrier, le premier résultat de la liste qui s'affiche correspond à un événement du 21 du mois courant. (ici vérifier qu'avant le premier résultat de la liste, la date s'affiche puis cliquer sur l'événement de la liste et vérifier que l'on est bien redirigé vers une page qui parle de l'événement du 21.
    }
    
    

    // Test de la Story #2-fiche_meetup (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testFicheMeetup() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/promenades-et-randonnees/");

        // clickable title
        // with meetup name, place, number of members, organisators, a button to join the event, and a picture

        //tab banner with "A propos", "Evénements", "Membres", "Photos", "Discussions", "Plus".

        //You should be able to click on past events, upcoming events, and be able to see all the members, and all the photos of a group. (These 4 links available on the page)

        //Si aucun prochain meetup n'est prévu, un bandeau doit apparaitre à la place indiquant ce message en titre: Quel sera notre prochain Meetup ?

        //Si je veux rejoindre le groupe, je dois cliquer sur rejoindre puis entrer mes informations de membre et donc m'identifier via facebook ou google ou identifiant de site. Sinon, je peux aussi m'inscrire et là je dois être rediriger vers /register/?method=email

        // Si j'ai une question, je dois pouvoir contacter l'organisateur depuis la fiche de membre. En cliquant sur contacter je dois alors être automatiquement redirigé vers la page de connexion https://secure.meetup.com/fr-FR/login/
        WebElement contactLink = driver.findElement(By.cssSelector(".orgInfo-message"));
        assertEquals(contactLink.getText(), "Contacter");
        contactLink.click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        assertThat(driver.getCurrentUrl(), containsString("https://secure.meetup.com/fr-FR/login/"));

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
