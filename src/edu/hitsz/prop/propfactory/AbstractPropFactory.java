package edu.hitsz.prop.propfactory;

import edu.hitsz.prop.AbstractProp;

/**
 * 道具工厂的抽象父类
 *
 * @author zhangzewei
 */
public abstract class AbstractPropFactory {

    /**
     * 工厂方法，处理道具的创建
     * @param locationX x轴坐标
     * @param locationY y轴坐标
     * @param speedX x轴移动速度
     * @param speedY y轴移动速度
     * @return 对应道具对象
     */
    public abstract AbstractProp creatProp(int locationX, int locationY, double speedX, double speedY);

}
