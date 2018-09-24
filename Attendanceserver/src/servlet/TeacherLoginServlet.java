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
import DAOImpl.LoginDAOImpl;
import VO.Teacher;

public class TeacherLoginServlet extends HttpServlet {
public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{
	Teacher t = new Teacher();
	PrintWriter out = res.getWriter();
	t.setName(req.getParameter("Teachername"));
	t.setTpwd(req.getParameter("password"));
	LoginDAO dao = new LoginDAOImpl();
	int flag = 0;
	String Tid = null;
	JSONObject FLAG = new JSONObject();
	JSONObject TID = new JSONObject();
	JSONArray result = new JSONArray();
	try{
		Tid = dao.queryByTname(t);
		if(Tid!=null){
			flag = 1;
			System.out.println("Tid is"+Tid);
		}
		System.out.println(flag);
		FLAG.put("flag", flag);
		TID.put("Tid", Tid);
		result.add(FLAG);
		result.add(TID);
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