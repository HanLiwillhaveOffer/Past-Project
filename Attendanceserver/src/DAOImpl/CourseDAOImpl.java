package DAOImpl;

import java.awt.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import VO.Course;
import VO.Location;
import VO.Student;
import DAO.CourseDAO;
import DB.DBConnect;

public class CourseDAOImpl implements CourseDAO {
	int year;
	int month;
	int day;
	String date;
	public ArrayList<Course> returnCourse() throws Exception {
		ArrayList<Course> courses = new ArrayList<Course>();
		String sql = "select * from course where state=?";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
        //System.out.println("1");
		// 下面是针对数据库的具体操作
		try {
			// 连接数据库
			System.out.println("2");
			dbc = new DBConnect();
			if(dbc.getConnection()==null){
				System.out.println("dbc is null");
			}
			pstmt = dbc.getConnection().prepareStatement(sql);
			pstmt.setString(1, "1");
			// 进行数据库查询操作
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// 查询出内容，之后将查询出的内容赋值给person对象
				//System.out.println("3");
				Course c = new Course();
				c.setName(rs.getString("name"));
				c.setNumber(rs.getString("No"));
				c.setCid(rs.getString("id"));
                courses.add(c);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// 关闭数据库连接
			dbc.close();
		}
		return courses;
	}

	public int addCourse(Course c) throws Exception {
		int flag = 0;
        String addsql = "insert into course(name,No,Tid,state) values(?,?,?,?)";
        PreparedStatement addpstmt = null;
        DBConnect dbc = null;
		try {
			// 连接数据库

			dbc = new DBConnect();
            
			addpstmt = dbc.getConnection().prepareStatement(addsql);
			addpstmt.setString(1, c.getName());
			addpstmt.setString(2, c.getNumber());
			addpstmt.setString(3, c.getTid());
			addpstmt.setString(4, c.getState());
            if(!c.getName().equals("")&&!c.getTid().equals("")&&!c.getNumber().equals("")) {
				
				addpstmt.executeUpdate();
				flag = 1;
			}

			addpstmt.close();
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// 关闭数据库连接
			dbc.close();
			

		}return flag;
	}

	public ArrayList<Course> returnTCourse(String tid) throws Exception {
		ArrayList<Course> courses = new ArrayList<Course>();
		String sql = "select * from course where Tid=?";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
        //System.out.println("1");
		// 下面是针对数据库的具体操作
		try {
			// 连接数据库
			System.out.println("2");
			dbc = new DBConnect();
			if(dbc.getConnection()==null){
				System.out.println("dbc is null");
			}
			pstmt = dbc.getConnection().prepareStatement(sql);
			pstmt.setString(1,tid);
			// 进行数据库查询操作
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// 查询出内容，之后将查询出的内容赋值给person对象
				//System.out.println("3");
				Course c = new Course();
				c.setName(rs.getString("name"));
				c.setNumber(rs.getString("No"));
				c.setCid(rs.getString("id"));
				c.setState(rs.getString("state"));
                courses.add(c);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// 关闭数据库连接
			dbc.close();
		}
		return courses;
	}

