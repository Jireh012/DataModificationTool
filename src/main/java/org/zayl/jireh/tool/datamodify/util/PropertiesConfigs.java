package org.zayl.jireh.tool.datamodify.util;

import org.apache.log4j.Logger;
import sun.java2d.cmm.Profile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author last_
 */
public class PropertiesConfigs {

    private static final Logger logger = Logger.getLogger(PropertiesConfigs.class);

    /**
     * 读取包外config.properties 配置文件
     *
     * @return Properties
     */
    public static Properties getProConfig() {
        Properties p = new Properties();
        InputStreamReader in = new InputStreamReader(new BufferedInputStream(PropertiesConfigs.class.getResourceAsStream("/config/config.properties")), StandardCharsets.UTF_8);
        try {
            p.load(in);
            in.close();
        } catch (IOException e) {
            //读取配置文件异常
            e.printStackTrace();
            logger.error("读取配置文件异常:" + e.getMessage());
        }
        return p;
    }

    /**
     * 读取包内config.properties 配置文件
     *
     * @return Properties
     */
    static Properties getPackageProConfig() {
        InputStreamReader in = new InputStreamReader(new BufferedInputStream(Objects.requireNonNull(PropertiesConfigs.class.getClassLoader().getResourceAsStream("config.properties"))), StandardCharsets.UTF_8);
        Properties p = new Properties();
        try {
            p.load(in);
            in.close();
        } catch (IOException e) {
            //读取配置文件出错
            e.printStackTrace();
        }
        return p;
    }

    public static void loadConf(String path) throws Exception {
        Properties props = new Properties();
        InputStreamReader in = new InputStreamReader(new BufferedInputStream(new FileInputStream(path)), StandardCharsets.UTF_8);
        loadProperty(props, in);
    }

    public static void loadConf1(String path) throws Exception {
        Properties props = new Properties();
        InputStreamReader in = new InputStreamReader(new BufferedInputStream(new FileInputStream(path)), StandardCharsets.UTF_8);
        loadProperty(props, in);
        Const.downFieldConfigFile =props.getProperty("downFieldConfigFile");
        Const.url=props.getProperty("url");
        Const.name=props.getProperty("name");
        Const.user=props.getProperty("user");
        Const.password=props.getProperty("password");
    }

    private static void loadProperty(Properties props, InputStreamReader in) throws IOException {
        props.load(in);
        Const.sourceFilePath = props.getProperty("sourceFilePath");
        Const.saveFilePath = props.getProperty("saveFilePath");
        Const.aimsType=props.getProperty("aimsType");

        Const.TestModel =props.getProperty("TestModel");
        Const.TestFileNameMMss =props.getProperty("TestFileNameMMss");
        Const.TestDirNameYmDH =props.getProperty("TestDirNameYmDH");
        Const.isProxy =props.getProperty("isProxy");
        Const.ProxyName =props.getProperty("ProxyName");
        Const.ProxyPassword =props.getProperty("ProxyPassword");
        Const.ProxyPort =Integer.parseInt(props.getProperty("ProxyPort"));
        Const.ProxyAddress =props.getProperty("ProxyAddress");
        Const.ProxyProtocol =Integer.parseInt(props.getProperty("ProxyProtocol"));
        Const.FileDelSwitch =Integer.parseInt(props.getProperty("FileDelSwitch"));

        Const.t00DataSTime =Integer.parseInt(props.getProperty("00DataSTime"));
        Const.t00DataETime =Integer.parseInt(props.getProperty("00DataETime"));
        Const.t15DataSTime =Integer.parseInt(props.getProperty("15DataSTime"));
        Const.t15DataETime =Integer.parseInt(props.getProperty("15DataETime"));
        Const.t30DataSTime =Integer.parseInt(props.getProperty("30DataSTime"));
        Const.t30DataETime =Integer.parseInt(props.getProperty("30DataETime"));
        Const.t45DataSTime =Integer.parseInt(props.getProperty("45DataSTime"));
        Const.t45DataETime =Integer.parseInt(props.getProperty("45DataETime"));
        Const.ForCount =Integer.parseInt(props.getProperty("ForCount"));
        Const.SleepTime =Integer.parseInt(props.getProperty("SleepTime"));

        Const.SFTP_PORT =Integer.parseInt(props.getProperty("SFTP_PORT"));
        Const.SFTP_PASSWORD =props.getProperty("SFTP_PASSWORD");
        Const.SFTP_USERNAME =props.getProperty("SFTP_USERNAME");
        Const.SFTP_ADDRESS =props.getProperty("SFTP_ADDRESS");
        Const.SFTP_DOWNLOAD_PATH =props.getProperty("SFTP_DOWNLOAD_PATH");


    }

