package controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.OrderService;

/**
 * Servlet implementation class GoToBuyPage
 */
@WebServlet("/ActionAddSubscription")
public class ActionAddSubscription extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "services/OrderService")
	private OrderService oService;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActionAddSubscription() {
        super();
    }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = getServletContext().getContextPath();
				
		try {
//			create a subscription
			Date date = new SimpleDateFormat("yyyy-mm-dd").parse(request.getParameter("startDate"));
			int idpkg = Integer.parseInt(request.getParameter("IDpackage"));
			int idp = Integer.parseInt(request.getParameter("IDperiod"));
			String[] idopts =request.getParameterValues("IDoptionals");
			List<Integer> idoptList = null;
			if (idopts!=null) idoptList = Arrays.asList(idopts).stream()
					.map(s -> Integer.parseInt(s))
					.collect(Collectors.toList());	
			
			request.getSession().setAttribute("subscription", 
					oService.createSubscription(date, idp, idpkg, idoptList));
			System.out.println("ADD SUBSCRIPTION:"+oService.createSubscription(date, idp, idpkg, idoptList).toString());
			path += "/GoToConfirmPage?successMsg='added sub'";
			
		}catch(Exception e) {
//			e.printStackTrace();
			path += "/GoToConfirmPage?errorMsg='error adding sub'";
		}
		
		response.sendRedirect(path);
	}

}
