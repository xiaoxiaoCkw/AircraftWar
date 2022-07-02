package edu.hitsz.observerpattern;

/**
 * 订阅者接口
 *
 * @author zhangzewei
 */
public interface Subscriber {
    /**
     * 更新
     * 只有敌机和敌机子弹需要更新(清除)，其他飞行物空实现
     */
    void update();
}
