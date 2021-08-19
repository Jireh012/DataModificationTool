package org.zayl.jireh.tool.datahandle.extractedtype;

import com.csvreader.CsvReader;
import org.apache.log4j.Logger;
import org.zayl.jireh.tool.datahandle.util.FileUtil;
import org.zayl.jireh.tool.datahandle.util.SftpUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Vector;

import static org.zayl.jireh.tool.datahandle.util.Const.*;

public class ExtractedType2 {
    private static final Logger logger = Logger.getLogger(ExtractedType2.class);

    private static int INDEX_网元名称 = 0;
    private static int INDEX_机框类型 = 0;
    private static int INDEX_资产序列号 = 0;


    public static void run() throws IOException, InterruptedException {
        logger.info("========SFTP登录========");
        SftpUtil sftp = new SftpUtil(SFTP_USERNAME, SFTP_PASSWORD, SFTP_ADDRESS, SFTP_PORT);
        sftp.login();
        logger.info("======SFTP登录完成======");
        logger.info("======清空下载路径======");
        FileUtil.deleteDir(saveFilePath);

        try {
            sftp.batchDownLoadFile(SFTP_DOWNLOAD_PATH + "/",
                    saveFilePath + File.separator, "", ".csv", false);

            logger.info("========初始化JDBC========");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://192.168.5.103:1433;DatabaseName=MMS",
                    "sa", "zhyw$zj123");
            connection.setAutoCommit(false);
            logger.info("========完成初始化JDBC========");
            File subrack4G = new File(saveFilePath + File.separator + "DIM_eNodeB_Subrack.csv");
            File subrack5G = new File(saveFilePath + File.separator + "DIM_BTS59005G_Subrack.csv");
            if (subrack5G.exists()) {
                PreparedStatement cmd = connection.prepareStatement(
                        "INSERT INTO 机框 " +
                                "VALUES  (?,?,?,?)");

                PreparedStatement delete = connection.prepareCall("DELETE FROM 机框 where type = '5G'");
                delete.addBatch();
                delete.executeBatch();

                CsvReader reader = new CsvReader(subrack5G.getPath(), ',', Charset.forName("GB2312"));
                boolean isFirst = true;
                // 读取每行的内容
                while (reader.readRecord()) {
                    if (isFirst) {
                        isFirst = false;
                        initDataPosition(reader);
                    } else {
                        if (!reader.get(INDEX_资产序列号).isEmpty()) {
                            cmd.setString(1, reader.get(INDEX_网元名称));
                            cmd.setString(2, reader.get(INDEX_机框类型));
                            cmd.setString(3, reader.get(INDEX_资产序列号));
                            cmd.setString(4, "5G");
                            cmd.addBatch();
                        }
                    }
                }
                reader.close();
                cmd.executeBatch();
                connection.commit();
            } else {
                logger.warn("5GSubrack.csv 文件不存在");
            }

            if (subrack4G.exists()) {
                PreparedStatement cmd = connection.prepareStatement(
                        "INSERT INTO 机框 " +
                                "VALUES  (?,?,?,?)");

                PreparedStatement delete = connection.prepareCall("DELETE FROM 机框 where type = '4G'");
                delete.addBatch();
                delete.executeBatch();

                CsvReader reader = new CsvReader(subrack4G.getPath(), ',', Charset.forName("GB2312"));
                boolean isFirst = true;
                // 读取每行的内容
                while (reader.readRecord()) {
                    if (isFirst) {
                        isFirst = false;
                        initDataPosition(reader);
                    } else {
                        if (!reader.get(INDEX_资产序列号).isEmpty()) {
                            cmd.setString(1, reader.get(INDEX_网元名称));
                            cmd.setString(2, reader.get(INDEX_机框类型));
                            cmd.setString(3, reader.get(INDEX_资产序列号));
                            cmd.setString(4, "4G");
                            cmd.addBatch();
                        }
                    }
                }
                reader.close();
                cmd.executeBatch();
                connection.commit();
            } else {
                logger.warn("4GSubrack.csv 文件不存在");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }finally {
            sftp.logout();
        }

    }

    private static void initDataPosition(CsvReader reader) throws IOException {
        int i = 0;
        //初始化相关列位置
        while (i < reader.getValues().length) {
            switch (reader.get(i)) {
                case "网元名称":
                    INDEX_网元名称 = i;
                    break;
                case "机框类型":
                    INDEX_机框类型 = i;
                    break;
                case "资产序列号":
                    INDEX_资产序列号 = i;
                    break;
                default:
                    break;
            }
            i++;
        }
    }
}
