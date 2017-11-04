package codedeploy.util;

import java.util.Properties;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SftpChannelUtil {
    private Session session = null;
    private Channel channel = null;


//返回一个Sftp通道类对象
    public ChannelSftp getChannel(int timeout,String ftpHost,String port,String ftpUserName,String ftpPassword) throws JSchException {

//        String ftpHost = "192.168.45.128";
//        String port = "22";
//        String ftpUserName = "root";
//        String ftpPassword = "hahahaha";

        int ftpPort = 22;//Default port
        if (port != null && !port.equals("")) {
            ftpPort = Integer.valueOf(port);
        }

        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象

        if (ftpPassword != null) {
            session.setPassword(ftpPassword); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        System.out.println("Session connected.");

        System.out.println("Opening Channel.");
        channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        System.out.println("Connected successfully to ftpHost = " + ftpHost + ",as ftpUserName = " + ftpUserName
                + ", returning: " + channel);
        return (ChannelSftp) channel;
    }

    public void closeChannel() throws Exception {
        if (channel != null) {
            channel.disconnect();
            channel=null;
        }
        if (session != null) {
            session.disconnect();
            session=null;
        }
    }

}