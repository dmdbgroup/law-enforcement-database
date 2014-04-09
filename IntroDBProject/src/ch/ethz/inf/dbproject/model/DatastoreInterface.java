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

	private final static List<Case> staticCaseList = new ArrayList<Case>();
	static
	{
		staticCaseList.add(new Case(0, 0, true, "title 1", "Noise pollution..",
				new Time(123)));
		staticCaseList.add(new Case(1, 1, true, "title 2",
				"Highway overspeed...", new Time(123456)));
	}

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
		try
		{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("select * from allcases");

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

}
