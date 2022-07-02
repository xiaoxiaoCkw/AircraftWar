package edu.hitsz.aircraft.enemyfactory;

import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.shoot.StraightShootStrategy;

/**
 * 精英敌机工厂
 *
 * @author zhangzewei
 */
public class EliteEnemyFactory extends AbstractEnemyFactory {

    private static int eliteHp = 60;
    private static double eliteSpeedX = 9;
    private static double eliteSpeedY = 5;

    @Override
    public AbstractEnemy createEnemy() {
        return new EliteEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                eliteSpeedX,
                eliteSpeedY,
                eliteHp,
                1,
                10,
                1,
                new StraightShootStrategy()
        );
    }

    public static int getEliteHp() {
        return eliteHp;
    }

    public static void setEliteHp(int eliteHp) {
        EliteEnemyFactory.eliteHp = eliteHp;
    }

    public static double getEliteSpeedX() {
        return eliteSpeedX;
    }

    public static void setEliteSpeedX(double eliteSpeedX) {
        EliteEnemyFactory.eliteSpeedX = eliteSpeedX;
    }

    public static double getEliteSpeedY() {
        return eliteSpeedY;
    }

    public static void setEliteSpeedY(double eliteSpeedY) {
        EliteEnemyFactory.eliteSpeedY = eliteSpeedY;
    }
}
