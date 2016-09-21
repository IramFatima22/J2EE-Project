package com.uic.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.custom.fileupload.UploadedFile;


@ManagedBean
@SessionScoped
public class ActionBeanInstructor<HttpServletResponse> {

	private UploadedFile uploadedFile;
	private String fileName;
	private String dataType;
	private String fileType = null;
	private Date dueDateTime = new Date();
	private boolean renderSetDate = false;
	private boolean renderAssessmentDetails=false;
	private String message;
	private ArrayList<CourseBean> courseList;
	private ArrayList<CourseBean> sectionList;
	private String courseId;
	private String courseName;
	private CourseBean courseBean;
	private boolean renderCourseList = false;
	private boolean renderCourseDetails = false;
	private boolean renderIsAdmin=false;
	private LoginBean loginBean2;
	private DBConnection con;
	private ResultSet rs;
	private ArrayList<StudentBean> student_list;
	private ArrayList<AssessmentBean> assessmentList;
	private boolean renderStudentDetails;
	private boolean renderSection;
	private Date dueDate;
	private boolean renderCalendar=false;
	//private String student_array[][];
	private Map<Long, Boolean> checked = new HashMap<Long, Boolean>();
	private ArrayList<Double> assessmentValues;
	private double[] notTakenPerQues;
	private double[] correctPerQues;
	private double[] wrongPerQues;
	private double[] score;
	private int totalStudents;
	private int totalStudentsTaken;
	private HtmlDataTable dataTable;
	private AssessmentBean dataItemAB=new AssessmentBean();
	private FacesContext context;
	private int duration;
	private int totalMarks;
	private String role;
	
	

