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

public class ChangePWServlet extends HttpServlet {
public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{
	PrintWriter out = res.getWriter();
     String username = req.getParameter("username");
     String oldpw = req.getParameter("oldpassword");
     String newpw = req.getParameter("newpassword");
     System.out.println(username+oldpw+newpw);
	LoginDAO dao = new LoginDAOImpl();
	int flag = 2;
	try{
		if(username.equals("")||oldpw.equals("")||newpw.equals("")){
		flag = 0;
		}else{
			flag = dao.changePW(username,oldpw,newpw);
		}
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