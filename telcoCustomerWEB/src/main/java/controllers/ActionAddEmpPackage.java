package controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import entities.TblUser;
import exceptions.IntegrityPackageException;
import exceptions.RoleException;
import services.ProductService;

/**
 * Servlet implementation class PaymentGareway
 */
@WebServlet("/ActionAddEmpPackage")
public class ActionAddEmpPackage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "services/ProductService")
	private ProductService pService;
	
    public ActionAddEmpPackage() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = getServletContext().getContextPath();

			
		try {

			TblUser u = (TblUser) request.getSession().getAttribute("user");
			String name = StringEscapeUtils.escapeJava(request.getParameter("name"));
			ArrayList<Integer> idservices = new ArrayList<>();
			ArrayList<Integer> idperiods = new ArrayList<>();
			ArrayList<Integer> idoptionals = new ArrayList<>();
			for (String id : request.getParameterValues("services"))
				idservices.add(Integer.parseInt(id));
			for (String id : request.getParameterValues("periods"))
				idperiods.add(Integer.parseInt(id));
			for (String id : request.getParameterValues("optionals"))
				idoptionals.add(Integer.parseInt(id));
			
			pService.createSrvpackage(name, idservices, idperiods, idoptionals, u.getPK_Users());
			path += "/GoToEmpCreate?successMsg='added package'";	

		} catch (RoleException e) {
			path += "/GoToHomePage?errorMsg='"+e.getMessage()+"'";
		} catch (IntegrityPackageException e) {
			path += "/GoToEmpCreate?errorMsg='"+e.getMessage()+"'";
		} catch (Exception e) {
//			if any error, omit info about it
//			e.printStackTrace();
			path += "/GoToEmpCreate?errorMsg='error adding package'";
		}

		response.sendRedirect(path);
	}

}
