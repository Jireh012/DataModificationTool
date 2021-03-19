package org.zayl.jireh.tool.datamodify.util;

import com.jcraft.jsch.Channel;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author last_
 */
public class Const {
	public static String sourceFilePath="";
	public static String saveFilePath="";
	public static String downFieldConfigFile="";
	public static String aimsType="";
	public static String TestModel="";
	public static String TestFileNameMMss="";
	public static String TestDirNameYmDH="";
	public static String isProxy="";
	public static String ProxyName="";
	public static String ProxyPassword="";
	public static int ProxyPort=0;
	public static String ProxyAddress="";
	public static int ProxyProtocol=0;
	public static int FileDelSwitch=0;
	public static int t00DataSTime=18;
	public static int t00DataETime=33;
	public static int t15DataSTime=33;
	public static int t15DataETime=48;
	public static int t30DataSTime=48;
	public static int t30DataETime=3;
	public static int t45DataSTime=3;
	public static int t45DataETime=18;
	public static int ForCount=20;
	public static int SleepTime=30;

	public static long startTime=0;
	public static String nowTime="";
	public static String TimeMm="";



	/**
	 * 创建一个map用于存放channel对象
	 */
	public static final Map<String, Channel> SFTP_CHANNEL_POOL = new HashMap<String, Channel>();
	public static final Map<String, Channel> SSH_CHANNEL_POOL = new HashMap<String, Channel>();
	public static final Map<String, String> FILE_IS_DOWN = new HashMap<String, String>();
	public static Properties SOURCE_PRO = null;

	/**
	 * 数据库连接
	 */
	public static Connection conn = null;

	public static String url="";
	public static String name="";
	public static String user="";
	public static String password="";

}
