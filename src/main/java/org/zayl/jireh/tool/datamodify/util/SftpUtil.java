package org.zayl.jireh.tool.datamodify.util;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

import static org.zayl.jireh.tool.datamodify.util.Const.*;


/**
 * 类说明 sftp工具类
 *
 * @author last_
 */
public class SftpUtil {
    private transient Logger log = Logger.getLogger(this.getClass());

    private ChannelSftp sftp;

    private Session session;
    /**
     * SFTP 登录用户名
     */
    private String username;
    /**
     * SFTP 登录密码
     */
    private String password;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * SFTP 服务器地址IP地址
     */
    private String host;
    /**
     * SFTP 端口
     */
    private int port;


    /**
     * 构造基于密码认证的sftp对象
     */
    public SftpUtil(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    /**
     * 构造基于秘钥认证的sftp对象
     */
    public SftpUtil(String username, String host, int port, String privateKey) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    public SftpUtil() {
    }


    /**
     * 连接sftp服务器
     */
    public void login() {
        try {
            JSch jsch = new JSch();
            if (privateKey != null) {
                // 设置私钥
                jsch.addIdentity(privateKey);
            }

            session = jsch.getSession(username, host, port);

            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            if ("true".equals(isProxy)) {
                switch (ProxyProtocol) {
                    case 1:
                        ProxySOCKS5 proxySOCKS5 = new ProxySOCKS5(ProxyAddress, ProxyPort);
                        proxySOCKS5.setUserPasswd(ProxyName, ProxyPassword);
                        session.setProxy(proxySOCKS5);
                        break;
                    case 2:
                        ProxySOCKS4 proxySOCKS4 = new ProxySOCKS4(ProxyAddress, ProxyPort);
                        proxySOCKS4.setUserPasswd(ProxyName, ProxyPassword);
                        session.setProxy(proxySOCKS4);
                        break;
                    case 3:
                        ProxyHTTP proxyHTTP = new ProxyHTTP(ProxyAddress, ProxyPort);
                        proxyHTTP.setUserPasswd(ProxyName, ProxyPassword);
                        session.setProxy(proxyHTTP);
                        break;
                    default:
                }
            }


            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接 server
     */
    public void logout() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }


    /**
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
     *
     * @param basePath     服务器的基础路径
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     * @param input        输入流
     */
    public void upload(String basePath, String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(basePath);
            sftp.cd(directory);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            String[] dirs = directory.split("/");
            String tempPath = basePath;
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) {
                    continue;
                }
                tempPath += "/" + dir;
                try {
                    sftp.cd(tempPath);
                } catch (SftpException ex) {
                    sftp.mkdir(tempPath);
                    sftp.cd(tempPath);
                }
            }
        }
        //上传文件
        sftp.put(input, sftpFileName);
    }


    /**
     * 下载文件。
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);

        byte[] fileData = IOUtils.toByteArray(is);


        return fileData;
    }


    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) throws SftpException {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }


    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

    //上传文件测试
    public static void main(String[] args) throws SftpException, IOException {
        //SftpUtil sftp = new SftpUtil("ftpuser", "Tdomc4@ftp", "10.212.111.185", 22);
        String fn="E:\\aaaaa2333.gz";
        SftpUtil sftp = new SftpUtil("root", "OOoo0000", "10.77.18.246", 22);
        sftp.login();
//        sftp.download("/root/2019090713",
//                "PM-ENB-EUTRANCELLTDD-02-*-20190907130000-15.csv.gz",
//                fn);

        String str=sftp.listFiles("/root/2019090713/PM-ENB-EUTRANCELLTDD-02-*-20190907130000-15.csv.gz").toString();
        int s= str.indexOf("V");
        System.out.println(str.substring(s,s+6));
        sftp.logout();
        //UnCompressFileGZIP.doUncompressFile(fn);




    }
}