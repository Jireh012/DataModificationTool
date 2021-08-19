package org.zayl.jireh.tool.datahandle;

import org.apache.log4j.Logger;
import org.zayl.jireh.tool.datahandle.extractedtype.ExtractedType0;
import org.zayl.jireh.tool.datahandle.extractedtype.ExtractedType1;
import org.zayl.jireh.tool.datahandle.extractedtype.ExtractedType2;
import org.zayl.jireh.tool.datahandle.util.PropertiesConfigs;
import org.zayl.jireh.tool.datahandle.util.RemoteShellExecutor;
import org.zayl.jireh.tool.datahandle.util.SftpUtilM;

import static org.zayl.jireh.tool.datahandle.util.Const.startTime;

/**
 * @author last_
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);
    private static Integer type = 0;

    public static void main(String[] args) throws Exception {
        logger.info("===========main开始============");
        startTime = System.currentTimeMillis();
        try {
            if (args.length <= 1) {
                PropertiesConfigs.loadConf();
                type = Integer.valueOf(args[0]);
            } else {
                PropertiesConfigs.loadConf(args[0]);
                PropertiesConfigs.loadSource(args[1]);
                type = Integer.valueOf(args[2]);
            }
        } catch (Exception e) {
            //读取配置异常
            e.printStackTrace();
            logger.error("读取配置异常" + e.getMessage());
        }

        switch (type) {
            default:
            case 0:
                ExtractedType0.run();
                break;
            case 1:
                ExtractedType1.run();
                break;
            case 2:
                ExtractedType2.run();
                break;
        }

        SftpUtilM.logoutList();
        RemoteShellExecutor.close();
        // do work end
        //退出主进程
        logger.info("===========主程序结束===========");
        logger.info("本次耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
    }

}
