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
import static org.zayl.jireh.tool.datahandle.util.Mathematical.StringToDouble;
import static org.zayl.jireh.tool.datahandle.util.Mathematical.StringToInt;

/**
 * @author Jireh
 */
public class HandleGnbSaLcuThread implements Runnable {

    private static int IRATHO_ATTOUTEUTRAN = 0;
    private static int IRATHO_ATTOUTEUTRAN_EPSFALLBACK = 0;
    private static int IRATHO_SUCCOUTEUTRAN_EPSFALLBACK = 0;
    private static int IRATHO_SUCCPREPOUTEUTRAN_EPSFALLBACK = 0;
    private static int IRATHO_SUCCPREPOUTEUTRAN = 0;
    private static int IRATHO_SUCCOUTEUTRAN = 0;
    private static int IRATHO_FAILPREPOUTEUTRAN_AMF = 0;
    private static int IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_AMF = 0;
    private static int IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_NOREPLY = 0;
    private static int IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_PREPFAILURE = 0;
    private static int IRATHO_FAILPREPOUTEUTRAN = 0;
    private static int IRATHO_FAILPREPOUTEUTRAN_PREPFAILURE = 0;
    private static int RRC_REDIRECTTOLTE = 0;
    private static int RRC_REDIRECTTOLTE_EPSFALLBACK = 0;
    private static int FLOW_NBRFAILESTAB = 0;
    private static int FLOW_NBRFAILESTAB_CAUSETRANSPORT = 0;
    private static int FLOW_NBRFAILESTAB_CAUSERADIORESOURCESNOTAVAILABLE = 0;
    private static int FLOW_NBRFAILESTAB_CAUSEFAILUREINRADIOINTERFACEPROCEDURE = 0;
    private static int RRC_ATTCONNESTAB = 0;
    private static int FLOW_NBRSUCCESTAB = 0;
    private static int FLOW_NBRATTESTAB = 0;
    private static int RRC_SuccConnEstab = 0;
    private static int CONTEXT_ATTRELGNB_UELOST = 0;
    private static int CONTEXT_ATTRELGNB_NORMAL = 0;
    private static int CONTEXT_ATTRELGNB = 0;
    private static int HO_SUCCOUTINTRADU = 0;
    private static int HO_ATTOUTCUINTRADU = 0;
    private static int HO_SUCCOUTINTERCUNG = 0;
    private static int HO_ATTOUTINTERCUNG = 0;
    private static int HO_SUCCOUTINTERCUXN = 0;
    private static int HO_ATTOUTINTERCUXN = 0;
    private static int HO_SUCCOUTINTRACUINTERDU = 0;
    private static int HO_ATTOUTINTRACUINTERDU = 0;


    private final CountDownLatch threadsSignal;
    private final Map.Entry<String, List<String>> sourceData;

    private final Logger logger = Logger.getLogger(HandleGnbSaLcuThread.class);

    public HandleGnbSaLcuThread(CountDownLatch threadsSignal, Map.Entry<String, List<String>> sourceData) {
        this.threadsSignal = threadsSignal;
        this.sourceData = sourceData;
    }

