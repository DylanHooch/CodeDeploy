package codedeploy.action;

import codedeploy.bean.*;
import codedeploy.util.DBOperationUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.util.List;
import java.util.Map;

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
        groups=dbo.queryGroup(false);
        recentOrders=dbo.queryOrder(10);
        return SUCCESS;
    }

    public String manageOrder() throws Exception{
        DBOperationUtil dbo=new DBOperationUtil();
        Map request=(Map) ActionContext.getContext().get("request");
        Map session=(Map) ActionContext.getContext().getSession();
        List<Host> testHosts=dbo.queryHost(-1,Constants.TESTHOST);
        request.put("allorder",dbo.queryOrder(0));
        session.put("hostList",testHosts);
        List<PHostGroup> groupList=dbo.queryGroup(false);
        session.put("groupList",groupList);
        request.put("tid",testHosts.get(0).getId());

        return "manageorder";
    }

    public String manageHost() throws Exception{
        DBOperationUtil dbo=new DBOperationUtil();
        Map request=(Map) ActionContext.getContext().get("request");
        request.put("","");
        return "managehost";
    }


}
