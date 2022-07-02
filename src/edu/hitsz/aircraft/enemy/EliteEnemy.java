package edu.hitsz.aircraft.enemy;

import edu.hitsz.application.Main;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.propfactory.AbstractPropFactory;
import edu.hitsz.prop.propfactory.BloodPropFactory;
import edu.hitsz.prop.propfactory.BombPropFactory;
import edu.hitsz.prop.propfactory.BulletPropFactory;
import edu.hitsz.shoot.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 精英敌机
 * 可按照一定轨迹、频率射击
 *
 * @author zhangzewei
 */
public class EliteEnemy extends AbstractEnemy {

    public EliteEnemy(int locationX, int locationY, double speedX, double speedY, int hp, int shootNum, int power, int direction, ShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY, hp, shootNum, power, direction, shootStrategy);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT){
            vanish();
        }
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> props = new LinkedList<>();
        AbstractPropFactory propFactory;
        AbstractProp prop;
        int ranNum = (int)(Math.random() * 100);
        if (ranNum < 25) {
            if (ranNum < 10) {
                propFactory = new BloodPropFactory();
            } else if (ranNum < 15) {
                propFactory = new BombPropFactory();
            } else {
                propFactory = new BulletPropFactory();
            }
            prop = propFactory.creatProp(
                    this.getLocationX(),
                    this.getLocationY(),
                    0,
                    5
            );
            props.add(prop);
        }
        return props;
    }

    @Override
    public void update() {
        this.vanish();
    }

}
