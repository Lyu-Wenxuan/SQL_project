package com.java.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
@SuppressWarnings("unchecked")
public class SQLTest {


    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //数据库名字medical_trade1
    static final String DB_URL = "jdbc:mysql://localhost:3306/medicine_system?useSSL=false&serverTimezone=UTC";
    // 数据库的用户名与密码，需要根据自己在PUTTY的设置
    static final String USER = "root";
    static final String PASS = "123456";

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        String name = null;
        String password = null;
        int i = -1;

        boolean login = login(name, password);

        while (login == false) {
            System.out.println("登陆失败");
            login = login(name, password);
        }

        System.out.println("登陆成功！");
        menu();
        choose(i);
    }

    //登录系统（要输入正确的用户名和密码）

    public static boolean login(String username, String password) throws IOException {
        System.out.println("Please input name:");
        BufferedReader in1 = new BufferedReader(new InputStreamReader(System.in));
        username = in1.readLine();
        System.out.println("Please input password:");
        BufferedReader in2 = new BufferedReader(new InputStreamReader(System.in));
        password = in2.readLine();
        //写用户名，密码
        if (username.equals("root") && password.equals("123456")) {
            return true;
        } else {
            return false;
        }
    }

    //菜单（据自己的设计的功能）

    static void menu() {

        System.out.println("Please choose the operations:(input the number,0 to quit)");
        System.out.println("********************************************");
        System.out.println("1:单表查询物流公司表");
        System.out.println("2:单表查询订单");
        System.out.println("3:输入id查询物流公司表");
        System.out.println("4:向订单表中插入元组");
        System.out.println("5:据序号删掉订单表一个元组");
        System.out.println("6:连接查询操作");//连接查询——有订单的医疗物资
        System.out.println("7:分组查询操作");//分组查询——根据地域将物流公司进行分组
        System.out.println("8:嵌套查询操作");//嵌套查询——具有单价小于50000的医疗物资的工厂
        System.out.println("9:配送员与车辆视图建立与查询");//建立并查询视图——关于配送员与车辆
        System.out.println("10:医院部分信息的视图建立与查询");//建立并查询视图——医院的部分信息
        System.out.println("11:仓库物资工厂负责人的视图建立与查询");//建立并查询视图——仓库，工厂，物资，负责人
        //System.out.println("10:trigger触发器");//触发器
        System.out.println("********************************************");
    }

    //选择（不用管）

    static void choose(int i) throws SQLException, ClassNotFoundException {
        String CIDin = null, Cname = null, SEID = null, Account = null, Phonenum = null, CIDde = null, CID = null;
        String OID = null;
        String MID = null;
        String HID = null;
        String OMON = null;
        String MTIME = null;
        Scanner sc3 = new Scanner(System.in);
        i = sc3.nextInt();
        switch (i) {
            case 0:
                System.out.println("Goodbye!");
                break;
            case 1:
                searchl_company();
                break;
            case 2:
                search_orderi();break;
            case 3:
                Searchby_Companyid(CID);break;
            case 4:
                insertOrder(OID,MID, HID, OMON, MTIME );
                break;
            case 5:
                deleteCompany(OID);
                break;
            case 6:
                supplies_in_order();
                break;
            case 7:
                search_company_GB();
                break;
            case 8:
                search_factory_NQ();
                break;
            case 9:
                Create_Search_View1();break;
            //case 10:
                //trigger_on_order();break;
            case 10:
                Create_Search_View2();break;
            case 11:
                Create_Search_View3();break;
            default:
                System.out.println("Please input again!");
                choose(i);
                break;
        }
    }

    //循环使用功能表（不用管）

    static void turn() throws SQLException, ClassNotFoundException {
        int i = 0;
        System.out.println("Press M To Menu,and press others to quit!");
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        if (str.equals("M") || str.equals("m")) {
            menu();
            choose(i);
        } else {
            System.out.println("Goodbye!");
        }
        return;
    }

    //查询所有物流公司

    public static void searchl_company() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            //查询语句是模式名.表名。。。否则报错
            sql = "SELECT c_id,c_name,c_address,c_phone FROM medicine_system.company";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int id = rs.getInt("c_id");
                String name = rs.getString("c_name");
                String address = rs.getString("c_address");
                String phone = rs.getString("c_phone");

                // 输出数据
                System.out.print("ID: " + id);
                System.out.print(", 姓名: " + name);
                System.out.print(",地址" + address);
                System.out.print(",电话" + phone);

                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();

            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //System.out.println("Goodbye!");
        System.out.println("本次表查询结束");
        System.out.println();
        turn();
    }

    //根据id查询——company表
    static void Searchby_Companyid(String CID) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Statement stmt = null;
        int i = 0;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("connecting database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("initiating a statement...");
            stmt = conn.createStatement();
            String sql = null;
            String sql2 = null;
            sql="select c_id from medicine_system.company";
            ResultSet res = stmt.executeQuery(sql);
            ArrayList cid = new ArrayList<>();
            while (res.next())
            {
                cid.add(res.getString("c_id"));
            }
            System.out.println("Please input the c_id:");
            Scanner sc2 = new Scanner(System.in);
            CID = sc2.nextLine();
            boolean contain = false;
            for(int j = 0;j<cid.size();j++)
            {
                if(cid.contains(CID))
                {   contain = true;

                    break;
                }
                else contain = false;break;
            }
            if(contain==true)
            {

                sql2= "SELECT c_id, c_name,c_address,c_phone  FROM medicine_system.company WHERE c_id='"+CID+"'";
                ResultSet rs = stmt.executeQuery(sql2);
                // 展开结果集数据库
                while (rs.next()) {
                    // 通过字段检索
                    int id = rs.getInt("c_id");
                    String name = rs.getString("c_name");
                    String address = rs.getString("c_address");
                    String phone = rs.getString("c_phone");

                    // 输出数据
                    System.out.print("ID: " + id);
                    System.out.print(", 姓名: " + name);
                    System.out.print(",地址" + address);
                    System.out.print(",电话" + phone);

                    System.out.print("\n");
                }
                // 完成后关闭
                rs.close();
            }
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //System.out.println("Goodbye!");
        System.out.println("本次根据id查询表结束");
        System.out.println();
        turn();
    }

    //向订单里添加元组
    static void insertOrder(String OID, String MID, String HID, String OMON, String MTIME) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Statement stmt = null;
        int i = 0;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("实例化Statement对象...");
            System.out.println("Please insert the data:");
            Scanner sc1 = new Scanner(System.in);
            String string = sc1.nextLine();
            String[] strings = string.split(",");

            OID = strings[0];
            MID = strings[1];
            HID = strings[2];
            OMON = strings[3];
            MTIME = strings[4];
            ArrayList<String> OIDlist = new ArrayList<>();
            ArrayList<String> MIDlist = new ArrayList<>();
            ArrayList<String> HIDlist = new ArrayList<>();
            ArrayList<String> OMONlist = new ArrayList<>();
            ArrayList<String> MTIMElist = new ArrayList<>();
            //Statement stmt = null;

            stmt = conn.createStatement();
            String sql = null;
            sql = "select * from medicine_system.order";
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                OIDlist.add(res.getString("o_id"));
                MIDlist.add(res.getString("o_m_id"));
                HIDlist.add(res.getString("o_h_id"));
                OMONlist.add(res.getString("o_money"));
                MTIMElist.add(res.getString("o_time"));
            }
            for (int j = 0; j < OIDlist.size(); j++) {
                if (OIDlist.contains(OID)) {
                    System.out.println("Data repeatException!");
                    reinsert();
                    break;
                }
            }

            String sql2 = "INSERT INTO medicine_system.order(o_id,o_m_id,o_h_id,o_money,o_time) VALUES (?,?,?,?,?)";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, OID);
            stmt2.setString(2, MID);
            stmt2.setString(3, HID);
            stmt2.setString(4, OMON);
            stmt2.setString(5, MTIME);
            int rs = stmt2.executeUpdate();
            System.out.println("Data null exception!");
            System.out.println("Insert successfully!");
            stmt.close();
            conn.close();
            turn();
        } catch (SQLException se) {
            se.printStackTrace();
            reinsert();
        }
    }
    static void reinsert() {
        String OID = null, MID = null, HID = null, MMON = null, MTIME = null;
        System.out.println("Please input again!");
        try {
            insertOrder(OID, MID, HID, MMON, MTIME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //删除操作

    static void deleteCompany(String OID) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int i = 0;
        Scanner sc2 = new Scanner(System.in); // 创建一个Scanner对象
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("connecting database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("initiating a statement...");
            pstmt = conn.prepareStatement("DELETE FROM medicine_system.order WHERE o_id = ?");
            String sql = "select o_id from medicine_system.order";
            ResultSet res = pstmt.executeQuery(sql);
            ArrayList<String> cid = new ArrayList<>();
            while (res.next()) {
                cid.add(res.getString("o_id"));
            }
            System.out.println("Please input the OID:");
            OID = sc2.nextLine();
            boolean contain = false;
            for (int j = 0; j < cid.size(); j++) {
                if (cid.get(j).equals(OID)) {
                    contain = true;
                    break;
                }
            }
            if (contain == true) {
                pstmt.setString(1, OID);
                int rs = pstmt.executeUpdate();
                System.out.println(rs);
                System.out.println("Delete successfully!");
            } else {
                System.out.println("Data not found!");
                redelete();
            }
            pstmt.close();
            conn.close();
            turn();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void redelete() throws SQLException, ClassNotFoundException {
        String CID = null;
        System.out.println("Please input again!");
        try {
            deleteCompany(CID);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //连接查询

    public static void supplies_in_order() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT m_id,m_name,m_price FROM medicine_system.medicine " +
                    "WHERE EXISTS (SELECT * FROM medicine_system.order WHERE medicine.m_id = order.o_m_id)";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int id = rs.getInt("m_id");
                String name = rs.getString("m_name");
                int price = rs.getInt("m_price");

                // 输出数据
                System.out.print("ID: " + id);
                System.out.print(", 名称: " + name);
                System.out.print(",单价:" + price);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();

            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //System.out.println("Goodbye!");
        System.out.println("本次连接查询结束");
        System.out.println();
        turn();
    }

    //分组查询

    public static void search_company_GB() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT c_address,COUNT(c_id) FROM medicine_system.company GROUP BY c_address";
            ResultSet rs = stmt.executeQuery(sql);
            // System.out.print("l_address    COUNT(l_id)");
            //System.out.print("\n");
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                String address = rs.getString("c_address");
                //int count = rs.getInt("count");
                // 输出数据
                System.out.print("厂家地址:"+address);
                //System.out.print(",数量:"+count);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();

            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //System.out.println("Goodbye!");
        System.out.println("本次分组查询结束");
        System.out.println();
        turn();
    }

    //套查询

    public static void search_factory_NQ() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT f_id,f_name,f_address FROM medicine_system.factor " +
                    "WHERE f_id IN(SELECT m_f_id FROM medicine_system.medicine WHERE m_price<50000 )";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int id = rs.getInt("f_id");
                String name = rs.getString("f_name");
                String address = rs.getString("f_address");
                // 输出数据
                System.out.print("编号：" + id);
                System.out.print(",名称：" + name);
                System.out.print(",地址：" + address);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();

            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //System.out.println("Goodbye!");
        System.out.println("本次嵌套查询结束");
        System.out.println();
        turn();
    }


    //建立并查询配送员与车辆视图
    public static void Create_Search_View1() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            //String sql1;
            String sql2;

            //sql1 = "CREATE VIEW p_car AS SELECT d_id,d_name,v_id,d_phone FROM medical_trade1.deliveryman,medical_trade1.vehicle WHERE medical_trade1.deliveryman.d_id=medical_trade1.vehicle.v_d_id";
            //stmt.executeUpdate(sql1);
            sql2 = "SELECT * FROM medicine_system.deliveryman_vehicle";
            ResultSet rs = stmt.executeQuery(sql2);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                String id = rs.getString("d_id");
                String name = rs.getString("d_name");
                String id2 = rs.getString("v_id");
                String phone = rs.getString("d_phone");
                // 输出数据
                System.out.print("配送员ID: " + id);
                System.out.print(", 姓名: " + name);
                System.out.print(",车辆ID:" + id2);
                System.out.print(",电话" + phone);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();

            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //System.out.println("Goodbye!");
        System.out.println("本次视图创建与查询查询结束");
        System.out.println();
        turn();
    }

    //单表查询订单
    public static void search_orderi() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql1;
            sql1 = "SELECT o_id,o_m_id,o_h_id,o_money,o_time FROM medicine_system.order";
            ResultSet rs = stmt.executeQuery(sql1);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                String id1 = rs.getString("o_id");
                int id2 = rs.getInt("o_m_id");
                int id3 = rs.getInt("o_h_id");
                int money = rs.getInt("o_money");
                String time = rs.getString("o_time");
                // 输出数据
                System.out.print("订单ID:" + id1);
                System.out.print(",物资ID:" + id2);
                System.out.print(",医院ID:" + id3);
                System.out.print(",总金额:" + money);
                System.out.print(",订单时间:"+time);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();

            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("本次订单信息查询结束！");
        turn();
    }

    //触发器
    static void trigger_on_order() throws SQLException, ClassNotFoundException {
        int i = 0;
        String sql = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("connecting database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("initiating a statement...");
            stmt = conn.createStatement();
            System.out.println("启动触发器!");
            sql = "CREATE TRIGGER  trig " +
                    "after INSER OR UPDATE ON `order` " +
                    "REFERENCING NEW row AS newTuple " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "IF(newTuple.o_money>100000) " +
                    "THEN SET newTuple.o_money=100000; " +
                    "END IF; " +
                    "END;";


            //System.out.println(sql);
            stmt.executeUpdate(sql);
            System.out.println("触发器已开启!");
            stmt.close();
            conn.close();
            turn();
        } catch (SQLException se) {
            //se.printStackTrace();
            System.out.println("创建触发器失败，错误信息为：" + se.getMessage());
            turn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //建立并查询视图  医院的部分信息
    public static void Create_Search_View2() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);


            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            //String sql1
            String sql2;

            //sql1 = "CREATE VIEW hospital_info AS SELECT h_name,h_address,h_phone FROM medicine_system.hospital";
            //stmt.executeUpdate(sql1);
            sql2 = "SELECT * FROM hospital_info";
            ResultSet rs = stmt.executeQuery(sql2);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int id = rs.getInt("h_id");
                String name = rs.getString("h_name");
                String address = rs.getString("h_address");
                //String phone = rs.getString("h_phone");
                // 输出数据
                System.out.print("医院编号:" + id);
                System.out.print(",医院名称:" + name);
                System.out.print(",医院地址:" + address);
                //System.out.print(",医院电话:" + phone);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();

            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //System.out.println("Goodbye!");
        System.out.println("本次视图创建与查询查询结束");
        System.out.println();
        turn();
    }

    //建立并查询视图
    public static void Create_Search_View3() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);


            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            //String sql1;
            String sql2;
