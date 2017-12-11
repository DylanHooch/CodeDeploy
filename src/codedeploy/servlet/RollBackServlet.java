package codedeploy.servlet;

import codedeploy.CodeDeploySystem;
import codedeploy.util.DBOperationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "RollBackServlet")
public class RollBackServlet extends HttpServlet {
    DBOperationUtil dbo=new DBOperationUtil();
    CodeDeploySystem cds=new CodeDeploySystem();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        cds.rollBackOrder(id);
        System.out.println("Rollback");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
