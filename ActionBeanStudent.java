package com.uic.edu;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;

import javax.faces.context.FacesContext;


@ManagedBean
@SessionScoped
public class ActionBeanStudent {
	private ArrayList<CourseBean> courseList1;
	private ArrayList<CourseBean> sectionList;
	private String courseId;
	private String courseName;
	private String section;
	private LoginBean loginBean2;
	//private CourseBean courseBean2;
	private ActionBeanInstructor actionBeanIns;
	private boolean renderIsStudent=false;
	private boolean renderIsInstructor=false;
	private ArrayList<AssessmentBean> assessmentList;
	private boolean renderAssessmentList=false;
	private ArrayList<QuestionBean> questionList;
	private HtmlDataTable dataTable;
    private AssessmentBean dataItemAB = new AssessmentBean();
	private DBConnection con;
	private ResultSet rs;
	private boolean renderShowSubmitAssessment=false;
	private String message;
	private boolean renderShowAssessmentResult=false;
	private boolean renderSection;
	FacesContext context;
	Map <String, Object> m;
	//private HtmlInputHidden dataItemId=new HtmlInputHidden();
	private boolean renderShowAssessment=false;
	private boolean renderReviewMarks=false;
	private Long millis;
	private Date startTime;
	//private Date endTime;
	private Long duration;
	private String userType;
	
	
	@PostConstruct
	public void init() {
	String reqPage=	FacesContext.getCurrentInstance().getViewRoot().getViewId();
	context = FacesContext.getCurrentInstance();
	m =context.getExternalContext().getSessionMap();
	/*Map <String, Object> m1 =
			context.getExternalContext().getRequestMap();*/
	
	loginBean2 = (LoginBean) m.get("loginBean1");
	//courseBean2=(CourseBean)m.get("courseBean");
	
	if(loginBean2.getRole().equalsIgnoreCase("instructor") || loginBean2.getRole().equalsIgnoreCase("TA") 
			|| loginBean2.getRole().equalsIgnoreCase("admin"))
	{
		renderIsInstructor=true;
		actionBeanIns=(ActionBeanInstructor)m.get("actionBeanInstructor");
		courseId=actionBeanIns.getCourseId();
	}
		
	if(loginBean2.getRole().equalsIgnoreCase("Student"))
	{
		renderIsStudent=true;
	}
	}
	
	public String showSectionDetails()
	{
		renderSection=true;
		renderShowAssessment=false;
		renderShowSubmitAssessment=false;
		renderAssessmentList=false;
		getSectionList();
		return "SUCCESS";
	}
	
