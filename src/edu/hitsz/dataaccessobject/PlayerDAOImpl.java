package edu.hitsz.dataaccessobject;

import edu.hitsz.application.MainMenu;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据访问对象实体类
 * 数据源 —— 文件
 *
 * @author zhangzewei
 */
public class PlayerDAOImpl implements PlayerDAO {

    private List<Player> players = new LinkedList<>();

    @Override
    public List<Player> getAllPlayers() {
        return players;
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public void write() {
        try {
            // 写入前先排序（降序）
            players.sort(new PlayerScore());
            // 对象序列化
            File file = chooseFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(players);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void read() {
        try {
            File file = chooseFile();
            if (!file.exists()) {
                // 文件不存在则创建
                file.createNewFile();
            } else {
                if (file.length() > 0) {
                    // 文件不为空时进行对象反序列化
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                    players = (List<Player>) ois.readObject();
                    ois.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据选择的难度，选择读写的文件
     *
     * @return 需要读写的文件
     */
    private File chooseFile() {
        File file;
        switch (MainMenu.getMode()) {
            case "EASY":
                file = new File("Easy_players.txt");
                break;
            case "NORMAL":
                file = new File("Normal_players.txt");
                break;
            default:
                file = new File("Hard_players.txt");
                break;
        }
        return file;
    }

}
