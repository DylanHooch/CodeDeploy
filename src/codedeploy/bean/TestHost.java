package codedeploy.bean;

public class TestHost extends Host {
    public TestHost(){}
    public TestHost(int id,String address)
    {
        super(id,address);
    }
    public int getType()
    {
        return Constants.TESTHOST;
    }
}
