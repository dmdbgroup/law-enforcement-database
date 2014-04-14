package ch.ethz.inf.dbproject.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

import ch.ethz.inf.dbproject.database.MySQLConnection;

/**
 * This class should be the interface between the web application and the
 * database. Keeping all the data-access methods here will be very helpful for
 * part 2 of the project.
 */
public final class DatastoreInterface
{

	private Connection sqlConnection;

	public DatastoreInterface()
	{
		this.sqlConnection = MySQLConnection.getInstance().getConnection();
	}

	public final Case getCaseById(final int id)
	{
		try
		{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call get_case(" + id + ")");

			Case c = null;
			if (rs.next())
			{
				c = new Case(rs);
			}

			rs.close();
			stmt.close();

			return c;

		} catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public final List<Case> getAllCases()
	{
		return getCaseList("call get_all_cases()");
	}

	public final List<CaseComment> getNotesForCaseId(int id)
	{
		try
		{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call get_notes_for_case("
					+ id + ")");

			final List<CaseComment> notes = new ArrayList<CaseComment>();
			while (rs.next())
			{
				notes.add(new CaseComment(rs));
			}

			rs.close();
			stmt.close();

			return notes;

		} catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	private final Conviction complementConviction(final Conviction conviction)
	{
		try
		{
			Conviction result = conviction;

			final Statement case_stmt = this.sqlConnection.createStatement();
			final ResultSet case_rs = case_stmt.executeQuery("call get_case("
					+ conviction.getCase_id() + ")");
			final Statement poi_stmt = this.sqlConnection.createStatement();
			final ResultSet poi_rs = poi_stmt.executeQuery("call get_poi("
					+ conviction.getPoi_id() + ")");
			final Statement type_stmt = this.sqlConnection.createStatement();
			final ResultSet type_rs = type_stmt.executeQuery("call get_type("
					+ conviction.getType_id() + ")");

			boolean ok = true;
			if (!case_rs.next())
			{
				ok = false;
				System.out.println("Error: couldnt find Case with case_id:"
						+ conviction.getCase_id());
			}
			if (!poi_rs.next())
			{
				ok = false;
				System.out.println("Error: couldnt find poi with poi_id:"
						+ conviction.getPoi_id());
			}
			if (!type_rs.next())
			{
				ok = false;
				System.out.println("Error: couldnt find type with type_id:"
						+ conviction.getType_id());
			}

			if (ok)
			{
				final Case c = new Case(case_rs);
				final PersonOfInterest p = new PersonOfInterest(poi_rs);
				final Category cat = new Category(type_rs);
				result.setCase(c);
				result.setPersonOfInterest(p);
				result.setCategory(cat);
			}
			return result;
		} catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public final List<Conviction> getConvictionsForCaseId(int id)
	{
		try
		{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call get_links_for_case("
					+ id + ")");
			final List<Conviction> convictions = new ArrayList<Conviction>();
			while (rs.next())
			{
				Conviction conviction = new Conviction(rs);
				conviction = complementConviction(conviction);
				convictions.add(conviction);
			}

			rs.close();
			stmt.close();

			return convictions;

		} catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public final List<Conviction> getConvictionsForPoiId(int id)
	{
		try
		{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call get_links_for_poi("
					+ id + ")");
			final List<Conviction> convictions = new ArrayList<Conviction>();
			while (rs.next())
			{
				Conviction conviction = new Conviction(rs);
				conviction = complementConviction(conviction);
				convictions.add(conviction);
			}

			rs.close();
			stmt.close();

			return convictions;

		} catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public void toggleCaseOpen(int id)
	{
		try
		{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call toggle_case_open("
					+ id + ")");

			rs.close();
			stmt.close();

		} catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	public final List<Case> getProjectsByCategory(int category)
	{
		return getCaseList("call search_cases_by_type_of_conviction(\""
				+ category + "\")");
	}

	public final List<Case> getCasesByStatus(Boolean open)
	{
		return getCaseList("call search_cases_by_status(" + open + ")");
	}

	private final List<Case> getCaseList(String query)
	{
		try
		{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery(query);

			final List<Case> cases = new ArrayList<Case>();
			while (rs.next())
			{
				Case c = new Case(rs);
				cases.add(c);
			}

			rs.close();
			stmt.close();

			return cases;

		} catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public final List<Case> getMostRecentOpenCases()
	{
		return getCaseList("call get_most_recent_open_cases()");
	}

	public final List<Case> getOldestUnsolvedCases()
	{
		return getCaseList("call get_oldest_unsolved_cases()");
	}

	public final List<Case> searchCasesByDescription(String parameter)
	{
		return getCaseList("call search_cases_by_similar_description(\"" + parameter
				+ "\")");
	}

	public final List<Category> getAllCategories()
	{
		try
		{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call get_all_categories()");
			final List<Category> cats = new ArrayList<Category>();
			while (rs.next())
			{
				Category c = new Category(rs);
				cats.add(c);
			}
			rs.close();
			stmt.close();
			return cats;
		} catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public final List<Case> searchCasesBySimilarCategory(String parameter)
	{
		return getCaseList("call search_cases_by_similar_type_of_conviction(\""+parameter+"\")");
	}

	public final List<Case> searchCasesBySimilarTitle(String parameter)
	{
		return getCaseList("call search_cases_by_similar_title(\""+parameter+"\")");
	}
	
	public final boolean userExists(String username, String password) throws SQLException
	{
		boolean result = false;
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call user_exists( \"" + username + "\" , \"" + password + "\")");
			if (!rs.next())
			{
				result = false;
			} else
			{
				result = true;
			}
		}	
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
		return result;
	}
	
	public final boolean usernameExists(String username) throws SQLException
	{
		boolean result = false;
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call username_exists( \"" + username + "\")");
			if (!rs.next())
			{
				result = false;
			} else
			{
				result = true;
			}
		}	
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
		return result;
	}
	
	public final void addUser(String username, String password) throws SQLException
	{
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("insert into user (name, password) values (\"" + username + "\", \"" + password + "\");");
			
		}	
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public final List<PersonOfInterest> getAllPoi(){
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call get_all_poi()");
			final List<PersonOfInterest> pois = new ArrayList<PersonOfInterest>();
			while (rs.next()){
				PersonOfInterest p = new PersonOfInterest(rs);
				pois.add(p);
			}
			rs.close();
			stmt.close();
			return pois;		
		}
		catch (final SQLException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public final List<PersonOfInterest> getAllUnsuspectedPois(int case_id){
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call get_all_unsuspected_pois(" + case_id + ")"); 
			final List<PersonOfInterest> pois = new ArrayList<PersonOfInterest>();
			while (rs.next()){
				PersonOfInterest p = new PersonOfInterest(rs);
				pois.add(p);
			}
			rs.close();
			stmt.close();
			return pois;		
		}
		catch (final SQLException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public final int addCategory(String name){
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			ResultSet rs = stmt.executeQuery("call get_id_from_type(\""+name+"\")");
			if (!rs.next()) {
				stmt.execute("INSERT INTO type (name) VALUES (\""+name+"\")");
				rs = stmt.executeQuery("call get_id_from_type(\""+name+"\")");
			}
			rs.next();
			int r = rs.getInt("id");
			rs.close();
			stmt.close();
			return r;		
		}
		catch (final SQLException ex){
			ex.printStackTrace();
			return 0;
		}
	}
	
	public final void addLink(int poi_id, int case_id, int type_id) {
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("INSERT INTO is_linked_to (poi_id, case_id, type_id) VALUES (\""+poi_id+"\", \""+case_id+"\", \""+type_id+"\")");
			stmt.close();	
		}
		catch (final SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public final void addCase(String title, String description, Time time, String address, String creator, Boolean open)
	{
		System.out.println("addCase("+title+" "+description+" "+time+" "+time+" "+address+" "+creator+" "+open+")");
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("call add_case(" +
					"\""+title+"\"," +
					"\""+description+"\"," +
					"\""+time+"\"," +
					"\""+address+"\"," +
					"\""+creator+"\"," +
					""+open+")");
			stmt.close();	
		}
		catch (final SQLException ex){
			ex.printStackTrace();
		}
	}

	public final void removeLink(int case_id, int poi_id) {
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("DELETE FROM is_linked_to WHERE case_id = \""+case_id+"\" AND poi_id = \""+poi_id+"\"");
			stmt.close();	
		}
		catch (final SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public final void updateConvictionDates(int case_id, int poi_id, String start_date, String end_date) {
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("UPDATE is_linked_to SET end_time = \""+end_date+"\", time = \""+start_date+"\" WHERE case_id = \""+case_id+"\" AND poi_id = \""+poi_id+"\"");
			stmt.close();
		}
		catch (final SQLException ex){
			ex.printStackTrace();
		}
	}
	public void addCaseComment(Integer case_id, String text)
	{
		System.out.println("addCaseComment("+case_id+",\""+text+")");
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("call add_case_note(" +""+case_id+"," +"\""+text+"\")");
			stmt.close();	
		}
		catch (final SQLException ex){
			ex.printStackTrace();
		}	
	}
	
	public void addPoiComment(Integer poi_id, String text)
	{
		System.out.println("addPoiComment("+poi_id+",\""+text+")");
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("call add_poi_note(\""+poi_id+"\",\""+text+"\")");
			stmt.close();	
		}
		catch (final SQLException ex){
			ex.printStackTrace();
		}	
	}

	public void deleteCase(Integer id)
	{
		System.out.println("deleteCase("+id+")");
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("call delete_case("+id+")");
			stmt.close();	
		}
		catch (final SQLException ex){
			ex.printStackTrace();
		}	
	}
	
	//Added
	public final List<Case> getCaseByPoiId(final int id){
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call get_links_for_poi (" + id+  ")");
			final List<Conviction> con = new ArrayList<Conviction>();
			while (rs.next()){
				Conviction c = new Conviction(rs);
				con.add(c);
			}
			final List<Case> c = new ArrayList<Case>();
			for(Conviction item: con){
				Case c2 = getCaseById(item.getCase_id());
				c.add(c2);
			}
			rs.close();
			stmt.close();
			return c;
		}
		catch (final SQLException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public final List<PoiComment> getNotesForPoiId(int id)
	{
		try
		{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call get_notes_for_poi("
					+ id + ")");

			final List<PoiComment> notes = new ArrayList<PoiComment>();
			while (rs.next())
			{
				notes.add(new PoiComment(rs));
			}

			rs.close();
			stmt.close();

			return notes;

		} catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public void deletePoi(Integer poi_id)
	{
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("call delete_poi("+poi_id+")");
			stmt.close();	
		}
		catch (final SQLException ex){
			ex.printStackTrace();
		}
	}

	public void addPoi(String firstname, String surname, Date birthday)
	{
		System.out.println("addPoi("+firstname+","+surname+","+birthday+")");
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("call add_poi(\""+firstname+"\",\""+surname+"\",\""+birthday+"\")");
			stmt.close();	
		}
		catch (final SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public final PersonOfInterest getPoiById(final int id)
	{
		try
		{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("call get_poi(" + id + ")");

			PersonOfInterest c = null;
			if (rs.next())
			{
				c = new PersonOfInterest(rs);
			}

			rs.close();
			stmt.close();

			return c;

		} catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	

}
