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

import se.majp.cms.model.Priority;
import se.majp.cms.model.Status;
import se.majp.cms.model.Story;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Provider
public class StoryJsonMapper implements MessageBodyWriter<Story>, MessageBodyReader<Story>
{
	private Gson gson;

	public StoryJsonMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(Story.class, new StoryAdapter()).create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(Story.class);
	}

	@Override
	public long getSize(Story t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(Story.class);
	}

	@Override
	public Story readFrom(Class<Story> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException
	{
		Story story = gson.fromJson(new InputStreamReader(entityStream), Story.class);

		return story;
	}

	@Override
	public void writeTo(Story story, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(story, Story.class, writer);
		}
	}

	private static final class StoryAdapter implements JsonSerializer<Story>, JsonDeserializer<Story>

	{
		@Override
		public JsonElement serialize(Story story, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject jsonStory = new JsonObject();
			jsonStory.add("storyId", new JsonPrimitive(story.getStoryId()));
			jsonStory.add("name", new JsonPrimitive(story.getName()));
			jsonStory.add("description", new JsonPrimitive(story.getDescription()));
			jsonStory.add("status", new JsonPrimitive(story.getStatus().toString()));
			jsonStory.add("priority", new JsonPrimitive(story.getPriority().toString()));

			return jsonStory;
		}

		@Override
		public Story deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject jsonStory = json.getAsJsonObject();

			String name = jsonStory.get("name").getAsString();
			String description = jsonStory.get("description").getAsString();
			String statusString = jsonStory.get("status").getAsString();
			String priorityString = jsonStory.get("priority").getAsString();
			Status status = Status.valueOf(statusString);
			Priority priority = Priority.valueOf(priorityString);

			return new Story(name, description, status, priority);
		}
	}
}
