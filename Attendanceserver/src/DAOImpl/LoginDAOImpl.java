package DAOImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.PrintWriter;


import DAO.LoginDAO;
import DB.DBConnect;
import VO.Student;
import VO.Teacher;
import Encryption.Encryption;
import java.security.*;

public class LoginDAOImpl implements LoginDAO {

	public int queryBySno(Student s) throws Exception {
		// TODO Auto-generated method stub
		int flag = 0;
		String sql = "select * from sinfo where Sno=?";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
        //System.out.println("1");
		// ������������ݿ�ľ������
		try {
			Encryption e = new Encryption();
			String encry = e.sign("123", "SHA-1");
			System.out.println(encry);
			// �������ݿ�
			System.out.println("2");
			dbc = new DBConnect();
			if(dbc.getConnection()==null){
				System.out.println("dbc is null");
			}
			pstmt = dbc.getConnection().prepareStatement(sql);
			pstmt.setString(1, s.getSno());
			// �������ݿ��ѯ����
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// ��ѯ�����ݣ�֮�󽫲�ѯ�������ݸ�ֵ��person����
				//System.out.println("3");
				if (rs.getString("password").equals(e.sign(s.getPassword(), "SHA-1"))) {
					flag = 1;

				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// �ر����ݿ�����
			dbc.close();
		}
		return flag;
	}



	public String queryByTname(Teacher t) throws Exception {
		String Tid = null;
		String sql = "select * from teacher where Tname=?";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
        System.out.println("1");
		// ������������ݿ�ľ������
		try {
			// �������ݿ�
			Encryption e = new Encryption();
			String pw = e.sign(t.getTpwd(), "SHA-1");
			System.out.println(t.getTpwd()+pw);
			dbc = new DBConnect();
			if(dbc.getConnection()==null){
				System.out.println("dbc is null");
			}
			pstmt = dbc.getConnection().prepareStatement(sql);
			pstmt.setString(1, t.getName());
			// �������ݿ��ѯ����
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// ��ѯ�����ݣ�֮�󽫲�ѯ�������ݸ�ֵ��person����
				System.out.println("3");
				if (rs.getString("Tpassword").equals(e.sign(t.getTpwd(), "SHA-1"))) {
					Tid = rs.getString("Tid");

				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// �ر����ݿ�����
			dbc.close();
		}
		return Tid;
	}

	public int changePW(String username, String oldpw, String newpw)
			throws Exception {
		int flag = 0;
		String ssql = "select * from teacher where Tname=?";
		String cgsql = "update teacher set Tpassword=? where Tname=?";
		PreparedStatement spstmt = null;
		PreparedStatement cgpstmt = null;
		DBConnect dbc = null;
		try {
			// �������ݿ�
			Encryption e = new Encryption();
			dbc = new DBConnect();
			if(dbc.getConnection()==null){
				System.out.println("dbc is null");
			}
			spstmt = dbc.getConnection().prepareStatement(ssql);
			cgpstmt = dbc.getConnection().prepareStatement(cgsql);
			spstmt.setString(1, username);
			cgpstmt.setString(1, e.sign(newpw, "SHA-1"));
			cgpstmt.setString(2, username);
			// �������ݿ��ѯ����
			ResultSet rs = spstmt.executeQuery();
			while (rs.next()) {
				// ��ѯ�����ݣ�֮�󽫲�ѯ�������ݸ�ֵ��person����
				if (rs.getString("Tpassword").equals(e.sign(oldpw, "SHA-1"))) {
					cgpstmt.executeUpdate();
					flag = 1;

				}
			}
			rs.close();
			spstmt.close();
			cgpstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// �ر����ݿ�����
			dbc.close();
		}
		return flag;
	}
}
