package org.zayl.jireh.tool.datamodify.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author Jireh
 */
public class JDBCSSHChannel {
    /**
     *
     * @param localPort local host recommends mysql 3306 redis 6379
     * @param sshHost   ssh host
     * @param sshPort   ssh port
     * @param sshUserName ssh username
     * @param sshPassWord ssh password
     * @param remotoHost remote machine address
     * @param remotoPort remote machine port
     */
    public static void goSSH(int localPort, String sshHost, int sshPort,
                             String sshUserName, String sshPassWord,
                             String remotoHost, int remotoPort) {
        try {
            JSch jsch = new JSch();
            //Log in to the springboard
            Session session = jsch.getSession(sshUserName, sshHost, sshPort);
            session.setPassword(sshPassWord);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            //Connect to mysql machine through ssh
            session.setPortForwardingL(localPort, remotoHost, remotoPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}