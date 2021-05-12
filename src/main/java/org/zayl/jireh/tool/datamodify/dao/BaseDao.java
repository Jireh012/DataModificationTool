package org.zayl.jireh.tool.datamodify.dao;

import com.huawei.gauss.util.Constant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDao {
    /**
     * 连接字符串
      */
    private final String url = "jdbc:zenith:@127.0.0.1:32080";
    /**
     * 用户名
      */
    private final String dbUserName = "sys";
    /**
     * 密码
      */
    private final String dbUserPwd = "Admin@123";
    protected Connection conn = null;

    public Connection getConnection() {
        if (conn != null) {
            return conn;
        }

        try {
            // 加载驱动类
            Class.forName("com.huawei.gauss.jdbc.ZenithDriver");
            // 获取数据库连接
            conn = DriverManager.getConnection(url, dbUserName, dbUserPwd);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

        }
        return conn;
    }

    /**
     * 封装底层数据库连接关闭方法
     * @author liuhaibing
     * @date 2019年12月18日
     * @version 1.0
     */

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库的自动事务提交功能，从自己控制事务的提交或者回滚
     * @author liuhaibing
     * @date 2019年12月18日
     * @version 1.0
     */

    public void openTransaction() {
        if (conn != null) {
            try {
                // 关闭数据库操作的自动commit功能
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 手动提交事务
     * @author liuhaibing
     * @date 2019年12月18日
     * @version 1.0
     */
    public void commit() {
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public void rollback() {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String [] args) {
        new BaseDao().getConnection();
    }
}
