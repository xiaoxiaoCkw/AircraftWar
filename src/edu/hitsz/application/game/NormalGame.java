package edu.hitsz.application.game;

import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.MobEnemy;
import edu.hitsz.aircraft.enemyfactory.AbstractEnemyFactory;
import edu.hitsz.aircraft.enemyfactory.BossEnemyFactory;
import edu.hitsz.aircraft.enemyfactory.EliteEnemyFactory;
import edu.hitsz.aircraft.enemyfactory.MobEnemyFactory;
import edu.hitsz.application.ImageManager;
import edu.hitsz.music.MusicThread;

/**
 * 普通模式
 *
 * @author zhangzewei
 */
public class NormalGame extends BaseGame {
    public NormalGame() {
        super();
        backGroundImage = ImageManager.NORMAL_MODE_BACKGROUND;
        enemyMaxNumber = 6;
        eliteEnemyProbability = 0.20;
        eliteEnemyMaxProbability = 0.50;
        bossScoreThreshold = 600;
        cycleDuration = 600;
        heroShootCycleDuration = 600;
        enemyShootCycleDuration = 600;
        difficultyCycleDuration = 9000;
    }

    @Override
    protected AbstractEnemy enemyCreateAction() {
        AbstractEnemyFactory enemyFactory;
        if (!bossExistFlag && scoreFlag) {
            // 当前不存在Boss敌机且得分达到Boss机产生阈值，则产生Boss敌机
            bossCrashFlag = false;
            // 播放Boss敌机背景音乐
            new MusicThread("src/audio/bgm_boss.wav").start();
            enemyFactory = new BossEnemyFactory();
            System.out.println("Boss敌机血量：" + BossEnemyFactory.getBossHp());
            bossExistFlag = true;
            scoreFlag = false;
            resetScore = 0;
        } else {
            // 否则，产生普通敌机或者精英敌机
            double ranNum = Math.random();
            if (ranNum <= eliteEnemyProbability) {
                enemyFactory = new EliteEnemyFactory();
            } else {
                enemyFactory = new MobEnemyFactory();
            }
        }
        return enemyFactory.createEnemy();
    }

    @Override
    protected void increaseDifficulty() {
        // 普通模式每隔9s提高一次难度
        System.out.print("提高难度! ");
        if (eliteEnemyProbability <= eliteEnemyMaxProbability) {
            eliteEnemyProbability += 0.01;
        } else {
            System.out.print("精英敌机产生概率不再提升, ");
        }
        System.out.print("精英敌机概率: " + String.format("%.2f, ", eliteEnemyProbability));

        EliteEnemyFactory.setEliteHp(EliteEnemyFactory.getEliteHp() + 1);
        MobEnemyFactory.setMobHp(MobEnemyFactory.getMobHp() + 1);
        System.out.print("敌机血量增加, ");

        EliteEnemyFactory.setEliteSpeedX(EliteEnemyFactory.getEliteSpeedX() + 0.3);
        EliteEnemyFactory.setEliteSpeedY(EliteEnemyFactory.getEliteSpeedY() + 0.3);
        MobEnemyFactory.setMobSpeedY(MobEnemyFactory.getMobSpeedY() + 0.5);
        System.out.println("敌机速度增加.");
    }

}
