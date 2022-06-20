package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.OrderAlreadyPaidException;
import services.OrderService;
import services.UserService;

/**
 * Servlet implementation class PaymentGareway
 */
@WebServlet("/ActionResultPayment")
public class ActionResultPayment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "services/OrderService")
	private OrderService oService;

	@EJB(name = "services/UserService")
	private UserService uService;

	public ActionResultPayment() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
//		o = oService.findToBillOfUser(u.getId(),o.getId());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = getServletContext().getContextPath();
		try {

			int idOrder = Integer.parseInt(request.getParameter("idOrder"));
			int idUser = Integer.parseInt(request.getParameter("idUser"));
			byte result = 0;
			if (request.getParameter("Buy") != null)
				result = 1;
			if (request.getParameter("Random") != null)
				result = (byte) ((int) (Math.random() * 2));
			oService.createBill(idOrder,idUser, result);
			// update the user state in the session
			request.getSession().setAttribute("user", uService.findUser(idUser));
			if (result == 0)
				path += "/GoToHomePage?" 
						+ "errorMsg='Bill Rejected'";
			else
				path += "/GoToSchedulePage?" 
						+ "successMsg='Bill Accepted'";
		} catch (OrderAlreadyPaidException e) {
			path += "/GoToHomePage?" + "errorMsg='" + e.getMessage() + "'";
		} catch (Exception e) {
			e.printStackTrace();
			path = "/GoToHomePage?" + "errorMsg='ResultPayment Cerror'";
		}

		response.sendRedirect(path);
	}

}
