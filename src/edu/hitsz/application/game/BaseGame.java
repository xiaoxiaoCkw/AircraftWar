package edu.hitsz.application.game;

import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.enemy.BossEnemy;
import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.MobEnemy;
import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.dataaccessobject.Player;
import edu.hitsz.dataaccessobject.PlayerDAO;
import edu.hitsz.dataaccessobject.PlayerDAOImpl;
import edu.hitsz.music.MusicThread;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BombProp;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class BaseGame extends JPanel {

    protected BufferedImage backGroundImage;
    protected int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    protected final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected final int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractEnemy> enemyAircrafts;
    protected final List<AbstractBullet> heroBullets;
    protected final List<AbstractBullet> enemyBullets;
    protected final List<AbstractProp> props;

    /**
     * 敌机数量最大值
     */
    protected int enemyMaxNumber;

    /**
     * 判断Boss敌机是否存在
     * 由于只能存在一个Boss敌机，当集合enemyAircrafts中没有Boss敌机时可以创建，否则不能创建
     */
    protected boolean bossExistFlag = false;

    /**
     * 判断得分是否达到阈值
     */
    protected boolean scoreFlag = false;

    /**
     * 判断Boss敌机是否坠毁
     */
    protected static boolean bossCrashFlag = false;
    /**
     * 判断游戏是否结束
     */
    protected static boolean gameOverFlag = false;

    protected int score = 0;
    protected int time = 0;

    /**
     * 用于记录产生Boss机的得分
     */
    protected int resetScore = 0;

    /**
     * 周期 (ms)
     * 指示敌机的产生频率
     */
    protected int cycleDuration;
    protected int cycleTime = 0;

    /**
     * 周期 (ms)
     * 指示英雄机射击的频率
     */
    protected int heroShootCycleDuration;
    protected int heroShootCycleTime = 0;

    /**
     * 周期 (ms)
     * 指示敌机射击的频率
     */
    protected int enemyShootCycleDuration;
    protected int enemyShootCycleTime = 0;

    /**
     * 周期 (ms)
     * 指示提升难度的频率
     */
    protected int difficultyCycleDuration;
    protected int difficultyCycleTime = 0;

    /**
     * 精英敌机产生概率
     */
    protected double eliteEnemyProbability;

    /**
     * 精英敌机产生最大概率
     */
    protected double eliteEnemyMaxProbability;

    /**
     * Boss敌机产生的得分阈值
     */
    protected int bossScoreThreshold;

    public BaseGame() {
        // 使用单例模式创建英雄机对象
        heroAircraft = HeroAircraft.getHeroAircraft();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     * 模版方法
     */
    public final void action() {

        // 播放游戏背景音乐
        new MusicThread("src/audio/bgm.wav").start();

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
//                System.out.println(time);
                // 新敌机产生
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    enemyAircrafts.add(enemyCreateAction());
                }
            }

            // 周期性执行（控制频率）
            if (heroAircraftShootCycleJudge()) {
                // 英雄机射出子弹
                heroAircraftShootAction();
            }

            // 周期性执行（控制频率）
            if (enemyShootCycleJudge()) {
                // 敌机射出子弹
                enemyShootAction();
            }

            // 周期性执行（控制频率）
            if (difficultyCycleJudge()) {
                // 难度提升
                increaseDifficulty();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            // 每个时刻重绘界面
            repaint();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                // 播放游戏结束音效
                new MusicThread("src/audio/game_over.wav").start();
                executorService.shutdown();
                gameOverFlag = true;
                System.out.println("Game Over!");
                // 玩家可选择是否记录得分，输入名字记录得分
                recordPlayer();
                synchronized (Main.OBJ) {
                    // 唤醒等待的Main线程
                    Main.OBJ.notify();
                }
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    protected boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 产生敌机
     *
     * @return 敌机
     */
    protected abstract AbstractEnemy enemyCreateAction();

    protected boolean heroAircraftShootCycleJudge() {
        heroShootCycleTime += timeInterval;
        if (heroShootCycleTime >= heroShootCycleDuration && heroShootCycleTime - timeInterval < heroShootCycleTime) {
            // 跨越到新的周期
            heroShootCycleTime %= heroShootCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    protected void heroAircraftShootAction() {
        // 英雄机射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    protected boolean difficultyCycleJudge() {
        difficultyCycleTime += timeInterval;
        if (difficultyCycleTime >= difficultyCycleDuration && difficultyCycleTime - timeInterval < difficultyCycleTime) {
            // 跨越到新的周期
            difficultyCycleTime %= difficultyCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 提高难度
     */
    protected abstract void increaseDifficulty();

    protected boolean enemyShootCycleJudge() {
        enemyShootCycleTime += timeInterval;
        if (enemyShootCycleTime >= enemyShootCycleDuration && enemyShootCycleTime - timeInterval < enemyShootCycleTime) {
            // 跨越到新的周期
            enemyShootCycleTime %= enemyShootCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    protected void enemyShootAction() {
        // TODO 敌机射击
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyBullets.addAll(enemyAircraft.shoot());
        }
    }

    protected void bulletsMoveAction() {
        for (AbstractBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (AbstractBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    protected void aircraftsMoveAction() {
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    protected void propsMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    protected void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for (AbstractBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                // 已击中或者失效的子弹，不再检测
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }
        // 英雄子弹攻击敌机
        for (AbstractBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    // 播放敌机被击中音效
                    new MusicThread("src/audio/bullet_hit.wav").start();
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        scoreIncrease(enemyAircraft);
                        // 掉落道具
                        props.addAll(enemyAircraft.dropProps());
                        if (resetScore >= bossScoreThreshold) {
                            // 分数达到Boss敌机产生阈值
                            scoreFlag = true;
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                // 道具已生效或者出界
                continue;
            }
            if (prop.crash(heroAircraft)) {
                // 道具碰撞到英雄机后生效
                if (prop instanceof BombProp) {
                    // 如果是炸弹道具，则添加敌机和敌机子弹为观察者
                    for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                        if (enemyAircraft.notValid()) {
                            continue;
                        }
                        ((BombProp) prop).addSubscriber(enemyAircraft);
                    }
                    for (AbstractBullet bullet : enemyBullets) {
                        if (bullet.notValid()) {
                            continue;
                        }
                        ((BombProp) prop).addSubscriber(bullet);
                    }
                    prop.propEffect(heroAircraft);
                    prop.vanish();
                    for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                        if (enemyAircraft.notValid()) {
                            scoreIncrease(enemyAircraft);
                            if (resetScore >= bossScoreThreshold) {
                                // 分数达到Boss敌机产生阈值
                                scoreFlag = true;
                            }
                        }
                    }
                } else {
                    prop.propEffect(heroAircraft);
                    prop.vanish();
                }
            }
        }

    }

    protected void scoreIncrease(AbstractEnemy enemyAircraft) {
        if (enemyAircraft instanceof MobEnemy) {
            // 击毁普通敌机加10分
            score += 10;
            if (!bossExistFlag) {
                resetScore += 10;
            }
        } else if (enemyAircraft instanceof EliteEnemy) {
            // 击毁精英敌机加30分
            score += 30;
            if (!bossExistFlag) {
                resetScore += 30;
            }
        } else if (enemyAircraft instanceof BossEnemy) {
            // Boss敌机坠毁
            bossCrashFlag = true;
            bossExistFlag = false;
            // 击毁Boss敌机加100分
            score += 100;
        }
    }

    /**
     * 弹出对话框，可选择输入名字记录得分
     */
    protected void recordPlayer() {
        String name;
        do {
            name = JOptionPane.showInputDialog(
                    this,
                    "游戏结束，你的得分为" + score + ".\n请输入名字记录得分：",
                    "输入",
                    JOptionPane.QUESTION_MESSAGE
            );
            if ("".equals(name)) {
                // 未输入名字时提示重新输入
                JOptionPane.showMessageDialog(
                        this,
                        "哎呀，好像忘记输入名字了，请重新输入～",
                        "温馨提示",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } while ("".equals(name));

        if (name != null) {
            // 如果选择记录得分
            PlayerDAO playerDAO = new PlayerDAOImpl();

            Player currentPlayer = new Player();

            // 获取游戏结束时的系统时间
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");

            currentPlayer.setName(name);
            currentPlayer.setScore(score);
            currentPlayer.setTime(dateFormat.format(date));

            // 将玩家数据写入文件
            playerDAO.read();
            playerDAO.addPlayer(currentPlayer);
            playerDAO.write();
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效的道具
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    protected void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(backGroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(backGroundImage, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        // 绘制道具
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);

        paintImageWithPositionRevised(g, props);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    protected void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    protected void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("Chalkboard", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

    public static boolean isBossCrash() {
        return bossCrashFlag;
    }

    public static boolean isGameOver() {
        return gameOverFlag;
    }

}
