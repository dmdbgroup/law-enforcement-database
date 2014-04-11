package ch.ethz.inf.dbproject.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonOfInterest
{
	private final int id;
	private final String firstname;
	private final String surname;
	private final Date birthday;

	public PersonOfInterest(final int id, final String firstname, final String surname, final Date birthday)
	{
		this.id = id;
		this.firstname = firstname;
		this.surname = surname;
		this.birthday = birthday;
	}

	public PersonOfInterest(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.firstname = rs.getString("firstname");
		this.surname = rs.getString("surname");
		this.birthday = rs.getDate("birthdate");
	}

	public int getId()
	{
		return id;
	}

	public Date getBirthday()
	{
		return birthday;
	}
	
	public String getCheckBox()
	{
		return "<input type=\"radio\" name=\"poi\" value = \"" + id + "\">" + this + "</input>";
	}

	public String getFirstname()
	{
		return firstname;
	}

	public String getSurname()
	{
		return surname;
	}
	
	public String toString()
	{
		return firstname+" "+surname + ", " + birthday;
	}
}
