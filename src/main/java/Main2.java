import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.Const;
import util.PropertiesConfigs;
import util.SftpUtilM;

import java.io.File;
import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import static util.Const.*;

/**
 * @author last_
 * Sftp Files download export to Sql
 */
public class Main2 {

    private static Logger logger = Logger.getLogger(Main2.class);

    public static void main(String[] args) throws Exception {
        logger.info("===========main开始============");
        startTime = System.currentTimeMillis();
        try {
            if (args.length == 0) {
                PropertiesConfigs.loadConf1();
            } else {
                PropertiesConfigs.loadConf1(args[0]);
                PropertiesConfigs.loadSource(args[1]);
                System.out.println(name);
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

        initDataBase();

        if (!"1".equals(TestModel)) {
            initTimeData();
        }

        File xlsFile = new File(sourceFilePath);
        // 获得工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
        // 获得工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
        Map<String, String> map = new HashMap<>();
        for (int s = 1; s < sheet.getPhysicalNumberOfRows(); s++) {
            XSSFRow sheetRow = sheet.getRow(s);
            System.out.println(sheetRow.getCell(0).toString());
            String source = sheetRow.getCell(0).toString();
            String type = sheetRow.getCell(1).toString();
            map.put(source, type);
        }

        File xlsFileDown = new File(downFieldConfigFile);
        // 获得工作簿
        XSSFWorkbook workbookDown = new XSSFWorkbook(new FileInputStream(xlsFileDown));
        // 获得工作表
        XSSFSheet sheetDown = workbookDown.getSheetAt(0);
        Map<String, Integer> mapDown = new HashMap<>();
        for (int s = 1; s < sheetDown.getPhysicalNumberOfRows(); s++) {
            XSSFRow sheetRow = sheetDown.getRow(s);
            String key = sheetRow.getCell(0).toString();
            int value = new Double(sheetRow.getCell(2).getNumericCellValue()).intValue();
            String type = sheetRow.getCell(3).toString();


            mapDown.put(key+"$"+type, value);
        }

        int threadNum = map.size();
        CountDownLatch threadSignal1 = new CountDownLatch(threadNum);
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("enmbx-pool-%d").setDaemon(true).build();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(threadNum,
                threadFactory);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            Runnable task = new Source1Thread(threadSignal1, entry,mapDown);
            // 执行
            executorService.execute(task);
        }

        // 等待所有子线程执行完
        threadSignal1.await();
        //固定线程池执行完成后 将释放掉资源 退出主进程
        //并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        executorService.shutdown();

        SftpUtilM.logoutList();
        conn.close();

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

    public static void initDataBase(){
        String url = Const.url;
        String name = Const.name;
        String user = Const.user;
        String password = Const.password;
        try {
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);//获取连接
            conn.setAutoCommit(false);//关闭自动提交，不然conn.commit()运行到这句会报错
        } catch (ClassNotFoundException | SQLException e1) {
            logger.error(e1.getMessage());
        }
    }
}
