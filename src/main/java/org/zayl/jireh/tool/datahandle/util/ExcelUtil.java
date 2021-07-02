package org.zayl.jireh.tool.datahandle.util;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jireh
 */
public class ExcelUtil {

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
