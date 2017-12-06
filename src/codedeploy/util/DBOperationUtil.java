package codedeploy.util;


import codedeploy.bean.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;


import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBOperationUtil {
    private static Connection connection = null;

    private static Connection connectDB() {
        if (connection == null) {
            String driverName = "com.mysql.jdbc.Driver";
            String dbURL = "jdbc:mysql://rm-wz9h00p5lzhh2mkbjao.mysql.rds.aliyuncs.com:3306";
            String sqltype = "mysql";
            try {

                Class.forName(driverName);
                Connection dbConn = DriverManager.getConnection(dbURL, "root", "123456qwerty!");
                return dbConn;

            } catch (Exception e)

            {

                e.printStackTrace();
                return null;
            }
        } else return connection;

    }

    public List<DeployOrder> queryOrder(int dayNum) {
        String SQL;
        Connection dbconn = this.connectDB();
        ResultSet rs = null;
        PreparedStatement pst;
        List<DeployOrder> dporders = new ArrayList();
        String[] Columnname;
        if (dayNum == 0)
        //返回包含全部订单的list
        {
            SQL = "select * from `codedeployment`.`Orders`";
            try {
                pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                rs = pst.executeQuery();
                int ColumnCount = rs.getMetaData().getColumnCount();
                Columnname = new String[ColumnCount];
                for (int j = 0; j < ColumnCount; j++)
                    Columnname[j] = rs.getMetaData().getColumnName(j + 1);
                while (rs.next()) {
                    int ono = rs.getInt("ONO");
                    java.util.Date date = rs.getTimestamp("ODate");
                    TestHost testHost = (TestHost) queryHost(rs.getInt("targetTHost"), Constants.TESTHOST).get(0);
                    PHostGroup targetGroup = (PHostGroup) queryGroup(rs.getInt("targetGroup"));
                    String name = rs.getString("OName");
                    List<Code> codeList = queryCode(-1, "", false, "", ono);
                    List<String> codePathList = new ArrayList<>(codeList.size());
                    List<Integer> codeIDList = new ArrayList<>(codeList.size());
                    for (int i = 0; i < codePathList.size(); i++) {
                        codePathList.add(codeList.get(i).getFilePath());
                        codeIDList.add(codeList.get(i).getCno());
                    }
                    int isReleasedInt = rs.getInt("isReleased");
                    boolean isReleased=false;
                    if(isReleasedInt==1)
                        isReleased=true;
                    dporders.add(new DeployOrder(ono, name, date, testHost, targetGroup, codePathList, codeIDList, isReleased));
                }
                dbconn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }

            return dporders;
        } else
        //返回dayNum天内订单的list
        {
            SQL = "SELECT * FROM `codedeployment`.`Orders` ORDER BY `日期` DESC  LIMIT 0,?;";
            try {
                pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, dayNum);
                rs = pst.executeQuery();
                while (rs.next()) {
                    int ono = rs.getInt("ONO");
                    java.util.Date date = rs.getTimestamp("ODate");
                    TestHost testHost = (TestHost) queryHost(rs.getInt("targetTHost"), Constants.TESTHOST).get(0);
                    PHostGroup targetGroup = (PHostGroup) queryGroup(rs.getInt("targetGroup"));
                    String name = rs.getString("OName");
                    List<Code> codeList = queryCode(-1, null, false, null, ono);
                    List<String> codePathList = new ArrayList<>(codeList.size());
                    List<Integer> codeIDList = new ArrayList<>(codeList.size());
                    for (int i = 0; i < codePathList.size(); i++) {
                        codePathList.add(codeList.get(i).getFilePath());
                        codeIDList.add(codeList.get(i).getCno());
                    }
                    boolean isReleased = rs.getBoolean("isReleased");
                    dporders.add(new DeployOrder(ono, name, date, testHost, targetGroup, codePathList, codeIDList, isReleased));
                }

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }

            return dporders;
        }
    }

    public List<DeployOrder> queryOrderByName(String name) {
        String SQL;
        Connection dbconn = this.connectDB();
        ResultSet rs = null;
        PreparedStatement pst;
        List<DeployOrder> dporders = new ArrayList();
        SQL = "SELECT * FROM `codedeployment`.`Orders` WHERE OName = ? ORDER BY `日期` DESC;";
        try {
            pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, name);
            rs = pst.executeQuery();
            while (rs.next()) {
                int ono = rs.getInt("ONO");
                java.util.Date date = rs.getTimestamp("ODate");
                TestHost testHost = (TestHost) queryHost(rs.getInt("targetTHost"), Constants.TESTHOST).get(0);
                PHostGroup targetGroup = (PHostGroup) queryGroup(rs.getInt("targetGroup"));
                List<Code> codeList = queryCode(-1, null, false, null, ono);
                List<String> codePathList = new ArrayList<>(codeList.size());
                List<Integer> codeIDList = new ArrayList<>(codeList.size());
                for (int i = 0; i < codePathList.size(); i++) {
                    codePathList.add(codeList.get(i).getFilePath());
                    codeIDList.add(codeList.get(i).getCno());
                }
                boolean isReleased = rs.getBoolean("isReleased");
                dporders.add(new DeployOrder(ono, name, date, testHost, targetGroup, codePathList, codeIDList, isReleased));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return dporders;
    }

    //TODO 改插入的LID
    public int insertOrder(DeployOrder order) {/////LID
        String SQL;
        PreparedStatement pst;
        Connection dbconn = this.connectDB();
        SQL = "insert into`codedeployment`.`Orders` (ODate,TargetGroup,TargetTHost,LID,IsReleased,OName)  values(?, ?, ?,?,?,?);";

        //先插订单
        try {
            pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
//            pst.setString(1, "null");
            Timestamp ts = new Timestamp(order.getDate().getTime());

            pst.setTimestamp(1, ts);
            pst.setInt(2, order.getTargetGroup().getId());  //改为TargetGroup
            pst.setInt(3, order.getTargetTHost().getId());  //改为TargetTHost
            pst.setInt(4, 1); //改为LID
            pst.setBoolean(5, order.isReleased());
            pst.setString(6,order.getName());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Constants.SUCCESS;
        //return Constants.FAILURE;
    }//LID



    public int deleteCodes(List<Code> codes) {
        for (int i = 0; i < codes.size(); i++) {
            int ret = deleteCode(codes.get(i).getCno());
            if (ret == Constants.FAILURE) {
                System.out.println("delete code failure:" + i);
            }
        }
        return Constants.SUCCESS;
    }

    public int querymaxPid ()
    {   DBOperationUtil dbo = new DBOperationUtil();
        Connection dbConn = dbo.connectDB();
        String sql = "select max(PID) from `codedeployment`.`ProductHost` ";
        ResultSet rs=null;

        try {
            Statement stat = dbConn.createStatement();
            rs = stat.executeQuery(sql);
            while(rs.next())
            {
                return rs.getInt(1)+1;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }
    public List<Host> queryHostbyGID(int GID)
    {   DBOperationUtil dbo = new DBOperationUtil();
        Connection dbConn = dbo.connectDB();
        String sql = "select * from `codedeployment`.`ProductHost` where GID = " + GID;
        ResultSet rs=null;
        List<Host> hostlist=new ArrayList<>();
        try {
            Statement stat = dbConn.createStatement();
            rs = stat.executeQuery(sql);
            while(rs.next())
            {
                hostlist.add(new ProductHost(rs.getInt("PID"), rs.getString("Address"), rs.getInt("GID")));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return hostlist;

    }
    //可以查询单个生产、测试、本地主机以及生产主机组，分别输入PID、TID、LID和GID，type为Constant类中的类型常数
    public List<Host> queryHost(int id, int type) {
        List<Host> hosts = new ArrayList<>();
        DBOperationUtil dbo = new DBOperationUtil();
        Connection dbConn = dbo.connectDB();
        ResultSet rs = queryHost_help(id, type, dbConn);
        if (rs == null)
            return null;

        if (type != Constants.PRODUCTHOST) {
            try {
                while (rs.next()){
                    id=rs.getInt("TID");
                    switch (type) {
                        case Constants.LOCALHOST: {
                            hosts.add(new LocalHost(id, rs.getString("Address"), rs.getString("User")));
                            break;
                        }
                        case Constants.TESTHOST: {
                            hosts.add(new TestHost(id, rs.getString("HAddress")));
                            break;
                        }
                        case Constants.PHOSTGROUP: {
                            hosts.add(new ProductHost(rs.getInt("GID"), rs.getString("GName"), rs.getInt("TID")));
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                while (rs.next()) {
                    {
                        hosts.add(new ProductHost(rs.getInt("PID"), rs.getString("Address"), rs.getInt("GID")));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hosts;
    }

    private ResultSet queryHost_help(int id, int type, Connection conn) {
        String tablename[] = {"", "", "", "`codedeployment`.`LocalHost`", "`codedeployment`.`TestHost`", "`codedeployment`.`ProductHost`", "`codedeployment`.`PHOSTGROUP`"};//此处与Constants中的常量绑定，修改Constants则此处也需要修改
        String idname[] = {"", "", "", "LID", "TID", "PID", "GID"};
        try {
            String sql = "select * from " + tablename[type] + " where " + idname[type] + " = " + id;

            if (id == -1)
                sql = "select * from " + tablename[type];
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Host> queryAllHost() {
        //List<Host> list = queryHost(-1, Constants.LOCALHOST);
        List<Host>list=new ArrayList<>();
        List<Host> pList=queryHost(-1, Constants.PRODUCTHOST);
        List<Host> tList=queryHost(-1,Constants.TESTHOST);

        if(pList!=null)
            list.addAll(pList);
        if(tList!=null)
            list.addAll(tList);
        return list;

    }
    public List<PHostGroup> queryGroupbyTID(int TID) {

        List<PHostGroup> grouplist = new ArrayList<>();
        DBOperationUtil dbo = new DBOperationUtil();
        Connection dbConn = dbo.connectDB();


        try {

            Statement stat = dbConn.createStatement();

            String querysql = "select * from `codedeployment`.`phostgroup` where TID="+TID;
            ResultSet rs = stat.executeQuery(querysql);
            List<Integer> gid=new ArrayList<>();
            List<String> gname =new ArrayList<>();
            while (rs.next()) {



                gid.add( rs.getInt("GID"));
                gname .add(rs.getString("GName"));

            }
            for(int i=0;i<gid.size();i++) {
                List<Host> hosts = new ArrayList<>();
                querysql = "select * from  `codedeployment`.`producthost` where GID ="+gid.get(i);
                ResultSet rs1 = stat.executeQuery(querysql);
                while (rs1.next()) {
                    int id = rs1.getInt("PID");
                    String address = rs1.getString("Address");
                    hosts.add(new ProductHost(id, address, gid.get(i)));

                }

                grouplist.add(new PHostGroup(hosts, gid.get(i), gname.get(i), TID));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return grouplist;

    }
    public List<Node> queryAllHostNode() {
        //List<Host> list = queryHost(-1, Constants.LOCALHOST);
        List<Node>list=new ArrayList<>();
        List<Host> tList=queryHost(-1,Constants.TESTHOST);
        List<PHostGroup>  Grouplist;

        for(int i=0;i<tList.size();i++)
        {
            list.add(new Node(tList.get(i).getId(),i,1,"",tList.get(i).getAddress()));
            Grouplist=queryGroupbyTID(tList.get(i).getId());
            for(int j=0;j<Grouplist.size();j++)
            {
                list.add(new Node(Grouplist.get(j).getId(),100+j,i,"",Grouplist.get(j).getName()));
                List<Host> hosts=Grouplist.get(j).getHosts();
                for(int k=0;k<hosts.size();k++)
                {
                    list.add(new Node(hosts.get(k).getId(),200+k,100+j,"",hosts.get(k).getAddress()));
                }


            }

        }
        if(list!=null)
            return list;
        else return null;
    }
    //一次查询所有的生产主机组
    public List<PHostGroup> queryGroup() {

        DBOperationUtil dbo = new DBOperationUtil();
        Connection dbConn = dbo.connectDB();

        try {

            Statement stat = dbConn.createStatement();
            String querysql = "select * from `codedeployment`.`phostgroup`";
            ResultSet rs = stat.executeQuery(querysql);

            if (rs.next()) {


                    int gid = rs.getInt("GID");
                    String gname = rs.getString("GName");
                    int tid = rs.getInt("TID");
                    List<PHostGroup> groups=new ArrayList<>();
                    groups.add(new PHostGroup(gid,gname,tid));
                    while(rs.next())
                    {
                        gid = rs.getInt("GID");
                        gname = rs.getString("GName");
                        tid = rs.getInt("TID");
                        groups.add(new PHostGroup(gid,gname,tid));
                    }
                    for(int i=0;i<groups.size();i++) {
                        querysql = "select * from `codedeployment`.`producthost` where GID =" + groups.get(i).getId();
                        ResultSet rs1 = stat.executeQuery(querysql);
                        List<Host> hosts = new ArrayList<>();
                        while (rs1.next())
                        {
                            int id = rs1.getInt("PID");
                            String address = rs1.getString("Address");
                            hosts.add(new TestHost(id, address));

                        }
                        groups.get(i).setHosts(hosts);
                    }
                    stat.close();
                    return groups;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


    public PHostGroup queryGroup(int GID) {
        PreparedStatement prstmt;
        try {
            String sql = "select * from `CodeDeployment`.`PHostGroup` where GID = ?";
            prstmt = connectDB().prepareStatement(sql);
            prstmt.setInt(1, GID);
            ResultSet rs = prstmt.executeQuery();
            rs.next();
            PHostGroup group = new PHostGroup();
            group.setId(GID);
            group.setHosts(queryHost(GID, Constants.PHOSTGROUP));
            group.setName(rs.getString("GName"));
            group.setTID(rs.getInt("TID"));
            rs.close();
            prstmt.close();

            return group;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int deleteHost(int ID, int type) {

        PreparedStatement prstmt = null;
        if (type == Constants.LOCALHOST) {
            try {
                String sql = "DELETE FROM  `codedeployment`.`localhost` " + " WHERE LID = ?";
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

        } else if (type == Constants.TESTHOST) {
            try {
                String sql = "DELETE FROM `codedeployment`.`testhost` " + "WHERE TID = ?";
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
        } else if (type == Constants.PRODUCTHOST) {
            try {
                String sql = "DELETE FROM `codedeployment`.`producthost` " + "WHERE PID = ?";
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
                String sql = "UPDATE  `codedeployment`.`localhost` " + "SET Address = ? " + "WHERE LID=? ";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setString(1, host.getAddress());
                prstmt.setInt(2, host.getId());
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
        } else if (type == Constants.TESTHOST) {
            try {
                String sql = "UPDATE `codedeployment`.`testhost` " + "SET HAddress = ? " + "WHERE TID=? ";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setString(1, host.getAddress());
                prstmt.setInt(2, host.getId());
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
        } else if (type == Constants.PRODUCTHOST) {
            try {
                String sql = "UPDATE `codedeployment`.`producthost` " + "SET Address = ? " + "WHERE PID=? ";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setString(1, host.getAddress());
                prstmt.setInt(2, host.getId());
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

    public int insertHost(Host host) {
        int type = host.getType();
        if (type == Constants.LOCALHOST) {
            //插本地
            String name = "`CodeDeploy`.`LocalHost`";
            PreparedStatement prstmt = null;
            try {
                String sql = "insert into " + name + " values(?,?,?)";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setString(1, "null");
                prstmt.setString(2, host.getAddress());
                prstmt.setString(3, ((LocalHost) host).getUser());
                prstmt.executeUpdate();
                prstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return Constants.FAILURE;
            }
            return Constants.SUCCESS;
        } else if (type == Constants.TESTHOST) {
            //插测试
            String name = "`CodeDeploy`.`TestHost`";
            PreparedStatement prstmt = null;
            try {
                String sql = "insert into " + name + " values(?,?)";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setString(1, "null");
                prstmt.setString(2, host.getAddress());
            } catch (SQLException e) {
                e.printStackTrace();
                return Constants.FAILURE;
            }
            return Constants.SUCCESS;
        } else if (type == Constants.PRODUCTHOST) {
            //插生产
            String name = "`codedeployment`.`ProductHost` ";
            PreparedStatement prstmt = null;
            try {
                String sql = "insert into " + name + " values(?,?,?)";
                prstmt = connectDB().prepareStatement(sql);
                System.out.println(querymaxPid());
                prstmt.setInt(1, querymaxPid());
                prstmt.setString(2, host.getAddress());
                prstmt.setInt(3, ((ProductHost) host).getLID());
                prstmt.executeUpdate();
                prstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return Constants.FAILURE;
            }
            return Constants.SUCCESS;
        } else return Constants.FAILURE;
    }
    public int updategroup (int GID,String GName) {
        //改group
        String name = "`codedeployment`.`phostgroup`";
        PreparedStatement prstmt = null;
        try {
            String sql = "update "+name+"set GName='"+GName+"'where GID="+GID;
            System.out.println(sql);
            prstmt = connectDB().prepareStatement(sql);
            prstmt.executeUpdate();
            prstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Constants.FAILURE;
        }
        return Constants.SUCCESS;
        //return Constants.FAILURE;
    }
    public int insertGroup(PHostGroup group) {
        //插group
        String name = "`CodeDeploy`.`PHostGroup`";
        PreparedStatement prstmt = null;
        try {
            String sql = "insert into " + name + " values(?,?,?)";
            prstmt = connectDB().prepareStatement(sql);
            prstmt.setString(1, "null");
            prstmt.setString(2, group.getName());
            prstmt.setInt(3, group.getTID());
            prstmt.executeUpdate();
            prstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Constants.FAILURE;
        }
        return Constants.SUCCESS;
        //return Constants.FAILURE;
    }



    public int deleteOrder(int ono){
        PreparedStatement prstmt = null;
        String sql="DELETE FROM `codedeploy`.`ORDERS` "+" WHERE ONO = ?";
        try {
            prstmt = connectDB().prepareStatement(sql);
            prstmt.setInt(1, ono);
            prstmt.execute();
            prstmt.close();
            return Constants.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Constants.FAILURE;
    }

    public int insertCode(List<Code> code) {
        String SQL;
        PreparedStatement pst;
        Connection dbconn = this.connectDB();
        Iterator<Code> it = code.iterator();
        Code comp;
        SQL = "insert into`codedeployment`.`Codes`(CNO,CNAME,ISBACKUP,MD5,ONO)   values(?,?, ?, ?,?);";

        while (it.hasNext()) {
            comp = it.next();
            try {
                pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, comp.getCno());
                pst.setString(2, comp.getFilename());
                pst.setBoolean(3, comp.isBackup());
                pst.setString(4, comp.getMd5());
                pst.setInt(5, comp.getOno());
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

    public int deleteCode(int CNO) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn=connectDB();
            String sql;
            sql = "delete  from "+"`codedeployment`.`Codes` where cno=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, CNO);
            int rs = stmt.executeUpdate();
            System.out.println("Delete !");//

            stmt.close();
            conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        return Constants.SUCCESS;
        //return Constants.FAILURE;
    }



    public List<Code> queryCode(int CNO, String CName, boolean isBackup, String MD5, int ONO) {
        List<Code> codeList = new ArrayList<Code>();

        Connection conn = null;
        PreparedStatement stmt=null;
        String whereClause = "";
        if (CNO != -1) whereClause += "cno=? and ";
        if (CName.length() != 0) whereClause += "cname=? and ";
        if (isBackup != false) whereClause += "isBackup=? and ";
        if (MD5.length() != 0) whereClause += "md5=? and ";
        if (ONO != -1) whereClause += "ono=? and ";// and?
        //if (filePath.length() != 0) whereClause += "filepath=? and ";
        whereClause = whereClause.substring(0, whereClause.length() - 5);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = connectDB();
            System.out.println(CNO);
            String sql;
            int i = 1;
            sql = "SELECT * from " + "`codedeployment`.`Codes` where " + whereClause;
            //sql = "SELECT * from " + "`CodeDeploy`.`Codes`";
            stmt = conn.prepareStatement(sql);
            if (CNO != -1) {
                stmt.setInt(i, CNO);
                i++;
            }
            if (CName.length() != 0) {
                stmt.setString(i, CName);
                i++;
            }
            if (isBackup != false) {
                stmt.setBoolean(i, isBackup);
                i++;
            }
            if (MD5.length() != 0) {
                stmt.setString(i, MD5);
                i++;
            }
            if (ONO != -1)
                stmt.setInt(i, ONO);


            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int cno = rs.getInt("cno");
                String cname = rs.getString("cname");
                boolean isBackup2 = rs.getBoolean("isbackup");
                String md5 = rs.getString("md5");
                int ono = rs.getInt("ono");
                codeList.add(new Code(cno, cname, isBackup2, md5, ono,""));
            }


            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return codeList;
    }

    public int updateOrderisReleased(DeployOrder order) {
        String SQL;
        PreparedStatement pst;
        Connection dbconn = this.connectDB();
        SQL = "update `codedeployment`.`Orders`  set IsReleased=true where ONO=?;";

        //先插订单
        try {
            pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, order.getOno());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Constants.SUCCESS;
        //return Constants.FAILURE;
    }

    public int updateCodeisBackup(int cno) {
        String SQL;
        PreparedStatement pst;
        Connection dbconn = this.connectDB();
        SQL = "update `codedeployment`.`Codes`  set IsBackup=true where CNO=?;";

        //先插订单
        try {
            pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cno);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Constants.SUCCESS;
        //return Constants.FAILURE;
    }



}
