package codedeploy.servlet;

import codedeploy.CodeDeploySystem;
import codedeploy.bean.DeployOrder;
import codedeploy.util.DBOperationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "ReleaseServlet")
public class ReleaseServlet extends HttpServlet {
    DBOperationUtil dbo=new DBOperationUtil();
    CodeDeploySystem cds=new CodeDeploySystem();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        cds.releaseOrder(id);
        System.out.println("ReleaseServlet2333333");
        List<DeployOrder> orderlist=dbo.queryOrder(0);
        request.setAttribute("allorder",orderlist);
       // request.getRequestDispatcher("orderChart.jsp").forward(request, response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
