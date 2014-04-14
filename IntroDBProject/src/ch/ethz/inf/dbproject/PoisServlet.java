//edit
package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.PersonOfInterest;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.AfterRequest;
import ch.ethz.inf.dbproject.util.BeforeRequest;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

@WebServlet(description= "Displays all Persons of Interest.", urlPatterns = { "/Pois"})
public final class PoisServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();
	
	public PoisServlet(){
		super();
	}
	
	private BeanTableHelper<PersonOfInterest> poisTable(User user)
	{
		/*******************************************************
		 * Construct a table to present all properties of a list of Pois
		 *******************************************************/
		final BeanTableHelper<PersonOfInterest> table = new BeanTableHelper<PersonOfInterest>(
				"pois" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				PersonOfInterest.class 	/* The class of the objects (rows) that will be displayed */
		);

		// Add columns to the new table
		table.addBeanColumn("First name", "firstname");
		table.addBeanColumn("Surname", "surname");
		table.addBeanColumn("Birthdate", "birthday");
		
		// Get a page with all informations about this person
		table.addLinkColumn("","View this person", "Poi?id=", "id");

		if(user != null)
		{
			// Get a link to delete this person
			table.addLinkColumn("", "Delete this person", "Pois?action=delete_poi&id=", "id");
		}
		
		
		return table;
	}
	
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final HttpSession session = request.getSession(true);

		BeforeRequest.execute(request);
		final User loggedUser = UserManagement.getCurrentlyLoggedInUser(session);
		
		String action = request.getParameter("action");
		String poi_id_string = request.getParameter("id");
		String filter = request.getParameter("filter");
		
		BeanTableHelper<PersonOfInterest> table = poisTable((User) session.getAttribute(UserManagement.SESSION_USER));
		
		
		if (poi_id_string != null) {
			Integer poi_id = Integer.parseInt(poi_id_string);
			if("delete_poi".equals(action))
			{
				dbInterface.deletePoi(poi_id);
			}
		}
		else if ("add_poi".equals(action)) {
			if (loggedUser == null) {
				session.setAttribute("message", "You have to be logged in for this action");
				table.addObjects(dbInterface.getAllPoi());
			}
			else {
				String firstname = request.getParameter("firstname");
				String surname = request.getParameter("surname");
				String birthdayString = request.getParameter("birthday");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
				
				if (firstname == null || surname == null || birthdayString == null){
					session.setAttribute("message", "An error has occurred. Please try again.");
					table.addObjects(dbInterface.getAllPoi());
				}
				else if (firstname.equals("") || surname.equals("") || birthdayString.equals("")){
					session.setAttribute("message", "Enter all values.");
					table.addObjects(dbInterface.getAllPoi());
				}
				else {
					long ms = 0;
					try
					{
						ms = sdf.parse(birthdayString).getTime();
					} catch (ParseException e)
					{
						session.setAttribute("message", "Please enter a valid date");
						this.getServletContext().getRequestDispatcher("/Pois.jsp").forward(request, response);
						return;
					}
					Date birthday = new Date(ms);
					dbInterface.addPoi(firstname, surname, birthday);
					table.addObjects(dbInterface.getAllPoi());
				}
			}
		} 
		else if (filter != null) {
			
			if(filter.equals("name")) {
				table.addObjects(this.dbInterface.getPoisByName(request.getParameter("nameTerm")));

			} 
			else if (filter.equals("date")) {
				try {
				table.addObjects(this.dbInterface.getPoisByConvDate(request.getParameter("dateTerm")));
				} 
				catch (Exception e) {
					session.setAttribute("message", "Please enter a valid date format.");
					table.addObjects(dbInterface.getAllPoi());
				}

			} 
			else if (filter.equals("type")) {
				table.addObjects(this.dbInterface.getPoisByConvType(request.getParameter("typeTerm")));
			}
			
		}
		else {
			table.addObjects(dbInterface.getAllPoi());
		}
		
		session.setAttribute("pois", table);
		
		AfterRequest.execute(request);
		
		this.getServletContext().getRequestDispatcher("/Pois.jsp").forward(request, response);
	}
}