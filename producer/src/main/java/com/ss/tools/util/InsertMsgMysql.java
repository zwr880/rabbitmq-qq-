package com.ss.tools.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertMsgMysql {
    public String MyName;
    public String DuiName;
    public String content;
    public String currentTime;
    public String messageType;
    public int i;

    public InsertMsgMysql(String MyName,String DuiName,String content,String currentTime,String messageType) throws SQLException {
        this.MyName=MyName;
        this.DuiName=DuiName;
        this.content=content;
        this.currentTime=currentTime;
        this.messageType=messageType;
        init();
    }
    public  void init() throws SQLException {
        Connection connection=JdbcUtil.getConnection();
        String sql = "insert into message(MyName,DuiName,messageType,content,currentTime)values(?,?,?,?,?)";
        //获取预处理对象，并给参数赋值
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setString(1,MyName);
        statement.setString(2,DuiName);
        statement.setString(3,messageType);
        statement.setString(4,content);
        statement.setString(5,currentTime);

        //执行sql语句（插入了几条记录，就返回几）
        i = statement.executeUpdate();  //executeUpdate：执行并更新
        System.out.println(i);
        //关闭jdbc连接
        statement.close();
        connection.close();
    }

}
