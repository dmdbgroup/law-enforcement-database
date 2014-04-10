package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Category;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.PersonOfInterest;
import ch.ethz.inf.dbproject.util.BeforeRequest;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Adding Links Page
 */
@WebServlet(description = "Displays a specific case.", urlPatterns = { "/Link" })
public final class LinkServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LinkServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		
		BeforeRequest.execute(request);
		final HttpSession session = request.getSession(true);
		
		session.removeAttribute("message");
		
		String caseIdString = request.getParameter("case_id");
		if (caseIdString == null) {
			this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
			return;
		}
		
		String action = request.getParameter("action");
		System.out.println(action);
		
		int case_id = Integer.parseInt(caseIdString.trim());
		session.setAttribute("case_id", case_id);
		
		if (("addlink").equals(action)) {
			String catString = (String) request.getParameter("category");
			String poiString = (String) request.getParameter("poi");
			if (catString == null || poiString == null) {
				session.setAttribute("message", "please set both a person of interest and a type of crime");
				this.getServletContext().getRequestDispatcher("/Link.jsp").forward(request, response);
			}
			else {
				int cat_id;
				if(request.getParameter("category").equals("insertnewcategory")) {
					cat_id = dbInterface.addCategory(request.getParameter("newcategory").toString().trim());
				}
				else {
					cat_id = Integer.parseInt((request.getParameter("category").toString().trim()));
				}
				int poi_id = Integer.parseInt(poiString.trim());
				dbInterface.addLink(poi_id, case_id, cat_id);
				session.setAttribute("message", "person was successfully linked to this case");
				request.setAttribute("id", case_id);
				this.getServletContext().getRequestDispatcher("/Case").forward(request, response);
			}
		} else {
			
			try {

				final List<PersonOfInterest> pois = dbInterface.getAllUnsuspectedPois(case_id);
				final List<Category> categories = dbInterface.getAllCategories();

				String pTable = "";
				for (PersonOfInterest pp : pois) {
					pTable = pTable + pp.getCheckBox() + "<br />";
				}
				
				String cTable = "";
				for (Category cc : categories) {
					cTable = cTable + cc.getRadioBox() + "<br />";
				}
				session.setAttribute("poisTable", pTable);	
				session.setAttribute("categoriesTable", cTable);
			
			
			} catch (final Exception ex) {
				ex.printStackTrace();
				this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
				return;
			}

			this.getServletContext().getRequestDispatcher("/Link.jsp").forward(request, response);
		}
	}
	
	
	private BeanTableHelper<PersonOfInterest> poisTable(final List<PersonOfInterest> pois)
	{
		/*******************************************************
		 * Construct a table to present all notes of a case
		 *******************************************************/
		final BeanTableHelper<PersonOfInterest> table = new BeanTableHelper<PersonOfInterest>(
				"selectPois" 		/* The table html id property */,
				"selectPoisTable" /* The table html class property; The same layout as the case table*/,
				PersonOfInterest.class 	/* The class of the objects (rows) that will be displayed */
		);

		// Add columns to the new table
		table.addBeanColumn("name and birthdate", "checkBox");

		table.addObjects(pois);
		
		return table;
	}
	
	private BeanTableHelper<Category> categoryTable(final List<Category> categories)
	{
		/*******************************************************
		 * Construct a table to present all notes of a case
		 *******************************************************/
		final BeanTableHelper<Category> table = new BeanTableHelper<Category>(
				"selectCat" 		/* The table html id property */,
				"selectCatTable" /* The table html class property; The same layout as the case table*/,
				Category.class 	/* The class of the objects (rows) that will be displayed */
		);

		// Add columns to the new table
		table.addBeanColumn("name", "radioBox");

		table.addObjects(categories);
		
		return table;
	}
}