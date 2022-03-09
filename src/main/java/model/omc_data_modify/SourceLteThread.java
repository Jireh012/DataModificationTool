package model.omc_data_modify;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.log4j.Logger;
import util.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static util.Const.*;
import static util.FileUtil.isChartPathExist;
import static util.Mathematical.StringToInt;

/**
 * @author last_
 */
public class SourceLteThread implements Runnable {

    private CountDownLatch threadsSignal;
    private Map.Entry<String, List<String>> sourceData;

    private Logger logger = Logger.getLogger(SourceLteThread.class);

    public SourceLteThread(CountDownLatch threadsSignal, Map.Entry<String, List<String>> sourceData) {
        this.threadsSignal = threadsSignal;
        this.sourceData = sourceData;
    }

    private void initDataPosition(CsvReader reader) throws IOException {
        int i = 0;
        //初始化相关列位置
        while (i < reader.getValues().length) {
            switch (reader.get(i)) {
                case "RRC.SuccConnEstab":
                    RRC_SuccConnEstab = i;
                    break;
                case "RRC.AttConnEstab":
                    RRC_AttConnEstab = i;
                    break;
                case "ERAB.HoFail.1":
                    ERAB_HoFail_1 = i;
                    break;
                case "ERAB.NbrAttEstab.1":
                    ERAB_NbrAttEstab_1 = i;
                    break;
                case "ERAB.NbrSuccEstab.1":
                    ERAB_NbrSuccEstab_1 = i;
                    break;
                case "ERAB.NbrReqRelEnb.1":
                    ERAB_NbrReqRelEnb_1 = i;
                    break;
                case "ERAB.NbrReqRelEnb.Normal.1":
                    ERAB_NbrReqRelEnb_Normal_1 = i;
                    break;


                case "RRU.PdschPrbAssn":
                    RRU_PdschPrbAssn_position = i;
                    break;
                case "RRU.PdschPrbTot":
                    RRU_PdschPrbTot_position = i;
                    break;
                case "RRU.PuschPrbAssn":
                    RRU_PuschPrbAssn_position = i;
                    break;
                case "RRU.PuschPrbTot":
                    RRU_PuschPrbTot_position = i;
                    break;
                case "PHY.NbrCqi0":
                    PHY_NbrCqi0_position = i;
                    break;
                case "PHY.NbrCqi1":
                    PHY_NbrCqi1_position = i;
                    break;
                case "PHY.NbrCqi2":
                    PHY_NbrCqi2_position = i;
                    break;
                case "PHY.NbrCqi3":
                    PHY_NbrCqi3_position = i;
                    break;
                case "PHY.NbrCqi4":
                    PHY_NbrCqi4_position = i;
                    break;
                case "PHY.NbrCqi5":
                    PHY_NbrCqi5_position = i;
                    break;
                case "PHY.NbrCqi6":
                    PHY_NbrCqi6_position = i;
                    break;
                case "PHY.NbrCqi7":
                    PHY_NbrCqi7_position = i;
                    break;
                case "PHY.NbrCqi8":
                    PHY_NbrCqi8_position = i;
                    break;
                case "PHY.NbrCqi9":
                    PHY_NbrCqi9_position = i;
                    break;
                case "PHY.NbrCqi10":
                    PHY_NbrCqi10_position = i;
                    break;
                case "PHY.NbrCqi11":
                    PHY_NbrCqi11_position = i;
                    break;
                case "PHY.NbrCqi12":
                    PHY_NbrCqi12_position = i;
                    break;
                case "PHY.NbrCqi13":
                    PHY_NbrCqi13_position = i;
                    break;
                case "PHY.NbrCqi14":
                    PHY_NbrCqi14_position = i;
                    break;
                case "PHY.NbrCqi15":
                    PHY_NbrCqi15_position = i;
                    break;
                case "PHY.ULMeanNL.PRB0":
                    PHY_ULMeanNL_PRB0_position = i;
                    break;
                case "PHY.ULMeanNL.PRB1":
                    PHY_ULMeanNL_PRB1_position = i;
                    break;
                case "PHY.ULMeanNL.PRB2":
                    PHY_ULMeanNL_PRB2_position = i;
                    break;
                case "PHY.ULMeanNL.PRB3":
                    PHY_ULMeanNL_PRB3_position = i;
                    break;
                case "PHY.ULMeanNL.PRB4":
                    PHY_ULMeanNL_PRB4_position = i;
                    break;
                case "PHY.ULMeanNL.PRB5":
                    PHY_ULMeanNL_PRB5_position = i;
                    break;
                case "PHY.ULMeanNL.PRB6":
                    PHY_ULMeanNL_PRB6_position = i;
                    break;
                case "PHY.ULMeanNL.PRB7":
                    PHY_ULMeanNL_PRB7_position = i;
                    break;
                case "PHY.ULMeanNL.PRB8":
                    PHY_ULMeanNL_PRB8_position = i;
                    break;
                case "PHY.ULMeanNL.PRB9":
                    PHY_ULMeanNL_PRB9_position = i;
                    break;
                case "PHY.ULMeanNL.PRB10":
                    PHY_ULMeanNL_PRB10_position = i;
                    break;
                case "PHY.ULMeanNL.PRB11":
                    PHY_ULMeanNL_PRB11_position = i;
                    break;
                case "PHY.ULMeanNL.PRB12":
                    PHY_ULMeanNL_PRB12_position = i;
                    break;
                case "PHY.ULMeanNL.PRB13":
                    PHY_ULMeanNL_PRB13_position = i;
                    break;
                case "PHY.ULMeanNL.PRB14":
                    PHY_ULMeanNL_PRB14_position = i;
                    break;
                case "PHY.ULMeanNL.PRB15":
                    PHY_ULMeanNL_PRB15_position = i;
                    break;
                case "PHY.ULMeanNL.PRB16":
                    PHY_ULMeanNL_PRB16_position = i;
                    break;
                case "PHY.ULMeanNL.PRB17":
                    PHY_ULMeanNL_PRB17_position = i;
                    break;
                case "PHY.ULMeanNL.PRB18":
                    PHY_ULMeanNL_PRB18_position = i;
                    break;
                case "PHY.ULMeanNL.PRB19":
                    PHY_ULMeanNL_PRB19_position = i;
                    break;
                case "PHY.ULMeanNL.PRB20":
                    PHY_ULMeanNL_PRB20_position = i;
                    break;
                case "PHY.ULMeanNL.PRB21":
                    PHY_ULMeanNL_PRB21_position = i;
                    break;
                case "PHY.ULMeanNL.PRB22":
                    PHY_ULMeanNL_PRB22_position = i;
                    break;
                case "PHY.ULMeanNL.PRB23":
                    PHY_ULMeanNL_PRB23_position = i;
                    break;
                case "PHY.ULMeanNL.PRB24":
                    PHY_ULMeanNL_PRB24_position = i;
                    break;
                case "PHY.ULMeanNL.PRB25":
                    PHY_ULMeanNL_PRB25_position = i;
                    break;
                case "PHY.ULMeanNL.PRB26":
                    PHY_ULMeanNL_PRB26_position = i;
                    break;
                case "PHY.ULMeanNL.PRB27":
                    PHY_ULMeanNL_PRB27_position = i;
                    break;
                case "PHY.ULMeanNL.PRB28":
                    PHY_ULMeanNL_PRB28_position = i;
                    break;
                case "PHY.ULMeanNL.PRB29":
                    PHY_ULMeanNL_PRB29_position = i;
                    break;
                case "PHY.ULMeanNL.PRB30":
                    PHY_ULMeanNL_PRB30_position = i;
                    break;
                case "PHY.ULMeanNL.PRB31":
                    PHY_ULMeanNL_PRB31_position = i;
                    break;
                case "PHY.ULMeanNL.PRB32":
                    PHY_ULMeanNL_PRB32_position = i;
                    break;
                case "PHY.ULMeanNL.PRB33":
                    PHY_ULMeanNL_PRB33_position = i;
                    break;
                case "PHY.ULMeanNL.PRB34":
                    PHY_ULMeanNL_PRB34_position = i;
                    break;
                case "PHY.ULMeanNL.PRB35":
                    PHY_ULMeanNL_PRB35_position = i;
                    break;
                case "PHY.ULMeanNL.PRB36":
                    PHY_ULMeanNL_PRB36_position = i;
                    break;
                case "PHY.ULMeanNL.PRB37":
                    PHY_ULMeanNL_PRB37_position = i;
                    break;
                case "PHY.ULMeanNL.PRB38":
                    PHY_ULMeanNL_PRB38_position = i;
                    break;
                case "PHY.ULMeanNL.PRB39":
                    PHY_ULMeanNL_PRB39_position = i;
                    break;
                case "PHY.ULMeanNL.PRB40":
                    PHY_ULMeanNL_PRB40_position = i;
                    break;
                case "PHY.ULMeanNL.PRB41":
                    PHY_ULMeanNL_PRB41_position = i;
                    break;
                case "PHY.ULMeanNL.PRB42":
                    PHY_ULMeanNL_PRB42_position = i;
                    break;
                case "PHY.ULMeanNL.PRB43":
                    PHY_ULMeanNL_PRB43_position = i;
                    break;
                case "PHY.ULMeanNL.PRB44":
                    PHY_ULMeanNL_PRB44_position = i;
                    break;
                case "PHY.ULMeanNL.PRB45":
                    PHY_ULMeanNL_PRB45_position = i;
                    break;
                case "PHY.ULMeanNL.PRB46":
                    PHY_ULMeanNL_PRB46_position = i;
                    break;
                case "PHY.ULMeanNL.PRB47":
                    PHY_ULMeanNL_PRB47_position = i;
                    break;
                case "PHY.ULMeanNL.PRB48":
                    PHY_ULMeanNL_PRB48_position = i;
                    break;
                case "PHY.ULMeanNL.PRB49":
                    PHY_ULMeanNL_PRB49_position = i;
                    break;
                case "PHY.ULMeanNL.PRB50":
                    PHY_ULMeanNL_PRB50_position = i;
                    break;
                case "PHY.ULMeanNL.PRB51":
                    PHY_ULMeanNL_PRB51_position = i;
                    break;
                case "PHY.ULMeanNL.PRB52":
                    PHY_ULMeanNL_PRB52_position = i;
                    break;
                case "PHY.ULMeanNL.PRB53":
                    PHY_ULMeanNL_PRB53_position = i;
                    break;
                case "PHY.ULMeanNL.PRB54":
                    PHY_ULMeanNL_PRB54_position = i;
                    break;
                case "PHY.ULMeanNL.PRB55":
                    PHY_ULMeanNL_PRB55_position = i;
                    break;
                case "PHY.ULMeanNL.PRB56":
                    PHY_ULMeanNL_PRB56_position = i;
                    break;
                case "PHY.ULMeanNL.PRB57":
                    PHY_ULMeanNL_PRB57_position = i;
                    break;
                case "PHY.ULMeanNL.PRB58":
                    PHY_ULMeanNL_PRB58_position = i;
                    break;
                case "PHY.ULMeanNL.PRB59":
                    PHY_ULMeanNL_PRB59_position = i;
                    break;
                case "PHY.ULMeanNL.PRB60":
                    PHY_ULMeanNL_PRB60_position = i;
                    break;
                case "PHY.ULMeanNL.PRB61":
                    PHY_ULMeanNL_PRB61_position = i;
                    break;
                case "PHY.ULMeanNL.PRB62":
                    PHY_ULMeanNL_PRB62_position = i;
                    break;
                case "PHY.ULMeanNL.PRB63":
                    PHY_ULMeanNL_PRB63_position = i;
                    break;
                case "PHY.ULMeanNL.PRB64":
                    PHY_ULMeanNL_PRB64_position = i;
                    break;
                case "PHY.ULMeanNL.PRB65":
                    PHY_ULMeanNL_PRB65_position = i;
                    break;
                case "PHY.ULMeanNL.PRB66":
                    PHY_ULMeanNL_PRB66_position = i;
                    break;
                case "PHY.ULMeanNL.PRB67":
                    PHY_ULMeanNL_PRB67_position = i;
                    break;
                case "PHY.ULMeanNL.PRB68":
                    PHY_ULMeanNL_PRB68_position = i;
                    break;
                case "PHY.ULMeanNL.PRB69":
                    PHY_ULMeanNL_PRB69_position = i;
                    break;
                case "PHY.ULMeanNL.PRB70":
                    PHY_ULMeanNL_PRB70_position = i;
                    break;
                case "PHY.ULMeanNL.PRB71":
                    PHY_ULMeanNL_PRB71_position = i;
                    break;
                case "PHY.ULMeanNL.PRB72":
                    PHY_ULMeanNL_PRB72_position = i;
                    break;
                case "PHY.ULMeanNL.PRB73":
                    PHY_ULMeanNL_PRB73_position = i;
                    break;
                case "PHY.ULMeanNL.PRB74":
                    PHY_ULMeanNL_PRB74_position = i;
                    break;
                case "PHY.ULMeanNL.PRB75":
                    PHY_ULMeanNL_PRB75_position = i;
                    break;
                case "PHY.ULMeanNL.PRB76":
                    PHY_ULMeanNL_PRB76_position = i;
                    break;
                case "PHY.ULMeanNL.PRB77":
                    PHY_ULMeanNL_PRB77_position = i;
                    break;
                case "PHY.ULMeanNL.PRB78":
                    PHY_ULMeanNL_PRB78_position = i;
                    break;
                case "PHY.ULMeanNL.PRB79":
                    PHY_ULMeanNL_PRB79_position = i;
                    break;
                case "PHY.ULMeanNL.PRB80":
                    PHY_ULMeanNL_PRB80_position = i;
                    break;
                case "PHY.ULMeanNL.PRB81":
                    PHY_ULMeanNL_PRB81_position = i;
                    break;
                case "PHY.ULMeanNL.PRB82":
                    PHY_ULMeanNL_PRB82_position = i;
                    break;
                case "PHY.ULMeanNL.PRB83":
                    PHY_ULMeanNL_PRB83_position = i;
                    break;
                case "PHY.ULMeanNL.PRB84":
                    PHY_ULMeanNL_PRB84_position = i;
                    break;
                case "PHY.ULMeanNL.PRB85":
                    PHY_ULMeanNL_PRB85_position = i;
                    break;
                case "PHY.ULMeanNL.PRB86":
                    PHY_ULMeanNL_PRB86_position = i;
                    break;
                case "PHY.ULMeanNL.PRB87":
                    PHY_ULMeanNL_PRB87_position = i;
                    break;
                case "PHY.ULMeanNL.PRB88":
                    PHY_ULMeanNL_PRB88_position = i;
                    break;
                case "PHY.ULMeanNL.PRB89":
                    PHY_ULMeanNL_PRB89_position = i;
                    break;
                case "PHY.ULMeanNL.PRB90":
                    PHY_ULMeanNL_PRB90_position = i;
                    break;
                case "PHY.ULMeanNL.PRB91":
                    PHY_ULMeanNL_PRB91_position = i;
                    break;
                case "PHY.ULMeanNL.PRB92":
                    PHY_ULMeanNL_PRB92_position = i;
                    break;
                case "PHY.ULMeanNL.PRB93":
                    PHY_ULMeanNL_PRB93_position = i;
                    break;
                case "PHY.ULMeanNL.PRB94":
                    PHY_ULMeanNL_PRB94_position = i;
                    break;
                case "PHY.ULMeanNL.PRB95":
                    PHY_ULMeanNL_PRB95_position = i;
                    break;
                case "PHY.ULMeanNL.PRB96":
                    PHY_ULMeanNL_PRB96_position = i;
                    break;
                case "PHY.ULMeanNL.PRB97":
                    PHY_ULMeanNL_PRB97_position = i;
                    break;
                case "PHY.ULMeanNL.PRB98":
                    PHY_ULMeanNL_PRB98_position = i;
                    break;
                case "PHY.ULMeanNL.PRB99":
                    PHY_ULMeanNL_PRB99_position = i;
                    break;

                case "PDCP.UpOctDl":
                    PDCP_UpOctDl = i;
                    break;
                case "PDCP.UpOctDl.9":
                    PDCP_UpOctDl9 = i;
                    break;
                case "PDCP.UpOctUl":
                    PDCP_UpOctUl = i;
                    break;
                case "PDCP.UpOctUl.9":
                    PDCP_UpOctUl9 = i;
                    break;

                case "PDCP.ThrpTimeDL":
                    PDCP_ThrpTimeDL = i;
                    break;
                case "PDCP.ThrpTimeDL.9":
                    PDCP_ThrpTimeDL9 = i;
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
            String type1 = sourceData.getKey().split("￥")[1];
            logger.info("SFTP登陆成功耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
            String path = null;
            String fileName = null;
            String str = null;
            long tttt = System.currentTimeMillis();
            if ("1".equals(TestModel)) {
                logger.info("获取：" + TestDirNameYmDH + "/PM-ENB-LTE-EUTRANCELL" + type1 + "-" +
                        properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                TestDirNameYmDH + "/PM-ENB-LTE-EUTRANCELL" + type1 + "-" +
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
                            Thread.sleep(SleepTime * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logger.error("线程异常：" + e.getMessage());
                        }
                    }
                }
                if ("[]".equals(str) || str == null) {
                    threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
                    logger.info(Thread.currentThread().getName() + "结束. 还有"
                            + threadsSignal.getCount() + " 个线程");
                    return;
                } else {
                    assert str != null;
                    String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
                    fileName = "PM-ENB-LTE-EUTRANCELL" + type1 + "-" + properties.get(source + ".id") +
                            "-" + verSion + "-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv";
                    logger.info("测试模式 文件名：" + fileName);
                    path = saveFilePath + TestDirNameYmDH + TestFileNameMMss + "_" + source + "_" + type1 + tttt + File.separator;
                    isChartPathExist(path);
                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + TestDirNameYmDH,
                            fileName + ".gz", path + fileName + ".gz");
                }
            } else {
                logger.info("获取：" + nowTime + "/PM-ENB-LTE-EUTRANCELL" + type1 + "-" +
                        properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                nowTime + "/PM-ENB-LTE-EUTRANCELL" + type1 + "-" +
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
                            Thread.sleep(SleepTime * 1000);
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
                    assert str != null;
                    String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
                    fileName = "PM-ENB-LTE-EUTRANCELL" + type1 + "-" + properties.get(source + ".id") + "-" +
                            verSion + "-" + nowTime + TimeMm + "-15.csv";
                    logger.info("正常模式 文件名：" + fileName);
                    path = saveFilePath + nowTime + TimeMm + "_" + source + "_" + type1 + tttt + File.separator;
                    isChartPathExist(path);
                    logger.info("========开始下载文件========");
                    try {
                        SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + nowTime, fileName + ".gz", path + fileName + ".gz");
                        logger.info("========开始删除文件========");
                        SftpUtilM.delete(sftp, properties.get(source + ".path") + "/" + nowTime, fileName + ".gz");
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
                        logger.info(Thread.currentThread().getName() + "结束. 还有"
                                + threadsSignal.getCount() + " 个线程");
                        return;
                    }
                }
            }
            UnCompressFileGZIP.doUncompressFile(path + fileName + ".gz");
            isChartPathExist(path + "Write" + File.separator);
            File y1 = new File(path + fileName + ".gz");
            logger.info(fileName + ".gz " + "原文件大小：" + y1.length());
            csvRun(path + fileName, path + "Write" + File.separator + fileName,
                    sourceData.getValue());
            logger.info("文件处理耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
            CompressFileGZIP.doCompressFile(path + "Write" + File.separator + fileName);
            File file = new File(path + "Write" + File.separator + fileName + ".gz");
            logger.info(fileName + ".gz " + "文件大小：" + file.length());
            try {
                InputStream is = new FileInputStream(file);
                if ("1".equals(TestModel)) {
                    SftpUtilM.upload(sftp, "/", properties.get(source + ".path") + "/" +
                            TestDirNameYmDH, fileName + ".gz", is);
                } else {
                    SftpUtilM.upload(sftp, "/", properties.get(source + ".path") + "/" +
                            nowTime, fileName + ".gz", is);
                }
                logger.info("=======" + source + type1 + "上传成功=======");
                long dalen;
                if ("1".equals(TestModel)) {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            TestDirNameYmDH + "/PM-ENB-LTE-EUTRANCELL" + type1 + "-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz").getSize();
                    logger.info("PM-ENB-LTE-EUTRANCELL" + type1 + "-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz  文件大小为：" + dalen);
                } else {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            nowTime + "/PM-ENB-LTE-EUTRANCELL" + type1 + "-" +
                            properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz").getSize();
                    logger.info("PM-ENB-LTE-EUTRANCELL" + type1 + "-" +
                            properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz  文件大小为：" + dalen);
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("文件操作异常：" + e.getMessage());
            } catch (SftpException e) {
                e.printStackTrace();
                logger.error("SFTP操作异常：" + e.getMessage());
            }

            logger.info("原文件上传166服务器");
            FileInputStream in = null;
            try {
                in = new FileInputStream(new File(path + fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.warn("文件操作错误");
            }

            FtpUtil.uploadFile("10.212.194.166", "zhangyang", "zhangyang", 221, source + nowTime, fileName, in);

        }

        // 线程结束时计数器减1
        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
        logger.info(Thread.currentThread().getName() + "结束. 还有"
                + threadsSignal.getCount() + " 个线程 耗时：" +
                (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
    }

    private void exit() {
        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
        logger.info(Thread.currentThread().getName() + "结束. 还有"
                + threadsSignal.getCount() + " 个线程");
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
                    if (value.toString().contains(reader.get(Integer.parseInt(Const.aimsType)) + "￥")) {
                        for (String li : value) {
                            if (reader.get(Integer.parseInt(Const.aimsType)).equals(li.split("￥")[2])) {
                                int pdschPrbAssn = Integer.parseInt(li.split("￥")[3]);
                                int puschPrbAssn = Integer.parseInt(li.split("￥")[4]);
                                int nbrCqi = Integer.parseInt(li.split("￥")[5]);
                                int ulmeannl = Integer.parseInt(li.split("￥")[6]);
                                int on1 = Integer.parseInt(li.split("￥")[8]);
                                int on2 = Integer.parseInt(li.split("￥")[9]);
                                int on3 = Integer.parseInt(li.split("￥")[10]);

                                try {
                                    if (pdschPrbAssn == 1) {
                                        int pdschPrbAssnData = StringToInt(reader.get(RRU_PdschPrbAssn_position));
                                        int pdschPrbTotData = StringToInt(reader.get(RRU_PdschPrbTot_position));
                                        if ((float) pdschPrbAssnData / pdschPrbTotData > 0.5) {
                                            logger.info("下行指标修正 当前：" + reader.get(2));
                                            logger.info("下行指标 当前值：" + pdschPrbAssnData);
                                            int min = 40, max = 50;
                                            double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
                                            //下行修改完成
                                            stringList[RRU_PdschPrbAssn_position] = String.valueOf(
                                                    (int) (rd * pdschPrbAssnData / 100) * 100);
                                            logger.info("下行指标 修改后值：" + stringList[RRU_PdschPrbAssn_position]);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warn("pdschPrbAssn aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
                                }

                                try {
                                    if (puschPrbAssn == 1) {
                                        //上行
                                        int puschPrbAssnData = StringToInt(reader.get(RRU_PuschPrbAssn_position));
                                        int puschPrbTot = StringToInt(reader.get(RRU_PuschPrbTot_position));
                                        if ((float) puschPrbAssnData / puschPrbTot > 0.5) {
                                            logger.info("上行指标修正 当前" + reader.get(2));
                                            logger.info("上行指标 当前值" + puschPrbAssnData);
                                            int min = 40, max = 50;
                                            double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
                                            stringList[RRU_PuschPrbAssn_position] = String.valueOf(
                                                    (int) (rd * puschPrbAssnData / 100) * 100);
                                            logger.info("puschPrbAssn 上行指标 修改后值：" + stringList[RRU_PuschPrbAssn_position]);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warn("aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
                                }

                                try {
                                    if (nbrCqi == 1) {
                                        int sum0To6 = 0, sum7To15 = 0;
                                        int[] intList = new int[7];
                                        for (int i = 0; i <= 15; i++) {
                                            if (i <= 6) {
                                                sum0To6 = sum0To6 + Integer.parseInt(reader.get(getPHYNbrCqiposition(i)));
                                                intList[i] = Integer.parseInt(reader.get(getPHYNbrCqiposition(i)));
                                            } else {
                                                sum7To15 = sum7To15 + Integer.parseInt(reader.get(getPHYNbrCqiposition(i)));
                                            }
                                        }
                                        if ((float) sum0To6 / (sum0To6 + sum7To15) > 0.2) {
                                            logger.info("NbrCqi指标修正 当前" + reader.get(2));
                                            double n = Math.ceil(sum0To6 - 0.25 * sum7To15);
                                            int rnd = (int) (new Random().nextDouble() * (sum0To6 - (n)) + (n));
                                            for (int j = 0; j <= 6; j++) {
                                                logger.info("NbrCqi指标" + j + " 修正前：" + stringList[getPHYNbrCqiposition(j)]);
                                                double bl = (double) intList[j] / sum0To6;
                                                stringList[getPHYNbrCqiposition(j)] = String.valueOf(Integer.parseInt(
                                                        stringList[getPHYNbrCqiposition(j)]) - (int) (rnd * bl));
                                                logger.info("NbrCqi指标" + j + " 修正后：" + stringList[getPHYNbrCqiposition(j)]);
                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warn("nbrCqi aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
                                }

                                try {
                                    if (ulmeannl == 1) {
                                        for (int i = 0; i <= 99; i++) {
                                            if (!reader.get(getPHYULMeanNLPRBPosition(i)).isEmpty() && Integer.parseInt(reader.get(getPHYULMeanNLPRBPosition(i))) > -110) {
                                                logger.info("ULMeanNL指标修正 当前:" + reader.get(2));
                                                logger.info("ULMeanNL指标 当前:" + reader.get(getPHYULMeanNLPRBPosition(i)));
                                                int min1 = 114, max1 = 118;
                                                int rd1 = -(min1 + (int) (Math.random() * ((max1 - min1) + 1)));
                                                stringList[getPHYULMeanNLPRBPosition(i)] = String.valueOf(rd1);
                                                logger.info("ULMeanNL指标 修改后:" + stringList[getPHYULMeanNLPRBPosition(i)]);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warn("ulmeannl aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
                                }

                                try {
                                    if (on1 == 1) {
                                        logger.info("ERAB_HoFail_1 指标修正 当前：" + reader.get(2));
                                        logger.info("ERAB_HoFail_1 指标修正 原值：" + stringList[ERAB_HoFail_1]);
                                        stringList[ERAB_HoFail_1] = String.valueOf(0);
                                        logger.info("ERAB_HoFail_1 指标修正 值：" + String.valueOf(0));

                                        logger.info("ERAB_NbrAttEstab_1 指标修正 当前：" + reader.get(2));
                                        logger.info("ERAB_NbrAttEstab_1 指标修正 原值：" + stringList[ERAB_NbrAttEstab_1]);
                                        stringList[ERAB_NbrAttEstab_1] = stringList[ERAB_NbrSuccEstab_1];
                                        logger.info("ERAB_NbrAttEstab_1 指标修正 值：" + stringList[ERAB_NbrSuccEstab_1]);

                                        logger.info("ERAB_NbrReqRelEnb_1 标修正 当前：" + reader.get(2));
                                        logger.info("ERAB_NbrReqRelEnb_1 标修正 原值：" + stringList[ERAB_NbrReqRelEnb_1]);
                                        stringList[ERAB_NbrReqRelEnb_1] = stringList[ERAB_NbrReqRelEnb_Normal_1];
                                        logger.info("ERAB_NbrReqRelEnb_1 标修正 值：" + stringList[ERAB_NbrReqRelEnb_Normal_1]);

                                        logger.info("RRC_AttConnEstab 标修正 当前：" + reader.get(2));
                                        logger.info("RRC_AttConnEstab 标修正 原值：" + stringList[RRC_AttConnEstab]);
                                        stringList[RRC_AttConnEstab] = stringList[RRC_SuccConnEstab];
                                        logger.info("RRC_AttConnEstab 标修正 值：" + stringList[RRC_SuccConnEstab]);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warn("on1 aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
                                }

                                try {
                                    if (on2 == 1) {
                                        if (reader.get(PDCP_UpOctDl).equals("0")) {
                                            logger.info("PDCP_UpOctDl 标修正 当前：" + reader.get(2));
                                            int min = 0, max = 100;
                                            double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
                                            stringList[PDCP_UpOctDl9] = String.valueOf(
                                                    (int) (rd * 378820 / 100) * 100);
                                            stringList[PDCP_UpOctDl] = stringList[PDCP_UpOctDl9];
                                            logger.info("PDCP_UpOctDl 标修正 值：" + stringList[PDCP_UpOctDl]);
                                        }

                                        if (reader.get(PDCP_UpOctUl).equals("0")) {
                                            logger.info("PDCP_UpOctUl 标修正 当前：" + reader.get(2));
                                            int min = 0, max = 100;
                                            double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
                                            stringList[PDCP_UpOctUl9] = String.valueOf(
                                                    (int) (rd * 25064 / 100) * 100);
                                            stringList[PDCP_UpOctUl] = stringList[PDCP_UpOctUl9];
                                            logger.info("PDCP.UpOctUl 标修正 值：" + stringList[PDCP_UpOctUl9]);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warn("on2 aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
                                }

                                try {
                                    if (on3 == 1) {
                                        int PDCP_UpOctDlData = (int) Float.parseFloat(reader.get(PDCP_UpOctDl));
                                        int PDCP_ThrpTimeDLData = (int) Float.parseFloat(reader.get(PDCP_ThrpTimeDL));
                                        int PDCP_ThrpTimeDL9Data = (int) Float.parseFloat(reader.get(PDCP_ThrpTimeDL9));
                                        int data = PDCP_UpOctDlData * 8 / PDCP_ThrpTimeDLData;

                                        if (data < 20) {
                                            logger.info("ON3 修正 " + reader.get(2) + " " + data);
                                            int min = 100, max = 110;
                                            double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
                                            logger.info("PDCP_ThrpTimeDL9 修正前：" + stringList[PDCP_ThrpTimeDL9]);
                                            stringList[PDCP_ThrpTimeDL9] = String.valueOf((int) (PDCP_UpOctDlData * 8 / (rd * 20)));
                                            logger.info("PDCP_ThrpTimeDL9 修正后：" + stringList[PDCP_ThrpTimeDL9]);

                                            logger.info("PDCP_ThrpTimeDL 修正前：" + stringList[PDCP_ThrpTimeDL]);
                                            stringList[PDCP_ThrpTimeDL] = String.valueOf(PDCP_ThrpTimeDLData +
                                                    (int) (Float.parseFloat(stringList[PDCP_ThrpTimeDL9]) - PDCP_ThrpTimeDL9Data));
                                            logger.info("PDCP_ThrpTimeDL 修正前：" + stringList[PDCP_ThrpTimeDL]);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warn("on3 aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
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

    private int ERAB_HoFail_1 = 0;
    private int ERAB_NbrAttEstab_1 = 0;
    private int ERAB_NbrSuccEstab_1 = 0;
    private int ERAB_NbrReqRelEnb_1 = 0;
    private int ERAB_NbrReqRelEnb_Normal_1 = 0;
    private int RRC_AttConnEstab = 0;
    private int RRC_SuccConnEstab = 0;

    private int RRU_PdschPrbAssn_position = 0;
    private int RRU_PdschPrbTot_position = 0;
    private int RRU_PuschPrbTot_position = 0;
    private int RRU_PuschPrbAssn_position = 0;
    private int PHY_NbrCqi0_position = 0;
    private int PHY_NbrCqi1_position = 0;
    private int PHY_NbrCqi2_position = 0;
    private int PHY_NbrCqi3_position = 0;
    private int PHY_NbrCqi4_position = 0;
    private int PHY_NbrCqi5_position = 0;
    private int PHY_NbrCqi6_position = 0;
    private int PHY_NbrCqi7_position = 0;
    private int PHY_NbrCqi8_position = 0;
    private int PHY_NbrCqi9_position = 0;
    private int PHY_NbrCqi10_position = 0;
    private int PHY_NbrCqi11_position = 0;
    private int PHY_NbrCqi12_position = 0;
    private int PHY_NbrCqi13_position = 0;
    private int PHY_NbrCqi14_position = 0;
    private int PHY_NbrCqi15_position = 0;
    private int PHY_ULMeanNL_PRB0_position = 0;
    private int PHY_ULMeanNL_PRB1_position = 0;
    private int PHY_ULMeanNL_PRB2_position = 0;
    private int PHY_ULMeanNL_PRB3_position = 0;
    private int PHY_ULMeanNL_PRB4_position = 0;
    private int PHY_ULMeanNL_PRB5_position = 0;
    private int PHY_ULMeanNL_PRB6_position = 0;
    private int PHY_ULMeanNL_PRB7_position = 0;
    private int PHY_ULMeanNL_PRB8_position = 0;
    private int PHY_ULMeanNL_PRB9_position = 0;
    private int PHY_ULMeanNL_PRB10_position = 0;
    private int PHY_ULMeanNL_PRB11_position = 0;
    private int PHY_ULMeanNL_PRB12_position = 0;
    private int PHY_ULMeanNL_PRB13_position = 0;
    private int PHY_ULMeanNL_PRB14_position = 0;
    private int PHY_ULMeanNL_PRB15_position = 0;
    private int PHY_ULMeanNL_PRB16_position = 0;
    private int PHY_ULMeanNL_PRB17_position = 0;
    private int PHY_ULMeanNL_PRB18_position = 0;
    private int PHY_ULMeanNL_PRB19_position = 0;
    private int PHY_ULMeanNL_PRB20_position = 0;
    private int PHY_ULMeanNL_PRB21_position = 0;
    private int PHY_ULMeanNL_PRB22_position = 0;
    private int PHY_ULMeanNL_PRB23_position = 0;
    private int PHY_ULMeanNL_PRB24_position = 0;
    private int PHY_ULMeanNL_PRB25_position = 0;
    private int PHY_ULMeanNL_PRB26_position = 0;
    private int PHY_ULMeanNL_PRB27_position = 0;
    private int PHY_ULMeanNL_PRB28_position = 0;
    private int PHY_ULMeanNL_PRB29_position = 0;
    private int PHY_ULMeanNL_PRB30_position = 0;
    private int PHY_ULMeanNL_PRB31_position = 0;
    private int PHY_ULMeanNL_PRB32_position = 0;
    private int PHY_ULMeanNL_PRB33_position = 0;
    private int PHY_ULMeanNL_PRB34_position = 0;
    private int PHY_ULMeanNL_PRB35_position = 0;
    private int PHY_ULMeanNL_PRB36_position = 0;
    private int PHY_ULMeanNL_PRB37_position = 0;
    private int PHY_ULMeanNL_PRB38_position = 0;
    private int PHY_ULMeanNL_PRB39_position = 0;
    private int PHY_ULMeanNL_PRB40_position = 0;
    private int PHY_ULMeanNL_PRB41_position = 0;
    private int PHY_ULMeanNL_PRB42_position = 0;
    private int PHY_ULMeanNL_PRB43_position = 0;
    private int PHY_ULMeanNL_PRB44_position = 0;
    private int PHY_ULMeanNL_PRB45_position = 0;
    private int PHY_ULMeanNL_PRB46_position = 0;
    private int PHY_ULMeanNL_PRB47_position = 0;
    private int PHY_ULMeanNL_PRB48_position = 0;
    private int PHY_ULMeanNL_PRB49_position = 0;
    private int PHY_ULMeanNL_PRB50_position = 0;
    private int PHY_ULMeanNL_PRB51_position = 0;
    private int PHY_ULMeanNL_PRB52_position = 0;
    private int PHY_ULMeanNL_PRB53_position = 0;
    private int PHY_ULMeanNL_PRB54_position = 0;
    private int PHY_ULMeanNL_PRB55_position = 0;
    private int PHY_ULMeanNL_PRB56_position = 0;
    private int PHY_ULMeanNL_PRB57_position = 0;
    private int PHY_ULMeanNL_PRB58_position = 0;
    private int PHY_ULMeanNL_PRB59_position = 0;
    private int PHY_ULMeanNL_PRB60_position = 0;
    private int PHY_ULMeanNL_PRB61_position = 0;
    private int PHY_ULMeanNL_PRB62_position = 0;
    private int PHY_ULMeanNL_PRB63_position = 0;
    private int PHY_ULMeanNL_PRB64_position = 0;
    private int PHY_ULMeanNL_PRB65_position = 0;
    private int PHY_ULMeanNL_PRB66_position = 0;
    private int PHY_ULMeanNL_PRB67_position = 0;
    private int PHY_ULMeanNL_PRB68_position = 0;
    private int PHY_ULMeanNL_PRB69_position = 0;
    private int PHY_ULMeanNL_PRB70_position = 0;
    private int PHY_ULMeanNL_PRB71_position = 0;
    private int PHY_ULMeanNL_PRB72_position = 0;
    private int PHY_ULMeanNL_PRB73_position = 0;
    private int PHY_ULMeanNL_PRB74_position = 0;
    private int PHY_ULMeanNL_PRB75_position = 0;
    private int PHY_ULMeanNL_PRB76_position = 0;
    private int PHY_ULMeanNL_PRB77_position = 0;
    private int PHY_ULMeanNL_PRB78_position = 0;
    private int PHY_ULMeanNL_PRB79_position = 0;
    private int PHY_ULMeanNL_PRB80_position = 0;
    private int PHY_ULMeanNL_PRB81_position = 0;
    private int PHY_ULMeanNL_PRB82_position = 0;
    private int PHY_ULMeanNL_PRB83_position = 0;
    private int PHY_ULMeanNL_PRB84_position = 0;
    private int PHY_ULMeanNL_PRB85_position = 0;
    private int PHY_ULMeanNL_PRB86_position = 0;
    private int PHY_ULMeanNL_PRB87_position = 0;
    private int PHY_ULMeanNL_PRB88_position = 0;
    private int PHY_ULMeanNL_PRB89_position = 0;
    private int PHY_ULMeanNL_PRB90_position = 0;
    private int PHY_ULMeanNL_PRB91_position = 0;
    private int PHY_ULMeanNL_PRB92_position = 0;
    private int PHY_ULMeanNL_PRB93_position = 0;
    private int PHY_ULMeanNL_PRB94_position = 0;
    private int PHY_ULMeanNL_PRB95_position = 0;
    private int PHY_ULMeanNL_PRB96_position = 0;
    private int PHY_ULMeanNL_PRB97_position = 0;
    private int PHY_ULMeanNL_PRB98_position = 0;
    private int PHY_ULMeanNL_PRB99_position = 0;


    private int PDCP_UpOctDl = 0;
    private int PDCP_UpOctDl9 = 0;

    private int PDCP_UpOctUl = 0;
    private int PDCP_UpOctUl9 = 0;

    private int PDCP_ThrpTimeDL = 0;
    private int PDCP_ThrpTimeDL9 = 0;

    private int getPHYULMeanNLPRBPosition(int c) {
        switch (c) {
            case 0:
                return PHY_ULMeanNL_PRB0_position;
            case 1:
                return PHY_ULMeanNL_PRB1_position;
            case 2:
                return PHY_ULMeanNL_PRB2_position;
            case 3:
                return PHY_ULMeanNL_PRB3_position;
            case 4:
                return PHY_ULMeanNL_PRB4_position;
            case 5:
                return PHY_ULMeanNL_PRB5_position;
            case 6:
                return PHY_ULMeanNL_PRB6_position;
            case 7:
                return PHY_ULMeanNL_PRB7_position;
            case 8:
                return PHY_ULMeanNL_PRB8_position;
            case 9:
                return PHY_ULMeanNL_PRB9_position;
            case 10:
                return PHY_ULMeanNL_PRB10_position;
            case 11:
                return PHY_ULMeanNL_PRB11_position;
            case 12:
                return PHY_ULMeanNL_PRB12_position;
            case 13:
                return PHY_ULMeanNL_PRB13_position;
            case 14:
                return PHY_ULMeanNL_PRB14_position;
            case 15:
                return PHY_ULMeanNL_PRB15_position;
            case 16:
                return PHY_ULMeanNL_PRB16_position;
            case 17:
                return PHY_ULMeanNL_PRB17_position;
            case 18:
                return PHY_ULMeanNL_PRB18_position;
            case 19:
                return PHY_ULMeanNL_PRB19_position;
            case 20:
                return PHY_ULMeanNL_PRB20_position;
            case 21:
                return PHY_ULMeanNL_PRB21_position;
            case 22:
                return PHY_ULMeanNL_PRB22_position;
            case 23:
                return PHY_ULMeanNL_PRB23_position;
            case 24:
                return PHY_ULMeanNL_PRB24_position;
            case 25:
                return PHY_ULMeanNL_PRB25_position;
            case 26:
                return PHY_ULMeanNL_PRB26_position;
            case 27:
                return PHY_ULMeanNL_PRB27_position;
            case 28:
                return PHY_ULMeanNL_PRB28_position;
            case 29:
                return PHY_ULMeanNL_PRB29_position;
            case 30:
                return PHY_ULMeanNL_PRB30_position;
            case 31:
                return PHY_ULMeanNL_PRB31_position;
            case 32:
                return PHY_ULMeanNL_PRB32_position;
            case 33:
                return PHY_ULMeanNL_PRB33_position;
            case 34:
                return PHY_ULMeanNL_PRB34_position;
            case 35:
                return PHY_ULMeanNL_PRB35_position;
            case 36:
                return PHY_ULMeanNL_PRB36_position;
            case 37:
                return PHY_ULMeanNL_PRB37_position;
            case 38:
                return PHY_ULMeanNL_PRB38_position;
            case 39:
                return PHY_ULMeanNL_PRB39_position;
            case 40:
                return PHY_ULMeanNL_PRB40_position;
            case 41:
                return PHY_ULMeanNL_PRB41_position;
            case 42:
                return PHY_ULMeanNL_PRB42_position;
            case 43:
                return PHY_ULMeanNL_PRB43_position;
            case 44:
                return PHY_ULMeanNL_PRB44_position;
            case 45:
                return PHY_ULMeanNL_PRB45_position;
            case 46:
                return PHY_ULMeanNL_PRB46_position;
            case 47:
                return PHY_ULMeanNL_PRB47_position;
            case 48:
                return PHY_ULMeanNL_PRB48_position;
            case 49:
                return PHY_ULMeanNL_PRB49_position;
            case 50:
                return PHY_ULMeanNL_PRB50_position;
            case 51:
                return PHY_ULMeanNL_PRB51_position;
            case 52:
                return PHY_ULMeanNL_PRB52_position;
            case 53:
                return PHY_ULMeanNL_PRB53_position;
            case 54:
                return PHY_ULMeanNL_PRB54_position;
            case 55:
                return PHY_ULMeanNL_PRB55_position;
            case 56:
                return PHY_ULMeanNL_PRB56_position;
            case 57:
                return PHY_ULMeanNL_PRB57_position;
            case 58:
                return PHY_ULMeanNL_PRB58_position;
            case 59:
                return PHY_ULMeanNL_PRB59_position;
            case 60:
                return PHY_ULMeanNL_PRB60_position;
            case 61:
                return PHY_ULMeanNL_PRB61_position;
            case 62:
                return PHY_ULMeanNL_PRB62_position;
            case 63:
                return PHY_ULMeanNL_PRB63_position;
            case 64:
                return PHY_ULMeanNL_PRB64_position;
            case 65:
                return PHY_ULMeanNL_PRB65_position;
            case 66:
                return PHY_ULMeanNL_PRB66_position;
            case 67:
                return PHY_ULMeanNL_PRB67_position;
            case 68:
                return PHY_ULMeanNL_PRB68_position;
            case 69:
                return PHY_ULMeanNL_PRB69_position;
            case 70:
                return PHY_ULMeanNL_PRB70_position;
            case 71:
                return PHY_ULMeanNL_PRB71_position;
            case 72:
                return PHY_ULMeanNL_PRB72_position;
            case 73:
                return PHY_ULMeanNL_PRB73_position;
            case 74:
                return PHY_ULMeanNL_PRB74_position;
            case 75:
                return PHY_ULMeanNL_PRB75_position;
            case 76:
                return PHY_ULMeanNL_PRB76_position;
            case 77:
                return PHY_ULMeanNL_PRB77_position;
            case 78:
                return PHY_ULMeanNL_PRB78_position;
            case 79:
                return PHY_ULMeanNL_PRB79_position;
            case 80:
                return PHY_ULMeanNL_PRB80_position;
            case 81:
                return PHY_ULMeanNL_PRB81_position;
            case 82:
                return PHY_ULMeanNL_PRB82_position;
            case 83:
                return PHY_ULMeanNL_PRB83_position;
            case 84:
                return PHY_ULMeanNL_PRB84_position;
            case 85:
                return PHY_ULMeanNL_PRB85_position;
            case 86:
                return PHY_ULMeanNL_PRB86_position;
            case 87:
                return PHY_ULMeanNL_PRB87_position;
            case 88:
                return PHY_ULMeanNL_PRB88_position;
            case 89:
                return PHY_ULMeanNL_PRB89_position;
            case 90:
                return PHY_ULMeanNL_PRB90_position;
            case 91:
                return PHY_ULMeanNL_PRB91_position;
            case 92:
                return PHY_ULMeanNL_PRB92_position;
            case 93:
                return PHY_ULMeanNL_PRB93_position;
            case 94:
                return PHY_ULMeanNL_PRB94_position;
            case 95:
                return PHY_ULMeanNL_PRB95_position;
            case 96:
                return PHY_ULMeanNL_PRB96_position;
            case 97:
                return PHY_ULMeanNL_PRB97_position;
            case 98:
                return PHY_ULMeanNL_PRB98_position;
            case 99:
                return PHY_ULMeanNL_PRB99_position;
            default:
                return 0;
        }

    }

    private int getPHYNbrCqiposition(int c) {
        switch (c) {
            case 0:
                return PHY_NbrCqi0_position;
            case 1:
                return PHY_NbrCqi1_position;
            case 2:
                return PHY_NbrCqi2_position;
            case 3:
                return PHY_NbrCqi3_position;
            case 4:
                return PHY_NbrCqi4_position;
            case 5:
                return PHY_NbrCqi5_position;
            case 6:
                return PHY_NbrCqi6_position;
            case 7:
                return PHY_NbrCqi7_position;
            case 8:
                return PHY_NbrCqi8_position;
            case 9:
                return PHY_NbrCqi9_position;
            case 10:
                return PHY_NbrCqi10_position;
            case 11:
                return PHY_NbrCqi11_position;
            case 12:
                return PHY_NbrCqi12_position;
            case 13:
                return PHY_NbrCqi13_position;
            case 14:
                return PHY_NbrCqi14_position;
            case 15:
                return PHY_NbrCqi15_position;
            default:
                return 0;
        }
    }


}
