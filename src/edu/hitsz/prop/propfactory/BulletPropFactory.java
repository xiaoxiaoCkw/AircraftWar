package edu.hitsz.prop.propfactory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BulletProp;

/**
 * 火力道具工厂
 *
 * @author zhangzewei
 */
public class BulletPropFactory extends AbstractPropFactory {
    @Override
    public AbstractProp creatProp(int locationX, int locationY, double speedX, double speedY) {
        return new BulletProp(locationX, locationY, speedX, speedY);
    }

}
