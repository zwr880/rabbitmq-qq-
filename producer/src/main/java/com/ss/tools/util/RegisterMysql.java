package com.ss.tools.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterMysql {
    public String username;
    public String password;
    public String ImgUrl;
    public int i;
    public RegisterMysql(String u,String p,String imgUrl) throws SQLException {
        this.username=u;
        this.password=p;
        if(imgUrl.isEmpty()){
            this.ImgUrl="image/R.jpg";
        }else{
            this.ImgUrl=imgUrl;
        }
        init();

    }
    private void init()throws SQLException{
        Connection connection=JdbcUtil.getConnection();
        String sql = "insert into user(username,password,UserPicture) values(?,?,?)";
        //获取预处理对象，并给参数赋值
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setString(1,username);
        statement.setString(2,password);
        statement.setString(3,ImgUrl);
        //执行sql语句（插入了几条记录，就返回几）
        i = statement.executeUpdate();  //executeUpdate：执行并更新
        System.out.println(i);
        //关闭jdbc连接
        statement.close();
        connection.close();
    }
}
