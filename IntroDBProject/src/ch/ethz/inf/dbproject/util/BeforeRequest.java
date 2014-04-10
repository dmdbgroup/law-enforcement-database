package ch.ethz.inf.dbproject.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


import ch.ethz.inf.dbproject.model.Category;
import ch.ethz.inf.dbproject.model.DatastoreInterface;

public class BeforeRequest {
	
	private static DatastoreInterface dbInterface = new DatastoreInterface();
	public static void execute(HttpServletRequest request) {
		List<Category> cats = dbInterface.getAllCategories();
		String r = "";
		for (Category c : cats) {
			r = r + "<div class=\"menuDiv2\"><a href=\"Cases?category_id="+c.getId()+"\">"+c.getName()+"</a></div>\n";
		}
		request.getSession(true).setAttribute("catmenu", r);
	}

}
