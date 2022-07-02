package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 道具类：
 * 火力道具、炸弹道具、加血道具
 *
 * @author zhangzewei
 */
public abstract class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(int locationX, int locationY, double speedX, double speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        } else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

    @Override
    public void update() {

    }

    /**
     * 道具效果
     * 火力道具：一段时间内英雄机改变弹道，增加攻击力
     * 炸弹道具：清除界面上除 boss 机外的所有敌机
     * 加血道具：英雄机增加生命值
     *
     * @param aircraft 道具效果作用于飞机
     */
    public abstract void propEffect(AbstractAircraft aircraft);

}
