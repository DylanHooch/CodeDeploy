package codedeploy;

import codedeploy.bean.Constants;
import codedeploy.bean.DeployOrder;
import codedeploy.bean.PHostGroup;
import codedeploy.bean.TestHost;

import java.util.List;

public class CodeDeploySystem {
    private int status;
    private int dataBaseStatus;
    public int CreateOrder(DeployOrder order)
    {
        return Constants.SUCCESS;
    }
    public int Rollback()
    {
        return Constants.SUCCESS;
    }
    public int ReleaseOrder(DeployOrder order)
    {
        return Constants.SUCCESS;
    }
    public int createRelation(List<TestHost> tHostList, List<PHostGroup> groupList)
    {
        return Constants.SUCCESS;
    }
    public int updateRelation(List<TestHost> tHostList,List<PHostGroup> groupList)
    {
        return Constants.SUCCESS;
    }
    public List<DeployOrder> getOrder(int days)
    {
        return null;
    }

}
