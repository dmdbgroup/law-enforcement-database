package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Object that represents a category of project (i.e. Theft, Assault...)
 */
public final class Category
{

	private final int id;
	private final String name;

	public Category(final int id, final String name)
	{
		this.id = id;
		this.name = name;
	}

	public Category(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.name = rs.getString("name");
	}

	public final int getId()
	{
		return id;
	}

	public final String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name;
	}
}
