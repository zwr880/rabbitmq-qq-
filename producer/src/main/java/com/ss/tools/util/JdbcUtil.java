package com.ss.tools.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JdbcUtil {
    private static String dirver;
    private static String url;
    private static String user;
    private static String password;

    static {
        try{
            Properties properties=new Properties();
            InputStream input =JdbcUtil.class.getClassLoader().getResourceAsStream("db.properties");
            properties.load(input);
            dirver=properties.getProperty("driver");
            url=properties.getProperty("url");
            user=properties.getProperty("user");
            password=properties.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        try{
            Class.forName(dirver);
            Connection connection= DriverManager.getConnection(url,user,password);
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
