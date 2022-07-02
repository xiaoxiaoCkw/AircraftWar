package edu.hitsz.aircraft.enemy;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.propfactory.AbstractPropFactory;
import edu.hitsz.prop.propfactory.BloodPropFactory;
import edu.hitsz.prop.propfactory.BombPropFactory;
import edu.hitsz.prop.propfactory.BulletPropFactory;
import edu.hitsz.shoot.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * Boss 敌机
 * 悬浮于界面上方，直至被消灭
 * 按照一定轨迹、频率模式发射子弹
 *
 * @author zhangzewei
 */
public class BossEnemy extends AbstractEnemy {

    public BossEnemy(int locationX, int locationY, double speedX, double speedY, int hp, int shootNum, int power, int direction, ShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY, hp, shootNum, power, direction, shootStrategy);
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> props = new LinkedList<>();
        AbstractPropFactory propFactory;
        AbstractProp prop;
        int propNum = (int)(Math.random() * 4) + 2;
        for (int i = 0; i < propNum; i++) {
            int propType = (int)(Math.random() * 5);
            if (propType == 0) {
                propFactory = new BombPropFactory();
            } else if (propType <= 2) {
                propFactory = new BloodPropFactory();
            } else {
                propFactory = new BulletPropFactory();
            }
            prop = propFactory.creatProp(
                    this.getLocationX() + (i * 2 - propNum + 1) * 20,
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
        this.decreaseHp(100);
    }

}
