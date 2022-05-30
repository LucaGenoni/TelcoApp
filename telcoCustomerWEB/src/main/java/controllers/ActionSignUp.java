package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import exceptions.CredentialsException;
import services.UserService;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/ActionSignUp")
public class ActionSignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "services/UserService")
	private UserService uService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActionSignUp() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = getServletContext().getContextPath() + "/GoToSignPage?";
		try {
			String usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			String email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			String pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
			if (usrn == null || email == null || pwd == null || usrn.isEmpty() || email.isEmpty() || pwd.isEmpty())
				throw new CredentialsException("SignUp failed.");
			String isEmploye = StringEscapeUtils.escapeJava(request.getParameter("employee"));

			if (isEmploye == null) uService.createUser(usrn, email, pwd, 0);
			else uService.createUser(usrn, email, pwd, 1);
			path += "successMsg='SignUp correctly'";
		} catch (CredentialsException e) {
			path += "errorMsg='"+e.getMessage()+"'";			
		} catch (Exception e) {
//			if any error, omit info about it
//			e.printStackTrace();
			path += "errorMsg='SignUp Cerror'";
		}

		response.sendRedirect(path);
	}
}
