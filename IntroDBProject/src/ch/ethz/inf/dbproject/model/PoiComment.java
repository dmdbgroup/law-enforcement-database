package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PoiComment {
	private final int id;
	private final int poi_id;
	private final String text;
	private PersonOfInterest poiObject;

	public PoiComment(final int id, final int poi_id, final String text)
	{
		this.id = id;
		this.poi_id = poi_id;
		this.text = text;
	}

	public PoiComment(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.poi_id = rs.getInt("poi_id");
		this.text = rs.getString("text");
	}

	public int getId()
	{
		return id;
	}

	public int getPoi_id()
	{
		return poi_id;
	}

	public String getText()
	{
		return text;
	}

	public PersonOfInterest getPoi()
	{
		return poiObject;
	}

	public void setPoi(PersonOfInterest poiObject)
	{
		this.poiObject = poiObject;
	}
}
