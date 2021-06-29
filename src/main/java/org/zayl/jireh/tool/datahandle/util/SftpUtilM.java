package org.zayl.jireh.tool.datahandle.util;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Vector;

import static org.zayl.jireh.tool.datahandle.util.Const.*;


/**
 * 类说明 sftp工具类
 *
 * @author last_
 */
public class SftpUtilM {
    private static Logger log = Logger.getLogger(SftpUtilM.class);

    /**
     * 连接sftp服务器
     */
    public static ChannelSftp login(String username, String password, String host, int port) {
        ChannelSftp sftp = null;
        try {
            if (SFTP_CHANNEL_POOL.get(host) == null) {
                JSch jsch = new JSch();
                Session session = jsch.getSession(username, host, port);

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
                SFTP_CHANNEL_POOL.put(host, channel);
                sftp = (ChannelSftp) channel;
                log.info("该channel是新连接的，ID为：" + channel.getId());
            } else {
                Channel channel = SFTP_CHANNEL_POOL.get(host);
                Session session = channel.getSession();
                if (!session.isConnected()) {
                    session.connect();
                }
                if (!channel.isConnected()) {
                    channel.connect();
                }
                sftp = (ChannelSftp) channel;
                log.info("该channel从map中获取的，ID为：" + channel.getId());
            }
        } catch (JSchException e) {
            e.printStackTrace();
            log.error("SFTP——JSch异常：" + e.getMessage());
        }
        return sftp;
    }

