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
    private static String localAddress;
    private static int lid;
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
        DBOperationUtil dbo=new DBOperationUtil();
        dbo.insertHost(new LocalHost(localAddress));
    }
    public static int getLid(){return lid;}
    public static void setLid(int lid){CodeDeploySystem.lid=lid;}
    public static String getLocalAddress(){return localAddress;}
    public static void setLocalAddress(String address){localAddress=address;}
    public int createOrder(DeployOrder order)
    {
        FetchFileUtil ffu=new FetchFileUtil();
        List<String> fpaths=order.getCodePathList();
        Host thost=order.getTargetTHost();
        for(int i=0;i<fpaths.size();i++){
            String filepath=fpaths.get(i);
            int ret=ffu.getFile("~//Document//temp",thost,"",filepath);

            if(ret==Constants.FAILURE){
                return Constants.FAILURE;
            }
        }
        DBOperationUtil dbo=new DBOperationUtil();
        dbo.insertOrder(order);
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
            if(orderlist.get(i).isReleased()==false&&orderlist.get(i).getOno()==id) {
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
        List<DeployOrder> orderlist=dbo.queryOrderByID(id);//拿到所有order
//        List<DeployOrder>orderlist=dbo.queryOrder(0);
        int i;
//        for(i=0;i<orderlist.size();i++)
//        {
//            if(orderlist.get(i).isReleased()) {
//                if(orderlist.get(i).getOno()==id)
//                    break;
//                else
//                    return Constants.FAILURE;
//            }
//        }
//
//        判断order能不能回滚，如果最新的order的id不等于id则返回-1
       // List<Integer> codeIDList=new ArrayList<>();
        List<Code> codeList=new ArrayList<>();
        for(i=0;i<orderlist.size();i++) {
           int orderID = orderlist.get(i).getOno(); //拿到order对应的codeIDList
            List<Code> tempCodelist =new ArrayList<>();
                    tempCodelist= dbo.queryCode(-1,"",null,"",orderID); //拿到order对应的codeIDList
            codeList.addAll(tempCodelist);
        }

        for(i=0;i<codeList.size();i++) {
            //codeList.addAll(dbo.queryCode(codeIDList.get(i),"", null, "", id));
            dbo.updateCodeisNotBackup(codeList.get(i).getCno());//修改数据库备份状态
        }//拿到order对应的codeList
//        List<Host> hosts=orderlist.get(i).getTargetGroup().getHosts();
//        //把备份直接传到订单对应的组下的所有主机
//        for(i=0;i<codeList.size();i++)
//        {
//            Code code=codeList.get(i);
//            for(int j=0;j<hosts.size();j++){
//                //ffu.sendFile(codeList.get(i).getFilePath(),hosts.get(j),"1234",code.getFilePath()+code.getFilename());
//            }
//        }

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
