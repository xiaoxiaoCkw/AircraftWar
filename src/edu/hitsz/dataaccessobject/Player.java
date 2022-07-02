package edu.hitsz.dataaccessobject;

import java.io.Serializable;

/**
 * 数值对象（Value Object）—— 玩家
 *
 * @author zhangzewei
 */
public class Player implements Serializable {
    private String name;
    private int score;
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return name + ", " + score + ", " + time;
    }
}
