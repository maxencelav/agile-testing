package codingfactory.rpgconsole.hero;

import codingfactory.rpgconsole.enemy.Enemy;
import java.util.Random;

public class Hero {

    private String name;
    private Integer level;
    private Integer hp;
    private Integer hpMax;
    private Integer atk;
    private Integer xp;

    public Hero(String name){ //construct
        this.name = name;
        this.level = 1;
        this.hp = 20;
        this.hpMax=20;
        this.atk = 2;
        this.xp = 0;
    }

    // region getters / covered
    public String getName() {
       return this.name;
    }

    public Integer getLevel(){
        return this.level;
    }

    public Integer getHp(){
        return this.hp;
    }

    public Integer getHpMax(){
        return this.hpMax;
    }

    public Integer getAtk(){
        return this.atk;
    }

    public Integer getXp(){
        return this.xp;
    }
    //endregion


    // region setters / covered
    private void setLevel(Integer newLevel) {
        this.level = newLevel;
    }
    private void setHp(Integer newHp) {
        this.hp = newHp;
    }
    private void setHpMax(Integer newHpMax) {
        this.hpMax = newHpMax;
    }
    private void setAtk(Integer newAtk) {
        this.atk = newAtk;
    }
    private void setXp(Integer newXp) {
        this.xp = newXp;
    }
    // endregion

    public void takeDamage(Integer damage){
        this.setHp(this.getHp() - damage);
    }

    public void attack(Enemy enemy) {
        Random rand = new Random();
        int randomNum = rand.nextInt(this.level + 1);
        enemy.takeDamage(this.atk+randomNum); //enemy take damage for hero attack
    }

    public void increaseXp(int addedXp) {
        this.setXp(this.getXp() + addedXp);
        if (this.getXp() > this.getHpMax() * this.getLevel()){
            this.levelUp();
        }
    }

    public void levelUp() {
        this.setXp(0);
        this.setAtk(this.getAtk()+1);
        this.setHpMax(this.getHpMax()+3);
        this.setLevel(this.level+1);
    }

}