	public boolean isRenderShowAssessmentResult() {
		return renderShowAssessmentResult;
	}


	
	public String getUserType() {
		if(loginBean2.getRole().equalsIgnoreCase("Instructor"))
		{
			userType="Instructor";
		}
		else if(loginBean2.getRole().equalsIgnoreCase("TA"))
		{
			userType="TA";
		}
		else
		{
			userType="Instructor";
		}
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public ArrayList<CourseBean> getSectionList() {
		sectionList = new ArrayList<CourseBean>();
		CourseBean courseBean;
		try {

			String sQuery = "select courseid,section from f15g107_course_table where courseName='"+courseName+"' and courseId in(select"
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

	public void setSectionList(ArrayList<CourseBean> sectionList) {
		this.sectionList = sectionList;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public boolean isRenderSection() {
		return renderSection;
	}

	public void setRenderSection(boolean renderSection) {
		this.renderSection = renderSection;
	}

	public boolean isRenderReviewMarks() {
		return renderReviewMarks;
	}



	public void setRenderReviewMarks(boolean renderReviewMarks) {
		this.renderReviewMarks = renderReviewMarks;
	}



	public void setRenderShowAssessmentResult(boolean renderShowAssessmentResult) {
		this.renderShowAssessmentResult = renderShowAssessmentResult;
	}



	public LoginBean getLoginBean2() {
		return loginBean2;
	}



	public void setLoginBean2(LoginBean loginBean2) {
		this.loginBean2 = loginBean2;
	}



	public boolean isRenderShowSubmitAssessment() {
		return renderShowSubmitAssessment;
	}



	public void setRenderShowSubmitAssessment(boolean renderShowSubmitAssessment) {
		this.renderShowSubmitAssessment = renderShowSubmitAssessment;
	}



	public boolean isRenderAssessmentList() {
		return renderAssessmentList;
	}



	public void setRenderAssessmentList(boolean renderAssessmentList) {
		this.renderAssessmentList = renderAssessmentList;
	}



	public boolean isRenderShowAssessment() {
		return renderShowAssessment;
	}



	public void setRenderShowAssessment(boolean renderShowAssessment) {
		this.renderShowAssessment = renderShowAssessment;
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


	public boolean isRenderIsStudent() {
		return renderIsStudent;
	}


	public void setRenderIsStudent(boolean renderIsStudent) {
		this.renderIsStudent = renderIsStudent;
	}


	public boolean isRenderIsInstructor() {
		return renderIsInstructor;
	}


	public void setRenderIsInstructor(boolean renderIsInstructor) {
		this.renderIsInstructor = renderIsInstructor;
	}


	public ArrayList<CourseBean> getCourseList1() {
		courseList1 = new ArrayList<CourseBean>();
		CourseBean courseBean;
		if(loginBean2.getRole().equalsIgnoreCase("student"))
		{
			try {

				String sQuery = "select courseid, coursename,section from f15g107_course_table where courseId in(select"
						+ " courseid from f15g107_user_course_table where userid='" + loginBean2.getUserId() + "')";
				con = new DBConnection();
				con.createConnection();
				rs = con.execQuery(sQuery);
				while (rs.next()) {
					courseBean = new CourseBean();
					courseBean.setCourseId(rs.getString(1));
					courseBean.setCourseName(rs.getString(2));
					courseBean.setCourseName(rs.getString(3));
					courseBean.setCourseDesc(rs.getString(2)+" "+rs.getString(3));
					courseList1.add(courseBean);
				}
				con.connClose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {

				String sQuery = "select distinct(coursename) from f15g107_course_table where courseId in(select"
						+ " courseid from f15g107_user_course_table where userid='" + loginBean2.getUserId() + "')";
				con = new DBConnection();
				con.createConnection();
				rs = con.execQuery(sQuery);
				while (rs.next()) {
					courseBean = new CourseBean();
					courseBean.setCourseName(rs.getString(1));
					courseList1.add(courseBean);
				}
				con.connClose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		

		
		
		return courseList1;
	}
	public void setCourseList1(ArrayList<CourseBean> courseList1) {
		this.courseList1 = courseList1;
	}
	public String getCourseId() {
		return courseId;
	}
	
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	public ArrayList<QuestionBean> getQuestionList() {
		return questionList;
	}


	public void setQuestionList(ArrayList<QuestionBean> questionList) {
		this.questionList = questionList;
	}


	public ArrayList<AssessmentBean> getAssessmentList() {
		
		AssessmentBean assessmentBean;
		ArrayList<String> assessmentsDoneList=new ArrayList<String>();
		assessmentList=new ArrayList<AssessmentBean>();
		try {
			//STR_TO_DATE(datestring, '%d/%m/%Y')
			
			
			
			String sQuery="select assessmentid, assessmentname, DATE_FORMAT(duedatetime,'%d/%m/%Y') from f15g107_assessment_table where courseid in(select"
					+ " courseid from f15g107_user_course_table where userid="+loginBean2.getUserId()
							+ " and courseid="+this.courseId+");";
			
			String sQuery1="select assessmentid from f15g107_assessment_result_table where userid='"+loginBean2.getUserId()+"';";
			
			
			con = new DBConnection();
			con.createConnection();
		
			ResultSet rs1=con.execQuery(sQuery1);
			if(!rs1.next())
			{
				
			}
			else {
				rs1.beforeFirst();
				
				while(rs1.next())
				{
					String assessmentId=rs1.getString(1);
					assessmentsDoneList.add(assessmentId);
				}
			}
			ResultSet rs=con.execQuery(sQuery);
			if(!rs.next())
			{
				renderAssessmentList=false;
				message="No assessments found for this course";
				context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(message));

				
			}
			else
			{
				
			
			rs.beforeFirst();
			while(rs.next())
			{
				assessmentBean=new AssessmentBean();
				assessmentBean.setAssessmentId(rs.getString(1));
				assessmentBean.setAssessmentName(rs.getString(2));	
				
				
				if(rs.getString(3)==null)
				{
					if(assessmentsDoneList!=null)
					{
						if(assessmentsDoneList.contains(rs.getString(1)))
						{
							assessmentBean.setDisplayAction("View Score");
						}else
						{
							assessmentBean.setDisplayAction("Take Assessment");
						}
					}
					else
					{
						assessmentBean.setDisplayAction("Take Assessment");
					}
					assessmentList.add(assessmentBean);
					
				}else{
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date date = formatter.parse(rs.getString(3));
					assessmentBean.setDueDate(date);
					//Date now =new Date();
					
						if(assessmentsDoneList!=null)
						{
							if(assessmentsDoneList.contains(rs.getString(1)))
							{
								assessmentBean.setDisplayAction("View Score");
							}else
							{
								if(loginBean2.getRole().equalsIgnoreCase("student"))
								{
								assessmentBean.setDisplayAction("Take Assessment");
								}
								else{
									assessmentBean.setDisplayAction("View Assessment");
								}
							}
						}
						else
						{
							if(loginBean2.getRole().equalsIgnoreCase("student"))
							{
								assessmentBean.setDisplayAction("Take Assessment");
							}
							else
							{
								assessmentBean.setDisplayAction("View Assessment");
							}
						}
						assessmentList.add(assessmentBean);
					}
				
				
			}
			}
			
			con.connClose();
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return assessmentList;
	}
	public void setAssessmentList(ArrayList<AssessmentBean> assessmentList) {
		this.assessmentList = assessmentList;
	}
	public String showAssessmentDetails()
	{
		
		try
		{
		String sQuery="select assessmentid, assessmentname, DATE_FORMAT(duedatetime,'%d/%m/%Y') from f15g107_assessment_table where courseid in(select"
				+ " courseid from f15g107_user_course_table where userid="+loginBean2.getUserId()
						+ " and courseid="+this.courseId+");";
		
		con = new DBConnection();
		con.createConnection();
	
		ResultSet rs=con.execQuery(sQuery);
		if(!rs.next())
		{
			renderAssessmentList=false;
			message="No assessments found for this course";
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(message));
			
		}
		else
		{
			renderAssessmentList=true;
		}
		}catch(Exception e)
		{
		}
		renderShowSubmitAssessment=false;
		renderShowAssessment=false;
		renderReviewMarks=false;
		renderShowAssessmentResult=false;
		//renderAssessmentList=false;
		return "SUCCESS";
	}
	

   /* public HtmlInputHidden getDataItemId() {
		return dataItemId;
	}


	public void setDataItemId(HtmlInputHidden dataItemId) {
		this.dataItemId = dataItemId;
	}
*/

	public String takeAssessment() {
		renderReviewMarks=false;
		renderShowAssessmentResult=false;
		message="";
		try
		{
		dataItemAB = (AssessmentBean) dataTable.getRowData();
		String sQuery1="select assessmentid,result from f15g107_assessment_result_table where userid='"+loginBean2.getUserId()+"'"
				+ "and assessmentId='"+ dataItemAB.getAssessmentId()+"';";
		con = new DBConnection();
		con.createConnection();
	
		ResultSet rs1=con.execQuery(sQuery1);
		if(!rs1.next())
		{
			startTime=new Date();
			renderShowAssessment=true;
			if(loginBean2.getRole().equalsIgnoreCase("student"))
			{
				renderShowSubmitAssessment=true;
			}
			String sQuery2="select assessmentid, assessmentname, DATE_FORMAT(duedatetime,'%d/%m/%Y'),duration from f15g107_assessment_table where courseid in(select"
					+ " courseid from f15g107_user_course_table where userid="+loginBean2.getUserId()
							+ " and courseid="+this.courseId+ " and assessmentId='"+ dataItemAB.getAssessmentId()+"');";
			
			ResultSet rs2=con.execQuery(sQuery2);
			rs2.next();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = formatter.parse(rs2.getString(3));
			duration=rs2.getLong(4);
			Date newDate=date;
			int d=date.getDate();
			newDate.setDate(d+1);
			Date now =new Date();
			if(newDate.after(now))
			{
				renderShowAssessment=true;
		        QuestionBean questionBean;
		        questionList=new ArrayList<QuestionBean>();
		        try {
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
					questionBean.setAnswerSubmitted("");
					questionList.add(questionBean);
				}
				
				
			}catch(SQLException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	        
			}
			else if(now.after(newDate) )
			{
				message="Due date for assessment passed";
				renderShowAssessment=false;
				renderShowSubmitAssessment=false;
				context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(message));
					
			}
		}
		else {
			renderShowAssessment=false;
			renderShowSubmitAssessment=false;
			
			String sQuery="select ar.QuestionNo,q.Question, ar.Result, q.Answer,q.tolerance from f15g107_assessment_result_table ar, f15g107_question_table q"
					+ " where ar.AssessmentId=q.AssessmentId and q.questionno=ar.questionno and ar.userid='"+loginBean2.getUserId()+"'"
				+ "and ar.assessmentId='"+ dataItemAB.getAssessmentId()+"';";
			
			ResultSet rs=con.execQuery(sQuery);
			QuestionBean questionBean;
			questionList=new ArrayList<QuestionBean>();
			while(rs.next())
			{
				Integer qNo=rs.getInt(1);
				String ques=rs.getString(2);
				String ansGiven=rs.getString(3);
				String ansActual=rs.getString(4);
				String toler=rs.getString(5);
				questionBean=new QuestionBean();
				questionBean.setQuestionNo(qNo);
				questionBean.setQuestion(ques);
				questionBean.setAnswerSubmitted(ansGiven);
				questionBean.setAnswer(ansActual);
				questionBean.setTolerance(toler);
				questionList.add(questionBean);
			}
			int totalMarks=questionList.size();
			int marks=0;
			for(int i=0;i<questionList.size();i++)
			{
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
						//String answer=questionList.get(i).getAnswer();
						
						
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
			
			message="Marks scored in assessment is "+marks+" out of "+totalMarks;
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(message));
			renderReviewMarks=true;
				
			
			
		}
	
		}catch(Exception e)
		{
			
		}
		
		con.connClose();
		        
        return "Success"; // Navigation case.
    }
	
	public String reviewResult()
	{
		renderShowAssessmentResult=true;
		return "success";
	}
	public String switchToStudent()
	{
		renderShowAssessment=false;
		renderShowSubmitAssessment=false;
		renderAssessmentList=false;
		renderSection=false;
		return "/InstructorPage.jsp?faces-redirect=true";
	}
	
	public String submitAssessment()
	{
		timeRemaining();
		if(millis<0)
		{
			renderShowAssessment=false;
			renderShowSubmitAssessment=false;
			message="Time duration for the assessment expired";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			
		}
		else
		{
		
		message="";
		
		renderShowAssessment=false;
		renderShowSubmitAssessment=false;
		
		try{
		con=new DBConnection();
		con.createConnection();
		String newMessage="";
		int check=0;
		for(int i=0;i<questionList.size();i++)
		{
			int questionNo=questionList.get(i).getQuestionNo();
			String answerSubmitted=questionList.get(i).getAnswerSubmitted();
			String iQuery="Insert into f15g107_assessment_result_table values("+dataItemAB.getAssessmentId()+
					","+loginBean2.getUserId()+","+questionNo+",'"+answerSubmitted+"');";
			newMessage=con.execUpdateQuery(iQuery);
			if(newMessage!=null)
			{
				check=1;
			}
				
		}
		if(check==0)
		{
			message="Assessment submitted successfully";
		}
		else
		{
			message=newMessage;
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(message));
		con.connClose();
		}catch(Exception e)
		{
		}
		}
		return "SUCCESS";
	}
	
	public String cancelSubmission()
	{
		renderShowAssessment=false;
		renderShowSubmitAssessment=false;
		return "SUCCESS";
	}
	public String timeRemaining()
	{
		if(duration==0 || duration==null )
		{
			millis=0L;
			message="No time allocated to the assessment";	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			
		}
		else
		{
		Date currentTime=new Date();
		Long comMillis=currentTime.getTime()-startTime.getTime();
		Long totMillis=duration*1000*60;
		millis=totMillis-comMillis;
		String timeLeft=String.format("%02d:%02d:%02d", 
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) -  
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
				TimeUnit.MILLISECONDS.toSeconds(millis) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		if(millis<0)
		{
			timeLeft="00:00:00";
		}
		message="Time Remaining is "+timeLeft;	
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
		}
		
		return "SUCCESS";
	}

}
