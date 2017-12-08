package codedeploy.bean;

public class ProductHost extends Host{
    private int GID;
    public ProductHost(){}
    public ProductHost(int id,String address,int GID)
    {
        super(id,address);
        this.GID=GID;
    }

    public int getGID() {
        return GID;
    }

    public void setGID(int LID) {
        this.GID = LID;
    }



    @Override
    public int getType() {
        return Constants.PRODUCTHOST;
    }
}
