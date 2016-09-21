package com.uic.edu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("1 ");
		PrintWriter out=response.getWriter();
		if (!ServletFileUpload.isMultipartContent(request)) {
            // if not, we stop here
            PrintWriter writer = response.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            return;
        }
		readFile(request);
		
		
	}

	private void readFile(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		DiskFileItemFactory factory=new DiskFileItemFactory();
		ServletFileUpload upload=new ServletFileUpload(factory);
		ArrayList<String> insQuery_List=new ArrayList<String>();
		String cQuery="";
		
		System.out.println("2 ");
		String [] perItem=null;
		try {
			List items=upload.parseRequest(request);
			
			Iterator i1=items.iterator();
			while(i1.hasNext())
			{
				FileItem item=(FileItem) i1.next();
				String fname=item.getName();
				String fnameNew=fname.substring(0, fname.indexOf('.'));
				System.out.println("name :"+fnameNew);
				InputStream is=item.getInputStream();
				BufferedReader br=new BufferedReader(new InputStreamReader(is));
				String line=br.readLine();
				perItem=line.split(",");
				cQuery="Create table dbo."+fnameNew+"( ";
				for(int i=0;i<perItem.length;i++)
				{
					cQuery=cQuery+ perItem[i]+ " varchar(10)";
					if(i!=(perItem.length-1))
							{
						cQuery=cQuery+", ";
							}
				}
				cQuery=cQuery+");";
				System.out.println("createQuery :"+cQuery);
				
				while(line!=null){
					line=br.readLine();
					System.out.println("line is :"+line);
					if(line==null)
					{
						break;
					}
					perItem=line.split(",");
					String insQuery="Insert into "+fnameNew+" values(";
					for(int i=0;i<perItem.length;i++)
					{
						insQuery=insQuery+" '"+ perItem[i]+"' ";
						if(i!=(perItem.length-1))
								{
							insQuery=insQuery+",";
								}
					}
					insQuery=insQuery+");";
					System.out.println("inser Query :"+insQuery);
					insQuery_List.add(insQuery);
					
					for(int j=0;j<perItem.length;j++)
					{
						System.out.println("item= "+perItem[j]);
					}
				}
			}
					
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		insertDB(cQuery,insQuery_List);
	}

	private void insertDB(String cQuery, ArrayList<String> insQuery_List) {
		// TODO Auto-generated method stub
		
		try {
			System.out.println("1");
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("2");
			Connection con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test","root","Iram");
			System.out.println("3..............."+cQuery);
			Statement st=con.createStatement();
			st.executeUpdate(cQuery);
			st.close();
			for(int i=0;i<insQuery_List.size();i++)
			{
				System.out.println("4........"+insQuery_List.get(i));
				Statement st1=con.createStatement();
				st1.executeUpdate(insQuery_List.get(i));
				st1.close();
			}	
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
