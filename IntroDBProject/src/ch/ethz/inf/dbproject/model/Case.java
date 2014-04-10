package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public final class Case
{

	private final int id;
	private final int address_id;
	private final String creator;
	private final boolean open;
	private final String title;
	private final String description;
	private final Time time;
	private Address address;

	/**
	 * Construct a new case.
	 */
	public Case(final int id, final int address_id, final String creator, final boolean open,
			final String title, final String description, final Time time)
	{
		this.id = id;
		this.address_id = address_id;
		this.creator = creator;
		this.open = open;
		this.title = title;
		this.description = description;
		this.time = time;
	}

	public Case(final ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.address_id = rs.getInt("address_id");
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

	public int getAddress_id()
	{
		return address_id;
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

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}
	
	public String getStreetWithNumber()
	{
		if(this.address == null)
		{
			return "";
		}
		else
		{
			return this.address.getStreet()+" "+this.address.getStreetNo();
		}
	}

	public String getCreator()
	{
		return creator;
	}
}