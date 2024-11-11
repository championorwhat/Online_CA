package com.onlineca.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class OnlineCAUserLogin
 */
@WebServlet("/userLogin")
public class OnlineCAUserLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	
	public void init() {
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/mydb", "root", "Acc1234$$");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String emailID = request.getParameter("login_email");
		String password = request.getParameter("login_password");
		
		ClassForStaticVar.setEmailID(emailID);
		
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("select count(*) from ca_user_account where emailID = '" + emailID + "' and password = '" + password + "'");)
		{
			resultSet.next();
			PrintWriter out = response.getWriter();
			if (resultSet.getInt(1) == 0) {
				RequestDispatcher rd = request.getRequestDispatcher("loginSignup.html");
				rd.include(request, response);
/*				out.println("<h1>");
				out.println("Invalid Email ID or Password.");
				out.println("</h1>");*/
			}
			else {
//				response.sendRedirect("mainpage.html");
				RequestDispatcher rd = request.getRequestDispatcher("mainpage.html");
//				request.setAttribute("message", emailID);
				rd.forward(request, response);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
