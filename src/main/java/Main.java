
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import model.omc_data_modify.*;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static util.Const.*;

/**
 * @author last_
 */
public class Main {

    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("===========main开始============");
        startTime = System.currentTimeMillis();
        try {
            if (args.length == 0) {
                PropertiesConfigs.loadConf();
            } else {
                PropertiesConfigs.loadConf(args[0]);
                PropertiesConfigs.loadSource(args[1]);
            }

            if (args[2] == null){
                throw new Exception("启动类型为空");
            }
        } catch (Exception e) {
            //读取配置异常
            e.printStackTrace();
            logger.error("读取配置异常" + e.getMessage());
        }
        if (SOURCE_PRO == null) {
            logger.warn("数据源异常，请检查sourceConfig是否被正确加载");
            return;
        }

        if (!"1".equals(TestModel)) {
            initTimeData();
        }

        File xlsFile = new File(sourceFilePath);
        // 获得工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
        // 获得工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
        Map<String, List<String>> map = new HashMap<>();
        Map<String, List<String>> map2 = new HashMap<>();
        for (int s = 1; s < sheet.getPhysicalNumberOfRows(); s++) {
            XSSFRow sheetRow = sheet.getRow(s);
            String source = sheetRow.getCell(0).toString();
            XSSFCell aims = sheetRow.getCell(1);
            String type1 = sheetRow.getCell(2).toString();
            int pdschPrbAssn = (int)sheetRow.getCell(3).getNumericCellValue();
            int puschPrbAssn = (int)sheetRow.getCell(4).getNumericCellValue();
            int nbrCqi = (int)sheetRow.getCell(5).getNumericCellValue();
            int ulmeannl = (int)sheetRow.getCell(6).getNumericCellValue();
            int estab= (int)sheetRow.getCell(7).getNumericCellValue();
            int on1=(int)sheetRow.getCell(8).getNumericCellValue();
            int on2=(int)sheetRow.getCell(9).getNumericCellValue();
            int on3=(int)sheetRow.getCell(10).getNumericCellValue();

            int sum01=pdschPrbAssn+puschPrbAssn+nbrCqi+ulmeannl+on1+on2+on3;

            // map是否包含此key，若已经包含则添加一个新的数字到对应value集合中
            String e = source + "￥" + type1 + "￥" +
                    aims + "￥" + pdschPrbAssn + "￥" + puschPrbAssn + "￥" + nbrCqi +
                    "￥" + ulmeannl+ "￥" +estab+ "￥" +on1+ "￥" +on2+ "￥" +on3;
            if(sum01>0){
                if (map.containsKey(source + "￥" + type1)) {
                    map.get(source + "￥" + type1).add(e);
                } else {
                    // map不包含此key，则重新创建一个新集合，并把这个数字添加进集合
                    // ，再把集合放到map中
                    List<String> newList = new ArrayList<>();
                    newList.add(e);
                    map.put(source + "￥" + type1, newList);
                }
            }

            if (estab==1){
                if (map2.containsKey(source + "￥" + type1)) {
                    map2.get(source + "￥" + type1).add(e);
                } else {
                    // map不包含此key，则重新创建一个新集合，并把这个数字添加进集合
                    // ，再把集合放到map中
                    List<String> newList = new ArrayList<>();
                    newList.add(e);
                    map2.put(source + "￥" + type1, newList);
                }
            }
        }

        int threadNum = map.size();
        int threadNum2 = map2.size();

        switch (args[2]){
            case "1":
                CountDownLatch threadSignal1 = new CountDownLatch(threadNum);
                CountDownLatch threadSignalLte = new CountDownLatch(threadNum);
                CountDownLatch threadSignal2 = new CountDownLatch(threadNum2);
                CountDownLatch threadSignal3 = new CountDownLatch(threadNum2);
                ThreadFactory threadFactory = new ThreadFactoryBuilder()
                        .setNameFormat("enmbx-pool-%d").setDaemon(true).build();
                ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(threadNum,
                        threadFactory);
                ScheduledExecutorService executorService2 = new ScheduledThreadPoolExecutor(threadNum2,
                        threadFactory);
                ScheduledExecutorService executorService3 = new ScheduledThreadPoolExecutor(threadNum2,
                        threadFactory);
                ScheduledExecutorService executorServiceLte = new ScheduledThreadPoolExecutor(threadNum,
                        threadFactory);
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    Runnable task = new SourceThread(threadSignal1, entry);
                    // 执行
                    executorService.execute(task);
                    Runnable taskLte = new SourceLteThread(threadSignalLte, entry);
                    // 执行
                    executorServiceLte.execute(taskLte);
                }

