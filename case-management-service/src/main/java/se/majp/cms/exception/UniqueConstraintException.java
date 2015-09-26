package se.majp.cms.exception;

public class UniqueConstraintException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public UniqueConstraintException()
	{
		super();
	}

	public UniqueConstraintException(String message)
	{
		super(message);
	}

	public UniqueConstraintException(String message, Throwable throwable)
	{
		super(message, throwable);
	}
}
