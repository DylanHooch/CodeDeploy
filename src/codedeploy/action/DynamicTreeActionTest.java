package codedeploy.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import codedeploy.bean.Host;
import codedeploy.bean.Node;
import codedeploy.bean.PHostGroup;
import codedeploy.bean.ProductHost;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.deploy.net.HttpRequest;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DynamicTreeActionTest extends ActionSupport {
    private static final long serialVersionUID = 1L;

    public String execute() throws Exception {
        HttpServletRequest request=ServletActionContext.getRequest();
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
                out.println("<node nodeId='"+node.getNodeId()+"' parentId='"+node.getParentId()+"' hrefAddress='"+node.getHrefAddress()+"'>"+node.getNodeName()+"</node>");
            }
            out.println("</nodes>");
        }
        out.flush();
        out.close();
        return NONE;
    }

    public ArrayList<Node> getNodeInfo()
    {    ArrayList<Node> nodes =new ArrayList<>();
        //查询全部主机列表 返回grouplist 这里暂时写测试数据
        List<PHostGroup> grouplist=new ArrayList<>();
        List<Host> hosts= new ArrayList<>();
        hosts.add(new ProductHost(3,"222.201",1));
        hosts.add(new ProductHost(4,"222.202",1));
        grouplist.add(new PHostGroup(hosts,1,"222.200",1));
        hosts= new ArrayList<>();
        hosts.add(new ProductHost(5,"222.301",2));
        hosts.add(new ProductHost(6,"222.302",2));
        grouplist.add(new PHostGroup(hosts,2,"222.300",2));
        nodes.add(new Node(123,0,-1,"","222.222"));
        for(int i=0;i<grouplist.size();i++)
        {
            hosts=grouplist.get(i).getHosts();
            nodes.add(new Node(123,i+1,0,"","222.213"));
            for(int j=0;j<hosts.size();j++) {
                nodes.add(new Node(123,hosts.get(j).getId(), grouplist.get(i).getId(), "", hosts.get(j).getAddress()));
            }
        }
        return nodes;
    }

}