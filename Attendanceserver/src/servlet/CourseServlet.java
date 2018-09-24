package servlet;

import java.awt.List;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.PrintWriter;
import java.util.ArrayList;

import DAO.CourseDAO;
import DAO.LoginDAO;
import DAOImpl.CourseDAOImpl;
import DAOImpl.LoginDAOImpl;
import VO.Course;

public class CourseServlet extends HttpServlet {
public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{
    res.setCharacterEncoding("UTF-8");
    res.setContentType("application/json; charset=utf-8");
	PrintWriter out = res.getWriter();
	CourseDAO dao = new CourseDAOImpl();
	JSONArray c = new JSONArray();
	ArrayList<Course> courses = new ArrayList<Course>();
	try{
		System.out.println("i'M IN");
		courses = dao.returnCourse();
		for(int i=0;i<courses.size();i++){
			JSONObject course = new JSONObject();
			course.put("name", courses.get(i).getName());
			course.put("No", courses.get(i).getNumber());
			course.put("ID", courses.get(i).getCid());
			System.out.println("jsonObject==>"+course);   
			System.out.println(course.getString("name"));
			c.add(course);
			
		}
		out.println(c.toString());
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