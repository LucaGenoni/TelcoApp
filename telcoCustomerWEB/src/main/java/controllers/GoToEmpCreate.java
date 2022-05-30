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

import entities.TblOptional;
import entities.TblPeriod;
import entities.TblService;
import entities.TblUser;
import exceptions.RoleException;
import services.ProductService;
import services.UserService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/GoToEmpCreate")
public class GoToEmpCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	
	@EJB(name = "services/ProductService")
	private ProductService eService;

	@EJB(name = "services/UserService")
	private UserService uService;

	public GoToEmpCreate() {
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
		String path = "/EmpCreate";

		String emsg = request.getParameter("errorMsg");
		if (emsg != null) ctx.setVariable("errorMsg", emsg);
		String smsg = request.getParameter("successMsg");
		if (smsg != null) ctx.setVariable("successMsg", smsg);
		

		try {	
			TblUser u = (TblUser) request.getSession().getAttribute("user");
			uService.checkIfEmployee(u.getPK_Users());
			List<TblService>  srv = eService.findAllServices();
			List<TblPeriod>  prd = eService.findAllPeriods();
			List<TblOptional>  opt = eService.findAllOptionals();
			ctx.setVariable("services", srv);	
			ctx.setVariable("periods", prd);
			ctx.setVariable("optionals", opt);

		} catch (RoleException e) {
//			e.printStackTrace();
			ctx.setVariable("errorMsgGoing", e.getMessage());
		} catch (Exception e) {
//			e.printStackTrace();
			ctx.setVariable("errorMsgGoing", "-Create Cerror ");
		}
		
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
