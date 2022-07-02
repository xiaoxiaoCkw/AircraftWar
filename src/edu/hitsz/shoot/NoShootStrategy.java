package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.AbstractBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 射击策略——不射击
 *
 * @author zhangzewei
 */
public class NoShootStrategy implements ShootStrategy {

    @Override
    public List<AbstractBullet> doShoot(AbstractAircraft aircraft) {
        return new LinkedList<>();
    }

}
