package com.ss.tools.util;

import com.ss.tools.show.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewMsgMysql {
    private String MyName;
    private String DuiName;
    public ReviewMsgMysql(String MyName,String DuiName){
        this.MyName=MyName;
        this.DuiName=DuiName;
    }
    public List<message> init() throws SQLException {
        List<message> messageList=new ArrayList<>();
        Connection connection=JdbcUtil.getConnection();
        String sql = "select * from message where MyName=? and DuiName=?";
        //获取预处理对象，并给参数赋值
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setString(1,MyName);
        statement.setString(2,DuiName);
        //执行sql语句（插入了几条记录，就返回几）
        ResultSet resultSet=statement.executeQuery();
        while(resultSet.next()){
           String messageType=resultSet.getString("messageType");
           String content=resultSet.getString("content");
           String currentTime=resultSet.getString("currentTime");
           messageList.add(new message(MyName,DuiName,messageType,content,currentTime));
        }
        //关闭jdbc连接
        statement.close();
        connection.close();
        return messageList;
    }
}
