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
import DAO.RecordDAO;
import DAOImpl.CourseDAOImpl;
import DAOImpl.LoginDAOImpl;
import DAOImpl.RecordDAOImpl;
import VO.Course;
import VO.Record;
import VO.Date;

public class RecordServlet extends HttpServlet {
public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{
	String Cid = req.getParameter("Cid");
	String code = req.getParameter("code");
    res.setCharacterEncoding("UTF-8");
    res.setContentType("application/json; charset=utf-8");
	PrintWriter out = res.getWriter();
	RecordDAO dao = new RecordDAOImpl();
	JSONArray r = new JSONArray();
	ArrayList<Record> records = new ArrayList<Record>();
	try{
		if(code.equals("current")){
			records = dao.returnCurrentRecords(Cid);
		}else{
			String year = req.getParameter("year");
			String month = req.getParameter("month");
			String day = req.getParameter("day");
			Date date = new Date(year,month,day);
		records = dao.returnRecords(Cid,date);}
		for(int i=0;i<records.size();i++){
			JSONObject record = new JSONObject();
			record.put("Sname", records.get(i).getSname());
			record.put("Sno", records.get(i).getSno());
			System.out.println("jsonObject==>"+record);   
			r.add(record);
			
		}
		out.println(r.toString());
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
