package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.util.BeforeRequest;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Case list page
 */
@WebServlet(description = "The home page of the project", urlPatterns = { "/Cases" })
public final class CasesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CasesServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) 
			throws ServletException, IOException {

		BeforeRequest.execute(request);
		final HttpSession session = request.getSession(true);

		/*******************************************************
		 * Construct a table to present all our results
		 *******************************************************/
		final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
				"cases" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				Case.class 	/* The class of the objects (rows) that will bedisplayed */
		);

		// Add columns to the new table
		table.addBeanColumn("Title", "title");
		table.addBeanColumn("open", "open");
		table.addBeanColumn("Case Description", "description");
		table.addBeanColumn("location", "address");
		table.addBeanColumn("time", "time");
		table.addBeanColumn("Creator", "creator");

		/*
		 * Column 4: This is a special column. It adds a link to view the
		 * Project. We need to pass the case identifier to the url.
		 */
		table.addLinkColumn(""	/* The header. We will leave it empty */,
				"View Case" 	/* What should be displayed in every row */,
				"Case?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
				"id" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);

		if((User) session.getAttribute(UserManagement.SESSION_USER) != null)
		{
			table.addLinkColumn(""	/* The header. We will leave it empty */,
					"Delete Case" 	/* What should be displayed in every row */,
					"Case?action=delete_case&id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
					"id" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
		}
		// Pass the table to the session. This will allow the respective jsp page to display the table.
		session.setAttribute("cases", table);

		// The filter parameter defines what to show on the Projects page
		final String action = request.getParameter("action");
		final String filter = request.getParameter("filter");
		final String category_id_string = request.getParameter("category_id");

		if("addcase".equals(action))
		{
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String timeString = request.getParameter("time");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			long ms = 0;
			try
			{
				ms = sdf.parse(timeString).getTime();
			} catch (ParseException e)
			{
				e.printStackTrace();
			}
			Time time = new Time(ms);
			String address = request.getParameter("address");
			String creator = request.getParameter("creator");
			dbInterface.addCase(title, description, time, address, creator, true);
			table.addObjects(this.dbInterface.getAllCases());
		}
		else if (filter == null && category_id_string == null) {

			// If no filter is specified, then we display all the cases!
			table.addObjects(this.dbInterface.getAllCases());

		} else if (category_id_string != null) {

			// filter cases by category name
			table.addObjects(this.dbInterface.getProjectsByCategory(Integer.parseInt(category_id_string.trim())));
			
		} else if (filter != null) {
		
			if(filter.equals("open")) {
				table.addObjects(this.dbInterface.getCasesByStatus(true));

			} else if (filter.equals("closed")) {
				table.addObjects(this.dbInterface.getCasesByStatus(false));

			} else if (filter.equals("recent")) {
				table.addObjects(this.dbInterface.getMostRecentCases());
			}
			
			else if (filter.equals("oldest")) {
				table.addObjects(this.dbInterface.getOldestUnsolvedCases());
			}
			
			else if(filter.equals("description")) {
				table.addObjects(this.dbInterface.searchCasesByDescription(request.getParameter("description")));
			}
			
			else if(filter.equals("category")) {
				table.addObjects(this.dbInterface.searchCasesBySimilarCategory(request.getParameter("categoryTerm")));
			}
			
			else if(filter.equals("title")) {
				table.addObjects(this.dbInterface.searchCasesBySimilarTitle(request.getParameter("titleTerm")));
			}
			
		} else {
			throw new RuntimeException("Code should not be reachable!");
		}

		// Finally, proceed to the Projects.jsp page which will render the Projects
		this.getServletContext().getRequestDispatcher("/Cases.jsp").forward(request, response);
	}
}