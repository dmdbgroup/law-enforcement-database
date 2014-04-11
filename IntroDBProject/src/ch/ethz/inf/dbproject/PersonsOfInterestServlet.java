//edit
package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.PersonOfInterest;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;
import ch.ethz.inf.dbproject.model.PersonOfInterestComment;

@WebServlet(description= "Displays all Persons of Interest.", urlPatterns = { "/PersonsOfInterest"})
public final class PersonsOfInterestServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();
	
	public PersonsOfInterestServlet(){
		super();
	}
	
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final HttpSession session = request.getSession(true);
	
		final BeanTableHelper<PersonOfInterest> table = new BeanTableHelper<PersonOfInterest>(
				"poi", "poiTable", PersonOfInterest.class);
		final BeanTableHelper<PersonOfInterestComment> table2 = new BeanTableHelper<PersonOfInterestComment>(
				"poiNote", "poiNoteTable", PersonOfInterestComment.class);
		
		table.addBeanColumn("ID", "id");
		table.addBeanColumn("Name", "name");
		table.addBeanColumn("Birthdate", "birthday");
		table.addLinkColumn("","View Cases", "Poi?id=", "id");
		
		table2.addBeanColumn("ID","poi_id");
		table2.addBeanColumn("Comment","text");
		table2.addLinkColumn("","View Cases", "Poi?id=","poi_id");
		
		session.setAttribute("pois", table);
		session.setAttribute("comments", table2);
		
		List<PersonOfInterest> l = this.dbInterface.getAllPoi();
		table.addObjects(l);
		
		List<Integer> i = new ArrayList<Integer>();
		
		
		
		for(PersonOfInterest p: l){
			i.add(p.getId());
			}
		
		table2.addObjects(this.dbInterface.getNotesForPoiIds(i));

		this.getServletContext().getRequestDispatcher("/Pois.jsp").forward(request, response);
	}
}