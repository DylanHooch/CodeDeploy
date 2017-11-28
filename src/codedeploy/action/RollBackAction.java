package codedeploy.action;

import codedeploy.CodeDeploySystem;
import codedeploy.bean.DeployOrder;
import com.opensymphony.xwork2.ActionSupport;

public class RollBackAction extends ActionSupport{

    @Override
    public String execute() throws Exception {
        CodeDeploySystem.rollback();
        return SUCCESS;
    }
}
