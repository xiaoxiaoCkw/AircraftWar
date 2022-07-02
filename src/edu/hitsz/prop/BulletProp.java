package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.music.MusicThread;
import edu.hitsz.shoot.ScatterShootStrategy;

/**
 * 火力道具
 * 一段时间内英雄机改变弹道，增加攻击力
 * 可叠加使用
 *
 * @author zhangzewei
 */
public class BulletProp extends AbstractProp {

    public BulletProp(int locationX, int locationY, double speedX, double speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void propEffect(AbstractAircraft aircraft) {
        Runnable r = () -> {
            System.out.println("FireSupply Active!");
            // 子弹数+2，直射改为散射
            aircraft.setShootNum(aircraft.getShootNum() + 2);
            aircraft.setShootStrategy(new ScatterShootStrategy());
            try {
                // 火力道具效果持续8s
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 复原
            aircraft.setShootNum(aircraft.getShootNum() - 2);
        };
        // 播放火力道具生效音效
        new MusicThread("src/audio/get_supply.wav").start();
        // 火力道具线程开始
        new Thread(r).start();
    }

}
