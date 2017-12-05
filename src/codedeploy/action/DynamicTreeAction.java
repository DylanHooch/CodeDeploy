package codedeploy.action;

import codedeploy.CodeDeploySystem;
import codedeploy.bean.*;
import codedeploy.util.DBOperationUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DynamicTreeAction extends ActionSupport{
    private static final long serialVersionUID = 1L;

    public String execute() throws Exception {
        HttpServletRequest request= ServletActionContext.getRequest();
        HttpServletResponse response=ServletActionContext.getResponse();
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/xml;charset=utf-8");
        PrintWriter out = response.getWriter();
        ArrayList<Node> list= getNodeInfo();
        if(list!=null&&list.size()>0){
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<nodes>");
            for(int i=0;i<list.size();i++){
                Node node = list.get(i);
                out.println("<node nodeId='"+node.getNodeId()+"' parentId='"+node.getParentId()+"' hrefAddress='"+node.getHrefAddress()+"'>"+node.getNodeName()+"' hostID='+"+"</node>");
            }
            out.println("</nodes>");
        }
        out.flush();
        out.close();
        return NONE;
    }

    public ArrayList<Node> getNodeInfo()
    {    ArrayList<Node> nodes =new ArrayList<>();
        DBOperationUtil dbo=new DBOperationUtil();
        List<PHostGroup> groups=dbo.queryGroup();
        //根节点：本地主机
        nodes.add(new Node(123,0,-1,"", CodeDeploySystem.localAddress));
        //第一层子节点：测试主机
        List<Host> testHosts=dbo.queryHost(-1, Constants.TESTHOST);
        TestHost testHost;
        for(int i=0;i<testHosts.size();i++)
        {
            testHost = (TestHost) testHosts.get(i);
            nodes.add(new Node(testHost.getId(),i,0,"",testHost.getAddress()));
            int tid=testHost.getId();
            int k=0;
            for(int j=0;j<groups.size();j++)
            {
                if(groups.get(j).getTID()==tid)
                {
                    //第二层子节点：生产主机组
                    int nodeId_group=100+(k++);//加入偏移量testHosts.size()保证id不重复
                    nodes.add(new Node(groups.get(j).getId(),nodeId_group,i,"",groups.get(j).getName()));
                    List<Host> pHosts=groups.get(j).getHosts();
                    for(int p=0;p<pHosts.size();p++)
                    {
                        //第三层子节点：生产主机
                        nodes.add(new Node(pHosts.get(p).getId(),200+p,nodeId_group,"",pHosts.get(p).getAddress()));
                    }
                }
            }
        }
        return nodes;

//        hosts.add(new ProductHost(3,"222.201",1));
//        hosts.add(new ProductHost(4,"222.202",1));
//        grouplist.add(new PHostGroup(hosts,1,"222.200",1));
//        hosts= new ArrayList<>();
//        hosts.add(new ProductHost(5,"222.301",2));
//        hosts.add(new ProductHost(6,"222.302",2));
//        grouplist.add(new PHostGroup(hosts,2,"222.300",2));
//        nodes.add(new Node(0,-1,"","222.222"));
//        for(int i=0;i<grouplist.size();i++)
//        {
//            hosts=grouplist.get(i).getHosts();
//            nodes.add(new Node(i+1,0,"","222.213"));
//            for(int j=0;j<hosts.size();j++)
//                nodes.add(new Node(hosts.get(j).getId(),grouplist.get(i).getId(),"",hosts.get(j).getAddress()));
//
//        }
//        return nodes;
    }

}
