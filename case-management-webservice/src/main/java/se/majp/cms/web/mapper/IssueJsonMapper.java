package se.majp.cms.web.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import se.majp.cms.model.Issue;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class IssueJsonMapper implements MessageBodyReader<Issue>
{
	private Gson gson;

	public IssueJsonMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(Issue.class, new IssueAdapter()).create();
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(Issue.class);
	}

	@Override
	public Issue readFrom(Class<Issue> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException
	{
		Issue issue = gson.fromJson(new InputStreamReader(entityStream), Issue.class);

		return issue;
	}

	private static final class IssueAdapter implements JsonDeserializer<Issue>
	{
		@Override
		public Issue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject jsonIssue = json.getAsJsonObject();

			String title = jsonIssue.get("title").getAsString();
			String description = jsonIssue.get("description").getAsString();

			return new Issue(title, description);
		}
	}

}
