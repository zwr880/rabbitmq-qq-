package com.ss.tools.util;

import com.ss.tools.show.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserListMysql {
    public String username;
    public UserListMysql(String u) throws SQLException {
        this.username=u;
    }
    public List<Users> init() throws SQLException {
        List<Users> userList=new ArrayList<>();
        Connection connection=JdbcUtil.getConnection();
        String sql = "select username,UserPicture from user where username != ?";
        //获取预处理对象，并给参数赋值
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setString(1,username);
        //执行sql语句（插入了几条记录，就返回几）
        ResultSet resultSet=statement.executeQuery();
        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String UserPicture = resultSet.getString("UserPicture");
            userList.add(new Users(username, UserPicture));
        }
        //关闭jdbc连接
        statement.close();
        connection.close();
        return userList;
    }
}