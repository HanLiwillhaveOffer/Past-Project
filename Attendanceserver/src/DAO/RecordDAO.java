package DAO;
import java.awt.List;
import java.util.ArrayList;

import VO.Course;
import VO.Student;
import VO.Location;
import VO.Record;
import VO.Date;
public interface RecordDAO {
	public ArrayList<Record> returnRecords(String Cid, Date date) throws Exception;
	public ArrayList<Record> returnCurrentRecords(String Cid) throws Exception;
}
