package com.ss.ui;

import com.ss.tools.util.RegisterMysql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField RePasswordField;
    private JLabel lb,lb1,lb2,lb3,lb4,lb5;
    private JButton RegisterButton;
    public String username;
    public String password;
    public String ImgUrl;
    public String RePassword;
    private ImageIcon image2;
    public RegisterFrame(){
        setSize(540,440);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(620,280);
        setResizable(false);
        ImageIcon image1=new ImageIcon("image/background.png");
        image1.setImage(image1.getImage().getScaledInstance(540,150,Image.SCALE_DEFAULT));
        image2=new ImageIcon("image/R.jpg");
        image2.setImage(image2.getImage().getScaledInstance(140,140,Image.SCALE_DEFAULT));
        lb =new JLabel(image1);//上半部分
        lb4=new JLabel(image2);//头像部分
        lb5=new JLabel("<html><a>更换头像</a></html>");
        lb5.setForeground(new Color(23,2,23));
        lb5.setFont(new Font("宋体",Font.PLAIN,13));
        final JPanel panelA=new JPanel();
        usernameField=new JTextField(20);
        passwordField=new JPasswordField(20);
        RePasswordField=new JPasswordField(20);
        lb1=new JLabel("<html><a>用户名</a></html>");
        lb1.setForeground(new Color(28,134,238));
        lb1.setFont(new Font("宋体",Font.PLAIN,16));
        lb2=new JLabel("<html><a>密码</a></html>");
        lb2.setForeground(new Color(28,134,238));
        lb2.setFont(new Font("宋体",Font.PLAIN,16));
        lb3=new JLabel("<html><a>确认密码</a></html>");
        lb3.setForeground(new Color(28,134,238));
        lb3.setFont(new Font("宋体",Font.PLAIN,16));
        RegisterButton=new JButton("注册");
        panelA.add(lb);
        panelA.add(lb4);
        panelA.add(lb5);
        panelA.add(lb1);
        panelA.add(lb2);
        panelA.add(lb3);
        panelA.add(usernameField);
        panelA.add(passwordField);
        panelA.add(RePasswordField);
        panelA.add(RegisterButton);
        panelA.setSize(540,190);
        panelA.setLayout(null);
        panelA.setBackground(Color.white);
        lb4.setBounds(43,8,100,100);
        lb5.setBounds(60,70,100,100);
        usernameField.setBounds(160,14,250,37);
        passwordField.setBounds(160,50,250,37);
        RePasswordField.setBounds(160,84,250,37);
        usernameField.setFont(new Font("宋体",Font.PLAIN,16));
        passwordField.setFont(new Font("宋体",Font.PLAIN,16));
        RePasswordField.setFont(new Font("宋体",Font.PLAIN,16));
        lb1.setBounds(420,10,80,34);
        lb2.setBounds(420,50,80,34);
        lb3.setBounds(420,90,80,34);
        RegisterButton.setBounds(160,130,250,37);
        RegisterButton.setBackground(new Color(0,176,239));
        RegisterButton.setForeground(Color.white);
        this.add(lb,BorderLayout.NORTH);
        this.add(panelA,BorderLayout.CENTER);
        lb5.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                choseImg();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        repaint(); // 在事件分发线程中刷新窗体
                    }
                });
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
        RegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username=usernameField.getText();
                password=String.valueOf(passwordField.getPassword());
                RePassword=String.valueOf(RePasswordField.getPassword());
                System.out.println(password);
                System.out.println(RePassword);
                if(!username.isEmpty()&&!password.isEmpty()&&!RePassword.isEmpty()) {
                    if (RePassword.equals(password)) {
                        //插入
                        try {
                            RegisterMysql registerMysql = new RegisterMysql(username, password, ImgUrl);
                            if (registerMysql.i == 1) {
                                //打开聊天界面
                                openChatUI(username);
                            } else {
                                JOptionPane.showMessageDialog(RegisterFrame.this, "register failed");
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(RegisterFrame.this, "password is different");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(RegisterFrame.this,"register failed");
                }
            }
        });
    }
    private void openChatUI(String username) throws SQLException {
        dispose(); // 关闭登录窗口
        ChatMainFrame chatUI=new ChatMainFrame(username);
        chatUI.setVisible(true);
    }
    private void choseImg(){
        JFileChooser jf=new JFileChooser("./");//在当前目录下找
        jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int val=jf.showOpenDialog(null);
        if(val==jf.APPROVE_OPTION){
            //正常选择文件
            File file = jf.getSelectedFile();
            if(file.isDirectory()){
                JOptionPane.showMessageDialog(RegisterFrame.this,"请选择图片");
            }
            else{
                ImgUrl=file.getAbsolutePath();
                image2=new ImageIcon(ImgUrl);
                image2.setImage(image2.getImage().getScaledInstance(140,140,Image.SCALE_DEFAULT));
                lb4.setIcon(image2);
            }
        }
        else{
            JOptionPane.showMessageDialog(RegisterFrame.this,"请选择图片");
        }

    }
}
