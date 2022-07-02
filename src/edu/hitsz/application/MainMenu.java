package edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 主菜单面板
 *
 * @author zhangzewei
 */
public class MainMenu {

    private JPanel mainMenu;
    private JButton easyModeButton;
    private JButton hardModeButton;
    private JButton normalModeButton;
    private JCheckBox audioCheckBox;

    /**
     * 选择的模式
     * 简单(EASY)
     * 普通(NORMAL)
     * 困难(HARD)
     */
    private static String mode;

    public MainMenu() {
        easyModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = "EASY";
                synchronized (Main.OBJ) {
                    // 唤醒等待的Main线程
                    Main.OBJ.notify();
                }
            }
        });
        normalModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = "NORMAL";
                synchronized (Main.OBJ) {
                    // 唤醒等待的Main线程
                    Main.OBJ.notify();
                }
            }
        });
        hardModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = "HARD";
                synchronized (Main.OBJ) {
                    // 唤醒等待的Main线程
                    Main.OBJ.notify();
                }
            }
        });
    }

    public JPanel getMainMenu() {
        return mainMenu;
    }

    public JCheckBox getAudioCheckBox() {
        return audioCheckBox;
    }

    public static String getMode() {
        return mode;
    }

}
