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

import ch.ethz.inf.dbproject.model.CaseComment;
import ch.ethz.inf.dbproject.model.Conviction;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.PersonOfInterest;
import ch.ethz.inf.dbproject.model.PoiComment;
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
			return;
		}
		final Integer id = Integer.parseInt(idString);
		String action = request.getParameter("action");
		if (action != null && "add_comment".equals(action.trim())) {
			String text = request.getParameter("comment");
			dbInterface.addPoiComment(id,text);
		}
		try{
			final PersonOfInterest aPoi = this.dbInterface.getPoiById(id);
			final List<Conviction> cons = dbInterface.getConvictionsForPoiId(id);
			final List<PoiComment> notes = dbInterface.getNotesForPoiId(id);
			
			final BeanTableHelper<PersonOfInterest> poiTable = poiTable(aPoi);
			final BeanTableHelper<Conviction> consTable = convictionTable(cons);
			final BeanTableHelper<PoiComment> notesTable = comsTable(notes);
			
			session.setAttribute("poi_id", id);
			session.setAttribute("poiTable", poiTable);
			session.setAttribute("consTable", consTable);	
			session.setAttribute("notesTable", notesTable);
			
		}
		catch (final Exception ex){
				ex.printStackTrace();
				this.getServletContext().getRequestDispatcher("/Pois.jsp").forward(request, response);
		}
		this.getServletContext().getRequestDispatcher("/Poi.jsp").forward(request, response);
			
	}
	
	private BeanTableHelper<PersonOfInterest> poiTable(final PersonOfInterest p)
	{
		/*******************************************************
		 * Construct a table to present all properties of a case
		 *******************************************************/
		final BeanTableHelper<PersonOfInterest> table = new BeanTableHelper<PersonOfInterest>(
				"cases" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				PersonOfInterest.class 	/* The class of the objects (rows) that will be displayed */
		);

		// Add columns to the new table
		table.addBeanColumn("Firstname", "firstname");
		table.addBeanColumn("Surmame", "surname");
		table.addBeanColumn("Birthdate", "birthday");

		table.addObject(p);
		table.setVertical(true);
		
		return table;
	}
	
	private BeanTableHelper<PoiComment> comsTable(final List<PoiComment> notes)
	{
		/*******************************************************
		 * Construct a table to present all notes of a case
		 *******************************************************/
		final BeanTableHelper<PoiComment> table = new BeanTableHelper<PoiComment>(
				"notes" 		/* The table html id property */,
				"casesTable" /* The table html class property; The same layout as the case table*/,
				PoiComment.class 	/* The class of the objects (rows) that will be displayed */
		);

		// Add columns to the new table
		table.addBeanColumn("text", "text");

		table.addObjects(notes);
		
		return table;
	}
	
	private BeanTableHelper<Conviction> convictionTable(final List<Conviction> convictions)
	{
		/*******************************************************
		 * Construct a table to present all notes of a case
		 *******************************************************/
		final BeanTableHelper<Conviction> table = new BeanTableHelper<Conviction>(
				"conviction" 		/* The table html id property */,
				"casesTable" /* The table html class property; The same layout as the case table*/,
				Conviction.class 	/* The class of the objects (rows) that will be displayed */
		);

		// Add columns to the new table
		table.addBeanColumn("case", "case");
		table.addBeanColumn("category", "category");
		table.addBeanColumn("start date", "date");
		table.addBeanColumn("end date", "endDate");
			
		table.addObjects(convictions);
		
		return table;
	}
	
}
