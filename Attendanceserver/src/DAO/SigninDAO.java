package DAO;

import VO.Course;
import VO.Location;
public interface SigninDAO {
public Location Signin(String Sno, Course c,String mac) throws Exception;
}
