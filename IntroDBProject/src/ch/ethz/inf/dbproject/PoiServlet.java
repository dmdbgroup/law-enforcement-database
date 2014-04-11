//edit
package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;


@WebServlet(description = "Displays a specific Person of Interest.", urlPatterns = {"/Poi"})
public final class PoiServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();
	
	public PoiServlet(){
		super();
	}
	
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException{
		final HttpSession session = request.getSession(true);
		
		final String idString = request.getParameter("id");
		if (idString == null){
			this.getServletContext().getRequestDispatcher("/PersonsOfInterest").forward(request,response);
		}
		try{
			
			final Integer id = Integer.parseInt(idString);
			final List<Case> con = this.dbInterface.getCaseByPoiId(id);
			
			final BeanTableHelper<Case> table = new BeanTableHelper<Case>("cases", "cTable", Case.class);
			
			table.addBeanColumn("Title", "title");
			table.addBeanColumn("open", "open");
			table.addBeanColumn("Case Description", "description");
			table.addBeanColumn("location", "streetWithNumber");
			table.addBeanColumn("time", "time");
			
			table.addObjects(con);
			table.setVertical(true);
			
			
			session.setAttribute("includedCases", table);
			
		}
		catch (final Exception ex){
				ex.printStackTrace();
				this.getServletContext().getRequestDispatcher("/Pois.jsp").forward(request, response);
		}
			this.getServletContext().getRequestDispatcher("/Poi.jsp").forward(request, response);
			
	}
	
}
