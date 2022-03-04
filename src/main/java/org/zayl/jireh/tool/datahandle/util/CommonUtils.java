package org.zayl.jireh.tool.datahandle.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.zayl.jireh.tool.datahandle.util.Const.*;

/**
 * @author Jireh
 * @date 2022-03-04
 */
public class CommonUtils {

    public static void initTimeData() {
        SimpleDateFormat mmFormat = new SimpleDateFormat("mm");
        int nowTimeMm = Integer.parseInt(mmFormat.format(new Date()));
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHH");
        if (nowTimeMm > t45DataSTime && nowTimeMm <= t45DataETime) {
            TimeMm = "4500";
            nowTime = timeFormat.format(new Date(System.currentTimeMillis() - 3600 * 1000));
        } else if (nowTimeMm > t00DataSTime && nowTimeMm <= t00DataETime) {
            TimeMm = "0000";
            nowTime = timeFormat.format(new Date(System.currentTimeMillis()));
        } else if (nowTimeMm > t15DataSTime && nowTimeMm <= t15DataETime) {
            TimeMm = "1500";
            nowTime = timeFormat.format(new Date(System.currentTimeMillis()));
        } else if (nowTimeMm > t30DataSTime || nowTimeMm <= t30DataETime) {
            TimeMm = "3000";
            if (nowTimeMm <= t30DataETime) {
                nowTime = timeFormat.format(new Date(System.currentTimeMillis() - 3600 * 1000));
            }
            if (nowTimeMm > t30DataSTime) {
                nowTime = timeFormat.format(new Date(System.currentTimeMillis()));
            }
        }
    }

}
