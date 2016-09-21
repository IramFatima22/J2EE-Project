package com.uic.edu;

import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import java.sql.ResultSetMetaData;

@ManagedBean
@SessionScoped
public class ActionBeanAdmin {
	
	private UploadedFile uploadedFile;
	private String fileName;
	
	private String tableSelected="";
	private ArrayList <String> tableList = null;
	private DBConnection con;
	private ResultSet rs;
	private String message;
	private ArrayList<Integer> columnList;
	private String [][] tableArray;
	private boolean renderTableHeader;
	private boolean renderTableDetails;
	private String[] columnArray;
	private boolean disable=false;
	private boolean renderButton=false;
	private String pkCol=null;
	private ArrayList<String> colList=null;
	private String [][] insertArray;
	private String [][] insertDataArray;
	private boolean renderInsertData;
	private boolean renderRemoveRow=false;
	private LoginBean loginBean2;
	private boolean renderMainAdmin=false;
	private boolean renderFileUpload=false;
	private HtmlDataScroller scroller;
	private String schema;
	private FacesContext context;

	
	


	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		//String path = context.getExternalContext().getRealPath("/path");
		loginBean2 = (LoginBean) m.get("loginBean1");
		DBConnection dbConnection = (DBConnection) m.get("dBConnection");
		schema=dbConnection.getSchema();
		if(loginBean2.getUserId()==null)
		{
			renderMainAdmin=false;
			loginBean2.setFirstName("Admin");
		}
		else
		{
			renderMainAdmin=true;
		}
	}
	public HtmlDataScroller getScroller() {
		return scroller;
	}


	public void setScroller(HtmlDataScroller scroller) {
		this.scroller = scroller;
	}
	
	public boolean isRenderFileUpload() {
		return renderFileUpload;
	}


	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}


	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public void setRenderFileUpload(boolean renderFileUpload) {
		this.renderFileUpload = renderFileUpload;
	}


	public boolean isRenderMainAdmin() {
		return renderMainAdmin;
	}


	public void setRenderMainAdmin(boolean renderMainAdmin) {
		this.renderMainAdmin = renderMainAdmin;
	}


	public LoginBean getLoginBean2() {
		return loginBean2;
	}


	public void setLoginBean2(LoginBean loginBean2) {
		this.loginBean2 = loginBean2;
	}


	public boolean isRenderRemoveRow() {
		return renderRemoveRow;
	}
	public void setRenderRemoveRow(boolean renderRemoveRow) {
		this.renderRemoveRow = renderRemoveRow;
	}
	public String[][] getInsertDataArray() {
		/*if(insertDataArray.length==1)
		{
			renderRemoveRow=false;
		}*/
		return insertDataArray;
	}
	public void setInsertDataArray(String[][] insertDataArray) {
		this.insertDataArray = insertDataArray;
	}
	public String[][] getInsertArray() {
		return insertArray;
	}
	public void setInsertArray(String[][] insertArray) {
		this.insertArray = insertArray;
	}
	
	
	
	
	
	
	public boolean isRenderTableHeader() {
		return renderTableHeader;
	}
	public void setRenderTableHeader(boolean renderTableHeader) {
		this.renderTableHeader = renderTableHeader;
	}
	
	public boolean isRenderInsertData() {
		return renderInsertData;
	}
	public void setRenderInsertData(boolean renderInsertData) {
		this.renderInsertData = renderInsertData;
	}
	public ArrayList<String> getColList() {
		return colList;
	}
	public void setColList(ArrayList<String> colList) {
		this.colList = colList;
	}
	private HashMap<String[][], Boolean> checked = new HashMap<String[][], Boolean>();
	
	
	public Map<String[][], Boolean> getChecked() {
		return checked;
	}
	public void setChecked(HashMap<String[][], Boolean> checked) {
		this.checked = checked;
	}
	public boolean isRenderButton() {
		return renderButton;
	}
	public void setRenderButton(boolean renderButton) {
		this.renderButton = renderButton;
	}
	public boolean isDisable() {
		
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public String[] getColumnArray() {
		return columnArray;
	}
	public void setColumnArray(String[] columnArray) {
		this.columnArray = columnArray;
	}
	public ArrayList<Integer> getColumnList() {
		return columnList;
	}
	public void setColumnList(ArrayList<Integer> columnList) {
		this.columnList = columnList;
	}
	public String[][] getTableArray() {
		return tableArray;
	}
	public void setTableArray(String[][] tableArray) {
		this.tableArray = tableArray;
	}
	public boolean isRenderTableDetails() {
		return renderTableDetails;
	}
	public void setRenderTableDetails(boolean renderTableDetails) {
		this.renderTableDetails = renderTableDetails;
	}
	
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/Home.jsp?faces-redirect=true";
	}
	
	public String getTableSelected() {
		return tableSelected;
	}
	public void setTableSelected(String tableSelected) {
		this.tableSelected = tableSelected;
	}
	public ArrayList<String> getTableList() {
		return tableList;
	}
	public void setTableList(ArrayList<String> tableList) {
		this.tableList = tableList;
	}
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String createTable()
	{
		message="";
		try {
			tableList=new ArrayList<String>();
				String cQuery=null;
				con=new DBConnection();
				Connection connection=con.createConnection();
				DatabaseMetaData dbmd=connection.getMetaData();
				String[] types = {"TABLE"};
				rs = dbmd.getTables(null, schema, null,types);
				if(rs != null) {
					while(rs.next()) {
						tableList.add(rs.getString("TABLE_NAME"));
					}
				}
				if(tableList.contains(tableSelected))
				{
					message="Table already present";
					renderTableDetails=false;
					renderTableHeader=false;
				}
				else
				{
				
				if(tableSelected.equalsIgnoreCase("f15g107_user_table"))
				{
					cQuery="CREATE TABLE f15g107_user_table (UserId int NOT NULL,"
							+ "FirstName varchar(15) ,LastName varchar(15) ,"
							+ "Username varchar(15) NOT NULL,Password varchar(32) ,"
							+ "Role varchar(15) , PRIMARY KEY (UserId))";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_course_table"))
				{
					cQuery="CREATE TABLE f15g107_course_table (CourseId int NOT NULL AUTO_INCREMENT UNIQUE,"
							+ "CourseName varchar(20),CourseDescription Varchar(30),"
							+ " Section varchar(15),PRIMARY KEY (CourseName, Section))";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_user_course_table"))
				{
					cQuery="CREATE TABLE f15g107_user_course_table (CourseId int ,"
							+ "UserId int references f15g107_user_table ON DELETE CASCADE, PRIMARY KEY (UserId,CourseId),"
							+ "CONSTRAINT  FOREIGN KEY (CourseId) REFERENCES f15g107_course_table(CourseId) ON DELETE CASCADE"
							+ ");";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_assessment_table"))
				{
					cQuery="CREATE TABLE f15g107_assessment_table(AssessmentId int NOT NULL AUTO_INCREMENT,"
							+ "AssessmentName varchar(25),CourseId int,"
							+ "DueDateTime date, Duration int, PRIMARY KEY (AssessmentId),"
							+ "CONSTRAINT  FOREIGN KEY (CourseId) REFERENCES f15g107_course_table(CourseId) ON DELETE CASCADE"
							+ ");";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_assessment_result_table"))
				{
					cQuery="CREATE TABLE f15g107_assessment_result_table (AssessmentId int ,"
							+ "UserId int, QuestionNo int, Result varchar(30),"
							+ "PRIMARY KEY (AssessmentId,UserId,QuestionNo),"
							+ "CONSTRAINT  FOREIGN KEY (AssessmentId) REFERENCES f15g107_assessment_table(AssessmentId) ON DELETE CASCADE,"
							+ "CONSTRAINT  FOREIGN KEY (UserId) REFERENCES f15g107_user_table(UserId) ON DELETE CASCADE);";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_loggingdetails_table"))
				{
					cQuery="CREATE TABLE f15g107_loggingdetails_table (LoggingId int NOT NULL AUTO_INCREMENT,"
							+ "UserId int ,IpAddress varchar(25),"
							+ "DateTime datetime,Action varchar(10), PRIMARY KEY (LoggingId),"
							+ "CONSTRAINT  FOREIGN KEY (UserId) REFERENCES f15g107_user_table(UserId) ON DELETE CASCADE);";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_question_table"))
				{
					cQuery="Create table f15g107_question_table (AssessmentId int,"
							+ "QuestionNo int, Question varchar(400), Answer varchar(30), Tolerance varchar(20),"
							+ "constraint primary key(AssessmentId,QuestionNo),"
							+ "CONSTRAINT  FOREIGN KEY (AssessmentId) REFERENCES f15g107_assessment_table(AssessmentId) ON DELETE CASCADE);";
				}
				
				message=con.execUpdateQuery(cQuery);
				con.connClose();
				if(message==null || message=="" )
				{
				message="Table created successfully";
				}
				renderTableDetails=false;
				renderTableHeader=true;
				}
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			viewTable();
			return "SUCCESS";
	}
	
	public String dropTable()
	{
		message="";
		try {
				String dQuery=null;
				con=new DBConnection();
				con.createConnection();
				if(tableSelected.equalsIgnoreCase("f15g107_user_table"))
				{
					//dQuery="delete from user_table where role in('Student','Instructor')";					
					dQuery="drop TABLE if exists f15g107_user_table";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_course_table"))
				{
					dQuery="drop TABLE if exists f15g107_course_table ";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_user_course_table"))
				{
					dQuery="drop TABLE if exists f15g107_user_course_table";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_assessment_table"))
				{
					dQuery="drop TABLE if exists f15g107_assessment_table";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_assessment_result_table"))
				{
					dQuery="drop TABLE if exists f15g107_assessment_result_table";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_loggingdetails_table"))
				{
					dQuery="drop TABLE if exists f15g107_loggingdetails_table";
				}
				else if(tableSelected.equalsIgnoreCase("f15g107_question_table"))
				{
					dQuery="drop TABLE if exists f15g107_question_table";
				}
				
				
				con.execUpdateQuery("SET FOREIGN_KEY_CHECKS=0;");
				message=con.execUpdateQuery(dQuery);
				con.connClose();
				if(message==null || message=="")
				{
				message="Table dropped successfully";
				}
				
				
		}catch (Exception e) {
			// TODO Auto-generated catch block
			message=e.getMessage();
			e.printStackTrace();
		}
		renderTableDetails=false;
		renderTableHeader=false;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
		return "success";
	}
	public String viewTable()
	{
		resetScrollerIndex();
		message="";
		checked.clear();
		renderInsertData=false;
		renderButton=true;
		if(tableSelected.equalsIgnoreCase("f15g107_user_table"))
		{
			renderButton=false;
		disable= true;
		}
		else if(!tableSelected.equalsIgnoreCase("f15g107_user_table"))
		{
			renderButton=true;
			disable= false;
		}
		tableList=new ArrayList<String>();
		columnList=new ArrayList<Integer>();
		
		try {
			
				String sQuery=null;
				con=new DBConnection();
				Connection connection=con.createConnection();
				DatabaseMetaData dbmd=connection.getMetaData();
				String[] types = {"TABLE"};
				rs = dbmd.getTables(null, schema, null,types);
				if(rs != null) {
					while(rs.next()) {
						tableList.add(rs.getString("TABLE_NAME"));
					}
				}
				if(tableList.contains(tableSelected))
				{
					sQuery="select * from "+tableSelected+" ;";
					ResultSet rs1=con.execQuery(sQuery);
					
					ResultSetMetaData rsmd=rs1.getMetaData();
					rs1.beforeFirst();
					int count=rsmd.getColumnCount();
					int k=0;
					rs1.last();
					int rowcount=rs1.getRow();
					rs1.beforeFirst();
					tableArray=new String[rowcount][count];
					
					ResultSet colSet = dbmd.getColumns(null, schema, tableSelected, null);
					int i2=0;
					colSet.last();
					colSet.beforeFirst();
					colSet.next();
					pkCol=colSet.getString("COLUMN_NAME");
					colSet.beforeFirst();
					colList=new ArrayList<String>();
					colList.add("Select");
					while(colSet.next())
					{
						colList.add(colSet.getString("COLUMN_NAME"));
						columnList.add(i2);
						i2++;
						
					}
					rs1.beforeFirst();
					if(!rs1.next())
					{
						
						message="No data present in table";
						renderTableDetails=false;
						renderTableHeader=true;
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
						
					}
					else
					{
					rs1.beforeFirst();
					while(rs1.next())
					{
						for(int i=1;i<=count;i++)
						{
							if(i==5 && tableSelected.equalsIgnoreCase("f15g107_user_table"))
							{
								tableArray[k][i-1]=rs1.getString(i).substring(0, 12);
							}
							else
							{
							tableArray[k][i-1]=rs1.getString(i);
							}
						}
						k++;
					}
					renderTableDetails=true;
					renderTableHeader=true;
					}
					
					

				}
				else
				{
					message="Table doesnot exist";
					renderTableDetails=false;
					renderTableHeader=false;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
				}
				con.connClose();
				
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "SUCCESS";
	}
	public String switchToInstructor()
	{
		renderFileUpload=false;
		renderTableHeader=false;
		renderTableDetails=false;
		renderInsertData=false;
		return "/InstructorPage.jsp?faces-redirect=true";
	}
	public String deleteTableData()
	{
		int unselected=0;
		
		
			System.out.println("why");
		String message1="";
		int check=0;
		ArrayList<String> tableid=new ArrayList<String>();
		for(int j=0;j<tableArray.length;j++)
		{
			if(checked.containsKey(tableArray[j]))
			{
				if(checked.get(tableArray[j]))
				{
					tableid.add(tableArray[j][0]);
					unselected=1;
				}
			}
		}
		System.out.println("uns :"+unselected);
		con=new DBConnection();
		con.createConnection();
		for (int i = 0; i < tableid.size(); i++) {
			String dQuery="Delete from "+tableSelected+" where "+pkCol+"="+tableid.get(i)+";";
			message1=con.execUpdateQuery(dQuery);
			if(message1==null || message1=="")
			{
			
			}
			else
			{
				check=1;
			}
		}
		if(unselected==0)
		{
			message1="Select rows to delete";
		}
		else if(check==0)
		{
			message1="Data deleted successfully";
			viewTable();
		}
		else{
			message1="Deletion failed";
		}
		
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message1));
		con.connClose();
		checked.clear();
		
		
			
		return "SUCCESS";
	}
	public String insertTableData()
	{
		try
		{
			renderInsertData=true;
			columnList=new ArrayList<Integer>();
			con=new DBConnection();
			Connection connection=con.createConnection();
			DatabaseMetaData dbmd=connection.getMetaData();
			
			ResultSet colSet = dbmd.getColumns(null, schema, tableSelected, null);
			//ResultSetMetaData rsmdCol=colSet.getMetaData();
			
			int i2=0;
			colSet.last();
			int columnNo=colSet.getRow();
			insertArray=new String[1][columnNo];
			insertDataArray=new String[1][columnNo];
			colSet.beforeFirst();
			while(colSet.next())
			{
				
				insertArray[0][i2]=colSet.getString("COLUMN_NAME");
				columnList.add(i2);
				i2++;
				
			}
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "SUCCESS";
	}
	public String insertData()
	{
		int check=0;
		message="";
		String message1="";
		renderRemoveRow=false;
		renderInsertData=false;
		try
		{
		con=new DBConnection();
		String iQuery="";
		
		//Connection connection=con.createConnection();
		if(tableSelected.equals("f15g107_user_table"))
		{
			for(int j=0;j<insertDataArray.length;j++)
			{
				iQuery="Insert into "+tableSelected+" values(";
				for(int i=0;i<insertDataArray[j].length;i++)
				{
					if(i==4)
					{
						iQuery=iQuery+"md5('"+insertDataArray[j][i]+"')";
					}
					else
					{
					iQuery=iQuery+"'"+insertDataArray[j][i]+"'";
					}
					if(i<insertDataArray[j].length-1)
					{
						iQuery=iQuery+",";
					}
				}
				iQuery=iQuery+");";
				System.out.println("step 2"+iQuery);
				message=con.execUpdateQuery(iQuery);
				System.out.println("message :"+message);
				
			}
			
		}
		System.out.println("step 3");
		if(tableSelected.equals("f15g107_user_course_table"))
		{
			for(int j=0;j<insertDataArray.length;j++)
			{
				String courseid="";
				String userid="";
				iQuery="Insert into "+tableSelected+" values(";
				for(int i=0;i<insertDataArray[j].length;i++)
				{
					iQuery=iQuery+"'"+insertDataArray[j][i]+"'";
					if(i<insertDataArray[j].length-1)
					{
						iQuery=iQuery+",";
					}
					if(i==0)
					{
						courseid=insertDataArray[j][0];
					}
					System.out.println("step 3 a");
					if(i==1)
					{
						System.out.println("step 3 b");
						userid=insertDataArray[j][1];
						String sQuery1="select role from f15g107_user_table where userid="+userid+";";
						System.out.println("step 3 b0"+sQuery1);
						ResultSet rset1=con.execQuery(sQuery1);
						System.out.println("step 3 b0a");
						rset1.next();
						System.out.println("step 3 b1");
						if(rset1.getString(1).equalsIgnoreCase("student"))
						{
							System.out.println("step 3 c");
							String sQuery2="select CourseName from f15g107_course_table where CourseId "
									+ "in (select CourseId from f15g107_user_course_table where userid="+userid+");";
							String sQuery3="select coursename from f15g107_course_table where CourseId="+courseid+";";
							ResultSet rset2=con.execQuery(sQuery2);
							ResultSet rset3=con.execQuery(sQuery3);
							rset3.next();
							String cname=rset3.getString(1);
							System.out.println("step 3 d");
							while(rset2.next())
							{
								String cname1=rset2.getString(1);
								if(cname.equalsIgnoreCase(cname1))
								{
									message1=message1+" "+userid;
									check=1;
									break;
								}
							}
						
							
						}
						
					}
				}
				System.out.println("step 4");
				iQuery=iQuery+");";
				System.out.println("step 4"+iQuery);
				message=con.execUpdateQuery(iQuery);
				
			}
			
		}
		
		
		else
		{
			for(int j=0;j<insertDataArray.length;j++)
			{
				iQuery="Insert into "+tableSelected+" values(";
				for(int i=0;i<insertDataArray[j].length;i++)
				{
					iQuery=iQuery+"'"+insertDataArray[j][i]+"'";
					if(i<insertDataArray[j].length-1)
					{
						iQuery=iQuery+",";
					}
				}
				iQuery=iQuery+");";
				message=con.execUpdateQuery(iQuery);
			}
		}
		if(check==1)
		{
			message="Warning: StudentId already registered for the course with different section ";
		}
		if(message==null)
		{
		message="Data inserted successfully";
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
		viewTable();
		con.connClose();
		}catch(Exception e)
		{
			message=e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			//viewTable();
			
		}
		System.out.println("step 5");
		return "SUCCESS";
		
	}
	
	
	public String insertMoreData()
	{
		int row=insertDataArray.length;
		int columnNo=insertDataArray[0].length;
		String tempDataArray[][]=new String[row+1][columnNo];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<columnNo;j++)
			{
				tempDataArray[i][j]=insertDataArray[i][j];
			}
		}
		insertDataArray=new String[row+1][columnNo];
		for(int i=0;i<row+1;i++)
		{
			for(int j=0;j<columnNo;j++)
			{
				insertDataArray[i][j]=tempDataArray[i][j];
			}
		}
		renderRemoveRow=true;
		return "SUCCESS";
	}
	public String removeLastRow()
	{
	
		int row=insertDataArray.length;
		int columnNo=insertDataArray[0].length;
		if(row==1)
		{
			renderInsertData=false;
		}
		else
		{
			String tempDataArray[][]=new String[row-1][columnNo];
			for(int i=0;i<row-1;i++)
			{
				for(int j=0;j<columnNo;j++)
				{
					tempDataArray[i][j]=insertDataArray[i][j];
				}
			}
			insertDataArray=new String[row-1][columnNo];
			for(int i=0;i<row-1;i++)
			{
				for(int j=0;j<columnNo;j++)
				{
					insertDataArray[i][j]=tempDataArray[i][j];
				}
			}
		}
		
		return "SUCCESS";
	}
	public String uploadTableData()
	{
		renderTableDetails=false;
		renderTableHeader=false;
		renderInsertData=false;
		renderFileUpload=true;
		return "SUCCESS";
	}
	public String uploadFileOld()
	{
		message="";
		con = new DBConnection();
		con.createConnection();
		String[] perItem = null;
		int pass_pos=-1;
		String userId=new String();
		String newMessage=new String();
		InputStream is;
		//int check_userid=0;
		try {
			is = uploadedFile.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = br.readLine();
			perItem = line.split(",");
			String insertCol="("+perItem[0];
			//int j=0;
			for (int i = 1; i < perItem.length; i++) {
				insertCol=insertCol+","+perItem[i];	
				if(perItem[i].equalsIgnoreCase("password"))
				{
					pass_pos=i;
				}
					
			}
			if(pass_pos==-1)
			{
				insertCol=insertCol+",password)";
			}
			else
			{
			insertCol=insertCol+")";
			}
			//int check=0;
				while (line != null) {
				
				line = br.readLine();
				if (line == null) {
					break;
				}
				perItem = line.split(",");
				String insQuery = "Insert into f15g107_user_table"+insertCol+" values(";
				for (int i = 0; i < perItem.length; i++) {
					userId=perItem[0];
					if(i==pass_pos)
					{
						insQuery=insQuery+"md5('"+perItem[i]+"')";
					}
					else
					{
					insQuery=insQuery+"'"+perItem[i]+"'";
					}
					if(i<perItem.length-1)
					{
						insQuery=insQuery+",";
					}
					
				}
				if(pass_pos==-1)
				{
					insQuery=insQuery+",md5('abc123')";
				}
				insQuery=insQuery+");";
				String message1=new String();
				message1=con.execUpdateQuery(insQuery);
				if(message1!=null)
				{
					if(message1.contains(userId))
					{
						newMessage=newMessage+" "+userId;
					}
				}
				}
				if(newMessage=="" || newMessage.isEmpty() || newMessage==null )
				{
					message ="File Uploaded Successfully";
				
				}
				else
				{
					message="Upload failed for the following userIds :"+newMessage;
				}
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(message));
				con.connClose();
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			String message=e.getMessage();
			e.printStackTrace();
			return message;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		viewTable();
		
		renderFileUpload=false;
		return "SUCCESS";
	}
	
	public String cancelUpload()
	{
		renderFileUpload=false;
		return "SUCCESS";
	}
	private void resetScrollerIndex() {
        if (scroller!=null && scroller.isPaginator())
            scroller.getUIData().setFirst(0);
    }
	
	
	public String exportTableData()
	{
			StringBuffer sb = new StringBuffer();
			String sQuery=new String();
			String path=new String();
			String fname=new String();
			con = new DBConnection();
			Connection c=con.createConnection();
			sQuery="select * from "+tableSelected+";";
			ResultSet rs=con.execQuery(sQuery);
			FileOutputStream fos = null;
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			
			try
			{
			DatabaseMetaData dbmd=c.getMetaData();
			ResultSet colSet = dbmd.getColumns(null, schema, tableSelected, null);
			//ResultSetMetaData rsmdCol=colSet.getMetaData();
			colSet.last();
			int colCount=colSet.getRow();
			colSet.beforeFirst();
			while(colSet.next())
			{
				
				sb.append(colSet.getString("COLUMN_NAME"));
				sb.append(',');
				
			}
			
			sb.append("\n");
			fname=tableSelected+".csv";
			String s="/"+tableSelected+".csv";
			path =fc.getExternalContext().getRealPath(s);
			while(rs.next())
			{
				for(int i=1;i<=colCount;i++)
				{
					String s1=rs.getString(i);
					if(s1.contains(","))
					{
						s1=s1.replaceAll(",", ";");
					}
					
					sb.append(s1);
					if(i<colCount)
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
					return "SUCCESS";
			}
	
	public String uploadFile()
	{
		message="";
		//int check=0;
		//ArrayList<String> perItem1=new ArrayList<String>();
		con = new DBConnection();
		con.createConnection();
		
		
		String[] perItem = null;
		int pass_pos=-1;
		//String userId=new String();
		//String newMessage=new String();
		InputStream is;
		//int check_userid=0;
		try {

			is = uploadedFile.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = br.readLine();
			perItem = line.split(",");
			String insertCol="("+perItem[0];
			//int j=0;
			for (int i = 1; i < perItem.length; i++) {
				insertCol=insertCol+","+perItem[i];	
				if(perItem[i].equalsIgnoreCase("password"))
				{
					pass_pos=i;
				}
					
			}
			if(pass_pos==-1 && tableSelected.equalsIgnoreCase("f15g107_user_table"))
			{
				insertCol=insertCol+",password)";
			}
			else
			{
			insertCol=insertCol+")";
			}
			int checkMsg=0;
				while (line != null) {
				
				line = br.readLine();
				if (line == null) {
					break;
				}
				perItem = line.split(",");
				String insQuery = "Insert into "+tableSelected+" "+insertCol+" values(";
				for (int i = 0; i < perItem.length; i++) {
					//userId=perItem[0];
					if(i==pass_pos && tableSelected.equalsIgnoreCase("f15g107_user_table"))
					{
						insQuery=insQuery+"md5('"+perItem[i]+"')";
					}
					else
					{
					insQuery=insQuery+"'"+perItem[i]+"'";
					}
					if(i<perItem.length-1)
					{
						insQuery=insQuery+",";
					}
					
				}
				if(pass_pos==-1 && tableSelected.equalsIgnoreCase("f15g107_user_table"))
				{
					insQuery=insQuery+",md5('abc123')";
				}
				insQuery=insQuery+");";
				//String message1=new String();
				message=con.execUpdateQuery(insQuery);
				if(message!=null)
				{
					checkMsg=1;
				}
				}
				if(checkMsg==0 )
				{
					message ="File Uploaded Successfully";
				
				}
				else
				{
					message="Upload failed for the following error :"+message;
				}
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
				con.connClose();
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			String message=e.getMessage();
			e.printStackTrace();
			return message;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		viewTable();
		
		renderFileUpload=false;
		return "SUCCESS";
	}

	
}
