package com.ss.ui;

import com.rabbitmq.client.*;
import com.ss.send.SendFactory;
import com.ss.send.sendFileFactory;
import com.ss.tools.file.ReadFile;
import com.ss.tools.file.ReserveFile;
import com.ss.tools.show.ShowTool;

import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;


public class ChatMainFramePanel extends JPanel {
    public JTextField messageField;
    public JTextField fileField;
    public JTextField ReFileField;
    public JTextPane chatArea;
    private JButton searchFileButton;
    private JButton sendMessageButton;
    private  JButton sendFileButton;
    private JButton chooseFileButton;
    private JButton receiveButton;
    private JButton reviewButton;
    public String msg;
    public String DuiName;
    public String MyName;
    private SendFactory sendFactory;
    private sendFileFactory sendFileFactory;
    public  File file;
    public ShowTool showTool=new ShowTool();
    private static String QUEUE_NAME;
    private static final String EXCHANGE_NAME="amq.direct";
    private static String BIND;
    public ChatMainFramePanel(String DuiName,String MyName){
        this.DuiName=DuiName;//对方
        this.MyName=MyName;//我的
        this.setLayout(new BorderLayout());
        initPanel();
    }
    public void initPanel() {
        JLabel chatLabel=new JLabel(DuiName+" chat area");
        chatLabel.setForeground(new Color(28,123,235));
        chatLabel.setFont(new Font("宋体",Font.PLAIN,14));
        chatArea = new JTextPane();
        HTMLEditorKit kit = new HTMLEditorKit();
        chatArea.setEditorKit(kit);
        chatArea.setEditable(false);
        chatArea.setContentType("text/html");
        JScrollPane scrollPane = new JScrollPane(chatArea);
        JPanel chatAreaPanel=new JPanel();
        chatAreaPanel.setLayout(new GridLayout());
        GridBagConstraints gri=new GridBagConstraints();
        gri.fill=GridBagConstraints.HORIZONTAL;
        gri.insets=new Insets(1,1,1,1);
        gri.gridx=0;
        gri.gridy=0;
        chatAreaPanel.add(scrollPane,gri);
        chatAreaPanel.add(scrollPane);
        JPanel messageInputPanel = new JPanel();
        messageInputPanel.setLayout(new GridBagLayout());
        messageField = new JTextField();
        sendMessageButton = new JButton("Send Message");
        receiveButton=new JButton("receive");
        ReFileField =new JTextField();
        sendFileButton = new JButton("Send File");
        chooseFileButton=new JButton("...");
        searchFileButton=new JButton("ViewAndSearch");
        reviewButton=new JButton("ViewAndMessage");
        fileField = new JTextField();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the messageInputPanel using GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        messageInputPanel.add(messageField, gbc);

        gbc.gridx=2;
        gbc.gridwidth=1;
        gbc.weightx=0.0;
        messageInputPanel.add(receiveButton,gbc);

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

        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        chooseFileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showTool.choseFile(fileField,this);
                 file=showTool.file;
            }
        });
        sendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendFileMessage();
                } catch (FileNotFoundException | SQLException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });
        receiveButton.addActionListener(new ActionListener() {//接收消息
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
        this.add(chatLabel,BorderLayout.NORTH);
        this.add(chatAreaPanel,BorderLayout.CENTER);
        this.add(messageInputPanel,BorderLayout.SOUTH);
        this.revalidate();
        this.repaint();
    }

    private void sendMessage() throws SQLException {//发送消息+发送者
        msg=messageField.getText();
        //刷新输入框
        messageField.setText("");
        //聊天界面内容显示
        showTool.appendJTextArea(msg,chatArea,MyName,DuiName,"right");
        //基于Rabbitmq发送短信
        sendFactory=new SendFactory(msg,MyName);
    }
    private void sendFileMessage() throws FileNotFoundException, SQLException {
            msg=file.getPath();
            System.out.println("发送文件路径"+msg);
            //显示已发送
            showTool.appendJTextArea(msg,chatArea,MyName,DuiName,"right");
            //刷新文件框
            fileField.setText("");
            //发送信息
            sendFileFactory =new sendFileFactory(msg,MyName);
    }
    private void receiveWork(){//接收消息
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
            channel.exchangeDeclare(EXCHANGE_NAME,"direct",true);
            BIND=DuiName;
            QUEUE_NAME=DuiName;
            System.out.println(BIND);
            System.out.println(QUEUE_NAME);
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,BIND);
            channel.basicConsume(QUEUE_NAME,true,new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[]body) throws IOException {
                    String messageType=properties.getMessageId();
                    String sendName=properties.getAppId();//对方的名字
                    System.out.println(sendName);
                    String content = new String(body, "UTF-8");
                    if(sendName.equals(BIND)){//是发给我的
                        if(messageType.equals("非文件")){
                            try {
                                showTool.appendJTextArea(content,chatArea,MyName,sendName,"left");
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            System.out.println("receive success");
                        }
                        else if(messageType.equals("文件")){
                            //将文件内容保存，提示输入文件名，默认保存在./
                            String fileName=JOptionPane.showInputDialog(null,"请设置文件名");
                            System.out.println("接收文件内容1"+content);
                            ReserveFile r=new ReserveFile(fileName,content);
                            String msg=r.msg;
                            try {
                                showTool.appendJTextArea(msg,chatArea,MyName,sendName,"left");
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            System.out.println("receive success");
                        }
                    }
                }
            });
            try {
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
            System.out.println("接收文件路径"+f.getAbsolutePath());
            ReadFile readFile=new ReadFile(f.getAbsolutePath());
            System.out.println("接收文件2"+readFile.content);
            ReFileField.setText(readFile.content);
            JOptionPane.showMessageDialog(this,"文件已保存，可到相应路径下查看内容也可直接查看文本框,路径为"+path);
        }else{
            JOptionPane.showMessageDialog(this,"未找到相关文件，可能文件未正确传输");
        }
    }
    private void reviewMsg() throws SQLException {
        showTool.reviewJTextArea(chatArea,MyName,DuiName);
    }
}