	public String openCourse(String Cid,String Tid, String latitude, String longtitude) throws Exception {
		String mes = null;
		String state = null;
		int signflag = 0;
        String addsql = "update course set state=1 where id=?";
        String delsql = "delete from signin where (Date=? and Cid=?)";
        String signselsql = "select * from signin where (Date=? and Cid=?)";
        String sql = "select * from course where id=?";
        String sesql = "select * from tsignin where (Tid=? and Cid=?)";
        String tsql = "delete from tsignin where (Tid=? and Cid=?) ";
        String insql = "insert into tsignin(Date,Tid,Cid,latitude,longtitude) values(?,?,?,?,?)";
        Calendar now = Calendar.getInstance(); 
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH)+1; 
		day = now.get(Calendar.DATE); 
		date = year + "-" +month+"-"+day;
        PreparedStatement openpstmt = null;
        PreparedStatement selpstmt = null;
        PreparedStatement tpstmt = null;
        PreparedStatement ipstmt = null;
        PreparedStatement sepstmt = null;
        PreparedStatement delpstmt = null;
        PreparedStatement signselpstmt = null;
        DBConnect dbc = null;
		try {
			// 连接数据库

			dbc = new DBConnect();
			selpstmt = dbc.getConnection().prepareStatement(sql);
			openpstmt = dbc.getConnection().prepareStatement(addsql);
			sepstmt = dbc.getConnection().prepareStatement(sesql);
			tpstmt = dbc.getConnection().prepareStatement(tsql);
			ipstmt = dbc.getConnection().prepareStatement(insql);
			delpstmt = dbc.getConnection().prepareStatement(delsql);
			signselpstmt = dbc.getConnection().prepareStatement(signselsql);
			delpstmt.setString(1,date);
			delpstmt.setString(2,Cid);
			signselpstmt.setString(1, date);
			signselpstmt.setString(2, Cid);
			ResultSet signrs = signselpstmt.executeQuery();
			while(signrs.next()){
				signflag = 1;
			}
			sepstmt.setString(1,Tid);
			sepstmt.setString(2,Cid);
			openpstmt.setString(1, Cid);
			selpstmt.setString(1,Cid);
			tpstmt.setString(1, Tid);
			tpstmt.setString(2,Cid);
			ipstmt.setString(1, date);
			ipstmt.setString(2, Tid);
			ipstmt.setString(3, Cid);
			ipstmt.setString(4, latitude);
			ipstmt.setString(5, longtitude);
			ResultSet rs = selpstmt.executeQuery();
			while(rs.next()){
				state=rs.getString("state");
			}
			System.out.println(latitude);
			ResultSet sers = sepstmt.executeQuery();
			while(sers.next()&&state.equals("0")){
				tpstmt.executeUpdate();
			}
			
	        if(signflag==1){
	        	delpstmt.executeUpdate();
	        }
            if(state.equals("0")) {
				openpstmt.executeUpdate();
				ipstmt.executeUpdate();
				mes ="open successfully";
			}else{
				mes = "already open";
			}
            sepstmt.close();
			signselpstmt.close();
            selpstmt.close();
			openpstmt.close();
			delpstmt.close();
		    tpstmt.close();
		    ipstmt.close();
		    sers.close();
		    signrs.close();
		    rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// 关闭数据库连接
			dbc.close();
			

		}return mes;
	}

	public String closeCourse(String Cid) throws Exception {
		String mes = null;
        String addsql = "update course set state=0 where id=?";
        String sql = "select * from course where id=?";
        PreparedStatement closepstmt = null;
        PreparedStatement selpstmt = null;
        DBConnect dbc = null;
		try {
			// 连接数据库

			dbc = new DBConnect();
			selpstmt = dbc.getConnection().prepareStatement(sql);
			closepstmt = dbc.getConnection().prepareStatement(addsql);
			closepstmt.setString(1, Cid);
			selpstmt.setString(1,Cid);
			ResultSet rs = selpstmt.executeQuery();
			while(rs.next()){
            if(rs.getString("state").equals("1")) {
				
            	closepstmt.executeUpdate();
				mes ="close successfully";
			}else{
				mes = "already closed";
			}}
            selpstmt.close();
			closepstmt.close();
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// 关闭数据库连接
			dbc.close();
			

		}return mes;
	}

	public Location returnLocation(String Cid) throws Exception {
		Location loc = new Location();
		String getLocsql = "select * from tsignin where Cid=?";
		PreparedStatement getLpstmt = null;
		DBConnect dbc = null;
		loc.setLatitude("0");
		loc.setLongtitude("0");
		try{
			loc.setFlag(0);
			dbc = new DBConnect();
			getLpstmt = dbc.getConnection().prepareStatement(getLocsql);
			getLpstmt.setString(1, Cid);
			ResultSet getLrs = getLpstmt.executeQuery();
			while(getLrs.next()){
                System.out.println("999");
				loc.setLatitude(getLrs.getString("latitude"));
				loc.setLongtitude(getLrs.getString("longtitude"));
				}
			getLrs.close();
			getLpstmt.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return loc;
	}
         
}
