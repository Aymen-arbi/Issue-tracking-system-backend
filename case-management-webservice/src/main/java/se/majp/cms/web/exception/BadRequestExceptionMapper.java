package se.majp.cms.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.majp.cms.exception.BadRequestException;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException>
{
	@Override
	public Response toResponse(BadRequestException exception)
	{
		return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).build();
	}
}
