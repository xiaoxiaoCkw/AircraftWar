package edu.hitsz.application;

import edu.hitsz.application.game.EasyGame;
import edu.hitsz.application.game.BaseGame;
import edu.hitsz.application.game.HardGame;
import edu.hitsz.application.game.NormalGame;

import javax.swing.*;
import java.awt.*;

/**
 * 程序入口
 *
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    /**
     * 对象锁
     */
    public static final Object OBJ = new Object();

    /**
     * 游戏音效开/关
     */
    public static boolean audioON;

    public static void main(String[] args) {

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建主菜单，选择难度、是否打开音效
        MainMenu mainMenu = new MainMenu();
        // 获取主菜单面板
        JPanel menuPanel =  mainMenu.getMainMenu();
        frame.add(menuPanel);
        frame.setVisible(true);

        // 等待玩家选择难度
        synchronized (OBJ) {
            try {
                OBJ.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        audioON = mainMenu.getAudioCheckBox().isSelected();

        // 根据选择的难度创建游戏
        System.out.println("Hello Aircraft War");
        BaseGame baseGame;
        switch (MainMenu.getMode()) {
            case "EASY":
                baseGame = new EasyGame();
                break;
            case "NORMAL":
                baseGame = new NormalGame();
                break;
            case "HARD":
                baseGame = new HardGame();
                break;
            default:
                return ;
        }
        frame.remove(menuPanel);
        frame.add(baseGame);
        frame.setVisible(true);
        baseGame.action();

        // 等待游戏结束
        synchronized (OBJ) {
            try {
                OBJ.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 展示得分排行榜
        ScoreRankingTable scoreRankingTable = new ScoreRankingTable();
        frame.remove(baseGame);
        frame.add(scoreRankingTable.getMainPanel());
        frame.setVisible(true);

    }

}