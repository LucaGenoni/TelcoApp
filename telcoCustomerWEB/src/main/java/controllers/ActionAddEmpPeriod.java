package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import entities.TblUser;
import exceptions.IntegrityOptionalException;
import exceptions.RoleException;
import services.ProductService;

/**
 * Servlet implementation class PaymentGareway
 */
@WebServlet("/ActionAddEmpPeriod")
public class ActionAddEmpPeriod extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "services/ProductService")
	private ProductService pService;
	
    public ActionAddEmpPeriod() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = getServletContext().getContextPath();
		
		try {
			
			TblUser u = (TblUser) request.getSession().getAttribute("user");
			int months = Integer.parseInt(request.getParameter("months"));
			double fee = Double.parseDouble(request.getParameter("fee"));
			
			pService.createPeriod(months, fee, u.getPK_Users());
			path += "/GoToEmpCreate?successMsg='added period'";	
			
		} catch (RoleException e) {
			path += "/GoToHomePage?errorMsg='"+e.getMessage()+"'";
		} catch (IntegrityOptionalException e) {
			path += "/GoToEmpCreate?errorMsg='"+e.getMessage()+"'";
		} catch (Exception e) {
//			if any error, omit info about it
//			e.printStackTrace();
			path += "/GoToEmpCreate?errorMsg='error adding period'";
		}

		response.sendRedirect(path);
	}

}