    private void initDataPosition(CsvReader reader) throws IOException {
        int i = 0;
        //初始化相关列位置
        while (i < reader.getValues().length) {
            switch (reader.get(i)) {
                case "IRATHO.AttOutEutran":
                    IRATHO_ATTOUTEUTRAN = i;
                    break;
                case "IRATHO.AttOutEutran.EpsFallBack":
                    IRATHO_ATTOUTEUTRAN_EPSFALLBACK = i;
                    break;
                case "IRATHO.SuccOutEutran.EpsFallBack":
                    IRATHO_SUCCOUTEUTRAN_EPSFALLBACK = i;
                    break;
                case "IRATHO.SuccPrepOutEutran.EpsFallBack":
                    IRATHO_SUCCPREPOUTEUTRAN_EPSFALLBACK = i;
                    break;
                case "IRATHO.SuccPrepOutEutran":
                    IRATHO_SUCCPREPOUTEUTRAN = i;
                    break;
                case "IRATHO.SuccOutEutran":
                    IRATHO_SUCCOUTEUTRAN = i;
                    break;
                case "IRATHO.FailPrepOutEutran.AMF":
                    IRATHO_FAILPREPOUTEUTRAN_AMF = i;
                    break;
                case "IRATHO.FailPrepOutEutran.EpsFallBack.AMF":
                    IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_AMF = i;
                    break;
                case "IRATHO.FailPrepOutEutran.EpsFallBack.NoReply":
                    IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_NOREPLY = i;
                    break;
                case "IRATHO.FailPrepOutEutran.EpsFallBack.PrepFailure":
                    IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_PREPFAILURE = i;
                    break;
                case "IRATHO.FailPrepOutEutran":
                    IRATHO_FAILPREPOUTEUTRAN = i;
                    break;
                case "IRATHO.FailPrepOutEutran.PrepFailure":
                    IRATHO_FAILPREPOUTEUTRAN_PREPFAILURE = i;
                    break;
                case "RRC.RedirectToLTE ":
                    RRC_REDIRECTTOLTE = i;
                    break;
                case "RRC.RedirectToLTE.Epsfallback":
                    RRC_REDIRECTTOLTE_EPSFALLBACK = i;
                    break;
                case "HO.SuccOutIntraDU":
                    HO_SUCCOUTINTRADU = i;
                    break;
                case "HO.AttOutCUIntraDU":
                    HO_ATTOUTCUINTRADU = i;
                    break;
                case "HO.SuccOutInterCuNG":
                    HO_SUCCOUTINTERCUNG = i;
                    break;
                case "HO.AttOutInterCuNG":
                    HO_ATTOUTINTERCUNG = i;
                    break;
                case "HO.SuccOutInterCuXn":
                    HO_SUCCOUTINTERCUXN = i;
                    break;
                case "HO.AttOutInterCuXn":
                    HO_ATTOUTINTERCUXN = i;
                    break;
                case "HO.SuccOutIntraCUInterDU":
                    HO_SUCCOUTINTRACUINTERDU = i;
                    break;
                case "HO.AttOutIntraCUInterDU":
                    HO_ATTOUTINTRACUINTERDU = i;
                    break;
                case "CONTEXT.AttRelgNB":
                    CONTEXT_ATTRELGNB = i;
                    break;
                case "CONTEXT.AttRelgNB.Normal":
                    CONTEXT_ATTRELGNB_NORMAL = i;
                    break;
                case "CONTEXT.AttRelgNB.UeLost":
                    CONTEXT_ATTRELGNB_UELOST = i;
                    break;
                case "Flow.NbrAttEstab":
                    FLOW_NBRATTESTAB = i;
                    break;
                case "Flow.NbrSuccEstab":
                    FLOW_NBRSUCCESTAB = i;
                    break;
                case "Flow.NbrFailEstab":
                    FLOW_NBRFAILESTAB = i;
                    break;
                case "Flow.NbrFailEstab.CauseTransport":
                    FLOW_NBRFAILESTAB_CAUSETRANSPORT = i;
                    break;
                case "Flow.NbrFailEstab.CauseRadioResourcesNotAvailable":
                    FLOW_NBRFAILESTAB_CAUSERADIORESOURCESNOTAVAILABLE = i;
                    break;
                case "Flow.NbrFailEstab.CauseFailureInRadioInterfaceProcedure":
                    FLOW_NBRFAILESTAB_CAUSEFAILUREINRADIOINTERFACEPROCEDURE = i;
                    break;
                case "RRC.AttConnEstab":
                    RRC_ATTCONNESTAB = i;
                    break;
                case "RRC.SuccConnEstab":
                    RRC_SuccConnEstab = i;
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
                logger.info("获取：" + TestDirNameYmDH + "/PM-GNB-SA-NRCELLCU-" +
                        properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                TestDirNameYmDH + "/PM-GNB-SA-NRCELLCU-" +
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
                    fileName = "PM-GNB-SA-NRCELLCU-" + properties.get(source + ".id") +
                            "-" + verSion + "-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv";
                    logger.info("测试模式 文件名：" + fileName);
                    path = saveFilePath + TestDirNameYmDH + TestFileNameMMss + "_" + source + "_" + tttt + File.separator;
                    isChartPathExist(path);
                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + TestDirNameYmDH,
                            fileName + ".gz", path + fileName + ".gz");
                }
            } else {
                logger.info("获取：" + nowTime + "/PM-GNB-SA-NRCELLCU-" +
                        properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                nowTime + "/PM-GNB-SA-NRCELLCU-" +
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
                    fileName = "PM-GNB-SA-NRCELLCU-" + properties.get(source + ".id") + "-" +
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
                            TestDirNameYmDH + "/PM-GNB-SA-NRCELLCU-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz").getSize();
                    logger.info("PM-GNB-SA-NRCELLCU-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz  修改后FTP文件大小为：" + dalen);
                } else {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            nowTime + "/PM-GNB-SA-NRCELLCU-" +
                            properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz").getSize();
                    logger.info("PM-GNB-SA-NRCELLCU-" +
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
                                int on1 = Integer.parseInt(li.split("￥")[2]);
                                int on2 = Integer.parseInt(li.split("￥")[3]);
                                int on4 = Integer.parseInt(li.split("￥")[5]);
                                int on6 = Integer.parseInt(li.split("￥")[7]);
                                int on7 = Integer.parseInt(li.split("￥")[8]);

                                try {
                                    if (on1 == 1) {
                                        logger.info("指标修正 当前：" + reader.get(2));
                                        int contextAttrelgnbUelostValue = StringToInt(reader.get(CONTEXT_ATTRELGNB_UELOST));
                                        if (contextAttrelgnbUelostValue > 0) {
                                            logger.info("CONTEXT_ATTRELGNB_UELOST指标 before：" + contextAttrelgnbUelostValue);
                                            stringList[CONTEXT_ATTRELGNB_UELOST] = "0";
                                        }
                                        logger.info("CONTEXT_ATTRELGNB_NORMAL指标修正 before：" + reader.get(CONTEXT_ATTRELGNB));
                                        stringList[CONTEXT_ATTRELGNB_NORMAL] = reader.get(CONTEXT_ATTRELGNB);
                                        logger.info("CONTEXT_ATTRELGNB_NORMAL指标修正 after：" + stringList[CONTEXT_ATTRELGNB_NORMAL]);
                                    }

                                    if (on2 == 1) {
                                        logger.info("FLOW_NBRSUCCESTAB 指标修正 before：" + reader.get(FLOW_NBRSUCCESTAB));
                                        stringList[FLOW_NBRSUCCESTAB] = reader.get(FLOW_NBRATTESTAB);
                                        logger.info("FLOW_NBRSUCCESTAB 指标修正 after：" + stringList[FLOW_NBRSUCCESTAB]);

                                        logger.info("FLOW_NBRFAILESTAB 指标修正 before：" + reader.get(FLOW_NBRFAILESTAB));
                                        stringList[FLOW_NBRFAILESTAB] = "0";
                                        logger.info("FLOW_NBRFAILESTAB 指标修正 after：" + stringList[FLOW_NBRFAILESTAB]);

                                        logger.info("FLOW_NBRFAILESTAB_CAUSETRANSPORT 指标修正 before：" + reader.get(FLOW_NBRFAILESTAB_CAUSETRANSPORT));
                                        stringList[FLOW_NBRFAILESTAB_CAUSETRANSPORT] = "0";
                                        logger.info("FLOW_NBRFAILESTAB_CAUSETRANSPORT 指标修正 after：" + stringList[FLOW_NBRFAILESTAB_CAUSETRANSPORT]);

                                        logger.info("FLOW_NBRFAILESTAB_CAUSERADIORESOURCESNOTAVAILABLE 指标修正 before：" + reader.get(FLOW_NBRFAILESTAB_CAUSERADIORESOURCESNOTAVAILABLE));
                                        stringList[FLOW_NBRFAILESTAB_CAUSERADIORESOURCESNOTAVAILABLE] = "0";
                                        logger.info("FLOW_NBRFAILESTAB_CAUSERADIORESOURCESNOTAVAILABLE 指标修正 after：" + stringList[FLOW_NBRFAILESTAB_CAUSERADIORESOURCESNOTAVAILABLE]);

                                        logger.info("FLOW_NBRFAILESTAB_CAUSEFAILUREINRADIOINTERFACEPROCEDURE 指标修正 before：" + reader.get(FLOW_NBRFAILESTAB_CAUSEFAILUREINRADIOINTERFACEPROCEDURE));
                                        stringList[FLOW_NBRFAILESTAB_CAUSEFAILUREINRADIOINTERFACEPROCEDURE] = "0";
                                        logger.info("FLOW_NBRFAILESTAB_CAUSEFAILUREINRADIOINTERFACEPROCEDURE 指标修正 after：" + stringList[FLOW_NBRFAILESTAB_CAUSEFAILUREINRADIOINTERFACEPROCEDURE]);
                                    }

                                    if (on4 == 1) {
                                        logger.info("HO.SuccOutIntraDU 指标修正 before：" + reader.get(HO_SUCCOUTINTRADU));
                                        stringList[HO_SUCCOUTINTRADU] = reader.get(HO_ATTOUTCUINTRADU);
                                        logger.info("HO.SuccOutIntraDU 指标修正 after：" + stringList[HO_SUCCOUTINTRADU]);

                                        logger.info("HO_SUCCOUTINTERCUNG 指标修正 before：" + stringList[HO_SUCCOUTINTERCUNG]);
                                        stringList[HO_SUCCOUTINTERCUNG] = reader.get(HO_ATTOUTINTERCUNG);
                                        logger.info("HO_SUCCOUTINTERCUNG 指标修正 after：" + stringList[HO_SUCCOUTINTERCUNG]);

                                        logger.info("HO_SUCCOUTINTERCUXN 指标修正 before：" + reader.get(HO_SUCCOUTINTERCUXN));
                                        stringList[HO_SUCCOUTINTERCUXN] = reader.get(HO_ATTOUTINTERCUXN);
                                        logger.info("HO_SUCCOUTINTERCUXN 指标修正 after：" + stringList[HO_SUCCOUTINTERCUXN]);

                                        logger.info("HO_SUCCOUTINTRACUINTERDU 指标修正 before：" + reader.get(HO_SUCCOUTINTRACUINTERDU));
                                        stringList[HO_SUCCOUTINTRACUINTERDU] = reader.get(HO_ATTOUTINTRACUINTERDU);
                                        logger.info("HO_SUCCOUTINTRACUINTERDU 指标修正 after：" + stringList[HO_SUCCOUTINTRACUINTERDU]);
                                    }

                                    if (on6 == 1 && StringToInt(reader.get(IRATHO_ATTOUTEUTRAN))>0) {
                                        String value1 = String.valueOf(Math.ceil(StringToDouble(reader.get(IRATHO_ATTOUTEUTRAN)) * 0.99));
                                        logger.info("IRATHO_ATTOUTEUTRAN_EPSFALLBACK 指标修正 before：" + reader.get(IRATHO_ATTOUTEUTRAN_EPSFALLBACK));
                                        stringList[IRATHO_ATTOUTEUTRAN_EPSFALLBACK] = value1;
                                        logger.info("IRATHO_ATTOUTEUTRAN_EPSFALLBACK 指标修正 after：" + stringList[IRATHO_ATTOUTEUTRAN_EPSFALLBACK]);

                                        logger.info("IRATHO_SUCCOUTEUTRAN_EPSFALLBACK 指标修正 before：" + reader.get(IRATHO_SUCCOUTEUTRAN_EPSFALLBACK));
                                        stringList[IRATHO_SUCCOUTEUTRAN_EPSFALLBACK] = value1;
                                        logger.info("IRATHO_SUCCOUTEUTRAN_EPSFALLBACK 指标修正 after：" + stringList[IRATHO_SUCCOUTEUTRAN_EPSFALLBACK]);

                                        logger.info("IRATHO_SUCCPREPOUTEUTRAN_EPSFALLBACK 指标修正 before：" + reader.get(IRATHO_SUCCPREPOUTEUTRAN_EPSFALLBACK));
                                        stringList[IRATHO_SUCCPREPOUTEUTRAN_EPSFALLBACK] = value1;
                                        logger.info("IRATHO_SUCCPREPOUTEUTRAN_EPSFALLBACK 指标修正 after：" + stringList[IRATHO_SUCCPREPOUTEUTRAN_EPSFALLBACK]);

                                        logger.info("IRATHO_SUCCPREPOUTEUTRAN 指标修正 before：" + reader.get(IRATHO_SUCCPREPOUTEUTRAN));
                                        stringList[IRATHO_SUCCPREPOUTEUTRAN] = stringList[IRATHO_ATTOUTEUTRAN];
                                        logger.info("IRATHO_SUCCPREPOUTEUTRAN 指标修正 after：" + stringList[IRATHO_SUCCPREPOUTEUTRAN]);

                                        logger.info("IRATHO_SUCCOUTEUTRAN 指标修正 before：" + reader.get(IRATHO_SUCCOUTEUTRAN));
                                        stringList[IRATHO_SUCCOUTEUTRAN] = stringList[IRATHO_ATTOUTEUTRAN];
                                        logger.info("IRATHO_SUCCOUTEUTRAN 指标修正 after：" + stringList[IRATHO_SUCCOUTEUTRAN]);

                                        logger.info("IRATHO_FAILPREPOUTEUTRAN_AMF 指标修正 before：" + reader.get(IRATHO_FAILPREPOUTEUTRAN_AMF));
                                        stringList[IRATHO_FAILPREPOUTEUTRAN_AMF] = "0";
                                        logger.info("IRATHO_FAILPREPOUTEUTRAN_AMF 指标修正 after：" + stringList[IRATHO_FAILPREPOUTEUTRAN_AMF]);

                                        logger.info("IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_AMF 指标修正 before：" + reader.get(IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_AMF));
                                        stringList[IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_AMF] = "0";
                                        logger.info("IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_AMF 指标修正 after：" + stringList[IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_AMF]);

                                        logger.info("IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_NOREPLY 指标修正 before：" + reader.get(IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_NOREPLY));
                                        stringList[IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_NOREPLY] = "0";
                                        logger.info("IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_NOREPLY 指标修正 after：" + stringList[IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_NOREPLY]);

                                        logger.info("IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_PREPFAILURE 指标修正 before：" + reader.get(IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_PREPFAILURE));
                                        stringList[IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_PREPFAILURE] = "0";
                                        logger.info("IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_PREPFAILURE 指标修正 after：" + stringList[IRATHO_FAILPREPOUTEUTRAN_EPSFALLBACK_PREPFAILURE]);

                                        logger.info("IRATHO_FAILPREPOUTEUTRAN 指标修正 before：" + reader.get(IRATHO_FAILPREPOUTEUTRAN));
                                        stringList[IRATHO_FAILPREPOUTEUTRAN] = "0";
                                        logger.info("IRATHO_FAILPREPOUTEUTRAN 指标修正 after：" + stringList[IRATHO_FAILPREPOUTEUTRAN]);

                                        logger.info("IRATHO_FAILPREPOUTEUTRAN_PREPFAILURE 指标修正 before：" + reader.get(IRATHO_FAILPREPOUTEUTRAN_PREPFAILURE));
                                        stringList[IRATHO_FAILPREPOUTEUTRAN_PREPFAILURE] = "0";
                                        logger.info("IRATHO_FAILPREPOUTEUTRAN_PREPFAILURE 指标修正 after：" + stringList[IRATHO_FAILPREPOUTEUTRAN_PREPFAILURE]);

                                    }

                                    if (on7 == 1 && StringToInt(reader.get(RRC_REDIRECTTOLTE))>0) {
                                        logger.info("RRC_REDIRECTTOLTE_EPSFALLBACK 指标修正 before：" + reader.get(RRC_REDIRECTTOLTE_EPSFALLBACK));
                                        stringList[RRC_REDIRECTTOLTE_EPSFALLBACK] = String.valueOf(Math.ceil(StringToDouble(reader.get(RRC_REDIRECTTOLTE))*0.99));
                                        logger.info("RRC_REDIRECTTOLTE_EPSFALLBACK 指标修正 after：" + stringList[RRC_REDIRECTTOLTE_EPSFALLBACK]);
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
