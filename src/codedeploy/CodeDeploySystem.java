package codedeploy;

import codedeploy.bean.Constants;
import codedeploy.bean.DeployOrder;
import codedeploy.bean.PHostGroup;
import codedeploy.bean.TestHost;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

public class CodeDeploySystem {
    private int status;
    private int dataBaseStatus;
    public static String localAddress;
    static{
        //获取本机的IP
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                System.out.println(netInterface.getName());
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        localAddress=ip.getHostAddress();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static int createOrder(DeployOrder order)
    {
        return Constants.SUCCESS;
    }
    public static int rollback()
    {
        return Constants.SUCCESS;
    }
    public static int releaseOrder(DeployOrder order)
    {
        return Constants.SUCCESS;
    }
    public static int createRelation(List<TestHost> tHostList, List<PHostGroup> groupList)
    {
        return Constants.SUCCESS;
    }
    public static int updateRelation(List<TestHost> tHostList,List<PHostGroup> groupList)
    {
        return Constants.SUCCESS;
    }
    public List<DeployOrder> getOrder(int days)
    {
        return null;
    }

}
