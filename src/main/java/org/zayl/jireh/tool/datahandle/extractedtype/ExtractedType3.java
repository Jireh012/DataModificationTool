package org.zayl.jireh.tool.datahandle.extractedtype;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zayl.jireh.tool.datahandle.model.type0.HandleGnbSaCuThread;
import org.zayl.jireh.tool.datahandle.model.type0.HandleGnbSaDuThread;
import org.zayl.jireh.tool.datahandle.model.type3.HandleGnbSaLcuThread;
import org.zayl.jireh.tool.datahandle.model.type3.HandleGnbSaLcupThread;
import org.zayl.jireh.tool.datahandle.model.type3.HandleGnbSaLduThread;
import org.zayl.jireh.tool.datahandle.util.CommonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import static org.zayl.jireh.tool.datahandle.util.Const.*;

public class ExtractedType3 {
    private static final Logger logger = Logger.getLogger(ExtractedType3.class);

    public static void run() throws IOException, InterruptedException {
        if (SOURCE_PRO == null) {
            logger.warn("数据源异常，请检查sourceConfig是否被正确加载");
            return;
        }

        if (!"1".equals(TestModel)) {
            CommonUtils.initTimeData();
        }

        File xlsFile = new File(sourceFilePath);
        // 获得工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
        // 获得工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
        Map<String, List<String>> map = new HashMap<>(16);
        for (int s = 1; s < sheet.getPhysicalNumberOfRows(); s++) {
            XSSFRow sheetRow = sheet.getRow(s);
            String source = sheetRow.getCell(0).toString();
            XSSFCell aims = sheetRow.getCell(1);
            int on1 = (int) sheetRow.getCell(2).getNumericCellValue();
            int on2 = (int) sheetRow.getCell(3).getNumericCellValue();
            int on3 = (int) sheetRow.getCell(4).getNumericCellValue();
            int on4 = (int) sheetRow.getCell(5).getNumericCellValue();
            int on5 = (int) sheetRow.getCell(6).getNumericCellValue();

            // map是否包含此key，若已经包含则添加一个新的数字到对应value集合中
            String e = source + "￥" + aims + "￥" + on1 + "￥" + on2 + "￥" + on3 + "￥" + on4+ "￥" + on5;

            if (map.containsKey(source)) {
                map.get(source).add(e);
            } else {
                // map不包含此key，则重新创建一个新集合，并把这个数字添加进集合
                // ，再把集合放到map中
                List<String> newList = new ArrayList<>();
                newList.add(e);
                map.put(source, newList);
            }
        }

        int threadNum = map.size();
        CountDownLatch threadSignal1 = new CountDownLatch(threadNum);
        CountDownLatch threadSignal2 = new CountDownLatch(threadNum);
        CountDownLatch threadSignal3 = new CountDownLatch(threadNum);
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("data-modify-pool-%d").setDaemon(true).build();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(threadNum,
                threadFactory);
        ScheduledExecutorService executorService2 = new ScheduledThreadPoolExecutor(threadNum,
                threadFactory);
        ScheduledExecutorService executorService3 = new ScheduledThreadPoolExecutor(threadNum,
                threadFactory);
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            Runnable task1 = new HandleGnbSaLcuThread(threadSignal1, entry);
            Runnable task2 = new HandleGnbSaLcupThread(threadSignal2, entry);
            Runnable task3 = new HandleGnbSaLduThread(threadSignal3, entry);
            // 执行
            executorService.execute(task1);
            executorService2.execute(task2);
            executorService3.execute(task3);
        }

        // 等待所有子线程执行完
        threadSignal1.await();
        threadSignal2.await();
        threadSignal3.await();

        //固定线程池执行完成后 将释放掉资源 退出主进程
        //并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        executorService.shutdown();
        executorService2.shutdown();
        executorService3.shutdown();
    }
}
