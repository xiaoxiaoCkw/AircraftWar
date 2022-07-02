package edu.hitsz.bullet;

/**
 * 敌机子弹
 *
 * @author hitsz
 */
public class EnemyBullet extends AbstractBullet {

    public EnemyBullet(int locationX, int locationY, double speedX, double speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    public EnemyBullet() {
        super();
    }

    @Override
    public void update() {
        this.vanish();
    }

}
