package com;

import java.sql.*;

/**
 * Created by zhangquanhong on 2017/8/15.
 */
public class PhoenixDemo {
//    private static String driver = "org.apache.phoenix.jdbc.PhoenixDriver";
//
//    public static void main(String[] args) throws SQLException {
//        try {
//            Class.forName(driver);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        Statement stmt = null;
//        ResultSet rset = null;
//
//        Connection con = DriverManager.getConnection("jdbc:phoenix:zqh,wzk,zzy:2181");
//        stmt = con.createStatement();
//        String sql = "select * from test3";
//        rset = stmt.executeQuery(sql);
//        while (rset.next()) {
//            System.out.println(rset.getString("mycolumn"));
//        }
//        stmt.close();
//        con.close();
//    }
}