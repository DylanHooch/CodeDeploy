package codedeploy.util;

import codedeploy.bean.DeployOrder;
import codedeploy.bean.Constants;

public class OrderUtil {
    public int createOrder(DeployOrder order)
    {
        //check if codes exist
        //get code files' MD5
        //insert codes information
        //insert order information
        return Constants.SUCCESS;
    }
    public int handleOrder(int orderID)
    {
        //query for order information(SQL)
        //query for codes
        //connect hosts
        //fetch codes from test host
        //insert
        //backup(fetch codes of same names from production host)
        //insert backup code information(SQL)
        //send codes to production host
        return Constants.SUCCESS;
    }
}
