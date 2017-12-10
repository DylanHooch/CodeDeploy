package codedeploy.action;

import codedeploy.bean.*;
import codedeploy.util.DBOperationUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderAction extends ActionSupport{
    DeployOrder order;
    DBOperationUtil dbo;
    String basepath="/code";
    int tid;
    String oname;
    String filenames;
    int gid;
    int ono;

    public int getTid() {
        return tid;
    }
    public void setTid(int tid){
        this.tid=tid;
    }

    public DeployOrder getOrder() {
        return order;
    }

    public void setOrder(DeployOrder order) {
        this.order = order;
    }

    public int getGid() {
        return gid;
    }

    public String getFilenames() {
        return filenames;
    }

    public String getOname() {
        return oname;
    }

    public void setFilenames(String filenames) {
        this.filenames = filenames;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    @Override
    public String execute() throws Exception {
        dbo=new DBOperationUtil();
        Map request= (Map)ActionContext.getContext().get("request");
        request.put("allorder",dbo.queryOrder(0));
        return SUCCESS;
    }
    //request --> tid,oname,filenames,gid
    //request <-- all orders
    public String create() throws Exception
    {
        dbo=new DBOperationUtil();
        HttpServletRequest request= ServletActionContext.getRequest();
        request.setCharacterEncoding("utf-8");

//        String oname=request.getParameter("oname");
//        String filenames=request.getParameter("filenames");
//        int tid = Integer.parseInt(request.getParameter("tid"));
//        int gid = Integer.parseInt(request.getParameter("gid"));


        String[] filelist=filenames.split("\n+");
        List<String> pathlist=new ArrayList<>();
        for(int i=0;i<filelist.length;i++){
            pathlist.add(basepath+"/"+filelist[i]);
        }
        Date date=new Date();
        List<Host> tHosts=dbo.queryHost(tid, Constants.TESTHOST);
        TestHost tHost=(TestHost)tHosts.get(0);
        PHostGroup pHosts=dbo.queryGroup(gid);
        if(pHosts==null)
            return ERROR;
        order=new DeployOrder(0,oname,date,tHost,pHosts,pathlist,false);
        dbo.insertOrder(order);

        List<DeployOrder> orders=dbo.queryOrder(0);
        request.setAttribute("allorder",orders);
        return SUCCESS;
    }
    //request --> ono
    //request <-- 删除后的 all orders
    public String delete() throws Exception{
        dbo=new DBOperationUtil();
        HttpServletRequest request= ServletActionContext.getRequest();
        request.setCharacterEncoding("utf-8");
        int ono=Integer.parseInt(request.getParameter("id"));
        dbo.deleteOrder(ono);
        List<DeployOrder> orders=dbo.queryOrder(0);
        request.setAttribute("allorder",orders);
        return SUCCESS;
    }

    //request --> name
    //request <-- 查询到的orders
    public String select() throws Exception{
        dbo=new DBOperationUtil();
        HttpServletRequest request= ServletActionContext.getRequest();
        request.setCharacterEncoding("utf-8");
        String name=request.getParameter("order_name");
        List<DeployOrder> orders=dbo.queryOrderByName(name);
        request.setAttribute("allorder",orders);

        return SUCCESS;
    }
    //TODO: 2017.12.06 檢查要不要在最后put tid
    public String refresh() throws Exception{
        dbo=new DBOperationUtil();
        Map request=(Map)ActionContext.getContext().get("request");
        Map session=(Map)ActionContext.getContext().getSession();
        int tid=(Integer)request.get("tid");

        //直接从会话中获取groupList，若过期则重新查询
        List<PHostGroup> groupList=(List<PHostGroup>)session.get("groupList");
        if(groupList==null)
        {
            groupList=dbo.queryGroup(false);
            session.put("groupList",groupList);
        }
        List<Host> hostList=(List<Host>)session.get("hostList");
        if(hostList==null)
        {
            hostList=dbo.queryHost(-1,Constants.TESTHOST);
            session.put("hostList",hostList);
        }
        int i;
        List<PHostGroup> groupListToShow=new ArrayList<>();
        groupListToShow.addAll(groupList);
        for(i=0;i<groupListToShow.size();)
        {
            if(groupListToShow.get(i).getTID()!=tid)
                groupListToShow.remove(i);
            else i++;
        }
        request.put("groupListToShow",groupListToShow);

        //request.put("tid",tid);
        return "refresh";
    }
}

