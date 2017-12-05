package codedeploy.servlet;
import codedeploy.bean.Node;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import codedeploy.util.DBOperationUtil;
import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet(name = "HostNodeServlet")
public class HostNodeServlet extends HttpServlet {
    DBOperationUtil dbo=new DBOperationUtil();

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/xml;charset=utf-8");
        PrintWriter out = response.getWriter();
        List<Node> list = dbo.queryAllHostNode();
        if (list != null && list.size() > 0) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<nodes>");
            for (int i = 0; i < list.size(); i++) {
                Node node = list.get(i);
                out.println("<node nodeId='" + node.getNodeId() + "' parentId='" + node.getParentId() + "' hostID='" + node.getId() + "' hrefAddress='" + node.getHrefAddress() + "'>" + node.getNodeName() + "</node>");
            }
            out.println("</nodes>");
        }
        out.flush();
        out.close();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
