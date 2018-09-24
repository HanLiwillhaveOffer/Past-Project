package DAOImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Calendar;  
import java.util.Date;  

import VO.Course;
import DAO.SigninDAO;
import DB.DBConnect;
import VO.Location;

public class SigninDAOImpl implements SigninDAO {
	String Sname;
	int year;
	int month;
	int day;
	String date;
	public Location Signin(String Sno, Course c,String mac) throws Exception {
		String state = "";
		Calendar now = Calendar.getInstance(); 
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH)+1; 
		day = now.get(Calendar.DATE); 
		date = year + "-" +month+"-"+day;
		System.out.println(date);
		Location loc = new Location();
		loc.setFlag(0);
		String getMacsql = "select * from signin where MAC=? and Cid=? and Date=?";
		String getSinfosql = "select * from signin where Cid=? and Sno=? and Date=?";
		String getSnamesql ="select * from sinfo where Sno=?";
		String getStatesql= "select * from course where id=?";
		String signinsql = "insert into signin(Sname,Sno,Cname,Cno,Date,Cid,MAC) values(?,?,?,?,?,?,?)";
		PreparedStatement getSpstmt = null;
		PreparedStatement Insertpstmt = null;
		PreparedStatement getStatepstmt = null;
		PreparedStatement getMpstmt = null;
		PreparedStatement getSinfopstmt = null;
		DBConnect dbc = null;
        System.out.println("1");
		// 下面是针对数据库的具体操作
		try {
			// 连接数据库
			
			System.out.println("5");
			dbc = new DBConnect();
			if(dbc.getConnection()==null){
				System.out.println("dbc is null");
			}
			getSpstmt = dbc.getConnection().prepareStatement(getSnamesql);
			getSpstmt.setString(1, Sno);
			// 进行数据库查询操作
			ResultSet rs = getSpstmt.executeQuery();
			while (rs.next()) {
				// 查询出内容，之后将查询出的内容赋值给person对象
				Sname = rs.getString("Sname");
				System.out.println(Sname+"111");
                }			
			rs.close();
			getSpstmt.close();
			getSinfopstmt = dbc.getConnection().prepareStatement(getSinfosql);
			getStatepstmt = dbc.getConnection().prepareStatement(getStatesql);
			getMpstmt = dbc.getConnection().prepareStatement(getMacsql);
    		Insertpstmt = dbc.getConnection().prepareStatement(signinsql);
    		getSinfopstmt.setString(1, c.getCid());
    		getSinfopstmt.setString(2, Sno);
    		getSinfopstmt.setString(3, date);
    		getStatepstmt.setString(1, c.getCid());
    		Insertpstmt.setString(1, Sname);
    		Insertpstmt.setString(2, Sno);
    		Insertpstmt.setString(3, c.getName());
    		Insertpstmt.setString(4, c.getNumber());
    		Insertpstmt.setString(5, date);
    		Insertpstmt.setString(6, c.getCid());
    		Insertpstmt.setString(7, mac);
    		getMpstmt.setString(1, mac);
    		getMpstmt.setString(2, c.getCid());
    		getMpstmt.setString(3, date);
			ResultSet getSrs = getStatepstmt.executeQuery();
			ResultSet getSinfors = getSinfopstmt.executeQuery();
			while(getSrs.next()){
				state = getSrs.getString("state");
			}
			ResultSet getMrs = getMpstmt.executeQuery();
			
			if(getMrs.next()){
				loc.setFlag(2);
			}
			else if(getSinfors.next()){
				loc.setFlag(4);
			}
			else if(!getSinfors.next()&&!getMrs.next()&&state.equals("0")){
				loc.setFlag(3);
			}
			else if(!getSinfors.next()&&!getMrs.next()&&state.equals("1")){
				System.out.println("888");
                if(Sname!=null&&c.getCid()!=null){
                System.out.println("77");
				loc.setFlag(1);
				Insertpstmt.executeUpdate();
                
			}}

			getStatepstmt.close();    
			getMrs.close();
			getMpstmt.close();
			Insertpstmt.close();
			
              
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// 关闭数据库连接
			dbc.close();
		}
		return loc;
	}

}
