package codedeploy.action;
import com.opensymphony.xwork2.ActionSupport;
public class LoginAction extends ActionSupport{
    private String username;
    private String password;
    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username){this.username=username;}
    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}
    public String checkLogin()
    {
        if(this.username.equals("admin")&&this.password.equals("acc"))
            return SUCCESS;
        else
            return LOGIN;
    }

}
