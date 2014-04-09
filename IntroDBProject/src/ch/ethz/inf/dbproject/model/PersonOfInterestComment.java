package ch.ethz.inf.dbproject.model;

public class PersonOfInterestComment
{
	private final int id;
	private final int poi_id;
	private final String text;
	private PersonOfInterest personOfInterest;

	public PersonOfInterestComment(final int id, final int poi_id,
			final String text)
	{
		this.id = id;
		this.poi_id = poi_id;
		this.text = text;
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

	public PersonOfInterest getPersonOfInterest()
	{
		return personOfInterest;
	}

	public void setPersonOfInterest(PersonOfInterest personOfInterest)
	{
		this.personOfInterest = personOfInterest;
	}
}
