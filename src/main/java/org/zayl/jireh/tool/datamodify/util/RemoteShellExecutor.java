package org.zayl.jireh.tool.datamodify.util;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.zayl.jireh.tool.datamodify.util.Const.SSH_CONNECTION_POOL;

/**
 * @author Jireh
 */
public class RemoteShellExecutor {

    private static Logger log = Logger.getLogger(RemoteShellExecutor.class);

    private Connection conn;
    /**
     * 远程机器IP
     */
    private String ip;
    /**
     * 用户名
     */
    private String osUsername;
    /**
     * 密码
     */
    private String password;
    private static String charset = Charset.defaultCharset().toString();

    private static final int TIME_OUT = 1000 * 5 * 60;

    public RemoteShellExecutor(String ip, String usr, String pasword) {
        this.ip = ip;
        this.osUsername = usr;
        this.password = pasword;
    }

    /**
     * 登录
     *
     * @return
     * @throws IOException
     */
    public static Connection login(String ip, String usr, String password) throws IOException {
        if (SSH_CONNECTION_POOL.get(ip) == null) {
            Connection conn = new Connection(ip);
            conn.connect();
            if (conn.authenticateWithPassword(usr, password)) {
                SSH_CONNECTION_POOL.put(ip, conn);
                return conn;
            }
        } else {
            return SSH_CONNECTION_POOL.get(ip);
        }
        return null;
    }


    /**
     * 登录
     *
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(osUsername, password);
    }

    /**
     * 执行脚本
     *
     * @param cmds
     * @return
     * @throws Exception
     */
    public int exec(String cmds) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int ret = -1;
        try {
            if (login()) {
                // Open a new {@link Session} on this connection
                Session session = conn.openSession();
                // Execute a command on the remote machine.
                session.execCommand(cmds);
                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, charset);

                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);

                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

                System.out.println("outStr=" + outStr);
                System.out.println("outErr=" + outErr);

                ret = session.getExitStatus();
            } else {
                // 自定义异常类 实现略
                throw new Exception("登录远程机器失败" + ip);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    /**
     * 执行脚本
     *
     * @param cmds
     * @return
     * @throws Exception
     */
    public static int exec(Connection conn, String cmds) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int ret = -1;
        try {
            if (conn != null) {
                // Open a new {@link Session} on this connection
                Session session = conn.openSession();
                // Execute a command on the remote machine.
                session.execCommand(cmds);
                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, charset);

                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);

                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

                log.info("outStr=" + outStr);
                log.info("outErr=" + outErr);

                ret = session.getExitStatus();
            } else {
                // 自定义异常类 实现略
                log.error("登录远程机器失败");
                throw new Exception("登录远程机器失败");
            }
        } finally {
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    private static String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }

    public static void close() {
        for (Connection connection : SSH_CONNECTION_POOL.values()) {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void main(String args[]) throws Exception {
        RemoteShellExecutor executor = new RemoteShellExecutor("192.168.100.4", "root", "Jireh8140.");
        // 执行myTest.sh 参数为java Know dummy
        System.out.println(executor.exec("ls -l"));
    }
}
