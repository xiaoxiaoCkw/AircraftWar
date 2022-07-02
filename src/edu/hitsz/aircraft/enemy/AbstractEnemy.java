package edu.hitsz.aircraft.enemy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.observerpattern.Subscriber;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.shoot.ShootStrategy;

import java.util.List;

/**
 * 所有敌机的抽象父类
 *
 * @author zhangzewei
 */
public abstract class AbstractEnemy extends AbstractAircraft {

    public AbstractEnemy(int locationX, int locationY, double speedX, double speedY, int hp, int shootNum, int power, int direction, ShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY, hp, shootNum, power, direction, shootStrategy);
    }

    /**
     * 道具掉落
     * 普通敌机不掉落道具
     * 精英敌机随机掉落一个道具
     * Boss敌机随机掉落多个道具
     *
     * @return 掉落的道具集合
     */
    public abstract List<AbstractProp> dropProps();

}
