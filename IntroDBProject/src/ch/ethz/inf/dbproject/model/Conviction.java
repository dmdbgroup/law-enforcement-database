package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Object that represents a conviction.
 */
public class Conviction
{

	private final Date date;
	private final Date endDate;
	private final int type_id;
	private final int poi_id;
	private final int case_id;
	private Category category;
	private PersonOfInterest personOfInterest;
	private Case caseObject;

	public Conviction(final Date date, final Date endDate, final int type_id,
			final int poi_id, final int case_id)
	{
		this.date = date;
		this.endDate = endDate;
		this.type_id = type_id;
		this.poi_id = poi_id;
		this.case_id = case_id;
	}

	public Conviction(ResultSet rs) throws SQLException
	{
		this.date = rs.getDate("time");
		this.endDate = rs.getDate("end_time");
		this.type_id = rs.getInt("type_id");
		this.poi_id = rs.getInt("poi_id");
		this.case_id = rs.getInt("case_id");
	}

	public int getCase_id()
	{
		return case_id;
	}

	public int getType_id()
	{
		return type_id;
	}

	public int getPoi_id()
	{
		return poi_id;
	}

	public Date getDate()
	{
		return date;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public PersonOfInterest getPersonOfInterest()
	{
		return personOfInterest;
	}

	public void setPersonOfInterest(PersonOfInterest personOfInterest)
	{
		this.personOfInterest = personOfInterest;
	}

	public Case getCase()
	{
		return caseObject;
	}

	public void setCase(Case caseObject)
	{
		this.caseObject = caseObject;
	}
}
