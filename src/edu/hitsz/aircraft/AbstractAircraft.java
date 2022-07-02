package edu.hitsz.aircraft;

import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.shoot.ShootStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;

    /**
     * 攻击方式
     */

    /**
     * 子弹一次发射数量
     */
    protected int shootNum;

    /**
     * 子弹伤害
     */
    protected int power;

    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    protected int direction;

    /**
     * 射击方式
     */
    protected ShootStrategy shootStrategy;

    public AbstractAircraft(int locationX, int locationY, double speedX, double speedY, int hp, int shootNum, int power, int direction, ShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.shootNum = shootNum;
        this.power = power;
        this.direction = direction;
        this.shootStrategy = shootStrategy;
    }

    public void decreaseHp(int decrease) {
        hp -= decrease;
        if (hp <= 0){
            hp = 0;
            vanish();
        }
    }

    public void increaseHp(int increase) {
        hp += increase;
        if (hp >= maxHp) {
            hp = maxHp;
        }
    }

    /**
     * 飞机射击方法
     *
     * @return
     *  可射击对象返回子弹
     *  非可射击对象返回null
     */
    public List<AbstractBullet> shoot() {
        return shootStrategy.doShoot(this);
    }

    public int getHp() {
        return hp;
    }

    public int getShootNum() {
        return shootNum;
    }

    public int getPower() {
        return power;
    }

    public int getDirection() {
        return direction;
    }

    public ShootStrategy getShootStrategy() {
        return shootStrategy;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }

}


