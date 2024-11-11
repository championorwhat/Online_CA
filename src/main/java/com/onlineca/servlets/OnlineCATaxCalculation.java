package com.onlineca.servlets;

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
@WebServlet("/taxCalculation")
public class OnlineCATaxCalculation extends HttpServlet {
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
		
		int salary = request.getParameter("gross_salary") == "" ? 0:Integer.parseInt(request.getParameter("gross_salary"));
		int deductions = request.getParameter("deductions") == "" ? 0:Integer.parseInt(request.getParameter("deductions"));
		int other_sources = request.getParameter("other_sources") == "" ? 0:Integer.parseInt(request.getParameter("other_sources"));
		int med80d = request.getParameter("80D") == "" ? 0:Integer.parseInt(request.getParameter("80D"));
		int med80dd = request.getParameter("80DD") == "" ? 0:Integer.parseInt(request.getParameter("80DD"));
		int med80ddb = request.getParameter("80DDB") == "" ? 0:Integer.parseInt(request.getParameter("80DDB"));
		int inv80c = request.getParameter("80C") == "" ? 0:Integer.parseInt(request.getParameter("80C"));
		int inv80ccc = request.getParameter("80CCC") == "" ? 0:Integer.parseInt(request.getParameter("80CCC"));
		int inv80ccd1b = request.getParameter("80CCD1B") == "" ? 0:Integer.parseInt(request.getParameter("80CCD1B"));
		int don80g = request.getParameter("80G") == "" ? 0:Integer.parseInt(request.getParameter("80G"));
		int don80gg = request.getParameter("80GG") == "" ? 0:Integer.parseInt(request.getParameter("80GG"));
		int don80ggc = request.getParameter("80GGC") == "" ? 0:Integer.parseInt(request.getParameter("80GGC"));
		int int80e = request.getParameter("80E") == "" ? 0:Integer.parseInt(request.getParameter("80E"));
		int int80ee = request.getParameter("80EE") == "" ? 0:Integer.parseInt(request.getParameter("80EE"));
		int int80eeb = request.getParameter("80EEB") == "" ? 0:Integer.parseInt(request.getParameter("80EEB"));
		int int80tta = request.getParameter("80TTA") == "" ? 0:Integer.parseInt(request.getParameter("80TTA"));
		int int80ttb = request.getParameter("80TTB") == "" ? 0:Integer.parseInt(request.getParameter("80TTB"));
		String emailID = ClassForStaticVar.getEmailID();
		String sql = "insert into ca_tax values(" + salary + "," + deductions + "," + other_sources + "," + med80d + "," + med80dd + "," + med80ddb + "," + inv80c + "," + inv80ccc + "," + 
		inv80ccd1b + "," + don80g + "," + don80gg + "," + don80ggc + "," + int80e + "," + int80ee + "," + int80eeb + "," + int80tta + "," + int80ttb + ",'" + emailID + "')";
		int form16 = salary - deductions + other_sources;
		med80d = med80d > 25000 ? 25000:med80d;
		med80dd = med80dd > 75000 ? 75000:med80dd;
		med80ddb = med80ddb > 40000 ? 40000:med80ddb;
		int medical = med80d + med80dd + med80ddb;
		inv80ccd1b = inv80ccd1b > 50000 ? 50000:inv80ccd1b;
		int invest = ((inv80c + inv80ccc) > 150000 ? 150000:(inv80c + inv80ccc)) + inv80ccd1b;
		int donation = (don80g/2) + (don80gg/2) + don80ggc;
		int80ee = int80ee > 200000 ? 200000:int80ee;
		int80eeb = int80eeb > 150000 ? 150000:int80eeb;
		int80tta = int80tta > 10000 ? 10000:int80tta;
		int80ttb = int80ttb > 50000 ? 50000:int80ttb;
		int interest = int80e + int80ee + int80eeb + (int80tta != 0 ? int80tta:int80ttb);
		int taxable_inc = form16 - medical - invest - donation - interest;
		int total_tax_old_regime = taxOldRegime(taxable_inc);
		total_tax_old_regime = (int) (total_tax_old_regime * 1.04);
		int total_tax_new_regime = taxNewRegime(form16);
		total_tax_new_regime = (int) (total_tax_new_regime * 1.04);
		try (Statement statement = connection.createStatement();) {
			int result = statement.executeUpdate(sql);
			PrintWriter out = response.getWriter();
			out.println("<h1>");
			out.println("form16: " + form16);
			out.println("<p>");			
			out.println("medical: " + medical);
			out.println("<p>");			
			out.println("invest: " + invest);
			out.println("<p>");			
			out.println("donation: " + donation);
			out.println("<p>");			
			out.println("interest: " + interest);
			out.println("<p>");			
			out.println("Total taxable income as per old regime: " + taxable_inc);
			out.println("<p>");			
			out.println("Total tax as per old tax regime: " + total_tax_old_regime);
			out.println("<p>");
			out.println("Total taxable income as per new regime: " + form16);
			out.println("<p>");			
			out.println("Total tax as per new tax regime: " + total_tax_new_regime);
			out.println("</h1>");		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int taxOldRegime(int tax_inc) {
		
		if (tax_inc > 1000000) {
			return (int) (112500 + (tax_inc - 1000000) * 0.3);
		}
		else if (tax_inc > 500000) {
			return (int) (12500 + (tax_inc - 500000) * 0.2);
		}
		else if (tax_inc > 250000) {
			return (int) ((tax_inc - 250000) * 0.05);
		}
		
		return 0;
	}

	public int taxNewRegime(int tax_inc) {
		
		if (tax_inc > 1500000) {
			return (int) (140000 + (tax_inc - 1500000) * 0.3);
		}
		else if (tax_inc > 1200000) {
			return (int) (80000 + (tax_inc - 1200000) * 0.2);
		}
		else if (tax_inc > 1000000) {
			return (int) (50000 + (tax_inc - 1000000) * 0.15);
		}
		else if (tax_inc > 700000) {
			return (int) (20000 + (tax_inc - 700000) * 0.1);
		}
		else if (tax_inc > 300000) {
			return (int) ((tax_inc - 300000) * 0.05);
		}
		
		return 0;
	}
	
	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
