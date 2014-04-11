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
import ch.ethz.inf.dbproject.util.AfterRequest;
import ch.ethz.inf.dbproject.util.BeforeRequest;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Case Details Page
 */
@WebServlet(description = "Displays a specific case.", urlPatterns = { "/Case" })
public final class CaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();
	private Integer id;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CaseServlet() {
		super();
	}
	
	private String getQuotedId()
	{
		return "\""+id+"\"";
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		BeforeRequest.execute(request);
		final HttpSession session = request.getSession(true);

		final String idString = request.getParameter("id");
		if (idString == null) {
			this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
			return;
		}

		try {
			id = Integer.parseInt(idString);
			session.setAttribute("case_id", id);

			// Check if a toggleOpen request was sent
			final String action = request.getParameter("action");
			if("toggleOpen".equals(action))
			{
				dbInterface.toggleCaseOpen(id);
			}			
			else if("add_comment".equals(action))
			{
				String text = request.getParameter("comment");
				dbInterface.addCaseComment(id,text);
			}
			else if("delete_case".equals(action))
			{
				dbInterface.deleteCase(id);
				AfterRequest.execute(request);
				this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
				return;
			}
			else if ("delete_link".equals(action)){
				String poi_id_string = request.getParameter("poi_id");
				if (poi_id_string != null && !poi_id_string.equals("")) {
					session.setAttribute("message", "link was successfully removed.");
					dbInterface.removeLink(id, Integer.parseInt(poi_id_string));
				}
			}
			else if ("update_date".equals(action)){
				String[] pois = request.getParameterValues("poi_id");
				String[] end_dates = request.getParameterValues("newenddates");
				String[] start_dates = request.getParameterValues("newstartdates");
				int a = pois.length;
				for (int i = 0; i < a; i++) {
					int poi_id = Integer.parseInt(pois[i]);
					String end_date = end_dates[i];
					String start_date = start_dates[i];
					dbInterface.updateConvictionDates(id, poi_id, start_date, end_date);
					session.setAttribute("message", "dates were successfully updated");
				}
			}

						final Case aCase = this.dbInterface.getCaseById(id);
			final List<CaseComment> notes = this.dbInterface.getNotesForCaseId(id);
			final List<Conviction> convictions = this.dbInterface.getConvictionsForCaseId(id);

			session.setAttribute("case_open", aCase.getOpen());
			session.setAttribute("caseTable", caseTable(aCase));	
			session.setAttribute("notesTable", notesTable(notes));
			session.setAttribute("convictionsTable", convictionTable(convictions, (UserManagement.getCurrentlyLoggedInUser(session)!=null), aCase));
			
		} catch (final Exception ex) {
			ex.printStackTrace();
			request.setAttribute("id", idString);
			session.setAttribute("message", "an error has occurred");
			this.getServletContext().getRequestDispatcher("/Case").forward(request, response);
			return;
		}
		AfterRequest.execute(request);
		this.getServletContext().getRequestDispatcher("/Case.jsp").forward(request, response);
	}
	
	private BeanTableHelper<Case> caseTable(final Case c)
	{
		/*******************************************************
		 * Construct a table to present all properties of a case
		 *******************************************************/
		final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
				"cases" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				Case.class 	/* The class of the objects (rows) that will be displayed */
		);

		// Add columns to the new table
		table.addBeanColumn("Title", "title");
		table.addBeanColumn("open", "open");
		table.addBeanColumn("Case Description", "description");
		table.addBeanColumn("location", "address");
		table.addBeanColumn("time", "time");
		table.addBeanColumn("Creator", "creator");

		table.addObject(c);
		table.setVertical(true);
		
		return table;
	}
	
	private BeanTableHelper<CaseComment> notesTable(final List<CaseComment> notes)
	{
		/*******************************************************
		 * Construct a table to present all notes of a case
		 *******************************************************/
		final BeanTableHelper<CaseComment> table = new BeanTableHelper<CaseComment>(
				"notes" 		/* The table html id property */,
				"casesTable" /* The table html class property; The same layout as the case table*/,
				CaseComment.class 	/* The class of the objects (rows) that will be displayed */
		);

		// Add columns to the new table
		table.addBeanColumn("text", "text");

		table.addObjects(notes);
		
		return table;
	}
	
	private BeanTableHelper<Conviction> convictionTable(final List<Conviction> convictions, boolean logged_in, Case a_case)
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
		table.addBeanColumn("name", "personOfInterest");
		table.addBeanColumn("category", "category");
		if (!a_case.getOpen() && logged_in) {
			table.addBeanColumn("start date", "startDateBox");
			table.addBeanColumn("end date", "endDateBox");
		}
		else if (!a_case.getOpen()) {
			table.addBeanColumn("date", "date");
			table.addBeanColumn("end date", "endDate");
		}
		if (logged_in && a_case.getOpen()) {
			table.addBeanColumn("remove suspect", "deleteButton");
		}
		table.addObjects(convictions);
		
		return table;
	}
}