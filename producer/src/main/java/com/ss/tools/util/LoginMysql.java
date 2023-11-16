package com.ss.tools.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginMysql {
    public String username;
    public String password;
    public int i;
    public LoginMysql(String u,String p) throws SQLException {
        this.username=u;
        this.password=p;
        init();
    }
    private void init() throws SQLException {
        Connection connection=JdbcUtil.getConnection();
        String sql = "select password from user where username=?";
        //获取预处理对象，并给参数赋值
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setString(1,username);
        //执行sql语句（插入了几条记录，就返回几）
        ResultSet resultSet=statement.executeQuery();
        while(resultSet.next()){
            String r=resultSet.getString("password");
            if(r.equals(password)){
                i=1;
            }
            else {
                i=0;
            }
        }
        //关闭jdbc连接
        statement.close();
        connection.close();
    }
}
