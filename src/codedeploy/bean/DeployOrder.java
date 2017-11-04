package codedeploy.bean;


import java.util.Date;
import java.util.List;

public class DeployOrder {
    private int ono;
    private Date date;
    private TestHost targetTHost;
    private PHostGroup targetGroup;
    private List<String> codePathList;
    private List<Integer> codeIDList;
    private boolean isReleased;
    public DeployOrder(){}
    public DeployOrder(int ono, Date date, TestHost targetTHost, PHostGroup pHostGroup,List<String> codePathList,boolean isReleased)
    {
        this.ono=ono;
        this.date=date;
        this.targetTHost=targetTHost;
        this.targetGroup=pHostGroup;
        this.codePathList=codePathList;
        this.isReleased=isReleased;
    }
    public DeployOrder(int ono, Date date, TestHost targetTHost, PHostGroup pHostGroup,List<String> codePathList,List<Integer> codeIDList,boolean isReleased)
    {
        this.ono=ono;
        this.codeIDList=codeIDList;
        this.date=date;
        this.targetTHost=targetTHost;
        this.targetGroup=pHostGroup;
        this.codePathList=codePathList;
        this.isReleased=isReleased;
    }
    public void setId(int ono)
    {
        this.ono=ono;
    }
    public void setDate(Date date)
    {
        this.date=date;
    }
    public void setTargetTHost(TestHost targetTHost)
    {
        this.targetTHost=targetTHost;
    }
    public void setTargetGroup(PHostGroup targetGroup)
    {
        this.targetTHost=targetTHost;
    }
    public void setCodeIDList(List<Integer> codeIDList) {
        this.codeIDList = codeIDList;
    }
    public int getOno(){return ono;}

    public Date getDate() {
        return date;
    }

    public Host getTargetTHost() {
        return targetTHost;
    }

    public List<Integer> getCodeIDList() {
        return codeIDList;
    }

    public PHostGroup getTargetGroup() {
        return targetGroup;
    }

    public void setOno(int ono) {
        this.ono = ono;
    }

    public List<String> getCodePathList() {
        return codePathList;
    }

    public void setCodePathList(List<String> codePathList) {
        this.codePathList = codePathList;
    }

    public boolean isReleased() {
        return isReleased;
    }

    public void setReleased(boolean released) {
        isReleased = released;
    }
}
