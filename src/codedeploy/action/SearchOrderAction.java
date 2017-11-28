package codedeploy.action;

import codedeploy.CodeDeploySystem;
import codedeploy.util.DBOperationUtil;
import com.opensymphony.xwork2.ActionSupport;

public class SearchOrderAction extends ActionSupport {
    String type;//"redirect"代表需要返回新页面，"update"代表需要返回一个表单
    int ID;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String execute() throws Exception {
        if(type=="redirect")
        {
            DBOperationUtil dbo=new DBOperationUtil();
            //dbo.queryOrder();
            return "redirect";
        }
        else if(type=="update")
        {

            return "update";
        }
        return ERROR;
    }
}