	public ArrayList<CourseBean> getSectionList() {
		sectionList = new ArrayList<CourseBean>();
		CourseBean courseBean;
		try {

			String sQuery = "select courseid, section from f15g107_course_table where courseName='"+courseName+"' and courseId in(select"
					+ " courseid from f15g107_user_course_table where userid='" + loginBean2.getUserId() + "')";
			con = new DBConnection();
			con.createConnection();
			rs = con.execQuery(sQuery);
			while (rs.next()) {
				courseBean = new CourseBean();
				courseBean.setCourseId(rs.getString(1));
				courseBean.setSection(rs.getString(2));
				sectionList.add(courseBean);
			}
			con.connClose();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return sectionList;
	}

	

	public double[] getNotTakenPerQues() {
		return notTakenPerQues;
	}



	public int getDuration() {
		duration=0;
		return duration;
	}



	public String getRole() {
		if(loginBean2.getRole().equalsIgnoreCase("TA"))
		{
			this.role="TA";
		}
		else
		{
			this.role="Instructor";
		}
		return role;
	}



	public void setRole(String role) {
		this.role = role;
	}



	public void setDuration(int duration) {
		this.duration = duration;
	}



	public void setNotTakenPerQues(double[] notTakenPerQues) {
		this.notTakenPerQues = notTakenPerQues;
	}



	public double[] getCorrectPerQues() {
		return correctPerQues;
	}



	public void setCorrectPerQues(double[] correctPerQues) {
		this.correctPerQues = correctPerQues;
	}



	public double[] getWrongPerQues() {
		return wrongPerQues;
	}



	public void setWrongPerQues(double[] wrongPerQues) {
		this.wrongPerQues = wrongPerQues;
	}



	public int getTotalStudentsTaken() {
		return totalStudentsTaken;
	}


	public void setTotalStudentsTaken(int totalStudentsTaken) {
		this.totalStudentsTaken = totalStudentsTaken;
	}


	public ArrayList<Double> getAssessmentValues() {
		return assessmentValues;
	}


	public void setAssessmentValues(ArrayList<Double> assessmentValues) {
		this.assessmentValues = assessmentValues;
	}


	public int getTotalStudents() {
		return totalStudents;
	}


	public void setTotalStudents(int totalStudents) {
		this.totalStudents = totalStudents;
	}


	public HtmlDataTable getDataTable() {
		return dataTable;
	}


	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	
	public AssessmentBean getDataItemAB() {
		return dataItemAB;
	}


	public void setDataItemAB(AssessmentBean dataItemAB) {
		this.dataItemAB = dataItemAB;
	}


	public void setSectionList(ArrayList<CourseBean> sectionList) {
		this.sectionList = sectionList;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public boolean isRenderSection() {
		return renderSection;
	}

	public void setRenderSection(boolean renderSection) {
		this.renderSection = renderSection;
	}

	public boolean isRenderCalendar() {
		return renderCalendar;
	}

	public void setRenderCalendar(boolean renderCalendar) {
		this.renderCalendar = renderCalendar;
	}

	public Date getDueDate() {
		dueDate=new Date();
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		
		this.dueDate = dueDate;
	}

	public ArrayList<StudentBean> getStudent_list() {
		return student_list;
	}

	public void setStudent_list(ArrayList<StudentBean> student_list) {
		this.student_list = student_list;
	}

	public boolean isRenderStudentDetails() {
		return renderStudentDetails;
	}

	public void setRenderStudentDetails(boolean renderStudentDetails) {
		this.renderStudentDetails = renderStudentDetails;
	}

	public boolean isRenderAssessmentDetails() {
		return renderAssessmentDetails;
	}

	public void setRenderAssessmentDetails(boolean renderAssessmentDetails) {
		this.renderAssessmentDetails = renderAssessmentDetails;
	}

	public Map<Long, Boolean> getChecked() {
		return checked;
	}

	public void setChecked(Map<Long, Boolean> checked) {
		this.checked = checked;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Date getDueDateTime() {
		return dueDateTime;
	}

	public void setDueDateTime(Date dueDateTime) {
		this.dueDateTime = dueDateTime;
	}

	public boolean isRenderSetDate() {
		if (fileType != null || !fileType.isEmpty()) {
			renderSetDate = true;
		}
		return renderSetDate;
	}

	public void setRenderSetDate(boolean renderSetDate) {

		this.renderSetDate = renderSetDate;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public String getFileName() {
		fileName="";
		return fileName;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String showDatePicker() {
		renderSetDate = true;
		return "Success";
	}

		@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		loginBean2 = (LoginBean) m.get("loginBean1");
		renderSetDate = false;
	}

	public boolean isRenderIsAdmin() {
		if(loginBean2.getRole().equalsIgnoreCase("admin"))
		{
			renderIsAdmin=true;
		}
		return renderIsAdmin;
	}

	public void setRenderIsAdmin(boolean renderIsAdmin) {
		this.renderIsAdmin = renderIsAdmin;
	}

	public LoginBean getLoginBean2() {
		return loginBean2;
	}

	public void setLoginBean2(LoginBean loginBean2) {
		this.loginBean2 = loginBean2;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
		renderSetDate = true;
	}

	public boolean isRenderCourseDetails() {
		return renderCourseDetails;
	}

	public int getTotalMarks() {
		return totalMarks;
	}



	public void setTotalMarks(int totalMarks) {
		this.totalMarks = totalMarks;
	}



	public void setRenderCourseDetails(boolean renderCourseDetails) {
		this.renderCourseDetails = renderCourseDetails;
	}

	public boolean isRenderCourseList() {
		return renderCourseList;
	}

	public double[] getScore() {
		return score;
	}



	public void setScore(double[] score) {
		this.score = score;
	}



	public void setRenderCourseList(boolean renderCourseList) {
		this.renderCourseList = renderCourseList;
	}

	public ActionBeanInstructor() {

	}

	public ArrayList<AssessmentBean> getAssessmentList() {
		return assessmentList;
	}

	public void setAssessmentList(ArrayList<AssessmentBean> assessmentList) {
		this.assessmentList = assessmentList;
	}

	public ArrayList<CourseBean> getCourseList() {

		courseList = new ArrayList<CourseBean>();
		CourseBean courseBean;
		try {

			String sQuery = "select distinct(coursename) from f15g107_course_table where courseId in(select"
					+ " courseid from f15g107_user_course_table where userid='" + loginBean2.getUserId() + "')";
			con = new DBConnection();
			con.createConnection();
			rs = con.execQuery(sQuery);
			while (rs.next()) {
				courseBean = new CourseBean();
				courseBean.setCourseName(rs.getString(1));
				courseList.add(courseBean);
			}
			con.connClose();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return courseList;
	}

	public void setCourse_list(ArrayList<CourseBean> course_list) {

	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
		
	}

	public String insertStudentData(ArrayList<StudentBean> student_list) {
		return null;
	}

	public String showAssessments()
	{
		renderAssessmentDetails=true;
		showAssessmentDetails();
		return "SUCCESS";
	}
	
	public String showCourseDetails()
	{
		renderSection=true;
		renderCourseDetails=false;
		renderAssessmentDetails=false;
		renderStudentDetails=false;
		getSectionList();
		return "SUCCESS";
		
	}
	public String showSectionDetails()
	{
		renderCourseDetails=true;
		renderAssessmentDetails=false;
		renderStudentDetails=false;
		return "success";
	}
	public String showAssessmentDetails() {
		renderStudentDetails=false;
		AssessmentBean assessmentBean;
		
		
		courseBean=new CourseBean();
		courseBean.setCourseId(this.courseId);
		assessmentList = new ArrayList<AssessmentBean>();
		try {
			String sQuery1 = "select count(u1.UserId) from f15g107_user_course_table u1,f15g107_user_table u2 where u1.UserId=u2.UserId"
					+ " and u2.Role='Student' and u1.courseId=" + this.courseId + ";";

			String sQuery = "select assessmentid, assessmentname from f15g107_assessment_table where courseid in(select"
					+ " courseid from f15g107_user_course_table where userid=" + loginBean2.getUserId() + " and courseid="
					+ this.courseId + ");";

			con = new DBConnection();
			con.createConnection();

			ResultSet rs = con.execQuery(sQuery);
			if(!rs.next())
			{
				message="No assessment found";
				FacesContext context = FacesContext.getCurrentInstance();
    			context.addMessage(null, new FacesMessage(message));	
    			renderAssessmentDetails=false;
			}
			else
			{
			ResultSet rs1 = con.execQuery(sQuery1);
			ArrayList<String> uid_list=new ArrayList<String>();
			String sQuery3 = "select u1.UserId from f15g107_user_course_table u1,f15g107_user_table u2 where u1.UserId=u2.UserId"
					+ " and u2.Role='Student' and u1.courseId=" + this.courseId + ";";
			ResultSet rs3 = con.execQuery(sQuery3);
			while(rs3.next())
			{
				uid_list.add(rs3.getString(1));
				
			}
			
			rs1.next();
			rs.beforeFirst();
			while (rs.next()) {
				int countTakenBy=0;
				assessmentBean = new AssessmentBean();
				assessmentBean.setTotalStudents(rs1.getInt(1));
				assessmentBean.setAssessmentId(rs.getString(1));
				
				
				String sQuery2 = "select distinct(a1.UserId) from f15g107_Assessment_Result_Table a1,f15g107_assessment_table a2 "
						+ "where a1.assessmentId=a2.assessmentId and a2.CourseId=" + this.courseId + " and "
						+ "a1.AssessmentId=" + rs.getInt(1) + ";";
				ResultSet rs2 = con.execQuery(sQuery2);
				while(rs2.next())
				{
					
					String uid=rs2.getString(1);
					if(uid_list.contains(uid))
					{
						countTakenBy=countTakenBy+1;
					}
					
				}
				rs2.next();
				assessmentBean.setTakenBy(countTakenBy);
				assessmentBean.setAssessmentName(rs.getString(2));
				assessmentList.add(assessmentBean);
			}
			}

			con.connClose();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "SUCCESS";

	}
	public String showStudentsRegistered()
	{
		renderStudentDetails=true;
		renderAssessmentDetails=false;
		StudentBean studentBean;
		student_list = new ArrayList<StudentBean>();
		try {
			
			String sQuery = "select userid,firstName, lastName,userName from f15g107_user_table where role='student' and userid in(select"
					+ " userid from f15g107_user_course_table where courseid="
					+ this.courseId + ");";

			con = new DBConnection();
			con.createConnection();

			ResultSet rs = con.execQuery(sQuery);
			if(!rs.next())
			{
				message="No student registered for the course";
				FacesContext context = FacesContext.getCurrentInstance();
    			context.addMessage(null, new FacesMessage(message));	
    			renderStudentDetails=false;
			}
			else
			{
			
			
			rs.beforeFirst();
			while (rs.next()) {
				studentBean = new StudentBean();
				studentBean.setUserId(rs.getString(1));
				studentBean.setFirstName(rs.getString(2));
				studentBean.setLastName(rs.getString(3));
				studentBean.setUserName(rs.getString(4));
				student_list.add(studentBean);
			}
			}

			con.connClose();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return "SUCCESS";
	}
	public String uploadFile() {
		int checkDate=0;
		int uploadFailed=0;
		String[] perItem = null;
		ArrayList<String> perItem1=new ArrayList<String>();
		con = new DBConnection();
		con.createConnection();
		if (fileType.equalsIgnoreCase("assessment")) {
			if(dueDate==null)
			{
				dueDate=new Date();
				checkDate=1;
			}
			Calendar cal=Calendar.getInstance();
			cal.setTime(dueDate);
			int month=cal.get(Calendar.MONTH);
			month=month+1;
			String date_str=cal.get(Calendar.DAY_OF_MONTH)+"/"+month+"/"+cal.get(Calendar.YEAR);
			try {
				// Upload success
				FacesContext.getCurrentInstance().addMessage("uploadForm",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "File upload was a total success!", null));
				//String fname = uploadedFile.getName();
				//String fnameNew = fname.substring(0, fname.indexOf('.'));
				InputStream is = uploadedFile.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line = br.readLine();
				//perItem = line.split(",");
				perItem1=splitFunc(line);
				String iquery = "Insert into f15g107_assessment_table(AssessmentName,CourseId,duedatetime,duration) values" + "('" + fileName + "',"
						+ this.courseId + ",STR_TO_DATE('"+date_str+"', '%d/%m/%Y'),"+this.duration+");";
				con.execUpdateQuery(iquery);
				
				int id=0;
				String sQuery="SELECT MAX(assessmentid) FROM f15g107_assessment_table;";
				
				ResultSet rs=con.execQuery(sQuery);
				while(rs.next())
				{
					id=rs.getInt(1);
				}
				while (line != null) {
					line = br.readLine();
					if (line == null) {
						break;
					}
					perItem1=splitFunc(line);
					for(int i=0;i<perItem1.size();i++)
					{
						String s=perItem1.get(i);
						if (s.contains("'")) {
							s=s.replace("'", "''");
							perItem1.remove(i);
							perItem1.set(i, s);
							
						}
					}
					String iQuery1="INSERT INTO f15g107_question_table(AssessmentId, QuestionNo,Question,Answer,Tolerance)VALUES ("
							+id+ ",";
					int i=0;
					for (i = 0; i < perItem1.size(); i++) {
						iQuery1 = iQuery1 + " '" + perItem1.get(i) + "' ";
						if (i != (perItem1.size() - 1)) {
							iQuery1 = iQuery1 + ",";
						}
					}
					if(i<4)
					{
						iQuery1 = iQuery1 + ",''";
					}
					iQuery1 = iQuery1 + ");";
					message=con.execUpdateQuery(iQuery1);
					if(message!=null)
					{
						String dQuery="delete from f15g107_question_table where assessmentid="+id+";";
						String dQuery1="delete from f15g107_assessment_table where assessmentid="+id+";";
						con.execUpdateQuery(dQuery);
						con.execUpdateQuery(dQuery1);
						message="Assessment upload failed with the following error :"+message;
						
						uploadFailed=1;
						break;
			            //return "failure";
					}
				}
				if(uploadFailed==1)
				{
					System.out.println("i am failing");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
					return "failed";
				}
				if(message==null && checkDate==1)
				{
					message="Assessment uploaded successfully with due date as current date";
					context = FacesContext.getCurrentInstance();
	    			context.addMessage(null, new FacesMessage(message));
				}

			} catch (Exception e) {
				// Upload failed
				FacesContext.getCurrentInstance().addMessage("uploadForm",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload was a failed.", null));
				e.printStackTrace();
			}
			//showAssessmentDetails();
			showAssessments();
			//con.connClose();
	} else if (fileType.equalsIgnoreCase("roster")) {
			ArrayList<String> userIdRegistered=new ArrayList<String>();
			try {
			String chQuery="select UserId from f15g107_user_table where role='student' and "
					+ "UserId in (select UserId from f15g107_user_course_table where CourseId in(select "
					+ "CourseId from f15g107_course_table where CourseName='"+courseName+"'))";
			ResultSet chset=con.execQuery(chQuery);
			while(chset.next())
			{
				userIdRegistered.add(chset.getString(1));
			}
			int userId_pos = 0;
			ArrayList<String> userIdList=new ArrayList<String>();
			InputStream is;
			int check_userid=0;
			
				is = uploadedFile.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line = br.readLine();
				perItem = line.split(",");
				for (int i = 0; i < perItem.length; i++) {
					if (perItem[i].equalsIgnoreCase("userid")) {
						userId_pos = i;
						check_userid=1;
					}
				}
				if(check_userid==0)
				{
					message="Selected file doesnot contain column 'UserID'. Browse another file";
					FacesContext context = FacesContext.getCurrentInstance();
		    		context.addMessage(null, new FacesMessage(message));
		    		renderStudentDetails=false;
		    		renderAssessmentDetails=false;
		            return "failure";

				}
				String sQuery="Select userid from f15g107_user_table where role='student';";
				ResultSet rs=con.execQuery(sQuery);
				if(!rs.next())
				{
					message="No students available to register for the course";
					renderStudentDetails=false;
					renderAssessmentDetails=false;
					FacesContext context = FacesContext.getCurrentInstance();
		    		context.addMessage(null, new FacesMessage(message));
		            return "failure";
				}
				else
				{
				rs.beforeFirst();
				while(rs.next())
				{
					Integer userId=rs.getInt(1);
					userIdList.add(userId.toString());
				}
				int check=0;
				int check2=0;
				String message1="";
				String message2="";
					while (line != null) {
					
					line = br.readLine();
					if (line == null) {
						break;
					}
					perItem = line.split(",");
					String insQuery = "Insert into f15g107_user_course_table(userid, courseid) values(";
					for (int i = 0; i < perItem.length; i++) {
						if (i == userId_pos) {
							if(!userIdList.contains(perItem[i]))
							{
								if(check==0)
								{
									message1="Student id not available in student database :"+perItem[i];
								}
								else
								{
								message1=message1+", "+perItem[i]+" ";
								}
								check++;
							}
							else if(userIdRegistered.contains(perItem[i]))
							{
								if(check2==0)
								{
									message2="Student id already registered for the course:"+perItem[i];
								}
								else
								{
								message2=message2+", "+perItem[i]+" ";
								}
								check2++;
							}
							else
							{
							insQuery = insQuery + " '" + perItem[i] + "','" + this.courseId + "');";
							
						}
					}
					con.execUpdateQuery(insQuery);
					}
					}
					if(check==0 && check2==0)
					{
						message="Students registered to course successfully";
						
					}
					else
					{
						message=message1+". "+message2;
						context = FacesContext.getCurrentInstance();
			    		context.addMessage(null, new FacesMessage(message));
			    		return "failure";
			    		
					}
				}
				context = FacesContext.getCurrentInstance();
	    		context.addMessage(null, new FacesMessage(message));
				showStudentsRegistered();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				String message=e.getMessage();
				e.printStackTrace();
				return message;
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		con.connClose();
		return "SUCCESS";

	}
	public String uploadAssessment()
	{
		int uploadFailed=0;
		renderCalendar=false;
		renderAssessmentDetails=true;
		String[] perItem = null;
		ArrayList<String> perItem1=new ArrayList<String>();
		con = new DBConnection();
		con.createConnection();
		
		if (fileType.equalsIgnoreCase("assessment")) {
			
			Calendar cal=Calendar.getInstance();
			cal.setTime(dueDate);
			String date_str=cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+1+"/"+cal.get(Calendar.YEAR);
			try {
				// Upload success
			
				FacesContext.getCurrentInstance().addMessage("uploadForm",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "File upload was a total success!", null));
				//String fname = uploadedFile.getName();
				//String fnameNew = fname.substring(0, fname.indexOf('.'));
				InputStream is = uploadedFile.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line = br.readLine();
				perItem1=splitFunc(line);
				String iquery = "Insert into f15g107_assessment_table(AssessmentName,CourseId,duedatetime) values" + "('" + fileName + "',"
						+ this.courseId + ",STR_TO_DATE('"+date_str+"', '%d/%m/%Y'));";
				con.execUpdateQuery(iquery);
				
				int id=0;
				String sQuery="SELECT MAX(assessmentid) FROM f15g107_assessment_table;";
				
				ResultSet rs=con.execQuery(sQuery);
				while(rs.next())
				{
					id=rs.getInt(1);
				}
				while (line != null) {
					line = br.readLine();
					if (line == null) {
						break;
					}
					//perItem = line.split(",");
					perItem1=splitFunc(line);
					for(int i=0;i<perItem1.size();i++)
					{
						String s=perItem1.get(i);
						if (s.contains("'")) {
							s=s.replace("'", "''");
							perItem1.remove(i);
							perItem1.set(i, s);
							
							int pos=s.indexOf("'");
							String temp=s.substring(0, pos);
							temp=temp+"''";
							temp=temp+s.substring(pos+1);
							perItem1.remove(i);
							perItem1.set(i, temp);
							
						}
					}
					String iQuery1="INSERT INTO f15g107_question_table(AssessmentId, QuestionNo,Question,Answer,Tolerance)VALUES ("
							+id+ ",";
					//String insQuery = "Insert into " + fileName + "() values(";
					int i=0;
					for (i = 0; i < perItem1.size(); i++) {
						iQuery1 = iQuery1 + " '" + perItem1.get(i) + "' ";
						if (i != (perItem1.size() - 1)) {
							iQuery1 = iQuery1 + ",";
						}
					}
					if(i<4)
					{
						iQuery1 = iQuery1 + ",''";
					}
					iQuery1 = iQuery1 + ");";
					message=con.execUpdateQuery(iQuery1);
					
					if(message!=null)
					{
						
						String dQuery="delete from f15g107_question_table where assessmentid="+id+";";
						String dQuery1="delete from f15g107_assessment_table where assessmentid="+id+";";
						con.execUpdateQuery(dQuery);
						con.execUpdateQuery(dQuery1);
						
						uploadFailed=1;
						
						break;
					}

				}
				if(uploadFailed==1)
				{
				message="Assessment upload failed with the following error :"+message;
				context = FacesContext.getCurrentInstance();
    			context.addMessage(null, new FacesMessage(message));
                return "failure";
				}

			} catch (Exception e) {
				// Upload failed
				FacesContext.getCurrentInstance().addMessage("uploadForm",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload failed.", null));
				e.printStackTrace();
			}
		}
			showAssessmentDetails();
			return "SUCCESS";
	}
	public String downloadFileOld()
	{	
		try {
			
			String sQuery = "select userid,firstname,lastname,username from f15g107_user_table where role='Student' and UserId in"
					+ "(select UserId from f15g107_user_course_table where CourseId=" + this.courseId + ");";
			con = new DBConnection();

			
			
			context = FacesContext.getCurrentInstance();
			ExternalContext ec = context.getExternalContext();
			FileOutputStream fos = null;
			String path =context.getExternalContext().getRealPath("/Roster_"+this.courseId+".csv");
			FileWriter writer = new FileWriter(path);
			
			writer.append("UserId,Firstname,LastName,UserName");
			writer.append("\n");
			ResultSet rs = con.execQuery(sQuery);
			while(rs.next())
			{
				for(int i=1;i<=4;i++)
				{
					writer.append(rs.getString(i));
					if(i<4)
					{ writer.append(',');
					}
				
				}
				writer.append("\n");
			}
			writer.flush();
			writer.close();
			con.connClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	public String downloadFile()
	{
			StringBuffer sb = new StringBuffer();
			String sQuery=new String();
			String path=new String();
			String fname=new String();
			con = new DBConnection();
			Connection c=con.createConnection();
			
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			FileOutputStream fos = null;
			

			int size=0;
			if(dataType.equalsIgnoreCase("sroster"))
			{
				fname="StudentRoster_"+this.courseId+".csv";
				String s="/studentRoster_"+this.courseId+".csv";
				path =fc.getExternalContext().getRealPath(s);
			sb.append("UserId, FirstName, LastName,UserName");
			sb.append("\n");
			sQuery = "select userid,firstname,lastname,username from f15g107_user_table where role='Student' and UserId in"
					+ "(select UserId from f15g107_user_course_table where CourseId=" + this.courseId + ");";
			size=4;
			ResultSet rs = con.execQuery(sQuery);
			try
			{
			while(rs.next())
			{
				for(int i=1;i<=size;i++)
				{
					sb.append(rs.getString(i));
					if(i<size)
					{ sb.append(',');
					}
				
				}
				sb.append("\n");
			}
			sb.append("\n");
			}catch(Exception e)
			{
			}
			
			}
			
			else if(dataType.equalsIgnoreCase("lroster"))
			{
				fname="LoggingRoster_"+this.courseId+".csv";
				String s="/loggingRoster_"+this.courseId+".csv";
				path =fc.getExternalContext().getRealPath(s);
				sb.append("UserId, UserName, IPAddress, DateTime, Action");
				sb.append("\n");
				sQuery = "select u.userid,u.username,l.ipaddress,l.dateTime, l.action from f15g107_user_table u, f15g107_loggingdetails_table l where "
						+ "u.userid=l.userid and role='Student' and u.UserId in"
						+ "(select UserId from f15g107_user_course_table where CourseId=" + this.courseId + ");";
				size=5;
				ResultSet rs = con.execQuery(sQuery);
				try
				{
				while(rs.next())
				{
					for(int i=1;i<=size;i++)
					{
						sb.append(rs.getString(i));
						if(i<size)
						{ sb.append(',');
						}
					
					}
					sb.append("\n");
				}
				sb.append("\n");
				}catch(Exception e)
				{
				}
			}
			else if(dataType.equalsIgnoreCase("aroster"))
			{
				fname="AssessmentRoster_"+this.courseId+".csv";
				String s="/assessmentRoster_"+this.courseId+".csv";
				path =fc.getExternalContext().getRealPath(s);
				try{
					sb.append("Assessment, UserId,UserName, Total Marks, Marks obtained");
					sb.append("\n");
					QuestionBean questionBean;
					ArrayList<QuestionBean> questionList;
					ArrayList<StudentBean> userId_List=new ArrayList<StudentBean>();
				String sQuery1="select assessmentid, assessmentname from f15g107_assessment_table where CourseId=" + this.courseId+";";
				String sQuery3="select uc.userid,u.username from f15g107_user_course_table uc, f15g107_user_table u"
						+ " where u.userid=uc.userid and u.role='student' and uc.CourseId=" + this.courseId+";";
				ResultSet rs3 = con.execQuery(sQuery3);
				while(rs3.next())
				{
					StudentBean studentBean=new StudentBean();
					studentBean.setUserId(rs3.getString(1));
					studentBean.setUserName(rs3.getString(2));
					userId_List.add(studentBean);
				}
				ResultSet rs = con.execQuery(sQuery1);
				while (rs.next())
				{
					String assessementId=rs.getString(1);
					String assessmentName=rs.getString(2);
					String sQuery4="select max(questionno) from f15g107_question_table where assessmentid="+assessementId+";";
					ResultSet rs4 = con.execQuery(sQuery4);
					rs4.next();
					int total=rs4.getInt(1);
					
					
					for(int i=0;i<userId_List.size();i++)
					{
						questionList=new ArrayList<QuestionBean>();
						sb.append(assessmentName);
						sb.append(",");
						sb.append(userId_List.get(i).getUserId());
						sb.append(",");
						sb.append(userId_List.get(i).getUserName());
						sb.append(",");
						String sQuery2="select q.questionno, q.answer, q.tolerance, ar.result from f15g107_question_table q,"
								+ " f15g107_assessment_result_table ar where ar.AssessmentId=q.AssessmentId and "
								+ " q.questionno=ar.questionno and"
								+ " ar.userid="+userId_List.get(i).getUserId()
								+ " and ar.assessmentid="+assessementId+";";
						ResultSet rs2 = con.execQuery(sQuery2);
						sb.append(total);
						sb.append(",");
						
						if(!rs2.next())
						{
							sb.append("Not Taken");
							sb.append("\n");
							
						}
						else
						{
							
						rs2.beforeFirst();
						while(rs2.next())
						{
							questionBean=new QuestionBean();
							
							questionBean.setQuestionNo(rs2.getInt(1));
							
							questionBean.setAnswer(rs2.getString(2));
							
							questionBean.setTolerance(rs2.getString(3));
							
							questionBean.setAnswerSubmitted(rs2.getString(4));
							
							questionList.add(questionBean);
							
							
						}
						
						//int totalMarks=questionList.size();
						int marks=0;
						for(int i1=0;i1<questionList.size();i1++)
						{
							String tolerance=questionList.get(i1).getTolerance();
							if(tolerance.isEmpty() ||tolerance=="" || tolerance==null )
							{
								if(questionList.get(i1).getAnswer().equalsIgnoreCase(questionList.get(i1).getAnswerSubmitted()))
								{
									marks=marks+1;
								}
							}
							else
							{
								Double tolerance_dbl=Double.parseDouble(tolerance);
								if(tolerance_dbl!=0.0)
								{
									//String answer=questionList.get(i1).getAnswer();
									
									String answerSub=questionList.get(i1).getAnswerSubmitted();
									if(answerSub==null || answerSub=="" || answerSub.isEmpty())
									{
										continue;
									}
									try{
									Double answer_dbl=Double.parseDouble(questionList.get(i1).getAnswer());
									Double answerSub_dbl=Double.parseDouble(answerSub);
									if((answerSub_dbl>=(answer_dbl-tolerance_dbl)) && (answerSub_dbl<=(answer_dbl+tolerance_dbl)))
									{
										marks=marks+1;
									}
									}catch(Exception e)
									{
										continue;
									}
									
								}
								if(tolerance_dbl==0.0)
								{
									if(questionList.get(i1).getAnswer().equalsIgnoreCase(questionList.get(i1).getAnswerSubmitted()))
									{
										marks=marks+1;
									}
								}
	
							}
							
						}sb.append(marks);
						sb.append("\n");
						
						}	
					}
					
				}
				}catch(Exception e)
				{
					
				}
				
			}
			
					File f = new File(path);
					try {
					fos = new FileOutputStream(path);
					
					fos.write(sb.toString().getBytes());
					
					fos.flush();
					fos.close();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					String mimeType = ec.getMimeType(path);
					FileInputStream in = null;
					byte b;
					ec.responseReset();
					ec.setResponseContentType(mimeType);
					ec.setResponseContentLength((int) f.length());
					ec.setResponseHeader(
					"Content-Disposition",
					"attachment; filename=\"" +
					fname + "\"");
					try {
					in = new FileInputStream(f);
					OutputStream output =
					ec.getResponseOutputStream();
					while(true) {
					b = (byte) in.read();
					if(b < 0)
					break;
					output.write(b);
					}
					}
					catch (Exception e) { }
					finally {
					try {
					in.close();
					}
					catch (Exception e) { }
					}
					fc.responseComplete();
					return "SUCCESS";
			}
	public String switchToAdmin()
	{
		renderCourseDetails=false;
		renderAssessmentDetails=false;
		renderStudentDetails=false;
		renderSection=false;
		return "/AdminPage.jsp?faces-redirect=true";
	}
	

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/Home.jsp?faces-redirect=true";
	}
	public String switchToStudent()
	{
		renderCourseDetails=false;
		renderAssessmentDetails=false;
		renderStudentDetails=false;
		renderSection=false;
		return "/StudentPage.jsp?faces-redirect=true";
		
	}

	public ArrayList<String> splitFunc(String s)
	{
		String [] lineItem=null;
		ArrayList<String> perItem2=new ArrayList<String>();
		int k=0;
		int i=0;
		String s3="";
		
		if(s.contains("\""))
		{
		do{
			if(s.charAt(i)=='\"')
			{
				
				for(int j=i+1;j<s.length();j++)
				{
					
					if(s.charAt(j)=='\"')
					{
						String s1=s.substring(k, i);
						
						String s2=s.substring(i+1,j);
						if(j<s.length()-2)
						s3=s.substring(j+2);
						
						if(!s1.isEmpty())
						{
							lineItem=s1.split(",");
							for(int m=0;m<lineItem.length;m++)
							{
								perItem2.add(lineItem[m]);
							}
							
						}
						
						if(s2!=null && s2!="")
							perItem2.add(s2);	
						i=j+1;
						k=i+1;
						break;
					}
				}
			}
			else{
				i++;
				int l=s.length();
				if(i==(l-1) && (!s3.isEmpty()))
				{
					lineItem=s3.split(",");
				for(int m=0;m<lineItem.length;m++)
				{
					perItem2.add(lineItem[m]);
				}
					
				}
				}
		}while(i<s.length());
		}
		else
		{
			lineItem=s.split(",");
			for(int m=0;m<lineItem.length;m++)
			{
				perItem2.add(lineItem[m]);
			}
		}
		
		return perItem2;
	}
	
	
	public String removeStudent()
	{
		int checkChecked=0;
		int check=0;
		ArrayList<StudentBean> checkedItems = new ArrayList<StudentBean>();

        for (StudentBean studentBean : student_list) {
            if (checked.get(studentBean.getUserId())) {
                checkedItems.add(studentBean);
               checkChecked=1;
                try {
        			String dQuery1="delete from f15g107_user_course_table where userid="+studentBean.getUserId()+";";
        			con = new DBConnection();
        			con.createConnection();
        			String message1=con.execUpdateQuery(dQuery1);
        			if(message1!=null)
        			{
        				check=1;
        				//message=message1;
        			}
        			con.connClose();
                }catch(Exception e)
                {
                	e.printStackTrace();
                }
            }
        }
        if(checkChecked==0)
        {
        	message="Select a student to drop";
        }
        else if(check==0)
        {
        	 message="Student dropped successfully from the course";	
        }
        else
        {
        	message="Dropping student failed";
        }
        context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(message));
        showStudentsRegistered();
		return "SUCCESS";

	}
	public String deleteAssessment()
	{
		int check=0;
		int chechChecked=0;
		ArrayList<AssessmentBean> checkedItems = new ArrayList<AssessmentBean>();

        for (AssessmentBean assessmentBean : assessmentList) {
            if (checked.get(assessmentBean.getAssessmentId())) {
                checkedItems.add(assessmentBean);
               chechChecked=1;
                try {
        			String dQuery1="delete from f15g107_Assessment_table where assessmentid="+assessmentBean.getAssessmentId()+";";
        			
        			String dQuery2 = "delete from f15g107_question_table where assessmentid="+assessmentBean.getAssessmentId()+";";

        			con = new DBConnection();
        			con.createConnection();
        			String message1=con.execUpdateQuery(dQuery1);
        			String message2=con.execUpdateQuery(dQuery2);
        			if(message1!=null)
        			{
        				check=1;
        				
        			}
        			else if(message2!=null)
        			{
        				check=1;
        			}
        			con.connClose();
                }catch(Exception e)
                {
                	e.printStackTrace();
                }
            }
        }
        if(chechChecked==0)
        {
        	message="Select assessment to delete";
        }
        else if(check==0)
        {
        	message="Assessment deleted successfully";
        }
        else
        {
        	message="Assessment deletion failed";
        }
        context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(message));
        
        showAssessmentDetails();
        checked.clear();
		return "SUCCESS";
	}
	
	public String callGraph1()
	{
		 try {
			 
			 int countStudentsTaken=0;
			dataItemAB = (AssessmentBean) dataTable.getRowData();
			con = new DBConnection();
 			con.createConnection();
 			ArrayList<String> uid_list=new ArrayList<String>();
			String sQuery1 = "select count(u1.UserId) from f15g107_user_course_table u1,f15g107_user_table u2 where u1.UserId=u2.UserId"
					+ " and u2.Role='Student' and u1.courseId=" + this.courseId + ";";
			ResultSet rs1=con.execQuery(sQuery1);
			rs1.next();
			totalStudents=rs1.getInt(1);
			String sQuery2 = "select u1.UserId from f15g107_user_course_table u1,f15g107_user_table u2 where u1.UserId=u2.UserId"
					+ " and u2.Role='Student' and u1.courseId=" + this.courseId + ";";
			ResultSet rs2=con.execQuery(sQuery2);
			while(rs2.next())
			{
				uid_list.add(rs2.getString(1));
			}
			
			String sQuery3 = "select distinct(a1.UserId) from f15g107_Assessment_Result_Table a1,f15g107_assessment_table a2 "
					+ "where a1.assessmentId=a2.assessmentId and a2.CourseId=" + this.courseId + " and "
					+ "a1.AssessmentId=" + dataItemAB.getAssessmentId() + ";";
			ResultSet rs3=con.execQuery(sQuery3);
			while(rs3.next())
			{
				if(uid_list.contains(rs3.getString(1)))
				{
					countStudentsTaken=countStudentsTaken+1;
				}
			}
			totalStudentsTaken=countStudentsTaken;
			String sQuery4 = "select questionno, answer,tolerance from f15g107_question_Table q1 where assessmentid="+
			dataItemAB.getAssessmentId()+";";
			ResultSet rs4=con.execQuery(sQuery4);
			rs4.last();
			int totQues=rs4.getRow();
			notTakenPerQues=new double[totQues];
			correctPerQues=new double[totQues];
			wrongPerQues=new double[totQues];
			int i=0;
			rs4.beforeFirst();
			while(rs4.next())
			{
				int qno=rs4.getInt(1);
				String ans=rs4.getString(2);
				String tolerance=rs4.getString(3);
				int notTakenCount=0;
				int correctCount=0;
				int wrongCount=0;
				
				String sQuery5 = "select userid,result from f15g107_Assessment_Result_Table where assessmentid="
				+dataItemAB.getAssessmentId()+" and questionno="+qno+";";
				ResultSet rs5=con.execQuery(sQuery5);
				while(rs5.next())
				{
					if(uid_list.contains(rs5.getString(1)))
					{
						String ansSub=rs5.getString(2);
						//
						if(tolerance.isEmpty() ||tolerance=="" || tolerance==null )
						{
							if(ansSub==null || ansSub=="" || ansSub.isEmpty())
							{
								notTakenCount=notTakenCount+1;
								continue;
							}
							if(ansSub.equals(ans))
							{
								correctCount=correctCount+1;
								continue;
							}
							else
							{
								wrongCount=wrongCount+1;
								continue;
							}
						}
						else
						{
							Double tolerance_dbl=Double.parseDouble(tolerance);
							if(tolerance_dbl!=0.0)
							{
								if(ansSub==null || ansSub=="" || ansSub.isEmpty())
								{
									notTakenCount=notTakenCount+1;
									continue;
								}
								try{
								Double answer_dbl=Double.parseDouble(ans);
								Double answerSub_dbl=Double.parseDouble(ansSub);
								if((answerSub_dbl>=(answer_dbl-tolerance_dbl)) && (answerSub_dbl<=(answer_dbl+tolerance_dbl)))
								{
									correctCount=correctCount+1;
									continue;
								}
								else
								{
									wrongCount=wrongCount+1;
									continue;
								}
								}catch(Exception e)
								{
									continue;
								}
								
							}
							if(tolerance_dbl==0.0)
							{
								if(ansSub==null || ansSub=="" || ansSub.isEmpty())
								{
									notTakenCount=notTakenCount+1;
									continue;
								}
								if(ans.equalsIgnoreCase(ansSub))
								{
									correctCount=correctCount+1;
									continue;
								}
								else
								{
									wrongCount=wrongCount+1;
									continue;
								}
							}
						
						}
						
					}
					
						
						
						
						
						//
					}
				notTakenPerQues[i]=(double)notTakenCount;
				correctPerQues[i]=(double)correctCount;
				wrongPerQues[i]=(double)wrongCount;
				i++;
				}
		 }catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		return "/GraphPage.jsp?faces-redirect=true";
	}
	
	public String callGraph()
	{
		ArrayList<Double> scoreList=new ArrayList<Double>();
		
		ArrayList<String> uid_list=new ArrayList<String>();
		QuestionBean questionBean;
		ArrayList<QuestionBean> questionList=new ArrayList<QuestionBean>();
		 try {
			 
			 	dataItemAB = (AssessmentBean) dataTable.getRowData();
				con = new DBConnection();
	 			con.createConnection();
	 			
	 			String sQuery2 = "select u1.UserId from f15g107_user_course_table u1,f15g107_user_table u2 where u1.UserId=u2.UserId"
						+ " and u2.Role='Student' and u1.courseId=" + this.courseId + ";";
				ResultSet rs2=con.execQuery(sQuery2);
				while(rs2.next())
				{
					uid_list.add(rs2.getString(1));
				}
				//score=new double[uid_list.size()];
				String sQuery="select questionno, question,answer,tolerance from f15g107_question_table where assessmentid="+
						dataItemAB.getAssessmentId()+";";
						ResultSet rs=con.execQuery(sQuery);
						while(rs.next())
						{
							questionBean=new QuestionBean();
							questionBean.setQuestionNo(rs.getInt(1));
							questionBean.setQuestion(rs.getString(2));
							questionBean.setAnswer(rs.getString(3));
							questionBean.setTolerance(rs.getString(4));
							questionList.add(questionBean);
						}
	 			for(int i1=0;i1<uid_list.size();i1++)
	 			{
		 			String sQuery1 = "select userid,questionno,result from f15g107_Assessment_Result_Table where assessmentid="
		 					+dataItemAB.getAssessmentId()+" and userid="+uid_list.get(i1)+";";
		 			ResultSet rs1=con.execQuery(sQuery1);
		 			if(!rs1.next())
		 			{
		 				continue;
		 			}
		 			rs1.beforeFirst();
					while(rs1.next())
					{
						for(int j=0;j<questionList.size();j++)
						{
							
							if(rs1.getInt(2)==questionList.get(j).getQuestionNo())
							{
								QuestionBean qb=questionList.get(j);
								qb.setAnswerSubmitted(rs1.getString(3));
								questionList.set(j, qb);
							}
						}
						
					}
	 			
	 			int marks=0;
	 			totalMarks=questionList.size();
	 			for(int i=0;i<questionList.size();i++)
				{
	 				if(i==0)
	 				{
					}
					String tolerance=questionList.get(i).getTolerance();
					String answerSub=questionList.get(i).getAnswerSubmitted();
					if(tolerance.isEmpty() ||tolerance=="" || tolerance==null )
					{
						if(answerSub==null || answerSub=="" || answerSub.isEmpty())
						{
							questionList.get(i).setResult("Not Attempted");
							continue;
						}
						if(questionList.get(i).getAnswer().equalsIgnoreCase(questionList.get(i).getAnswerSubmitted()))
						{
							questionList.get(i).setResult("Correct");
							marks=marks+1;
						}
						else
						{
							questionList.get(i).setResult("Wrong");
						}
					}
					else
					{
						Double tolerance_dbl=Double.parseDouble(tolerance);
						if(tolerance_dbl!=0.0)
						{
							if(answerSub==null || answerSub=="" || answerSub.isEmpty())
							{
								questionList.get(i).setResult("Not Attempted");
								continue;
							}
							try{
							Double answer_dbl=Double.parseDouble(questionList.get(i).getAnswer());
							Double answerSub_dbl=Double.parseDouble(answerSub);
							if((answerSub_dbl>=(answer_dbl-tolerance_dbl)) && (answerSub_dbl<=(answer_dbl+tolerance_dbl)))
							{
								questionList.get(i).setResult("Correct");
								marks=marks+1;
							}
							else
							{
								questionList.get(i).setResult("Wrong");
							}
							}catch(Exception e)
							{
								continue;
							}
							
						}
						if(tolerance_dbl==0.0)
						{
							if(answerSub==null || answerSub=="" || answerSub.isEmpty())
							{
								questionList.get(i).setResult("Not Attempted");
								continue;
							}
							if(questionList.get(i).getAnswer().equalsIgnoreCase(questionList.get(i).getAnswerSubmitted()))
							{
								questionList.get(i).setResult("Correct");
								marks=marks+1;
							}
							else
							{
								questionList.get(i).setResult("Wrong");
							}
						}
					}
				}
	 			Double d=(double)marks;
	 			scoreList.add(d);
					//score[k]=marks;
					//k++;
					
				}
	 			int size=scoreList.size();
	 			score=new double[size];
	 			
	 			for(int l=0;l<scoreList.size();l++)
	 			{
	 				score[l]=scoreList.get(l);
	 			}
		 }catch(Exception e)
		 {
			 
		 }
		
		 if(scoreList.size()>0)
		 {
			 return "/GraphPage.jsp?faces-redirect=true"; 
		 }
		 else
		 {
			 message="No student took the assessment";
			 	context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(message));
				return "SUCCESS";	
				
		 }
	}
	
	public String exportAssessment()
	{
		int checkChecked=0;
		
		ArrayList<AssessmentBean> checkedItems = new ArrayList<AssessmentBean>();

        for (AssessmentBean assessmentBean : assessmentList) {
        	StringBuffer sb = new StringBuffer();
    		String path=new String();
    		String fname=new String();
    		FileOutputStream fos = null;
    		FacesContext fc = FacesContext.getCurrentInstance();
    		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    		
            if (checked.get(assessmentBean.getAssessmentId())) {
                checkedItems.add(assessmentBean);
                checkChecked=1;
               
        try {
        			String sQuery="select questionno, question, answer, tolerance from "
        					+ "f15g107_question_table where assessmentid="+assessmentBean.getAssessmentId()+";";
		
		con = new DBConnection();
		Connection c=con.createConnection();
		
		ResultSet rs=con.execQuery(sQuery);
		
		
		DatabaseMetaData dbmd=c.getMetaData();
		//ResultSet colSet = dbmd.getColumns(null, , "f15g107_question_table", null);
		//ResultSetMetaData rsmdCol=colSet.getMetaData();
		
			sb.append("QuestionNo,Question,Answer,Tolerance");
			
		
		sb.append("\n");
		fname="assessment.csv";
		String s="/"+"assessment.csv";
		path =fc.getExternalContext().getRealPath(s);
		while(rs.next())
		{
			for(int i=1;i<=4;i++)
			{
				String s1=rs.getString(i);
				if(s1.contains(","))
				{
					s1=s1.replaceAll(",", ";");
				}
				
				sb.append(s1);
				if(i<4)
				{ sb.append(',');
				}
			
			}
			sb.append("\n");
		}
		}catch(Exception e)
		{
		}
		
		
		
		
		
				File f = new File(path);
				try {
				fos = new FileOutputStream(path);
				
				fos.write(sb.toString().getBytes());
				
				fos.flush();
				fos.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				String mimeType = ec.getMimeType(path);
				FileInputStream in = null;
				byte b;
				ec.responseReset();
				ec.setResponseContentType(mimeType);
				ec.setResponseContentLength((int) f.length());
				ec.setResponseHeader(
				"Content-Disposition",
				"attachment; filename=\"" +
				fname + "\"");
				try {
				in = new FileInputStream(f);
				OutputStream output =
				ec.getResponseOutputStream();
				while(true) {
				b = (byte) in.read();
				if(b < 0)
				break;
				output.write(b);
				}
				}
				catch (Exception e) { }
				finally {
				try {
				in.close();
				}
				catch (Exception e) { }
				}
				fc.responseComplete();
            }
        }
        if(checkChecked==0)
        {
        	message="No assessment selected. Select assessment to export";
        	context = FacesContext.getCurrentInstance();
    		context.addMessage(null, new FacesMessage(message));
        }
		//checkedItems.clear();
        checked.clear();
		return "SUCCESS";
	}
	
	
	
}
