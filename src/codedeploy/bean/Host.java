package codedeploy.bean;

public abstract class Host {
    private int id;
    private String address;
    public Host(){}
    public Host(int id,String address)
    {
        this.id=id;
        this.address=address;
    }
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getAddress(){return address;}
    public void setAddress(String address){this.address=address;}
    public abstract int getType();

}
