package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;

/**
 * 加血道具
 * 英雄机增加生命值
 *
 * @author zhangzewei
 */
public class BloodProp extends AbstractProp {

    public BloodProp(int locationX, int locationY, double speedX, double speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void propEffect(AbstractAircraft aircraft) {
        System.out.println("BloodSupply Active!");
        aircraft.increaseHp(30);
    }

}
