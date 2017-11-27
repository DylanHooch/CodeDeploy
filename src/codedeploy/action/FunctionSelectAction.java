package codedeploy.action;

import codedeploy.bean.Constants;
import codedeploy.bean.DeployOrder;
import codedeploy.bean.LocalHost;
import codedeploy.bean.PHostGroup;
import codedeploy.util.DBOperationUtil;
import com.opensymphony.xwork2.ActionSupport;

import java.util.List;

public class FunctionSelectAction extends ActionSupport{
    LocalHost localhost;
    List<PHostGroup> groups;
    List<DeployOrder> recentOrders;

    public LocalHost getLocalhost() {
        return localhost;
    }

    public void setLocalhost(LocalHost localhost) {
        this.localhost = localhost;
    }

    public List<PHostGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<PHostGroup> groups) {
        this.groups = groups;
    }

    @Override
    public String execute() throws Exception {
        DBOperationUtil dbo=new DBOperationUtil();
        localhost=(LocalHost)dbo.queryHost(-1, Constants.LOCALHOST);
        groups=dbo.queryGroup();
        recentOrders=dbo.queryOrder(10);
        return SUCCESS;
    }

    public String viewOrders() throws Exception{
        return SUCCESS;
    }

    public String createOrders() throws Exception{
        return SUCCESS;
    }

    public String manageHost() throws Exception{
        return SUCCESS;
    }

    public String rollBack() throws Exception{
        return SUCCESS;
    }

}
