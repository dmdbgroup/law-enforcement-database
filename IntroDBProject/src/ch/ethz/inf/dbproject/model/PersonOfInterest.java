package ch.ethz.inf.dbproject.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonOfInterest
{
	private final int id;
	private final String name;
	private final Date birthday;

	public PersonOfInterest(final int id, final String name, final Date birthday)
	{
		this.id = id;
		this.name = name;
		this.birthday = birthday;
	}

	public PersonOfInterest(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.name = rs.getString("name");
		this.birthday = rs.getDate("birthdate");
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public Date getBirthday()
	{
		return birthday;
	}
	
	public String toString()
	{
		return name;
	}
}
