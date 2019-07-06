package net.javaguides.login.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/student")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
    
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getServletPath();

        try {
            switch (action) {
                case "/stud":
                	System.out.println("IN ADD");
                	 RequestDispatcher dispatche = request.getRequestDispatcher("student.jsp");
                     dispatche.forward(request, response);
                //    insertProduct(request, response);
                  
                    break;
                case "/student":
                	System.out.println("IN PRODUCT");
                	  RequestDispatcher dispatcher = request.getRequestDispatcher("student.jsp");
                      dispatcher.forward(request, response);
                    break;
			
			// default: insertProduct(request, response); break;
			
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
	}



}
