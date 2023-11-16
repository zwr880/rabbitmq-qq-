package com.ss.tools.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageUrlMysql {
    private String username;//主键
    public int i;
    public String ImgUrl;
    public ImageUrlMysql(String name) throws SQLException {
        this.username=name;
        ImgUrl=search();
    }
    public String search() throws SQLException {
        Connection connection=JdbcUtil.getConnection();
        String sql = "select UserPicture from user where username=?";
        //获取预处理对象，并给参数赋值
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setString(1,username);
        //执行sql语句（插入了几条记录，就返回几）
        ResultSet resultSet=statement.executeQuery();
        while(resultSet.next()){
            String r=resultSet.getString("UserPicture");
            if(!r.isEmpty()){
                return r;
            }
        }
        //关闭jdbc连接
        statement.close();
        connection.close();
        return null;
    }
}
