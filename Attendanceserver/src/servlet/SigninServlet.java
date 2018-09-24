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
import DAO.SigninDAO;
import DAOImpl.LoginDAOImpl;
import DAOImpl.SigninDAOImpl;
import VO.Course;
import VO.Location;

public class SigninServlet extends HttpServlet {
public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{
	String Sno;
	double dis;
	String MAC = req.getParameter("MAC");
	Course c = new Course();
	PrintWriter out = res.getWriter();
    c.setCid(req.getParameter("Cid"));
    c.setName(req.getParameter("coursename"));
    c.setNumber(req.getParameter("courseno"));
    dis = Double.valueOf(req.getParameter("dis"));
    System.out.println("distance is"+dis);
    Sno = req.getParameter("studentnumber");
    System.out.println(Sno+c.getCid());
	SigninDAO dao = new SigninDAOImpl();
	Location loc = new Location();
	try{
		if((int)dis>=30){
			loc.setFlag(0);
		}
		else{
		System.out.println(dis+ "<20");
		loc = dao.Signin(Sno,c,MAC);}
        System.out.println(loc.getFlag());
		out.println(loc.getFlag());
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
