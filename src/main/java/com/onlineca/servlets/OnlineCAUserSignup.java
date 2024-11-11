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
@WebServlet("/userSignup")
public class OnlineCAUserSignup extends HttpServlet {
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
		
		String emailID = request.getParameter("signup_email");
		String password = request.getParameter("signup_password");
		
//		ClassForStaticVar.setEmailID(emailID);
		
		try (Statement statement = connection.createStatement();) {
			int result = statement.executeUpdate("insert into ca_user_account values('" + emailID + "','" + password + "')");
/*			PrintWriter out = response.getWriter();
			out.println("<h1>");
			out.println("User created.");
			out.println("</h1>");		*/
			RequestDispatcher rd = request.getRequestDispatcher("loginSignup.html");
//			request.setAttribute("message", emailID);
			rd.forward(request, response);

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
