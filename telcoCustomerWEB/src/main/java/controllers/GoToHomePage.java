package controllers;

import java.io.IOException;
//import it.polimi.db2.musicwishlist.services.*;
//import it.polimi.db2.musicwishlist.entities.*;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
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

import entities.TblOrder;
import entities.TblPackage;
import entities.TblUser;
import services.ProductService;
import services.UserService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/GoToHomePage")
public class GoToHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	
	@EJB(name = "services/ProductService")
	private ProductService pService;
	
	@EJB(name = "services/UserService")
	private UserService uService;
	
	public GoToHomePage() {
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
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path = "/Home";

		String emsg = request.getParameter("errorMsg");
		if (emsg != null) ctx.setVariable("errorMsg", emsg);
		String smsg = request.getParameter("successMsg");
		if (smsg != null) ctx.setVariable("successMsg", smsg);
		

		try {
//			update user info if signed in
			TblUser u = (TblUser) request.getSession().getAttribute("user");
			if (u != null) {
//				fetch rejected orders
				List<TblOrder>  rejected = uService.findAllRejectedOrdersOfUser(u.getPK_Users());
				if(rejected!=null && !rejected.isEmpty())
					ctx.setVariable("rejected", rejected);				
			}	

			List<TblPackage>  pkg = pService.findAllPackages();
			ctx.setVariable("packages", pkg);
		} catch (Exception e) {
//			e.printStackTrace();
			ctx.setVariable("errorMsgGoing", "- Critical error");
		}
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
