package codedeploy.bean;



public class LocalHost extends Host {
    private String user;
    public LocalHost(){super();}
    public LocalHost(String address)
    {
        super(0,address);
    }
    public LocalHost(int id,String address,String user)
    {
        super(id,address);
        this.user=user;
    }

    @Override
    public int getType() {
        return Constants.LOCALHOST;
    }
    public String getUser(){return user;}

    public void setUser(String user) {
        this.user = user;
    }
}
