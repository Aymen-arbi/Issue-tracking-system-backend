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

import se.majp.cms.model.User;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Provider
public final class UserJsonMapper implements MessageBodyReader<User>, MessageBodyWriter<User>
{
	private Gson gson;

	public UserJsonMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(User.class);
	}

	@Override
	public long getSize(User t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(User user, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(user, User.class, writer);
		}
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(User.class);
	}

	@Override
	public User readFrom(Class<User> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException
	{
		User user = gson.fromJson(new InputStreamReader(entityStream), User.class);

		return user;
	}

	private static final class UserAdapter implements JsonSerializer<User>, JsonDeserializer<User>
	{
		@Override
		public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			final JsonObject jsonUser = json.getAsJsonObject();

			String email = jsonUser.get("email").getAsString();
			String password = jsonUser.get("password").getAsString();
			String firstName = jsonUser.get("firstName").getAsString();
			String lastName = jsonUser.get("lastName").getAsString();

			return new User(email, password, firstName, lastName);
		}

		@Override
		public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject jsonUser = new JsonObject();
			jsonUser.add("userId", new JsonPrimitive(user.getUserId()));
			jsonUser.add("email", new JsonPrimitive(user.getEmail()));
			jsonUser.add("password", new JsonPrimitive(user.getPassword()));
			jsonUser.add("firstName", new JsonPrimitive(user.getFirstName()));
			jsonUser.add("lastName", new JsonPrimitive(user.getLastName()));

			return jsonUser;
		}

	}

}
