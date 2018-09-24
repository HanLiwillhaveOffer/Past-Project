package DAOImpl;
import java.awt.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import VO.Course;
import VO.Location;
import VO.Student;
import VO.Date;
import VO.Record;
import DAO.CourseDAO;
import DAO.RecordDAO;
import DB.DBConnect;
public class RecordDAOImpl implements RecordDAO {

	public ArrayList<Record> returnRecords(String Cid, Date date)
			throws Exception {
		String Date;
		String year = date.getYear();
		String month = date.getMonth();
		String day = date.getDay();
		Date = year + "-" +month+"-"+day;
		ArrayList<Record> records = new ArrayList<Record>();
		String sql = "select * from signin where Cid=? and Date=?";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		try {
			// �������ݿ�
			dbc = new DBConnect();
			if(dbc.getConnection()==null){
				System.out.println("dbc is null");
			}
			pstmt = dbc.getConnection().prepareStatement(sql);
			pstmt.setString(1, Cid);
			pstmt.setString(2, Date);
			// �������ݿ��ѯ����
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// ��ѯ�����ݣ�֮�󽫲�ѯ�������ݸ�ֵ��person����
				//System.out.println("3");
				Record r = new Record();
                r.setSname(rs.getString("Sname"));
                r.setSno(rs.getString("Sno"));
                records.add(r);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// �ر����ݿ�����
			dbc.close();
		}
		return records;
	}

	public ArrayList<Record> returnCurrentRecords(String Cid) throws Exception {
		String Date;
		Calendar now = Calendar.getInstance(); 
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH)+1; 
		int day = now.get(Calendar.DATE); 
		Date = year + "-" +month+"-"+day;
		ArrayList<Record> records = new ArrayList<Record>();
		String sql = "select * from signin where Cid=? and Date=?";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		try {
			// �������ݿ�
			dbc = new DBConnect();
			if(dbc.getConnection()==null){
				System.out.println("dbc is null");
			}
			pstmt = dbc.getConnection().prepareStatement(sql);
			pstmt.setString(1, Cid);
			pstmt.setString(2, Date);
			// �������ݿ��ѯ����
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// ��ѯ�����ݣ�֮�󽫲�ѯ�������ݸ�ֵ��person����
				//System.out.println("3");
				Record r = new Record();
                r.setSname(rs.getString("Sname"));
                r.setSno(rs.getString("Sno"));
                records.add(r);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// �ر����ݿ�����
			dbc.close();
		}
		return records;
	}

}