                for (Map.Entry<String, List<String>> entry : map2.entrySet()) {
                    Runnable task = new ConnEstabThread(threadSignal2, entry);
                    // 执行
                    executorService2.execute(task);

                    Runnable task1 = new CoverEstabThread(threadSignal3, entry);
                    // 执行
                    executorService3.execute(task1);
                }

                // 等待所有子线程执行完
                threadSignal1.await();
                threadSignal2.await();
                threadSignal3.await();
                threadSignalLte.await();
                //固定线程池执行完成后 将释放掉资源 退出主进程
                //并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
                executorService.shutdown();
                executorService2.shutdown();
                executorService3.shutdown();
                executorServiceLte.shutdown();
                break;
            case "2":
                CountDownLatch threadSignalTest2 = new CountDownLatch(threadNum);
                ThreadFactory threadFactoryTest2 = new ThreadFactoryBuilder()
                        .setNameFormat("enmbx-pool-%d").setDaemon(true).build();
                ScheduledExecutorService executorServiceTest2 = new ScheduledThreadPoolExecutor(threadNum,
                        threadFactoryTest2);
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    Runnable task = new DeleteThread(threadSignalTest2, entry);
                    // 执行
                    executorServiceTest2.execute(task);
                }
                threadSignalTest2.await();
                executorServiceTest2.shutdown();
                break;
            default:

        }
        SftpUtilM.logoutList();
        // do work end
        //退出主进程
        logger.info("===========主程序结束===========");
        logger.info("本次耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
    }

    private static void initTimeData() {
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

    /**
     * 描述：对表格中数值进行格式化
     *
     * @param cell
     * @return
     */
    public static Object getCellValue(XSSFCell cell) {
        Object value = null;
        DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); // 日期格式化
        DecimalFormat df2 = new DecimalFormat("0"); // 格式化数字

        switch (cell.getCellType()) {
            case STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = df.format(cell.getNumericCellValue());
                } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

    public static List<Map<String, Object>> getXls(XSSFWorkbook workbook) {
        // 返回数据
        List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
        XSSFRow row = null;
        XSSFCell cell = null;
        XSSFSheet sheet1 = null;
        // 遍历Excel中所有的sheet
        Map<String, String> m2 = new HashMap<String, String>();
        m2.put("sourceAddress", "sourceAddress");
        m2.put("aims", "aims");
        m2.put("type1", "type1");
        m2.put("PdschPrbAssn", "PdschPrbAssn");
        m2.put("PuschPrbAssn", "PuschPrbAssn");
        m2.put("NbrCqi", "NbrCqi");
        m2.put("ULMeanNL", "ULMeanNL");

        // 遍历Excel中所有的sheet
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet1 = workbook.getSheetAt(i);
            if (sheet1 == null) {
                continue;
            }

            // 取第一行标题
            row = sheet1.getRow(0);
            String[] title = null;
            if (row != null) {
                title = new String[row.getLastCellNum()];
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    title[y] = (String) getCellValue(cell);
                }
            } else {
                continue;
            }

            // 遍历当前sheet中的所有行
            for (int j = 1; j < sheet1.getLastRowNum() + 1; j++) {
                row = sheet1.getRow(j);
                Map<String, Object> m = new HashMap<String, Object>();
                // 遍历所有的列
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    String key = title[y];
                    m.put(m2.get(key), getCellValue(cell));
                }
                ls.add(m);
            }

        }
        return ls;
    }

}
