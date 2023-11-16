package com.ss.ui;

import com.ss.tools.util.LoginMysql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame{

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel lb,lb1,lb2,lb3;
    public String username;
    public String password;
    private JLabel registerLabel;
    public  LoginFrame(){
        setSize(540,440);
        setTitle("producer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(620,280);
        setResizable(false);
        ImageIcon image1=new ImageIcon("image/background.png");
        image1.setImage(image1.getImage().getScaledInstance(540,200,Image.SCALE_DEFAULT));
        lb =new JLabel(image1);//上半部分
        JPanel panelA=new JPanel();
        ImageIcon image2=new ImageIcon("image/R.jpg");
        image2.setImage(image2.getImage().getScaledInstance(140,140,Image.SCALE_DEFAULT));
        lb3=new JLabel(image2);//头像部分
        usernameField=new JTextField(20);
        passwordField=new JPasswordField(20);
        lb1=new JLabel("<html><a>用户名</a></html>");
        lb1.setForeground(new Color(28,134,238));
        lb1.setFont(new Font("宋体",Font.PLAIN,16));
        lb2=new JLabel("<html><a>密码</a></html>");
        lb2.setForeground(new Color(28,134,238));
        lb2.setFont(new Font("宋体",Font.PLAIN,16));
        loginButton=new JButton("login");
        registerLabel=new JLabel("<html><a>注册账号</a></html>");
        registerLabel.setForeground(new Color(28,134,238));
        registerLabel.setFont(new Font("宋体",Font.PLAIN,16));
        panelA.add(lb);
        panelA.add(lb1);
        panelA.add(lb2);
        panelA.add(lb3);
        panelA.add(registerLabel);
        panelA.add(usernameField);
        panelA.add(passwordField);
        panelA.add(loginButton);
        panelA.setSize(540,190);
        panelA.setLayout(null);
        panelA.setBackground(Color.white);
        lb3.setBounds(43,8,100,100);
        usernameField.setBounds(160,14,250,37);
        passwordField.setBounds(160,48,250,37);
        usernameField.setFont(new Font("宋体",Font.PLAIN,16));
        passwordField.setFont(new Font("宋体",Font.PLAIN,16));
        lb1.setBounds(420,10,80,34);
        lb2.setBounds(420,50,80,34);
        registerLabel.setBounds(320,90,120,20);
        loginButton.setBounds(160,130,250,37);
        loginButton.setBackground(new Color(0,176,239));
        loginButton.setForeground(Color.white);
        this.add(lb,BorderLayout.NORTH);
        this.add(panelA,BorderLayout.CENTER);
        //注册，设置头像
         registerLabel.addMouseListener(new MouseListener() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 openSingUpUI();
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
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username=usernameField.getText();
                password = String.valueOf(passwordField.getPassword());
                if(!username.isEmpty()&&!(password.isEmpty())){
                    //登录mysql
                    try {
                        LoginMysql loginMysql=new LoginMysql(username,password);
                        if(loginMysql.i==1){
                            openChatUI(username);
                        }
                        else{
                            JOptionPane.showMessageDialog(LoginFrame.this,"login failed");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }
               else{
                   JOptionPane.showMessageDialog(LoginFrame.this,"login failed");
               }
               usernameField.setText("");
               passwordField.setText("");
            }
        });
        add(panelA);
    }
    private void openSingUpUI(){
        dispose();
        RegisterFrame registerFrame=new RegisterFrame();
        registerFrame.setVisible(true);
    }

    private void openChatUI(String username) throws SQLException {
        dispose(); // 关闭登录窗口
        ChatMainFrame chatUI=new ChatMainFrame(username);
        chatUI.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                new LoginFrame().setVisible(true);
            }
        });
    }
}
