package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaseComment
{
	private final int id;
	private final int case_id;
	private final String text;
	private Case caseObject;

	public CaseComment(final int id, final int case_id, final String text)
	{
		this.id = id;
		this.case_id = case_id;
		this.text = text;
	}

	public CaseComment(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.case_id = rs.getInt("case_id");
		this.text = rs.getString("text");
	}

	public int getId()
	{
		return id;
	}

	public int getCase_id()
	{
		return case_id;
	}

	public String getText()
	{
		return text;
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
