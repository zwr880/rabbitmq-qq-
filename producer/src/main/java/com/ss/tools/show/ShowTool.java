package com.ss.tools.show;

import com.ss.tools.util.InsertMsgMysql;
import com.ss.tools.util.ReviewMsgMysql;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShowTool {
    public File file;
    public ShowTool(){}

    public String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义时间格式
        Date now = new Date(); // 获取当前时间
        return dateFormat.format(now); // 格式化时间并返回
    }
    public void appendJTextArea(final String info, final JTextPane chatArea, final String name,String DuiName, final String alignment) throws SQLException {
        final String alignTag;
        final String currentTime = getCurrentTime();
        final String pre;
        if (alignment.equals("left")) {
            alignTag = "<div style='text-align: left;'>";
            pre = currentTime + " " + DuiName + "<br>";
        } else {
            alignTag = "<div style='text-align: right;'>";
            pre = name +" "+currentTime + "<br>";
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (info != null) {
                    String msg = alignTag + info + "<br>";
                    String htmlMessage = msg + pre + "</div>";
                    HTMLDocument doc = (HTMLDocument) chatArea.getDocument();
                    if (doc instanceof HTMLDocument) {
                        try {
                            Element rootElement = doc.getDefaultRootElement();
                            Element lastElement = rootElement.getElement(rootElement.getElementCount() - 1);
                            int endOffset = lastElement.getEndOffset();
                            doc.insertAfterEnd(doc.getParagraphElement(endOffset), htmlMessage);
                            chatArea.setCaretPosition(doc.getLength());
                        } catch (BadLocationException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        chatArea.setText(chatArea.getText() + htmlMessage);
                    }
                }
            }
        });
       InsertMsgMysql insertMsgMysql=new InsertMsgMysql(name,DuiName,info,currentTime,alignment);
    }

    public void choseFile(JTextField fileField, ActionListener panel) {
        JFileChooser fc=new JFileChooser("./");//在当前目录下寻找
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int val=fc.showOpenDialog(null);
        if(val==fc.APPROVE_OPTION){
            //正常选择文件
            file = fc.getSelectedFile();
            if(file.isDirectory()){
                JOptionPane.showMessageDialog((Component) panel,"文件过大，请选择文件","警告",JOptionPane.WARNING_MESSAGE);
            }
            else{
                //System.out.println(file.getPath());
                fileField.setText(fc.getSelectedFile().toString());
            }
        }
        else{
            fileField.setText("未选择文件");
        }
    }
    public void reviewJTextArea(final JTextPane chatArea,String MyName,String DuiName) throws SQLException {
        ReviewMsgMysql reviewMsgMysql=new ReviewMsgMysql(MyName,DuiName);
        List<message>  messageList=reviewMsgMysql.init();
        for(message m:messageList){
            final String alignment=m.getMessageType();
            System.out.println(alignment);
            final String currentTime =m.getCurrentTime();
            String name1=m.getMyName();
            String name2=m.getDuiName();
            final String info=m.getContent();
            String pre = null;
            String alignTag = null;
            if (alignment.equals("left")) {
                alignTag = "<div style='text-align: left;'>";
                pre = currentTime + " " + name2 + "<br>";

            }
            else if(alignment.equals("right")){
                alignTag = "<div style='text-align: right;'>";
                pre = name1 +" "+currentTime + "<br>";
                System.out.println(alignment+"ok");
            }
            final String finalAlignTag = alignTag;
            final String finalPre = pre;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (info != null) {
                        String msg = finalAlignTag + info + "<br>";
                        String htmlMessage = msg + finalPre + "</div>";
                        HTMLDocument doc = (HTMLDocument) chatArea.getDocument();
                        if (doc instanceof HTMLDocument) {
                            try {
                                Element rootElement = doc.getDefaultRootElement();
                                Element lastElement = rootElement.getElement(rootElement.getElementCount() - 1);
                                int endOffset = lastElement.getEndOffset();
                                doc.insertAfterEnd(doc.getParagraphElement(endOffset), htmlMessage);
                                chatArea.setCaretPosition(doc.getLength());
                            } catch (BadLocationException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            chatArea.setText(chatArea.getText() + htmlMessage);
                        }
                    }
                }
            });
        }
    }
}
