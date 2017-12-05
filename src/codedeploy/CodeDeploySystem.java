package codedeploy;

import codedeploy.bean.*;
import codedeploy.util.DBOperationUtil;
import codedeploy.util.FetchFileUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
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
    public static int releaseOrder(int id)
    {
        DBOperationUtil dbo=new DBOperationUtil();
        FetchFileUtil ffu=new FetchFileUtil();
        List<DeployOrder>orderlist=dbo.queryOrder(0);
        int i;
        for(i=0;i<orderlist.size();i++)
        {
            if(orderlist.get(i).isReleased()) {
                dbo.updateOrderisReleased(orderlist.get(i));//发布状态
                break;
            }
        }
        //id 发布没 /没 修改 修改发布状态、备份状态
        return Constants.SUCCESS;
    }
    public static int rollBackOrder(int id){
        DBOperationUtil dbo=new DBOperationUtil();
        FetchFileUtil ffu=new FetchFileUtil();
        List<DeployOrder>orderlist=dbo.queryOrder(0);//拿到所有order
        int i;
        for(i=0;i<orderlist.size();i++)
        {
            if(orderlist.get(i).isReleased()) {
                if(orderlist.get(i).getOno()==id)
                    break;
                else
                    return Constants.FAILURE;
            }
        }

        //判断order能不能回滚，如果最新的order的id不等于id则返回-1

        List<Integer> codeIDList=orderlist.get(i).getCodeIDList(); //拿到order对应的codeIDList
        List<Code> codeList=new ArrayList<>();
        for(i=0;i<codeIDList.size();i++) {
            codeList.addAll(dbo.queryCode(codeIDList.get(i),"", false, "", id));
            dbo.updateCodeisBackup(codeIDList.get(i));//修改数据库备份状态
        }//拿到order对应的codeList
        List<Host> hosts=orderlist.get(i).getTargetGroup().getHosts();
        //把备份直接传到订单对应的组下的所有主机
        for(i=0;i<codeList.size();i++)
        {
            Code code=codeList.get(i);
            for(int j=0;j<hosts.size();j++){
                //ffu.sendFile(codeList.get(i).getFilePath(),hosts.get(j),"1234",code.getFilePath()+code.getFilename());
            }
        }

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
