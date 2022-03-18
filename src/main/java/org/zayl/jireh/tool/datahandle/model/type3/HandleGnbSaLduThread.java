package org.zayl.jireh.tool.datahandle.model.type3;

import ch.ethz.ssh2.Connection;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.log4j.Logger;
import org.zayl.jireh.tool.datahandle.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static org.zayl.jireh.tool.datahandle.util.Const.*;
import static org.zayl.jireh.tool.datahandle.util.FileUtil.isChartPathExist;

/**
 * @author last_
 */
public class HandleGnbSaLduThread implements Runnable {

    private static final String FILE_NAME ="PM-GNB-SA-NRCELLDU";

    private static int RLC_UPOCTDL = 0;
    private static int RLC_UPLASTTTIOCTDL = 0;
    private static int RLC_THRPTIMEDL = 0;


    private final CountDownLatch threadsSignal;
    private final Map.Entry<String, List<String>> sourceData;

    private final Logger logger = Logger.getLogger(HandleGnbSaLduThread.class);

    public HandleGnbSaLduThread(CountDownLatch threadsSignal, Map.Entry<String, List<String>> sourceData) {
        this.threadsSignal = threadsSignal;
        this.sourceData = sourceData;
    }

    private void initDataPosition(CsvReader reader) throws IOException {
        int i = 0;
        //初始化相关列位置
        while (i < reader.getValues().length) {
            switch (reader.get(i)) {
                case "RLC.UpLastTtiOctDl":
                    RLC_UPLASTTTIOCTDL = i;
                    break;
                case "RLC.UpOctDl":
                    RLC_UPOCTDL = i;
                    break;
                case "RLC.ThrpTimeDL":
                    RLC_THRPTIMEDL = i;
                    break;
                default:
                    break;
            }
            i++;
        }
    }

