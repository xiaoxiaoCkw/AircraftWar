package edu.hitsz.application;

import edu.hitsz.dataaccessobject.Player;
import edu.hitsz.dataaccessobject.PlayerDAO;
import edu.hitsz.dataaccessobject.PlayerDAOImpl;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 得分排行榜面板
 *
 * @author zhangzewei
 */
public class ScoreRankingTable {

    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JTable scoreTable;
    private JLabel headerLabel;
    private JScrollPane tableScrollPanel;
    private JButton deleteButton;
    private JLabel modeName;

    public ScoreRankingTable() {

        modeName.setText("难度：" + MainMenu.getMode());

        PlayerDAO playerDAO = new PlayerDAOImpl();
        playerDAO.read();
        List<Player> players = playerDAO.getAllPlayers();

        // 表头名称
        String[] columnName = {"名次", "玩家名", "得分", "记录时间"};

        String[][] tableData = new String[players.size()][4];

        for (int i = 0; i < players.size(); i++) {
            // 将数据写入表格
            Player currentPlayer = players.get(i);
            tableData[i][0] = Integer.toString(i + 1);
            tableData[i][1] = currentPlayer.getName();
            tableData[i][2] = Integer.toString(currentPlayer.getScore());
            tableData[i][3] = currentPlayer.getTime();
        }

        // 表格模型
        DefaultTableModel model = new DefaultTableModel(tableData, columnName) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        // scoreTable从表格模型获取数据
        scoreTable.setModel(model);
        tableScrollPanel.setViewportView(scoreTable);

        // 表头设置
        JTableHeader tableHeader = scoreTable.getTableHeader();
        tableHeader.setFont(new Font("SimSong", Font.BOLD, 18));
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);

        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.DELETE) {
                    // 监听到表格发生删除操作时修改名次
                    for (int i = 0; i < players.size(); i++) {
                        model.setValueAt(i+1, i, 0);
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 选择一行删除
                int row = scoreTable.getSelectedRow();
                if (row != -1) {
                    int result = JOptionPane.showConfirmDialog(
                            mainPanel,
                            "确认删除？",
                            "选择一个选项",
                            JOptionPane.YES_NO_CANCEL_OPTION
                    );
                    if (result == JOptionPane.YES_OPTION) {
                        // 确认删除后，修改文件数据
                        players.remove(row);
                        playerDAO.write();
                        // 删除选中行
                        model.removeRow(row);
                    }
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return  mainPanel;
    }

}
