package codedeploy.util;

import codedeploy.bean.Constants;
import codedeploy.bean.Host;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FetchFileUtil {
    private int timeout=1000;//ms
    private String username="root";
    private String password="123456";
    public int getFile(String dstFolderPath, Host srcHost,String port,String filePath)
    {
        SftpChannelUtil channelUtil=new SftpChannelUtil();
        try{
            ChannelSftp channelSftp=channelUtil.getChannel(timeout,srcHost.getAddress(),port,username,password);
            System.out.println(filePath);
            channelSftp.get(filePath,dstFolderPath);
            channelUtil.closeChannel();
            return Constants.SUCCESS;
        }catch (SftpException e)
        {
            e.printStackTrace();
            try {
                channelUtil.closeChannel();
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
            return Constants.FAILURE;
        }catch (Exception e) {
            e.printStackTrace();
            try {
                channelUtil.closeChannel();
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
            return Constants.FAILURE;
        }
    }
    public int getFiles(String dstFolderPath,Host srcHost,String port,List<String> filePaths)
    {
        SftpChannelUtil channelUtil=new SftpChannelUtil();
        try{
            ChannelSftp channelSftp=channelUtil.getChannel(timeout,srcHost.getAddress(),port,username,password);
            for(int i=0;i<filePaths.size();i++)
            {
                channelSftp.get(filePaths.get(i),dstFolderPath);
            }
            channelUtil.closeChannel();
            return Constants.SUCCESS;
        }catch (SftpException e)
        {
            e.printStackTrace();
            try {
                channelUtil.closeChannel();
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
            return Constants.FAILURE;
        }catch (Exception e)
        {
            e.printStackTrace();
            try {
                channelUtil.closeChannel();
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
            return Constants.FAILURE;
        }
    }
    public int sendFile(String dstFolderPath,Host dstHost,String port,String filePath)
    {
        SftpChannelUtil channelUtil=new SftpChannelUtil();
        try{
            ChannelSftp channelSftp=channelUtil.getChannel(timeout,dstHost.getAddress(),port,username,password);
            channelSftp.put(filePath,dstFolderPath,ChannelSftp.OVERWRITE);
            channelUtil.closeChannel();
            return Constants.SUCCESS;
        }catch (SftpException e)
        {
            e.printStackTrace();
            try {
                channelUtil.closeChannel();
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
            return Constants.FAILURE;
        }catch (Exception e)
        {
            e.printStackTrace();
            try {
                channelUtil.closeChannel();
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
            return Constants.FAILURE;
        }
    }
    public int sendFiles(String dstFolderPath,Host dstHost,String port,List<String> filePaths)
    {
        SftpChannelUtil channelUtil=new SftpChannelUtil();
        try{
            ChannelSftp channelSftp=channelUtil.getChannel(timeout,dstHost.getAddress(),port,username,password);
            for(int i=0;i<filePaths.size();i++)
                channelSftp.put(filePaths.get(i),dstFolderPath,ChannelSftp.OVERWRITE);
            channelUtil.closeChannel();
            return Constants.SUCCESS;
        }catch (SftpException e)
        {
            e.printStackTrace();
            try {
                channelUtil.closeChannel();
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
            return Constants.FAILURE;
        }catch (Exception e)
        {
            e.printStackTrace();
            try {
                channelUtil.closeChannel();
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
            return Constants.FAILURE;
        }
    }
    public String calcMD5(String filePath)
    {
        DigestUtils digestUtils=new DigestUtils();
        File file=new File(filePath);
        try {
            FileInputStream data = new FileInputStream(file);
            return digestUtils.md5Hex(data);
        }catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