    @Override
    public void run() {
        logger.info(Thread.currentThread().getName() + "...开始...");
        Properties properties = SOURCE_PRO;
        String source = sourceData.getKey().split("￥")[0];
        if (properties.get(source + ".user") == null) {
            logger.warn("SFTP连接参数错误,请检查sourceConfig配置文件!!!");
            threadsSignal.countDown();
            logger.info(Thread.currentThread().getName() + "结束. 还有"
                    + threadsSignal.getCount() + " 个线程");
            return;
        }
        ChannelSftp sftp = SftpUtilM.login(properties.get(source + ".user").toString(),
                properties.get(source + ".password").toString(), source, 22);
        if (sftp == null) {
            logger.warn("SFTP登陆失败，请检查网络和连接参数");
        } else {
            logger.info("SFTP登陆成功耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
            String path;
            String fileName;
            String str = null;
            long tttt = System.currentTimeMillis();
            if ("1".equals(TestModel)) {
                logger.info("获取：" + TestDirNameYmDH + "/"+FILE_NAME+"-" +
                        properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                TestDirNameYmDH + "/"+FILE_NAME+"-" +
                                properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz").toString();
                    } catch (SftpException e) {
                        e.printStackTrace();
                        logger.error("SFTP操作异常：" + e.getMessage());
                    }
                    if (!"[]".equals(str) && str != null) {
                        break;
                    } else {
                        logger.info(Thread.currentThread().getName() + "获取不到该时段文件，" +
                                SleepTime + " (秒) 后继续查询");
                        try {
                            Thread.sleep(SleepTime * 1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logger.error("线程异常：" + e.getMessage());
                        }
                    }
                }
                if ("[]".equals(str) || str == null) {
                    logger.warn("文件不存在,exit");
                    threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
                    logger.info(Thread.currentThread().getName() + "结束. 还有"
                            + threadsSignal.getCount() + " 个线程");
                    return;
                } else {
                    String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
                    fileName = ""+FILE_NAME+"-" + properties.get(source + ".id") +
                            "-" + verSion + "-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv";
                    logger.info("测试模式 文件名：" + fileName);
                    path = saveFilePath + TestDirNameYmDH + TestFileNameMMss + "_" + source + "_" + tttt + File.separator;
                    isChartPathExist(path);
                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + TestDirNameYmDH,
                            fileName + ".gz", path + fileName + ".gz");
                }
            } else {
                logger.info("获取：" + nowTime + "/"+FILE_NAME+"-" +
                        properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                nowTime + "/"+FILE_NAME+"-" +
                                properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz").toString();
                    } catch (SftpException e) {
                        e.printStackTrace();
                        logger.error("SFTP操作异常：" + e.getMessage());
                    }
                    if (!"[]".equals(str) && str != null) {
                        break;
                    } else {
                        logger.info(Thread.currentThread().getName() + "获取不到该时段文件，" +
                                SleepTime + " (秒) 后继续查询");
                        try {
                            Thread.sleep(SleepTime * 1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logger.error("线程异常：" + e.getMessage());
                        }
                    }

                }
                if ("[]".equals(str) || str == null) {
                    logger.warn("文件不存在,exit");
                    threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
                    logger.info(Thread.currentThread().getName() + "结束. 还有"
                            + threadsSignal.getCount() + " 个线程");
                    return;
                } else {
                    String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
                    fileName = ""+FILE_NAME+"-" + properties.get(source + ".id") + "-" +
                            verSion + "-" + nowTime + TimeMm + "-15.csv";
                    logger.info("正常模式 文件名：" + fileName);
                    path = saveFilePath + nowTime + TimeMm + "_" + source + "_" + tttt + File.separator;
                    isChartPathExist(path);
                    logger.info("========开始下载文件========");
                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + nowTime,
                            fileName + ".gz", path + fileName + ".gz");
                    logger.info("========开始删除文件========");
                    SftpUtilM.delete(sftp, properties.get(source + ".path") + "/" + nowTime, fileName + ".gz");
                }
            }
            UnCompressFileGZIP.doUncompressFile(path + fileName + ".gz");
            isChartPathExist(path + "Write" + File.separator);
            File y1 = new File(path + fileName + ".gz");
            logger.info(fileName + ".gz " + "原文件大小：" + y1.length());
            csvRun(path + fileName, path + "Write" + File.separator + fileName,
                    sourceData.getValue());
            logger.info("文件处理耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
            String fileLocal = path + "Write" + File.separator + fileName;
            CompressFileGZIP.doCompressFile(fileLocal);
            File file = new File(fileLocal + ".gz");
            logger.info(fileName + ".gz " + "修改后本地文件大小：" + file.length());
            try {
                InputStream is = new FileInputStream(file);
                String uploadPath;
                if ("1".equals(TestModel)) {
                    uploadPath = properties.get(source + ".path") + "/" + TestDirNameYmDH;
                } else {
                    uploadPath = properties.get(source + ".path") + "/" + nowTime;
                }
                SftpUtilM.upload(sftp, "/", uploadPath, fileName + ".gz", is);
                logger.info("=======" + source + "上传成功=======");

                Connection connection = RemoteShellExecutor.login(source, properties.get(source + ".user").toString(),
                        properties.get(source + ".password").toString());

                if (connection == null) {
                    logger.error("=======" + source + "SSH connection error=======");
                } else {
                    try {
                        RemoteShellExecutor.exec(connection, "ls -l " + uploadPath + "/" + fileName + ".gz");
                        logger.info("执行 chmod 750 " + uploadPath + "/" + fileName + ".gz");
                        RemoteShellExecutor.exec(connection, "chmod 750 " + uploadPath + "/" + fileName + ".gz");
                        logger.info("================alert===============");
                        RemoteShellExecutor.exec(connection, "ls -l " + uploadPath + "/" + fileName + ".gz");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                long dalen;
                if ("1".equals(TestModel)) {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            TestDirNameYmDH + "/"+FILE_NAME+"-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz").getSize();
                    logger.info(""+FILE_NAME+"-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz  修改后FTP文件大小为：" + dalen);
                } else {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            nowTime + "/"+FILE_NAME+"-" +
                            properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz").getSize();
                    logger.info(""+FILE_NAME+"-" +
                            properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz  修改后FTP文件大小为：" + dalen);
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("文件操作异常：" + e.getMessage());
            } catch (SftpException e) {
                e.printStackTrace();
                logger.error("SFTP操作异常：" + e.getMessage());
            }
        }

        // 线程结束时计数器减1
        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
        logger.info(Thread.currentThread().getName() + "结束. 还有"
                + threadsSignal.getCount() + " 个线程 耗时：" +
                (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
    }

    private void csvRun(String readerPath, String writePath, List<String> value) {
        try {
            CsvReader reader = new CsvReader(readerPath, '|', StandardCharsets.UTF_8);
            boolean isFirst = true;
            String[] stringList;
            reader.readHeaders();
            //读取表头 TimeStamp
            String[] title = reader.getHeaders();
            CsvWriter writerTemp = new CsvWriter(writePath, '|', StandardCharsets.UTF_8);
            writerTemp.setUseTextQualifier(false);
            writerTemp.writeRecord(title);
            // 读取每行的内容
            while (reader.readRecord()) {
                if (isFirst) {
                    isFirst = false;
                    //读取列头 rmUID
                    writerTemp.writeRecord(reader.getValues());
                    initDataPosition(reader);
                } else {
                    //每一行的值
                    stringList = reader.getValues();
                    stringList[2] = "\"" + stringList[2] + "\"";
                    if (value.toString().contains(reader.get(Integer.parseInt(Const.aimsType)))) {
                        for (String li : value) {
                            if (reader.get(Integer.parseInt(Const.aimsType)).equals(li.split("￥")[1])) {
                                int on5 = Integer.parseInt(li.split("￥")[6]);

                                try {
                                    if (on5 == 1) {
                                        float rlc = Float.parseFloat(reader.get(RLC_THRPTIMEDL));
                                        float v1 = Float.parseFloat(reader.get(RLC_UPOCTDL)) - Float.parseFloat(reader.get(RLC_UPLASTTTIOCTDL));
                                        float v2 = v1 * 8 / rlc;

                                        if (rlc > 2 && v2 < 100) {
                                            logger.info("RLC_THRPTIMEDL 指标修正 before：" + reader.get(RLC_THRPTIMEDL));
                                            stringList[RLC_THRPTIMEDL] = String.valueOf((int) (rlc / 2));
                                            logger.info("RLC_THRPTIMEDL 指标修正 after：" + stringList[RLC_THRPTIMEDL]);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warn("aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
                                }
                                break;
                            }
                        }
                    }
                    writerTemp.writeRecord(stringList);
                }
            }
            reader.close();
            // 关闭Writer
            writerTemp.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("操作异常：" + e.getMessage());
        }
    }

}
