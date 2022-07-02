package edu.hitsz.aircraft.enemyfactory;

import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.shoot.NoShootStrategy;

/**
 * 普通敌机工厂
 *
 * @author zhangzewei
 */
public class MobEnemyFactory extends AbstractEnemyFactory {

    private static int mobHp = 30;
    private static double mobSpeedY = 5;

    @Override
    public AbstractEnemy createEnemy() {
        return new MobEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                0,
                mobSpeedY,
                mobHp,
                0,
                0,
                0,
                new NoShootStrategy()
        );
    }

    public static int getMobHp() {
        return mobHp;
    }

    public static void setMobHp(int mobHp) {
        MobEnemyFactory.mobHp = mobHp;
    }

    public static double getMobSpeedY() {
        return mobSpeedY;
    }

    public static void setMobSpeedY(double mobSpeedY) {
        MobEnemyFactory.mobSpeedY = mobSpeedY;
    }
}
