package codedeploy.util;


import codedeploy.bean.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;


import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBOperationUtil {
    private static Connection connection=null;
    private static Connection connectDB()
    {
        if(connection==null) {
            String driverName = "com.mysql.jdbc.Driver";
            String dbURL = "jdbc:mysql://rm-wz9dxgtjy3jzb6qdlo.mysql.rds.aliyuncs.com:3306";
            String sqltype = "mysql";
            try {

                Class.forName(driverName);
                Connection dbConn = DriverManager.getConnection(dbURL, "qgg", "123456");
                return dbConn;

            } catch (Exception e)

            {

                e.printStackTrace();
                return null;
            }
        }
        else return connection;

    }
    public List<DeployOrder> queryOrder(int dayNum)
    {   String SQL;
        Connection dbconn=this.connectDB();
        ResultSet   rs = null;
        PreparedStatement pst;
        List<DeployOrder> dporders=new ArrayList();
        String[] Columnname;
        if(dayNum==0)
        //返回包含全部订单的list
        {   SQL="select * from `codedeployment`.`Orders`";
            try {
                pst =dbconn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
                rs=pst.executeQuery();
                int   ColumnCount=rs.getMetaData().getColumnCount();
                Columnname=new String[ColumnCount];
                for(int j=0;j<ColumnCount;j++)
                    Columnname[j]=rs.getMetaData().getColumnName(j+1);

                while(rs.next())
                {
                    int ono=rs.getInt("ONO");
                    java.util.Date date=rs.getTimestamp("ODate");
                    TestHost testHost=(TestHost)queryHost(rs.getInt("targetTHost"),Constants.TESTHOST).get(0);
                    PHostGroup targetGroup=(PHostGroup)queryGroup(rs.getInt("targetGroup"));
                    List<Code> codeList=queryCode(-1,null,-1,null,ono);
                    List<String> codePathList=new ArrayList<>(codeList.size());
                    List<Integer> codeIDList=new ArrayList<>(codeList.size());
                    for(int i=0;i<codePathList.size();i++)
                    {
                        codePathList.add(codeList.get(i).getFilePath());
                        codeIDList.add(codeList.get(i).getCno());
                    }
                    boolean isReleased=rs.getBoolean("isReleased");
                    dporders.add(new DeployOrder(ono,date,testHost,targetGroup,codePathList,codeIDList,isReleased));
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }


            return dporders;
        }
        else
        //返回dayNum天内订单的list
        {	SQL="SELECT * FROM `codedeployment`.`Orders` ORDER BY `日期` DESC  LIMIT 0,?;";
            try {
                pst =dbconn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
                rs=pst.executeQuery();
                pst.setInt(1, dayNum);
                rs=pst.executeQuery();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }
    }


    public int insertOrder(DeployOrder order)
    { 	String SQL;
        PreparedStatement pst;
        Connection dbconn=this.connectDB();
        SQL= "insert into`codedeployment`.`Orders` (ONO,ODate,TargetGroup,TargetTHost,LID,IsReleased)  values(?,?, ?, ?,?,?);";

        //先插订单
        try {
            pst =dbconn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, order.getOno());
            Timestamp ts=new Timestamp(order.getDate().getTime());
            pst.setTimestamp(2, ts);
            pst.setInt(3,order.getTargetGroup().getId());  //改为TargetGroup
            pst.setInt(4, order.getTargetTHost().getId());  //改为TargetTHost
            pst.setString(5,"NULL"); //改为LID
            pst.setBoolean(6,order.isReleased());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Constants.SUCCESS;
        //return Constants.FAILURE;
    }

    public int insertCode(List<Code> code)
    { 	String SQL;
        PreparedStatement pst;
        Connection dbconn=this.connectDB();
        Iterator<Code> it = code.iterator();
        Code comp;
        SQL= "insert into`codedeployment`.`Codes`   values(?,?, ?, ?,?);";

        while (it.hasNext()) {
            comp=it.next();
            try {
                pst =dbconn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, comp.getCno());
                pst.setString(2, comp.getFilename());
                pst.setBoolean(3, comp.isBackup());
                pst.setString(4, comp.getMd5());
                pst.setInt(5,comp.getOno());
                pst.executeUpdate();
                pst.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Constants.SUCCESS;
        //return Constants.FAILURE;

    }
    public int deleteCode(int CNO)
    {
        return Constants.SUCCESS;
        //return Constants.FAILURE;
    }
    public int deleteCodes(List<Code> codes){
        for(int i=0;i<codes.size();i++)
        {
            int ret=deleteCode(codes.get(i).getCno());
            if(ret==Constants.FAILURE)
            {
                System.out.println("delete code failure:"+i);
            }
        }
        return Constants.SUCCESS;
    }
    //可以查询单个生产、测试、本地主机以及生产主机组，分别输入PID、TID、LID和GID，type为Constant类中的类型常数
    public List<Host> queryHost(int id,int type)
    {
        List<Host> hosts=new ArrayList<>();
        DBOperationUtil dbo=new DBOperationUtil();
        Connection dbConn=dbo.connectDB();

        ResultSet rs=queryHost_help(id,type,dbConn);
        if(rs==null) return null;

        if(type!=Constants.PHOSTGROUP){
            try {
                if(!rs.next()) return null;
                switch (type) {
                    case Constants.LOCALHOST: {
                        hosts.add(new LocalHost(id, rs.getString("Address"),rs.getString("User")));
                        break;
                    }
                    case Constants.TESTHOST: {
                        hosts.add(new TestHost(id, rs.getString("Address")));
                        break;
                    }
                    case Constants.PRODUCTHOST: {
                        hosts.add(new ProductHost(id, rs.getString("Address"),rs.getInt("GID")));
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            try {
                if(rs.next()) {
                    do {
                        hosts.add(new ProductHost(rs.getInt("PID"), rs.getString("Address"),id));
                    } while (rs.next());
                }else
                    return null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return hosts;
    }

    private ResultSet queryHost_help(int id,int type,Connection conn){
        String tablename[]={"","","","LocalHost","TestHost","ProductHost","ProductHost"};
        String idname[]={"","","","LID","TID","PID","GID"};
        try {
            String sql="select * from "+tablename[type]+" where "+idname[type]+" = "+id;
            Statement stat=conn.createStatement();
            ResultSet rs=stat.executeQuery(sql);
            return rs;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //一次查询所有的生产主机组
    public List<PHostGroup> queryGroup()
    {

        List<PHostGroup> grouplist=new ArrayList<>();

        DBOperationUtil dbo=new DBOperationUtil();
        Connection dbConn=dbo.connectDB();

        try {

            Statement stat = dbConn.createStatement();
            String querysql="select * from PHostGroup";
            ResultSet rs=stat.executeQuery(querysql);

            if(rs.next()) {

                do {
                    List<Host> hosts = new ArrayList<>();
                    int gid = rs.getInt("GID");
                    String gname=rs.getString("GName");
                    int tid=rs.getInt("TID");
                    querysql = "select * from ProductHost where GID =" + gid;
                    ResultSet rs1 = stat.executeQuery(querysql);


                    if(rs1.next()) {
                        do {
                            int id = rs1.getInt("PID");
                            String address = rs1.getString("Address");
                            hosts.add(new TestHost(id, address));
                        } while(rs1.next());
                    }else{
                        return null;
                    }

                    grouplist.add(new PHostGroup(hosts,gid,gname,tid));
                } while(rs.next());

            } else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return grouplist;

    }
    public PHostGroup queryGroup(int GID)
    {
        String name="`CodeDeploy`.`PHostGroup`";
        PreparedStatement prstmt;
        try{
            String sql="select from "+name+" where GID=?";
            prstmt=connectDB().prepareStatement(sql);
            prstmt.setInt(1,GID);
            ResultSet rs=prstmt.executeQuery();
            rs.next();
            PHostGroup group=new PHostGroup();
            group.setId(GID);
            group.setHosts(queryHost(GID,Constants.PHOSTGROUP));
            group.setName(rs.getString("GName"));
            group.setTID(rs.getInt("TID"));
            rs.close();
            prstmt.close();

            return group;
        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public int deleteHost(int ID, int type) {

        PreparedStatement prstmt = null;
        if (type == Constants.LOCALHOST) {
            try {
                String sql = "DELETE FROM LOCALHOST " + " WHERE LID = ?";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setInt(1, ID);
                prstmt.executeUpdate();
                prstmt.close();
                return Constants.SUCCESS;
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }

        }
        else if (type == Constants.TESTHOST) {
            try {
                String sql = "DELETE FROM TESTHOST " + "WHERE TID = ?";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setInt(1, ID);
                prstmt.executeUpdate();
                prstmt.close();
                return Constants.SUCCESS;
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }
        }
        else if (type == Constants.PRODUCTHOST) {
            try {
                String sql = "DELETE FROM PRODUCTHOST " + "WHERE PID = ?";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setInt(1, ID);
                prstmt.executeUpdate();
                prstmt.close();
                return Constants.SUCCESS;
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }
        }
        return Constants.FAILURE;

    }

    public int deleteHost(Host host)

    {
        return deleteHost(host.getId(), host.getType());
    }

    public int updateHost(Host host) {
        int type = host.getType();
        PreparedStatement prstmt = null;
        if (type == Constants.LOCALHOST) {
            try {
                String sql = "UPDATE LOCALHOST " + "SET Address = ? ,User = ?" + "WHERE LID=? ";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setString(1, host.getAddress());
                prstmt.setString(2, ((LocalHost) host).getUser());
                prstmt.setInt(3, host.getId());
                prstmt.executeUpdate();
                prstmt.close();
                return Constants.SUCCESS;
            }catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }
        }
        else if (type == Constants.TESTHOST) {
            try {
                String sql = "UPDATE TESTHOST " + "SET Address = ? " + "WHERE TID=? ";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setString(1, host.getAddress());
                prstmt.setInt(2, host.getId());
                prstmt.executeUpdate();
                prstmt.close();
                return Constants.SUCCESS;
            }catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }
        }
        else if (type == Constants.PRODUCTHOST) {
            try {
                String sql = "UPDATE PRODUCTHOST " + "SET Address = ? " + "WHERE PID=? ";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setString(1, host.getAddress());
                prstmt.setInt(2, host.getId());
                prstmt.executeUpdate();
                prstmt.close();
                return Constants.SUCCESS;
            }catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }
        }
        return Constants.FAILURE;
    }
    public int insertHost(Host host)
    {
        int type=host.getType();
        if(type== Constants.LOCALHOST) {
            //插本地
            String name="`CodeDeploy`.`LocalHost`";
            PreparedStatement prstmt=null;
            try {
                String sql = "insert into "+name+" values(?,?,?)";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setInt(1,host.getId());
                prstmt.setString(2,host.getAddress());
                prstmt.setString(3,((LocalHost)host).getUser());
                prstmt.executeUpdate();
                prstmt.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                return Constants.FAILURE;
            }
            return Constants.SUCCESS;
        }
        else if(type==Constants.TESTHOST)
        {
            //插测试
            String name="`CodeDeploy`.`TestHost`";
            PreparedStatement prstmt=null;
            try{
                String sql="insert into "+name+" values(?,?)";
                prstmt=connectDB().prepareStatement(sql);
                prstmt.setInt(1,host.getId());
                prstmt.setString(2,host.getAddress());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                return Constants.FAILURE;
            }
            return Constants.SUCCESS;
        }
        else if(type==Constants.PRODUCTHOST) {
            //插生产
            String name="`CodeDeploy`.`ProductHost`";
            PreparedStatement prstmt=null;
            try{
                String sql="insert into "+name+" values(?,?,?)";
                prstmt=connectDB().prepareStatement(sql);
                prstmt.setInt(1,host.getId());
                prstmt.setString(2,host.getAddress());
                prstmt.setInt(3,((ProductHost)host).getLID());
                prstmt.executeUpdate();
                prstmt.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                return Constants.FAILURE;
            }
            return Constants.SUCCESS;
        }
        else return Constants.FAILURE;
    }
    public int insertGroup(PHostGroup group)
    {
        //插group
        String name="`CodeDeploy`.`PHostGroup`";
        PreparedStatement prstmt=null;
        try{
            String sql="insert into "+name+" values(?,?,?)";
            prstmt=connectDB().prepareStatement(sql);
            prstmt.setInt(1,group.getId());
            prstmt.setString(2,group.getName());
            prstmt.setInt(3,group.getTID());
            prstmt.executeUpdate();
            prstmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return Constants.FAILURE;
        }
        return Constants.SUCCESS;
        //return Constants.FAILURE;
    }
    public List<Code> queryCode(int CNO, String CName, int isBackup, String MD5, int ONO) {
        return null;
    }
}
