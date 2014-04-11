package ch.ethz.inf.dbproject.model;

import java.sql.Connection;
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
				c = complementCase(c);
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
		return getCaseList("select * from allcases");
	}

	private final Case complementCase(final Case c) throws SQLException
	{
		Case result = c;
		final Statement stmt = this.sqlConnection.createStatement();
		final ResultSet rs = stmt.executeQuery("call get_address("
				+ c.getAddress_id() + ")");
		if (!rs.next())
		{
			System.out.println("Error: couldnt find address with id:"
					+ c.getAddress_id());
		} else
		{
			final Address a = new Address(rs);
			result.setAddress(a);
		}
		return result;
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
				c = complementCase(c);
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

	public final List<Case> getMostRecentCases()
	{
		return getCaseList("select * from newestcases");
	}

	public final List<Case> getOldestUnsolvedCases()
	{
		return getCaseList("select * from oldestcases");
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
			final ResultSet rs = stmt.executeQuery("select * from alltypes");
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
			final ResultSet rs = stmt.executeQuery("select * from user where name = \"" + username + "\" and password = \"" + password + "\";");
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
			final ResultSet rs = stmt.executeQuery("select * from user where name = \"" + username + "\";");
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
			final ResultSet rs = stmt.executeQuery("select * from allpoi");
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
			final ResultSet rs = stmt.executeQuery("SELECT p1.id, p1.name, p1.birthdate from poi p1 WHERE p1.id NOT IN (SELECT p2.id FROM poi AS p2 JOIN is_linked_to AS il ON p2.id = il.poi_id WHERE il.case_id = \"" + case_id + "\" GROUP BY p2.id)"); 
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
			ResultSet rs = stmt.executeQuery("SELECT name FROM type WHERE name = \""+name+"\"");
			if (!rs.next()) stmt.execute("INSERT INTO type (name) VALUES (\""+name+"\")");
			rs.close();
			rs = stmt.executeQuery("SELECT id FROM type WHERE name = \""+name+"\"");
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
			stmt.execute("INSERT INTO is_linked_to (poi_id, case_id, type_id, time) VALUES (\""+poi_id+"\", \""+case_id+"\", \""+type_id+"\", \"2013-02-23 00:00:00\")");
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
	
	public final void updateConvictionEndDate(int case_id, int poi_id, String date) {
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("UPDATE is_linked_to SET end_time = \""+date+"\" WHERE case_id = \""+case_id+"\" AND poi_id = \""+poi_id+"\"");
			stmt.close();
		}
		catch (final SQLException ex){
			ex.printStackTrace();
		}
	}
}
