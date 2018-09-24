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

import DAO.CourseDAO;
import DAO.LoginDAO;
import DAOImpl.CourseDAOImpl;
import DAOImpl.LoginDAOImpl;
import VO.Course;

public class ProcessCourseServlet extends HttpServlet {
public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{
    String signal = req.getParameter("signal");
    String Cid = req.getParameter("Cid");
    String Tid = req.getParameter("Tid");
    String latitude = req.getParameter("latitude");
    String longtitude = req.getParameter("longtitude");
    System.out.println(signal);
	CourseDAO dao = new CourseDAOImpl();
	String mes = null;
	PrintWriter out = res.getWriter();
    try{
      if(signal.equals("1")){
    	  System.out.println("i'm opening course");
    	  mes = dao.openCourse(Cid,Tid,latitude,longtitude);
    	  System.out.println(mes);
      }else{
    	  mes= dao.closeCourse(Cid);
      }
      
	  out.println(mes);
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