package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Servlet implementation class GoToBuyPage
 */
@WebServlet("/GoToPaymentPage")
public class GoToPaymentPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToPaymentPage() {
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());		
		String path = "/PaymentGateway";	

		String emsg = request.getParameter("errorMsg");
		if (emsg != null) ctx.setVariable("errorMsg", emsg);
		String smsg = request.getParameter("successMsg");
		if (smsg != null) ctx.setVariable("successMsg", smsg);

		try {
			String idOrder = request.getParameter("idOrder");
			String idUser = request.getParameter("idUser");
			String username = request.getParameter("username");
			String price = request.getParameter("price");
			ctx.setVariable("idOrder", idOrder);
			ctx.setVariable("idUser", idUser);
			
			ctx.setVariable("username", username);
			ctx.setVariable("price", price);
		}catch(Exception e) {
//			e.printStackTrace();
			ctx.setVariable("errorMsgGoing", "lack of information");
		}
		templateEngine.process(path, ctx, response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
