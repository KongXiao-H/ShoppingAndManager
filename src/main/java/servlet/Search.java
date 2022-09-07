package servlet;

import com.alibaba.fastjson.JSONObject;
import model.JdbcUtil;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import bean.Merchandise;

@WebServlet("/search")
public class Search extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setHeader("Access-Control-Allow-Origin","http://localhost:3000");

        String key=req.getParameter("key");

        JdbcUtil jdbcUtil=new JdbcUtil();
        jdbcUtil.getConnection();
        JSONObject jo=new JSONObject();
        List<Object> list=new ArrayList<>();
        ResultSet rs= jdbcUtil.getAllData();

        try {
            while (rs.next()) {
                String pattern   = ".*"+key+".*";
                if(Pattern.matches(pattern, rs.getString("des"))){
                    Merchandise merchandise=new Merchandise(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
                    list.add(merchandise);
                    System.out.println(merchandise);
                }
            }
            jo.put("result",list);
            System.out.println(jo);
            resp.getWriter().print(jo);
        }catch (Exception e){System.out.println(e);}

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req,resp);
    }
}
