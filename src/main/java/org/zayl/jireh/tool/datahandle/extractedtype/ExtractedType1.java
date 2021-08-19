package org.zayl.jireh.tool.datahandle.extractedtype;

import com.csvreader.CsvReader;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jcraft.jsch.ChannelSftp;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zayl.jireh.tool.datahandle.model.HandleGnbSaCuThread;
import org.zayl.jireh.tool.datahandle.model.HandleGnbSaDuThread;
import org.zayl.jireh.tool.datahandle.model.HandleGnbsNsaDuThread;
import org.zayl.jireh.tool.datahandle.util.FileUtil;
import org.zayl.jireh.tool.datahandle.util.MapUtil;
import org.zayl.jireh.tool.datahandle.util.SftpUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import static org.zayl.jireh.tool.datahandle.util.Const.*;

public class ExtractedType1 {
    private static final Logger logger = Logger.getLogger(ExtractedType1.class);

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

        Vector<?> list;
        HashMap<String, Integer> data = new HashMap<>();
        try {
            list = sftp.listFiles(SFTP_DOWNLOAD_PATH + "/*");
            if (list.size() > 0) {
                for (Object o : list) {
                    ChannelSftp.LsEntry lsEntry1 = (ChannelSftp.LsEntry) o;
                    if (lsEntry1.getAttrs().isDir()) {
                        data.put(((ChannelSftp.LsEntry) o).getFilename(), ((ChannelSftp.LsEntry) o).getAttrs().getMTime());
                    }
                }

                data = MapUtil.sortByValue(data);
                for (Map.Entry<String, Integer> entry : data.entrySet()) {
                    String s = entry.getKey();
                    list = sftp.listFiles(SFTP_DOWNLOAD_PATH + "/" + s + "/*.csv");
                    if (list.size() > 0) {
                        sftp.batchDownLoadFile(SFTP_DOWNLOAD_PATH + "/" + s + "/",
                                saveFilePath + File.separator, "", ".csv", false);
                        break;
                    }
                }

                sftp.logout();

                logger.info("========初始化JDBC========");
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                Connection connection = DriverManager.getConnection("jdbc:sqlserver://192.168.5.103:1433;DatabaseName=MMS",
                        "sa", "zhyw$zj123");
                connection.setAutoCommit(false);
                logger.info("========完成初始化JDBC========");
                File jikuang = new File(saveFilePath + File.separator + "机框.csv");
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

//                    PreparedStatement delete = connection.prepareCall("DELETE FROM 机框 where type = '" + type + "'");
//                    delete.addBatch();
//                    delete.executeBatch();

                    CsvReader reader = new CsvReader(jikuang.getPath(), ',', StandardCharsets.UTF_8);
                    boolean isFirst = true;
                    // 读取每行的内容
                    while (reader.readRecord()) {
                        if (isFirst) {
                            isFirst = false;
                            initDataPosition(reader);
                        } else {
                            if (!reader.get(INDEX_资产序列号).isEmpty()){
                                cmd.setString(1, reader.get(INDEX_网元名称));
                                cmd.setString(2, reader.get(INDEX_机框类型));
                                cmd.setString(3, reader.get(INDEX_资产序列号));
                                cmd.setString(4, type);
                                cmd.addBatch();
                            }
                        }
                    }
                    reader.close();
                    cmd.executeBatch();
                    connection.commit();
                    connection.close();
                } else {
                    logger.warn("机框.csv 文件不存在");
                }
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
