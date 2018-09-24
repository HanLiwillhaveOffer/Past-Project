package DAO;

import VO.Student;
import VO.Teacher;

public interface LoginDAO {
public int queryBySno(Student s) throws Exception;
public String queryByTname(Teacher t) throws Exception;
public int changePW(String username, String oldpw, String newpw) throws Exception;

}
