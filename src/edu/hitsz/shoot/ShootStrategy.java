package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.AbstractBullet;

import java.util.List;

/**
 * 射击策略接口，所有具体射击策略都要实现该接口
 *
 * @author zhangzewei
 */
public interface ShootStrategy {

    /**
     * 执行射击策略的方法
     *
     * @param aircraft 执行射击的飞机对象
     * @return 子弹集合
     */
    List<AbstractBullet> doShoot(AbstractAircraft aircraft);

}
