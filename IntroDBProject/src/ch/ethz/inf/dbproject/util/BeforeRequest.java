package ch.ethz.inf.dbproject.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import ch.ethz.inf.dbproject.model.Category;
import ch.ethz.inf.dbproject.model.DatastoreInterface;

public class BeforeRequest {
	
	private static DatastoreInterface dbInterface = new DatastoreInterface();
	public static void execute(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		if (session.getAttribute("message") != null) {
			session.removeAttribute("message");
		}
		List<Category> cats = dbInterface.getAllCategories();
		String r = "";
		for (Category c : cats) {
			r = r + "<div class=\"menuDiv2\"><a href=\"Cases?category_id="+c.getId()+"\">"+c.getName()+"</a></div>\n";
		}
		if (r.equals("")) r = "<div class=\"menuDiv2\">none</div>";
		session.setAttribute("catmenu", r);
	}

}
