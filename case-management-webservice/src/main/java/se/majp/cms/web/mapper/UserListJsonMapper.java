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

import se.majp.cms.model.User;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public final class UserListJsonMapper implements MessageBodyWriter<ArrayList<User>>
{
	private Gson gson;

	public UserListJsonMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(ArrayList.class, new UserListAdapter()).create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		if (type.isAssignableFrom(ArrayList.class) && genericType instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] actualTypeArgs = parameterizedType.getActualTypeArguments();

			if (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(User.class))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public long getSize(ArrayList<User> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(ArrayList<User> users, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(users, ArrayList.class, writer);
		}
	}

	private static final class UserListAdapter implements JsonSerializer<ArrayList<User>>
	{
		@Override
		public JsonElement serialize(ArrayList<User> users, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonArray jsonUsers = new JsonArray();
			users.forEach(user -> {
				JsonObject jsonUser = new JsonObject();
				JsonArray jsonStories = new JsonArray();
				user.getStories().forEach(story -> {
					JsonObject jsonStory = new JsonObject();
					jsonStory.add("name", new JsonPrimitive(story.getName()));
					jsonStory.add("description", new JsonPrimitive(story.getDescription()));
					jsonStory.add("status", new JsonPrimitive(String.valueOf(story.getStatus())));
					jsonStory.add("priority", new JsonPrimitive(String.valueOf(story.getPriority())));
					jsonStories.add(jsonStory);
				});
				jsonUser.add("userId", new JsonPrimitive(user.getUserId()));
				jsonUser.add("email", new JsonPrimitive(user.getEmail()));
				jsonUser.add("password", new JsonPrimitive(user.getPassword()));
				jsonUser.add("firstName", new JsonPrimitive(user.getFirstName()));
				jsonUser.add("lastName", new JsonPrimitive(user.getLastName()));
				jsonUsers.add(jsonUser);
			});

			return jsonUsers;
		}
	}

}
