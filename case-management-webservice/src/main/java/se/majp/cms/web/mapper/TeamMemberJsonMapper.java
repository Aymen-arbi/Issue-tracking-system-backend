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

import se.majp.cms.model.Role;
import se.majp.cms.model.TeamMember;
import se.majp.cms.model.User;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class TeamMemberJsonMapper implements MessageBodyReader<TeamMember>
{
	private Gson gson;

	public TeamMemberJsonMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(TeamMember.class, new TeamMemberAdapter()).create();
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{

		return type.isAssignableFrom(TeamMember.class);
	}

	@Override
	public TeamMember readFrom(Class<TeamMember> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException
	{
		TeamMember teamMember = gson.fromJson(new InputStreamReader(entityStream), TeamMember.class);
		return teamMember;
	}

	private static final class TeamMemberAdapter implements JsonDeserializer<TeamMember>
	{
		@Override
		public TeamMember deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			JsonObject jsonTeamMember = json.getAsJsonObject();
			JsonObject jsonUser = jsonTeamMember.get("user").getAsJsonObject();

			String userId = jsonUser.get("userId").getAsString();
			String email = jsonUser.get("email").getAsString();
			String password = jsonUser.get("password").getAsString();
			String firstName = jsonUser.get("firstName").getAsString();
			String lastName = jsonUser.get("lastName").getAsString();
			String roleString = jsonTeamMember.get("role").getAsString();
			Role role = Role.valueOf(roleString);

			return new TeamMember(new User(userId, email, password, firstName, lastName), role);
		}
	}

}
