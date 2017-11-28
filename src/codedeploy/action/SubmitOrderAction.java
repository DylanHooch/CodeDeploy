package codedeploy.action;

import codedeploy.CodeDeploySystem;
import codedeploy.bean.DeployOrder;
import codedeploy.util.OrderUtil;
import com.opensymphony.xwork2.ActionSupport;

public class SubmitOrderAction extends ActionSupport{
    DeployOrder order;

    public DeployOrder getOrder() {
        return order;
    }

    public void setOrder(DeployOrder order) {
        this.order = order;
    }

    @Override
    public String execute() throws Exception {
        CodeDeploySystem.createOrder(order);
        return SUCCESS;
    }
}
