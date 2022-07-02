package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 射击策略——直射
 *
 * @author zhangzewei
 */
public class StraightShootStrategy implements ShootStrategy {

    @Override
    public List<AbstractBullet> doShoot(AbstractAircraft aircraft) {
        List<AbstractBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection()*2;
        double speedX = 0;
        double speedY = aircraft.getSpeedY() + aircraft.getDirection()*5;

        int shootNum = aircraft.getShootNum();
        AbstractBullet abstractBullet;
        for(int i = 0; i < shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            if (aircraft instanceof HeroAircraft) {
                abstractBullet = new HeroBullet();
            } else {
                abstractBullet = new EnemyBullet();
            }
            abstractBullet.setLocation(x + (i * 2 - shootNum + 1) * 10, y);
            abstractBullet.setSpeedX(speedX);
            abstractBullet.setSpeedY(speedY);
            abstractBullet.setPower(aircraft.getPower());
            res.add(abstractBullet);
        }

        return res;
    }

}
