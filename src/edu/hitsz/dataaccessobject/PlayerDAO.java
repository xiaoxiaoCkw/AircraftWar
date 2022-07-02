package edu.hitsz.dataaccessobject;

import java.util.List;

/**
 * 数据访问对象接口，该接口定义了在一个模型对象上要执行的标准操作
 *
 * @author zhangzewei
 */
public interface PlayerDAO {

    /**
     * 获取全部玩家对象的集合
     *
     * @return 玩家对象的集合
     */
    List<Player> getAllPlayers();

    /**
     * 添加玩家对象
     *
     * @param player 需要添加的玩家对象
     */
    void addPlayer(Player player);

    /**
     * 向数据源写入数据
     */
    void write();

    /**
     * 从数据源读出数据
     */
    void read();

}
