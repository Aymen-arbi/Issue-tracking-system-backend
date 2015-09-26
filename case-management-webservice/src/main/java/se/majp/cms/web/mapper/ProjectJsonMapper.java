package se.majp.cms.web.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;

import se.majp.cms.model.Project;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Provider
public class ProjectJsonMapper implements MessageBodyReader<Project>, MessageBodyWriter<Project>
{
	private Gson gson;

	public ProjectJsonMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(Project.class, new ProjectAdapter()).create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(Project.class);
	}

	@Override
	public long getSize(Project t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(Project project, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(project, Project.class, writer);
		}
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(Project.class);
	}

	@Override
	public Project readFrom(Class<Project> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException
	{
		Project project = gson.fromJson(new InputStreamReader(entityStream), Project.class);
		return project;
	}

	private static final class ProjectAdapter implements JsonSerializer<Project>, JsonDeserializer<Project>
	{
		@Override
		public Project deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			final JsonObject jsonProject = json.getAsJsonObject();
			String name = jsonProject.get("name").getAsString();
			String description = jsonProject.get("description").getAsString();

			return new Project(name, description);
		}

		@Override
		public JsonElement serialize(Project project, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject jsonProject = new JsonObject();
			jsonProject.add("projectId", new JsonPrimitive(project.getProjectId()));
			jsonProject.add("name", new JsonPrimitive(project.getName()));
			jsonProject.add("description", new JsonPrimitive(project.getDescription()));
			return jsonProject;
		}
	}
}
