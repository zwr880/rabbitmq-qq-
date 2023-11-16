package com.ss.ui;

import com.ss.tools.show.Users;
import com.ss.tools.util.ImageUrlMysql;
import com.ss.tools.util.UserListMysql;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;



public class ChatMainFrame extends JFrame {

    public JPanel userPanel;
    public JPanel mainPanel;
    public JLabel lb,lb1,groupLb;
    public JPanel labelPanel;
    private ChatMainFramePanel chatPanel;
    private ChatMainFrameGroupPanel groupChatPanel;

    public ChatMainFrame(String username) throws SQLException {
        final String name=username;
        setTitle("Main Chat Frame "+name+" welcome!!!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(620,280);
        setSize(650,550);
        setResizable(false);
        // 用户列表界面
        userPanel=new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx =0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(1, 1, 1, 1);  // 设置组件间距为零
        ImageUrlMysql imageUrlMysql = new ImageUrlMysql(name);
        ImageIcon image1 = new ImageIcon(imageUrlMysql.ImgUrl);
        image1.setImage(image1.getImage().getScaledInstance(100, 90, Image.SCALE_DEFAULT));
        lb = new JLabel(image1);
        lb1=new JLabel("<html><a>联系人列表</a></html>");
        lb1.setForeground(new Color(28,123,238));
        lb1.setFont(new Font("宋体",Font.PLAIN,18));
        userPanel.add(lb,gbc);
        gbc.gridy++;
        userPanel.add(lb1,gbc);
        gbc.gridy++;
        UserListMysql users=new UserListMysql(name);
        List<Users> userList=users.init();
        for (final Users user : userList) {
            ImageIcon image=new ImageIcon(user.getAvatarUrl());
            image.setImage(image.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
            JLabel avatarLabel = new JLabel(image);
            avatarLabel.setBorder(new LineBorder(Color.BLACK, 1));  // 添加边框，可去掉或自定义
            final JLabel nameLabel = new JLabel(user.getUsername());
            nameLabel.setForeground(new Color(28, 12, 2));
            nameLabel.setFont(new Font("宋体", Font.PLAIN, 16));
            // 创建一个面板，使用FlowLayout，让头像和用户名在同一行
            JPanel userInfoPanel = new JPanel();
            userInfoPanel.add(avatarLabel);
            userInfoPanel.add(nameLabel);
            nameLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(!nameLabel.getText().equals("SDA群")){
                        chatPanel=new ChatMainFramePanel(nameLabel.getText(),name);
                        System.out.println("DuiName"+nameLabel.getText());
                        System.out.println("MyName"+name);
                        switchMainPanelContent(chatPanel);
                    }else{
                        groupChatPanel=new ChatMainFrameGroupPanel(nameLabel.getText(),name);
                        System.out.println("GroupName"+nameLabel.getText());
                        System.out.println("MyName"+name);
                        switchMainPanelContent(groupChatPanel);
                    }

                }
                @Override
                public void mousePressed(MouseEvent e) {

                }
                @Override
                public void mouseReleased(MouseEvent e) {
                }
                @Override
                public void mouseEntered(MouseEvent e) {

                }
                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            userPanel.add(userInfoPanel,gbc);
            gbc.gridy++;
        }
        JScrollPane scrollPane=new JScrollPane(userPanel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.NORTH);
        setLayout(new BorderLayout());
        add(panel,BorderLayout.WEST);
        mainPanel=new JPanel();
        add(mainPanel,BorderLayout.CENTER);
    }
    private void switchMainPanelContent(JPanel content) {
        content.setPreferredSize(mainPanel.getSize());
        mainPanel.removeAll();
        mainPanel.add(content);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ChatMainFrame("Zhao_wanru").setVisible(true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }
}
