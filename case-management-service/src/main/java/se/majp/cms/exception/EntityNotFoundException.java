package se.majp.cms.exception;

public class EntityNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String message, Throwable throwable)
	{
		super(message, throwable);
	}

	public EntityNotFoundException(String message)
	{
		super(message);
	}

	public EntityNotFoundException()
	{
		super();
	}
}
