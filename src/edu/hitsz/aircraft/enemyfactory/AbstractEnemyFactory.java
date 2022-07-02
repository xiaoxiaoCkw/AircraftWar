package edu.hitsz.aircraft.enemyfactory;

import edu.hitsz.aircraft.enemy.AbstractEnemy;

/**
 * 所有敌机工厂的抽象父类
 *
 * @author zhangzewei
 */
public abstract class AbstractEnemyFactory {

    /**
     * 工厂方法，处理敌机创建
     * @return 对应敌机对象
     */
    public abstract AbstractEnemy createEnemy();

}