    /**
     * 连接sftp服务器 带私Key
     */
    public static ChannelSftp login(String username, String password, String host, int port, String privateKey) {
        ChannelSftp sftp = null;
        try {
            if (SFTP_CHANNEL_POOL.get(host) == null) {
                JSch jsch = new JSch();
                if (privateKey != null) {
                    // 设置私钥
                    jsch.addIdentity(privateKey);
                }
                Session session = jsch.getSession(username, host, port);

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
                SFTP_CHANNEL_POOL.put(host, channel);
                sftp = (ChannelSftp) channel;
                log.info("该channel是新连接的，ID为：" + channel.getId());
            } else {
                Channel channel = SFTP_CHANNEL_POOL.get(host);
                Session session = channel.getSession();
                if (!session.isConnected()) {
                    session.connect();
                }
                if (!channel.isConnected()) {
                    channel.connect();
                }
                sftp = (ChannelSftp) channel;
                log.info("该channel从map中获取的，ID为：" + channel.getId());
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return sftp;
    }

    public static void logoutList() {
        //遍历map中的键
        for (Channel channel : SFTP_CHANNEL_POOL.values()) {
            Session session = null;
            try {
                session = channel.getSession();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            ChannelSftp sftp = (ChannelSftp) channel;
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

        if (SSH_CHANNEL_POOL.size()>0){
            for (Channel channel : SSH_CHANNEL_POOL.values()) {
                Session session = null;
                try {
                    session = channel.getSession();
                } catch (JSchException e) {
                    e.printStackTrace();
                }
                ChannelExec ssh = (ChannelExec) channel;
                if (ssh != null) {
                    if (ssh.isConnected()) {
                        ssh.disconnect();
                    }
                }
                if (session != null) {
                    if (session.isConnected()) {
                        session.disconnect();
                    }
                }
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
    public static void upload(ChannelSftp sftp, String basePath, String directory, String sftpFileName, InputStream input) throws SftpException {
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
    public static void download(ChannelSftp sftp, String directory, String downloadFile, String saveFile) {
        if (directory != null && !"".equals(directory)) {
            try {
                sftp.cd(directory);
            } catch (SftpException e) {
                e.printStackTrace();
                log.error("SFTP操作异常：" + e.getMessage());
            }
        }
        File file = new File(saveFile);
        try {
            sftp.get(downloadFile, new FileOutputStream(file));
        } catch (SftpException e) {
            e.printStackTrace();
            log.error("SFTP操作异常：" + e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("文件异常：" + e.getMessage());
        }
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     */
    public static byte[] download(ChannelSftp sftp, String directory, String downloadFile) throws SftpException, IOException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);

        byte[] fileData = IOUtils.toByteArray(is);
        return fileData;
    }

    /**
     * test
     */
    public static synchronized void test(ChannelSftp sftp) throws SftpException {
        try {
            System.out.println(sftp.getSession().getHost());
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public static void delete(ChannelSftp sftp, String directory, String deleteFile)  {
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            log.error("SFTP操作异常：文件目录不存在！");
        }
        try {
            sftp.rm(deleteFile);
        } catch (SftpException e) {
            log.error("SFTP操作异常：删除文件失败！");
        }
    }


    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     */
    public static Vector<?> listFiles(ChannelSftp sftp, String directory) throws SftpException {
        try {
            return sftp.ls(directory);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            String[] dirs = directory.split("/");
            String tempPath = directory;
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
        return sftp.ls(directory);
    }


    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     */
    public static SftpATTRS listFiles1(ChannelSftp sftp, String directory) throws SftpException {
        try {
            return sftp.lstat(directory);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            String[] dirs = directory.split("/");
            String tempPath = directory;
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) {
                    continue;
                }
                tempPath += "/" + dir;
                try {
                    sftp.cd(tempPath);
                } catch (SftpException ex) {
                    log.error("文件不存在"+directory);
                    sftp.mkdir(tempPath);
                    sftp.cd(tempPath);
                }
            }
        }
        return sftp.lstat(directory);
    }


    /**
     * 连接ssh服务器
     */
    public static ChannelExec loginSsh(String username, String password, String host, int port) {
        ChannelExec ssh = null;
        try {
            if (SSH_CHANNEL_POOL.get(host) == null) {
                JSch jsch = new JSch();
                Session session = jsch.getSession(username, host, port);

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

                Channel channel = session.openChannel("exec");
                channel.connect();
                SSH_CHANNEL_POOL.put(host, channel);
                ssh = (ChannelExec) channel;
                log.info("该channel是新连接的，ID为：" + channel.getId());
            } else {
                Channel channel = SSH_CHANNEL_POOL.get(host);
                Session session = channel.getSession();
                if (!session.isConnected()) {
                    session.connect();
                }
                if (!channel.isConnected()) {
                    channel.connect();
                }
                ssh = (ChannelExec) channel;
                log.info("该channel从map中获取的，ID为：" + channel.getId());
            }
        } catch (JSchException e) {
            e.printStackTrace();
            log.error("SFTP——JSch异常：" + e.getMessage());
        }
        return ssh;
    }


    public void runCmd(ChannelExec ssh,String cmd, String charset) throws Exception
    {
        ssh.setCommand(cmd);
        ssh.setInputStream(null);
        ssh.setErrStream(System.err);
        ssh.connect();
        InputStream in = ssh.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName(charset)));
        String buf = null;
        while ((buf = reader.readLine()) != null)
        {
            System.out.println(buf);
        }
        reader.close();
    }


    //上传文件测试
    public static void main(String[] args) throws SftpException, IOException {
        //SftpUtil sftp = new SftpUtil("ftpuser", "Tdomc4@ftp", "10.212.111.185", 22);
        isProxy = "true";
        ProxyProtocol = 1;
        ProxyAddress = "10.77.18.254";
        ProxyPort = 40081;
        ProxyName = "linyile";
        ProxyPassword = "lyl$zj123";
        ChannelSftp sftp = login("ftpuser", "Tdomc4@ftp", "10.212.111.185", 22);
        boolean isOn = true;
        while (isOn) {
            try {
                log.info("大小：" + listFiles1(sftp, "/opt/oss/server/var/itf_n/ftpFile/ZJ/WZ/WX/HW/OMC4/PM/2019101810/" +
                        "PM-ENB-EUTRANCELLTDD-02-*-20191018100000-15.csv.gz").getSize());
            } catch (Exception e) {
                log.warn("暂无搜索到文件！");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logoutList();
    }
}