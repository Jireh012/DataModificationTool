package org.zayl.jireh.tool.datahandle.extractedtype;

import com.csvreader.CsvReader;
import org.apache.log4j.Logger;
import org.zayl.jireh.tool.datahandle.util.FileUtil;
import org.zayl.jireh.tool.datahandle.util.SftpUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Vector;

import static org.zayl.jireh.tool.datahandle.util.Const.*;

public class ExtractedType1 {
    private static final Logger logger = Logger.getLogger(ExtractedType1.class);

    private static int INDEX_网元名称 = 0;
    private static int INDEX_机框类型 = 0;
    private static int INDEX_资产序列号 = 0;


    public static void run() {
        logger.info("========SFTP登录========");
        SftpUtil sftp = new SftpUtil(SFTP_USERNAME, SFTP_PASSWORD, SFTP_ADDRESS, SFTP_PORT);
        sftp.login();
        logger.info("======SFTP登录完成======");
        logger.info("======清空下载路径======");
        FileUtil.deleteDir(saveFilePath);
        FileUtil.isChartPathExist(saveFilePath);

        try {
            sftp.batchDownLoadFile(SFTP_DOWNLOAD_PATH + "/",
                    saveFilePath + File.separator, "", ".csv", false);

            logger.info("========初始化JDBC========");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://192.168.5.103:1433;DatabaseName=MMS",
                    "sa", "zhyw$zj123");
            connection.setAutoCommit(false);
            logger.info("========完成初始化JDBC========");
            File jikuang = new File(saveFilePath + File.separator + "DIM_eNodeB_Subrack.csv");
            if (jikuang.exists()) {
                PreparedStatement cmd = connection.prepareStatement(
                        "INSERT INTO 机框 " +
                                "VALUES  (?,?,?,?)");

                String type;
                if (saveFilePath.contains("5G")) {
                    type = "5G";
                } else if (saveFilePath.contains("4G")) {
                    type = "4G";
                } else {
                    type = "NONE";
                }

                CsvReader reader = new CsvReader(jikuang.getPath(), ',', StandardCharsets.UTF_8);
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
                            cmd.setString(4, type);
                            cmd.addBatch();
                        }
                    }
                }

                sftp.logout();
                reader.close();
                cmd.executeBatch();
                connection.commit();
                connection.close();
            } else {
                logger.warn("DIM_eNodeB_Subrack.csv 文件不存在");
            }


        } catch (Exception e) {
            logger.warn(e.getMessage());
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