    public static void loadConf() throws Exception {
        Properties props = new Properties();
        InputStreamReader in = new InputStreamReader(new BufferedInputStream(Objects.requireNonNull(PropertiesConfigs.class.getClassLoader().getResourceAsStream("config.properties"))), StandardCharsets.UTF_8);
        loadProperty(props, in);
        Properties props1 = new Properties();
        InputStreamReader in1 = new InputStreamReader(new BufferedInputStream(Objects.requireNonNull(PropertiesConfigs.class.getClassLoader().getResourceAsStream("sourceConfig.properties"))), StandardCharsets.UTF_8);
        props1.load(in1);
        Const.SOURCE_PRO=props1;
    }

    public static void loadConf1() throws Exception {
        Properties props = new Properties();
        InputStreamReader in = new InputStreamReader(new BufferedInputStream(Objects.requireNonNull(PropertiesConfigs.class.getClassLoader().getResourceAsStream("config1.properties"))), StandardCharsets.UTF_8);
        loadProperty(props, in);
        Const.downFieldConfigFile =props.getProperty("downFieldConfigFile");
        Const.url=props.getProperty("url");
        Const.name=props.getProperty("name");
        Const.user=props.getProperty("user");
        Const.password=props.getProperty("password");

        Properties props1 = new Properties();
        InputStreamReader in1 = new InputStreamReader(new BufferedInputStream(Objects.requireNonNull(PropertiesConfigs.class.getClassLoader().getResourceAsStream("sourceConfig1.properties"))), StandardCharsets.UTF_8);
        props1.load(in1);
        Const.SOURCE_PRO=props1;
    }

    public static Properties loadSource() throws Exception {
        Properties props = new Properties();
        InputStreamReader in = new InputStreamReader(new BufferedInputStream(PropertiesConfigs.class.getResourceAsStream("/config/sourceConfig.properties")), StandardCharsets.UTF_8);
        props.load(in);
        return props;
    }

    public static void loadSource(String path) throws Exception {
        Properties props = new Properties();
        InputStreamReader in = new InputStreamReader(new BufferedInputStream(new FileInputStream(path)), StandardCharsets.UTF_8);
        props.load(in);
        Const.SOURCE_PRO=props;
    }

    /**
     * 传递键值对的Map，更新properties文件
     *
     * @param fileName
     *            文件名(放在resource源包目录下)，需要后缀
     * @param keyValueMap
     *            键值对Map
     */
    public static void updateProperties(String fileName, Map<String, String> keyValueMap) {
        // 输入流
        // InputStream
        // inputStream=PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
        // 获取文件的路径
        String filePath = Profile.class.getClassLoader().getResource(fileName).getFile();
        System.out.println("propertiesPath:" + filePath);
        Properties props = new Properties();
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            // 从输入流中读取属性列表（键和元素对）
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            props.load(br);
            br.close();
            // 写入属性文件
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
            // 清空旧的文件
            // props.clear();
            for (String key : keyValueMap.keySet()) {
                props.setProperty(key, keyValueMap.get(key));
            }
            props.store(bw, "改变数据");
            System.out.println(props.getProperty("url"));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Visit " + filePath + " for updating " + "" + " value error");
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
