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

import entities.TblAlert;
import entities.TblOrder;
import entities.TblUser;
import entities.Vw_Report134;
import entities.Vw_Report2;
import entities.Vw_Report6Best;
import exceptions.RoleException;
import services.ReportService;
import services.UserService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/GoToEmpReports")
public class GoToEmpReports extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	
	@EJB(name = "services/ReportService")
	private ReportService rService;

	@EJB(name = "services/UserService")
	private UserService uService;

	public GoToEmpReports() {
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
		String path = "/EmpReports";

		String emsg = request.getParameter("errorMsg");
		if (emsg != null) ctx.setVariable("errorMsg", emsg);
		String smsg = request.getParameter("successMsg");
		if (smsg != null) ctx.setVariable("successMsg", smsg);

		try {	
			TblUser u = (TblUser) request.getSession().getAttribute("user");
			uService.checkIfEmployee(u.getPK_Users());
			
			List<Vw_Report134>  r134 = rService.findAllVw_Report134();
			List<Vw_Report2>  r2 = rService.findAllVw_Report2();
			List<TblUser>  r5user = rService.findAllInsolventTblUser();
			List<TblOrder>  r5order = rService.findAllRejectedTblOrder();
			List<TblAlert>  r5alert = rService.findAllTblAlert();
			List<Vw_Report6Best>  r6 = rService.findAllVw_Report6Best();
			
			ctx.setVariable("report134", r134);	
			ctx.setVariable("report2", r2);
			ctx.setVariable("insolventUsers", r5user);
			ctx.setVariable("rejectedOrders", r5order);	
			ctx.setVariable("alerts", r5alert);	
			ctx.setVariable("bestOpt", r6);	

		} catch (RoleException e) {
//			e.printStackTrace();
			ctx.setVariable("errorMsgGoing", e.getMessage());
		} catch (Exception e) {
//			e.printStackTrace();
			ctx.setVariable("errorMsgGoing", "reports Cerror");
		}
		
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
