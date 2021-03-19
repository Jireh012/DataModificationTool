package org.zayl.jireh.tool.datamodify.util;

import java.text.DecimalFormat;

/**
 * @author last_
 */
public class Mathematical {
    /**
     * TODO 除法运算，保留小数
     *
     * @param a 被除数
     * @param b 除数
     * @return 商
     * @author 袁忠明
     * @date 2018-4-17下午2:24:48
     */
    public static String txfloat(int a, int b) {
        // TODO 自动生成的方法存根
        //设置保留位数
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((float) a / b);
    }

    public static int StringToInt(String d){
        double num;
        DecimalFormat myformat=new DecimalFormat("#0");
        num=Double.parseDouble(d);//装换为double类型
        num=Double.parseDouble(myformat.format(num));//保留2为小数
        return (int)num;
    }

}
