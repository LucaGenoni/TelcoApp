package controllers;

import java.io.IOException;

//import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/GoToSignPage") //name= , urlPatterns="/"
public class GoToSignPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;

	public GoToSignPage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		this.templateEngine = new TemplateEngine();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("/templates");
		templateResolver.setSuffix(".html");
		this.templateEngine.setTemplateResolver(templateResolver);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		String path = "/Sign";

		String emsg = request.getParameter("errorMsg");
		if (emsg != null) ctx.setVariable("errorMsg", emsg);
		String smsg = request.getParameter("successMsg");
		if (smsg != null) ctx.setVariable("successMsg", smsg);
		
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
