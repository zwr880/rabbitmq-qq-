package com.ss.ui;

import com.rabbitmq.client.*;
import com.ss.send.SendGroupFactory;
import com.ss.send.SendGroupFileFactory;
import com.ss.send.sendFileFactory;
import com.ss.tools.file.ReadFile;
import com.ss.tools.file.ReserveFile;
import com.ss.tools.show.ShowTool;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class ChatMainFrameGroupPanel extends JPanel {

    public JTextField messageField;
    private JTextField fileField;
    public JTextField ReFileField;
    public JTextPane chatArea;
    private JButton sendMessageButton;
    private  JButton sendFileButton;
    private JButton chooseFileButton;
    private JButton connectButton;
    private JButton searchFileButton;
    private JButton reviewButton;
    private static String QUEUE_NAME;
    private static String BIND;
    private static final String EXCHANGE_NAME="amq.fanout";
    private String messageType;
    public String msg;
    public String name;
    public String groupName;
    private SendGroupFactory sendGroupFactory;
    private File file;
    private SendGroupFileFactory sendGroupFileFactory;
    public ShowTool showTool=new ShowTool();

    public ChatMainFrameGroupPanel(String groupName,String username){
        this.groupName=groupName;
        this.name=username;
        this.setLayout(new BorderLayout());
        initPanel2();
    }
    public void initPanel2() {
        JLabel chatLabel=new JLabel("chat area");
        chatLabel.setForeground(new Color(28,123,235));
        chatLabel.setFont(new Font("宋体",Font.PLAIN,14));
        chatArea = new JTextPane();
        HTMLEditorKit kit = new HTMLEditorKit();
        chatArea.setEditorKit(kit);
        chatArea.setEditable(false);
        chatArea.setContentType("text/html");
        JScrollPane scrollPane = new JScrollPane(chatArea);
        JPanel chatShowPanel=new JPanel();
        chatShowPanel.setLayout(new GridLayout());
        GridBagConstraints gri=new GridBagConstraints();
        gri.fill=GridBagConstraints.HORIZONTAL;
        gri.insets=new Insets(1,1,1,1);
        gri.gridx=0;
        gri.gridy=0;
        chatShowPanel.add(scrollPane,gri);
        JPanel messageInputPanel = new JPanel();
        messageInputPanel.setLayout(new GridBagLayout());
        messageField = new JTextField();
        sendMessageButton = new JButton("Send Message");
        fileField = new JTextField();
        sendFileButton = new JButton("Send File");
        chooseFileButton=new JButton("...");
        connectButton=new JButton("receive");
        ReFileField=new JTextField();
        searchFileButton=new JButton("ViewAndSearch");
        reviewButton=new JButton("ViewAndMessage");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        messageInputPanel.add(messageField, gbc);

        gbc.gridx=2;
        gbc.gridwidth=1;
        gbc.weightx=0.0;
        messageInputPanel.add(connectButton,gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0; // Don't expand
        messageInputPanel.add(sendMessageButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        messageInputPanel.add(fileField, gbc);

        gbc.gridx=2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0; // Don't expand
        messageInputPanel.add(chooseFileButton,gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0; // Don't expand
        messageInputPanel.add(sendFileButton, gbc);

        gbc.gridx=0;
        gbc.gridy=2;
        gbc.gridwidth=2;
        gbc.weightx=0.7;
        messageInputPanel.add(ReFileField,gbc);


        gbc.gridx=2;
        gbc.gridwidth=1;
        gbc.weightx=0.0;
        messageInputPanel.add(reviewButton,gbc);

        gbc.gridx=3;
        gbc.gridwidth=1;
        gbc.weightx=0.0;
        messageInputPanel.add(searchFileButton,gbc);

        //试把消息区域改为两个，一个发送区域，一个接收区域,一左一右
        this.add(chatLabel,BorderLayout.NORTH);
        this.add(chatShowPanel,BorderLayout.CENTER);
        this.add(messageInputPanel,BorderLayout.SOUTH);
        this.revalidate();
        this.repaint();
        chooseFileButton.addActionListener(new ActionListener() {//选择文件
            @Override
            public void actionPerformed(ActionEvent e) {
                showTool.choseFile(fileField,this);
                file=showTool.file;
            }
        });
        sendMessageButton.addActionListener(new ActionListener() {//发送群消息
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        sendFileButton.addActionListener(new ActionListener() {//发送文件
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendFileMessage();
                } catch (SQLException | FileNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receiveWork();
            }
        });
        searchFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchFile();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });
        reviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    reviewMsg();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }
    private void sendMessage() throws SQLException {
        msg=messageField.getText();
        //刷新输入框
        messageField.setText("");
        //聊天界面内容显示
        showTool.appendJTextArea(msg,chatArea,name,groupName,"right");
        //基于Rabbitmq发送短信
        sendGroupFactory=new SendGroupFactory(msg,name);
    }
    private void sendFileMessage() throws SQLException, FileNotFoundException {
        msg=file.getPath();
        //显示已发送
        showTool.appendJTextArea(msg,chatArea,name,groupName,"right");
        //刷新文件框
        fileField.setText("");
        //读取文件内容
//            ReadFile readFile=new ReadFile(msg);
        //发送信息
         sendGroupFileFactory=new SendGroupFileFactory(msg,name);
    }
    private void receiveWork(){
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("114.132.150.220");//?
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("Zhao_wanru");
        connectionFactory.setPassword("wanru");
        connectionFactory.setVirtualHost("Zhao_wanru");
        Connection connection = null;
        Channel channel = null;
        try{
            connection=connectionFactory.newConnection();
            channel=connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout",true);
            QUEUE_NAME=name+groupName;
            BIND="";
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,BIND);
            channel.basicConsume(QUEUE_NAME,true,new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[]body) throws IOException {
                    String sendName=properties.getAppId();
                    messageType=properties.getMessageId();
                    System.out.println(messageType);
                    String content = new String(body, "UTF-8");
                    if(!sendName.equals(name)){//不是本人发的
                        if(messageType.equals("文件")){
                            //将文件内容保存，提示输入文件名，默认保存在./
                            String fileName=JOptionPane.showInputDialog(null,"请设置文件名");
                            ReserveFile r=new ReserveFile(fileName,content);
                            String msg=r.msg;
                            try {
                                showTool.appendJTextArea(msg,chatArea,name,sendName,"left");
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            System.out.println("文件已接");
                        }else if(messageType.equals("非文件")){
                            try {
                                showTool.appendJTextArea(content,chatArea,name,sendName,"left");
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }//否则就不加
                }
            });
            try {//等待线程，方便一直接收消息
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void searchFile() throws FileNotFoundException {
        JFileChooser fc=new JFileChooser("./");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int val=fc.showOpenDialog(null);
        if(val==fc.APPROVE_OPTION){
            //正常选择文件
            File f = fc.getSelectedFile();
            String path=fc.getSelectedFile().toString();
            ReadFile r=new ReadFile(path);
            ReFileField.setText(r.content);
            JOptionPane.showMessageDialog(this,"文件已保存，可到相应路径下查看内容也可直接查看文本框,路径为"+path);
        }else{
            JOptionPane.showMessageDialog(this,"未找到相关文件，可能文件未正确传输");
        }
    }
    private void reviewMsg() throws SQLException {
        showTool.reviewJTextArea(chatArea,name,groupName);
    }
}
