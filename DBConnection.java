package com.uic.edu;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.DatabaseMetaData;

@ManagedBean
@SessionScoped
public class DBConnection {
	private Connection con=null;
	private String driver_str;
	private String url;
	//="jdbc:mysql://131.193.209.54:3306/f15g107";
	private String username;
	private String password;
	private String message;
	private String databaseName;
	private String hostName;
	private String schema;
	private static final int DEFAULT_BUFFER_SIZE = 10240;
	private FacesContext context;
	
	@PostConstruct
	  public void init() {
		  username="f15g107";
		  hostName="131.193.209.54";
		  schema="f15g107";
		  databaseName="mysql";
		  context=FacesContext.getCurrentInstance();
		  ExternalContext externalContext = context.getExternalContext();
		  Map<String, Object> sessionMap = externalContext.getSessionMap();
	  }
	  
	
	public Connection getCon() {
		return con;
	}


	public void setCon(Connection con) {
		this.con = con;
	}


	public String getUsername() {
		
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
		
	}
	public String getPassword() {
		
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
		
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String connectToDB()
	{
		message="";
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("username_S", username);
		sessionMap.put("password_S", password);
		if(databaseName.equalsIgnoreCase("mysql"))
		{
	    	driver_str="com.mysql.jdbc.Driver";
	    	url = "jdbc:mysql://" +
	    			hostName + ":3306/" +
	    			schema;
	    	sessionMap.put("driver_S",driver_str);
	    	sessionMap.put("url_S",url);
	   	}
		else if(databaseName.equalsIgnoreCase("db2"))
		{
	    	driver_str="com.ibm.db2.jcc.DB2Driver";
	    	url = "jdbc:db2://" +
	    			hostName + ":50000/" +
	    			schema;
	    	sessionMap.put("driver_S",driver_str);
	    	sessionMap.put("url_S",url);
	   
				}
	    	else if(databaseName.equalsIgnoreCase("oracle"))
	    	{
	    		driver_str="oracle.jdbc.driver.OracleDriver";
	    	url = "jdbc:oracle:thin:@" +
	    			hostName + ":1521:" +
	    			schema;
	    	sessionMap.put("driver_S",driver_str);
	    	sessionMap.put("url_S",url);
	   
	    	}
		
		try
		{
		createConnection();
		if(con!=null)
		{
			return "/Login.jsp?faces-redirect=true";
		}
		else
		{
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Connection failed"));
			return "False";
		}
		}catch(Exception e)
		{	
			
			return "false";
		}
		
	}
	public Connection createConnection()
	{
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		String username1 = (String) sessionMap.get("username_S");
		String password1=	(String) sessionMap.get("password_S");
		
		String driver1=(String) sessionMap.get("driver_S");
		
		String url1=(String) sessionMap.get("url_S");
		try {
	    	Class.forName(driver1);
			con = DriverManager.getConnection(url1,username1,password1);
		} catch (SQLException e) {
			message="Connection failed "+e.getMessage();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	public void connClose()
	{
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public String execUpdateQuery(String query) {
		// TODO Auto-generated method stub
		int count=0;
		
		createConnection();
		Statement st;
		try {
			if(con!=null)
			{
			
			st = con.createStatement();
			
			count=st.executeUpdate(query);
			
			st.close();
			}
			else
			{
				
			}
		} catch (SQLException e) {
			message=e.getMessage();
			return message;
			//System.out.println("error :"+message);
			//e.printStackTrace();
		}
		String countS=""+count;
		
		return message;
	}
	public ResultSet execQuery(String query) {
		// TODO Auto-generated method stub
		Statement st;
		createConnection();
		ResultSet rs=null;
		try {
			
			st = con.createStatement();
			
			rs=st.executeQuery(query);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public String downloadPDF2()
	{
		context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		FileOutputStream fos = null;
		String path=ec.getRealPath("/EADPresentation1.pdf");
		File f = new File(path);
		StringBuffer sb=new StringBuffer();
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
		path + "\"");
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
		return "success";
	}
	
	public String downloadPDF1()
	{
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String path=externalContext.getRealPath("/EADPresentation1.pdf");
        String fname="EADPresentation1.pdf";
        File file = new File(path);
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            response.reset();
            response.setHeader("Content-Type", "application/pdf");
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + fname + "\"");
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
            	//String str = new String(buffer, "UTF-8");
                output.write(buffer, 0, length);
            }

            // Finalize task.
            output.flush();
        }catch(FileNotFoundException e)
        {
        e.printStackTrace();	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally {
            // Gently close streams.
            close(output);
            close(input);
        }

        facesContext.responseComplete();
		return "SUCCESS";
	}
	
	
	public String downloadPDF()
	{
		
		context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String path=externalContext.getRealPath("/temp");
        String fname="f15g107_Documentation.pdf";
        String fileName=path+"/"+fname;
        File file = new File(fileName);
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        try {
            // Open file.
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            response.reset();
            response.setHeader("Content-Type", "application/pdf");
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + fname + "\"");
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
            	String str = new String(buffer, "UTF-8");
                output.write(buffer, 0, length);
            }

            // Finalize task.
            output.flush();
        }catch(FileNotFoundException e)
        {
        e.printStackTrace();	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally {
            // Gently close streams.
            close(output);
            close(input);
        }

        context.responseComplete();
		return "SUCCESS";
	}
	public String downloadUG()
	{
		
		context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String path=externalContext.getRealPath("/temp");
        
        String fname="f15g107_UserGuide.pdf";
        String fileName=path+"/"+fname;
        File file = new File(fileName);
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        
        try {
            // Open file.
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);

            // Init servlet response.
            response.reset();
            response.setHeader("Content-Type", "application/pdf");
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + fname + "\"");
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
        
            	//String str = new String(buffer, "UTF-8");
                output.write(buffer, 0, length);
            }

            // Finalize task.
            output.flush();
        }catch(FileNotFoundException e)
        {
        e.printStackTrace();	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally {
            // Gently close streams.
            close(output);
            close(input);
        }

        context.responseComplete();
		return "SUCCESS";
	}
	
	public String downloadPG()
	{
		
		context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String path=externalContext.getRealPath("/temp");
        
        String fname="f15g107_ProgrammerGuide.pdf";
        String fileName=path+"/"+fname;
        File file = new File(fileName);
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        
        try {
            // Open file.
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);

            // Init servlet response.
            response.reset();
            response.setHeader("Content-Type", "application/pdf");
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + fname + "\"");
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
        
            	//String str = new String(buffer, "UTF-8");
                output.write(buffer, 0, length);
            }

            // Finalize task.
            output.flush();
        }catch(FileNotFoundException e)
        {
        e.printStackTrace();	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally {
            // Gently close streams.
            close(output);
            close(input);
        }

        context.responseComplete();
		return "SUCCESS";
	}
	
	
	private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it. It may be useful to 
                // know that this will generally only be thrown when the client aborted the download.
                e.printStackTrace();
            }
        }
    }
}

