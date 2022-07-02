package edu.hitsz.aircraft.enemyfactory;

import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.shoot.ScatterShootStrategy;

/**
 * Boss 敌机工厂
 *
 * @author zhangzewei
 */
public class BossEnemyFactory extends AbstractEnemyFactory {
    private static int bossHp = 500;
    @Override
    public AbstractEnemy createEnemy() {
        return new BossEnemy(
                Main.WINDOW_WIDTH / 2,
                ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2,
                5,
                0,
                bossHp,
                3,
                10,
                1,
                new ScatterShootStrategy()
        );
    }
    public static void setBossHp(int hp) {
        bossHp = hp;
    }
    public static int getBossHp() {
        return bossHp;
    }
}
