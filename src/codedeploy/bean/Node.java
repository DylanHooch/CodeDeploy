package codedeploy.bean;

public class Node  {
    private int id;
    private int nodeId;
    private int  parentId;
    private String hrefAddress;
    private String nodeName;

    public Node(int  nodeId,int  parentId,String hrefAddress,String nodeName)
    {

        this.nodeId = nodeId;
        this.parentId = parentId;
        this.hrefAddress = hrefAddress;
        this.nodeName = nodeName;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int  getNodeId() {
        return nodeId;
    }

    public void setNodeId(int  nodeId) {
        this.nodeId = nodeId;
    }

    public int  getParentId() {
        return parentId;
    }

    public void setParentId(int  parentId) {
        this.parentId = parentId;
    }

    public String getHrefAddress() {
        return hrefAddress;
    }

    public void setHrefAddress(String hrefAddress) {
        this.hrefAddress = hrefAddress;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

}