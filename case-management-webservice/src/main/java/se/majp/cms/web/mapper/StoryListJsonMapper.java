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

import se.majp.cms.model.Story;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class StoryListJsonMapper implements MessageBodyWriter<ArrayList<Story>>
{
	private Gson gson;

	public StoryListJsonMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(ArrayList.class, new StoryListAdapter()).create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		if (type.isAssignableFrom(ArrayList.class) && genericType instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] actualTypeArgs = parameterizedType.getActualTypeArguments();

			if (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(Story.class))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public long getSize(ArrayList<Story> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(ArrayList<Story> stories, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(stories, ArrayList.class, writer);
		}
	}

	private static final class StoryListAdapter implements JsonSerializer<ArrayList<Story>>
	{
		@Override
		public JsonElement serialize(ArrayList<Story> stories, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonArray jsonStories = new JsonArray();

			stories.forEach(story -> {
				JsonObject jsonStory = new JsonObject();
				JsonArray jsonIssues = new JsonArray();

				story.getIssues().forEach(issue -> {
					JsonObject jsonIssue = new JsonObject();
					jsonIssue.add("title", new JsonPrimitive(issue.getTitle()));
					jsonIssue.add("description", new JsonPrimitive(issue.getDescription()));
					jsonIssues.add(jsonIssue);
				});

				jsonStory.add("storyId", new JsonPrimitive(story.getStoryId()));
				jsonStory.add("name", new JsonPrimitive(story.getName()));
				jsonStory.add("description", new JsonPrimitive(story.getDescription()));
				jsonStory.add("status", new JsonPrimitive(String.valueOf(story.getStatus())));
				jsonStory.add("priority", new JsonPrimitive(String.valueOf(story.getPriority())));
				jsonStory.add("issues", jsonIssues);

				jsonStories.add(jsonStory);
			});

			return jsonStories;
		}

	}

}
