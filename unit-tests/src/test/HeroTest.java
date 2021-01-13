package test;

import codingfactory.rpgconsole.enemy.Enemy;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import codingfactory.rpgconsole.hero.Hero;

public class HeroTest {
	Hero hero;
	String sep = "\n";

	static String fx_bold = "\u001B[1m";
	static String fx_yellow = "\033[33m";
	static String fx_end = "\033[0m";


	//region initialisation
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println( fx_yellow + fx_bold + "###### BEFORE TESTS ######" + fx_end);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println( fx_yellow + fx_bold + "###### AFTER ALL TESTS ######" + fx_end);
	}

	@Before
	public void setUp() throws Exception {
		hero = new Hero("Jaina Portvaillant");
		System.out.println("\n---- Test Start ----" );
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("---- Test Ended ----\n");
	}
	//endregion

	/**
	 * Check if hero's properties increased when level up, and xp down to 0
	 * @throws Exception
	 */
	@Test
	public void testHeroLevelUp() throws Exception {
		System.out.println(fx_bold + "Test Level UP" + fx_end);

		// checks that the hero is a valid hero
		assertThat(hero, hasProperty("level"));
		assertThat(hero, hasProperty("atk"));
		assertThat(hero, hasProperty("hpMax"));

		// gets current values for hero
		Integer oldLevel = hero.getLevel();
		Integer oldAtk= hero.getAtk();
		Integer oldHpMax= hero.getHpMax();

		// adds a level to our hero
		hero.levelUp();

		// checks that the hero has successfully leveled up
		assertThat(hero, hasProperty("level", is(oldLevel+1)));
		assertThat(hero, hasProperty("atk", is(oldAtk+1)));
		assertThat(hero, hasProperty("hpMax", is(oldHpMax+3)));
		assertThat(hero, hasProperty("xp", is(0)));

		// logs results
		System.out.println(
				"Level: " + oldLevel + " -> " + hero.getLevel() + sep +
				"ATK: " + oldAtk + " -> " + hero.getAtk() + sep +
				"Max HP: " + oldHpMax + " -> " + hero.getHpMax());
	}

	/**
	 * Check what happen when hero gain xp, and finally level up
	 * @throws Exception
	 */
	@Test
	public void testHeroGainXp() throws Exception {
		System.out.println(fx_bold + "Test XP Gain" + fx_end);

		// checks that the hero is a valid hero
		assertThat(hero, hasProperty("level"));
		assertThat(hero, hasProperty("atk"));
		assertThat(hero, hasProperty("hpMax"));

		// gets current values for hero
		Integer winXp = 20;
		Integer oldLevel = hero.getLevel();
		Integer oldAtk = hero.getAtk();
		Integer oldHpMax = hero.getHpMax();
		Integer oldXp = hero.getXp();

		// increases XP for the hero
		hero.increaseXp(winXp);

		// checks that the XP for the hero is correct
		// AND that he has NOT leveled up
		assertThat(hero, hasProperty("xp", is(oldXp + winXp)));
		assertThat(hero, hasProperty("level", is(oldLevel)));
		assertThat(hero, hasProperty("atk", is(oldAtk)));
		assertThat(hero, hasProperty("hpMax", is(oldHpMax)));

		// logs results
		System.out.println("XP Increase" + sep +
				"Max HP: " + hero.getHpMax() + sep +
				"ATK: " + hero.getAtk() + sep +
				"Level: " + hero.getLevel() + sep +
				"XP: " + hero.getXp());
	}

	/**
	 * Check what happen when hero gain xp, and finally level up
	 * @throws Exception
	 */
	@Test
	public void testHeroGainXpLevelUp() throws Exception {
		System.out.println(fx_bold + "Test XP Gain & Level UP" + fx_end);

		// checks that the hero is a valid hero
		assertThat(hero, hasProperty("level"));
		assertThat(hero, hasProperty("atk"));
		assertThat(hero, hasProperty("hpMax"));

		// gets current values for hero
		Integer winXp = 20;
		Integer oldLevel = hero.getLevel();
		Integer oldAtk = hero.getAtk();
		Integer oldHpMax = hero.getHpMax();
		Integer oldXp = hero.getXp();

		// increases XP for the hero
		hero.increaseXp(winXp);

		// checks that the XP for the hero is correct
		// AND that he has NOT leveled up
		assertThat(hero, hasProperty("xp", is(oldXp + winXp)));
		assertThat(hero, hasProperty("level", is(oldLevel)));
		assertThat(hero, hasProperty("atk", is(oldAtk)));
		assertThat(hero, hasProperty("hpMax", is(oldHpMax)));

		// logs results
		System.out.println("1st XP increase" + sep +
				"Max HP: " + hero.getHpMax() + sep +
				"ATK: " + hero.getAtk() + sep +
				"Level: " + hero.getLevel() + sep +
				"XP: " + hero.getXp());

		// increases XP for the hero for a second
		hero.increaseXp(winXp);

		// checks that the XP for the hero is correct
		// AND that he has leveled up
		assertThat(hero, hasProperty("xp", is(0)));
		assertThat(hero, hasProperty("level", is(oldLevel + 1)));
		assertThat(hero, hasProperty("atk", is(oldAtk + 1)));
		assertThat(hero, hasProperty("hpMax", is(oldHpMax + 3)));

		//
		System.out.println("\n");


		// logs results
		System.out.println("2nd XP increase" + sep +
				"Max HP: " + oldHpMax + " -> " + hero.getHpMax() + sep +
				"ATK: " + oldAtk + " -> " + hero.getAtk() + sep +
				"Level: " + oldLevel + " -> " + hero.getLevel() + sep +
				"XP: " + hero.getXp());
	}

	/**
	 * Check if hero's hp decrease when taking some damage
	 * @throws Exception
	 */
	@Test
	public void testHeroTakeDamage() throws Exception {
		System.out.println(fx_bold + "Test Hero takes Damage from Enemy" + fx_end);

		// checks that the hero is a valid hero
		assertThat(hero, hasProperty("hp"));

		//gets current value for hero
		Integer oldHp = hero.getHp();

		// makes the hero take damage
		int damage = 3;
		hero.takeDamage(damage);

		// checks that the HP for the hero is correct
		assertThat(hero, hasProperty("hp", is(oldHp-damage)));

		// logs results
		System.out.println("Hero HP: " + oldHp + " -> " + hero.getHp());
	}

	/**
	 * Check if enemy's hp decrease when taking some damage from hero
	 * @throws Exception
	 */
	@Test
	public void testHeroAttack() throws Exception {
		System.out.println(fx_bold + "Test Hero Attacks against Enemy" + fx_end);

		// creates an enemy
		Enemy enemy = new Enemy("lapin feroce", 1);

		// gets current value for HP
		Integer oldEnemyHp = enemy.getHp();

		// makes the enemy take damage
		hero.attack(enemy);

		// checks that the enemy has lost HP
		assertThat(enemy, hasProperty("hp"));
		assertThat(oldEnemyHp, greaterThan(enemy.getHp()));

		// logs result
		System.out.println("Enemy HP: " + oldEnemyHp + " -> " + enemy.getHp());
	}

	/**
	 * Check if all hero's properties are defined
	 * @throws Exception
	 */
	@Test
	public void testHeroProperties() throws Exception {
		System.out.println( fx_bold + "Test Hero Properties" + fx_end);

		assertThat(hero, hasProperty("name"));
        assertThat(hero, hasProperty("name", is("Jaina Portvaillant")));

		assertThat(hero, hasProperty("level"));
		assertThat(hero, hasProperty("level", is(1)));

		assertThat(hero, hasProperty("hp"));
		assertThat(hero, hasProperty("hp", is(20)));

		assertThat(hero, hasProperty("hpMax"));
		assertThat(hero, hasProperty("hpMax", is(20)));

		assertThat(hero, hasProperty("atk"));
		assertThat(hero, hasProperty("atk", is(2)));

		assertThat(hero, hasProperty("xp"));
		assertThat(hero, hasProperty("xp", is(0)));
	}

}
