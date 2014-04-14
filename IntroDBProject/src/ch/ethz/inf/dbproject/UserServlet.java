package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.BeforeRequest;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

@WebServlet(description = "Page that displays the user login / logout options.", urlPatterns = { "/User" })
public final class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	public final static String SESSION_USER_LOGGED_IN = "userLoggedIn";
	public final static String SESSION_USER_DETAILS = "userDetails";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		BeforeRequest.execute(request);
		final HttpSession session = request.getSession(true);
		final User loggedUser = UserManagement.getCurrentlyLoggedInUser(session);

		if (loggedUser == null) {
			// Not logged in!
			session.setAttribute(SESSION_USER_LOGGED_IN, false);
		} else {
			// Logged in
			session.setAttribute(SESSION_USER_LOGGED_IN, true);
		}

		// TODO display registration

		final String action = request.getParameter("action");
		
		if (action != null && action.trim().equals("login") 	&& loggedUser == null) {
			final String username = request.getParameter("username");
			// Note: It is really not safe to use HTML get method to send passwords.
			// However for this project, security is not a requirement.
			final String password = request.getParameter("password");
			// TODO
			// Ask the data store interface if it knows this user
			// Retrieve User
			// Store this user into the session
			try {
				if (dbInterface.userExists(username, password)) {
					User u = new User(username, password);
					session.setAttribute(UserManagement.SESSION_USER, u);
					
					final BeanTableHelper<User> userDetails = new BeanTableHelper<User>("userDetails", "userDetails", User.class);
					userDetails.addBeanColumn("Name", "name");
					userDetails.addObject(u);

					session.setAttribute(SESSION_USER_DETAILS, userDetails);
					session.setAttribute(SESSION_USER_LOGGED_IN, true);
				}
				else {
					session.setAttribute("message", "Wrong combination, please try again.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if (action != null && action.trim().equals("register") && loggedUser == null) {
			final String username = request.getParameter("username");
			// Note: It is really not safe to use HTML get method to send passwords.
			// However for this project, security is not a requirement.
			final String password = request.getParameter("password");
			

			// TODO
			// Ask the data store interface if it knows this user
			// Retrieve User
			// Store this user into the session
			try {
				if (username == null || password == null){
					session.setAttribute("message", "An error has occored. Please try again.");
					this.getServletContext().getRequestDispatcher("/Registration.jsp").forward(request, response);
					return;
				}
				else if ("".equals(username) || "".equals(password)) {
					session.setAttribute("message", "Please enter both a username and a password.");
					this.getServletContext().getRequestDispatcher("/Registration.jsp").forward(request, response);
					return;
				}
				else if (dbInterface.usernameExists(username)) {
					session.setAttribute("message", "Username already exists. Please choose another one.");
					this.getServletContext().getRequestDispatcher("/Registration.jsp").forward(request, response);
					return;
				}
				else {
					User u = new User(username, password);
					session.setAttribute(UserManagement.SESSION_USER, u);
					dbInterface.addUser(username, password);
					
					final BeanTableHelper<User> userDetails = new BeanTableHelper<User>("userDetails", "userDetails", User.class);
					userDetails.addBeanColumn("Name", "name");
					userDetails.addObject(u);

					session.setAttribute(SESSION_USER_LOGGED_IN, true);
					session.setAttribute(SESSION_USER_DETAILS, userDetails);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (action != null && action.trim().equals("showreg") && loggedUser == null){
			this.getServletContext().getRequestDispatcher("/Registration.jsp").forward(request, response);
			return;
		}
		else if (action != null && action.trim().equals("logout") && loggedUser != null) {
			session.setAttribute(SESSION_USER_LOGGED_IN, false);
			session.removeAttribute(UserManagement.SESSION_USER);
		}


		// Finally, proceed to the User.jsp page which will render the profile
		this.getServletContext().getRequestDispatcher("/User.jsp").forward(request, response);

	}

}
