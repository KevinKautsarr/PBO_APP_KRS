package com.dinus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDB {
    static String user="root";
    static String pass="";
    static String url="jdbc:mysql://localhost/dbuniv";
    //static String driver="com.mysql.cj.jdbc.Driver";
    public  static Connection getConn(){
        Connection conn=null;
        try{
            //Class.forName(driver);
            conn= DriverManager.getConnection(url,user,pass);
        }catch (SQLException e){
            throw  new RuntimeException();
        }
        return conn;
    }

}
