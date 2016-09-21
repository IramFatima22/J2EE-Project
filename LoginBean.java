package com.uic.edu;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable, Cloneable {
		  private String username;
		  private String password;
		  private String firstName;
		  private Integer userId;
		  private String role;
		  private DBConnection con;
		  private ResultSet rs;
		  private boolean renderAuthentication;
		  private String message;
		  private boolean renderPasswordChange=false;
		  
		  @PostConstruct
		  public void init() {
			  username="Adm22";
			  password="Oct2015";
			  renderAuthentication=false;
		  }
		  
		

		public boolean isRenderPasswordChange() {
			return renderPasswordChange;
		}



		public void setRenderPasswordChange(boolean renderPasswordChange) {
			this.renderPasswordChange = renderPasswordChange;
		}



		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public boolean isRenderAuthentication() {
			return renderAuthentication;
		}

		public void setRenderAuthentication(boolean renderAuthentication) {
			this.renderAuthentication = renderAuthentication;
		}

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public String getUsername() {
		    return this.username;
		  }

		  public void setUsername(String username) {
		    this.username = username;
		  }

		  public String getPassword() {
		    return this.password;
		  }

		  public void setPassword(String password) {
		    this.password = password;
		  }
		  
		  public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String checkValidity() {
			if(username.equalsIgnoreCase("Adm22") && password.equals("Oct2015"))
			{
				
				return "admin";
			}
			else
			{
			String userId_str=new String();
			int check=0;
			try {
				String sQuery="select * from f15g107_User_Table;";
				con = new DBConnection();
				con.createConnection();
				rs=con.execQuery(sQuery);
				String passwordHash=convertToHash(password);
				while(rs.next())
				{
					String userName_db=rs.getString(4);
					String password_db=rs.getString(5);
					if(username.equalsIgnoreCase(userName_db) && 
							passwordHash.equals(password_db))
					{
						userId_str=rs.getString(1);
						this.userId=rs.getInt(1);
						this.firstName=rs.getString(2);
						this.role=rs.getString(6);
						check=1;
						break;
						
					}
				}
				
				con.connClose();
				if(check==0)
				{
					renderAuthentication=true;
					return "false";
				}
				else
				{
						ExternalContext ec=FacesContext.getCurrentInstance().getExternalContext();
						HttpServletRequest request=(HttpServletRequest)ec.getRequest();
						Date date= new Date();
						Timestamp timestamp=new Timestamp(date.getTime());
						String ipAddr=request.getHeader("X-FORWARDED-FOR");
						if(ipAddr==null || ipAddr.isEmpty())
						{
							ipAddr=request.getRemoteAddr();
						}
						String iQuery="insert into f15g107_loggingdetails_table(UserId,IpAddress,DateTime,Action)"
								+ " values ("+this.userId+",'"+ipAddr+"','"+timestamp+"','login');";
						
						con = new DBConnection();
						con.createConnection();
						message=con.execUpdateQuery(iQuery);
						con.connClose();
					
					
					if(role.equalsIgnoreCase("student"))
						return "student";
					else if(role.equalsIgnoreCase("instructor") || role.equalsIgnoreCase("TA"))
						return "instructor";
					else if(role.equalsIgnoreCase("admin"))
					{
						return "admin";
					}
					else
					{
						renderAuthentication=true;
						return "false";
					}
				}
				}catch(Exception e)
				{
					e.printStackTrace();
					return "false";
				}
			}
		  }
		
		public String convertToHash(String pass)
		{
			MessageDigest m;
			String hashtext="";
			try {
				m = MessageDigest.getInstance("MD5");
			
			m.reset();
			m.update(pass.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while(hashtext.length() < 32 ){
			  hashtext = "0"+hashtext;
			  
			}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return hashtext;
	   
		}
		public String logout() {
			
			if(this.userId!=null)
			{
			
			ExternalContext ec=FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest request=(HttpServletRequest)ec.getRequest();
			Date date= new Date();
			Timestamp timestamp=new Timestamp(date.getTime());
			String ipAddr=request.getHeader("X-FORWARDED-FOR");
			if(ipAddr==null || ipAddr.isEmpty())
			{
				ipAddr=request.getRemoteAddr();
			}
			String iQuery="insert into f15g107_loggingdetails_table(UserId,IpAddress,DateTime,Action)"
					+ " values ("+this.userId+",'"+ipAddr+"','"+timestamp+"','logout');";
			
			try
			{
				con = new DBConnection();
				con.createConnection();
				message=con.execUpdateQuery(iQuery);
				con.connClose();
			
			}catch(Exception e)
			{
			}
			}
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			return "/Home.jsp?faces-redirect=true";
		}
		
		public String changePassword()
		{
			renderPasswordChange=true;
			return "SUCCESS";
		}


}
