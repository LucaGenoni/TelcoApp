package controllers;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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

import entities.TblPackage;
import services.ProductService;

/**
 * Servlet implementation class GoToBuyPage
 */
@WebServlet("/GoToBuyPage")
public class GoToBuyPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;

	@EJB(name = "services/ProductService")
	private ProductService pService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToBuyPage() {
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
		String path = "/Buy";

		String emsg = request.getParameter("errorMsg");
		if (emsg != null) ctx.setVariable("errorMsg", emsg);
		String smsg = request.getParameter("successMsg");
		if (smsg != null) ctx.setVariable("successMsg", smsg);
		
		try {
			int idpkg = Integer.parseInt(request.getParameter("id"));
			TblPackage  pkg = pService.findSrvpackage(idpkg);
			ctx.setVariable("package", pkg);
			ctx.setVariable("startDay", Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS)));
		}catch(Exception e) {
//			e.printStackTrace();
			ctx.setVariable("errorMsgGoing", "-Buy Cerror ");
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
