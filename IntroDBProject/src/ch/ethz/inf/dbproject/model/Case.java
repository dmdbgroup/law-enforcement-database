package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public final class Case
{

	private final int id;
	// The address has the form: street nr, plz ort
	private final String address;
	private final String creator;
	private final boolean open;
	private final String title;
	private final String description;
	private final Time time;

	/**
	 * Construct a new case.
	 */
	public Case(final int id, final String address, final String creator, final boolean open,
			final String title, final String description, final Time time)
	{
		this.id = id;
		this.address = address;
		this.creator = creator;
		this.open = open;
		this.title = title;
		this.description = description;
		this.time = time;
	}

	public Case(final ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.address = rs.getString("address");
		this.creator = rs.getString("creator");
		this.open = rs.getBoolean("open");
		this.title = rs.getString("title");
		this.description = rs.getString("description");
		this.time = rs.getTime("time");
	}

	public int getId()
	{
		return id;
	}

	public String getAddress()
	{
		return address;
	}

	public boolean getOpen()
	{
		return open;
	}

	public String getTitle()
	{
		return title;
	}

	public String getDescription()
	{
		return description;
	}

	public Time getTime()
	{
		return time;
	}
	
	public String getStreetWithNumber()
	{
		return address;
	}

	public String getCreator()
	{
		return creator;
	}
}