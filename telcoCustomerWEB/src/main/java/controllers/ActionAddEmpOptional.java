package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import entities.TblUser;
import exceptions.IntegrityOptionalException;
import exceptions.RoleException;
import services.ProductService;

/**
 * Servlet implementation class PaymentGareway
 */
@WebServlet("/ActionAddEmpOptional")
public class ActionAddEmpOptional extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "services/ProductService")
	private ProductService pService;
	
    public ActionAddEmpOptional() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
//		o = oService.findToBillOfUser(u.getId(),o.getId());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = getServletContext().getContextPath();
		
		try {

			TblUser u = (TblUser) request.getSession().getAttribute("user");
			String name = StringEscapeUtils.escapeJava(request.getParameter("name"));
			double fee = Double.parseDouble(request.getParameter("fee"));
			pService.createOptional(name, fee, u.getPK_Users());
			path += "/GoToEmpCreate?successMsg='added optional'";	
			
		} catch (RoleException e) {
			path += "/GoToHomePage?errorMsg='"+e.getMessage()+"'";
		} catch (IntegrityOptionalException e) {
			path += "/GoToEmpCreate?errorMsg='"+e.getMessage()+"'";
		} catch (Exception e) {
//			if any error, omit info about it
//			e.printStackTrace();
			path += "/GoToEmpCreate?errorMsg='error adding optional'";
		}

		response.sendRedirect(path);
	}

}
