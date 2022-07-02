package edu.hitsz.application.game;

import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemyfactory.AbstractEnemyFactory;
import edu.hitsz.aircraft.enemyfactory.EliteEnemyFactory;
import edu.hitsz.aircraft.enemyfactory.MobEnemyFactory;
import edu.hitsz.application.ImageManager;

/**
 * 简单模式
 *
 * @author zhangzewei
 */
public class EasyGame extends BaseGame {
    public EasyGame() {
        super();
        backGroundImage = ImageManager.EASY_MODE_BACKGROUND;
        enemyMaxNumber = 5;
        eliteEnemyProbability = 0.20;
        bossScoreThreshold = 1000;
        cycleDuration = 800;
        heroShootCycleDuration = 600;
        enemyShootCycleDuration = 800;
    }

    @Override
    protected AbstractEnemy enemyCreateAction() {
        AbstractEnemyFactory enemyFactory;
        double ranNum = Math.random();
        if (ranNum <= eliteEnemyProbability) {
            enemyFactory = new EliteEnemyFactory();
        } else {
            enemyFactory = new MobEnemyFactory();
        }
        return enemyFactory.createEnemy();
    }

    @Override
    protected boolean difficultyCycleJudge() {
        // 简单模式不需要提高难度
        return false;
    }

    @Override
    protected void increaseDifficulty() {

    }

}

