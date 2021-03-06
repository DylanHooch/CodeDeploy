package codedeploy.action;

import codedeploy.CodeDeploySystem;
import codedeploy.bean.*;
import codedeploy.util.DBOperationUtil;
import codedeploy.util.FetchFileUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderAction extends ActionSupport{
    DeployOrder order;
    DBOperationUtil dbo;
    String basepath="/tmp/code";
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


        String[] filelist=filenames.split("[\r\n]+");
        System.out.println(filelist.length);
        List<String> pathlist=new ArrayList<>();
        for(int i=0;i<filelist.length;i++){
            pathlist.add(basepath+"/"+filelist[i]);
            System.out.println(filelist[i]);
        }
        Date date=new Date();
        List<Host> tHosts=dbo.queryHost(tid, Constants.TESTHOST);
        TestHost tHost=(TestHost)tHosts.get(0);
        PHostGroup pHosts=dbo.queryGroup(gid);
        if(pHosts==null)
            return ERROR;
        order=new DeployOrder(0,oname,date,tHost,pHosts,pathlist,false);
        dbo.insertOrder(order);
        order=dbo.queryOrderByName(oname).get(0);

        File dir = new File("temp\\");
        if (dir.exists()) {
            System.out.println(dir.getAbsolutePath());
        }
        //创建目录
        else {
            dir.mkdirs();
        }
        FetchFileUtil ffu=new FetchFileUtil();
        List<Code> codes=new ArrayList<>();
        for(String s:pathlist){
            ffu.getFile("temp\\",tHost,"22",s);
            String tmp[]=s.split("/");
            String codename=tmp[tmp.length-1];
            String md5=ffu.calcMD5("temp\\"+codename);
            codes.add(new Code(-1,s,false,md5,order.getOno()));
        }
        dbo.insertCode(codes);
        List<DeployOrder> orders=dbo.queryOrder(0);
        request.setAttribute("allorder",orders);
        return SUCCESS;
    }
    public String refreshOrder() throws Exception{
        dbo=new DBOperationUtil();
        HttpServletRequest request= ServletActionContext.getRequest();
        request.setCharacterEncoding("utf-8");
        List<DeployOrder> orders;

        orders=dbo.queryOrder(0);
        request.setAttribute("allorder",orders);

        return "refreshorder";
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
        List<DeployOrder> orders;
        if(name.equals("allorder")){
               orders=dbo.queryOrder(0);
        }
        else {
            orders = dbo.queryOrderByName(name);
        }
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
    public String release() throws Exception{
        dbo=new DBOperationUtil();
        HttpServletRequest request= ServletActionContext.getRequest();
        request.setCharacterEncoding("utf-8");
        int id =Integer.parseInt(request.getParameter("id"));
        CodeDeploySystem.releaseOrder(id);
        System.out.println("ReleaseAction2333333");
        List<DeployOrder> orderlist=dbo.queryOrder(0);
        request.setAttribute("allorder",orderlist);
        return "release";
    }
}

