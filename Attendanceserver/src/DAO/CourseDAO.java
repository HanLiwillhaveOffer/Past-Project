package DAO;
import java.awt.List;
import java.util.ArrayList;

import VO.Course;
import VO.Student;
import VO.Location;
public interface CourseDAO {
public ArrayList<Course> returnCourse() throws Exception;
public int addCourse(Course c)throws Exception;
public ArrayList<Course> returnTCourse(String tid) throws Exception;
public String openCourse(String Cid, String Tid, String latitude, String longtitude)throws Exception;
public String closeCourse(String Cid)throws Exception;
public Location returnLocation(String Cid) throws Exception;
}

