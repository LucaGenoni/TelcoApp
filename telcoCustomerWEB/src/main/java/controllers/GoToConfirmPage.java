package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import entities.TblOrder;
import entities.TblUser;
import exceptions.NoSuchOrderException;
import services.OrderService;

/**
 * Servlet implementation class GoToBuyPage
 */
@WebServlet("/GoToConfirmPage")
public class GoToConfirmPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;

	@EJB(name = "services/OrderService")
	private OrderService oService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoToConfirmPage() {
		super();
	}

	public void init() throws ServletException {
		this.templateEngine = new TemplateEngine();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("/templates");
		templateResolver.setSuffix(".html");
		this.templateEngine.setTemplateResolver(templateResolver);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		String path = "/Confirm";

		String emsg = request.getParameter("errorMsg");
		if (emsg != null)
			ctx.setVariable("errorMsg", emsg);
		String smsg = request.getParameter("successMsg");
		if (smsg != null)
			ctx.setVariable("successMsg", smsg);

		String ido = request.getParameter("id");
		if (ido != null) {
			try {
				request.getSession().removeAttribute("subscription");
				int idOrder = Integer.parseInt(request.getParameter("id"));
				// if there is an id then remove any sub and replace it
				TblUser u = (TblUser) request.getSession().getAttribute("user");
				TblOrder o = oService.findRejectOrder(idOrder, u.getPK_Users());
				request.getSession().setAttribute("subscription", o);
			} catch (NoSuchOrderException e) {
//				e.printStackTrace();
				ctx.setVariable("errorMsgGoing", e.getMessage());
			} catch (Exception e) {
//				e.printStackTrace();
				ctx.setVariable("errorMsgGoing", "Fetching order error");
			}

		}
		templateEngine.process(path, ctx, response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
