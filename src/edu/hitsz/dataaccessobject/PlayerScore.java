package edu.hitsz.dataaccessobject;

import java.util.Comparator;

/**
 * 集合排序比较器
 * 根据得分降序排序
 *
 * @author zhangzewei
 */
public class PlayerScore implements Comparator<Player> {

    @Override
    public int compare(Player p1, Player p2) {
        return Integer.compare(p2.getScore(), p1.getScore());
    }

}
