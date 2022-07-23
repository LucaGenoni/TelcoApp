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
import exceptions.*;
import services.ProductService;

/**
 * Servlet implementation class PaymentGareway
 */
@WebServlet("/ActionAddEmpService")
public class ActionAddEmpService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "services/ProductService")
	private ProductService pService;

	public ActionAddEmpService() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = getServletContext().getContextPath();

		try {

			TblUser u = (TblUser) request.getSession().getAttribute("user");
			String type = StringEscapeUtils.escapeJava(request.getParameter("type"));
			int sms, min, gb; 
			double feeSms, feeMin, feeGb;
			try {
				sms = Integer.parseInt(request.getParameter("sms"));
				feeSms = Double.parseDouble(request.getParameter("feeSms"));
			}catch (NumberFormatException e) {
				sms = 0;
				feeSms = 0;
			}
			try {
				min = Integer.parseInt(request.getParameter("min"));
				feeMin = Double.parseDouble(request.getParameter("feeMin"));
			}catch (NumberFormatException e) {
				min = 0;
				feeMin = 0;
			}
			try {
				gb = Integer.parseInt(request.getParameter("gb"));
				feeGb = Double.parseDouble(request.getParameter("feeGb"));
			}catch (NumberFormatException e) {
				gb = 0;
				feeGb = 0;
			}
			pService.createService(type, sms, min, gb, feeSms, feeMin, feeGb, u.getPK_Users());
			path += "/GoToEmpCreate?successMsg='added service'";

		} catch (RoleException e) {
			path += "/GoToHomePage?errorMsg='" + e.getMessage() + "'";
		} catch (IntegrityServiceException e) {
			path += "/GoToEmpCreate?errorMsg='" + e.getMessage() + "'";
		} catch (Exception e) {
//			if any error, omit info about it
//			e.printStackTrace();
			path += "/GoToEmpCreate?errorMsg='error adding service'";
		}

		response.sendRedirect(path);
	}

}
