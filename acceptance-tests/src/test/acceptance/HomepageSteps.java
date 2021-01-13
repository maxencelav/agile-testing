package test.acceptance;

import java.util.concurrent.TimeUnit;

import java.lang.*;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;

public class HomepageSteps {

	public static WebDriver driver;

	@Before
	public void beforeScenario() {
		System.setProperty("webdriver.chrome.driver", "/Library/Java/JUNIT/chromedriver");
		driver = new ChromeDriver();
		// Seems no more working in last Chrome versions
		// driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	@Given("^je suis sur la homepage$")
	public void je_suis_sur_la_homepage() throws Throwable {
		driver.get("https://www.meetup.com/fr-FR/");
	}

	@Then("^le titre doit être \"([^\"]*)\"$")
	public void le_titre_doit_être(String arg1) throws Throwable {
		assertEquals(driver.getTitle(), arg1);
	}

	@Then("^la description contient \"([^\"]*)\"$")
	public void la_description_doit_être(String arg1) throws Throwable {
		// By CSS Selector
		assertTrue(
				driver.findElement(By.cssSelector("meta[name='description']")).getAttribute("content").contains(arg1));
		// By XPATH, si vous préférez...
		// assertEquals(driver.findElement(By.xpath("//meta[@name='description']")).getAttribute("content"),
		// arg1);
	}

	@Then("^la punchline \\(h(\\d+)\\) du site doit être \"([^\"]*)\"$")
	public void la_punchline_h_du_site_doit_être(int arg1, String arg2) throws Throwable {
		// h1
		WebElement h1 = driver.findElement(By.xpath("//h1"));
		assertEquals(arg2, h1.getText());
	}

	@Then("^le bouton d'inscription doit contenir le text \"([^\"]*)\"$")
	public void le_bouton_d_inscrpiton_doit_contenir_le_text(String arg1) throws Throwable {
		WebElement buttonJoin = driver.findElement(By.xpath("//a[@id='joinMeetupButton']"));
		assertEquals(arg1, buttonJoin.getText());

	}

	@Then("^le bouton d'inscription doit mener vers la page \"([^\"]*)\"$")
	public void le_bouton_d_inscription_doit_mener_vers_la_page(String arg1) throws Throwable {
		WebElement buttonJoin = driver.findElement(By.xpath("//a[@id='joinMeetupButton']"));
		// button join meetup;
		assertEquals(arg1, buttonJoin.getAttribute("href"));

	}

	/*
	 * @Then("^le bouton d'inscription doit être présent \"([^\"]*)\"$") public void
	 * le_bouton_d_inscription_doit_être_présent(String arg1) throws Throwable {
	 * throw new PendingException(); }
	 */

	@After
	public void afterScenario() {
		driver.quit();
	}

}
