package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.PrintWriter;

import DAO.CourseDAO;
import DAOImpl.CourseDAOImpl;
import VO.Course;

public class AddCourseServlet extends HttpServlet {
public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{
	Course c = new Course();
	PrintWriter out = res.getWriter();
    c.setName(req.getParameter("coursename"));
    c.setNumber(req.getParameter("courseno"));
    c.setState("0");
    c.setTid(req.getParameter("Tid"));
	CourseDAO dao = new CourseDAOImpl();
	int flag = 0;
    try{
      flag = dao.addCourse(c);
	  out.println(flag);
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