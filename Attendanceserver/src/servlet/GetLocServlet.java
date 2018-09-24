package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.PrintWriter;
import DAO.LoginDAO;
import DAO.CourseDAO;
import DAOImpl.LoginDAOImpl;
import DAOImpl.CourseDAOImpl;
import VO.Course;
import VO.Location;

public class GetLocServlet extends HttpServlet {
public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{
	String Cid;
    Cid = req.getParameter("Cid");
	CourseDAO dao = new CourseDAOImpl();
	Location loc = new Location();
	PrintWriter out = res.getWriter();
	try{
		loc = dao.returnLocation(Cid);
		JSONObject result = new JSONObject();
		result.put("latitude", loc.getLatitude());
		result.put("longtitude", loc.getLongtitude());
		System.out.println(result.toString());
		out.println(result.toString());
	}catch(Exception e){
		e.printStackTrace();
	}
	out.close();
	
}
	
public void doPost(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{
            doGet(req,res);
}
}
