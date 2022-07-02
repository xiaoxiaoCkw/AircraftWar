package edu.hitsz.prop.propfactory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;

/**
 * 加血道具工厂
 *
 * @author zhangzewei
 */
public class BloodPropFactory extends AbstractPropFactory {
    @Override
    public AbstractProp creatProp(int locationX, int locationY, double speedX, double speedY) {
        return new BloodProp(locationX, locationY, speedX, speedY);
    }

}
