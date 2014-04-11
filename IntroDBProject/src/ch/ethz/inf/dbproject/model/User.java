package ch.ethz.inf.dbproject.model;

/**
 * Object that represents a registered in user.
 */
public final class User
{

	private final String password;
	private final String name;

	public User(final String name, final String password)
	{
		this.password = password;
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public String getName()
	{
		return name;
	}
}
