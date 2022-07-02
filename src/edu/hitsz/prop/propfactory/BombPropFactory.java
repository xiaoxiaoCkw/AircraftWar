package edu.hitsz.prop.propfactory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BombProp;

/**
 * 炸弹道具工厂
 *
 * @author zhangzewei
 */
public class BombPropFactory extends AbstractPropFactory {
    @Override
    public AbstractProp creatProp(int locationX, int locationY, double speedX, double speedY) {
        return new BombProp(locationX, locationY, speedX, speedY);
    }

}
