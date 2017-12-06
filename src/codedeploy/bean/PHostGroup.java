package codedeploy.bean;

import java.util.List;

//生产主机组
public class PHostGroup {
    private List<Host> hosts;
    private int id;
    private String name;
    private int TID;
    public PHostGroup(){}
    public PHostGroup(List<Host> hosts,int id,String name,int TID)
    {
        this.hosts=hosts;
        this.id=id;
        this.name=name;
        this.TID=TID;
    }
    public PHostGroup(int id,String name,int TID)
    {
        this.id=id;
        this.name=name;
        this.TID=TID;
    }
    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getTID() {
        return TID;
    }

    public void setTID(int TID) {
        this.TID = TID;
    }
}
