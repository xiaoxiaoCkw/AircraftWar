package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.music.MusicThread;
import edu.hitsz.observerpattern.Subscriber;

import java.util.LinkedList;
import java.util.List;

/**
 * 炸弹道具
 * 清除界面上除 boss 机外的所有敌机
 * 观察者模式——发布者
 *
 * @author zhangzewei
 */
public class BombProp extends AbstractProp {

    /**
     * 观察者列表
     */
    private final List<Subscriber> subscriberList = new LinkedList<>();

    public BombProp(int locationX, int locationY, double speedX, double speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void propEffect(AbstractAircraft aircraft) {
        System.out.println("BombSupply Active!");
        // 播放炸弹道具生效音效
        new MusicThread("src/audio/bomb_explosion.wav").start();
        notifyUpdate();
    }

    /**
     * 添加观察者
     *
     * @param subscriber 观察者
     */
    public void addSubscriber(Subscriber subscriber) {
        subscriberList.add(subscriber);
    }

    /**
     * 删除观察者
     *
     * @param subscriber 观察者
     */
    public void removeSubscriber(Subscriber subscriber) {
        subscriberList.remove(subscriber);
    }

    /**
     * 通知所有观察者
     */
    public void notifyUpdate() {
        for (Subscriber subscriber : subscriberList) {
            subscriber.update();
        }
    }


}