//            sql1 = "CREATE VIEW warehouse_supplies_factory_person AS SELECT w_id,w_m_id,m_name,w_f_id,f_name,p_name " +
//                    "FROM medicine_system.warehouse,medicine_system.factor,medicine_system.medicine,medicine_system.person  " +
//                    "WHERE warehouse.w_m_id=medicine.m_id " +
//                    "AND warehouse.w_f_id=factor.f_id " +
//                    "AND person.p_w_id=warehouse.w_id";
//            stmt.executeUpdate(sql1);
            sql2 = "SELECT * FROM warehouse_supplies_factory_person";
            ResultSet rs = stmt.executeQuery(sql2);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                String id1 =rs.getString("w_id");
                String address = rs.getString("w_address");
                int id2 =rs.getInt("m_id");
                String name1 = rs.getString("m_name");
                int id3 = rs.getInt("f_id");
                String name2 = rs.getString("f_name");
                String name3 = rs.getString("p_name");
                // 输出数据
                System.out.print("仓库ID:" + id1);
                System.out.print("仓库地址:" + address);
                System.out.print(",物资ID:" + id2);
                System.out.print(",物资名称:" + name1);
                System.out.print(",工厂ID:" + id3);
                System.out.print(",工厂名称:" + name2);
                System.out.print(",负责人姓名:" + name3);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();

            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //System.out.println("Goodbye!");
        System.out.println("本次视图创建与查询结束");
        System.out.println();
        turn();
    }
}