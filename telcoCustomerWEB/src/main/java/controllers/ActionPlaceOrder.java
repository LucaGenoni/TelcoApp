package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.TblOrder;
import entities.TblUser;
import services.OrderService;

/**
 * Servlet implementation class PaymentGareway
 */
@WebServlet("/ActionPlaceOrder")
public class ActionPlaceOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "services/OrderService")
	private OrderService oService;

	public ActionPlaceOrder() {
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
			TblOrder o = (TblOrder) request.getSession().getAttribute("subscription");

			if (o != null) {
				TblOrder oToBuy;
				if (o.getPK_Orders() > 0)
					oToBuy = oService.findRejectOrder(o.getPK_Orders(), u.getPK_Users());
				else
					oToBuy = oService.createOrder(u, o);
				request.getSession().removeAttribute("subscription");

				// fake request of payment
				path += "/GoToPaymentPage?idOrder=" + oToBuy.getPK_Orders() 
						+ "&idUser=" + u.getPK_Users() 
						+ "&username=" + u.getUsername() 
						+ "&price=" + oToBuy.getPrice();
			} else
				path += "/GoToConfirmPage?errorMsg='no subscription to order'";

		} catch (Exception e) {
//			if any error, omit info about it
//			e.printStackTrace();
			path += "/GoToConfirmPage?errorMsg='error placing order'";
		}

		response.sendRedirect(path);
	}

}
