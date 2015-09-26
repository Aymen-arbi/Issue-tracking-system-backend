package se.majp.cms.exception;

public class PermissionDeniedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public PermissionDeniedException()
	{
		super();
	}

	public PermissionDeniedException(String message)
	{
		super(message);
	}

	public PermissionDeniedException(String message, Throwable throwable)
	{
		super(message, throwable);
	}
}
