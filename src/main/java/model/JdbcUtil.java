package model;

import bean.User;

import java.sql.*;

public class JdbcUtil {
    static final String drive = "com.mysql.jdbc.Driver";
    static final String url = "jdbc:mysql://localhost:3306/123";
    static final String username = "root";
    static final String password = "password";
    static Connection connection = null;
    private String sql;

    //连接数据库
    public void getConnection() {
        try {
            Class.forName(drive);
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据库连接失败！");
        }
    }

    //关闭数据库连接
    public void closeCon() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("数据库连接已经关闭！");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param dataTable 要连接的表
     * @param args 表的列
     * @param selectAll 否得到所有数据
     * @param start 开始
     * **/
    //数据库查询方法
    public ResultSet select(String dataTable, int start, boolean selectAll, String... args) {
        String str = String.join(",",args);
        String sql;
        if(selectAll){
            sql="select "+str+" from "+dataTable;
        }else {
            sql="select "+str+" from "+dataTable+" limit "+start+",40";
        }
        System.out.println(sql);
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 得到productions表中的所有数据
     * **/
    public ResultSet getAllData(){
        String sql = "select * from productions";
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 判断用户是否存在
     * **/
    public boolean hasUser(String user){
        String sql = "select user from user where user="+user;
        Statement stmt;
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.isBeforeFirst()){
                rs.next();
                System.out.println(rs.getString(1)+"该用户存在");
                return true;//该用户存在
            }else {
                System.out.println("用户不存在");
                return false;//用户不存在
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("出现错误,默认返回false");
        return false;
    }

    /**
     * 核验密码
     * **/
    public boolean verifyPwd(String user,String password) {
        String sql = "select password from user where user="+user;
        Statement stmt;
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()){
                String pwd=User.getTurePassword(rs.getString(1));
                if(pwd.equals(password)){
                    System.out.println("得到的密码"+password+"输入的密码"+pwd);
                    return true;//密码正确
                }else {
                    System.out.println(password+"输入的密码"+rs.getString(1));
                    return false;//密码不正确
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("默认返回错误");
        return false;
    }

    /**
     * 得到某一用户的购物车数据
     * **/
    public ResultSet getTrolley(String user,String table){
        Statement stmt;
        ResultSet rs = null;

        String sql="SELECT * from productions " +
                "WHERE productions.id IN (" +
                "SELECT id from "+
                table+
                " WHERE "+table+".user="+user+")";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * @param table 表
     * @param rules 分组依据
     * **/
    public ResultSet getGroupedInfo(String table,String rules){
        Statement stmt;
        ResultSet rs = null;

        String sql="SELECT "+rules+" from "+ table +
                " GROUP BY "+rules;
        System.out.println(sql);
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getEqual(String table,String equal,String equal2){
        Statement stmt;
        ResultSet rs = null;

        String sql="SELECT * from productions WHERE id "+
                "IN (SELECT proid FROM "+ table +
                " WHERE "+equal +"="+equal2+")";
        System.out.println(sql);
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public int insertData(String table,String[] field,String[] value){
        sql= "INSERT INTO "+
                table+" ("+String.join(",",field)+") "+ "value ("+String.join(",",value)+")" ;
        System.out.println(String.join(",",value));
        return handleUpdate();
    }

    private int handleUpdate() {
        System.out.println(sql);
        Statement stmt;
        int rst=0 ;
        try {
            stmt = connection.createStatement();
            rst=stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rst;
    }

    public int deleteData(String table,String id) {
        sql="DELETE FROM "+table + " WHERE id="+id;
        return handleUpdate();
    }

    public int updateData(String table,String id ,String[] field,String[] value){
        StringBuilder new_sql= new StringBuilder();
        for(int i=0;i<field.length;i++){
             new_sql.append(field[i]).append("=").append(value[i]);
             if(i!= field.length-1){
                 new_sql.append(",");
             }
        }
        System.out.println(new_sql);
        sql="UPDATE `"+table+"`" +
                "SET "+new_sql +
                "WHERE id="+id;
        Statement stmt;
        int rst=0 ;
        try {
            stmt = connection.createStatement();
            rst=stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rst;
    }

    public String statisticData(String table){
        sql="SELECT COUNT(*) as num FROM "+table;

        return getStatistic();
    }

    public String statisticData(String table,String user){
        sql="SELECT COUNT(*) as num FROM "+table+" WHERE user= "+ user;

        return getStatistic();
    }

    private String getStatistic() {
        Statement stmt;
        ResultSet rs;

        String res="0";

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                res=rs.getString("num");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}
