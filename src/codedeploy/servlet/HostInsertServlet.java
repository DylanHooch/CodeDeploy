package codedeploy.servlet;

import codedeploy.bean.*;
import codedeploy.util.DBOperationUtil;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Niko on 2017/11/28.
 */
public class HostInsertServlet extends javax.servlet.http.HttpServlet {

    DBOperationUtil dbo=new DBOperationUtil();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        if (request.getParameter("type")!=null) {
            boolean flg=false;
            int type = Integer.parseInt(request.getParameter("type"));
            String address = request.getParameter("Address");
            int id = Integer.parseInt(request.getParameter("id"));
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();

            if(type== Constants.TESTHOST){
                PHostGroup phg=new PHostGroup(-1,address,id);
                dbo.insertGroup(phg);
                out.print("数据增加成功");
            }
          else {
                String ip[]=address.split("\\.");
                int ipnum[]=new int[4];
                if(ip.length!=4)
                {
                    out.print("增加失败,请确认输入ip地址是否合法");
                    return;
                }
                else
                {
                    for(int i=0;i<4;i++)
                        ipnum[i]=Integer.parseInt(ip[i]);
                    if(!(ipnum[0]<256&&ipnum[0]>-1&&ipnum[1]<256&&ipnum[1]>-1&&ipnum[2]<256&&ipnum[2]>-1&&ipnum[3]<256&&ipnum[3]>-1))
                    {
                        out.print("增加失败,请确认输入ip地址是否合法");
                        return;
                    }

                }
                //insert
                Host inshost = new TestHost(id, address);
                switch (type) {
//                case Constants.LOCALHOST:inshost=new LocalHost(id,address,"admin");break;
                    case Constants.LOCALHOST:
                        inshost = new TestHost(-1, address);
                        break;//不允许插入test主机
                    case Constants.PHOSTGROUP:
                        inshost = new ProductHost(-1, address, id);
                        break;
                    default:
                        flg = true;
                        break;
                }
                if (!flg)
                    dbo.insertHost(inshost);
                out.print("数据增加成功");
            }
        }


    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request,response);
    }
}
