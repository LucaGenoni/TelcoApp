package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import entities.TblUser;
import exceptions.CredentialsException;
import services.UserService;

/**
 * Servlet implementation class SignIn
 */
@WebServlet("/ActionSignIn")
public class ActionSignIn extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB(name = "services/UserService")
	private UserService uService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActionSignIn() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = getServletContext().getContextPath();

		try {
			String usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			String pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
			if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty())
				throw new CredentialsException("SignIp failed. (Empty)");

			TblUser user = uService.checkCredentials(usrn, pwd);
			request.getSession().setAttribute("user", user);
			
			if (request.getSession().getAttribute("subscription") == null)
				if (user.getRole()==0) path += "/GoToHomePage?";
				else path += "/GoToEmpCreate?";
			else path += "/GoToConfirmPage?";
			path += "successMsg='SignIn correctly'";			
		} catch ( CredentialsException e) {
			path += "/GoToSignPage?errorMsg='" + e.getMessage() + "'";
		} catch (PersistenceException e) {
//			e.printStackTrace();
			path += "/GoToSignPage?errorMsg='SignIn Cerror'";
		}
		response.sendRedirect(path);
	}
}
