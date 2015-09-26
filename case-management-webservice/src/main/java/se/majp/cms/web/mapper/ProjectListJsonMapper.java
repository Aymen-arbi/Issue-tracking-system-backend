package se.majp.cms.web.mapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;

import se.majp.cms.model.Project;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public final class ProjectListJsonMapper implements MessageBodyWriter<ArrayList<Project>>
{
	private Gson gson;

	public ProjectListJsonMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(ArrayList.class, new ProjectListAdapter()).create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		if (type.isAssignableFrom(ArrayList.class) && genericType instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] actualTypeArgs = parameterizedType.getActualTypeArguments();

			if (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(Project.class))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public long getSize(ArrayList<Project> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(ArrayList<Project> projects, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(projects, ArrayList.class, writer);
		}
	}

	private static final class ProjectListAdapter implements JsonSerializer<ArrayList<Project>>
	{
		@Override
		public JsonElement serialize(ArrayList<Project> projects, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonArray jsonProjects = new JsonArray();

			projects.forEach(project -> {
				JsonObject jsonProject = new JsonObject();
				JsonArray jsonBacklog = new JsonArray();
				JsonArray jsonTeam = new JsonArray();

				project.getBacklog().forEach(story -> {
					JsonObject jsonStory = new JsonObject();
					jsonStory.add("name", new JsonPrimitive(story.getName()));
					jsonStory.add("description", new JsonPrimitive(story.getDescription()));
					jsonStory.add("status", new JsonPrimitive(String.valueOf(story.getStatus())));
					jsonStory.add("priority", new JsonPrimitive(String.valueOf(story.getPriority())));
					jsonBacklog.add(jsonStory);
				});

				project.getTeam().getUsers().forEach((user, role) -> {
					JsonObject jsonUser = new JsonObject();
					jsonUser.add("email", new JsonPrimitive(user.getEmail()));
					jsonUser.add("role", new JsonPrimitive(String.valueOf(role)));
					jsonTeam.add(jsonUser);
				});

				jsonProject.add("projectId", new JsonPrimitive(project.getProjectId()));
				jsonProject.add("name", new JsonPrimitive(project.getName()));
				jsonProject.add("description", new JsonPrimitive(project.getDescription()));
				jsonProject.add("team", jsonTeam);
				jsonProject.add("backlog", jsonBacklog);

				jsonProjects.add(jsonProject);
			});

			return jsonProjects;
		}
	}

}
