package codedeploy.util;


import codedeploy.CodeDeploySystem;
import codedeploy.bean.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBOperationUtil {
    private static Connection connection = null;
    private int lid=1;

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
            SQL = "select * from `codedeployment`.`orders` where `orders`.`LID`=?";
            try {
                pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1,CodeDeploySystem.getLid());
                rs = pst.executeQuery();
                int ColumnCount = rs.getMetaData().getColumnCount();
                Columnname = new String[ColumnCount];
                for (int j = 0; j < ColumnCount; j++)
                    Columnname[j] = rs.getMetaData().getColumnName(j + 1);
                while (rs.next()) {
                    int ono = rs.getInt("ONO");
                    java.util.Date date = rs.getTimestamp("ODate");
                    TestHost testHost = (TestHost) queryHost(rs.getInt("targetTHost"), Constants.TESTHOST).get(0);
                    PHostGroup targetGroup =queryGroup(rs.getInt("targetGroup"));
                    String name = rs.getString("OName");
                    List<Code> codeList = queryCode(-1, "", false, "", ono);
                    List<String> codePathList = new ArrayList<>(codeList.size());
                    List<Integer> codeIDList = new ArrayList<>(codeList.size());
                    for (int i = 0; i < codePathList.size(); i++) {
                        codePathList.add(codeList.get(i).getFilename());
                        codeIDList.add(codeList.get(i).getCno());
                    }
                    int isReleasedInt = rs.getInt("isReleased");
                    boolean isReleased=false;
                    if(isReleasedInt==1)
                        isReleased=true;
                    dporders.add(new DeployOrder(ono, name, date, testHost, targetGroup, codePathList, codeIDList, isReleased,CodeDeploySystem.getLid()));

                }
                dbconn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }

            return dporders;
        } else
        //返回dayNum天内订单的list
        {
            SQL = "SELECT * FROM `codedeployment`.`Orders` where `LID`=? and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(`odate`) ORDER BY `odate` DESC;";
            try {
                pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1,CodeDeploySystem.getLid());
                pst.setInt(2, dayNum);
                rs = pst.executeQuery();
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
                        codePathList.add(codeList.get(i).getFilename());
                        codeIDList.add(codeList.get(i).getCno());
                    }
                    boolean isReleased = rs.getBoolean("isReleased");
                    dporders.add(new DeployOrder(ono, name, date, testHost, targetGroup, codePathList, codeIDList, isReleased,CodeDeploySystem.getLid()));
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
        SQL = "SELECT * FROM `codedeployment`.`Orders` where `LID`=? and `oname`=? ORDER BY `odate` DESC;";
        try {
            pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,CodeDeploySystem.getLid());
            pst.setString(2, name);
            rs = pst.executeQuery();
            while (rs.next()) {
                int ono = rs.getInt("ONO");
                java.util.Date date = rs.getTimestamp("ODate");
                TestHost testHost = (TestHost) queryHost(rs.getInt("targetTHost"), Constants.TESTHOST).get(0);
                PHostGroup targetGroup =queryGroup(rs.getInt("targetGroup"));
                int lid=rs.getInt("LID");
                List<Code> codeList = queryCode(-1, null, false, null, ono);
                List<String> codePathList = new ArrayList<>(codeList.size());
                List<Integer> codeIDList = new ArrayList<>(codeList.size());
                for (int i = 0; i < codePathList.size(); i++) {
                    codePathList.add(codeList.get(i).getFilename());
                    codeIDList.add(codeList.get(i).getCno());
                }
                boolean isReleased = rs.getBoolean("isReleased");
                dporders.add(new DeployOrder(ono, name, date, testHost, targetGroup, codePathList, codeIDList, isReleased,lid));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return dporders;
    }
    public List<DeployOrder> queryOrderByID(int id)
    {
        String SQL;
        Connection dbconn = this.connectDB();
        ResultSet rs;
        PreparedStatement pst;
        List<DeployOrder> dporders = new ArrayList();
        SQL = "SELECT * FROM `codedeployment`.`Orders` where `ONO`=? ORDER BY `odate` DESC;";
        try {
            pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,id);
            rs = pst.executeQuery();
            while (rs.next()) {
                int ono = rs.getInt("ONO");
                java.util.Date date = rs.getTimestamp("ODate");
                TestHost testHost = (TestHost) queryHost(rs.getInt("targetTHost"), Constants.TESTHOST).get(0);
                PHostGroup targetGroup =queryGroup(rs.getInt("targetGroup"));
                int lid=rs.getInt("LID");
                List<Code> codeList = queryCode(-1, null, false, null, ono);
                List<String> codePathList = new ArrayList<>(codeList.size());
                List<Integer> codeIDList = new ArrayList<>(codeList.size());
                for (int i = 0; i < codePathList.size(); i++) {
                    codePathList.add(codeList.get(i).getFilename());
                    codeIDList.add(codeList.get(i).getCno());
                }
                String name = rs.getString("oname");
                boolean isReleased = rs.getBoolean("isReleased");
                dporders.add(new DeployOrder(ono, name, date, testHost, targetGroup, codePathList, codeIDList, isReleased,lid));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return dporders;
    }

    public int insertOrder(DeployOrder order) {/////LID
        String SQL;
        PreparedStatement pst;
        Connection dbconn = this.connectDB();
        SQL = "insert into`codedeployment`.`Orders` (ODate,TargetGroup,TargetTHost,LID,IsReleased,OName)  values(?, ?, ?,?,?,?);";


        try {
            //先插订单
            pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
//            pst.setString(1, "null");
            Timestamp ts = new Timestamp(order.getDate().getTime());

            pst.setTimestamp(1, ts);
            pst.setInt(2, order.getTargetGroup().getId());  //改为TargetGroup
            pst.setInt(3, order.getTargetTHost().getId());  //改为TargetTHost
            pst.setInt(4, CodeDeploySystem.getLid());
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
    }
    public List<DeployOrder> queryOrderByBriefName(String name,int dayNum) {
        String SQL;
        Connection dbconn = this.connectDB();
        ResultSet rs = null;
        PreparedStatement pst;
        List<DeployOrder> dporders = new ArrayList();
        SQL = "SELECT * FROM `codedeployment`.`Orders` where `LID`=? and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(`odate`) and OName like '%"+name+"%'ORDER BY  `odate`  DESC;";
        try {
            pst = dbconn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, CodeDeploySystem.getLid());
            pst.setInt(2, dayNum);
            rs = pst.executeQuery();
            System.out.println(rs.getFetchSize());
            while (rs.next()) {
                int ono = rs.getInt("ONO");
                java.util.Date date = rs.getTimestamp("ODate");
                TestHost testHost = (TestHost) queryHost(rs.getInt("targetTHost"), Constants.TESTHOST).get(0);
                PHostGroup targetGroup = (PHostGroup) queryGroup(rs.getInt("targetGroup"));
                List<Code> codeList = queryCode(-1, "", false, "", ono);
                List<String> codePathList = new ArrayList<>(codeList.size());
                List<Integer> codeIDList = new ArrayList<>(codeList.size());
                for (int i = 0; i < codePathList.size(); i++) {
                    codePathList.add(codeList.get(i).getFilename());
                    codeIDList.add(codeList.get(i).getCno());
                }
                boolean isReleased = rs.getBoolean("isReleased");
                dporders.add(new DeployOrder(ono,rs.getString("OName"), date, testHost, targetGroup, codePathList, codeIDList, isReleased));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return dporders;
    }


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
        String sql = "select max(PID) from `codedeployment`.`ProductHost`,codedeployment.lp where" +
                " codedeployment.lp.pid=codedeployment.producthost.PID and " +
                "codedeployment.lp.LID=?";
        ResultSet rs=null;

        try {
            PreparedStatement stat = dbConn.prepareStatement(sql);
            stat.setInt(1,CodeDeploySystem.getLid());
            rs = stat.executeQuery();
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
    {
        DBOperationUtil dbo = new DBOperationUtil();
        Connection dbConn = dbo.connectDB();
        String sql = "select * from `codedeployment`.`ProductHost`, `codedeployment`.`LP` " +
                "where `codedeployment`.`ProductHost`.`PID`=`codedeployment`.`lp`.`PID`" +
                " and `codedeployment`.`lp`.`lid`=?" +
                " and GID=?;" ;
        ResultSet rs=null;
        List<Host> hostlist=new ArrayList<>();
        try {
            PreparedStatement stat = dbConn.prepareStatement(sql);
            stat.setInt(1,CodeDeploySystem.getLid());
            stat.setInt(2,GID);
            rs = stat.executeQuery();
            while(rs.next())
            {
                hostlist.add(new ProductHost(rs.getInt("PID"), rs.getString("Address"), rs.getInt("GID")));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return hostlist;

    }
    //可以查询单个生产、测试、本地主机，分别输入PID、TID、LID，type为Constant类中的类型常数
    public List<Host> queryHost(int id, int type) {
        List<Host> hosts = new ArrayList<>();
        DBOperationUtil dbo = new DBOperationUtil();
        Connection dbConn = dbo.connectDB();
        ResultSet rs = queryHost_help(id, type, dbConn);
        if (rs == null)
            return null;

        if (type != Constants.PHOSTGROUP) {
            try {
                while (rs.next()){
                    switch (type) {
                        case Constants.LOCALHOST: {
                            hosts.add(new LocalHost(rs.getInt("LID"), rs.getString("Address"), rs.getString("User")));
                            break;
                        }
                        case Constants.TESTHOST: {
                            hosts.add(new TestHost(rs.getInt("TID"), rs.getString("Address")));
                            break;
                        }
                        case Constants.PRODUCTHOST:{
                            hosts.add(new ProductHost(rs.getInt("PID"),rs.getString("Address"),rs.getInt("GID")));
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
        String tablename[] = {"", "", "", "`codedeployment`.`LocalHost`", "`codedeployment`.`TestHost`", "`codedeployment`.`ProductHost`", "`codedeployment`.`ProductHost`"};//此处与Constants中的常量绑定，修改Constants则此处也需要修改
        String idname[] = {"", "", "", "LID", "`codedeployment`.`testhost`.`TID`", "`codedeployment`.`producthost`.PID", "GID"};
        String table;
        if(type==Constants.TESTHOST)
            table=tablename[type]+",`codedeployment`.`lt`";
        else if(type==Constants.PRODUCTHOST)
            table=tablename[type]+",`codedeployment`.`lp`";
        else table=tablename[type];
        String whereclause;
        if(type==Constants.TESTHOST)
            whereclause=tablename[type]+".`tid`=`codedeployment`.`lt`.`tid` and " +
                    "`codedeployment`.`lt`.`lid`=?";
        else if(type==Constants.PRODUCTHOST)
            whereclause=tablename[type]+".`pid`=`codedeployment`.`lp`.`pid` and " +
                    "`codedeployment`.`lp`.`lid`=?";
        else whereclause=" ";
        try {
            String sql = "select * from " + table + " where " + whereclause+" and "+idname[type]+"=?;";
            if (id == -1)
                sql = "select * from " + table+" where "+whereclause+";";
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1,CodeDeploySystem.getLid());
            if(id!=-1)
                stat.setInt(2,id);
            ResultSet rs = stat.executeQuery();
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
    public List<PHostGroup> queryGroup(boolean detail) {

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
                    if(detail){
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
            if(rs.next())
            {
                PHostGroup group = new PHostGroup();
                group.setId(GID);
                //group.setHosts(queryHost(GID, Constants.PHOSTGROUP));
                group.setName(rs.getString("GName"));
                group.setTID(rs.getInt("TID"));
                rs.close();
                prstmt.close();
                return group;
            }
            return null;
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
    public int deleteHosbyGID(int GID) {

            PreparedStatement prstmt = null;
            try {
                String sql = "DELETE FROM `codedeployment`.`producthost` WHERE GID = ?";
                prstmt = connectDB().prepareStatement(sql);
                prstmt.setInt(1, GID);
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
                String sql = "UPDATE `codedeployment`.`testhost` " + "SET Address = ? " + "WHERE TID=? ";
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
            String name = "`codedeployment`.`LocalHost`";
            CallableStatement cstmt = null;
            try {
                cstmt = connectDB().prepareCall("{call `codedeployment`.`insertLocalHost`(?,?)}"); // 创建一个预编译的存储过程调用SQL语句
                cstmt.setString(1, host.getAddress()); // 参数索引指定，第一个参数
                cstmt.registerOutParameter(2, Types.INTEGER); // 注册输出参数以及其SQL类型
                cstmt.execute(); // 执行存储过程
                int lid = cstmt.getInt(2); // 计算完成后获取输出参数的值
                CodeDeploySystem.setLid(lid);
            } catch (SQLException e) {
                e.printStackTrace();
                return Constants.FAILURE;
            }
            return Constants.SUCCESS;
        } else if (type == Constants.TESTHOST) {
            //插测试
            try {
//               String name = "`codedeployment`.`TestHost`";
//                PreparedStatement prstmt;
//                String sql = "insert into " + name + " values(?,?);";
//                prstmt = connectDB().prepareStatement(sql);
//                prstmt.setString(1, null);
//                prstmt.setString(2, host.getAddress());
//                prstmt.executeUpdate();
//                prstmt.close();
                CallableStatement cstmt;
                cstmt = connectDB().prepareCall("{call `codedeployment`.`insertTestHost`(?,?)}"); // 创建一个预编译的存储过程调用SQL语句
                cstmt.setString(1, host.getAddress()); // 参数索引指定，第一个参数
                cstmt.setInt(2, CodeDeploySystem.getLid()); // 注册输出参数以及其SQL类型
                cstmt.execute(); // 执行存储过程
            } catch (SQLException e) {
                e.printStackTrace();
                return Constants.FAILURE;
            }
            return Constants.SUCCESS;
        } else if (type == Constants.PRODUCTHOST) {
            //插生产
            try {
//                String name = "`codedeployment`.`ProductHost` ";
//                PreparedStatement prstmt = null;
//                String sql = "insert into " + name + " values(?,?,?)";
//                prstmt = connectDB().prepareStatement(sql);
//                prstmt.setString(1, null);
//                prstmt.setString(2, host.getAddress());
//                prstmt.setInt(3, ((ProductHost) host).getGID());
//                prstmt.executeUpdate();
//                prstmt.close();
                CallableStatement cstmt;
                cstmt = connectDB().prepareCall("{call `codedeployment`.`insertProductHost`(?,?,?)}"); // 创建一个预编译的存储过程调用SQL语句
                cstmt.setString(1, host.getAddress()); // 参数索引指定，第一个参数
                cstmt.setInt(2,((ProductHost)host).getGID());
                cstmt.setInt(3, CodeDeploySystem.getLid()); // 注册输出参数以及其SQL类型
                cstmt.execute(); // 执行存储过程
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
        String name = "`codedeployment`.`PHostGroup`";
        PreparedStatement prstmt = null;
        try {
            String sql = "insert into " + name + " values(?,?,?)";
            prstmt = connectDB().prepareStatement(sql);
            prstmt.setNull(1, Types.INTEGER);
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

    public int deleteGroup(int GID) {

        PreparedStatement prstmt = null;
        String sql="DELETE FROM `codedeployment`.`phostgroup`  WHERE GID = ?";
        try {
            prstmt = connectDB().prepareStatement(sql);
            prstmt.setInt(1, GID);
            prstmt.execute();
            prstmt.close();
            deleteHosbyGID(GID);
            return Constants.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Constants.FAILURE;
    }


    public int deleteOrder(int ono){
        PreparedStatement prstmt = null;
        String sql="DELETE FROM `codedeployment`.`ORDERS` "+" WHERE ONO = ?";
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
        Iterator<Code> it = code.iterator();
        Code comp;


        while (it.hasNext()) {
            comp = it.next();
            try {
//                SQL = "insert into`codedeployment`.`Codes`(CNO,CNAME,ISBACKUP,MD5,ONO)   values(?,?, ?, ?,?);";
//                pst = connectDB().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
//                pst.setInt(1, comp.getCno());
//                pst.setString(2, comp.getFilename());
//                pst.setBoolean(3, comp.isBackup());
//                pst.setString(4, comp.getMd5());
//                pst.setInt(5, comp.getOno());
//                pst.executeUpdate();
//                pst.close();

                CallableStatement cstmt=connectDB().prepareCall("{call insertCode(?,?,?,?)}");
                cstmt.setString(1,comp.getFilename());
                if(comp.isBackup())
                    cstmt.setInt(2,1);
                else cstmt.setInt(2,0);
                cstmt.setString(3,comp.getMd5());
                cstmt.setInt(4,comp.getOno());
                cstmt.execute();
            }
            catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return Constants.FAILURE;
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
                codeList.add(new Code(cno, cname, isBackup2, md5, ono));
            }

            rs.close();
            stmt.close();

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
