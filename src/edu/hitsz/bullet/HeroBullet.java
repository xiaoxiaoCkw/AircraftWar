package edu.hitsz.bullet;

/**
 * 英雄机子弹
 *
 * @author hitsz
 */
public class HeroBullet extends AbstractBullet {

    public HeroBullet(int locationX, int locationY, double speedX, double speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    public HeroBullet() {
        super();
    }

    @Override
    public void update() {

    }

}
