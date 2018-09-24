package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import DAO.LoginDAO;
import DAOImpl.LoginDAOImpl;
import VO.Student;

public class loginServlet extends HttpServlet {
public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{
	Student s = new Student();
	PrintWriter out = res.getWriter();
	s.setSno(req.getParameter("studentnumber"));
	s.setPassword(req.getParameter("password"));
	 System.out.println(s.getSno());
	 System.out.println("i");
	LoginDAO dao = new LoginDAOImpl();
	int flag = 0;
	try{
		flag = dao.queryBySno(s);
		System.out.println(flag);
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
