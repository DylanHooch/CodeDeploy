package codedeploy;

import codedeploy.bean.Constants;
import codedeploy.bean.DeployOrder;
import codedeploy.bean.PHostGroup;
import codedeploy.bean.TestHost;

import java.util.List;

public class CodeDeploySystem {
    private int status;
    private int dataBaseStatus;
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
