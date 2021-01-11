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

public class EnemyTest {
    Enemy enemy;
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
        enemy = new Enemy("lapin feroce", 1);
		System.out.println("\n---- Test Start ----" );
    }

    @After
    public void tearDown() throws Exception {
		System.out.println("---- Test Ended ----\n");
    }
    //endregion

    /**
     * Check if enemy's hp decrease when taking some damage
     * @throws Exception
     */
    @Test
    public void testEnemyTakeDamage() throws Exception {
        // checks that the enemy is a valid enemy
        assertThat(enemy, hasProperty("hp"));

		//gets current value for enemy
        Integer oldHp = enemy.getHp();

		// makes the enemy take damage
        int damage = 3;
        enemy.takeDamage(damage);

		// checks that the enemy has lower HP
        assertThat(enemy, hasProperty("hp", is(oldHp-damage)));

        // logs results
        System.out.println("Enemy HP: " + oldHp + " -> " + enemy.getHp());
    }


    /**
     * Check if hero's hp decrease when taking some damage
     * @throws Exception
     */
    @Test
    public void testEnemyAttack() throws Exception {
        Hero hero = new Hero("Ilias");

        // gets current value for hero's HP
        Integer oldHeroHp = hero.getHp();

        // makes the hero take damage from enemy
        enemy.attack(hero);

        // checks that the hero has lost HP
        assertThat(hero, hasProperty("hp"));
        assertThat(oldHeroHp, greaterThan(hero.getHp()));

        // logs result
        System.out.println("Hero HP: " + oldHeroHp + " -> " + hero.getHp());
    }

    /**
     * Check if all enemy's properties are defined
     * @throws Exception
     */
    @Test
    public void testEnemyProperties() throws Exception {
        assertThat(enemy, hasProperty("name"));
        assertThat(enemy, hasProperty("name", is("lapin feroce")));

        assertThat(enemy, hasProperty("level"));
        assertThat(enemy, hasProperty("level", is(1)));

        assertThat(enemy, hasProperty("hp"));
        assertThat(enemy, hasProperty("hp", is(15)));

        assertThat(enemy, hasProperty("atk"));
        assertThat(enemy, hasProperty("atk", is(1)));
    }

}
